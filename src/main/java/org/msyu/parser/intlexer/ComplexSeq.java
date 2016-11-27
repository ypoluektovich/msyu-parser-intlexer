package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.msyu.parser.intlexer.DFA.NO_TRANSITION;

public final class ComplexSeq extends ADef {

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

	private void process(DfaBuilder b, List<DfaBuilder> elements) {
		int[] stateCountSums = new int[elements.size()];
		int jointStateCount = 0;
		{
			for (int i = 0; i < elements.size(); ++i) {
				DfaBuilder element = elements.get(i);
				b.basis = b.basis == null ? element.basis : RangeSet.basis(b.basis, element.basis);
				jointStateCount += element.stateCount;
				stateCountSums[i] = jointStateCount;
			}
		}

		Map<BitSet, Integer> indexByState = new HashMap<>();
		Queue<BitSet> queue = new ArrayDeque<>();

		BitSet state = BitSet.valueOf(new byte[]{1});
		{
			boolean terminal = false;
			int elementIx = 0;
			DfaBuilder element = elements.get(elementIx);
			int nextElementState = 0;
			while (element.terminals.get(nextElementState) && elementIx < elements.size() - 1) {
				element = elements.get(++elementIx);
				state.set(stateCountSums[elementIx - 1]);
				nextElementState = 0;
			}
			if (elementIx == elements.size() - 1 && element.terminals.get(nextElementState)) {
				terminal = true;
			}
			b.terminals.set(0, terminal);
		}
		indexByState.put(state, b.stateCount);
		++b.stateCount;
		queue.add(state);

		while ((state = queue.poll()) != null) {
			int stateIndex = indexByState.get(state);
			for (int rangeIx = 0; rangeIx < b.basis.size(); ++rangeIx) {
				BitSet nextState = new BitSet(jointStateCount);
				boolean terminal = false;
				for (int substate = state.nextSetBit(0); substate >= 0; substate = state.nextSetBit(substate + 1)) {
					int elementIx = Arrays.binarySearch(stateCountSums, substate);
					int elementState;
					if (elementIx >= 0) {
						++elementIx;
						elementState = 0;
					} else {
						elementIx = -(elementIx + 1);
						elementState = elementIx == 0 ? substate : (substate - stateCountSums[elementIx - 1]);
					}
					DfaBuilder element = elements.get(elementIx);
					int elementRangeIx = element.basis.find(b.basis.getStart(rangeIx));
					int nextElementState = elementRangeIx >= 0 ? element.getTransition(elementState, elementRangeIx) : NO_TRANSITION;
					if (nextElementState != NO_TRANSITION) {
						nextState.set((elementIx == 0 ? 0 : stateCountSums[elementIx - 1]) + nextElementState);
						while (element.terminals.get(nextElementState) && elementIx < elements.size() - 1) {
							element = elements.get(++elementIx);
							nextState.set(stateCountSums[elementIx - 1]);
							nextElementState = 0;
						}
						if (elementIx == elements.size() - 1 && element.terminals.get(nextElementState)) {
							terminal = true;
						}
					}
				}
				if (!nextState.isEmpty()) {
					Integer nextIndex = indexByState.get(nextState);
					if (nextIndex == null) {
						nextIndex = b.stateCount++;
						indexByState.put(nextState, nextIndex);
						b.terminals.set(nextIndex, terminal);
						queue.add(nextState);
					}
					b.setTransition(stateIndex, rangeIx, nextIndex);
				}
			}
		}
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
