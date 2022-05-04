create table user_role
(
    id        serial       not null primary key,
    role_name varchar(128) not null unique,
    priority  int          not null
);
comment on table user_role is 'User role information';
comment on column user_role.id is 'Role id';
comment on column user_role.role_name is 'Role name';
comment on column user_role.priority is 'Role priority';