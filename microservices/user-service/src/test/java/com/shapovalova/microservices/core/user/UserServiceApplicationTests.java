package com.shapovalova.microservices.core.user;

import com.shapovalova.api.core.user.User;
import com.shapovalova.microservices.core.user.datalayer.UserEntity;
import com.shapovalova.microservices.core.user.datalayer.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

//import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static reactor.core.publisher.Mono.just;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class UserServiceApplicationTests {

	private static final int USER_ID_OKAY = 1;
	private static final int USER_ID_NOT_FOUND = 13;
	private static final String USER_ID_INVALID_STRING = "not-integer";
	private static final int USER_ID_INVALID_NEGATIVE_VALUE = -1;
	private static final int USER_ID_RESERVED = 8;

	@Autowired
	private WebTestClient client;

	@Autowired
	private UserRepository repository;

	@BeforeEach
	public void setupDb()
	{
		repository.deleteAll();
	}
	
	
	@Test
	public void getUserById() {
		//add the user entity to the db
		UserEntity entity = new UserEntity(USER_ID_OKAY, "name-" + USER_ID_OKAY, "lastname-" + USER_ID_OKAY,"login-" + USER_ID_OKAY, "password-" + USER_ID_OKAY);
		repository.save(entity);

		//make sure it's in the repo
		assertTrue(repository.findByUserId(USER_ID_OKAY).isPresent());
		
		client.get()
				.uri("/user/" + USER_ID_OKAY)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.userId").isEqualTo(USER_ID_OKAY);
	}

	@Test
	public void getUserInvalidParameterString() {
		client.get()
				.uri("/user/" + USER_ID_INVALID_STRING)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user/" + USER_ID_INVALID_STRING)
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getUserNotFound() {
		client.get()
				.uri("/user/" + USER_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user/" + USER_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("No user found for userId: " + USER_ID_NOT_FOUND);
	}

	@Test
	public void getUserInvalidParameterNegativeValue() {
		client.get()
				.uri("/user/" + USER_ID_INVALID_NEGATIVE_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user/" + USER_ID_INVALID_NEGATIVE_VALUE)
				.jsonPath("$.message").isEqualTo("Invalid userId: " + USER_ID_INVALID_NEGATIVE_VALUE);
	}

	@Test
	public void getUserInvalidParameterReserved() {
		client.get()
				.uri("/user/" + USER_ID_RESERVED)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user/" + USER_ID_RESERVED)
				.jsonPath("$.message").isEqualTo("Access denied. Private user found for Id: " + USER_ID_RESERVED);
	}

	@Test
	public void createUser()
	{
		//add the user entity to the db
		User model = new User(USER_ID_OKAY, "name-" + USER_ID_OKAY, "lastname-" + USER_ID_OKAY,"login-" + USER_ID_OKAY, "password-" + USER_ID_OKAY,"SA");

		client.post()
				.uri("/user")
				.body(just(model), User.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.userId").isEqualTo(USER_ID_OKAY);
		assertTrue(repository.findByUserId(USER_ID_OKAY).isPresent());
	}

//	@Test
//	public void deleteUser(){
//		UserEntity entity = new UserEntity(USER_ID_OKAY, "name-" + USER_ID_OKAY, "lastname-" + USER_ID_OKAY,"login-" + USER_ID_OKAY, "password-" + USER_ID_OKAY);
//		repository.save(entity);
//
//		assertTrue(repository.findByUserId(USER_ID_OKAY).isPresent());
//
//		client.delete()
//				.uri("/user/" + USER_ID_OKAY)
//				.accept(MediaType.APPLICATION_JSON)
//				.exchange()
//				.expectStatus().isOk()
//				.expectBody();
//
//		assertFalse(repository.findByUserId(USER_ID_OKAY).isPresent());
//	}
	
	@Test
	void contextLoads()
	{

	}

}
