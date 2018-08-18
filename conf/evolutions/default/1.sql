# --- !Ups

CREATE TABLE car_adverts (
  id INT(11) AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  fuel_type VARCHAR(255) NOT NULL,
  price INT(11) NOT NULL,
  is_new TINYINT(1) NOT NULL,
  mileage INT(11),
  first_registration DATE
);

# --- !Downs

DROP TABLE IF EXISTS car_adverts;