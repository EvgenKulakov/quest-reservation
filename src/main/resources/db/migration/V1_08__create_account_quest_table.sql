CREATE TABLE IF NOT EXISTS account_quest
(
    account_id INTEGER NOT NULL,
    quest_id   INTEGER NOT NULL,
    PRIMARY KEY (account_id, quest_id),
    CONSTRAINT user_quest_user_id_fk FOREIGN KEY (account_id) REFERENCES accounts (id),
    CONSTRAINT user_quest_quest_id_fk FOREIGN KEY (quest_id) REFERENCES quests (id)
);