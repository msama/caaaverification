/**
 * 
 */
package uk.ac.ucl.cs.InputMatrix.faultDetection;

import uk.ac.ucl.cs.InputMatrix.validation.ContextHazardDetectionOptimized;


/**
 * @author rax
 *
 */
public class PathFinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		int numVar=5;
		final long expectedIterations=ContextHazardDetectionOptimized.numberOfPermutations(numVar,2,numVar);
		long iterations=0;

		//start the path
		int[] index=new int[]{0,1};
		do{
			
			//increasing size
			while(index.length<=numVar){
				
				if(index.length>=2){
					//output
					StringBuffer bf=new StringBuffer();
					bf.append("{");
					
					for(int bf_i=0;bf_i<index.length;bf_i++)
					{
						bf.append(index[bf_i]);
						if(bf_i!=index.length-1)
						{
							bf.append(',');
						}
					}
					bf.append("}");
					System.out.println(bf.toString());
	
					//+++++
					//cool stuff i am supposed to do
					
					iterations++;
					//+++++
				}
				
				if(index.length+1>numVar)
				{
					break;
				}
				int[] newIndex=new int[index.length+1];
				for(int i=0;i<index.length;i++)
				{
					newIndex[i]=index[i];
				}
				newIndex[index.length]=ContextHazardDetectionOptimized.getSmallestAvilable(index, numVar, index.length);
				index=newIndex;
			}
							
			if(ContextHazardDetectionOptimized.isEnd(index,numVar, index.length)==true)
			{
				break;
			}
			
			//decreasing
			int decreasedIndex=index.length;
			int value;
			boolean stop=false;
			do{
				decreasedIndex--;
				value=index[decreasedIndex];
				
				if(decreasedIndex==0)
				{
					stop=true;
				}
				do{
					value++;	
					if(value<numVar&&!ContextHazardDetectionOptimized.valueInUse(value, numVar, index, decreasedIndex))
					{
						stop=true;
						break;
					}
				}while(value<numVar);
			}while(stop==false); //WRONG!!!
			int[] newIndex=new int[decreasedIndex+1];
			for(int i=0;i<newIndex.length-1;i++)
			{
				newIndex[i]=index[i];
			}
			newIndex[newIndex.length-1]=value;//this.getSmallestAvilable(index, numVar, newIndex.length);
			index=newIndex;
		}
		while(true);
		
		System.out.println("Performed :"+iterations+", Expected: "+expectedIterations);
		assert(iterations==expectedIterations);
		
	}
	


}
