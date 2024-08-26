package com.roman.service;

import com.roman.dao.entity.Customer;
import com.roman.dao.repository.customer.CustomerRepository;
import com.roman.service.dto.customer.CreateAndLoginCustomerDto;
import com.roman.service.dto.customer.ShowCustomerDto;
import com.roman.service.dto.customer.UpdateCustomerDto;
import com.roman.service.mapper.CustomerMapper;
import com.roman.service.validation.CustomerValidator;
import com.roman.service.validation.exception.CustomerException;
import com.roman.service.validation.exception.CustomerNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.roman.service.validation.ExceptionMessage.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final CustomerValidator validation;


    @Transactional
    public void save(CreateAndLoginCustomerDto dto){
        List<Customer> customers = validation.checkUsernameExist(dto.getUsername());
        if(!customers.isEmpty()){
            throw new CustomerException(CUSTOMER_USERNAME_EXCEPTION);
        }
        Customer customer = mapper.mapToCustomer(dto);
        repository.saveAndFlush(customer);
    }

    @Transactional
    public ShowCustomerDto login(CreateAndLoginCustomerDto dto){
        List<Customer> customers = validation.checkUsernameExist(dto.getUsername());

        if(customers.isEmpty()){
           throw new CustomerException(CUSTOMER_AUTH_EXCEPTION);
        } else {
            Customer customer = customers.get(0);
            String password = customer.getPassword();
            if(dto.getPassword().equals(password)){
                return mapper.mapToShowCustomerDto(customer);
            } else {
                throw new CustomerException(CUSTOMER_AUTH_EXCEPTION);
            }
        }
    }


    public ShowCustomerDto findById(Long id){
        Optional<Customer> mayBeCustomer = repository.findCustomerById(id);
        if(mayBeCustomer.isEmpty()){
            throw new CustomerNotExistException(CUSTOMER_ID_EXCEPTION.formatted(id));
        }

        return mapper.mapToShowCustomerDto(mayBeCustomer.get());
    }

    public List<ShowCustomerDto> findAllCustomer(int pageNumber, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Customer> customers = repository.findAllBy(pageRequest);
        return customers.stream().map(mapper::mapToShowCustomerDto).toList();
    }

    @Transactional
    public ShowCustomerDto update(UpdateCustomerDto dto){
        Long id = Long.valueOf(dto.getId());
        Optional<Customer> mayBeCustomer = repository.findCustomerById(id);
        if(mayBeCustomer.isEmpty()){
            throw new CustomerNotExistException(CUSTOMER_ID_EXCEPTION.formatted(id));
        }
        Customer customer = mayBeCustomer.get();
        String newUsername = dto.getUsername();
        if(!customer.getUsername().equals(newUsername)){
            List<Customer> customers = validation.checkUsernameExist(newUsername);
            if (!customers.isEmpty()){
                throw new CustomerException(CUSTOMER_USERNAME_EXCEPTION);
            }
        }

        Customer updatedCustomer = mapper.mapToCustomer(dto, customer);
        Customer savedCustomer = repository.saveAndFlush(updatedCustomer);

        return mapper.mapToShowCustomerDto(savedCustomer);
    }

    @Transactional
    public Long delete(Long id){
        Optional<Customer> mayBeCustomer = repository.findCustomerById(id);
        if(mayBeCustomer.isEmpty()){
            throw new CustomerNotExistException(CUSTOMER_ID_EXCEPTION.formatted(id));
        }
        repository.deleteCustomerById(id);
        return id;
    }
}
