package com.roman.service;

import com.roman.aop.annotation.CheckExist;
import com.roman.dao.entity.Category;
import com.roman.dao.entity.Customer;
import com.roman.dao.repository.category.CategoryRepository;
import com.roman.service.dto.category.CreateCategoryDto;
import com.roman.service.dto.category.ShowCategoryDto;
import com.roman.service.dto.category.ShowCategoryWithoutNewsDto;
import com.roman.service.dto.category.UpdateCategoryDto;
import com.roman.service.mapper.CategoryMapper;
import com.roman.service.validation.CategoryValidator;
import com.roman.service.validation.CustomerValidator;
import com.roman.service.validation.exception.CategoryException;
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
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryValidator validator;
    private final CustomerValidator customerValidator;
    private final CategoryMapper mapper;


    @Transactional
    public ShowCategoryWithoutNewsDto create(CreateCategoryDto dto, String authorId){
        Long id = Long.valueOf(authorId);
        Optional<Customer> mayBeCustomer = customerValidator.checkCustomerExist(id);

        if(mayBeCustomer.isEmpty()){
            throw new CustomerNotExistException(CUSTOMER_ID_EXCEPTION.formatted(id));
        }

        String title = dto.getTitle();
        List<Category> categories = validator.checkTitle(title);
        if(!categories.isEmpty()){
            throw new CategoryException(CATEGORY_EXIST_EXCEPTION.formatted(title));
        }

        Category category = mapper.mapToCategory(dto, mayBeCustomer.get());
        Category savedCategory = repository.saveAndFlush(category);

        return mapper.mapToShowWithoutNews(savedCategory);
    }

    public List<ShowCategoryWithoutNewsDto> findAll(int pageNumber, int pageSize){
        Page<Category> categories = repository.findAllBy(PageRequest.of(pageNumber, pageSize));
        return categories.stream().map(mapper::mapToShowWithoutNews).toList();
    }

    @CheckExist
    public ShowCategoryDto findCategoryById(Long id){
        Category category = repository.findCategoryById(id);
        return mapper.mapToShow(category);
    }

    @Transactional
    @CheckExist
    public ShowCategoryDto update(UpdateCategoryDto dto){
        Long id = Long.valueOf(dto.getId());
        Category category = repository.findCategoryById(id);

        String title = dto.getTitle();
        if(!category.getTitle().equals(title)){
            List<Category> categories = validator.checkTitle(title);
            if(!categories.isEmpty()){
                throw new CategoryException(CATEGORY_EXIST_EXCEPTION.formatted(title));
            }
        }
        Category updateCategory = mapper.mapToCategory(dto, category);
        Category updatedCategory = repository.saveAndFlush(updateCategory);

        return mapper.mapToShow(updatedCategory);
    }

    @Transactional
    @CheckExist
    public Long deleteById(Long id){
        repository.deleteById(id);
        return id;
    }
}
