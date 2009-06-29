/**
 * 
 */
package uk.ac.ucl.cs.afsm.common;

/**
 * Identifies a common source of information wich can be used to generate variables.
 * For instance WiFi available networks and WiFi signal level use the same context
 * which conceptually is WiFi. 
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class Context {

	private String name;
	
	/**
	 * 
	 */
	public Context() {
	}

	/**
	 * @param name
	 */
	public Context(String name) {
		this();
		this.name = name;
	}

	/**
	 * State if two instances of {@link Context} identifies the same context
	 * by comparing their names.
	 * 
	 * @param obj the instance with which this instance should be compared.
	 * @return <code>true</code> if <code>obj</code> is a {@link Context} and neither
	 * 	its name or the name of the current instance are <code>null</code>,
	 * 	<code>false</code> otherwise.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Context)) {
			return false;
		}
		Context c = (Context) obj;
		if (name == null || c.name == null) {
			return false;
		}
		return name.equals(c.name);
	}
}
