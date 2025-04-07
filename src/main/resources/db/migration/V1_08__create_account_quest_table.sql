CREATE TABLE IF NOT EXISTS account_quest
(
    account_id int NOT NULL,
    quest_id   int NOT NULL,
    PRIMARY KEY (account_id, quest_id),
    KEY account_quest_account_id_fk (account_id),
    KEY account_quest_quest_id_fk (quest_id),
    CONSTRAINT user_quest_user_id_fk FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT user_quest_quest_id_fk FOREIGN KEY (quest_id) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;