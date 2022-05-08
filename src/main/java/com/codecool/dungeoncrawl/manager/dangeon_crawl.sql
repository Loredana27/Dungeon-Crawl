create table if not exists game
(
    id          serial
        constraint game_pk
            primary key,
    name        text not null,
    actualMap integer,
    saveDate  date not null
);


create table if not exists player
(
    player_id serial
        constraint player_pk
            primary key,
    name      text    not null,
    posX    integer not null,
    posY    integer not null,
    game_id   integer
        constraint player_game_id_fk
            references game
);


create table if not exists enemy
(
    enemy_id serial
        constraint enemy_pk
            primary key,
    type     text,
    posX   integer not null,
    posY   integer not null,
    game_id  integer
        constraint enemy_game_id_fk
            references game
);



create table if not exists item
(
    item_id serial
        constraint item_pk
            primary key,
    type    text,
    posX integer not null,
    posY  integer not null,
    game_id integer
        constraint item_game_id_fk
            references game
);


create table if not exists available_item
(
    available_item_id serial
        constraint available_item_pk
            primary key,
    type              text,
    posX           integer not null,
    posY           integer not null,
    game_id           integer
        constraint available_item_game_id_fk
            references game
);


