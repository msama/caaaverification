/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public abstract class InitEl extends Streamable {

	public static InitEl create(final Literal<Name> literal) {
		return new InitEl(){
			@Override
			protected void printInternal() {
				writeInto(pw, literal);
			}
		};
	}
	
	public static InitEl create(final FHead fHead, final Number number) {
		if (!definedKeys.contains(RequireKey.FLUENTS)) {
			throw new IllegalStateException(":fluents is required.");
		}
		return new InitEl(){
			@Override
			protected void printInternal() {
				pw.print("(= ");
				writeInto(pw, fHead);
				pw.print(" ");
				writeInto(pw, number);
				pw.print(")");
			}
		};
	}

	// TODO(rax): Add timed initial literals
}
