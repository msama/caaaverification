/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

/**
 * @author rax
 *
 */
public class MethodDef extends Streamable {

	private ActionFunctor functor;
	private Name name;
	private TypedList<Variable> parameters;
	private ActionDefBody body;
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.pddlgen.ebnf.Streamable#printInternal()
	 */
	@Override
	protected void printInternal() {
		pw.print("(:method ");
		writeInto(pw, functor);
		++alignment;
		align();
		if (name != null) {
			pw.print("(:name ");
			writeInto(pw, name);
			align();
		}
		pw.print(":parameters (");
		writeInto(pw, parameters);
		pw.print(")");
		writeInto(pw, body);
		
		--alignment;
		align();
		pw.print(")");
	}
	
	public static MethodDef create(ActionFunctor functor, TypedList<Variable> parameters, ActionDefBody body) {
		if (functor == null) {
			throw new IllegalArgumentException("ActionFunctor cannot be null");
		}
		if (parameters == null) {
			throw new IllegalArgumentException("TypedList<Variable> cannot be null");
		}
		if (body == null) {
			throw new IllegalArgumentException("ActionDefBody cannot be null");
		}
		MethodDef method = new MethodDef();
		method.functor = functor;
		method.parameters = parameters;
		method.body = body;
		return method;
	}
	
	public static MethodDef create(ActionFunctor functor, Name name, TypedList<Variable> parameters, ActionDefBody body) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		MethodDef method = create(functor, parameters, body);
		method.name = name;
		return method;
	}
	
}
