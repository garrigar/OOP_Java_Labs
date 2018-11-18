package myfirstpackage;

public class MySecondClass {
	private int eins;
	private int zwei;
	
	public int getEins() {
		return eins; 
	}
	public int getZwei() { 
		return zwei; 
	}
	public void setEins(int x) { 
		eins = x; 
	}
	public void setZwei(int x) { 
		zwei = x; 
	}
	
	public MySecondClass(int a, int b){
		eins = a;
		zwei = b;
	}
	
	public int act() {
		return eins + zwei;
	}
}