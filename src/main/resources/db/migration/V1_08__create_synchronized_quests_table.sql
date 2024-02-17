CREATE TABLE IF NOT EXISTS synchronized_quests
(
    id_first_quest  int NOT NULL,
    id_second_quest int NOT NULL,
    PRIMARY KEY (id_first_quest, id_second_quest),
    KEY id_second_quest (id_second_quest),
    CONSTRAINT synchronized_quests_ibfk_1 FOREIGN KEY (id_first_quest) REFERENCES quests (id),
    CONSTRAINT synchronized_quests_ibfk_2 FOREIGN KEY (id_second_quest) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;