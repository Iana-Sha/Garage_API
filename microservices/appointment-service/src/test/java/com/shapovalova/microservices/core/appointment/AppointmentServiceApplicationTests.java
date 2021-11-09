package com.shapovalova.microservices.core.appointment;

import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.microservices.core.appointment.datalayer.AppointmentEntity;
import com.shapovalova.microservices.core.appointment.datalayer.AppointmentRepository;
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
import static reactor.core.publisher.Mono.just;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:appointment-db"})
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class AppointmentServiceApplicationTests {

	private static final int USER_ID_OKAY = 1;
	private static final int USER_ID_NOT_FOUND = 113;
	private static final String USER_ID_INVALID_STRING = "not-integer";
	private static final int USER_ID_INVALID_NEGATIVE_VALUE = -1;
	private static final int USER_ID_RESERVED = 8;
	
	private static final int APPOINTMENT_ID = 1;

	@Autowired
	private WebTestClient client;
	
	@Autowired
	private AppointmentRepository repository;
	
	@BeforeEach
	public void setupDb(){repository.deleteAll();}

	@Test
	public void getAppointmentsById() {
		int expectedLength = 3;
		
		//add the appointments to the repo
		AppointmentEntity entity1 = new AppointmentEntity(USER_ID_OKAY, APPOINTMENT_ID, "Date-1", "Time-1");
		repository.save(entity1);

		AppointmentEntity entity2 = new AppointmentEntity(USER_ID_OKAY, APPOINTMENT_ID + 1, "Date-2", "Time-2");
		repository.save(entity2);

		AppointmentEntity entity3 = new AppointmentEntity(USER_ID_OKAY, APPOINTMENT_ID + 2, "Date-3", "Time-3");
		repository.save(entity3);

		assertEquals(expectedLength, repository.findByUserId(USER_ID_OKAY).size());

		client.get()
				.uri("/appointment?userId=" + USER_ID_OKAY)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(expectedLength)
				.jsonPath("$[0].userId").isEqualTo(USER_ID_OKAY)
				.jsonPath("$[1].userId").isEqualTo(USER_ID_OKAY)
				.jsonPath("$[2].userId").isEqualTo(USER_ID_OKAY);
	}

	@Test
	public void createAppointment(){

		int expectedSize = 1;

		//create the appointment
		Appointment appointment = new Appointment(USER_ID_OKAY, APPOINTMENT_ID,
				"Date" + APPOINTMENT_ID, "Time"+APPOINTMENT_ID, "SA");

		//send the POST request
		client.post()
				.uri("/appointment")
				.body(just(appointment), Appointment.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();

		assertEquals(expectedSize, repository.findByUserId(USER_ID_OKAY).size());
	}

	@Test
	public void deleteAppointment(){

		AppointmentEntity entity = new AppointmentEntity(USER_ID_OKAY, APPOINTMENT_ID, "Date-1", "Time-1");
		//save it
		repository.save(entity);

		//verify there are 1 entity for user id 1
		assertEquals(1, repository.findByUserId(USER_ID_OKAY).size());

		//send the DELETE request
		client.delete()
				.uri("/appointment?userId=" + USER_ID_OKAY)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody();

		//verify there are no entities for user id 1
		assertEquals(0, repository.findByUserId(USER_ID_OKAY).size());
	}

	@Test
	public void getAppointmentsMissingParameter() {
		client.get()
				.uri("/appointment")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/appointment")
				.jsonPath("$.message").isEqualTo("Required int parameter 'userId' is not present");
	}

	@Test
	public void getAppointmentsInvalidParameterString() {
		client.get()
				.uri("/appointment?userId=" + USER_ID_INVALID_STRING)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/appointment")
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getAppointmentsNotFound() {
		int expectedLength = 0;
		client.get()
				.uri("/appointment?userId=" + USER_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.length()").isEqualTo(expectedLength);
	}

	@Test
	public void getAppointmentsInvalidParameterNegativeValue() {
		client.get()
				.uri("/appointment?userId=" + USER_ID_INVALID_NEGATIVE_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/appointment")
				.jsonPath("$.message").isEqualTo("Invalid userId: " + USER_ID_INVALID_NEGATIVE_VALUE);
	}

	@Test
	public void getAppointmentsInvalidParameterReserved() {
		client.get()
				.uri("/appointment?userId=" + USER_ID_RESERVED)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/appointment")
				.jsonPath("$.message").isEqualTo("Access denied. Private user found for Id: " + USER_ID_RESERVED);
	}

}
