-- noinspection SqlWithoutWhereForFile
DELETE FROM companies;
DELETE FROM clients;
DELETE FROM quests;
DELETE FROM reservations;
DELETE FROM accounts;
DELETE FROM account_quest;

INSERT INTO companies (id, name, money)
VALUES (1, 'test company', 10000.00);

INSERT INTO clients (id, first_name, last_name, phone, email, comments, blacklist_id, company_id)
VALUES
(1, 'TestName_client', 'TestSurname_client', '+79995201511', '', null, null, 1),
(2, 'TestName_client', 'TestSurname_client', '+79995201511', '', null, null, 1),
(3, 'TestName_client', 'TestSurname_client', '+79995201511', '', null, null, 1),
(4, 'TestName_client', 'TestSurname_client', '+79995201511', '', null, null, 1),
(5, 'TestName_client', 'TestSurname_client', '+79995201511', '', null, null, 1),
(6, 'TestName_client', 'TestSurname_client', '+79995201511', '', null, null, 1);

-- Костыль для корректной работы H2 после hibernate.ddl-auto: create-drop
ALTER TABLE quests ALTER COLUMN slot_list TYPE TEXT USING slot_list::TEXT;

INSERT INTO quests (id, quest_name, min_persons, max_persons, auto_block, sms, slot_list, company_id, statuses)
VALUES (1, 'Quest One', 1, 6, '00:00:00', null, e'{
  "monday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
  "tuesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
  "wednesday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
  "thursday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
  "friday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
  "saturday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ],
  "sunday" : [ {"time" : "12:00", "price" : 3000}, {"time" : "13:00", "price" : 3000}, {"time" : "14:00", "price" : 3000}, {"time" : "15:00", "price" : 3000}, {"time" : "16:00", "price" : 3000}, {"time" : "17:00", "price" : 3000}, {"time" : "18:00", "price" : 3000}, {"time" : "19:00", "price" : 3000}, {"time" : "20:00", "price" : 3000}, {"time" : "21:00", "price" : 3000} ]
}', 1, 'NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED');

INSERT INTO quests (id, quest_name, min_persons, max_persons, auto_block, sms, slot_list, company_id, statuses)
VALUES (2, 'Quest Two', 1, 5, '00:00:00', null, e'{
  "monday" : [ {"time" : "12:30","price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
  "tuesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
  "wednesday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
  "thursday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
  "friday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
  "saturday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ],
  "sunday" : [ {"time" : "12:30", "price" : 1500}, {"time" : "14:00", "price" : 1500}, {"time" : "16:30", "price" : 1500}, {"time" : "18:30", "price" : 1500}, {"time" : "20:00", "price" : 1500}, {"time" : "22:00", "price" : 1500} ]
}', 1, 'NEW_RESERVE,CANCEL,CONFIRMED,NOT_COME,COMPLETED');

INSERT INTO reservations (id, date_reserve, time_reserve, time_created, time_last_change, changed_slot_time, quest_id, status_type, source_reserve, price, changed_price, client_id, count_persons, admin_comment, client_comment, history_messages)
VALUES
(1, '2025-04-21', '16:00:00', '2025-04-21 00:29:51.003024', '2025-04-21 00:29:51.006025', null, 1, 'CONFIRMED', 'default', 3000.00, null, 1, 1, '', null, 'default'),
(2, '2025-04-21', '17:00:00', '2025-04-21 00:29:53.345590', null, null, 1, 'BLOCK', 'default', null, null, null, null, null, null, 'default'),
(3, '2025-04-22', '17:00:00', '2025-04-21 00:30:51.116089', '2025-04-21 00:30:51.119760', null, 1, 'NOT_COME', 'default', 3000.00, null, 2, 1, '', null, 'default'),
(4, '2025-04-21', '12:00:00', '2025-04-21 00:31:02.138441', '2025-04-21 00:31:02.146137', null, 1, 'CANCEL', 'default', 3000.00, null, 3, 1, '', null, 'default'),
(5, '2025-04-22', '14:00:00', '2025-04-21 00:55:53.354729', '2025-04-21 00:55:53.398694', null, 1, 'CONFIRMED', 'default', 3000.00, null, 4, 1, '', null, 'default'),
(6, '2025-04-22', '21:00:00', '2025-04-21 00:56:03.752728', '2025-04-21 00:56:03.757734', null, 1, 'NOT_COME', 'default', 3000.00, null, 5, 1, '', null, 'default'),
(7, '2025-04-21', '16:30:00', '2025-04-21 00:58:38.605035', null, null, 2, 'BLOCK', 'default', null, null, null, null, null, null, 'default'),
(8, '2025-04-21', '18:30:00', '2025-04-21 00:58:45.820886', '2025-04-21 00:58:50.315852', null, 2, 'COMPLETED', 'default', 1500.00, null, 6, 3, '', null, 'default');

INSERT INTO accounts (id, login, password, first_name, last_name, phone, role, company_id)
VALUES
(1, 'admin@gmail.com', '$2a$10$I6WnbfYRb2Z8uBysTKy5l.uSazvJYhqFgsj4LQ.5vZc65TmGlcat6', 'Test', 'Евгений', '+79995554433', 'ROLE_OWNER', 1),
(2, 'second@gmail.com', '$2a$10$l0VKw9rNsW.z609bimtWEOyjrVSYWf8Lskriij08nAyS1PqLNfnxq', 'second', 'second', null, 'ROLE_ADMIN', 1),
(3, 'third@gmail.com', '$2a$10$CIkKUYln9NlUsaMS2ODTkOVR7HyoszJm/DWAIf9MFSW14HqqHDvw6', 'second', 'third', null, 'ROLE_USER', 1);

INSERT INTO account_quest (account_id, quest_id)
VALUES
(1, 1),
(2, 1),
(3, 1),
(2, 2),
(1, 2),
(3, 2);





