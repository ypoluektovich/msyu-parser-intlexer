package org.msyu.parser.intlexer;

import java.util.HashMap;
import java.util.Map;

public abstract class ADef {
	ADef() {
	}

	/**
	 * Build a Deterministic Finite Automaton that accepts the language of this definition.
	 */
	public final DFA buildDFA() {
		DfaBuilder builder = process(new HashMap<>());
		return new DFA(builder.basis, builder.transitionTable, builder.terminals);
	}

	final DfaBuilder process(Map<ADef, DfaBuilder> cache) {
		DfaBuilder builder = cache.get(this);
		if (builder == null) {
			builder = new DfaBuilder();
			process(builder, cache);
			builder = new DfaBuilder(builder);
			cache.put(this, builder);
		}
		return builder;
	}

	protected abstract void process(DfaBuilder b, Map<ADef, DfaBuilder> cache);

}
