INSERT INTO companies (companies_name, companies_description)
VALUES ('EPAM', 'more than 12000 specialists in Ukraine'),
	   ('Ciklum', 'more than 3000 specialists in Ukraine'),
       ('SoftServe', 'more than 9600 specialists in Ukraine'),
       ('GlobalLogic', 'more than 6500 specialists in Ukraine'),
       ('Luxoft', 'more than 3600 specialists in Ukraine');

INSERT INTO developers (first_name, last_name, age, gender, city, salary, companies_id)
VALUES ('Tetyana', 'Stepanova', 39, 'female', 'Kyiv', 1000, 1),
        ('Anton', 'Shevchenko', 44, 'male', 'Cherkasy', 3000, 2),
        ('Olesya', 'Pryadilnikova', 46, 'female', 'Lviv', 3000, 4),
        ('Mukola', 'Piskoviy', 35, 'male', 'Kyiv', 4000, 3),
        ('Borys', 'Salagatov', 32, 'male', 'Chernivtsi', 2000, 5),
        ('Anna', 'Dmutrenko', 38, 'female', 'Dnipro', 2000, 4);

INSERT INTO skills (dev_language, skill_level)
VALUES ('Java', 'Junior'),
       ('Java', 'Middle'),
       ('Java', 'Senior'),
       ('Java Script', 'Junior'),
       ('Java Script', 'Middle'),
       ('Java Script', 'Senior'),
       ('C++', 'Junior'),
       ('C++', 'Middle'),
       ('C++', 'Senior'),
       ('C Sharp', 'Junior'),
       ('C Sharp', 'Middle'),
       ('C Sharp', 'Senior');

INSERT INTO customers (customers_name, customers_descriptions)
VALUES ('all.biz', 'В2В marketplace'),
	   ('Allegro Group', 'E-commerce'),
       ('Deshevshe.net', 'Onlain electronic store'),
       ('Citrus', 'Onlain and offline electronic store'),
       ('Finline', 'Financial service');

INSERT INTO projects (project_name, project_description, release_date, companies_id, customer_id)
VALUES ('MusicApp', 'application for listening music', '2021-10-7', 2, 4),
	   ('CloudApp', 'application for work with clouds', '2020-12-5', 4, 2),
       ('StoreApp', 'application for onlain store', '2022-01-25', 3, 3),
       ('PayApp', 'application for financial payments', '2022-03-15', 1, 5),
       ('B2BApp', 'application for B2B marketplace', '2022-04-17', 5, 1),
       ('OnlinePayApp', 'application for pay online', '2022-05-19', 3, 3),
       ('BaseForStore', 'development of the base fo the store', '2022-06-10', 2, 4),
       ('WebApp', 'create website for online store', '2022-02-06', 5, 2),
       ('SearchApp', 'application for searching', '2021-08-25', 1, 1),
       ('SecurityApp', 'application for security payments online', '2021-07-19', 4, 5);

INSERT INTO developers_skills (developers_id, skills_id)
VALUES (1, 1), (2, 9), (3, 3), (4, 6), (5, 11), (6, 2),
       (1, 4), (2, 6), (3, 6), (3, 7), (4, 8), (5, 8), (6, 5), (4, 1);

INSERT INTO developers_projects (developers_id, project_id)
VALUES (1, 1), (1, 3), (1, 6), (2, 10), (2, 4), (3, 2), (3, 6),
       (3, 4), (4, 8), (4, 5), (5, 9), (5, 7), (6, 8), (6, 7),
	   (1, 9), (3, 10), (6, 3), (5, 2), (5, 1), (4, 2), (5, 3), (4, 1);