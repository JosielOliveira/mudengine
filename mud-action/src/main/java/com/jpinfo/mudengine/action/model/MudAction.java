package com.jpinfo.mudengine.action.model;

import javax.persistence.*;

import com.jpinfo.mudengine.common.action.Action;

import lombok.Data;

@Entity
@Table(name="MUD_ACTION")
@SequenceGenerator(name="MUD_ACTION_SEQ", sequenceName="MUD_ACTION_SEQ", allocationSize=1)
@Data
public class MudAction {
	
	@Id
	@Column(name="ACTION_UID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MUD_ACTION_SEQ")
	private Long actionId;
	
	@Column(name="ISSUER_CODE")
	private Long issuerCode;
	
	@Column(name="ACTOR_CODE")
	private Long actorCode;

	@Column(name="ACTION_CLASS_CODE")
	private String actionClassCode;
	
	@Column(name="MEDIATOR_CODE")
	private String mediatorCode;

	@Column(name="MEDIATOR_TYPE")
	private String mediatorType;
	
	@Column(name="TARGET_CODE")
	private String targetCode;

	@Column(name="TARGET_TYPE")
	private String targetType;
	
	@Column(name="START_TURN")
	private Long startTurn;
	
	@Column(name="END_TURN")
	private Long endTurn;
	
	@Column(name="CUR_STATE")
	private Integer currState;
	
	@Column(name="SUCCESS_RATE")
	private Float successRate;

	@Column(name="WORLD_NAME")
	private String worldName;
	
	@Column(name="ACTION_TYPE")
	private String runType;
	
	public MudAction() {
		this.currState = 0;
		
	}
	
	@Transient
	public void setTargetTypeEnum(Action.EnumTargetType enumTargetType) {
		this.targetType = enumTargetType.toString();
	}
	
	@Transient
	public void setCurrStateEnum(Action.EnumActionState enumState) {
		this.currState = enumState.ordinal();
	}
}
