package com.jpinfo.mudengine.being.model;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The persistent class for the mud_skill database table.
 * 
 */
@Entity
@Table(name="mud_skill")
@Data
@EqualsAndHashCode(of= {"code"})
public class MudSkill implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String code;

	private String description;

	private String name;

	//bi-directional many-to-one association to MudSkillCategory
	@ManyToOne
	@JoinColumn(name="category_code")
	private MudSkillCategory category;
}