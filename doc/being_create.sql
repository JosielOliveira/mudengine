set role MUDENGINE_BEING;

create sequence MUDENGINE_BEING.MUD_BEING_SEQ;

CREATE TABLE MUDENGINE_BEING.MUD_ATTRIBUTE (
		ATTR_CODE		varchar(5) not null,
		NAME			varchar(30) not null,
		DESCRIPTION		varchar(100),		
		CONSTRAINT MUD_ATTRIBUTES_PK PRIMARY KEY (ATTR_CODE)
);

CREATE TABLE MUDENGINE_BEING.MUD_SKILL_CATEGORY (
		CATEGORY_CODE	varchar(5) not null,
		NAME			varchar(30) not null,
		ATTR_CODE_BASED	varchar(5),
		DESCRIPTION		varchar(100),		
		CONSTRAINT MUD_SKILL_CATG_PK PRIMARY KEY (CATEGORY_CODE),
		FOREIGN KEY (ATTR_CODE_BASED) REFERENCES MUDENGINE_BEING.MUD_ATTRIBUTE(ATTR_CODE)
);
		
CREATE TABLE MUDENGINE_BEING.MUD_SKILL (
		SKILL_CODE		varchar(20) not null,
		CATEGORY_CODE	varchar(5) not null,
		NAME			varchar(30) not null,
		DESCRIPTION		varchar(100),		
		CONSTRAINT MUD_SKILL_PK PRIMARY KEY (SKILL_CODE),
		FOREIGN KEY (CATEGORY_CODE) REFERENCES MUDENGINE_BEING.MUD_SKILL_CATEGORY(CATEGORY_CODE)
);

CREATE TABLE MUDENGINE_BEING.MUD_PLAYER (
		PLAYER_ID		bigint not null,
		LOGIN			varchar(30) not null,
		PASSWORD		varchar(64) not null,
		NAME			varchar(30) not null,
		CONSTRAINT MUD_PLAYER_PK PRIMARY KEY (PLAYER_ID)
);	

CREATE TABLE MUDENGINE_BEING.MUD_BEING (
		BEING_CODE		bigint not null,
		BEING_CLASS		varchar(20) not null,
		NAME			varchar(50) not null,
		PLAYER_ID		bigint,		
		CURRENT_WORLD	varchar(30),
		CURRENT_PLACE	integer,
		CONSTRAINT MUD_BEING_PK PRIMARY KEY (BEING_CODE),
		FOREIGN KEY (PLAYER_ID) REFERENCES MUDENGINE_BEING.MUD_PLAYER(PLAYER_ID)
);

CREATE TABLE MUDENGINE_BEING.MUD_BEING_ATTR (
		BEING_CODE		bigint not null,
		ATTR_CODE		varchar(5) not null,
		ATTR_VALUE		real not null,
		CONSTRAINT MUD_BEING_ATTR_PK PRIMARY KEY (BEING_CODE, ATTR_CODE),
		FOREIGN KEY (BEING_CODE) REFERENCES MUDENGINE_BEING.MUD_BEING(BEING_CODE),
		FOREIGN KEY (ATTR_CODE) REFERENCES MUDENGINE_BEING.MUD_ATTRIBUTE(ATTR_CODE)
);

CREATE TABLE MUDENGINE_BEING.MUD_BEING_SKILL (
		BEING_CODE		bigint not null,
		SKILL_CODE		varchar(20) not null,
		SKILL_VALUE 	real not null,
		CONSTRAINT MUD_BEING_SKILLS_PK PRIMARY KEY (BEING_CODE, SKILL_CODE),
		FOREIGN KEY (BEING_CODE) REFERENCES MUDENGINE_BEING.MUD_BEING(BEING_CODE),
		FOREIGN KEY (SKILL_CODE) REFERENCES MUDENGINE_BEING.MUD_SKILL(SKILL_CODE)
);

CREATE TABLE MUDENGINE_BEING.MUD_BEING_ATTR_MODIFIER (
		BEING_CODE		bigint not null,
		ATTR_CODE		varchar(5) not null,
		ORIGIN_CODE		varchar(20) not null,
		ORIGIN_TYPE		varchar(30) not null,
		
		ATTR_OFFSET		real not null,
		END_TURN		integer,
		CONSTRAINT MUD_BEING_ATTR_MOD_PK PRIMARY KEY (BEING_CODE, ATTR_CODE, ORIGIN_CODE, ORIGIN_TYPE),
		FOREIGN KEY (BEING_CODE) REFERENCES MUDENGINE_BEING.MUD_BEING(BEING_CODE),
		FOREIGN KEY (ATTR_CODE) REFERENCES MUDENGINE_BEING.MUD_ATTRIBUTE(ATTR_CODE)
);

CREATE TABLE MUDENGINE_BEING.MUD_BEING_SKILL_MODIFIER (
		BEING_CODE		bigint not null,
		SKILL_CODE		varchar(20) not null,
		ORIGIN_CODE		varchar(20) not null,
		ORIGIN_TYPE		varchar(30) not null,
		SKILL_OFFSET 	real not null,
		END_TURN		integer,
		CONSTRAINT MUD_BEING_SKILL_MOD_PK PRIMARY KEY (BEING_CODE, SKILL_CODE, ORIGIN_CODE, ORIGIN_TYPE),
		FOREIGN KEY (BEING_CODE) REFERENCES MUDENGINE_BEING.MUD_BEING(BEING_CODE),
		FOREIGN KEY (SKILL_CODE) REFERENCES MUDENGINE_BEING.MUD_SKILL(SKILL_CODE)
);


CREATE TABLE MUDENGINE_BEING.MUD_BEING_ITEM (
		BEING_CODE		bigint not null,
		ITEM_CODE		integer not null,
		QTTY			integer not null default 0,
		USAGE_COUNT		integer,
		CONSTRAINT MUD_BEING_ITEMS_PK PRIMARY KEY (BEING_CODE, ITEM_CODE),
		FOREIGN KEY (BEING_CODE) REFERENCES MUDENGINE_BEING.MUD_BEING(BEING_CODE)
);
