create table roles (
    id serial primary key,
    name varchar(64)
);

create table users (
    id uuid primary key,
    username varchar(100),
    image_url text,
    email varchar(200),
    phone_number varchar(11),
    status varchar(10),
    last_updated timestamp,
    password varchar(200),
    user_role int not null,
    foreign key(user_role) references roles(id)
);

create table user_friends(
    user_id uuid references users(id),
    friend_id uuid references users(id),
    primary key (user_id, friend_id)
);

create table categories (
    id serial primary key,
    name varchar(64)
);

create table books (
    id serial primary key,
    title varchar(64),
    author varchar(64),
    published_date date,
    page int,
    description varchar(10000),
    image_url text,
    content text,
    recommended_age int,
    book_category int not null,
    foreign key(book_category) references categories(id)
);

create table favorites (
    id uuid primary key,
    user_id uuid not null,
    foreign key(user_id) references users(id)
);

create table favorite_books(
    favorite_id uuid references favorites(id),
    book_id serial references books(id),
    primary key (favorite_id, book_id)
);

create table posts (
    id uuid primary key,
    title varchar(200),
    content_image text,
    content_text varchar(10000),
    created_time timestamp,
    user_post uuid not null,
    foreign key(user_post) references users(id) on delete cascade
);

create table post_sharing (
    id uuid primary key,
    shared_time timestamp,
    user_id uuid,
    post_id uuid,
    shared_to_user_id uuid,
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (post_id) references posts(id) on delete cascade,
    foreign key (shared_to_user_id) references users(id) on delete cascade
);

create table rates (
    id serial primary key,
    rate_value int check (rate_value between 1 and 5),
    created_time timestamp,
    user_rate uuid not null,
    post_rate uuid not null,
    foreign key(user_rate) references users(id),
    foreign key(post_rate) references posts(id)
);

create table chats (
    id uuid primary key,
    created_time timestamp,
    last_access timestamp,
    user1 uuid not null,
    user2 uuid not null,
    foreign key (user1) references users(id),
    foreign key (user2) references users(id)
);

create table messages (
    id uuid primary key,
    text varchar(500),
    created_time timestamp,
    sender_id uuid not null,
    chat_id uuid not null,
    foreign key (sender_id) references users(id),
    foreign key (chat_id) references chats(id)
);

create table comments (
    id int primary key generated by default as identity,
    text varchar(1000),
    parent_id int,
    created_time timestamp,
    user_comment uuid not null,
    post_comment uuid,
    foreign key(user_comment) references users(id),
    foreign key(post_comment) references posts(id)
);

create table user_histories(
    id int primary key generated by default as identity,
    action_detail varchar(500),
    track_time timestamp,
    user_history uuid not null,
    foreign key(user_history) references users(id)
);

create table advertisements(
    id int primary key generated by default as identity,
    title text,
    company_name text,
    company_email text,
    status varchar(10),
    image_url text,
    link_url text,
    start_date timestamp,
    end_date timestamp
);