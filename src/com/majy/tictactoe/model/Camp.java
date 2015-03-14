package com.majy.tictactoe.model;

public enum Camp {
	JOUER{
		@Override
		public Camp adv() {
			return CPU;
		}		
		public String toString(){
			return "o";
		}
	},
	
	CPU{
		@Override
		public Camp adv() {
			return JOUER;
		}
		public String toString(){
			return "x";
		}
	},
	
	VIDE{
		@Override
		public Camp adv() {
			return null;
		}
		public String toString(){
			return "-";
		}
	};
	
	public abstract Camp adv();
}