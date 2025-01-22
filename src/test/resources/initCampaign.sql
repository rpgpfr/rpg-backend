DROP TABLE IF EXISTS CAMPAIGN;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    USERNAME   VARCHAR(255),
    FIRST_NAME VARCHAR(255),
    LAST_NAME  VARCHAR(255),
    PRIMARY KEY (USERNAME)
);

CREATE TABLE CAMPAIGN
(
    NAME     VARCHAR(255),
    USERNAME VARCHAR(255),
    FOREIGN KEY (USERNAME) REFERENCES USERS (USERNAME) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (NAME, USERNAME)
);

INSERT INTO USERS
VALUES ('alvin.h', 'Alvin', 'Hamaide'),
       ('tom.e', 'Tom', 'Engelibert'),
       ('philippe.p', 'Philippe', 'Plaia');

INSERT INTO CAMPAIGN
VALUES ('Campagne 1', 'alvin.h'),
       ('Campagne 2', 'alvin.h'),
       ('Campagne 3', 'alvin.h'),
       ('Campagne 4', 'tom.e'),
       ('Campagne 5', 'tom.e');