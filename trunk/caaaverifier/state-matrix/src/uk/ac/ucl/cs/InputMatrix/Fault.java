/**
 * Generic object containing a generic fault.
 * Subclasses may contain reference to states.
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

/**
 * @author rax
 *
 */
public class Fault {

	private String _input;
	
	private String _name;

	/**
	 * @param _input
	 * @param _name
	 */
	public Fault(String _input, String _name) {
		super();
		this.setInput(_input);
		this.setName(_name);
	}

	/**
	 * @param _input the _input to set. A String {0,1,*}* 
	 */
	public void setInput(String _input) {
		this._input = _input;
	}

	/**
	 * @return the _input
	 */
	public String getInput() {
		return _input;
	}

	/**
	 * @param _name the _name to set
	 */
	public void setName(String _name) {
		this._name = _name;
	}

	/**
	 * @return the _name
	 */
	public String getName() {
		return _name;
	}

	@Override
	public String toString() {
		return "Fault "+this.getName()+" with input "+this.getInput();
	}
	
}
