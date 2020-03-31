-- -----------------------------------------------------
-- Schema hotel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hotel`;
USE `hotel` ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `hotel`.`customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hotel`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `f_name` VARCHAR(45) NOT NULL,
  `l_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `password` VARCHAR(150) NOT NULL,
  `phone` VARCHAR(45) NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC));
  
SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `hotel`.`rooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hotel`.`rooms` (
  `id` INT NOT NULL,
  `price_per_night` DECIMAL NOT NULL,
  `max_guests` INT NOT NULL,
  PRIMARY KEY (`id`));
  
SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `hotel`.`reservations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hotel`.`reservations` (
  `id` INT NOT NULL,
  `customer_id` INT NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `total_cost` DECIMAL(11,2) NULL DEFAULT 0,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `customer_id_idx` (`customer_id` ASC),
  CONSTRAINT `customer_id`
    FOREIGN KEY (`customer_id`)
    REFERENCES `hotel`.`customers` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `hotel`.`room_reservations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hotel`.`room_reservations` (
  `room_id` INT NOT NULL,
  `reservation_id` INT NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`room_id`, `reservation_id`),
  INDEX `fk_reservation_idx` (`reservation_id` ASC),
  CONSTRAINT `fk_reservation`
    FOREIGN KEY (`reservation_id`)
    REFERENCES `hotel`.`reservations` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_room`
    FOREIGN KEY (`room_id`)
    REFERENCES `hotel`.`rooms` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
SHOW WARNINGS;
