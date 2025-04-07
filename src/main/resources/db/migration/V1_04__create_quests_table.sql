CREATE TABLE IF NOT EXISTS quests
(
    id          int          NOT NULL AUTO_INCREMENT,
    quest_name  varchar(100) NOT NULL,
    min_persons int          NOT NULL,
    max_persons int          NOT NULL,
    auto_block  time         NOT NULL,
    sms         varchar(70)  DEFAULT NULL,
    slot_list   text         NOT NULL,
    company_id  int          NOT NULL,
    statuses    text         NOT NULL,
    PRIMARY KEY (id),
    KEY quests_company_id_fk (company_id),
    CONSTRAINT quests_company_id_fk FOREIGN KEY (company_id) REFERENCES companies (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;