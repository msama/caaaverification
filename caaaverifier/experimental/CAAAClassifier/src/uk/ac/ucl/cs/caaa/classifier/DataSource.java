/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

/**
 * A container for data before it is used. 
 * This is used to feed the classifier and the oracle.
 * 
 * External access to the data is sync to an internal monitor.
 * 
 * This class represent the physical context and therefore the 
 * variable here contained is a kind of conceptual variable which 
 * should be accessed only by dedicathed components or API such as 
 * {@link DataHandler} 
 * 
 * @author rax
 *
 */
public class DataSource<T> {


	private T value;
	private Object lock = new Object();
	private String id;

	public DataSource(String id) {
		super();
		this.id = id;
	}
	
	/**
	 * Retrieves the stored value.
	 * 
	 * @return the stored value
	 */
	public T getValue() {
		synchronized (lock) {
			return value;
		}
		
	}

	/**
	 * Set the stored value.
	 * 
	 * @param value the new value to assign.
	 */
	public void setValue(T value) {
		synchronized (lock) {
			this.value = value;
			System.out.println(id + " set to " + value);
		}
	}

	/**
	 * @return the id.
	 */
	public String getId() {
		return id;
	}
	
}