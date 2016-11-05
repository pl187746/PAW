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

insert into Label (label_id,board_id,name,color) values (null, 3, '', '#FF0000');
insert into Label (label_id,board_id,name,color) values (null, 3, '', '#00FF00');
insert into Label (label_id,board_id,name,color) values (null, 3, '', '#0000FF');
insert into Label (label_id,board_id,name,color) values (null, 3, 'A', '#FFFFFF');
insert into Label (label_id,board_id,name,color) values (null, 3, 'X', '#FFFFFF');
insert into Label (label_id,board_id,name,color) values (null, 1, 'Q', '#FFFF00');
insert into Label (label_id,board_id,name,color) values (null, 1, 'W', '#00FF00');
insert into Label (label_id,board_id,name,color) values (null, 1, '', '#FFFFFF');
insert into Label (label_id,board_id,name,color) values (null, 1, 'r', '#FF0000');
insert into Label (label_id,board_id,name,color) values (null, 1, 'g', '#00FF00');
insert into Label (label_id,board_id,name,color) values (null, 1, 'b', '#0000FF');

insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (1,7);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (2,6);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (2,7);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (4,7);
insert into CARD_LABELS (LABELED_CARDS_CARD_ID,LABELS_LABEL_ID) values (4,6);
