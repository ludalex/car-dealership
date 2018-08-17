# --- !Ups

CREATE TABLE car_adverts (
  id INT(11)  AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  fuel_type VARCHAR(255),
  price INT(11),
  is_new TINYINT(1),
  mileage INT(11),
  first_registration VARCHAR(255)
);

# --- !Downs

DROP TABLE IF EXISTS car_adverts;