CREATE TABLE user_(
   id_user BIGINT AUTO_INCREMENT,
   last_name VARCHAR(50)  NOT NULL,
   first_name VARCHAR(50)  NOT NULL,
   email VARCHAR(50)  NOT NULL,
   status VARCHAR(50)  NOT NULL,
   date_of_birth DATE NOT NULL,
   phone VARCHAR(50)  NOT NULL,
   password VARCHAR(50)  NOT NULL,
   role VARCHAR(50)  NOT NULL,
   last_connected DATETIME,
   date_creation DATETIME NOT NULL DEFAULT  CURRENT_TIMESTAMP,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   PRIMARY KEY(id_user),
   UNIQUE(email)
);

CREATE TABLE payment_card(
   id_payment_card BIGINT AUTO_INCREMENT,
   id_stripe VARCHAR(50)  NOT NULL,
   last_digit VARCHAR(4)  NOT NULL,
   brand VARCHAR(20) ,
   exp_month SMALLINT NOT NULL,
   exp_year SMALLINT,
   is_default BOOLEAN,
   date_creation DATETIME NOT NULL DEFAULT  CURRENT_TIMESTAMP,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   id_user BIGINT NOT NULL,
   PRIMARY KEY(id_payment_card),
   UNIQUE(id_stripe),
   FOREIGN KEY(id_user) REFERENCES user_(id_user)
);


CREATE TABLE planet(
   id_planet SMALLINT AUTO_INCREMENT,
   name VARCHAR(50)  NOT NULL,
   date_creation DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   PRIMARY KEY(id_planet)
);

CREATE TABLE spaceport(
   id_spaceport INT AUTO_INCREMENT,
   name VARCHAR(50)  NOT NULL,
   date_creation DATETIME NOT NULL DEFAULT  CURRENT_TIMESTAMP,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   id_planet SMALLINT NOT NULL,
   PRIMARY KEY(id_spaceport),
   FOREIGN KEY(id_planet) REFERENCES planet(id_planet)
);

CREATE TABLE flight(
   id_flight BIGINT AUTO_INCREMENT,
   num_flight VARCHAR(50)  NOT NULL,
   date_departure DATETIME NOT NULL,
   date_arrival DATETIME NOT NULL,
   seats SMALLINT NOT NULL,
   seats_available SMALLINT NOT NULL,
   status VARCHAR(50)  NOT NULL,
   price DECIMAL(15,2)   NOT NULL,
   date_creation DATETIME NOT NULL DEFAULT  CURRENT_TIMESTAMP,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   departure_spaceport_id INT NOT NULL,
   arrival_spaceport_id INT NOT NULL,
   PRIMARY KEY(id_flight),
   UNIQUE(num_flight),
   FOREIGN KEY(departure_spaceport_id) REFERENCES spaceport(id_spaceport),
   FOREIGN KEY(arrival_spaceport_id) REFERENCES spaceport(id_spaceport)
);

CREATE TABLE booking(
   id_booking BIGINT AUTO_INCREMENT,
   seat_number TINYINT NOT NULL,
   price DECIMAL(15,2)   NOT NULL,
   first_name_passenger VARCHAR(50)  NOT NULL,
   number_passenger VARCHAR(50)  NOT NULL,
   passenger_age TINYINT NOT NULL,
   gender BOOLEAN NOT NULL,
   status VARCHAR(50)  NOT NULL,
   date_creation DATETIME NOT NULL DEFAULT  CURRENT_TIMESTAMP,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   id_flight BIGINT NOT NULL,
   id_user BIGINT NOT NULL,
   PRIMARY KEY(id_booking),
   FOREIGN KEY(id_flight) REFERENCES flight(id_flight),
   FOREIGN KEY(id_user) REFERENCES user_(id_user)
);

CREATE TABLE invoice(
   id_invoice BIGINT AUTO_INCREMENT,
   invoice_number VARCHAR(50)  NOT NULL,
   invoice_data TEXT COLLATE utf8mb4_unicode_ci
 NOT NULL,
   date_creation DATETIME NOT NULL,
   created_by VARCHAR(50)  NOT NULL,
   last_modification_date DATETIME,
   last_modification_by VARCHAR(50) ,
   version SMALLINT NOT NULL DEFAULT  0,
   id_booking BIGINT NOT NULL,
   id_user BIGINT NOT NULL,
   PRIMARY KEY(id_invoice),
   UNIQUE(id_booking),
   UNIQUE(invoice_number),
   FOREIGN KEY(id_booking) REFERENCES booking(id_booking),
   FOREIGN KEY(id_user) REFERENCES user_(id_user)
);
