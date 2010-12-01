package com.tweakcraft.server.util;

public class Rand {
	
	public static int get(int min,int max){
		if(max <= min)return min;
		
		double rnd = Math.random();
		int diff = max - min;
		int Random = (int)Math.round(diff*rnd);
		return Random+min;
	}
}
