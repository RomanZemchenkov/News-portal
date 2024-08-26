package com.roman.service;

import com.roman.BaseTest;
import com.roman.service.dto.customer.CreateAndLoginCustomerDto;
import com.roman.service.dto.customer.ShowCustomerDto;
import com.roman.service.dto.customer.UpdateCustomerDto;
import com.roman.service.validation.exception.CustomerException;
import com.roman.service.validation.exception.CustomerNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class CustomerServiceIT extends BaseTest {

    private static final Long ID = 1L;
    private static final Long DOESNT_EXIST_ID = 100L;

    private final CustomerService service;

    @Autowired
    public CustomerServiceIT(CustomerService service) {
        this.service = service;
    }

    @ParameterizedTest
    @DisplayName("Тест удачного сохранения пользователя")
    @MethodSource("argumentsForSuccessfulSaveCustomer")
    void successfulSaveCustomer(CreateAndLoginCustomerDto dto) {
        Assertions.assertDoesNotThrow(() -> service.save(dto));
    }

    static Stream<Arguments> argumentsForSuccessfulSaveCustomer(){
        return Stream.of(
                Arguments.of(new CreateAndLoginCustomerDto("Kollega","password")),
                Arguments.of(new CreateAndLoginCustomerDto("TestUsername","password"))
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного сохранения пользователя")
    @MethodSource("argumentsForUnsuccessfulSaveCustomer")
    void unsuccessfulSaveCustomer(CreateAndLoginCustomerDto dto) {
        Assertions.assertThrows(CustomerException.class,() -> service.save(dto));
    }

    static Stream<Arguments> argumentsForUnsuccessfulSaveCustomer(){
        return Stream.of(
                Arguments.of(new CreateAndLoginCustomerDto("Kollega1484","password"))
        );
    }

    @Test
    @DisplayName("Тест удачной авторизации пользователя")
    void successfulLoginCustomer(){
        Assertions.assertDoesNotThrow(() -> service.login(new CreateAndLoginCustomerDto("Kollega1484","passworD4")));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачной авторизации пользователя")
    @MethodSource("argumentsForUnsuccessfulLoginTest")
    void unsuccessfulLoginCustomer(CreateAndLoginCustomerDto dto){
        Assertions.assertThrows(CustomerException.class,
                () -> service.login(dto));
    }

    static Stream<Arguments> argumentsForUnsuccessfulLoginTest(){
        return Stream.of(
                Arguments.of(new CreateAndLoginCustomerDto("NotExistUsername","password")),
                Arguments.of(new CreateAndLoginCustomerDto("Kollega1484","pass"))
        );
    }

    @Test
    @DisplayName("Тест удачного поиска пользователя по id")
    void successfulFindCustomerById(){
        Assertions.assertDoesNotThrow(() -> service.findById(ID));
    }

    @Test
    @DisplayName("Тест неудачного поиска пользователя по id")
    void unsuccessfulFindCustomerById(){
        Assertions.assertThrows(CustomerNotExistException.class,() -> service.findById(DOESNT_EXIST_ID));
    }

    @ParameterizedTest
    @DisplayName("Поиск всех пользователей")
    @MethodSource("argumentsForFindAllCustomers")
    void findAllCustomers(int pageNumber, int pageSize, int expectedCustomerCount){
        List<ShowCustomerDto> customers = service.findAllCustomer(pageNumber, pageSize);
        assertThat(customers).hasSize(expectedCustomerCount);
    }

    static Stream<Arguments> argumentsForFindAllCustomers(){
        return Stream.of(
                Arguments.of(0,3,3),
                Arguments.of(0,2,2),
                Arguments.of(1,2,1),
                Arguments.of(2,1,1)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного обновления пользователя")
    @MethodSource("argumentsForSuccessfulUpdateCustomer")
    void successfulUpdateCustomer(UpdateCustomerDto dto){
        Assertions.assertDoesNotThrow(() -> service.update(dto));

        List<ShowCustomerDto> allCustomer = service.findAllCustomer(0, 3);
        System.out.println(allCustomer);
    }

    static Stream<Arguments> argumentsForSuccessfulUpdateCustomer(){
        return Stream.of(
                Arguments.of(new UpdateCustomerDto("1","Kollega","password","Ivan","Ivanov","1980-02-02")),
                Arguments.of(new UpdateCustomerDto("2","Izumi","newPassword","Alina","Metrofanova","1993-12-07")),
                Arguments.of(new UpdateCustomerDto("3","DungeonMaster","newPassword","Ivan","Ivanov","2011-12-12"))
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного обновления пользователя")
    @MethodSource("argumentsForUnsuccessfulUpdateCustomer")
    void unsuccessfulUpdateCustomer(UpdateCustomerDto dto){
        Assertions.assertDoesNotThrow(() -> service.update(dto));

        List<ShowCustomerDto> allCustomer = service.findAllCustomer(0, 3);
        System.out.println(allCustomer);
    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateCustomer(){
        return Stream.of(
                Arguments.of(new UpdateCustomerDto("1","Kollega","password","Ivan","Ivanov","1980-02-02")),
                Arguments.of(new UpdateCustomerDto("2","Izumi","newPassword","Alina","Metrofanova","1993-12-07")),
                Arguments.of(new UpdateCustomerDto("3","DungeonMaster","newPassword","Ivan","Ivanov","2011-12-12"))
        );
    }

    @Test
    @DisplayName("Тест удаления пользователя")
    void deleteCustomerById(){
        Assertions.assertDoesNotThrow(() -> assertThat(service.delete(ID)).isEqualTo(ID));
    }
}
