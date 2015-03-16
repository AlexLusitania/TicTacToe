package com.majy.tictactoe.util;

public enum Camp {
	X{
		@Override
		public Camp adv() {
			return O;
		}		
		public String toString(){
			return "o";
		}
	},
	
	O{
		@Override
		public Camp adv() {
			return X;
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