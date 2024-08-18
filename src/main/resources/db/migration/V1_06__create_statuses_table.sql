CREATE TABLE IF NOT EXISTS statuses
(
    id          int         NOT NULL AUTO_INCREMENT,
    status_type enum (
        'EMPTY',
        'BLOCK',
        'MODIFIED',
        'NEW_RESERVE',
        'CANCEL',
        'CONFIRMED',
        'NOT_COME',
        'COMPLETED')        NOT NULL,
    text        varchar(50) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

LOCK TABLES `statuses` WRITE;
INSERT INTO `statuses`
VALUES (1, 'EMPTY', 'Без бронирования'),
       (2, 'NEW_RESERVE', 'Новый'),
       (3, 'CANCEL', 'Отменён'),
       (4, 'CONFIRMED', 'Подтверждён'),
       (5, 'NOT_COME', 'Не пришёл'),
       (6, 'COMPLETED', 'Завершён');
UNLOCK TABLES;