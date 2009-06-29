/**
 * 
 */
package uk.ac.ucl.cs.afsm2obdd.validation;

import uk.ac.ucl.cs.afsm2obdd.AfsmParser;

/**
 * @author rax
 *
 */
public abstract class Algorithm {
	
	public Fault[] compute(AfsmParser parser) {
		if (!parser.isEncodeAfsmBDD()) {
			return computeLinear(parser);
		} else {
			return computeSymbolic(parser);
		}
	}

	public abstract Fault[] computeLinear(AfsmParser parser);

	public abstract Fault[] computeSymbolic(AfsmParser parser);
}
