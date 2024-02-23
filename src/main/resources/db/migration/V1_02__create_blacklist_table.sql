CREATE TABLE IF NOT EXISTS blacklist
(
    id         int  NOT NULL AUTO_INCREMENT,
    client_id  int  NOT NULL,
    messages   text NOT NULL,
    company_id int  NOT NULL,
    PRIMARY KEY (id),
    KEY blacklist_company_id_fk (company_id),
    CONSTRAINT `blacklist_company_id_fk` FOREIGN KEY (company_id) REFERENCES companies (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;