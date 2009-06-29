/**
 * 
 */
package uk.ac.ucl.cs.afsm.common.predicate;

/**
 * @author rax
 *
 */
public abstract class Constant implements Predicate{

	public static final Constant TRUE = new True();
	public static final Constant FALSE = new False();
	
	public static class True extends Constant {} 
	public static class False extends Constant {} 
}
