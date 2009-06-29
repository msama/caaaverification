/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

/**
 * @author rax
 *
 */
public abstract class ContextGenerator extends Thread {

	protected DataSource<Double>[] sources; 
	protected boolean generating = false;

	public ContextGenerator(DataSource<Double>... sources) {
		super("ContextGenerator " + sources.toString());
		this.sources = sources;
	}

	public DataSource<Double>[] getSource() {
		return sources;
	}
	
	protected abstract void generateValue();

	public boolean isGenerating() {
		return generating;
	}

	public void setGenerating(boolean generating) {
		if (generating == this.generating) {
			return;
		}
		this.generating = generating;
		if (generating) {
			this.start();
		}
	}

	@Override
	public void run() {
		while (generating) {
			generateValue();
		}
	}
	
}
