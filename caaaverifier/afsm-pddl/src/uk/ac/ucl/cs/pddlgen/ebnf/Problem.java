package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

public class Problem extends Streamable {

	private Name domainName;
	private Name problemName;
	
	private RequireDef requireDef;
	private Situation situation;
	private ObjectDeclaration objectDeclaration;
	private Init init;
	private List<Goal> goals = new ArrayList<Goal>();
	private Constraints constraints;
	private LengthSpec lengthSpec;
	
	
	
	/**
	 * @param domainName
	 * @param problemName
	 * @param requireDef
	 * @param situation
	 * @param objectDeclaration
	 * @param init
	 * @param goals
	 * @param lengthSpec
	 */
	public Problem(Name domainName,
			Name problemName, 
			RequireDef requireDef,
			Situation situation, 
			ObjectDeclaration objectDeclaration,
			Init init, 
			List<Goal> goals,
			Constraints constraints,
			//MetricSpec metricSpec,
			LengthSpec lengthSpec) {
		this.domainName = domainName;
		this.problemName = problemName;
		this.requireDef = requireDef;
		this.situation = situation;
		this.objectDeclaration = objectDeclaration;
		this.init = init;
		this.goals = goals;
		this.constraints = constraints;
		this.lengthSpec = lengthSpec;
	}



	@Override
	protected void printInternal() {
		pw.print("(define (problem " + problemName + ")");
		++alignment;
		align();
		pw.print("(:domain " + domainName + ")");
		align();
		if (requireDef != null) {
			writeInto(pw, requireDef);
			align();
		}
		if (situation != null) {
			writeInto(pw, situation);
			align();
		}
		if (objectDeclaration != null) {
			writeInto(pw, objectDeclaration);
			align();
		}
		if (init != null) {
			writeInto(pw, init);
			align();
		}
		for (Goal g : goals){
			writeInto(pw, g);
			align();
		}
		
		writeIntoIfDefined(constraints, pw);
		align();
		
		if (lengthSpec != null) {
			writeInto(pw, lengthSpec);
			align();
		}
		--alignment;
		align();
		pw.print(')');
	}

	public Name getProblemName() {
		return problemName;
	}

}
