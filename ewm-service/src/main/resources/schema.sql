create table if not exists category
(
    id   bigint generated by default as identity
        primary key,
    name varchar(255)
);

create unique index if not exists category_name_unique_index
    on category (name);

create table if not exists compilation
(
    id     bigint generated by default as identity
        primary key,
    pinned boolean not null,
    title  varchar(500)
);

create table if not exists location
(
    id  bigint generated by default as identity
        primary key,
    lat real,
    lon real
);

create table if not exists users
(
    id    bigint generated by default as identity
        primary key,
    email varchar(255),
    name  varchar(255)
);

create unique index if not exists user_name_unique_index on users(name);

create table if not exists event
(
    id                 bigint generated by default as identity
        primary key,
    annotation         varchar(2000),
    created_on         timestamp,
    description        varchar(7000),
    event_date         timestamp,
    paid               boolean not null,
    participant_limit  integer,
    published_on       timestamp,
    request_moderation boolean,
    state              varchar(255),
    title              varchar(255),
    category           bigint
        constraint category_fk
            references category,
    initiator          bigint
        constraint users_fk
            references users,
    location           bigint
        constraint location_fk
            references location
);

create table if not exists compilations_events
(
    compilation_id bigint not null
        constraint compilation_fk
            references compilation,
    event_id       bigint not null
        constraint event_fk
            references event
);

create table if not exists requests
(
    id        bigint generated by default as identity
        primary key,
    created   timestamp,
    status    varchar(255),
    event     bigint
        constraint request_event_fk
            references event,
    requester bigint
        constraint request_users_fk
            references users
);

