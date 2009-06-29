/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.util.*;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Oracle class which knows all the data source and which can 
 * get their values instantaneously.
 * 
 * <p>This class meant to be a twin implementation of the classifier. It is eventually 
 * possible to use a copy of the same classifier and feed it istantaneously. 
 * 
 * @author rax
 *
 * @param <T> the type of the predicted result.
 */
public abstract class Oracle {
	protected Map<String, DataSource<?>> sources = new HashMap<String, DataSource<?>>(); 
	
	public Oracle(DataSource<?>... dataSources) {
		for (DataSource<?> s : dataSources) {
			sources.put(s.getId(), s);
		}
		
	}
	
	/**
	 * Factory method to get an oracle from a classifier.
	 * 
	 * @param classifier
	 * @param attributes
	 * @param classAttribute
	 * @param sources
	 * @return
	 */
	public static Oracle getOracleForClassifier(final Classifier classifier, 
			final FastVector attributes, final Attribute classAttribute,
			List<DataSource<?>> sources) {
		DataSource<?>[] dataSources = sources.toArray(new DataSource<?>[sources.size()]);
		return getOracleForClassifier(classifier, attributes, classAttribute, dataSources);
	}
	
	/**
	 * Factory method which generates an Oracle from a given classifier.
	 * 
	 * @param classifier
	 * @param attributes
	 * @param classAttribute
	 * @param dataSources
	 * @return
	 */
	public static Oracle getOracleForClassifier(final Classifier classifier, 
			final FastVector attributes, final Attribute classAttribute,
			DataSource<?>... dataSources){
		return new Oracle (dataSources) {
			public Instance predictResult() {
				
				Instances liveSet = new Instances("LiveSet", attributes, 1);
				liveSet.setClassIndex(attributes.size()-1);
				
				Instance liveInstance = new Instance(attributes.size());
				liveInstance.setDataset(liveSet);
				
				for (int i = 0; i< attributes.size() - 1; i++) {
					Attribute att = (Attribute) attributes.elementAt(i);
					Object value = 	sources.get(att.name()).getValue();
					if (value == null) {
						liveInstance.setMissing(att);
					} else if (att.isNominal()) {
						liveInstance.setValue(att, value.toString());
					} else if (att.isNumeric()) {
						if (value instanceof Double) {
							liveInstance.setValue(att, ((Double)value).doubleValue());
						} else {
							throw new IllegalStateException("Don't know what to do when Numeric attribute is not double: " + value);
						}
					} else {
						throw new IllegalStateException("Don't know what to do when attribute is neither Numeric or Nominal.");
					}
				}
				
				double result;
				try {
					result = classifier.classifyInstance(liveInstance);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				liveInstance.setClassValue((int)result);
				return liveInstance;
			}
		};
	}
	
	public abstract Instance predictResult(); 
}
