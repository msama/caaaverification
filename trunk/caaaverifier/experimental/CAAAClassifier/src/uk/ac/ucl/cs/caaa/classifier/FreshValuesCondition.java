/**
 * 
 */
package uk.ac.ucl.cs.caaa.classifier;

/**
 * @author rax
 *
 */
public class FreshValuesCondition implements Condition {

	ConditionedClockedExecutor executor;
	
	@Override
	public boolean compute() {
		long time = System.currentTimeMillis();
		for (int i = 0; i < executor.refreshRates.length; i++) {
			if ((time - executor.refreshTime[i]) >= executor.refreshRates[i]/2 ) {
				System.out.println("starv " + (time - executor.refreshTime[i]) + " del " + executor.refreshRates[i]);
				return false; //XXX CREATES STARVATION IF A VARIABLE IS SLOW
			}
		}
		return true;
	}

	@Override
	public void setOwner(ConditionedClockedExecutor executor) {
		this.executor = executor;
	}

}
