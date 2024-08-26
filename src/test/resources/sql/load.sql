INSERT INTO customer(username, password)
VALUES ('Kollega1484', 'passworD4'),
       ('Izumi', 'passworD4'),
       ('DungeonMaster', 'passworD4');

INSERT INTO customer_info(id, firstname, lastname, birthday)
VALUES (1, 'Roman', 'Zemchenkov', '2000-02-02'),
       (2, 'Kristina', 'Bashirova', '2000-02-14'),
       (3, 'Petya', 'Kvashin', '1999-01-31');

INSERT INTO category(title, customer_id)
VALUES ('IT', 1),
       ('Animal', 1);

INSERT INTO news(title, text, category_id, customer_id)
VALUES ('Test IT news', 'Something text', (SELECT c.id FROM category c WHERE c.title = 'IT'), 1),
       ('Test animal news', 'Something text', (SELECT c.id FROM category c WHERE c.title = 'Animal'), 1),
       ('Computers', 'Something text', (SELECT c.id FROM category c WHERE c.title = 'IT'), 2);

INSERT INTO comment(customer_id, text, news_id)
VALUES (1, 'Something comment', 1),
       (1, 'One more comment', 1),
       (1, 'One more comment', 2),
       (2, 'Something comment', 2),
       (2, 'Something comment', 2),
       (2, 'Something comment', 3),
       (3, 'One more comment', 1),
       (3, 'One more comment', 3);

