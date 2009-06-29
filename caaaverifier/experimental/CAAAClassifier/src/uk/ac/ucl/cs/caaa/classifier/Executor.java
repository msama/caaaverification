/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;


import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Executor represent an abstract implementation of the Adaptation Manager
 * of a CAAA. 
 * 
 * @author rax
 *
 */
public abstract class Executor implements PropertyChangeListener{

	public static final String ADAPTATION_EVENT = "AdaptationEvent";
	
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
	
	private Executor() {
		adaptationEventSupport = new PropertyChangeSupport(this);
	}
	
	public Executor(Classifier classifier, FastVector attributes, 
			Instances trainingSet, List<String> classes, 
			List<DataSource<?>> sources, long refreshRate, long refreshDelta) {
		this();
		this.trainingSet = trainingSet;
		
		try {
			this.classifier = Classifier.makeCopy(classifier);
		} catch (Exception e) {
			// Weka has a really bad exception handling
			throw new RuntimeException(e);
		}
		
		this.attributes = attributes;
		
		for (DataSource<?> s : sources) {
			DataHandler handler = new DataHandler(refreshRate, refreshDelta, s);
			handler.addPropertyChangeListener(s.getId(), this);
			handler.start();
			handlers.put(s.getId(), handler);
		}
		
		for (int i = 0; i < attributes.size() - 1; i++) {
			Attribute att = (Attribute)attributes.elementAt(i);
			if (handlers.get(att.name()) == null) {
				throw new IllegalStateException("No DataSource for attribute " + att.name() + " has been defined.");
			}
		}
		
		this.classes = classes.toArray(new String[classes.size()]);
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		synchronized(localValues) {
			localValues.put(evt.getPropertyName(), evt.getNewValue());
		}
		handleExecution(evt.getPropertyName());
	}
	
	
	public void terminateExecution() {
		for (DataHandler h : handlers.values()) {
			h.stopDataGeneration();
			h.interrupt();
		}
	}
	/**
	 * Process the local copy of values.
	 */
	public abstract void handleExecution(String refreshedVariable);
	
	protected void classifyInstanceOnDemand() {
		Instances liveSet = new Instances("LiveSet", attributes, 1);
		liveSet.setClassIndex(attributes.size()-1);
		
		Instance instance = new Instance(attributes.size());
		instance.setDataset(liveSet);
		synchronized(localValues) {
			for (int i = 0; i < attributes.size() - 1; i++) {
				Attribute att = (Attribute)attributes.elementAt(i); 
				Object value = localValues.get(att.name());
				if (value instanceof Double) {
					instance.setValue(att, ((Double) value).doubleValue());
				} else if (value instanceof String) {
					instance.setValue(att, (String) value);
				} else if (value == null){
					instance.setMissing(att);
				} else {
					throw new IllegalStateException("Don't know what to do when attribute is neither Numeric or Nominal: " + value);
				}
			}
		}
		try {
			int result = (int)classifier.classifyInstance(instance);
			instance.setClassValue(classes[result]);
			oldValue = newValue;
			newValue = instance;
			adaptationEventSupport.firePropertyChange(ADAPTATION_EVENT, oldValue, newValue); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
