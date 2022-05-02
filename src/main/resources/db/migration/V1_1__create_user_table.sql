create table users
(
    id        bigserial    not null primary key,
    user_name varchar(128) not null unique,
    password  varchar(128) not null,
    role      varchar(128) not null

);
comment on table users is 'User information';
comment on column users.id is 'User id';
comment on column users.user_name is 'User name';
comment on column users.password is 'User password';
comment on column users.role is 'User role';
