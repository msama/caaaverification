/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author rax
 *
 */
public class ExecutorBuilder {

	protected Instances trainingSet;
	protected PropertyChangeSupport adaptationEventSupport;
	protected Instance oldValue;
	protected Instance newValue;
	
	protected Classifier classifier;
	protected FastVector attributes;
	protected Attribute classAttribute;
	protected String[] classes;
	protected Map<String, DataHandler> handlers = new HashMap<String, DataHandler>(); 
	protected Map<String, Object> localValues = new HashMap<String, Object>();
	
	
	public ExecutorBuilder setTrainingSet(Instances trainingSet) {
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		this.trainingSet = trainingSet;
		return this;
	}
	public ExecutorBuilder setClassifier(Classifier classifier) {
		try {
			this.classifier = Classifier.makeCopy(classifier);
		} catch (Exception e) {
			// Weka has a really bad exception handling
			throw new RuntimeException(e);
		}
		return this;
	}
	public ExecutorBuilder setAttributes(FastVector attributes) {
		this.attributes = attributes;
		return this;
	}
	public ExecutorBuilder setClassAttribute(Attribute classAttribute) {
		this.classAttribute = classAttribute;
		return this;
	}
	
	public ExecutorBuilder setDataSource(DataSource s, long refreshRate, long refreshDelta) {
		DataHandler handler = new DataHandler(refreshRate, refreshDelta, s);
		//handler.addPropertyChangeListener(s.getId(), this);
		handler.start();
		handlers.put(s.getId(), handler);
		return this;
	}
	
	public ExecutorBuilder setClasses(String[] classes) {
		this.classes = classes;
		return this;
	}
	
	public ExecutorBuilder setClasses( List<String> classes) {
		this.classes = classes.toArray(new String[classes.size()]);
		return this;
	}
}
