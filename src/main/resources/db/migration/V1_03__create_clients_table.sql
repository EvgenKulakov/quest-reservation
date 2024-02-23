CREATE TABLE IF NOT EXISTS clients
(
    id           int NOT NULL AUTO_INCREMENT,
    first_name   varchar(25) DEFAULT NULL,
    last_name    varchar(25) DEFAULT NULL,
    phone        varchar(12) DEFAULT NULL,
    email        varchar(45) DEFAULT NULL,
    blacklist_id int         DEFAULT NULL,
    company_id   int NOT NULL,
    PRIMARY KEY (id),
    KEY clients_company_id_fk (company_id),
    KEY clients_blacklist_id_fk (blacklist_id),
    CONSTRAINT clients_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT clients_blacklist_id_fk FOREIGN KEY (blacklist_id) REFERENCES blacklist (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE blacklist
    ADD INDEX blacklist_client_id_fk (client_id);

ALTER TABLE blacklist
    ADD CONSTRAINT blacklist_client_id_fk FOREIGN KEY (client_id) REFERENCES clients (id);
