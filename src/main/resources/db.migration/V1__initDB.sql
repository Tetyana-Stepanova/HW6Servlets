CREATE TABLE companies(
companies_id INT AUTO_INCREMENT PRIMARY KEY,
companies_name VARCHAR(100),
companies_description VARCHAR(200)
);

CREATE TABLE developers(
developers_id INT AUTO_INCREMENT PRIMARY KEY,
first_name varchar(50) NOT NULL,
last_name varchar(100) NOT NULL,
age INT,
gender VARCHAR(10),
city VARCHAR(100),
salary MEDIUMINT,
companies_id INT,
FOREIGN KEY (companies_id) REFERENCES companies(companies_id) ON DELETE SET NULL
);

CREATE TABLE skills(
skills_id INT AUTO_INCREMENT PRIMARY KEY,
dev_language VARCHAR(50),
skill_level VARCHAR(20)
);

CREATE TABLE developers_skills(
developers_id INT NOT NULL,
skills_id INT NOT NULL,
FOREIGN KEY (developers_id) REFERENCES developers(developers_id) ON DELETE CASCADE,
FOREIGN KEY (skills_id) REFERENCES skills(skills_id) ON DELETE CASCADE,
UNIQUE (developers_id, skills_id)
);

CREATE TABLE customers(
customer_id INT AUTO_INCREMENT PRIMARY KEY,
customers_name VARCHAR(100),
customers_descriptions VARCHAR(100)
);

CREATE TABLE projects(
project_id INT AUTO_INCREMENT PRIMARY KEY,
project_name VARCHAR(100),
project_description VARCHAR(200),
release_date DATE,
companies_id INT,
customer_id INT,
FOREIGN KEY (companies_id) REFERENCES companies (companies_id) ON DELETE SET NULL,
FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON DELETE SET NULL
);

CREATE TABLE developers_projects(
developers_id INT NOT NULL,
project_id INT NOT NULL,
FOREIGN KEY (developers_id) REFERENCES developers(developers_id) ON DELETE CASCADE,
FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE,
UNIQUE (developers_id, project_id)
);