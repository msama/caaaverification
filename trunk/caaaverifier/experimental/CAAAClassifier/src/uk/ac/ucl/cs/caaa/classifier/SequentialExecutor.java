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
public class SequentialExecutor extends Executor{


	
	protected EvaluationThread evaluationThread = new EvaluationThread();
	
	
	public SequentialExecutor(Classifier classifier, 
			FastVector attributes, Instances trainingSet, 
			List<String> classes, List<DataSource<?>> sources, 
			long refreshRate, long refreshDelta) {
		super(classifier, attributes, trainingSet, classes, sources, refreshRate, refreshDelta);
		evaluationThread.start();
	}

	@Override
	public void handleExecution(String refreshedVariable) {
		synchronized (evaluationThread) {
			evaluationThread.startComputation = true;
			evaluationThread.notify();
		}
	}
	
	
	private class EvaluationThread extends Thread {
		
		public EvaluationThread() {
			super();
			setName("SequentialExecutor");
			setDaemon(true);
		}

		private volatile boolean startComputation = false;
		
		
		private boolean stopped = false;
		
		public void run() {
			while (!stopped) {		
				synchronized(evaluationThread) {
					while (!startComputation) {
						try {
							wait();
						} catch (InterruptedException e) {
							if (stopped) {
								return;
							}
						}
					}
				}
				
				classifyInstanceOnDemand();
				synchronized(evaluationThread) {
					startComputation = false;
				}
			}
		}
	}

}
