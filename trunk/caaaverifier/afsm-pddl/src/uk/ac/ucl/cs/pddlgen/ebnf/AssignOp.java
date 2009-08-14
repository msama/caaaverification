/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public abstract class AssignOp extends Streamable {

	public static AssignOp assign() {
		return new AssignOp(){
			@Override
			protected void printInternal() {
				pw.print("assign");
			}
		};
	}
	
	public static AssignOp scaleUp() {
		return new AssignOp(){
			@Override
			protected void printInternal() {
				pw.print("scale-up");
			}
		};
	}

	public static AssignOp scaleDown() {
		return new AssignOp(){
			@Override
			protected void printInternal() {
				pw.print("scale-down");
			}
		};
	}
	
	public static AssignOp increase() {
		return new AssignOp(){
			@Override
			protected void printInternal() {
				pw.print("increase");
			}
		};
	}
	
	public static AssignOp decrease() {
		return new AssignOp(){
			@Override
			protected void printInternal() {
				pw.print("decrease");
			}
		};
	}
}
