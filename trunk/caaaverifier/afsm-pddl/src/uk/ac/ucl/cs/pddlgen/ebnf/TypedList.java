/**
 * 
 */
package uk.ac.ucl.cs.pddlgen.ebnf;

import java.util.List;

/**
 * @author rax
 *
 */
public abstract class TypedList<T extends Streamable> extends Streamable {
	
	public static<T extends Streamable> TypedList<T> create(final List<T> names) {
		if (names == null) {
			throw new IllegalArgumentException("Names cannot be null!");
		}
		if (names.size() > 0) {
			throw new IllegalArgumentException("Names must contain at least a name!");
		}
		TypedList<T> typedList = new TypedList<T>() {
			@Override
			protected void printInternal() {
				writeInto(names);
			}
		};
		return typedList;
	}
	
	public static<T extends Streamable> TypedList<T> create(final List<T> names, final Type type, final TypedList<T> tail) {
		if (names == null) {
			throw new IllegalArgumentException("Names cannot be null!");
		}
		if (names.size() > 0) {
			throw new IllegalArgumentException("Names must contain at least a name!");
		}
		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null!");
		}
		if (!definedKeys.contains(RequireKey.TYPING)) {
			throw new IllegalStateException(
			"Statement <type> requires <require-key> :typing.");
		}
		TypedList<T> typedList = new TypedList<T>() {
			@Override
			protected void printInternal() {
				writeInto(names);
				pw.print(" - ");
				writeInto(type);
				writeIntoIfDefined(tail);
			}
		};
		return typedList;
	}
}
