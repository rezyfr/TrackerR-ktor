alter table category
    add "icon_id" integer;

alter table category
    add constraint category_icon_id_fkey
        foreign key ("icon_id") references icon (id);

