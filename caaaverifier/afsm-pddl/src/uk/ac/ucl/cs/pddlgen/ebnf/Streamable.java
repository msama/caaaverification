/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.io.PrintWriter;
import java.util.*;

/**
 * @author rax
 *
 */
public abstract class Streamable {
	
	protected PrintWriter pw;
	protected int alignment = 0;
	
	protected final static Set<RequireKey> definedKeys = new HashSet<RequireKey>();
	
	public final void startWriting(PrintWriter pw) {
		this.pw = pw;
		printToStream();
	}
	
	protected final void printToStream() {
		printInternal();
	}
	
	protected abstract void printInternal();

	
	protected void align() {
		pw.print("\n");
		for (int i = 0; i < alignment; i++) {
			pw.print("\t");
		}
	}
	
	protected void writeIntoIfDefined(Streamable st) {
		if (st != null) {
			st.printToStream();
			align();
		}
	}
	
	protected void writeIntoIfDefined(List<? extends Streamable> elements) {
		if (elements == null) {
			throw new IllegalArgumentException(
					"This element is required and cannot be null!");
		}
		for (Streamable st : elements) {
			writeIntoIfDefined(st);
		}
	}
	
	protected void writeInto(Streamable st) {
		if (st == null) {
			throw new IllegalArgumentException(
					"This element is required and cannot be null!");
		}
		
		st.printToStream();
		align();
	}
	
	protected void writeInto(List<? extends Streamable> elements) {
		if (elements == null) {
			throw new IllegalArgumentException(
					"This element is required and cannot be null!");
		}
		if (elements.size() == 0) {
			throw new IllegalArgumentException(
				"At least one element of this type must be defined!");
		}
		for (Streamable st : elements) {
			writeInto(st);
		}
	}
	
	protected void writeInto(Streamable... elements) {
		if (elements == null) {
			throw new IllegalArgumentException(
					"This element is required and cannot be null!");
		}
		if (elements.length == 0) {
			throw new IllegalArgumentException(
				"At least one element of this type must be defined!");
		}
		for (Streamable st : elements) {
			writeInto(st);
		}
	}
}
