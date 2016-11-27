package org.msyu.parser.intlexer;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class Rpt extends AComplexDef {

	private final ADef repeated;

	public Rpt(ADef repeated) {
		this.repeated = requireNonNull(repeated, "repeated def must be nonnull");
	}


	@Override
	protected final void process(DfaBuilder b, Map<ADef, DfaBuilder> cache) {
		process(b, Collections.singletonList(repeated.process(cache)));
	}

	@Override
	protected final boolean buildInitialState(List<DfaBuilder> elements, int[] stateCountSums, BitSet state) {
		state.set(0);
		return true;
	}

	@Override
	protected final boolean closeEpsilonTransitions(
			int elementState,
			int elementIx,
			List<DfaBuilder> elements,
			int[] stateCountSums,
			BitSet state
	) {
		if (elements.get(elementIx).terminals.get(elementState)) {
			state.set(0);
			return true;
		}
		return false;
	}


	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != Rpt.class) {
			return false;
		}
		return repeated.equals(((Rpt) obj).repeated);

	}

	@Override
	public final int hashCode() {
		return repeated.hashCode() + 1;
	}

}
