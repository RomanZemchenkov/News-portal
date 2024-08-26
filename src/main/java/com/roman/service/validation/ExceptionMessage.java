package com.roman.service.validation;

public class ExceptionMessage {

    public static final String LOGGED_EXCEPTION = "You are`not logged in.";

    public static final String CUSTOMER_ID_EXCEPTION = "Customer with id %d doesn`t exist.";
    public static final String CUSTOMER_USERNAME_EMPTY_EXCEPTION = "Username should be not empty.";
    public static final String CUSTOMER_PASSWORD_EXCEPTION = "Password must be at least 8 characters long, contain one uppercase letter and one number.";
    public static final String CUSTOMER_USERNAME_EXCEPTION = "This username already exists.";
    public static final String CUSTOMER_AUTH_EXCEPTION = "These username or password are not correct.";

    public static final String CATEGORY_EXIST_EXCEPTION = "Category with title '%s' already exists.";
    public static final String CATEGORY_DOESNT_EXIST = "Category with id %d doesn`t exist.";
    public static final String CATEGORY_AUTHOR_EXCEPTION = "You aren`t author of this category, you can`t delete her.";
    public static final String CATEGORY_TITLE_EXCEPTION = "Category title should be not empty.";

    public static final String NEWS_TITLE_EXIST_EXCEPTION = "News with title '%s' already exist.";
    public static final String NEWS_DOESNT_EXIST_EXCEPTION = "News with id %d doesn`t exist.";
    public static final String NEWS_AUTHOR_EXCEPTION = "You aren`t author of this news, you can`t delete her.";
    public static final String NEWS_TITLE_EMPTY_EXCEPTION = "News title should be not empty.";
    public static final String NEWS_TEXT_EMPTY_EXCEPTION = "You are creating a news item, can you write a short text?";

    public static final String COMMENT_EMPTY_EXCEPTION = "Comment should be not empty.";
    public static final String COMMENT_DOESNT_EXIST_EXCEPTION = "This comment doesn`t exist";
    public static final String COMMENT_AUTHOR_EXCEPTION = "You aren`t author of this comment, you can`t delete his.";
}
