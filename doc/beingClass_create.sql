set role MUDENGINE_BEING_CLASS;

CREATE TABLE MUDENGINE_BEING_CLASS.MUD_BEING_CLASS (
		BEING_CLASS		varchar(20) not null,
		NAME			varchar(30) not null,
		DESCRIPTION     varchar(200),
		SIZE			integer,
		WEIGHT_CAPACITY integer,
		CONSTRAINT MUD_BEING_CLASS_PK PRIMARY KEY (BEING_CLASS)
);
		
CREATE TABLE MUDENGINE_BEING_CLASS.MUD_BEING_CLASS_ATTR (
		BEING_CLASS		varchar(20) not null,
		ATTR_CODE		varchar(5) not null,
		ATTR_VALUE		integer not null default 0,
		CONSTRAINT MUD_BEING_CLASS_ATTR_PK PRIMARY KEY (BEING_CLASS, ATTR_CODE),
		FOREIGN KEY (BEING_CLASS) REFERENCES MUDENGINE_BEING_CLASS.MUD_BEING_CLASS(BEING_CLASS)
);

CREATE TABLE MUDENGINE_BEING_CLASS.MUD_BEING_CLASS_SKILL (
		BEING_CLASS		varchar(20) not null,
		SKILL_CODE		varchar(20) not null,
		SKILL_VALUE		integer not null default 0,
		CONSTRAINT MUD_BEING_CLASS_SKILLS_PK PRIMARY KEY (BEING_CLASS, SKILL_CODE),
		FOREIGN KEY (BEING_CLASS) REFERENCES MUDENGINE_BEING_CLASS.MUD_BEING_CLASS(BEING_CLASS)
);