INSERT INTO Board VALUES (NULL,'Tabliczka 1');
INSERT INTO Board VALUES (NULL,'Tablica 2');
INSERT INTO Board VALUES (NULL,'Taboret 3');

INSERT INTO CARD_LIST (list_id,name,board_id,archive) VALUES (NULL, 'Lista 1/1', 1,FALSE );
INSERT INTO CARD_LIST (list_id,name,board_id,archive) VALUES (NULL, 'Lista 1/2', 1,FALSE );
INSERT INTO CARD_LIST (list_id,name,board_id,archive) VALUES (NULL, 'Lista 1/3', 1,FALSE );
INSERT INTO CARD_LIST (list_id,name,board_id,archive) VALUES (NULL, 'Lista 2/1', 2,FALSE );

INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 1/1/1',1,FALSE );
INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 1/1/2',1,FALSE );
INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 1/3/1',3,FALSE );
INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 1/3/2',3,FALSE );
INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 1/3/3',3,FALSE );
INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 1/3/4',3,FALSE );
INSERT INTO CARD (card_id,name,list_id,archive) VALUES (NULL, 'Karta 2/1/1',4,FALSE );

INSERT INTO AUTHORITY(name) VALUES('ROLE_USER');
INSERT INTO AUTHORITY(name) VALUES('ROLE_ADMIN');
INSERT INTO AUTHORITY(name) VALUES('ROLE_ANONYMOUS');

INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','a@x.com');
INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','b@x.com');
INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'ccc','cp','c@x.com');
INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'ddd','dp','d@x.com');

INSERT INTO USER_AUTHORITY(user_id, authority_name) VALUES(1, 'ROLE_ADMIN')
INSERT INTO USER_AUTHORITY(user_id, authority_name) VALUES(1, 'ROLE_USER')


insert into Label (label_id,board_id,name,color) values (null, 3, '', 'red');
insert into Label (label_id,board_id,name,color) values (null, 3, '', 'green');
insert into Label (label_id,board_id,name,color) values (null, 3, '', 'blue');
insert into Label (label_id,board_id,name,color) values (null, 3, 'A', 'white');
insert into Label (label_id,board_id,name,color) values (null, 3, 'X', 'white');
insert into Label (label_id,board_id,name,color) values (null, 1, 'Q', 'yellow');
insert into Label (label_id,board_id,name,color) values (null, 1, 'W', 'green');
