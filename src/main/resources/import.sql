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

INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'admin','ap','a@x.com');
INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'bbb','bp','b@x.com');
INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'ccc','cp','c@x.com');
INSERT INTO USER (user_id,login,password,email) VALUES (NULL, 'ddd','dp','d@x.com');