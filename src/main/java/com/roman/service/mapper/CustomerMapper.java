package com.roman.service.mapper;

import com.roman.dao.entity.Customer;
import com.roman.dao.entity.CustomerInfo;
import com.roman.service.dto.customer.CreateAndLoginCustomerDto;
import com.roman.service.dto.customer.ShowCustomerDto;
import com.roman.service.dto.customer.UpdateCustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "customerInfo", expression = "java(createCustomerInfo())")
    Customer mapToCustomer(CreateAndLoginCustomerDto dto);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "customerInfo.firstname", source = "firstname")
    @Mapping(target = "customerInfo.lastname", source = "lastname")
    @Mapping(target = "customerInfo.birthday", source = "birthday")
    Customer mapToCustomer(UpdateCustomerDto dto,@MappingTarget Customer existCustomer);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstname",source = "customerInfo.firstname")
    @Mapping(target = "lastname",source = "customerInfo.lastname")
    @Mapping(target = "birthday",source = "customerInfo.birthday")
    ShowCustomerDto mapToShowCustomerDto(Customer customer);

    default CustomerInfo createCustomerInfo(){
        return new CustomerInfo();
    }
}
