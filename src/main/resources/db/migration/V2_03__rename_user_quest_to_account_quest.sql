alter table user_quest
    change user_id account_id int not null;

rename table user_quest to account_quest;