/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * Defines the effect 
 * 
 * @author rax
 *
 */
public class Action {

	private String id;
	
	private Predicate effect;

	/**
	 * @param id
	 */
	public Action(String id) {
		super();
		this.id = id;
	}

	/**
	 * @param id
	 * @param effect
	 */
	public Action(String id, Predicate effect) {
		super();
		this.id = id;
		this.effect = effect;
	}
	
	/**
	 * @return the effect
	 */
	public Predicate getEffect() {
		return effect;
	}

	/**
	 * @param effect the effect to set
	 */
	public void setEffect(Predicate effect) {
		this.effect = effect;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
