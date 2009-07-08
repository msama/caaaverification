/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class DomainVarsDef extends Streamable {

	TypedList<DomainVarDeclaration> typedList;
	
	private DomainVarsDef() {
		
	}

	@Override
	protected void printInternal() {
		pw.print("(:domain-variables ");
		writeInto(pw, typedList);
		pw.print(")");
	}

	public static DomainVarsDef create(TypedList<DomainVarDeclaration> typedList) {
		if (typedList == null) {
			throw new IllegalStateException(
					"Statement <domain-variables> must have a <typed list (domain-var-declaration)>.");
		}
		DomainVarsDef vars = new DomainVarsDef();
		vars.typedList = typedList;
		return vars;
	} 
}
