package com.roman.service;

import com.roman.dao.entity.Category;
import com.roman.dao.entity.Customer;
import com.roman.dao.entity.News;
import com.roman.dao.filter.NewsFilter;
import com.roman.dao.repository.news.NewsRepository;
import com.roman.service.dto.news.*;
import com.roman.service.mapper.NewsMapper;
import com.roman.service.validation.CategoryValidator;
import com.roman.service.validation.CustomerValidator;
import com.roman.service.validation.NewsValidator;
import com.roman.service.validation.exception.CategoryException;
import com.roman.service.validation.exception.CustomerException;
import com.roman.service.validation.exception.NewsException;
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
public class NewsService {

    private final NewsRepository repository;
    private final NewsValidator validator;
    private final CategoryValidator categoryValidator;
    private final CustomerValidator customerValidator;
    private final NewsMapper mapper;

    /*
    Придумать способ, как улучшить запрос так, чтобы у пользователя не доставались его комментарии и информация о нём
     */
    @Transactional
    public CreatedNewsDto create(CreateNewsDto dto, String authorId){
        String title = dto.getTitle();
        Long categoryId = Long.valueOf(dto.getCategoryId());
        Long customerId = Long.valueOf(authorId);

        Optional<Category> mayBeCategory = categoryValidator.checkCategoryExist(categoryId);
        if(mayBeCategory.isEmpty()){
            throw new CategoryException(CATEGORY_DOESNT_EXIST.formatted(categoryId));
        }

        Optional<Customer> mayBeCustomer = customerValidator.checkCustomerExist(customerId);
        if(mayBeCustomer.isEmpty()){
            throw new CustomerException(CUSTOMER_ID_EXCEPTION.formatted(customerId));
        }

        Optional<News> news = validator.checkTitle(title);
        if(news.isPresent()){
            throw new NewsException(NEWS_TITLE_EXIST_EXCEPTION.formatted(title));
        }

        News newNews = mapper.mapToNews(dto, mayBeCategory.get(),mayBeCustomer.get());
        News savedNews = repository.saveAndFlush(newNews);

        String author = savedNews.getCustomer().getUsername();
        String categoryTitle = savedNews.getCategory().getTitle();
        return mapper.mapToCreatedDto(savedNews, author, categoryTitle);
    }

    public ShowNewsDto findById(Long id){
        Optional<News> mayBeNews = repository.findNewsWithAllInformationById(id);
        if(mayBeNews.isEmpty()){
            throw new NewsException(NEWS_DOESNT_EXIST_EXCEPTION.formatted(id));
        }

        News foundNews = mayBeNews.get();
        return mapper.mapToShow(foundNews);
    }

    public List<ShowNewsWithoutCommentsDto> findAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<News> newsPage = repository.findAllBy(pageRequest);

        return newsPage
                .stream()
                .map(this::createShow)
                .toList();
    }

    public List<ShowNewsWithoutCommentsDto> findAllByFilter(FilterNewsDto filterDto){
        NewsFilter.Builder builder = new NewsFilter.Builder();
        String customerId = filterDto.getCustomerId();
        String categoryId = filterDto.getCategoryId();
        if(!customerId.isBlank()){
            builder.customerId(Long.valueOf(customerId));
        }
        if(!categoryId.isBlank()){
            builder.categoryId(Long.valueOf(categoryId));
        }
        NewsFilter filter = builder.build();
        Page<News> newsPage;
        String size = filterDto.getSize();
        String page = filterDto.getPage();


        if(page.equals("0") || size.equals("0")){
            newsPage = repository.findNewsByFilter(filter);
        } else {
            PageRequest pageRequest = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
            newsPage = repository.findNewsByFilter(filter,pageRequest);
        }
        return newsPage.stream().map(this::createShow).toList();
    }

    @Transactional
    public ShowNewsDto update(UpdateNewsDto dto){
        Long newsId = Long.valueOf(dto.getId());
        News existNews = repository.findNewsWithAllInformationById(newsId).get();

        Long categoryId = Long.valueOf(dto.getCategoryId());
        Optional<Category> mayBeCategory = categoryValidator.checkCategoryExist(categoryId);
        if(mayBeCategory.isEmpty()){
            throw new CategoryException(CATEGORY_DOESNT_EXIST.formatted(categoryId));
        }

        String presentTitle = dto.getTitle();
        if(!presentTitle.equals(existNews.getTitle())){
            Optional<News> news = validator.checkTitle(presentTitle);
            if(news.isPresent()){
                throw new NewsException(NEWS_TITLE_EXIST_EXCEPTION.formatted(presentTitle));
            }
        }

        News newsBeforeUpdate = mapper.mapToNews(dto, existNews, mayBeCategory.get());
        News updatedNews = repository.saveAndFlush(newsBeforeUpdate);

        return mapper.mapToShow(updatedNews);
    }

    @Transactional
    public Long delete(Long id){
        Optional<News> mayBeNews = repository.findById(id);
        mayBeNews.ifPresentOrElse(repository::delete,() -> {throw new NewsException(NEWS_DOESNT_EXIST_EXCEPTION.formatted(id));});
        return id;
    }

    private ShowNewsWithoutCommentsDto createShow(News news){
        String author = news.getCustomer().getUsername();
        String categoryTitle = news.getCategory().getTitle();
        String countOfComments = Integer.toString(news.getComments().size());
        return mapper.mapToShow(news,author,categoryTitle,countOfComments);
    }


}
