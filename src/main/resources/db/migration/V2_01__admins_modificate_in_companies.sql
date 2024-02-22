alter table admins
    change username name varchar(100) not null;

alter table admins
    drop column email;

alter table admins
    drop column phone;

alter table admins
    drop column password;

alter table admins
    modify money DECIMAL(10, 2) not null;

alter table admins
    drop column role;

rename table admins to companies;

alter table companies
    modify owner_id int not null;