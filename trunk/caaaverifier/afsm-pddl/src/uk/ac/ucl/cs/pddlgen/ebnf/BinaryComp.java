package uk.ac.ucl.cs.pddlgen.ebnf;

public abstract class BinaryComp extends Streamable {

	public static BinaryComp greater() {
		return new BinaryComp() {
			@Override
			protected void printInternal() {
				pw.print(">");
			}
		};
	}
	
	public static BinaryComp lesser() {
		return new BinaryComp() {
			@Override
			protected void printInternal() {
				pw.print("<");
			}
		};
	}
	
	public static BinaryComp equals() {
		return new BinaryComp() {
			@Override
			protected void printInternal() {
				pw.print("=");
			}
		};
	}
	
	public static BinaryComp greaterEquals() {
		return new BinaryComp() {
			@Override
			protected void printInternal() {
				pw.print(">=");
			}
		};
	}
	
	public static BinaryComp lesserEquals() {
		return new BinaryComp() {
			@Override
			protected void printInternal() {
				pw.print("<=");
			}
		};
	}

}
