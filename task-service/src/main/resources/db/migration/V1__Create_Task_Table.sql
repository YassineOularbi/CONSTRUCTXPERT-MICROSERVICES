CREATE TABLE task (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      type VARCHAR(255) NOT NULL,
                      start_date DATE NOT NULL,
                      end_date DATE NOT NULL,
                      description TEXT NOT NULL,
                      priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
                      status ENUM('TODO', 'IN_PROGRESS', 'COMPLETED') NOT NULL,
                      project_id BIGINT NOT NULL
);