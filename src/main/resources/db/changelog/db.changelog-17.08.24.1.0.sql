--liquibase formatted sql

--changeset roman:1
CREATE TABLE customer
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL CHECK ( length(password) >= 8 )
);

--changeset roman:2
CREATE TABLE customer_info
(
    id BIGINT PRIMARY KEY REFERENCES customer(id) ON DELETE CASCADE,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    birthday DATE
);

--changeset roman:3
CREATE TABLE category
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) UNIQUE NOT NULL
);

--changeset roman:4
CREATE TABLE news
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) UNIQUE NOT NULL,
    text TEXT NOT NULL,
    category_id BIGINT REFERENCES category(id) ON DELETE SET NULL,
    customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE
);

--changeset roman:5
CREATE TABLE comment
(
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    news_id BIGINT REFERENCES news(id) ON DELETE CASCADE
);