CREATE TABLE person(
                       id bigint generated by default as identity primary key,
                       username varchar not null unique,
                       email varchar not null unique,
                       password varchar not null,
                       role varchar not null,
                       account_type varchar not null
);

CREATE TABLE Post(
                     id bigint generated by default as identity primary key,
                     owner_username varchar references person(username) not null,
                     title varchar not null,
                     description varchar not null,
                     views int default 0,
                     photo bytea not null,
                     created_at timestamp default current_timestamp
);

CREATE TABLE Comment(
                        id bigint generated by default as identity primary key,
                        post_id bigint references post(id) on delete cascade not null,
                        person_username varchar references person(username) not null,
                        description text not null,
                        created_at timestamp default current_timestamp
);

CREATE TABLE Photo_like(
                           id bigint generated by default as identity primary key,
                           post_id bigint references post(id) on delete cascade not null,
                           person_username varchar references person(username) not null
);