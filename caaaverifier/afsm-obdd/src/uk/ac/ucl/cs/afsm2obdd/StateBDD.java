/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ucl.cs.afsm.common.Rule;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.BDDVarSet;

/**
 * @author -RAX- (Michele Sama)
 *
 */
public class StateBDD {

	private final String name;
	private final BDD encoding;
	private final BDDVarSet encodingVarSet;
	
	private final BDD futureEncoding;
	private final BDDVarSet futureEncodingVarSet;
	
	private BDD inStateCondition;
	private int priority = Rule.DEFAULT_PRIORITY;
	private BDD inStateAssumption;
	public final List<RuleBDD> outGoingRules = new ArrayList<RuleBDD>();
	
	public BDD activation;
	
	/**
	 * @param encoding
	 */
	public StateBDD(String name, BDD encoding, BDD futureEncoding) {
		this.name = name;
		this.encoding = encoding;
		encodingVarSet = this.encoding.toVarSet();
		this.futureEncoding = futureEncoding;
		futureEncodingVarSet = futureEncoding.toVarSet();
	}

	/**
	 * @return the inStateCondition
	 */
	public BDD getInStateCondition() {
		return inStateCondition;
	}

	/**
	 * @param inStateCondition the inStateCondition to set
	 */
	public void setInStateCondition(BDD inStateCondition) {
		this.inStateCondition = inStateCondition;
	}

	/**
	 * @return the inStateAssumption
	 */
	public BDD getInStateAssumption() {
		return inStateAssumption;
	}

	/**
	 * @param inStateAssumption the inStateAssumption to set
	 */
	public void setInStateAssumption(BDD inStateAssumption) {
		this.inStateAssumption = inStateAssumption;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the encoding
	 */
	public BDD getEncoding() {
		return encoding;
	}

	/**
	 * @return the encodingVarSet
	 */
	public BDDVarSet getEncodingVarSet() {
		return encodingVarSet;
	}

	/**
	 * @return the futureEncoding
	 */
	public BDD getFutureEncoding() {
		return futureEncoding;
	}

	/**
	 * @return the futureEncodingVarSet
	 */
	public BDDVarSet getFutureEncodingVarSet() {
		return futureEncodingVarSet;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	public void createActivationBDD2 () {
		BDD activation = encoding.getFactory().zero();
		for (int i = Rule.MAX_PRIORITY; i <= Rule.LOW_PRIORITY; i++) {
			BDD activationAtPriority = encoding.getFactory().zero();
						
			BDD inclusion = encoding.getFactory().zero();
			BDD exclusion = encoding.getFactory().zero();
			// Add rules at higher priority
			for (RuleBDD rule : outGoingRules) {
				if (rule.getPriority() == i) {
					BDD activeRule = rule.getTrigger()
						.apply(rule.getEncoding(), BDDFactory.and)
						.apply(rule.getDestination().getFutureEncoding(), BDDFactory.and);
					inclusion = inclusion.apply(activeRule, BDDFactory.or);
				} else if (rule.getPriority() < i) {
					exclusion = exclusion.apply(rule.getTrigger(), BDDFactory.or);
				}
			}
			
			if (priority <= i) {
				exclusion = exclusion.apply(inStateCondition, BDDFactory.or);
			}
			
			activationAtPriority = inclusion.apply(exclusion, BDDFactory.diff);
			activation = activation.apply(activationAtPriority, BDDFactory.or);
		}
		this.activation = activation;
	}
	
	public BDD getActivation() {
		if (activation == null) {
			createActivationBDD2();
		}
		return activation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
