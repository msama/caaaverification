/**
 * This class takes a set of independent variables and creates all the possible combinations
 * Arrays of input are stored in a c[][] in which each line is an intance and in which each column is a variable
 * Independent variables are marked as '*'
 */
package uk.ac.ucl.cs.InputMatrix.inputSpaceReduction;

/**
 * @author rax
 *
 */
public class ChartesianCombinator {

	
	public static char[][] combineInputSpace(char s0[][], char s1[][])
	{
		if(s0.length==0) return s1;
		if(s1.length==0) return s0;
		if(s1[0].length!=s0[0].length) throw new RuntimeException("InputSpace size must be the same. Apparently the two spaces have different variables.");
		
		//TODO 
		throw new RuntimeException("Not implemented yet"); 
	}
}
