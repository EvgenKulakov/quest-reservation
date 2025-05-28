CREATE TABLE IF NOT EXISTS quests
(
    id          SERIAL PRIMARY KEY,
    quest_name  VARCHAR(255)        NOT NULL,
    min_persons INTEGER             NOT NULL,
    max_persons INTEGER             NOT NULL,
    auto_block  TIME                NOT NULL,
    sms         VARCHAR(70)         DEFAULT NULL,
    slot_list   TEXT                NOT NULL,
    company_id  INTEGER             NOT NULL,
    statuses    VARCHAR(50) ARRAY   NOT NULL,
    CONSTRAINT quests_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id)
);