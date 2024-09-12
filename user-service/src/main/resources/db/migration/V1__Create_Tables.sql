CREATE DATABASE IF NOT EXISTS constructxpert_user_db;
CREATE TABLE admin (
                       id BIGINT NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE client (
                          id BIGINT NOT NULL,
                          PRIMARY KEY (id)
);

CREATE TABLE supervisor (
                        id BIGINT NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      role ENUM('ADMIN', 'CLIENT', 'SUPERVISOR') NOT NULL,
                      username VARCHAR(255) NOT NULL,
                      profile_picture VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id)
);

ALTER TABLE admin
    ADD CONSTRAINT FK_admin_user FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE client
    ADD CONSTRAINT FK_client_user FOREIGN KEY (id) REFERENCES user (id);

ALTER TABLE supervisor
    ADD CONSTRAINT FK_supervisor_user FOREIGN KEY (id) REFERENCES user (id);