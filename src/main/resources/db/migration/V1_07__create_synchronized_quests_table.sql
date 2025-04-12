CREATE TABLE IF NOT EXISTS synchronized_quests
(
    id_first_quest  INTEGER NOT NULL,
    id_second_quest INTEGER NOT NULL,
    PRIMARY KEY (id_first_quest, id_second_quest),
    CONSTRAINT id_first_quest_fk FOREIGN KEY (id_first_quest) REFERENCES quests (id),
    CONSTRAINT id_second_quest_fk FOREIGN KEY (id_second_quest) REFERENCES quests (id)
);