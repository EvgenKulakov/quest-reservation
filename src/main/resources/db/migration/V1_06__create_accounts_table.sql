CREATE TABLE IF NOT EXISTS accounts
(
    id          int                                           NOT NULL AUTO_INCREMENT,
    login       varchar(100) UNIQUE                           NOT NULL,
    password    varchar(68)                                   NOT NULL,
    first_name  varchar(25)  DEFAULT NULL,
    last_name   varchar(30)  DEFAULT NULL,
    phone       varchar(12)  DEFAULT NULL,
    role        enum ('ROLE_OWNER', 'ROLE_ADMIN','ROLE_USER') NOT NULL,
    company_id  int                                           NOT NULL,
    PRIMARY KEY (id),
    KEY accounts_company_id_fk (company_id),
    CONSTRAINT accounts_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;