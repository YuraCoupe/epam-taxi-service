/*
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE DATABASE taxi_service
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

initdb -E UTF8

CREATE EXTENSION "uuid-ossp";
*/

CREATE TABLE person(
	id SERIAL PRIMARY KEY,
	phone_number VARCHAR(100) NOT NULL UNIQUE,
	password VARCHAR(200) NOT NULL,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL
	role_id VARCHAR(50) REFERENCES role(id) NOT NULL
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    title varchar(50) NOT NULL UNIQUE
);

CREATE TABLE car_brand (
	id SERIAL PRIMARY KEY,
    title varchar(20) NOT NULL UNIQUE
);

CREATE TABLE car_model (
	id SERIAL PRIMARY KEY,
    brand_id integer REFERENCES car_brand(id) NOT NULL,
    title varchar(20) NOT NULL
);

CREATE TABLE car_category (
	id SERIAL PRIMARY KEY,
    title varchar(20) NOT NULL UNIQUE
);

CREATE TABLE car_status (
	id SERIAL PRIMARY KEY,
    title varchar(20) NOT NULL UNIQUE
);

CREATE TABLE car (
	id SERIAL PRIMARY KEY,
    model_id integer REFERENCES car_model(id) NOT NULL,
    capacity integer NOT NULL,
    category_id integer REFERENCES car_category(id) NOT NULL,
    status_id integer REFERENCES car_status(id) NOT NULL,
    license_plate varchar(20) UNIQUE
    driver_id integer REFERENCES user(id)
    current_trip_id integer REFERENCES trip(id)
);

CREATE TABLE street_type (
id SERIAL PRIMARY KEY,
    title varchar(50) NOT NULL
);

CREATE TABLE street (
	id SERIAL PRIMARY KEY,
	type_id integer REFERENCES street_type (id) NOT NULL,
    title varchar(50) NOT NULL
);

CREATE TABLE address (
	id SERIAL PRIMARY KEY,
    street_id integer REFERENCES street (id) NOT NULL,
    building_number varchar(10) NOT NULL
);

CREATE TABLE trip_status (
	id SERIAL PRIMARY KEY,
    title varchar(20)
);

CREATE TABLE trip (
	id SERIAL PRIMARY KEY,
    perso_id integer REFERENCES person (id) NOT NULL,
    dep_address_id integer REFERENCES address (id) NOT NULL,
    dest_address_id integer REFERENCES address (id) NOT NULL,
    number_of_passengers numeric NOT NULL,
    category_id integer REFERENCES car_category (id) NOT NULL,
    price numeric(2) NOT NULL,
    status_id integer REFERENCES trip_status (id) NOT NULL,
    open_time TIMESTAMP WITH TIME ZONE NOT NULL,
    close_time TIMESTAMP WITH TIME ZONE,
    distance numeric(2) NOT NULL
);

CREATE TABLE trip_car (
	id SERIAL PRIMARY KEY,
    trip_id integer REFERENCES trip (id) NOT NULL,
    car_id integer REFERENCES car (id) NOT NULL
);

CREATE TABLE discount_rate (
	id SERIAL PRIMARY KEY,
    money_spent numeric(2) NOT NULL,
    discount integer NOT NULL
);

CREATE TABLE price_rate (
	id SERIAL PRIMARY KEY,
    category_id integer REFERENCES car_category(id) NOT NULL UNIQUE,
    rate numeric(2) NOT NULL,
    min_order_price numeric(20) NOT NULL
);