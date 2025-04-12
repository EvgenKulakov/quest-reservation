CREATE TABLE IF NOT EXISTS blacklist
(
    id         SERIAL PRIMARY KEY,
    client_id  INTEGER NOT NULL,
    messages   TEXT    NOT NULL,
    company_id INTEGER NOT NULL,
    CONSTRAINT blacklist_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE INDEX IF NOT EXISTS blacklist_company_id_inx ON blacklist (company_id);