CREATE TABLE profile_tab(
    id serial,
    username varchar(50) NOT NULL,
    password varchar(100) NOT NULL,
    firstname varchar(30) NOT NULL,
    lastname varchar(30) NOT NULL,
    email varchar(30) NOT NULL,

    PRIMARY KEY (id)
 );

CREATE TABLE vehicle_tab(
    id serial,
    title varchar(30) NOT NULL,
    type varchar(30) NOT NULL,
    generation int NOT NULL,

    profile_id bigint NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (profile_id) REFERENCES profile_tab(id)
);

CREATE TABLE role_tab(
    id serial,
    name varchar(30) NOT NULL,

    PRIMARY KEY (id)
);

INSERT INTO role_tab (id,name) VALUES (1,'USER');
INSERT INTO role_tab (id,name) VALUES (2,'ADMIN');

CREATE TABLE users_roles(
    users_id bigint NOT NULL,
    roles_id bigint NOT NULL,
    FOREIGN KEY (users_id) REFERENCES profile_tab(id),
    FOREIGN KEY (roles_id) REFERENCES role_tab(id)
);

CREATE TABLE device_tab(
    id serial,
    title varchar(100) NOT NULL,
    attribute varchar(30) NOT NULL,
    description text NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE vehicles_devices(
    vehicle_id bigint NOT NULL,
    device_id bigint NOT NULL,

    FOREIGN KEY (vehicle_id) REFERENCES vehicle_tab(id),
    FOREIGN KEY (device_id) REFERENCES device_tab(id)
);

CREATE TABLE IoT_tab(
    id serial,
    thing_name varchar(100) NOT NULL,
    thing_attribute varchar(100) NOT NULL,
    vehicle_id bigint NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle_tab(id)
);

