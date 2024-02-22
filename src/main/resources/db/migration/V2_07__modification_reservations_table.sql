alter table reservations
    modify id INT8 auto_increment;

alter table reservations
    change admin_id company_id int not null;