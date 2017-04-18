package com.jpinfo.mudengine.common.being;

import java.io.Serializable;

/**
 * The persistent class for the mud_player database table.
 * 
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer playerId;

	private String login;

	private String name;

	public Player() {
	}

	public Integer getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}