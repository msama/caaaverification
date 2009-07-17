package uk.ac.ucl.cs.pddlgen.ebnf;

public abstract class LengthSpec extends Streamable {

	public static LengthSpec createSerial(final int serial) {
		return new LengthSpec(){

			@Override
			protected void printInternal() {
				pw.print("(:length (:serial " + serial + "))");
			}
			
		};
	}
	
	public static LengthSpec createParallel(final int parallel) {
		return new LengthSpec(){

			@Override
			protected void printInternal() {
				pw.print("(:length (:parallel " + parallel + "))");
			}
			
		};
	}
	
	public static LengthSpec createParallel(final int serial, final int parallel) {
		return new LengthSpec(){

			@Override
			protected void printInternal() {
				pw.print("(:length (:serial " + serial + ")) (:parallel " + parallel + "))");
			}
			
		};
	}
	
}
