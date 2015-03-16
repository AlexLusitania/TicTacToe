package com.majy.tictactoe.model;

import com.majy.tictactoe.util.Camp;
import com.majy.tictactoe.util.JoueurType;

public class Joueur {
	private JoueurType type;
	private Camp camp;
	private String name;
	
	public Joueur(JoueurType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Joueur(JoueurType type, String name, Camp camp) {
		this(type, name);
		this.camp = camp;
	}
	
	public JoueurType getType() {
		return type;
	}
	public void setType(JoueurType type) {
		this.type = type;
	}
	public Camp getCamp() {
		return camp;
	}
	public void setCamp(Camp camp) {
		this.camp = camp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
}
