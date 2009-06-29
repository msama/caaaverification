/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.util.List;

import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * @author rax
 *
 */
public class ClockedExecutor extends Executor {

	private long clock = 200;  
	boolean running = true;
	private Thread handlerThread = new HandlerThread();
	
	/**
	 * @param classifier
	 * @param attributes
	 * @param trainingSet
	 * @param classes
	 * @param sources
	 * @param refreshRate
	 * @param refreshDelta
	 */
	public ClockedExecutor(Classifier classifier, FastVector attributes,
			Instances trainingSet, List<String> classes,
			List<DataSource<?>> sources, long refreshRate, long refreshDelta) {
		super(classifier, attributes, trainingSet, classes, sources,
				refreshRate, refreshDelta);
		handlerThread.start();
	}
	
	public long getClock() {
		return clock;
	}

	public void setClock(long clock) {
		this.clock = clock;
	}


	/* (non-Javadoc)
	 * @see uk.ac.ucl.cs.caaa.classifier.Executor#handleExecution()
	 */
	@Override
	public void handleExecution(String refreshedVariable) {
	}

	private class HandlerThread extends Thread {
		
		public void run() {
			while(running) {
				classifyInstanceOnDemand();
				try {
					Thread.sleep(clock);
				} catch (InterruptedException e) {
					break;
				}
			}
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
}
