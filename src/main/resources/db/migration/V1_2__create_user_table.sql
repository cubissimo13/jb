create table users
(
    id        bigserial    not null primary key,
    user_name varchar(128) not null unique,
    password  varchar(128) not null,
    role_id   int          not null,
    constraint role_id_fk foreign key (role_id) references user_role(id)
);
comment on table users is 'User information';
comment on column users.id is 'User id';
comment on column users.user_name is 'User name';
comment on column users.password is 'User password';
comment on column users.role_id is 'User role';
