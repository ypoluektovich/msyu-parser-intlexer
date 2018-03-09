package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class ComplexSeq extends AComplexDef {

	private final List<ADef> elements;

	private final int length;

	private final boolean nullable;

	public ComplexSeq(List<? extends ADef> elements) {
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		this.elements = CopyList.immutable(elements, rs -> requireNonNull(rs, "all elements must be nonnull"));

		int length = 0;
		boolean nullable = true;
		for (ADef element : this.elements) {
			int elementLength = element.getLength();
			if (elementLength == -1) {
				length = -1;
			} else if (length != -1) {
				length += elementLength;
			}
			nullable &= element.isNullable();
		}
		this.length = length;
		this.nullable = nullable;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public boolean isNullable() {
		return nullable;
	}


	@Override
	protected final void process(DfaBuilder b, Map<ADef, DfaBuilder> cache) {
		process(
				b,
				elements.stream()
						.map(innerDef -> innerDef.process(cache, false))
						.collect(Collectors.toList())
		);
	}

	@Override
	protected final boolean buildInitialState(List<DfaBuilder> elements, int[] stateCountSums, BitSet state, Set<Integer> terminatedElements) {
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
