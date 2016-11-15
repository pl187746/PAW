INSERT INTO Board (name) VALUES ('Tabliczka 1');
INSERT INTO Board (name) VALUES ('Tablica 2');
INSERT INTO Board (name) VALUES ('Taboret 3');

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
INSERT INTO USER_AUTHORITY(user_id, authority_name) VALUES(2, 'ROLE_USER')

insert into Label (label_id,board_id,name,color) values (null, 3, '', '#ff0000');
insert into Label (label_id,board_id,name,color) values (null, 3, '', '#00ff00');
insert into Label (label_id,board_id,name,color) values (null, 3, '', '#0000ff');
insert into Label (label_id,board_id,name,color) values (null, 3, 'A', '#ffffff');
insert into Label (label_id,board_id,name,color) values (null, 3, 'X', '#ffffff');
insert into Label (label_id,board_id,name,color) values (null, 1, 'Q', '#ffff00');
insert into Label (label_id,board_id,name,color) values (null, 1, 'W', '#00ff00');
insert into Label (label_id,board_id,name,color) values (null, 1, '', '#ffffff');
insert into Label (label_id,board_id,name,color) values (null, 1, 'r', '#ff0000');
insert into Label (label_id,board_id,name,color) values (null, 1, 'g', '#00ff00');
insert into Label (label_id,board_id,name,color) values (null, 1, 'b', '#0000ff');

insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (1,7);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (2,6);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (2,7);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (4,7);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (4,6);

insert into BOARD_MEMBERS (board_id, user_id) values (1, 1);
insert into BOARD_MEMBERS (board_id, user_id) values (1, 2);

insert into TEAM (name) values ('Żółwie nindża');
insert into TEAM (name) values ('Czterej pancerni');

insert into TEAM_USERS (TEAM_ID, USER_ID) values (1, 1);
insert into TEAM_USERS (TEAM_ID, USER_ID) values (1, 3);
insert into TEAM_USERS (TEAM_ID, USER_ID) values (2, 1);
insert into TEAM_USERS (TEAM_ID, USER_ID) values (2, 2);
insert into TEAM_USERS (TEAM_ID, USER_ID) values (2, 4);
