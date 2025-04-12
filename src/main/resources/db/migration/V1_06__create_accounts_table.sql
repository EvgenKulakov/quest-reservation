CREATE TABLE IF NOT EXISTS accounts
(
    id         SERIAL PRIMARY KEY,
    login      VARCHAR(255) UNIQUE                                               NOT NULL,
    password   VARCHAR(255)                                                      NOT NULL,
    first_name VARCHAR(255) DEFAULT NULL,
    last_name  VARCHAR(255) DEFAULT NULL,
    phone      VARCHAR(255) DEFAULT NULL,
    role       VARCHAR CHECK (role IN ('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_USER')) NOT NULL,
    company_id INTEGER                                                           NOT NULL,
    CONSTRAINT accounts_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id)
);