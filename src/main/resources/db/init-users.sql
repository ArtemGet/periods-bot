CREATE TABLE public.users
(
    id   uuid primary key,
    t_id bigint unique not null,
    name varchar
)
