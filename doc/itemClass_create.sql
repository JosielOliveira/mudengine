set role mudengine_item_class;

CREATE TABLE MUDENGINE_ITEM_CLASS.MUD_ITEM_CLASS (
		ITEM_CLASS     varchar(30) not null,
		SIZE			real not null,
		WEIGHT			real not null,
		DURABILITY		integer,
		CONSTRAINT MUD_ITEM_CLASS_PK PRIMARY KEY (ITEM_CLASS)
);

CREATE TABLE MUDENGINE_ITEM_CLASS.MUD_ITEM_CLASS_SKILL (
		ITEM_CLASS		varchar(30) not null,
		SKILL_CODE		varchar(20) not null,
		SKILL_OFFSET	real not null,
		CONSTRAINT MUD_ITEM_CLASS_SKILL_PK PRIMARY KEY (ITEM_CLASS, SKILL_CODE),
		FOREIGN KEY (ITEM_CLASS) REFERENCES MUDENGINE_ITEM_CLASS.MUD_ITEM_CLASS(ITEM_CLASS)
);

CREATE TABLE MUDENGINE_ITEM_CLASS.MUD_ITEM_CLASS_ATTR (
		ITEM_CLASS     varchar(30) not null,
		ATTR_CODE		varchar(5) not null,
		ATTR_OFFSET		real not null,
		CONSTRAINT MUD_ITEM_CLASS_ATTR_PK PRIMARY KEY (ITEM_CLASS, ATTR_CODE),
		FOREIGN KEY (ITEM_CLASS) REFERENCES MUDENGINE_ITEM_CLASS.MUD_ITEM_CLASS(ITEM_CLASS)
);

reset role;
