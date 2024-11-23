CREATE TABLE user
(
    id           BIGINT NOT NULL AUTO_INCREMENT,
    created_at   DATETIME(6),
    updated_at   DATETIME(6),
    email        VARCHAR(255) UNIQUE,
    nickname     VARCHAR(255),
    password     VARCHAR(255),
    phone_number VARCHAR(255),
    user_role    ENUM ('ADMIN', 'USER'),
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE draw
(
    id                  BIGINT NOT NULL AUTO_INCREMENT,
    created_at          DATETIME(6),
    updated_at          DATETIME(6),
    draw_type           ENUM ('FIRST_COME', 'PERIOD'),
    end_time            DATETIME(6),
    product_description VARCHAR(255),
    product_image       VARCHAR(255),
    product_name        VARCHAR(255),
    result_time         DATETIME(6),
    start_time          DATETIME(6),
    total_winner        INT,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE review
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    contents   VARCHAR(255),
    user_id    BIGINT,
    draw_id    BIGINT,
    image      VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (draw_id) REFERENCES draw (id)
) ENGINE = InnoDB;

CREATE TABLE user_draw
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    user_id    BIGINT,
    draw_id    BIGINT,
    win        BIT,
    created_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (draw_id) REFERENCES draw (id)
) ENGINE = InnoDB;

CREATE TABLE favorite_draw
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    draw_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (draw_id) REFERENCES draw (id)
) ENGINE = InnoDB;