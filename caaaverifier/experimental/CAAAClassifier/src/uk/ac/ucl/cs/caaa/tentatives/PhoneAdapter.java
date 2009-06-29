/**
 * 
 */
package uk.ac.ucl.cs.caaa.tentatives;

import java.io.File;
import java.io.IOException;
import java.util.*;

import uk.ac.ucl.cs.caaa.classifier.Util;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 * @author rax
 *
 */
public class PhoneAdapter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArffLoader loader = new ArffLoader();
		try {
			loader.setFile(new File(PhoneAdapter.class.getResource("PhoneAdapter.arff").getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Instances trainingSet = null;
		try {
			trainingSet = loader.getDataSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		
		Classifier classifier = new MultilayerPerceptron();	
		
		try {
			classifier.buildClassifier(trainingSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Extract classes
		List<String> classValues = Util.getValueFromAttribute(trainingSet.classAttribute());
		
		System.out.println(classifier.toString());
		
		// Idle, False, 0, 0, 0, True, 2, {phone1, phone2}, Empty --> Idle 
		Instance live = new Instance(3);
		live.setDataset(trainingSet);
		live.setValue(0, "Idle");
		live.setValue(1, "False");
		live.setValue(2, 0);
		live.setValue(3, 0);
		live.setValue(4, 0);
		live.setValue(5, "True");
		live.setValue(6, 2);
		live.setValue(7, "{phone1, phone2}");
		live.setValue(8, "Empty");
		
		try {
			double result = classifier.classifyInstance(live);
			double[] resultSet = classifier.distributionForInstance(live);
			
			System.out.println("Instance " + live + " classified as " + classValues.get((int)result));
			System.out.println(Arrays.toString(resultSet));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
