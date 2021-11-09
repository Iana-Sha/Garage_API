package com.shapovalova.microservices.core.car;

import com.shapovalova.api.core.car.Car;
import com.shapovalova.microservices.core.car.datalayer.CarEntity;
import com.shapovalova.microservices.core.car.datalayer.CarRepository;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "spring.datasource.url=jdbc:h2:mem:car-db"})
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class CarServiceApplicationTests {

	private static final int USER_ID_OKAY = 1;
	private static final int USER_ID_NOT_FOUND = 213;
	private static final String USER_ID_INVALID_STRING = "not-integer";
	private static final int USER_ID_INVALID_NEGATIVE_VALUE = -1;
	private static final int USER_ID_RESERVED = 8;
	private static final int CARID = 1;

	@Autowired
	private WebTestClient client;
	
	@Autowired
	private CarRepository repository;

	@BeforeEach
	public void setupDb(){repository.deleteAll();}
	@Test
	public void getCarsByCarId() {
		int expectedLength = 3;

		CarEntity entity1 = new CarEntity(USER_ID_OKAY, 1, "123abc", "Toyota-1", "Corolla-1","2008","grey");
		repository.save(entity1);
		CarEntity entity2 = new CarEntity(USER_ID_OKAY, 2, "123abc", "Toyota-2", "Corolla-2","2008","grey");
		repository.save(entity2);
		CarEntity entity3 = new CarEntity(USER_ID_OKAY, 3, "123abc", "Toyota-3", "Corolla-3","2008","grey");
		repository.save(entity3);

		
		client.get()
				.uri("/car?userId=" + USER_ID_OKAY)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(expectedLength)
				.jsonPath("$[0].userId").isEqualTo(USER_ID_OKAY);
				//.jsonPath("$[1].userId").isEqualTo(USER_ID_OKAY)
				//.jsonPath("$[2].userId").isEqualTo(USER_ID_OKAY);
	}

	@Test
	public void getCarsMissingParameter() {
		client.get()
				.uri("/car")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/car")
				.jsonPath("$.message").isEqualTo("Required int parameter 'userId' is not present");
	}

	@Test
	public void getCarsInvalidParameterString() {
		client.get()
				.uri("/car?userId=" + USER_ID_INVALID_STRING)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/car")
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getCarsNotFound() {
		int expectedLength = 0;
		client.get()
				.uri("/car?userId=" + USER_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(expectedLength);
	}

	@Test
	public void getCarsInvalidParameterNegativeValue() {
		client.get()
				.uri("/car?userId=" + USER_ID_INVALID_NEGATIVE_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/car")
				.jsonPath("$.message").isEqualTo("Invalid userId: " + USER_ID_INVALID_NEGATIVE_VALUE);
	}

	@Test
	public void getCarsInvalidParameterReserved() {
		client.get()
				.uri("/car?userId=" + USER_ID_RESERVED)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/car")
				.jsonPath("$.message").isEqualTo("Access denied. Private user found for Id: " + USER_ID_RESERVED);
	}

	@Test
	public void createCar(){
		Car car = new Car(USER_ID_OKAY, CARID, "Vin " + CARID, "make" + CARID, "model" + CARID, "year" + CARID, "color" + CARID, "SA");
		client.post()
				.uri("/car")
				.body(just(car), Car.class)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody();
		//verify it's in the database
		assertEquals(1, repository.findByUserId(USER_ID_OKAY).size());
	}

	@Test
	public void deleteAppointments()
	{
		CarEntity entity = new CarEntity(USER_ID_OKAY, CARID, "vin-1", "make" + CARID, "model"+CARID, "year"+CARID, "color"+CARID);
		repository.save(entity);

		assertEquals(1, repository.findByUserId(USER_ID_OKAY).size());

		client.delete()
				.uri("/car?userId=" + USER_ID_OKAY)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody();
		assertEquals(0,repository.findByUserId(USER_ID_OKAY).size());

	}

	@Test
	void contextLoads() {
	}

}
