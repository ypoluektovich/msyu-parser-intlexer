package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class ComplexSeq extends AComplexDef {

	private final List<ADef> elements;

	public ComplexSeq(List<? extends ADef> elements) {
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		this.elements = CopyList.immutable(elements, rs -> requireNonNull(rs, "all elements must be nonnull"));
	}


	@Override
	protected final void process(DfaBuilder b, Map<ADef, DfaBuilder> cache) {
		process(
				b,
				elements.stream()
						.map(innerDef -> innerDef.process(cache))
						.collect(Collectors.toList())
		);
	}

	@Override
	protected final boolean buildInitialState(List<DfaBuilder> elements, int[] stateCountSums, BitSet state) {
		state.set(0);
		return closeEpsilonTransitions(0, 0, elements, stateCountSums, state);
	}

	@Override
	protected final boolean closeEpsilonTransitions(
			int elementState,
			int elementIx,
			List<DfaBuilder> elements,
			int[] stateCountSums,
			BitSet state
	) {
		DfaBuilder element = elements.get(elementIx);
		while (element.terminals.get(elementState) && elementIx < elements.size() - 1) {
			element = elements.get(++elementIx);
			state.set(stateCountSums[elementIx - 1]);
			elementState = 0;
		}
		return elementIx == elements.size() - 1 && element.terminals.get(elementState);
	}


	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != ComplexSeq.class) {
			return false;
		}
		return elements.equals(((ComplexSeq) obj).elements);

	}

	@Override
	public final int hashCode() {
		return elements.hashCode();
	}

}
