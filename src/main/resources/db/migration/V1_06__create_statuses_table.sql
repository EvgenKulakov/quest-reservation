CREATE TABLE IF NOT EXISTS statuses
(
    id          int                                                                                 NOT NULL AUTO_INCREMENT,
    status_type enum ('BLOCK','MODIFIED','NEW_RESERVE','CANCEL','CONFIRMED','NOT_COME','COMPLETED') NOT NULL,
    text        varchar(20)                                                                         NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

LOCK TABLES `statuses` WRITE;
INSERT INTO `statuses` VALUES (1,'NEW_RESERVE','Новый'),(2,'CANCEL','Отменён'),(3,'CONFIRMED','Подтверждён'),(4,'NOT_COME','Не пришёл'),(5,'COMPLETED','Завершён');
UNLOCK TABLES;