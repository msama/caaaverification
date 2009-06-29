/**
 * 
 */
package benchmark.obdd;

import benchmark.AfsmBenchMark;
import phoneAdapter.PhoneAdapterAfsm;
import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;
import uk.ac.ucl.cs.afsm2obdd.AfsmParser;
import uk.ac.ucl.cs.afsm2obdd.validation.Fault;
import uk.ac.ucl.cs.afsm2obdd.validation.MetastabilityAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.NondeterministicActivationAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.ReachabilityDetactionAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.RuleLivenessAlgorithm;
import uk.ac.ucl.cs.afsm2obdd.validation.StateLivenessAlgorithm;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class ValidateBenchmark {

	AdaptationFiniteStateMachine afsm;
	AfsmParser linearParser;
	AfsmParser symbolicParser;
	AfsmParser hybridParser;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ValidateBenchmark();
	}

	public ValidateBenchmark() {
		double modelGen[] = new double[2];
		double[][] averageTimes = new double[5][2];
		double t;
		
		int attempts = 10;
		for (int i = 0; i < attempts; i++) {
			afsm = (new AfsmBenchMark(10,40,25)).getAdaptationFiniteStateMachine();
			modelGen[0] -= System.currentTimeMillis();
			linearParser = new AfsmParser(afsm, false);
			modelGen[0] += System.currentTimeMillis();
			modelGen[1] -= System.currentTimeMillis();
			//symbolicParser = new AfsmParser(afsm, true);
			modelGen[1] += System.currentTimeMillis();
			
			NondeterministicActivationAlgorithm nondet = new NondeterministicActivationAlgorithm();
			t = -System.currentTimeMillis();
			//nondet.compute(symbolicParser);
			t += System.currentTimeMillis();
			averageTimes[0][0] += t;
			t = -System.currentTimeMillis();
			nondet.compute(linearParser);
			t += System.currentTimeMillis();
			averageTimes[0][1] += t;
			
			RuleLivenessAlgorithm ruleLiveness = new RuleLivenessAlgorithm();
			t = -System.currentTimeMillis();
			//ruleLiveness.compute(symbolicParser);
			t += System.currentTimeMillis();
			averageTimes[1][0] += t;
			t = -System.currentTimeMillis();
			ruleLiveness.compute(linearParser);
			t += System.currentTimeMillis();
			averageTimes[1][1] += t;
			
			StateLivenessAlgorithm stateLiveness = new StateLivenessAlgorithm();
			t = -System.currentTimeMillis();
			//stateLiveness.compute(symbolicParser);
			t += System.currentTimeMillis();
			averageTimes[2][0] += t;
			t = -System.currentTimeMillis();
			stateLiveness.compute(linearParser);
			t += System.currentTimeMillis();
			averageTimes[2][1] += t;
			
			MetastabilityAlgorithm metastability = new MetastabilityAlgorithm();
			t = -System.currentTimeMillis();
			//metastability.compute(symbolicParser);
			t += System.currentTimeMillis();
			averageTimes[3][0] += t;
			t = -System.currentTimeMillis();
			metastability.compute(linearParser);
			t += System.currentTimeMillis();
			averageTimes[3][1] += t;
			
			ReachabilityDetactionAlgorithm reachability = new ReachabilityDetactionAlgorithm();
			t = -System.currentTimeMillis();
			//reachability.compute(symbolicParser);
			t += System.currentTimeMillis();
			averageTimes[4][0] += t;
			t = -System.currentTimeMillis();
			reachability.compute(linearParser);
			t += System.currentTimeMillis();
			averageTimes[4][1] += t;
			
			linearParser.getFactory().done();
			//symbolicParser.getFactory().done();
		}
		
		modelGen[0] = modelGen[0] / attempts;
		modelGen[1] = modelGen[1] / attempts;
		for (int i = 0; i < averageTimes.length; i++) {
			averageTimes[i][0] = averageTimes[i][0] / attempts;
			averageTimes[i][1] = averageTimes[i][1] / attempts;
		}
		
		System.out.println();
		System.out.println("Average time to generate the linear model(ms): " + modelGen[0]);
		System.out.println("Average time NondeterministicActivationAlgorithm hybrid(ms): " + averageTimes[0][1]);
		System.out.println("Average time RuleLivenessAlgorithm hybrid(ms): " + averageTimes[1][1]);
		//System.out.println("Average time StateLivenessAlgorithm hybrid(ms): " + averageTimes[2][1]);
		System.out.println("Average time MetastabilityAlgorithm hybrid(ms): " + averageTimes[3][1]);
		System.out.println("Average time ReachabilityDetactionAlgorithm hybrid(ms): " + averageTimes[4][1]);
		
		System.out.println();
		System.out.println("Average time to generate the model(ms): " + modelGen[1]);
		System.out.println("Average time NondeterministicActivationAlgorithm symbolic(ms): " + averageTimes[0][0]);
		System.out.println("Average time RuleLivenessAlgorithm symbolic(ms): " + averageTimes[1][0]);
		//System.out.println("Average time StateLivenessAlgorithm symbolic(ms): " + averageTimes[2][0]);
		System.out.println("Average time MetastabilityAlgorithm symbolic(ms): " + averageTimes[3][0]);
		System.out.println("Average time ReachabilityDetactionAlgorithm symbolic(ms): " + averageTimes[4][0]);
		
		System.out.println(modelGen[1] + " & " + averageTimes[0][0] + " & " + averageTimes[1][0] + " & " + averageTimes[3][0] + " & " + averageTimes[4][0] +
				   " & " + modelGen[0] + " & " + averageTimes[0][1] + " & " + averageTimes[1][1] + " & " + averageTimes[3][1] + " & " + averageTimes[4][1]);
	}
	

}
