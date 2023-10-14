create table users
(
    user_id    int auto_increment primary key,
    first_name varchar(32) not null,
    last_name  varchar(32) not null,
    email      varchar(50) not null unique,
    username   varchar(50) not null unique,
    password   varchar(50) not null
);
create table posts
(
    post_id int auto_increment primary key,
    user_id int         not null references users (user_id),
    title   varchar(64) not null,
    content text        not null
);
create table replies
(
    reply_id int auto_increment primary key,
    post_id  int  not null references posts (post_id),
    user_id  int  not null references users (user_id),
    content  text not null
);
create table likes
(
    like_id int auto_increment primary key,
    post_id int not null references posts (post_id),
    user_id int not null references users (user_id)
);
create table admins
(
    admin_id     int auto_increment primary key,
    user_id      int not null references users (user_id),
    phone_number varchar(15) unique
);
create table tags
(
    tag_id  int auto_increment primary key,
    content varchar(25) not null unique
);
create table posts_tags
(
    post_id int not null references posts (post_id),
    tag_id  int not null references tags (tag_id)
);