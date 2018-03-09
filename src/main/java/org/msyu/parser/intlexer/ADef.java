package org.msyu.parser.intlexer;

import java.util.HashMap;
import java.util.Map;

public abstract class ADef {
	ADef() {
	}

	/**
	 * Get the length of all strings matching this definition (if defined).
	 * @return the length, or {@code -1} if the length varies.
	 */
	public abstract int getLength();

	/**
	 * Check whether this definition matches the empty string.
	 */
	public abstract boolean isNullable();

	/**
	 * Build a Deterministic Finite Automaton that accepts the language of this definition.
	 */
	public final DFA buildDFA() {
		DfaBuilder builder = process(new HashMap<>(), true);
		return new DFA(builder.basis, builder.transitionTable, builder.terminals, builder.elementsByTerminal);
	}

	final DfaBuilder process(Map<ADef, DfaBuilder> cache, boolean trackTerminals) {
		DfaBuilder builder = cache.get(this);
		if (builder == null) {
			builder = new DfaBuilder(trackTerminals);
			process(builder, cache);
			builder = new DfaBuilder(builder);
			cache.put(this, builder);
		}
		return builder;
	}

	protected abstract void process(DfaBuilder b, Map<ADef, DfaBuilder> cache);

}
