CREATE TABLE IF NOT EXISTS companies
(
    id    int            NOT NULL AUTO_INCREMENT,
    name  varchar(50)    NOT NULL,
    money decimal(10, 2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
