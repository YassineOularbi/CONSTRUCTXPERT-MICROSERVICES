CREATE TABLE resource (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                          title VARCHAR(255) NOT NULL,
                          provider VARCHAR(255) NOT NULL,
                          acquisition_date DATE NOT NULL,
                          picture VARCHAR(255) NOT NULL,
                          quantity VARCHAR(255) NOT NULL,
                          availability BOOLEAN NOT NULL,
                          type ENUM('EQUIPMENT', 'VEHICLE', 'MATERIAL') NOT NULL
);