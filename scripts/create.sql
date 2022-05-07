CREATE TABLE category (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL
);

CREATE TABLE subcategory (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    category_id INTEGER NOT NULL REFERENCES CATEGORY
);

CREATE TABLE cloth (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    price NUMBER(10, 2) NOT NULL,
    count INTEGER NOT NULL,
    subcategory_id INTEGER NOT NULL REFERENCES SUBCATEGORY,
    description VARCHAR2(500)
);

CREATE TABLE rating (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    rating NUMBER(2, 1) NOT NULL,
    cloth_id INTEGER NOT NULL REFERENCES CLOTH
);

CREATE TABLE order_status (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL
);

CREATE TABLE role (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL
);

CREATE TABLE status (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL
);

CREATE TABLE "USER" (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    login VARCHAR2(100) NOT NULL,
    password VARCHAR2(100) NOT NULL,
    email VARCHAR2(100),
    phone_number VARCHAR2(100) NOT NULL,
    first_name VARCHAR2(100),
    second_name VARCHAR2(100),
    country VARCHAR2(100),
    city VARCHAR2(100),
    street VARCHAR2(100),
    house_number VARCHAR2(100),
    zip NUMBER(6),
    deleted_at DATE,
    status_id INTEGER NOT NULL REFERENCES STATUS,
    role_id INTEGER NOT NULL REFERENCES ROLE
);

CREATE TABLE "ORDER" (
    id INT GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    completion_date DATE,
    user_id INTEGER NOT NULL REFERENCES "USER",
    worker_id INTEGER NOT NULL REFERENCES "USER",
    order_status_id INTEGER NOT NULL REFERENCES ORDER_STATUS
);

CREATE TABLE many_order_to_many_cloth (
    order_id INTEGER NOT NULL REFERENCES "ORDER",
    cloth_id INTEGER NOT NULL REFERENCES CLOTH,
    count INTEGER NOT NULL,
    CONSTRAINT many_order_to_many_cloth_pk PRIMARY KEY (order_id, cloth_id)
);

INSERT INTO Category(name) VALUES('hardware');
INSERT INTO Category(name) VALUES('furniture');
INSERT INTO Category(name) VALUES('tool');

INSERT INTO Subcategory(name, category_id) VALUES ('sub_hardware', 1);
INSERT INTO Subcategory(name, category_id) VALUES ('sub_furniture', 2);
INSERT INTO Subcategory(name, category_id) VALUES ('sub_tool', 3);

INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 1', 100.5, 10, 1, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 2', 10.45, 10, 1, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 3', 76.5, 20, 2, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 4', 14.5, 30, 1, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 5', 16.5, 40, 3, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 6', 73.6, 50, 1, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 7', 100.5, 55, 1, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 8', 92.67, 60, 1, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 9', 89.1, 65, 2, 'first cloth');
INSERT INTO CLOTH(name, price, count, subcategory_id, description) VALUES ('cloth 10', 600.5, 70, 1, 'first cloth');

INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 1);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 2);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 3);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 4);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 5);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 6);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 7);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 8);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 9);
INSERT INTO RATING(rating, cloth_id) VALUES (4.2, 10);

INSERT INTO ORDER_STATUS(name) VALUES ('waiting for payment');
INSERT INTO ORDER_STATUS(name) VALUES ('waiting for order approval');
INSERT INTO ORDER_STATUS(name) VALUES ('approved');
INSERT INTO ORDER_STATUS(name) VALUES ('sent');
INSERT INTO ORDER_STATUS(name) VALUES ('canceled');

INSERT INTO ROLE(name) VALUES ('admin');
INSERT INTO ROLE(name) VALUES ('user');
INSERT INTO ROLE(name) VALUES ('worker');

INSERT INTO STATUS(name) VALUES ('none');
INSERT INTO STATUS(name) VALUES ('free');
INSERT INTO STATUS(name) VALUES ('busy');

INSERT INTO "USER"(login, password, phone_number, status_id, role_id) VALUES ('admin', 'pass', '+420775184920', 1, 1);
INSERT INTO "USER"(login, password, email, phone_number, first_name, second_name, country, city, street, house_number, zip, status_id, role_id) VALUES ('user', 'pass', 'example@mail.cz', '+420775184920', 'ratmir', 'gaitov', 'kazakhstan', 'ridder', 'lazo', '12/2', 70800, 1, 2);
INSERT INTO "USER"(login, password, phone_number, status_id, role_id) VALUES ('worker', 'pass', '+420775184920', 2, 3);
