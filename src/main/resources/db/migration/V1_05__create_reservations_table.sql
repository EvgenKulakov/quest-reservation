CREATE TABLE IF NOT EXISTS reservations
(
    id                int                                                           NOT NULL AUTO_INCREMENT,
    date_reserve      date                                                          NOT NULL,
    time_reserve      time                                                          NOT NULL,
    time_created      datetime                                                      DEFAULT NULL,
    time_last_change  datetime                                                      DEFAULT NULL,
    changed_slot_time time                                                          DEFAULT NULL,
    quest_id          int                                                           NOT NULL,
    status_type       varchar(20)                                                   NOT NULL,
    source_reserve    varchar(45)                                                   NOT NULL,
    changed_price     int                                                           DEFAULT NULL,
    client_id         int                                                           DEFAULT NULL,
    count_persons     int                                                       DEFAULT NULL,
    admin_comment     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
    client_comment    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
    history_messages  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    admin_id          int                                                           NOT NULL,
    PRIMARY KEY (id),
    KEY client_id (client_id),
    KEY reservations_quests_id_fk (quest_id),
    KEY reservations_admins_id_fk (admin_id),
    CONSTRAINT reservations_admins_id_fk FOREIGN KEY (admin_id) REFERENCES admins (id),
    CONSTRAINT reservations_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT reservations_quests_id_fk FOREIGN KEY (quest_id) REFERENCES quests (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;