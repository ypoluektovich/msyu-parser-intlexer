package org.msyu.parser.intlexer;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.msyu.parser.intlexer.DFA.NO_TRANSITION;

public abstract class AComplexDef extends ADef {
	AComplexDef() {
	}

	protected final void process(DfaBuilder b, List<DfaBuilder> elements) {
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

		BitSet state = new BitSet();
		b.terminals.set(0, buildInitialState(elements, stateCountSums, state));
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
						terminal |= closeEpsilonTransitions(nextElementState, elementIx, elements, stateCountSums, nextState);
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

	protected abstract boolean buildInitialState(List<DfaBuilder> elements, int[] stateCountSums, BitSet state);

	protected abstract boolean closeEpsilonTransitions(
			int elementState,
			int elementIx,
			List<DfaBuilder> elements,
			int[] stateCountSums,
			BitSet state
	);

}
