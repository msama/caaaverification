/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * @author rax
 *
 */
public class ConditionedClockedExecutor extends Executor {

	private long clock = 200;  
	boolean running = true;
	private Thread handlerThread = new ConditionedHandlerThread();
	private Condition condition;
	
	private Object lock = new Object();
	private volatile boolean needToCompute = false;
	
	long refreshTime[];
	long refreshRates[];
	
	/**
	 * @param classifier
	 * @param attributes
	 * @param trainingSet
	 * @param classes
	 * @param sources
	 * @param refreshRate
	 * @param refreshDelta
	 */
	public ConditionedClockedExecutor(Classifier classifier,
			FastVector attributes, Instances trainingSet, List<String> classes,
			List<DataSource<?>> sources, long refreshRate, long refreshDelta) {
		super(classifier, attributes, trainingSet, classes, sources,
				refreshRate, refreshDelta);
		refreshTime = new long[sources.size()];
		for (int i = 0; i < refreshTime.length; i++) {
			refreshTime[i] = 0;
		}
		
		refreshRates = new long[sources.size()];
		int i = 0;
		for (DataSource<?> s : sources) {
			refreshRates[i++] = handlers.get(s.getId()).getRefreshRate();
		}
		
		condition = new FreshValuesCondition();
		condition.setOwner(this);
		handlerThread.start();
	}

	public long getClock() {
		return clock;
	}

	public void setClock(long clock) {
		this.clock = clock;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.caaa.classifier.Executor#handleExecution(java.lang.String)
	 */
	@Override
	public void handleExecution(String refreshedVariable) {
		for (int i = 0; i < refreshTime.length; i++) {
			Attribute att = (Attribute)attributes.elementAt(i);
			if(refreshedVariable.equals(att.name())) {
				refreshTime[i] = System.currentTimeMillis();
				break;
			}
		}
		synchronized (lock) {
			needToCompute = true;
		}
	}

	
	@Override
	public void terminateExecution() {
		synchronized (handlerThread) {
			if (running) {
				running = false;
				handlerThread.interrupt();
			}
		}
		super.terminateExecution();
	}
	
	private class ConditionedHandlerThread extends Thread {
		
		public void run() {
			while(running) {
				boolean compute;
				if (needToCompute && condition.compute()) {
					classifyInstanceOnDemand();
					synchronized (lock) {
						needToCompute = false;
					}
				}
				try {
					Thread.sleep(clock);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}
