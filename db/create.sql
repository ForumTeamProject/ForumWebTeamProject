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
    photo_url  longtext null,
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
    photo_url     longtext null,
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

INSERT INTO tags (content) VALUES
                               ('Tag Sample 1'),
                               ('Tag Sample 2'),
                               ('Tag Sample 3'),
                               ('Tag Sample 4'),
                               ('Tag Sample 5'),
                               ('Tag Sample 6'),
                               ('Tag Sample 7'),
                               ('Tag Sample 8'),
                               ('Tag Sample 9'),
                               ('Tag Sample 10');

INSERT INTO users (first_name, last_name, email, username, password, photo_url) VALUES
                                                                                    ('John', 'Doe', 'john@example.com', 'johndoe', 'password', 'default-icon.png'),
                                                                                    ('Alice', 'Smith', 'alice@example.com', 'alicesmith', 'password', 'default-icon.png'),
                                                                                    ('Bob', 'Johnson', 'bob@example.com', 'bobjohnson', 'password', 'default-icon.png'),
                                                                                    ('Emily', 'Brown', 'emily@example.com', 'emilybrown', 'password', 'default-icon.png'),
                                                                                    ('David', 'Lee', 'david@example.com', 'davidlee', 'password', 'default-icon.png'),
                                                                                    ('Sophia', 'Garcia', 'sophia@example.com', 'sophiagarcia', 'password', 'default-icon.png'),
                                                                                    ('Oliver', 'Martinez', 'oliver@example.com', 'olivermartinez', 'password', 'default-icon.png'),
                                                                                    ('Emma', 'Lopez', 'emma@example.com', 'emmalopez', 'password', 'default-icon.png'),
                                                                                    ('Mia', 'Perez', 'mia@example.com', 'miaperez', 'password', 'default-icon.png'),
                                                                                    ('Ethan', 'Gonzalez', 'ethan@example.com', 'ethangonzalez', 'password', 'default-icon.png');

-- Assuming each user has a phone number
INSERT INTO phone_numbers (user_id, number) VALUES
                                                (1, '1234567890'),
                                                (2, '2345678901'),
                                                (3, '3456789012'),
                                                (4, '4567890123'),
                                                (5, '5678901234'),
                                                (6, '6789012345'),
                                                (7, '7890123456'),
                                                (8, '8901234567'),
                                                (9, '9012345678'),
                                                (10, '0123456789');

-- Assuming each user has a post
INSERT INTO posts (user_id, title, content, creation_date, photo_url) VALUES
                                                                          (1, 'First Post', 'This is the content of the first post.', NOW(), 'default-icon.jpg'),
                                                                          (2, 'Second Post', 'This is the content of the second post.', NOW(), 'default-icon.jpg'),
                                                                          (3, 'Third Post', 'This is the content of the third post.', NOW(), 'default-icon.jpg'),
                                                                          (4, 'Fourth Post', 'This is the content of the fourth post.', NOW(), 'default-icon.jpg'),
                                                                          (5, 'Fifth Post', 'This is the content of the fifth post.', NOW(), 'default-icon.jpg'),
                                                                          (6, 'Sixth Post', 'This is the content of the sixth post.', NOW(), 'default-icon.jpg'),
                                                                          (7, 'Seventh Post', 'This is the content of the seventh post.', NOW(), 'default-icon.jpg'),
                                                                          (8, 'Eighth Post', 'This is the content of the eighth post.', NOW(), 'default-icon.jpg'),
                                                                          (9, 'Ninth Post', 'This is the content of the ninth post.', NOW(), 'default-icon.jpg'),
                                                                          (10, 'Tenth Post', 'This is the content of the tenth post.', NOW(), 'default-icon.jpg');

-- Assuming users like posts
INSERT INTO likes (post_id, user_id) VALUES
                                         (1, 2), -- User 2 likes Post 1
                                         (2, 3), -- User 3 likes Post 2
                                         (3, 4), -- User 4 likes Post 3
                                         (4, 5), -- User 5 likes Post 4
                                         (5, 6), -- User 6 likes Post 5
                                         (6, 7), -- User 7 likes Post 6
                                         (7, 8), -- User 8 likes Post 7
                                         (8, 9), -- User 9 likes Post 8
                                         (9, 10), -- User 10 likes Post 9
                                         (10, 1); -- User 1 likes Post 10

-- Assuming posts have multiple tags
INSERT INTO posts_tags (post_id, tag_id) VALUES
                                             (1, 2), -- Post 1 is tagged with Tag 2
                                             (2, 3), -- Post 2 is tagged with Tag 3
                                             (3, 4), -- Post 3 is tagged with Tag 4
                                             (4, 5), -- Post 4 is tagged with Tag 5
                                             (5, 6), -- Post 5 is tagged with Tag 6
                                             (6, 7), -- Post 6 is tagged with Tag 7
                                             (7, 8), -- Post 7 is tagged with Tag 8
                                             (8, 9), -- Post 8 is tagged with Tag 9
                                             (9, 10), -- Post 9 is tagged with Tag 10
                                             (10, 1); -- Post 10 is tagged with Tag 1

-- Assuming users reply to posts
INSERT INTO replies (post_id, user_id, content) VALUES
                                                    (1, 3, 'Reply content for Post 1 by User 3'),
                                                    (2, 4, 'Reply content for Post 2 by User 4'),
                                                    (3, 5, 'Reply content for Post 3 by User 5'),
                                                    (4, 6, 'Reply content for Post 4 by User 6'),
                                                    (5, 7, 'Reply content for Post 5 by User 7'),
                                                    (6, 8, 'Reply content for Post 6 by User 8'),
                                                    (7, 9, 'Reply content for Post 7 by User 9'),
                                                    (8, 10, 'Reply content for Post 8 by User 10'),
                                                    (9, 1, 'Reply content for Post 9 by User 1'),
                                                    (10, 2, 'Reply content for Post 10 by User 2');

INSERT INTO roles (role_id, name) VALUES
                                      (1, 'admin'),
                                      (2, 'blockedUser'),
                                      (3, 'user');

-- Assuming users have roles
INSERT INTO roles_users (role_id, user_id) VALUES
                                               (1, 1), -- User 1 has the role of 'admin'
                                               (2, 2), -- User 2 has the role of 'user'
                                               (3, 3), -- User 3 has the role of 'blockedUser'
                                               (2, 4), -- User 4 has the role of 'user'
                                               (2, 5), -- User 5 has the role of 'user'
                                               (2, 6), -- User 6 has the role of 'user'
                                               (2, 7), -- User 7 has the role of 'user'
                                               (2, 8), -- User 8 has the role of 'user'
                                               (2, 9), -- User 9 has the role of 'user'
                                               (2, 10); -- User 10 has the role of 'user'