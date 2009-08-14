/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.io.PrintStream;
import java.util.*;

/**
 * @author rax
 *
 */
public abstract class Streamable {
	
	protected PrintStream pw;
	// TODO(rax): this is ugly but will do for now.
	protected static int alignment = 0;
	
	public final static Set<RequireKey> definedKeys = new HashSet<RequireKey>();
	
	public final void startWriting(PrintStream pw) {
		this.pw = pw;
		printToStream(pw);
	}
	
	protected final void printToStream(PrintStream pw) {
		this.pw = pw;
		printInternal();
	}
	
	protected abstract void printInternal();

	
	protected void align() {
		pw.print("\n");
		for (int i = 0; i < alignment; i++) {
			pw.print("\t");
		}
	}
	
	protected void writeIntoIfDefined(Streamable st, PrintStream pw) {
		if (st != null) {
			st.printToStream(pw);
			//align();
		}
	}
	
	protected void writeIntoIfDefined(List<? extends Streamable> elements, PrintStream pw) {
		if (elements == null) {
			throw new IllegalArgumentException(
					"This element is required and cannot be null!");
		}
		for (Streamable st : elements) {
			writeIntoIfDefined(st, pw);
		}
	}
	
	protected void writeInto(PrintStream pw, Streamable st) {
		if (st == null) {
			throw new IllegalArgumentException(
					"This element is required and cannot be null!");
		}
		
		st.printToStream(pw);
		//align();
	}
	
	protected <T extends Streamable> void writeSpaceSeparatedList(final List<T> list) {
		int i = 0;
		for (T s : list) {
			writeInto(pw, s);
			if (++i < list.size()) {
				pw.print(" ");
			}
		}
	}
	
	protected <T extends Streamable> void writeSpaceSeparatedList(final T... list) {
		int i = 0;
		for (T s : list) {
			writeInto(pw, s);
			if (++i < list.length) {
				pw.print(" ");
			}
		}
	}
	
	protected <K extends Streamable> void writeAlignedList(Iterable<K> list) {
		++alignment;
		for (K s : list) {
			align();
			writeInto(pw, s);
		}
		--alignment;
		align();
	}

}
