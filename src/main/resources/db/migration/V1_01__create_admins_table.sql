CREATE TABLE IF NOT EXISTS admins
(
    id       int                             NOT NULL AUTO_INCREMENT,
    username varchar(50)                     NOT NULL,
    email    varchar(50)                     NOT NULL,
    phone    varchar(12) DEFAULT NULL,
    password varchar(68)                     NOT NULL,
    money    int                             NOT NULL,
    role     enum ('ROLE_ADMIN','ROLE_USER') NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
