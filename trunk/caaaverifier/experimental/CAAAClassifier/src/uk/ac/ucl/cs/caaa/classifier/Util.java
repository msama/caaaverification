/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * @author rax
 *
 */
public class Util {
	
	
	/**
	 * Uninstantiable. Utility class.
	 */
	private Util() {}

	/**
	 * Load a training set from a given Arff file. The specified file 
	 * must follow the specification at
	 * {@link http://www.cs.waikato.ac.nz/~ml/weka/arff.html}
	 * 
	 * @param filename the training file name.
	 * @return the loaded {@link Instances}.
	 * @throws IOException if the file is not following the spec.
	 */
	public static Instances loadInstancesFromArff(String filename) throws IOException {
		ArffLoader loader = new ArffLoader();
		loader.setFile(new File(filename));
		
		Instances trainingSet =  loader.getDataSet();

		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		return trainingSet;
	}
	
	/**
	 * @param attribute
	 * @return
	 */
	public static List<String> getValueFromAttribute(Attribute attribute) {
		List<String> values = new ArrayList<String>();
		Enumeration<?> classesEnum = attribute.enumerateValues();
		while(classesEnum.hasMoreElements())
		{
			values.add(classesEnum.nextElement().toString());
		}
		return values;
	}
	
	/**
	 * @param dataSet
	 * @return
	 */
	public static FastVector getAttributeFromInstances(Instances dataSet) {
		FastVector attributes = new FastVector();
		for (int i = 0; i < dataSet.numAttributes(); i++) {
			attributes.addElement(dataSet.attribute(i));
		}
		
		return attributes;
	}
	
	
	public static String[] getAttributeNames(Instances dataSet) {
		String[] attributeNames = new String[dataSet.numAttributes()];
		Enumeration<?> attributesEnum = dataSet.enumerateAttributes();
		int i = 0;
		while(attributesEnum.hasMoreElements())
		{
			attributeNames[i] = ((Attribute) attributesEnum.nextElement()).name();
			i++;
		}
		return attributeNames;
	}
	
	/**
	 * @param dataSet
	 * @return
	 */
	public static List<DataSource<?>> createDataSourcesFromInstances(Instances dataSet) {
		List<DataSource<?>> sources = new ArrayList<DataSource<?>>();
		
		Enumeration<?> attributesEnum = dataSet.enumerateAttributes();
		while(attributesEnum.hasMoreElements())
		{
			Attribute att = (Attribute) attributesEnum.nextElement();
			if (att.isNominal()) {
				DataSource<String> s = new DataSource<String>(att.name());
				sources.add(s);
			} else if (att.isNumeric()) {
				DataSource<Double> s = new DataSource<Double>(att.name());
				sources.add(s);
			} else {
				throw new IllegalStateException("Only Nominal and Numeric attributes are supported.");
			}
		}
		
		return sources;
	}
	
	protected static List<ContextGenerator> createAndStartMultipleGenerator(List<DataSource<?>> sources, long refreshRate, long refreshDelta) {
		List<ContextGenerator> generators = new ArrayList<ContextGenerator>();
		for (DataSource<?> d : sources) {
			ContextGenerator g = new RandomContextGenerator(refreshRate, refreshDelta, (DataSource<Double>)d);
			g.setGenerating(true);
			generators.add(g);
		}
		return generators;
	}
	
	protected static ContextGenerator createAndStartSingleGenerator(List<DataSource<?>> sources, long refreshRate, long refreshDelta) {
		DataSource<Double>[] sourceArray = sources.toArray(new DataSource[sources.size()]);
		ContextGenerator generator = new UpDownContextGenerator(refreshRate, refreshDelta, sourceArray);
		generator.setGenerating(true);
		return generator;
	}
}
