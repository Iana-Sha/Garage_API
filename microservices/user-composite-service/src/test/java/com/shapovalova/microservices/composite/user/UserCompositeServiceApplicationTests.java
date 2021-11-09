package com.shapovalova.microservices.composite.user;

import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.api.core.car.Car;
import com.shapovalova.api.core.user.User;
import com.shapovalova.microservices.composite.user.createUser.integrationlayer.AppointmentCreateIntegration;
import com.shapovalova.microservices.composite.user.createUser.integrationlayer.CarCreateIntegration;
import com.shapovalova.microservices.composite.user.createUser.integrationlayer.UserCompositeCreateIntegration;
import com.shapovalova.microservices.composite.user.deleteUser.integrationlayer.AppointmentDeleteIntegration;
import com.shapovalova.microservices.composite.user.deleteUser.integrationlayer.CarDeleteIntegration;
import com.shapovalova.microservices.composite.user.deleteUser.integrationlayer.UserCompositeDeleteIntegration;
import com.shapovalova.microservices.composite.user.getUser.integrationlayer.AppointmentGetIntegration;
import com.shapovalova.microservices.composite.user.getUser.integrationlayer.CarGetIntegration;
import com.shapovalova.microservices.composite.user.getUser.integrationlayer.UserCompositeGetIntegration;
import com.shapovalova.utils.exception.InvalidInputException;
import com.shapovalova.utils.exception.NotFoundException;
import com.shapovalova.utils.exception.ReservedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class UserCompositeServiceApplicationTests {

	private static final int USER_ID_OKAY = 1;
	private static final int USER_ID_NOT_FOUND = 13;
	private static final String USER_ID_INVALID_STRING = "not-integer";
	private static final int USER_ID_INVALID_NEGATIVE_VALUE = -1;
	private static final int USER_ID_RESERVED = 8;

	@Autowired
	private WebTestClient client;

	@MockBean
	private UserCompositeGetIntegration userCompositeGetIntegration;

	@MockBean
	private UserCompositeDeleteIntegration userCompositeDeleteIntegration;

	@MockBean
	private UserCompositeCreateIntegration userCompositeCreateIntegration;

	@MockBean
	private AppointmentGetIntegration appointmentGetIntegration;

	@MockBean
	private AppointmentDeleteIntegration appointmentDeleteIntegration;

	@MockBean
	private AppointmentCreateIntegration appointmentCreateIntegration;

	@MockBean
	private CarGetIntegration carGetIntegration;

	@MockBean
	private CarDeleteIntegration carDeleteIntegration;

	@MockBean
	private CarCreateIntegration carCreateIntegration;


	@BeforeEach
	void setup(){
		when(userCompositeGetIntegration.getUser(USER_ID_OKAY))
				.thenReturn(new User(USER_ID_OKAY, "Iana", "Shapovalova", "ishap","ishap123", "mockAddress"));
		when(carGetIntegration.getCars(USER_ID_OKAY))
				.thenReturn(singletonList(new Car(USER_ID_OKAY, 1, "4S3BMHB68B3286050", "Honda", "Civic", "2006", "Blue", "mockAddress")));
		when(appointmentGetIntegration.getAppointments(USER_ID_OKAY))
				.thenReturn(singletonList(new Appointment(USER_ID_OKAY, 1, "2021-04-17", "8AM", "mockAddress")));
		when(userCompositeGetIntegration.getUser(USER_ID_NOT_FOUND))
				.thenThrow(new NotFoundException("NOT FOUND: " + USER_ID_NOT_FOUND));
		when(userCompositeGetIntegration.getUser(USER_ID_INVALID_NEGATIVE_VALUE))
				.thenThrow(new InvalidInputException("INVALID NEGATIVE VALUE: " + USER_ID_INVALID_NEGATIVE_VALUE));
		when(userCompositeGetIntegration.getUser(USER_ID_RESERVED))
				.thenThrow(new ReservedException("USER ID RESERVED: " + USER_ID_RESERVED));
	}

	

	@Test
	public void getUserById() {
		client.get()
				.uri("/user-composite/" + USER_ID_OKAY)
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
				.uri("/user-composite/" + USER_ID_INVALID_STRING)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user-composite/" + USER_ID_INVALID_STRING)
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getUserNotFound() {
		client.get()
				.uri("/user-composite/" + USER_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user-composite/" + USER_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("NOT FOUND: " + USER_ID_NOT_FOUND);
	}

	@Test
	public void getUserInvalidParameterNegativeValue() {
		client.get()
				.uri("/user-composite/" + USER_ID_INVALID_NEGATIVE_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user-composite/" + USER_ID_INVALID_NEGATIVE_VALUE)
				.jsonPath("$.message").isEqualTo("INVALID NEGATIVE VALUE: " + USER_ID_INVALID_NEGATIVE_VALUE);
	}

	@Test
	public void getUserInvalidParameterReserved() {
		client.get()
				.uri("/user-composite/" + USER_ID_RESERVED)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/user-composite/" + USER_ID_RESERVED)
				.jsonPath("$.message").isEqualTo("USER ID RESERVED: " + USER_ID_RESERVED);
	}

}
