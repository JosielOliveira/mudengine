set role mudengine_world;

create sequence mudengine_world.MUD_PLACE_SEQ;

CREATE TABLE mudengine_world.MUD_PLACE_CLASS (
	PLACE_CLASS_CODE	varchar(20) NOT NULL,
	NAME				varchar(30) NOT NULL,
	DESCRIPTION			varchar(500),
	SIZE_CAPACITY		integer,
	WEIGHT_CAPACITY		integer,
	PARENT_CLASS_CODE   varchar(20),
	DEMISE_CLASS_CODE   varchar(20),
	BUILD_COST			integer,
	BUILD_EFFORT		integer,	
	CONSTRAINT MUD_PLACE_CLASS_PK PRIMARY KEY (PLACE_CLASS_CODE)
);


CREATE TABLE mudengine_world.MUD_PLACE_CLASS_ATTR (
	PLACE_CLASS_CODE	varchar(20) NOT NULL,
	ATTR_CODE		varchar(5) not null,
	ATTR_VALUE		integer not null default 0,
	CONSTRAINT MUD_PLACE_CLASS_ATTR_PK PRIMARY KEY (PLACE_CLASS_CODE, ATTR_CODE),
	FOREIGN KEY (PLACE_CLASS_CODE) REFERENCES mudengine_world.MUD_PLACE_CLASS(PLACE_CLASS_CODE) on delete cascade
);

CREATE TABLE mudengine_world.MUD_PLACE  (
	PLACE_CODE			integer NOT NULL,
	PLACE_CLASS_CODE	varchar(20) NOT NULL,
	CONSTRAINT MUD_PLACE_PK PRIMARY KEY (PLACE_CODE),
	FOREIGN KEY (PLACE_CLASS_CODE) REFERENCES mudengine_world.MUD_PLACE_CLASS(PLACE_CLASS_CODE)
);
	
	
CREATE TABLE mudengine_world.MUD_PLACE_EXIT (
	PLACE_CODE			integer NOT NULL,
	NAME				varchar(30) NOT NULL,
	DIRECTION			varchar(10) NOT NULL,
	OPENED				boolean NOT NULL DEFAULT 'true',
	LOCKED				boolean NOT NULL DEFAULT 'false',
	LOCKABLE			boolean NOT NULL DEFAULT 'false',
	VISIBLE				boolean NOT NULL DEFAULT 'true',
	TARGET_PLACE_CODE	integer NOT NULL,
	CONSTRAINT MUD_PLACE_EXITS_PK PRIMARY KEY (PLACE_CODE, DIRECTION),
	FOREIGN KEY (PLACE_CODE) REFERENCES mudengine_world.MUD_PLACE(PLACE_CODE) on delete cascade,
	FOREIGN KEY (TARGET_PLACE_CODE) REFERENCES mudengine_world.MUD_PLACE(PLACE_CODE) on delete cascade
);

CREATE TABLE mudengine_world.MUD_PLACE_ATTR (
	PLACE_CODE		integer NOT NULL,
	ATTR_CODE		varchar(5) not null,
	ATTR_VALUE		integer not null default 0,
	CONSTRAINT MUD_PLACE_ATTR_PK PRIMARY KEY (PLACE_CODE, ATTR_CODE),
	FOREIGN KEY (PLACE_CODE) REFERENCES mudengine_world.MUD_PLACE(PLACE_CODE) on delete cascade
);
