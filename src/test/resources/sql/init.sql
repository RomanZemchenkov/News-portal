DROP TABLE IF EXISTS customer_info;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL CHECK ( length(password) >= 8 )
);

CREATE TABLE customer_info
(
    id BIGINT PRIMARY KEY REFERENCES customer(id) ON DELETE CASCADE,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    birthday DATE
);

CREATE TABLE category
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) UNIQUE NOT NULL,
    customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE news
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(128) UNIQUE NOT NULL,
    text TEXT NOT NULL,
    category_id BIGINT REFERENCES category(id) ON DELETE SET NULL,
    customer_id BIGINT REFERENCES customer(id) ON DELETE SET NULL
);

CREATE TABLE comment
(
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    news_id BIGINT REFERENCES news(id) ON DELETE CASCADE
);