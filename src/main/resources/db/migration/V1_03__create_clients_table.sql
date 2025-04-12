CREATE TABLE IF NOT EXISTS clients
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255) DEFAULT NULL,
    last_name    VARCHAR(255) DEFAULT NULL,
    phone        VARCHAR(255) DEFAULT NULL,
    email        VARCHAR(255) DEFAULT NULL,
    comments     TEXT         DEFAULT NULL,
    blacklist_id INTEGER      DEFAULT NULL,
    company_id   INTEGER NOT NULL,
    CONSTRAINT clients_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT clients_blacklist_id_fk FOREIGN KEY (blacklist_id) REFERENCES blacklist (id)
);

CREATE INDEX IF NOT EXISTS clients_company_id_inx ON clients (company_id);
CREATE INDEX IF NOT EXISTS clients_blacklist_id_inx ON clients (blacklist_id);

ALTER TABLE blacklist
    ADD CONSTRAINT blacklist_client_id_fk FOREIGN KEY (client_id) REFERENCES clients (id);
CREATE INDEX IF NOT EXISTS blacklist_client_id_inx ON blacklist (client_id);