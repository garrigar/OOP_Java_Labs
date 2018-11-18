class MyFirstClass {
	public static void main(String[] args) {
		MySecondClass o = new MySecondClass(0, 0);
		int i, j;
		for (i = 1; i <= 8; i++) {
			for(j = 1; j <= 8; j++) {
				o.setEins(i);
				o.setZwei(j);
				System.out.print(o.act());
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}

class MySecondClass {
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