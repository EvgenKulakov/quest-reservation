CREATE TABLE IF NOT EXISTS reservations
(
    id                int8          NOT NULL AUTO_INCREMENT,
    date_reserve      date          NOT NULL,
    time_reserve      time          NOT NULL,
    time_created      datetime      NOT NULL,
    time_last_change  datetime      DEFAULT NULL,
    changed_slot_time time          DEFAULT NULL,
    quest_id          int           NOT NULL,
    status_type       varchar(20)   NOT NULL,
    source_reserve    varchar(45)   NOT NULL,
    price             decimal(8, 2) DEFAULT NULL,
    changed_price     decimal(8, 2) DEFAULT NULL,
    client_id         int           DEFAULT NULL,
    count_persons     int           DEFAULT NULL,
    admin_comment     varchar(200)  DEFAULT NULL,
    client_comment    varchar(200)  DEFAULT NULL,
    history_messages  text          NOT NULL,
    PRIMARY KEY (id),
    KEY reservations_client_id_fk (client_id),
    KEY reservations_quests_id_fk (quest_id),
    CONSTRAINT reservations_client_id_fk FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT reservations_quests_id_fk FOREIGN KEY (quest_id) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;