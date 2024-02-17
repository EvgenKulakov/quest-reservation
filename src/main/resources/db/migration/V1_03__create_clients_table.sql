CREATE TABLE IF NOT EXISTS clients
(
    id           int         NOT NULL AUTO_INCREMENT,
    first_name   varchar(25) NOT NULL,
    last_name    varchar(25) DEFAULT NULL,
    phone        varchar(12) NOT NULL,
    email        varchar(45) DEFAULT NULL,
    blacklist_id int         DEFAULT NULL,
    admin_id     int         NOT NULL,
    PRIMARY KEY (id),
    KEY admin_id (admin_id),
    KEY black_list_id (blacklist_id),
    CONSTRAINT clients_ibfk_1 FOREIGN KEY (admin_id) REFERENCES admins (id),
    CONSTRAINT clients_ibfk_2 FOREIGN KEY (blacklist_id) REFERENCES blacklist (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;