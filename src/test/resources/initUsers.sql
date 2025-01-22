DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    USERNAME   VARCHAR(255),
    FIRST_NAME VARCHAR(255),
    LAST_NAME  VARCHAR(255),
    PRIMARY KEY (USERNAME)
);

INSERT INTO USERS
VALUES ('alvin.h', 'Alvin', 'Hamaide'),
       ('tom.e', 'Tom', 'Engelibert'),
       ('philippe.p', 'Philippe', 'Plaia');