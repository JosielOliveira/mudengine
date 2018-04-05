set role MUDENGINE_ACTION;

create sequence MUDENGINE_ACTION.MUD_ACTION_SEQ;

CREATE TABLE MUDENGINE_ACTION.MUD_ACTION_CLASS (
		ACTION_CLASS_CODE	varchar(15) not null,
		VERB				varchar(10) not null,
		MEDIATOR_TYPE		varchar(20),
		TARGET_TYPE			varchar(20),
		ACTION_TYPE			integer not null default 0,
		SUCCESS_RATE_EXPRESSION varchar(200),
		NRO_TURNS_EXPRESSION varchar(200),
		CONSTRAINT MUD_ACTION_CLASS_PK PRIMARY KEY (ACTION_CLASS_CODE)
);

CREATE TABLE MUDENGINE_ACTION.MUD_ACTION_CLASS_PREREQ (
		ACTION_CLASS_CODE	varchar(15) not null,
		EVAL_ORDER			integer not null,
		CHECK_EXPRESSION	varchar(200) not null,
		FAIL_EXPRESSION		varchar(200),
		CONSTRAINT MUD_ACTION_CLASS_PREREQ_PK PRIMARY KEY (ACTION_CLASS_CODE, EVAL_ORDER),
		FOREIGN KEY (ACTION_CLASS_CODE) REFERENCES MUDENGINE_ACTION.MUD_ACTION_CLASS(ACTION_CLASS_CODE)
);

CREATE TABLE MUDENGINE_ACTION.MUD_ACTION_CLASS_EFFECT (
		ACTION_CLASS_CODE	varchar(15) not null,
		EVAL_ORDER			integer not null,
		EFFECT_EXPRESSION	varchar(200) not null,
		MESSAGE_EXPRESSION	varchar(200),
		CONSTRAINT MUD_ACTION_CLASS_EFFECT_PK PRIMARY KEY (ACTION_CLASS_CODE, EVAL_ORDER),
		FOREIGN KEY (ACTION_CLASS_CODE) REFERENCES MUDENGINE_ACTION.MUD_ACTION_CLASS(ACTION_CLASS_CODE)
);
		
CREATE TABLE MUDENGINE_ACTION.MUD_ACTION (
		ACTION_UID			bigint not null,
		ISSUER_CODE			bigint not null,
		ACTOR_CODE			bigint not null,		
		ACTION_CLASS_CODE	varchar(15) not null,
		MEDIATOR_CODE		varchar(200),
		MEDIATOR_TYPE		varchar(20),
		TARGET_CODE			varchar(20) not null,
		TARGET_TYPE			varchar(20) not null,
		
		START_TURN			bigint,
		END_TURN			bigint,
		CUR_STATE			integer not null default 0,  -- 0=NotStarted, 1=Started, 2=Completed, 3=Cancelled, 4=Refused
		SUCCESS_RATE		real,
		
		CONSTRAINT MUD_ACTION_PK PRIMARY KEY (ACTION_UID),
		FOREIGN KEY (ACTION_CLASS_CODE) REFERENCES MUDENGINE_ACTION.MUD_ACTION_CLASS(ACTION_CLASS_CODE)
);
