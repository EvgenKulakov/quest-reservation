alter table blacklist
    add client_id int not null after id;

alter table blacklist
    drop column phone;

alter table blacklist
    change admin_id company_id int not null;

alter table blacklist
    add constraint blacklist_clients_id_fk
        foreign key (client_id) references clients (id);

alter table blacklist
    add constraint blacklist_companies_id_fk
        foreign key (company_id) references companies (id);