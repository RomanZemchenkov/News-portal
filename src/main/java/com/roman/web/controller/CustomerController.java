package com.roman.web.controller;

import com.roman.aop.annotation.CheckSession;
import com.roman.service.CustomerService;
import com.roman.service.dto.customer.CreateAndLoginCustomerDto;
import com.roman.service.dto.customer.ShowCustomerDto;
import com.roman.service.dto.customer.UpdateCustomerDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping("/{customerId}")
    public ResponseEntity<ShowCustomerDto> findById(@PathVariable("customerId") Long customerId){
        ShowCustomerDto customer = service.findById(customerId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ShowCustomerDto>> findAll(@RequestParam("page") int page,
                                                         @RequestParam("size") int size){
        List<ShowCustomerDto> customers = service.findAllCustomer(page, size);
        return new ResponseEntity<>(customers,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> create(@Validated @RequestBody CreateAndLoginCustomerDto dto){
        service.save(dto);
        Response response = new Response("Customer with username '%s' was registered".formatted(dto.getUsername()));
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ShowCustomerDto> login(@RequestBody CreateAndLoginCustomerDto dto, HttpSession session){
        ShowCustomerDto customer = service.login(dto);
        session.setAttribute("customerId",customer.getId());
        return new ResponseEntity<>(customer,HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    @CheckSession
    public ResponseEntity<Response> logout(HttpSession session){
        session.invalidate();
        return new ResponseEntity<>(new Response("Good buy"),HttpStatus.ACCEPTED);
    }

    @PutMapping
    @CheckSession
    public ResponseEntity<ShowCustomerDto> update(@Validated @RequestBody UpdateCustomerDto dto){
        ShowCustomerDto update = service.update(dto);
        return new ResponseEntity<>(update,HttpStatus.OK);
    }

    @DeleteMapping("")
    @CheckSession
    public ResponseEntity<Response> delete(HttpSession session){
        String customerId = (String) session.getAttribute("customerId");
        Long id = service.delete(Long.valueOf(customerId));
        Response response = new Response("Customer with id '%d' was deleted.".formatted(id));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
