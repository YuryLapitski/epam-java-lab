create table tag
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

create table gift_certificate
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(50)  NOT NULL UNIQUE,
    description      VARCHAR(100) NOT NULL,
    price            DECIMAL(6,2) NOT NULL,
    duration         SMALLINT NOT NULL,
    create_date      DATETIME(3) NOT NULL,
    last_update_date DATETIME(3) NOT NULL
);

create table tag_gift_certificate
(
    tag_id BIGINT NOT NULL,
    gift_certificate_id BIGINT NOT NULL,

    FOREIGN KEY (tag_id) REFERENCES tag (id),
    FOREIGN KEY (gift_certificate_id) REFERENCES tag (id)
);

create table users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    login      VARCHAR(50) NOT NULL UNIQUE

);

create table orders
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT NOT NULL,
    gift_certificate_id BIGINT NOT NULL,
    price               DECIMAL(6, 2)   NOT NULL,
    create_date         DATETIME(3)     NOT NULL,

    FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

