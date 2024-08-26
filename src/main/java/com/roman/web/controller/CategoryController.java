package com.roman.web.controller;

import com.roman.annotation.CheckAuthor;
import com.roman.annotation.CheckSession;
import com.roman.service.CategoryService;
import com.roman.service.dto.category.CreateCategoryDto;
import com.roman.service.dto.category.ShowCategoryDto;
import com.roman.service.dto.category.ShowCategoryWithoutNewsDto;
import com.roman.service.dto.category.UpdateCategoryDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    @CheckSession
    public ResponseEntity<ShowCategoryWithoutNewsDto> create(@RequestBody @Validated CreateCategoryDto dto,
                                                             HttpSession session){
        String customerId = (String) session.getAttribute("customerId");
        ShowCategoryWithoutNewsDto newCategory = service.create(dto, customerId);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<ShowCategoryDto> findById(@PathVariable("categoryId") Long id){
        ShowCategoryDto category = service.findCategoryById(id);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ShowCategoryWithoutNewsDto>> findAll(@RequestParam("page") int page,
                                                                    @RequestParam("size") int size){
        List<ShowCategoryWithoutNewsDto> categories = service.findAll(page, size);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PutMapping
    @CheckSession
    @CheckAuthor
    public ResponseEntity<ShowCategoryDto> update(@RequestBody UpdateCategoryDto dto){
        ShowCategoryDto updatedCategory = service.update(dto);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @CheckSession
    @CheckAuthor
    public ResponseEntity<Response> delete(@PathVariable("categoryId") Long categoryId){
        Long deletedId = service.deleteById(categoryId);
        Response response = new Response("Category with id %d was deleted.".formatted(deletedId));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
