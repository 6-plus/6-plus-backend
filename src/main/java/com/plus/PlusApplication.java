package com.plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableJpaAuditing
@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@ConfigurationPropertiesScan
public class PlusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlusApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner demo(DrawRepository drawRepository, UserRepository userRepository, UserDrawRepository userDrawRepository, RabbitMqService rabbitMqService, RabbitTemplate rabbitTemplate) {
	// 	return args -> {
	//
	//
	// 		int userCount = 30;
	//
	// 		Draw draw = Draw.builder().totalWinner(10).build();
	// 		draw = drawRepository.save(draw);
	// 		Long drawId = draw.getId();
	//
	// 		List<UserDrawSaveReqDto> userDrawSaveReqDtoList = new ArrayList<>();
	// 		for(int i = 0; i < userCount; i++){
	// 			User user = User.builder().nickname("user" + i).build();
	// 			user = userRepository.save(user);
	// 			userDrawSaveReqDtoList.add(UserDrawSaveReqDto.builder().userId(user.getId()).drawId(drawId).build());
	// 		}
	// 		ExecutorService executorsService = Executors.newFixedThreadPool(userCount);
	//
	// 		Thread.sleep(1000);
	// 		for (UserDrawSaveReqDto userDrawSaveReqDto : userDrawSaveReqDtoList) {
	// 			executorsService.submit(() -> rabbitMqService.saveUserDraw(userDrawSaveReqDto));
	// 			Thread.sleep(1);
	// 		}
	// 	};
	// }

}
