/**
 * 
 */
package uk.ac.ucl.cs.afsm.common.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represent a boolean operator
 * 
 * @author -RAX- (Michele Sama)
 *
 */
public class Operator implements Predicate {

	protected Operator() {
	}
	
	public static Operator not(Predicate predicate) {
		return new Not(predicate);
	}
	
	public static Operator or(Predicate... predicate) {
		return new Or(predicate);
	}
	
	public static Operator or(Collection<Predicate> predicates) {
		return new Or(predicates);
	}
	
	public static Operator and(Predicate... predicate) {
		return new And(predicate);
	}
	
	public static Operator and(Collection<Predicate> predicates) {
		return new And(predicates);
	}
	
	public static final class Not extends Operator {
		
		private Predicate predicate;
		
		public Not() {
		}
		
		public Not(Predicate predicate) {
			this();
			this.predicate = predicate;
		}

		/**
		 * @return the variable
		 */
		public Predicate getPredicate() {
			return predicate;
		}

		/**
		 * @param variable the variable to set
		 */
		public void setPredicate(Predicate predicate) {
			this.predicate = predicate;
		}		
		
	}
	
	public static final class Or extends Operator
			implements Iterable<Predicate> {
		
		private List<Predicate> predicates = new ArrayList<Predicate>();
		
		public Or() {
		}
		
		public Or(Predicate... predicate) {
			this();
			for (Predicate p : predicate) {
				predicates.add(p);
			}
		}
		
		public Or(Collection<Predicate> predicates) {
			this();
			this.predicates.addAll(predicates);
		}

		/**
		 * @param e
		 * @return
		 * @see java.util.List#add(java.lang.Object)
		 */
		public boolean add(Predicate e) {
			return predicates.add(e);
		}

		/**
		 * @param index
		 * @return
		 * @see java.util.List#get(int)
		 */
		public Predicate get(int index) {
			return predicates.get(index);
		}

		/**
		 * @return
		 * @see java.util.List#size()
		 */
		public int size() {
			return predicates.size();
		}	

		@Override
		public Iterator<Predicate> iterator() {
			return predicates.iterator();
		}
		
	}
	
	public static final class And extends Operator
			implements Iterable<Predicate> {
		
		private List<Predicate> predicates = new ArrayList<Predicate>();
		
		public And() {
		}
		
		public And(Predicate... predicate) {
			this();
			for (Predicate p : predicate) {
				predicates.add(p);
			}
		}

		public And(Collection<Predicate> predicates) {
			this();
			this.predicates.addAll(predicates);
		}
		
		/**
		 * @param e
		 * @return
		 * @see java.util.List#add(java.lang.Object)
		 */
		public boolean add(Predicate e) {
			return predicates.add(e);
		}

		/**
		 * @param index
		 * @return
		 * @see java.util.List#get(int)
		 */
		public Predicate get(int index) {
			return predicates.get(index);
		}

		/**
		 * @return
		 * @see java.util.List#size()
		 */
		public int size() {
			return predicates.size();
		}

		@Override
		public Iterator<Predicate> iterator() {
			return predicates.iterator();
		}	
		
	}
}
