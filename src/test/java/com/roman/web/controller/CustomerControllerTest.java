package com.roman.web.controller;

import com.roman.BaseTest;
import com.roman.service.dto.customer.UpdateCustomerDto;
import com.roman.service.validation.exception.CustomerException;
import jakarta.servlet.http.HttpSession;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.roman.service.validation.ExceptionMessage.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest extends BaseTest {

    private final MockMvc mock;
    private static final String EXIST_CUSTOMER_USERNAME = "Kollega1484";
    private static final String EXIST_CUSTOMER_PASSWORD = "passworD4";
    private static final Long EXIST_CUSTOMER_ID = 1L;

    @Autowired
    public CustomerControllerTest(MockMvc mock) {
        this.mock = mock;
    }

    @ParameterizedTest
    @DisplayName("Тест удачного создания пользователя")
    @MethodSource("argumentsForSuccessfulCreateCustomer")
    void successfulCreateCustomer(String username, String password) throws Exception {
        ResultActions actions = mock.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username" : "%s",
                           "password" : "%s"
                        }
                        """.formatted(username, password)));
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", Matchers.is("Customer with username '%s' was registered".formatted(username))));
    }

    static Stream<Arguments> argumentsForSuccessfulCreateCustomer() {
        return Stream.of(
                Arguments.of("NewUsername", "validPassw4"),
                Arguments.of("Parenek", "imveryGoodBoy1")
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного создания пользователя")
    @MethodSource("argumentsForUnsuccessfulCreateCustomer")
    void unsuccessfulCreateCustomer(String username, String password, String message) throws Exception {
        ResultActions actions = mock.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username" : "%s",
                           "password" : "%s"
                        }
                        """.formatted(username, password)));
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is(message)))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulCreateCustomer() {
        return Stream.of(
                Arguments.of(EXIST_CUSTOMER_USERNAME, "validPassw4", CUSTOMER_USERNAME_EXCEPTION),
                Arguments.of("Parenek", "notvalidpass", CUSTOMER_PASSWORD_EXCEPTION)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного входа в аккаунт")
    @MethodSource("argumentForSuccessfulLoginTest")
    void successfulLoginCustomer(String username, String password, String expectedId) throws Exception {
        ResultActions actions = mock.perform(post("/api/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username" : "%s",
                           "password" : "%s"
                        }
                        """.formatted(username, password)));
        actions.andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", Matchers.is(expectedId)))
                .andExpect(jsonPath("$.username", Matchers.is(username)));
    }

    static Stream<Arguments> argumentForSuccessfulLoginTest() {
        return Stream.of(
                Arguments.of(EXIST_CUSTOMER_USERNAME, EXIST_CUSTOMER_PASSWORD, "1"),
                Arguments.of("Izumi", EXIST_CUSTOMER_PASSWORD, "2")
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного входа в аккаунт")
    @MethodSource("argumentForUnsuccessfulLoginTest")
    void unsuccessfulLoginCustomer(String username, String password, String message) throws Exception {
        ResultActions actions = mock.perform(post("/api/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username" : "%s",
                           "password" : "%s"
                        }
                        """.formatted(username, password)));
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is(message)));
    }

    static Stream<Arguments> argumentForUnsuccessfulLoginTest() {
        return Stream.of(
                Arguments.of(EXIST_CUSTOMER_USERNAME, "notValidPass", CUSTOMER_AUTH_EXCEPTION),
                Arguments.of("NotExistCustomer", EXIST_CUSTOMER_PASSWORD, CUSTOMER_AUTH_EXCEPTION)
        );
    }

    @Test
    @DisplayName("Тест удачного поиска по id")
    void successfulFindById() throws Exception {
        mock.perform(get("/api/customers/" + EXIST_CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.is(EXIST_CUSTOMER_USERNAME)));
    }

    @Test
    @DisplayName("Тест неудачного поиска по id")
    void unsuccessfulFindById() throws Exception {
        mock.perform(get("/api/customers/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is(CUSTOMER_ID_EXCEPTION.formatted(100))));
    }

    @ParameterizedTest
    @DisplayName("Тест поиска всех пользователей")
    @MethodSource("argumentsForFindAllTest")
    void findAllCustomers(String pageNumber, String pageSize, int expectedCountOfCustomers) throws Exception {
        ResultActions actions = mock.perform(get("/api/customers?page=%s&size=%s".formatted(pageNumber, pageSize)));
//                .param("page", pageNumber)
//                .param("size", pageSize));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(expectedCountOfCustomers)));
    }

    static Stream<Arguments> argumentsForFindAllTest() {
        return Stream.of(
                Arguments.of("0", "3", 3),
                Arguments.of("0", "1", 1),
                Arguments.of("1", "1", 1),
                Arguments.of("1", "2", 1),
                Arguments.of("1", "3", 0)
        );
    }

    @Test
    @DisplayName("Тест удачного выхода из аккаунта")
    void successfulLogoutCustomer() throws Exception {
        MockHttpSession session = loginCustomer();

        ResultActions actions = mock.perform(post("/api/customers/logout")
                .session(session));

        actions.andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message", Matchers.is("Good buy")));
    }

    @Test
    @DisplayName("Тест неудачного выхода из аккаунта")
    void unsuccessfulLogoutCustomer() throws Exception {
        mock.perform(post("/api/customers/logout"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", Matchers.is(LOGGED_EXCEPTION)));
    }

    @Test
    @DisplayName("Тест удачного обновления пользователя")
    void successfulUpdateCustomer() throws Exception {
        MockHttpSession session = loginCustomer();
        String id = "1";
        String username = "Kollega2000";
        String password = "NewValidPass4";
        String firstname = "Romario";
        String lastname = "Zem";
        String birthday = "2020-01-01";

        ResultActions actions = mock.perform(put("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "id" : "%s",
                           "username" : "%s",
                           "password" : "%s",
                           "firstname" : "%s",
                           "lastname" : "%s",
                           "birthday" : "%s"
                        }
                        """.formatted(id, username, password, firstname, lastname, birthday))
                .session(session));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(id)))
                .andExpect(jsonPath("$.username", Matchers.is(username)))
                .andExpect(jsonPath("$.firstname", Matchers.is(firstname)))
                .andExpect(jsonPath("$.lastname", Matchers.is(lastname)))
                .andExpect(jsonPath("$.birthday", Matchers.is(birthday)));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного обновления пользователя")
    @MethodSource("argumentsForUnsuccessfulUpdateCustomer")
    void unsuccessfulUpdateCustomer(UpdateCustomerDto dto, boolean hasSession, String expectedErrorMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }
        String id = dto.getId();
        String username = dto.getUsername();
        String password = dto.getPassword();
        String firstname = dto.getFirstname();
        String lastname = dto.getLastname();
        String birthday = dto.getBirthday();

        ResultActions actions = mock.perform(put("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "id" : "%s",
                           "username" : "%s",
                           "password" : "%s",
                           "firstname" : "%s",
                           "lastname" : "%s",
                           "birthday" : "%s"
                        }
                        """.formatted(id, username, password, firstname, lastname, birthday))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedErrorMessage)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateCustomer() {
        return Stream.of(
                Arguments.of(new UpdateCustomerDto("1", "Kollega1484", "validPass4", "Name", "Lastname", "2000-01-01"), false, LOGGED_EXCEPTION),
                Arguments.of(new UpdateCustomerDto("4", "Kollega1484", "validPass4", "Name", "Lastname", "2000-01-01"), true, CUSTOMER_ID_EXCEPTION.formatted(4)),
                Arguments.of(new UpdateCustomerDto("1", "Izumi", "validPass4", "Name", "Lastname", "2000-01-01"), true, CUSTOMER_USERNAME_EXCEPTION),
                Arguments.of(new UpdateCustomerDto("1", "Kollega1484", "notvalidpass", "Name", "Lastname", "2000-01-01"), true, CUSTOMER_PASSWORD_EXCEPTION),
                Arguments.of(new UpdateCustomerDto("1", "", "validPass4", "Name", "Lastname", "2000-01-01"), true, CUSTOMER_USERNAME_EMPTY_EXCEPTION)
        );
    }

    @Test
    @DisplayName("Тест удачного удаления пользователя")
    void successfulDeleteCustomer() throws Exception {
        mock.perform(delete("/api/customers").session(loginCustomer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("Customer with id '1' was deleted.")));
    }

    @Test
    @DisplayName("Тест неудачного удаления пользователя")
    void unsuccessfulDeleteCustomer() throws Exception {
        MockHttpSession session = loginCustomer();
        session.invalidate();
        ResultActions actions = mock.perform(delete("/api/customers").session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",Matchers.is(LOGGED_EXCEPTION)));

    }


    MockHttpSession loginCustomer() throws Exception {
        MvcResult mvcResult = mock.perform(post("/api/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username" : "Kollega1484",
                           "password" : "passworD4"
                        }
                        """)).andReturn();
        return (MockHttpSession) mvcResult.getRequest().getSession();
    }

}
