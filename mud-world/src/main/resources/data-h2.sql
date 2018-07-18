insert into mud_place_class(place_class_code, name, description) values('RUIN', 'Ruin', 'Demised PlaceClass');
insert into mud_place_class(place_class_code, name, description) values('TEST', 'Test', 'Test PlaceClass');
insert into mud_place_class(place_class_code, name, description) values('TESTBLDG', 'Test Building', 'Test Building PlaceClass');
insert into mud_place_class(place_class_code, name, description, demise_class_code) values('ATEST', 'Test', 'Demisable PlaceClass', 'RUIN');

insert into mud_place_class_attr(place_class_code, attr_code, attr_value) values('TEST', 'HP', 50);
insert into mud_place_class_attr(place_class_code, attr_code, attr_value) values('TEST', 'MAXHP', 500);

insert into mud_place_class_attr(place_class_code, attr_code, attr_value) values('TESTBLDG', 'HP2', 3);
insert into mud_place_class_attr(place_class_code, attr_code, attr_value) values('TESTBLDG', 'MAXH2', 8);

insert into mud_place_class_attr(place_class_code, attr_code, attr_value) values('ATEST', 'HP', 50);
insert into mud_place_class_attr(place_class_code, attr_code, attr_value) values('ATEST', 'MAXHP', 500);


INSERT INTO mud_place(place_code, place_class_code) values(1, 'TEST');
insert into mud_place_attr(place_code, attr_code, attr_value) values(1, 'HP', 50);
insert into mud_place_attr(place_code, attr_code, attr_value) values(1, 'MAXHP', 100);

INSERT INTO mud_place(place_code, place_class_code) values(2, 'TEST');
insert into mud_place_attr(place_code, attr_code, attr_value) values(2, 'HP', 50);
insert into mud_place_attr(place_code, attr_code, attr_value) values(2, 'MAXHP', 100);

INSERT INTO mud_place(place_code, place_class_code) values(3, 'TEST');
insert into mud_place_attr(place_code, attr_code, attr_value) values(3, 'HP', 50);
insert into mud_place_attr(place_code, attr_code, attr_value) values(3, 'MAXHP', 100);

INSERT INTO mud_place(place_code, place_class_code) values(4, 'TEST');
insert into mud_place_attr(place_code, attr_code, attr_value) values(4, 'HP', 50);
insert into mud_place_attr(place_code, attr_code, attr_value) values(4, 'MAXHP', 100);

INSERT INTO mud_place(place_code, place_class_code) values(5, 'TEST');
insert into mud_place_attr(place_code, attr_code, attr_value) values(5, 'HP', 50);
insert into mud_place_attr(place_code, attr_code, attr_value) values(5, 'MAXHP', 100);


INSERT INTO mud_place(place_code, place_class_code) values(6, 'ATEST');
insert into mud_place_attr(place_code, attr_code, attr_value) values(6, 'HP', 50);
insert into mud_place_attr(place_code, attr_code, attr_value) values(6, 'MAXHP', 100);


INSERT INTO mud_place(place_code, place_class_code) values(7, 'TEST');

INSERT INTO mud_place_exit(place_code, direction, name, opened, visible, target_place_code) values (1, 'OUT', 'Plain', true, true, 2);

alter sequence MUD_PLACE_SEQ restart with 8;