alter table wallet
    alter column icon type int8 using icon::int8;

alter table wallet
    add constraint wallet_icon_id__fkey
        foreign key (icon) references icon (id);

