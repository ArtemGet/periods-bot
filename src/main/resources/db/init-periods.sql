CREATE TABLE periods
(
    id         uuid primary key,
    start_date date not null,
    user_id    uuid not null,
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
)
