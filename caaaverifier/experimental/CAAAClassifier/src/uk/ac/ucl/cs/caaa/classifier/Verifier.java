/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import weka.core.Instance;

/**
 * @author rax
 *
 */
public class Verifier implements PropertyChangeListener {

	private HazardDetection hazardDetection;
	
	private int stopAfter = 0;
	private PrintWriter pw;
	private int matching = 0;
	private int failures = 0;
	
	private Executor executor;
	private Oracle oracle;

	public Verifier(HazardDetection hazard, Executor executor, Oracle oracle, String filename,  int stopAfter) {
		super();
		this.hazardDetection = hazard;
		this.executor = executor;
		this.oracle = oracle;
		this.stopAfter = stopAfter;
				
		try {
			pw = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		this.executor.adaptationEventSupport.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (Executor.ADAPTATION_EVENT.equals(evt.getPropertyName())) {
			Instance obtainedClass = (Instance) evt.getNewValue();
			Instance predictedInstance = oracle.predictResult();
			pw.print(obtainedClass + "; ");
			pw.print(predictedInstance + "; ");
			
			if (obtainedClass.classValue() != predictedInstance.classValue()) {
				failures ++;
				pw.println("false; ");
				System.err.println("Found: " + obtainedClass + " predicted: " + predictedInstance + " WARNING");
			} else {
				matching ++;
				pw.println("true; ");
				System.out.println("Found: " + obtainedClass + " predicted: " + predictedInstance);
			}
			
			stopAfter--;
			if (stopAfter == 0) {
				performTermination();
			}
		}
	}
	
	public void performTermination() {
		pw.print("Summary true = " + matching + "; ");
		pw.println("false = " + failures + "; ");
		pw.flush();
		pw.close();
		hazardDetection.terminate();
	}
	
}
