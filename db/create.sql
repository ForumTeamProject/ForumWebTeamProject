create table roles
(
    role_id int auto_increment
        primary key,
    name    varchar(25) not null,
    constraint name
        unique (name)
);

create table tags
(
    tag_id  int auto_increment
        primary key,
    content varchar(25) not null,
    constraint content
        unique (content)
);

create table users
(
    user_id    int auto_increment
        primary key,
    first_name varchar(32) not null,
    last_name  varchar(32) not null,
    email      varchar(50) not null,
    username   varchar(50) not null,
    password   varchar(50) not null,
    constraint email
        unique (email),
    constraint username
        unique (username)
);

create table phone_numbers
(
    number_id int auto_increment
        primary key,
    user_id   int         not null,
    number    varchar(25) null,
    constraint phone_numbers_users_fk
        foreign key (user_id) references users (user_id)
);

create table posts
(
    post_id       int auto_increment
        primary key,
    user_id       int          not null,
    title         varchar(64)  not null,
    content       text         not null,
    creation_date timestamp    not null,
    photo_url     varchar(100),
    constraint posts_users_fk
        foreign key (user_id) references users (user_id)
);

create table likes
(
    like_id int auto_increment
        primary key,
    post_id int not null,
    user_id int not null,
    constraint likes_posts_fk
        foreign key (post_id) references posts (post_id),
    constraint likes_users_fk
        foreign key (user_id) references users (user_id)
);

create index post_id
    on likes (post_id);

create index user_id
    on likes (user_id);

create index user_id
    on posts (user_id);

create table posts_tags
(
    post_id int not null,
    tag_id  int not null,
    constraint posts_tags_fk
        foreign key (post_id) references posts (post_id),
    constraint tags_posts_fk
        foreign key (tag_id) references tags (tag_id)
);

create index post_id
    on posts_tags (post_id);

create index tag_id
    on posts_tags (tag_id);

create table replies
(
    reply_id int auto_increment
        primary key,
    post_id  int  not null,
    user_id  int  not null,
    content  text not null,
    constraint replies_posts_fk
        foreign key (post_id) references posts (post_id),
    constraint replies_users_fk
        foreign key (user_id) references users (user_id)
);

create index post_id
    on replies (post_id);

create index user_id
    on replies (user_id);

create table roles_users
(
    role_id int not null,
    user_id int not null,
    constraint roles_users_fk1
        foreign key (role_id) references roles (role_id),
    constraint roles_users_fk2
        foreign key (user_id) references users (user_id)
);

