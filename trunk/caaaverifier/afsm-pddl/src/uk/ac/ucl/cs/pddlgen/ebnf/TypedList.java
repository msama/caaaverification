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
public abstract class TypedList<T extends Streamable> extends Streamable {
	
	public static<K extends Streamable> TypedList<K> create(final List<K> names) {
		if (names == null) {
			throw new IllegalArgumentException("Names cannot be null!");
		}
		if (names.size() < 1) {
			throw new IllegalArgumentException("Names must contain at least a name!");
		}
		TypedList<K> typedList = new TypedList<K>() {
			@Override
			protected void printInternal() {
				super.writeSpaceSeparatedList(names);
			}
		};
		return typedList;
	}
	
	public static<K extends Streamable> TypedList<K> create(final K name, 
			final Type type, final TypedList<K> tail) {
		List<K> list = new ArrayList<K>();
		list.add(name);
		return TypedList.create(list, type, tail);
	}
	
	public static<K extends Streamable> TypedList<K> create(final List<K> names, 
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
		TypedList<K> typedList = new TypedList<K>() {
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
		return typedList;
	}
	
}
