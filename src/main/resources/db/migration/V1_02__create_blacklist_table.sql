CREATE TABLE IF NOT EXISTS blacklist
(
    id       int                                                           NOT NULL AUTO_INCREMENT,
    phone    varchar(12)                                                      NOT NULL,
    messages varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    admin_id int                                                           NOT NULL,
    PRIMARY KEY (id),
    KEY blacklist_admins_id_fk (admin_id),
    CONSTRAINT `blacklist_admins_id_fk` FOREIGN KEY (admin_id) REFERENCES admins (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;