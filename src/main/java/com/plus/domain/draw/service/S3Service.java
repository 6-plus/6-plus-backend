package com.plus.domain.draw.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.plus.domain.common.exception.ExpectedException;
import com.plus.domain.common.exception.enums.ExceptionCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// MultipartFile을 전달받아 S3에 업로드
	public String upload(MultipartFile multipartFile, String dirName) throws IOException {
		File uploadFile = convert(multipartFile)
			.orElseThrow(() -> new ExpectedException(ExceptionCode.FILE_NOT_FOUND));
		return upload(uploadFile, dirName);
	}

	private String upload(File uploadFile, String dirName) {
		String fileName = dirName + "/" + uploadFile.getName();
		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return uploadImageUrl;
	}

	private String putS3(File uploadFile, String fileName) {
		amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
		return amazonS3.getUrl(bucket, fileName).toString();
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("파일 삭제 완료: {}", targetFile.getName());
		} else {
			log.warn("파일 삭제 실패: {}", targetFile.getName());
		}
	}

	public Optional<File> convert(MultipartFile multipartFile) throws IOException {
		File convertFile = new File(multipartFile.getOriginalFilename());
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(multipartFile.getBytes());
			}
			return Optional.of(convertFile);
		}
		return Optional.empty();
	}

	// 이미지 삭제 메서드
	public void delete(String imageUrl) {
		// URL에서 파일 이름 추출
		String fileName = extractFileNameFromUrl(imageUrl);
		try {
			amazonS3.deleteObject(bucket, fileName);
			log.info("S3에서 파일 삭제 완료: {}", fileName);
		} catch (Exception e) {
			log.error("S3에서 파일 삭제 실패: {}", fileName, e);
			throw new ExpectedException(ExceptionCode.FILE_DELETE_FAILED);
		}
	}

	// URL에서 S3 파일 경로 추출
	private String extractFileNameFromUrl(String imageUrl) {
		String bucketUrl = amazonS3.getUrl(bucket, "").toString();

		if (!imageUrl.startsWith(bucketUrl)) {
			log.error("유효하지 않은 파일 URL: {}", imageUrl);
			throw new ExpectedException(ExceptionCode.INVALID_FILE_URL);
		}

		return imageUrl.substring(bucketUrl.length());
	}
}