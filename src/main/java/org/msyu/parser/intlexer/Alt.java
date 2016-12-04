package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class Alt extends AComplexDef {

	private final List<ADef> alternatives;

	public Alt(List<? extends ADef> alternatives) {
		if (alternatives.isEmpty()) {
			throw new IllegalArgumentException("alternatives list must not be empty");
		}
		this.alternatives = CopyList.immutable(alternatives, rs -> requireNonNull(rs, "all alternatives must be nonnull"));
	}


	@Override
	protected final void process(DfaBuilder b, Map<ADef, DfaBuilder> cache) {
		process(
				b,
				alternatives.stream()
						.map(innerDef -> innerDef.process(cache, false))
						.collect(Collectors.toList())
		);
	}

	@Override
	protected final boolean buildInitialState(List<DfaBuilder> elements, int[] stateCountSums, BitSet state, Set<Integer> terminatedElements) {
		boolean terminal = false;
		int jointStateIx = 0;
		for (int elementIx = 0; elementIx < stateCountSums.length; ++elementIx) {
			state.set(jointStateIx);
			jointStateIx = stateCountSums[elementIx];
			boolean elementTerminated = closeEpsilonTransitions(0, elementIx, elements, stateCountSums, state);
			if (elementTerminated) {
				if (terminatedElements != null) {
					terminatedElements.add(elementIx);
				}
				terminal = true;
			}
		}
		return terminal;
	}

	@Override
	protected final boolean closeEpsilonTransitions(
			int elementState,
			int elementIx,
			List<DfaBuilder> elements,
			int[] stateCountSums,
			BitSet state
	) {
		return elements.get(elementIx).terminals.get(elementState);
	}


	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != Alt.class) {
			return false;
		}
		return alternatives.equals(((Alt) obj).alternatives);

	}

	@Override
	public final int hashCode() {
		return alternatives.hashCode();
	}

}
