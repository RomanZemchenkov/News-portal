--liquibase formatted sql

--changeset roman:1
ALTER TABLE category
ADD COLUMN customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE;