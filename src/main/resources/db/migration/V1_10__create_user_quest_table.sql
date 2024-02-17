CREATE TABLE IF NOT EXISTS user_quest
(
    user_id  int NOT NULL,
    quest_id int NOT NULL,
    PRIMARY KEY (user_id, quest_id),
    KEY quest_id (quest_id),
    CONSTRAINT user_quest_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT user_quest_ibfk_2 FOREIGN KEY (quest_id) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;