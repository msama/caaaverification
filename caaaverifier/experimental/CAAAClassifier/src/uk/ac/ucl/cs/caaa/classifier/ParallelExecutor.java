/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.util.List;

import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author rax
 *
 */
public class ParallelExecutor extends Executor {

	protected Instance oldValue;
	protected Instance newValue;
	
	
	public ParallelExecutor(Classifier classifier, 
			FastVector attributes, Instances trainingSet, 
			List<String> classes, List<DataSource<?>> sources, 
			long refreshRate, long refreshDelta) {
		super(classifier, attributes, trainingSet, classes, sources, refreshRate, refreshDelta);
	}

	@Override
	public void handleExecution(String refreshedVariable) {
		classifyInstanceOnDemand();
	}



}
