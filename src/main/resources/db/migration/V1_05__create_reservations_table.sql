CREATE TABLE IF NOT EXISTS reservations
(
    id                SERIAL8 PRIMARY KEY,
    date_reserve      DATE          NOT NULL,
    time_reserve      TIME          NOT NULL,
    time_created      TIMESTAMP     NOT NULL,
    time_last_change  TIMESTAMP     DEFAULT NULL,
    changed_slot_time TIME          DEFAULT NULL,
    quest_id          INTEGER       NOT NULL,
    status_type       VARCHAR(255)  NOT NULL,
    source_reserve    VARCHAR(255)  NOT NULL,
    price             DECIMAL(8, 2) DEFAULT NULL,
    changed_price     DECIMAL(8, 2) DEFAULT NULL,
    client_id         INTEGER       DEFAULT NULL,
    count_persons     INTEGER       DEFAULT NULL,
    admin_comment     VARCHAR(500)  DEFAULT NULL,
    client_comment    VARCHAR(500)  DEFAULT NULL,
    history_messages  TEXT          NOT NULL,
    CONSTRAINT reservations_client_id_fk FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT reservations_quests_id_fk FOREIGN KEY (quest_id) REFERENCES quests (id)
);

CREATE INDEX IF NOT EXISTS reservations_client_id_inx ON reservations (client_id);
CREATE INDEX IF NOT EXISTS reservations_quests_id_inx ON reservations (quest_id);