CREATE TABLE IF NOT EXISTS synchronized_quests
(
    id_first_quest  int NOT NULL,
    id_second_quest int NOT NULL,
    PRIMARY KEY (id_first_quest, id_second_quest),
    KEY id_first_quest_fk (id_first_quest),
    KEY id_second_quest_fk (id_second_quest),
    CONSTRAINT id_first_quest_fk FOREIGN KEY (id_first_quest) REFERENCES quests (id),
    CONSTRAINT id_second_quest_fk FOREIGN KEY (id_second_quest) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;