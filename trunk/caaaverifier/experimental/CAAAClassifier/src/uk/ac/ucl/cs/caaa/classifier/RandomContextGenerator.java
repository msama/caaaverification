/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

/**
 * @author rax
 *
 */
public class RandomContextGenerator extends ContextGenerator {

	private long refreshRate = 1;
	private long refreshDelta = 0;
	
	/**
	 * @param source
	 */
	public RandomContextGenerator(long refreshRate, long refreshDelta, DataSource<Double>... sources) {
		super(sources);
		this.refreshRate = refreshRate;
		this.refreshDelta = refreshDelta;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.caaa.classifier.ContextGenerator#generateValue()
	 */
	@Override
	protected void generateValue() {
		try {
			double value = Math.random();
			for (DataSource<Double> d : sources) {
				d.setValue(Double.valueOf(value));
			}
			long delay = (long)(refreshRate + refreshDelta * (0.5 - Math.random()));
			if (delay < 0) {
				delay = 1;
			}
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// nothign to be done.
		}
	}

}
