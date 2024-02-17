CREATE TABLE IF NOT EXISTS status_quest
(
    status_id int NOT NULL,
    quest_id  int NOT NULL,
    PRIMARY KEY (status_id, quest_id),
    KEY quest_id (quest_id),
    CONSTRAINT status_quest_ibfk_1 FOREIGN KEY (status_id) REFERENCES statuses (id),
    CONSTRAINT status_quest_ibfk_2 FOREIGN KEY (quest_id) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;