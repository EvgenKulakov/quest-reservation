CREATE TABLE IF NOT EXISTS users
(
    id         int                             NOT NULL AUTO_INCREMENT,
    username   varchar(50)                     NOT NULL,
    first_name varchar(30) DEFAULT NULL,
    last_name  varchar(30) DEFAULT NULL,
    password   varchar(68)                     NOT NULL,
    email      varchar(50)                     NOT NULL,
    role       enum ('ROLE_ADMIN','ROLE_USER') NOT NULL,
    admin_id   int                             NOT NULL,
    PRIMARY KEY (id),
    KEY admin_id (admin_id),
    CONSTRAINT users_ibfk_1 FOREIGN KEY (admin_id) REFERENCES admins (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;