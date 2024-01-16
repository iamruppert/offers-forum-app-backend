package com.lukasz.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}
/*
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			OfferServiceImpl offerService
	){
		return args -> {
			var admin = RegisterRequest.builder()
					.name("John")
					.surname("Doe")
					.pesel("12345678901")
					.country("USA")
					.email("john.email1111111@example.com")
					.username("john_doe")
					.password("password123")
					.build();

			service.registerAdmin(admin);

			var user = RegisterRequest.builder()
					.name("John")
					.surname("Doe")
					.pesel("12345678901")
					.country("USA")
					.email("john.email2222222222@example.com")
					.username("john_doe")
					.password("password123")
					.build();

			service.register(user);

			for (int i = 1; i <= 10; i++) {
				RegisterRequest recruiter = RegisterRequest.builder()
						.name("John" + i)
						.surname("Doe" + i)
						.pesel("1234567890" + i)
						.country("USA")
						.email("email" + i + "@example.com")
						.username("john_doe" + i)
						.password("password123")
						.build();

				service.registerRecruiter(recruiter);
			}

			OfferRequest offerRequest = new OfferRequest();
			offerRequest.setName("Software Developer 11");
			offerRequest.setPosition("Java Developer");
			offerRequest.setKeywords(Set.of("Java, Spring, Hibernate"));
			offerRequest.setCurrency(Currency.USD);
			offerRequest.setSalary("9000");
			offerRequest.setEmail("email1@example.com");

			offerService.createOffer(offerRequest);

			OfferRequest offerRequest2 = new OfferRequest();
			offerRequest2.setName("Software Developer 2");
			offerRequest2.setPosition("Java Developer");
			offerRequest2.setKeywords(Set.of("Java, Spring, Hibernate"));
			offerRequest2.setCurrency(Currency.USD);
			offerRequest2.setSalary("8000");
			offerRequest2.setEmail("email1@example.com");

			offerService.createOffer(offerRequest2);

			OfferRequest offerRequest3 = new OfferRequest();
			offerRequest3.setName("Developer 3");
			offerRequest3.setPosition("Java Developer");
			offerRequest3.setKeywords(Set.of("Java, Spring, Hibernate"));
			offerRequest3.setCurrency(Currency.USD);
			offerRequest3.setSalary("7000");
			offerRequest3.setEmail("email1@example.com");

			offerService.createOffer(offerRequest3);


			OfferRequest offerRequest4 = new OfferRequest();
			offerRequest4.setName("Software Developer 10");
			offerRequest4.setPosition("Java Developer");
			offerRequest4.setKeywords(Set.of("Java, Spring, Hibernate"));
			offerRequest4.setCurrency(Currency.USD);
			offerRequest4.setSalary("8400");
			offerRequest4.setEmail("email1@example.com");

			offerService.createOffer(offerRequest4);

			OfferRequest offerRequest5 = new OfferRequest();
			offerRequest5.setName("Software Developer 5");
			offerRequest5.setPosition("Java Developer");
			offerRequest5.setKeywords(Set.of("Java, Spring, Hibernate"));
			offerRequest5.setCurrency(Currency.USD);
			offerRequest5.setSalary("1000");
			offerRequest5.setEmail("email1@example.com");

			offerService.createOffer(offerRequest5);

		};
	}
*/

}
