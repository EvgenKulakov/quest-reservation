alter table users
    change username email_login varchar(100) not null;

alter table users
    drop column email;

alter table users
    modify role enum ('ROLE_OWNER', 'ROLE_ADMIN', 'ROLE_USER') null;

alter table users
    change admin_id company_id int not null;

rename table users to accounts;

alter table accounts
    add phone varchar(12) null after last_name;