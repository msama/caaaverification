/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rax
 *
 */
public abstract class FunctionTypedList<T extends Streamable> extends Streamable {
	
	public static<K extends Streamable> FunctionTypedList<K> create(final List<K> names) {
		if (names == null) {
			throw new IllegalArgumentException("Names cannot be null!");
		}
		FunctionTypedList<K> functionTypedList = new FunctionTypedList<K>() {
			@Override
			protected void printInternal() {
				super.writeSpaceSeparatedList(names);
			}
		};
		return functionTypedList;
	}
	
	public static<K extends Streamable> FunctionTypedList<K> create(final List<K> names, 
			final Type type, final TypedList<K> tail) {
		if (names == null) {
			throw new IllegalArgumentException("Names cannot be null!");
		}
		if (names.size() < 0) {
			throw new IllegalArgumentException("Names must contain at least a name!");
		}
		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null!");
		}
		if (!definedKeys.contains(RequireKey.TYPING)) {
			throw new IllegalStateException(
			"Statement <type> requires <require-key> :typing.");
		}
		FunctionTypedList<K> functionTypedList = new FunctionTypedList<K>() {
			@Override
			protected void printInternal() {
				super.writeSpaceSeparatedList(names);
				pw.print(" - ");
				writeInto(pw, type);
				if (tail != null) {
					pw.print(" ");
				}
				writeIntoIfDefined(tail, pw);
			}
		};
		return functionTypedList;
	}
}