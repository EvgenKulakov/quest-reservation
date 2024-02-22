CREATE TABLE IF NOT EXISTS quests
(
    id          int                                                          NOT NULL AUTO_INCREMENT,
    quest_name  varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    min_persons int                                                      NOT NULL,
    max_persons int                                                      NOT NULL,
    auto_block  time                                                         NOT NULL,
    sms         varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
    slot_list   varchar(10000)                                               NOT NULL,
    admin_id    int                                                          NOT NULL,
    PRIMARY KEY (id),
    KEY admin_id (admin_id),
    CONSTRAINT quests_ibfk_1 FOREIGN KEY (admin_id) REFERENCES admins (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;