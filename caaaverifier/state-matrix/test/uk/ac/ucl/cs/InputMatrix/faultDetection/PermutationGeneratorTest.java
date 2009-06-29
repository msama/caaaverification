/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.faultDetection;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ucl.cs.InputMatrix.validation.PermutationGenerator;

/**
 * @author michsama
 *
 */
public class PermutationGeneratorTest {

	protected static void print(int[] val)
	{
		System.out.print("{");
		for(int i=0;i<val.length;i++)
		{
			System.out.print(val[i]);
			if(i<val.length-1)
			{
				System.out.print(",");
			}
		}
		System.out.println("}");
	}
	
	
	protected void assertSetValidity(int[] val)
	{
		for(int i=0;i<val.length-1;i++)
		{
			for(int j=i+1;j<val.length;j++)
			{
				assertFalse(val[i]==val[j]);
			}
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.PermutationGenerator#getNext()}.
	 */
	@Test
	public void testGetNext() {
		PermutationGenerator gen=new PermutationGenerator(4);
		
		while(gen.hasNext())
		{
			int[] val=gen.getNext();
			PermutationGeneratorTest.print(val);
			this.assertSetValidity(val);
		}
		
		
	}

	/**
	 * Test method for {@link uk.ac.ucl.cs.InputMatrix.validation.PermutationGenerator#hasNext()}.
	 */
	@Test
	public void testHasNext() {
		PermutationGenerator gen;
		long combinations=0;
		int size=2;

		gen=new PermutationGenerator(size);
		combinations=0;
		
		while(gen.hasNext())
		{
			gen.getNext();
			combinations++;
		}
		System.out.println("Set size="+size+": combination="+gen.numberOfPermutations()+" explored="+combinations);
		assertEquals(gen.numberOfPermutations(),combinations);
		
		size=3;
		gen=new PermutationGenerator(size);
		combinations=0;
		
		while(gen.hasNext())
		{
			gen.getNext();
			combinations++;
		}
		System.out.println("Set size="+size+": combination="+gen.numberOfPermutations()+" explored="+combinations);
		assertEquals(gen.numberOfPermutations(),combinations);
		
		size=4;
		gen=new PermutationGenerator(size);
		combinations=0;
		
		while(gen.hasNext())
		{
			gen.getNext();
			combinations++;
		}
		System.out.println("Set size="+size+": combination="+gen.numberOfPermutations()+" explored="+combinations);
		assertEquals(gen.numberOfPermutations(),combinations);
		
		size=10;
		gen=new PermutationGenerator(size);
		combinations=0;
		
		while(gen.hasNext())
		{
			gen.getNext();
			combinations++;
		}
		System.out.println("Set size="+size+": combination="+gen.numberOfPermutations()+" explored="+combinations);
		assertEquals(gen.numberOfPermutations(),combinations);
		
	}

}
