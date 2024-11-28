package com.plus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.plus.domain.draw.entity.Draw;
import com.plus.domain.draw.repository.DrawRepository;
import com.plus.domain.rabbitMq.RabbitMqService;
import com.plus.domain.rabbitMq.UserDrawSaveReqDto;
import com.plus.domain.user.entity.User;
import com.plus.domain.user.repository.UserRepository;

@EnableJpaAuditing
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@ConfigurationPropertiesScan
public class PlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlusApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(DrawRepository drawRepository, UserRepository userRepository,
		RabbitMqService rabbitMqService) {
		return args -> {

			int userCount = 100, totalWinner = 1;

			Draw draw = Draw.builder().totalWinner(totalWinner).build();
			draw = drawRepository.save(draw);
			Long drawId = draw.getId();

			List<UserDrawSaveReqDto> userDrawSaveReqDtoList = new ArrayList<>();
			for (int i = 0; i < userCount; i++) {
				User user = User.builder().nickname("user" + i).build();
				user = userRepository.save(user);
				userDrawSaveReqDtoList.add(UserDrawSaveReqDto.builder().userId(user.getId()).drawId(drawId).build());
			}
			ExecutorService executorsService = Executors.newFixedThreadPool(50);

			Thread.sleep(1000);
			for (UserDrawSaveReqDto userDrawSaveReqDto : userDrawSaveReqDtoList) {
				executorsService.submit(() -> rabbitMqService.saveUserDraw(userDrawSaveReqDto));
				Thread.sleep(1);
			}
		};
	}

}
