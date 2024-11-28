CREATE TABLE IF NOT EXISTS user
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

CREATE TABLE IF NOT EXISTS draw
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
    current_applicants  INT, -- 현재 응모인원 추가
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS notification
(
    id                BIGINT NOT NULL AUTO_INCREMENT,
    notification_time DATETIME(6),
    draw_id           BIGINT,
    status            ENUM ('PENDING', 'COMPLETE'),
    type              ENUM ('BEFORE_START', 'BEFORE_END'),
    PRIMARY KEY (id),
    FOREIGN KEY (draw_id) REFERENCES draw (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS review
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    contents   VARCHAR(255),
    user_id    BIGINT,
    draw_id    BIGINT,
    image      VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (draw_id) REFERENCES draw (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS user_draw
(
    id         BIGINT NOT NULL AUTO_INCREMENT,
    user_id    BIGINT,
    draw_id    BIGINT,
    win        BIT,
    created_at DATETIME(6),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (draw_id) REFERENCES draw (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS favorite_draw
(
    id      BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    draw_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (draw_id) REFERENCES draw (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB;
