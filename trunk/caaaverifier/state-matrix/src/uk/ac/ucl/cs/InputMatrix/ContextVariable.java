/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix;

import java.util.Vector;

/**
 * @author rax
 *
 */
public class ContextVariable {

	private String _name;
	private long _refreshRate;

	protected Vector<PredicateVariable> _ownedVariables=new Vector<PredicateVariable>();

	/**
	 * @param _name
	 */
	public ContextVariable(String _name, long refresh) {
		super();
		this._name = _name;
		this._refreshRate=refresh;
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

	/**
	 * @param obj
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	public void addVariable(PredicateVariable obj) {
		_ownedVariables.addElement(obj);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.Vector#elementAt(int)
	 */
	public PredicateVariable variableAt(int index) {
		return _ownedVariables.elementAt(index);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.util.Vector#removeElement(java.lang.Object)
	 */
	public boolean removeVariable(Object obj) {
		return _ownedVariables.removeElement(obj);
	}

	/**
	 * @return
	 * @see java.util.Vector#size()
	 */
	public int size() {
		return _ownedVariables.size();
	}



	public long getRefreshRate() {
		return _refreshRate;
	}
	
	/**
	 * Factory method for {@link ContextVariable}s.
	 * 
	 * @param afsm
	 * @param name
	 * @param refresh
	 * @return
	 */
	public static ContextVariable context(AdaptationFSM afsm, String name, long refresh) {
		ContextVariable cv = new ContextVariable(name, refresh);
		afsm.addContextVariable(cv);
		return cv;
	}
}
