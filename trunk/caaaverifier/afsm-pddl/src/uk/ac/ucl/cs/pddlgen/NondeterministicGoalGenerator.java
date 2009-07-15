/**
 * 
 */
package uk.ac.ucl.cs.pddlgen;

import uk.ac.ucl.cs.afsm.common.AdaptationFiniteStateMachine;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class NondeterministicGoalGenerator {

	AdaptationFiniteStateMachine afsm;
	AfsmParser parser;
	
	/**
	 * @param afsm
	 * @param parser
	 */
	public NondeterministicGoalGenerator(AdaptationFiniteStateMachine afsm,
			AfsmParser parser) {
		this.afsm = afsm;
		this.parser = parser;
	}

	

}