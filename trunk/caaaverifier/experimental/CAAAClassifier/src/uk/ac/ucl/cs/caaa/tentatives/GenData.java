/**
 * 
 */
package uk.ac.ucl.cs.caaa.tentatives;

/**
 * @author rax
 *
 */
public class GenData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//genAAndBEqualsTrue(200);
		genAThenB(100);
	}

	public static void genAAndBEqualsTrue(int n) {
		for (int i = 0; i < n; i++) {
			double a = Math.random();
			double b = Math.random();
			// A&B | !A&!B
			System.out.println(a + ", " + b + ", " + ((a>=0.5 && b>=0.5)||(a<0.5 && b<0.5)));
		}
	}
	
	public static void genAThenB(int n) {
		for (int i = 0; i < n; i++) {
			double a = Math.random();
			double b = Math.random();
	
			// A high or B
			System.out.println(a + ", " + b + ", " + ((a >= 0.5) ? "A" : ((b >= 0.5) ? "B" : "0")));
		}
	}
}
