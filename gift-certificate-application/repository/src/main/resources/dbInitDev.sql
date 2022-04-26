INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('certificate 1', 'description 1', 10.99, 10, '2022-04-10 11:11:11', '2022-04-12 11:11:11'),
       ('certificate 2', 'description 2', 20.99, 10, '2021-10-08 11:11:11', '2022-04-12 11:11:11'),
       ('certificate 3', 'description 3', 30.99, 10, '2021-10-08 11:11:11', '2022-04-12 11:11:11');

INSERT INTO tag (name) VALUES ('first'), ('second'), ('third'), ('fourth');

INSERT INTO tag_gift_certificate (tag_id, gift_certificate_id) VALUES (1, 1), (1, 2), (1, 3), (2, 2), (3, 2);