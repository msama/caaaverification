/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.io.IOException;
import java.util.*;


import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.FastVector;
import weka.core.Instances;


/**
 * @author rax
 *
 */
public class HazardDetection {

	protected Instances trainingSet;
	protected FastVector attributes;
	protected List<DataSource<?>> sources;
	protected List<String> classValues;
	
	
	protected Classifier classifier;
	protected Oracle oracle;
	protected Executor executor;
	protected Verifier verifier;
	
	protected ContextGenerator generator;
	protected List<ContextGenerator> generators;
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final int attempts = 1000;
			// parallel hold hazard
			
			//thread slower than context
			createAndRunParallel("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts);
			createAndRunParallel("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts);
			
			// same speed
			createAndRunParallel("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts);
			createAndRunParallel("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts);
			
			// context faster
			createAndRunParallel("HoldHazard.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts);
			createAndRunParallel("HoldHazard.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts);
			
			//thread slower than context
			createAndRunParallel("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts);
			createAndRunParallel("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts);
			
			// same speed
			createAndRunParallel("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts);
			createAndRunParallel("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts);
			
			// context faster
			createAndRunParallel("AthenB.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts);
			createAndRunParallel("AthenB.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts);
			
			
			// sequential hold hazard
			//thread slower than context
			/*
			createAndRunSequential("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts);
			createAndRunSequential("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts);
			
			// same speed
			createAndRunSequential("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts);
			createAndRunSequential("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts);
			
			// context faster
			createAndRunSequential("HoldHazard.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts);
			createAndRunSequential("HoldHazard.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts);
			
			//thread slower than context
			createAndRunSequential("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts);
			createAndRunSequential("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts);
			
			// same speed
			createAndRunSequential("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts);
			createAndRunSequential("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts);
			
			// context faster
			createAndRunSequential("AthenB.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts);
			createAndRunSequential("AthenB.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts);
			*/
			
			// clocked hold hazard
			//thread slower than context
			/*createAndRunClocked("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts, 10);
			createAndRunClocked("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts, 10);
			
			// same speed
			createAndRunClocked("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts, 10);
			createAndRunClocked("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts, 10);
			
			// context faster
			createAndRunClocked("HoldHazard.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts, 10);
			createAndRunClocked("HoldHazard.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts, 10);
			
			//thread slower than context
			createAndRunClocked("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts, 10);
			createAndRunClocked("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts, 10);
			
			// same speed
			createAndRunClocked("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts, 10);
			createAndRunClocked("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts, 10);
			
			// context faster
			createAndRunClocked("AthenB.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts, 10);
			createAndRunClocked("AthenB.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts, 10);
			*/
			
			/*
			createAndRunConditionedClocked("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts, 10);
			createAndRunConditionedClocked("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts, 10);
			
			// same speed
			createAndRunConditionedClocked("HoldHazard.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts, 10);
			createAndRunConditionedClocked("HoldHazard.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts, 10);
			
			// context faster
			createAndRunConditionedClocked("HoldHazard.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts, 10);
			createAndRunConditionedClocked("HoldHazard.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts, 10);
			
			//thread slower than context
			createAndRunConditionedClocked("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 10, 0, attempts, 10);
			createAndRunConditionedClocked("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 10, 0, attempts, 10);
			
			// same speed
			createAndRunConditionedClocked("AthenB.arff", new MultilayerPerceptron(), true, 100, 0, 100, 0, attempts, 10);
			createAndRunConditionedClocked("AthenB.arff", new MultilayerPerceptron(), false, 100, 0, 100, 0, attempts, 10);
			
			// context faster
			createAndRunConditionedClocked("AthenB.arff", new MultilayerPerceptron(), true, 10, 0, 100, 0, attempts, 10);
			createAndRunConditionedClocked("AthenB.arff", new MultilayerPerceptron(), false, 10, 0, 100, 0, attempts, 10);
			*/

			} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void createAndRunParallel(String arffFile, Classifier classifier, boolean same, long generationRate, long generationDelta, long refreshRate, long refreshDelta, int attempt) throws Exception {
		HazardDetection hazard = new HazardDetection();
		hazard.loadArffFromFile(arffFile);
		hazard.setClassifier(classifier);
		hazard.createOracle();
		hazard.createParallelExecutor(refreshRate, refreshDelta);
		hazard.setUpVerifier(classifier.getClass().getSimpleName() + "_" + arffFile + "_parallel_"+ (same?"same":"independent") + 
				"_[rr"+ refreshRate + ",rd" + refreshDelta + ",gr" + generationRate + ",gd" + generationDelta +"]" +".csv", 
				attempt);
		if (same) {
			hazard.createAndStartSingleGenerator(generationRate, generationDelta);	
		} else {
			hazard.createAndStartMultipleGenerator(generationRate, generationDelta);
		}
	}
	
	public static void createAndRunSequential(String arffFile, Classifier classifier, boolean same, long generationRate, long generationDelta, long refreshRate, long refreshDelta, int attempt) throws Exception {
		HazardDetection hazard = new HazardDetection();
		hazard.loadArffFromFile(arffFile);
		hazard.setClassifier(classifier);
		hazard.createOracle();
		hazard.createSequentialExecutor(refreshRate, refreshDelta);
		hazard.setUpVerifier(classifier.getClass().getSimpleName() + "_" + arffFile + "_sequential_"+ (same?"same":"independent") + 
				"_[rr"+ refreshRate + ",rd" + refreshDelta + ",gr" + generationRate + ",gd" + generationDelta +"]" +".csv", 
				attempt);
		if (same) {
			hazard.createAndStartSingleGenerator(generationRate, generationDelta);	
		} else {
			hazard.createAndStartMultipleGenerator(generationRate, generationDelta);
		}	
	}
	
	public static void createAndRunClocked(String arffFile, Classifier classifier, boolean same, long generationRate, long generationDelta, long refreshRate, long refreshDelta, int attempt) throws Exception {
		createAndRunClocked(arffFile, classifier, same, generationRate, generationDelta, refreshRate, refreshDelta, attempt, 100);
	}
	
	public static void createAndRunClocked(String arffFile, Classifier classifier, boolean same, long generationRate, long generationDelta, long refreshRate, long refreshDelta, int attempt, long clock) throws Exception {
		HazardDetection hazard = new HazardDetection();
		hazard.loadArffFromFile(arffFile);
		hazard.setClassifier(classifier);
		hazard.createOracle();
		hazard.createClockedExecutor(refreshRate, refreshDelta);
		((ClockedExecutor)hazard.executor).setClock(clock);
		hazard.setUpVerifier(
				classifier.getClass().getSimpleName() + "_" + arffFile + "_clocked_" + (same?"same":"independent") + 
				"_[ck" + clock + ",rr" + refreshRate + ",rd" + refreshDelta + ",gr" + generationRate + ",gd" + generationDelta + "]" + ".csv", 
				attempt);
		if (same) {
			hazard.createAndStartSingleGenerator(generationRate, generationDelta);	
		} else {
			hazard.createAndStartMultipleGenerator(generationRate, generationDelta);
		}	
	}
	
	public static void createAndRunConditionedClocked(String arffFile, Classifier classifier, boolean same, long generationRate, long generationDelta, long refreshRate, long refreshDelta, int attempt, long clock) throws Exception {
		HazardDetection hazard = new HazardDetection();
		hazard.loadArffFromFile(arffFile);
		hazard.setClassifier(classifier);
		hazard.createOracle();
		hazard.createConditionedClockedExecutor(refreshRate, refreshDelta);
		((ConditionedClockedExecutor)hazard.executor).setClock(clock);
		hazard.setUpVerifier(classifier.getClass().getSimpleName() + "_" + arffFile + "_controlledclocked_"+ (same?"same":"independent") + 
				"_[ck" + clock + ",rr" + refreshRate + ",rd" + refreshDelta + ",gr" + generationRate + ",gd" + generationDelta +"]" +".csv", 
				attempt);
		if (same) {
			hazard.createAndStartSingleGenerator(generationRate, generationDelta);	
		} else {
			hazard.createAndStartMultipleGenerator(generationRate, generationDelta);
		}	
	}
	
	public void loadArffFromFile(String filename) throws IOException {
		trainingSet = Util.loadInstancesFromArff(
				HazardDetection.class.getResource(filename).getFile());
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		
		attributes = Util.getAttributeFromInstances(trainingSet);
		sources = Util.createDataSourcesFromInstances(trainingSet);
		classValues = Util.getValueFromAttribute(trainingSet.classAttribute());
		
	}
	
	public void setClassifier(Classifier classifier) throws Exception {
		this.classifier = classifier;
		classifier.buildClassifier(trainingSet);
		System.out.println(classifier.toString());
	}
	
	public void createOracle() throws Exception {
		oracle = Oracle.getOracleForClassifier(
				Classifier.makeCopy(classifier), attributes, this.trainingSet.classAttribute(), sources);
	}
	
	public void createParallelExecutor(long refreshRate, long refreshDelta) throws Exception {
		executor = new ParallelExecutor(
				Classifier.makeCopy(classifier), attributes, trainingSet, classValues, sources, refreshRate, refreshDelta);
	}
	
	public void createSequentialExecutor(long refreshRate, long refreshDelta) throws Exception {
		executor = new SequentialExecutor(
				Classifier.makeCopy(classifier), attributes, trainingSet, classValues, sources, refreshRate, refreshDelta);
	}

	public void createClockedExecutor(long refreshRate, long refreshDelta) throws Exception {
		executor = new ClockedExecutor(
				Classifier.makeCopy(classifier), attributes, trainingSet, classValues, sources, refreshRate, refreshDelta);
	}
	
	public void createConditionedClockedExecutor(long refreshRate, long refreshDelta) throws Exception {
		executor = new ConditionedClockedExecutor(
				Classifier.makeCopy(classifier), attributes, trainingSet, classValues, sources, refreshRate, refreshDelta);
	}
	
	public void setUpVerifier(String outputFilename, int stopAfter) {
		verifier = new Verifier(this, executor, oracle, outputFilename, stopAfter);
	}
	
	public void createAndStartSingleGenerator(long refreshRate, long refreshDelta) {
		//start context generation
		generator = Util.createAndStartSingleGenerator(sources, refreshRate, refreshDelta);			
	}
	
	public void createAndStartMultipleGenerator(long refreshRate, long refreshDelta) {
		//start context generation
		generators = Util.createAndStartMultipleGenerator(sources, 100, 50);
	}
	
	public void terminate() {
		if (generator != null) {
			generator.setGenerating(false);
			generator.interrupt();
		}
		if (generators != null) {
			for (ContextGenerator gen : generators) {
				gen.setGenerating(false);
				gen.interrupt();
			}
		}
		executor.terminateExecution();
	}
}
