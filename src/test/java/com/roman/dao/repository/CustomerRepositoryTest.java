package com.roman.dao.repository;

import com.roman.dao.entity.Customer;
import com.roman.dao.entity.CustomerInfo;
import com.roman.dao.repository.customer.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CustomerRepositoryTest extends BaseRepositoryTest{

    private final CustomerRepository repository;
    private final EntityManager entityManager;
    private final static String BASE_USERNAME = "Kollega1484";
    private final static String BASE_PASSWORD = "password";
    private final static Long BASE_ID = 1L;

    @Autowired
    public CustomerRepositoryTest(CustomerRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Тест сохранения пользователя")
    void saveCustomerTest(){
        CustomerInfo customerInfo = new CustomerInfo("Roman", "Zemchenkov", LocalDate.of(2000, 2, 8));
        Customer customer = new Customer("NewUser", "validPass", customerInfo);
        Customer savedCustomer = repository.saveAndFlush(customer);

        Assertions.assertEquals(savedCustomer.getId(),4L);
    }

    @Test
    @DisplayName("Тест сохранения с таким же username")
    void saveWithExistUsername(){
        CustomerInfo customerInfo = entityManager.createQuery("SELECT c FROM CustomerInfo c WHERE c.id = :id", CustomerInfo.class)
                .setParameter("id", 1L).getSingleResult();
        /*
        Как я понимаю, при следующем вызове метода saveAndFlush происходит проверка на isNew и после неё, так как у сущности нет id
        и чего-то ещё - вызывается запрос с базе данных, чтобы проверить, есть ли там такая сущность
        делается insert и rollback, так как  insert был неудачным
         */
        Customer customer1 = new Customer(BASE_USERNAME, BASE_PASSWORD, customerInfo);
        Assertions.assertThrows(Throwable.class, () -> repository.saveAndFlush(customer1));
    }

    @ParameterizedTest
    @DisplayName("Тест поиска пользователя")
    @MethodSource("argumentsForFindByIdTest")
    void findCustomerById(Long id, boolean expectedResult){
        Optional<Customer> mayBeCustomer = repository.findById(id);
        assertThat(mayBeCustomer.isPresent()).isEqualTo(expectedResult);
    }

    static Stream<Arguments> argumentsForFindByIdTest(){
        return Stream.of(
                Arguments.of(BASE_ID, true),
                Arguments.of(4L, false)
        );
    }


    @Test
    @DisplayName("Тест обновления пользователя")
    void updateCustomer(){
        Customer customer = entityManager.find(Customer.class, 1L);
        String newUsername = "NewUsername";
        customer.setUsername(newUsername);
        Customer savedCustomer = repository.saveAndFlush(customer);

        assertThat(savedCustomer.getUsername()).isEqualTo(newUsername);
    }

    @Test
    @DisplayName("Тест поиска всех пользователей")
    void findAllCustomers(){
        List<Customer> customer = repository.findAll();
        assertThat(customer).hasSize(3);
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void deleteCustomerById(){
        Assertions.assertDoesNotThrow(() -> repository.deleteById(BASE_ID));
    }

}
