/**
 * 
 */
package uk.ac.ucl.cs.caaa.tentatives;

import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.classifiers.trees.NBTree;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author rax
 *
 */
public class MainTentative1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Classifier classifier = new NBTree();
		
		FastVector classValues = new FastVector(3);
		classValues.addElement("Walking");
		classValues.addElement("Driving");
		Attribute classAttribute = new Attribute("State", classValues);
		
		Attribute speed = new Attribute("Speed");
		Attribute bt = new Attribute("Bluetooth");
		FastVector attributes = new FastVector();
		attributes.addElement(speed);
		attributes.addElement(bt);
		attributes.addElement(classAttribute);
		Instances trainingSet = new Instances("TestSet", attributes, 2);
		trainingSet.setClass(classAttribute);
		
		Instance instance1 = new Instance(3);
		instance1.setDataset(trainingSet);
		instance1.setValue(speed, 5);
		instance1.setMissing(bt);
		instance1.setClassValue("Walking");
		
		Instance instance2 = new Instance(3);
		instance2.setDataset(trainingSet);
		instance2.setValue(speed, 50);
		instance2.setMissing(bt);
		instance2.setClassValue("Driving");
		
		
		
		trainingSet.add(instance1);
		trainingSet.add(instance2);
		
		try {
			classifier.buildClassifier(trainingSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(classifier.toString());
		
		Instance live = new Instance(3);
		live.setDataset(trainingSet);
		live.setValue(speed, 5);
		live.setMissing(bt);
		try {
			double result = classifier.classifyInstance(live);
			double[] resultSet = classifier.distributionForInstance(live);
			
			System.out.println("Instance " + live + " classified as " + classValues.elementAt((int)result));
			System.out.println(Arrays.toString(resultSet));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
}
