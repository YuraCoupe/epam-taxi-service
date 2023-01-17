INSERT INTO role (title)
VALUES
('ROLE_ADMINISTRATOR'),
('ROLE_CLIENT'),
('ROLE_DRIVER');

INSERT INTO trip_status (title)
VALUES
('Open'),
('Confirmed'),
('Processing'),
('Canceled'),
('Completed');

INSERT INTO car_status (title)
VALUES
('available for order'),
('on route'),
('inactive');

INSERT INTO street_type (title)
VALUES
('street'),
('avenue'),
('boulevard'),
('square'),
('lane');

/*INSERT INTO street (type_id, title)
VALUES
('Chervonoi kalyny'),
('Balzaka'),
('Kashtanova');
*/

INSERT INTO street (type_id, title)
SELECT t.id, st.title
FROM  (
   VALUES
      ('avenue',  'Chervonoi kalyny')
    , ('street', 'Balzaka')
    , ('street', 'Kashtanova')
  ) AS st(id, title)
JOIN   street_type t ON t.title = st.id;

INSERT INTO address (street_id, building_number)
SELECT s.id, b.building
FROM (
    VALUES
        ('Chervonoi kalyny', '1'),
        ('Chervonoi kalyny', '1A'),
        ('Chervonoi kalyny', '1B'),
        ('Chervonoi kalyny', '1V'),
        ('Chervonoi kalyny', '3'),
        ('Chervonoi kalyny', '3A'),
        ('Chervonoi kalyny', '3B'),
        ('Chervonoi kalyny', '5'),
        ('Chervonoi kalyny', '5A'),
        ('Chervonoi kalyny', '7'),
        ('Chervonoi kalyny', '7A'),
        ('Chervonoi kalyny', '15'),
        ('Kashtanova', '1/9'),
        ('Kashtanova', '3'),
        ('Kashtanova', '5'),
        ('Kashtanova', '7'),
        ('Kashtanova', '9'),
        ('Kashtanova', '11'),
        ('Balzaka', '2'),
        ('Balzaka', '2a'),
        ('Balzaka', '4'),
        ('Balzaka', '72')
    ) AS b(street, building)
JOIN street s ON s.title = b.street;

INSERT INTO car_brand (title)
VALUES
('Audi'),
('BMW'),
('Chevrolet'),
('Daewoo'),
('Dodge'),
('Ford'),
('Honda'),
('Hyundai'),
('KIA'),
('Lancia'),
('Lexus'),
('Mazda'),
('Mercedes-Benz'),
('Nissan'),
('Opel'),
('Renault'),
('Subaru'),
('Tesla'),
('Toyota'),
('Volkswagen'),
('Volvo'),
('ZAZ');

INSERT INTO car_model (brand_id, title)
SELECT b.id, c.model
FROM  (
   VALUES
      ('Audi',  'A4')
    , ('Audi', 'A6')
    , ('BMW', '3 series')
    , ('BMW', '5 series')
    , ('BMW', '7 series')
    , ('Chevrolet', 'Aveo')
    , ('Chevrolet', 'Lacetti')
    , ('Daewoo', 'Lanos')
    , ('Daewoo', 'Nubira')
    , ('Dodge', 'Caliber')
    , ('Dodge', 'Journey')
    , ('Ford', 'Focus')
    , ('Ford', 'Fusion')
    , ('Honda', 'Accord')
    , ('Honda', 'Civic')
    , ('Hyundai', 'Accent')
    , ('Hyundai', 'Elentra')
    , ('Hyundai', 'Sonata')
    , ('KIA', 'Rio')
    , ('KIA', 'Optima')
    , ('Lancia', 'Ypsilon')
    , ('Lexus', 'LS')
    , ('Lexus', 'ES')
    , ('Mazda', '3')
    , ('Mazda', '6')
    , ('Mercedes-Benz', 'B-class')
    , ('Mercedes-Benz', 'C-class')
    , ('Mercedes-Benz', 'E-class')
    , ('Mercedes-Benz', 'S-class')
    , ('Nissan', 'Leaf')
    , ('Nissan', 'X-trail')
    , ('Opel', 'Insignia')
    , ('Opel', 'Astra')
    , ('Renault', 'Megane')
    , ('Renault', 'Logan')
    , ('Subaru', 'Impreza')
    , ('Subaru', 'Legacy')
    , ('Tesla', 'Model S')
    , ('Tesla', 'Model 3')
    , ('Toyota', 'Camry')
    , ('Toyota', 'Corolla')
    , ('Volkswagen', 'Golf')
    , ('Volkswagen', 'Passat')
    , ('Volvo', 'S60')
    , ('Volvo', 'S80')
    , ('ZAZ', 'Sens')
    , ('ZAZ', 'Lanos')
  ) AS c(brand, model)
JOIN car_brand b ON b.title = c.brand;

INSERT INTO car_category (title)
VALUES
    ('Standart'),
    ('Comfort'),
    ('Business'),
    ('Wagon'),
    ('Bus'),
    ('Green');

INSERT INTO price_rate (category_id, rate, min_order_price)
SELECT c.id, p.rate, p.min_order_price
FROM (
    VALUES
        ('Standart', 10, 60)
        , ('Comfort', 11.5, 70)
        , ('Business', 13.5, 95)
        , ('Wagon', 10.5, 75)
        , ('Bus', 11.5, 105)
        , ('Green', 12, 100)
     ) AS p (title, rate, min_order_price)
JOIN car_category c ON c.title = p.title;

INSERT INTO person (phone_number, password, first_name, last_name)
VALUES
('', '12345', 'Yuriy', 'Kozhukhar'),
('+380505555555', '12345', 'Andriy', 'Schevchenko'),
('+380507777777', '12345', 'Vitaliy', 'Klychko'),
('+380509999999', '12345', 'Ilon', 'Mask'),
('+380501111111', '12345', 'Mick', 'Schumacher'),
('+380502222222', '12345', 'Kimi', 'Raikonen'),
('+380503333333', '12345', 'Sebastian', 'Vettel');

INSERT INTO person_role (person_id, role_id)
SELECT p.id, r.id
FROM (
    VALUES
        ('+380504402412', 'ROLE_ADMINISTRATOR'),
        ('+380504402412', 'ROLE_CLIENT'),
        ('+380505555555', 'ROLE_CLIENT'),
        ('+380507777777', 'ROLE_CLIENT'),
        ('+380509999999', 'ROLE_CLIENT'),
        ('+380509999999', 'ROLE_CLIENT'),
        ('+380501111111', 'ROLE_DRIVER'),
        ('+380501111111', 'ROLE_CLIENT'),
        ('+380502222222', 'ROLE_DRIVER'),
        ('+380502222222', 'ROLE_CLIENT'),
        ('+380503333333', 'ROLE_DRIVER'),
        ('+380503333333', 'ROLE_CLIENT')
    ) AS c (phone_number, role)
JOIN person p on p.phone_number = c.phone_number
JOIN role r on r.title = c.role;

/*INSERT INTO car (model_id, capacity, category_id, status_id, license_plate)
SELECT m.id, capacity, c.id, s.id, license_plate
FROM (
    VALUES
        ('Corolla', 4, 'Standart', 'inactive', 'KA1111AA')
     ) AS car (model, capacity, category, status, license_plate)
JOIN car_model m ON m.title = car.model
JOIN car_category c ON c.title = car.category
JOIN car_status s ON s.title = car.status;

SELECT car.id AS id, b.id AS brand_id, b.title AS brand_title, m.id AS model_id, m.title AS model_title,
       car.capacity, c.id AS category_id, c.title AS category_title, s.id AS status_id,
	   s.title AS status_title, car.license_plate
FROM car
JOIN car_model m ON m.id = car.model_id
JOIN car_brand b ON b.id = m.brand_id
JOIN car_category c ON c.id = car.category_id
JOIN car_status s ON s.id = car.status_id
ORDER BY s.title;
*/


