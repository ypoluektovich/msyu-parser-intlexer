package org.msyu.parser.intlexer;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import static java.util.Objects.requireNonNull;
import static org.msyu.parser.intlexer.DFA.NO_TRANSITION;

public final class Rpt extends ADef {

	private final ADef repeated;

	public Rpt(ADef repeated) {
		this.repeated = requireNonNull(repeated, "repeated def must be nonnull");
	}


	@Override
	protected final void process(DfaBuilder b, Map<ADef, DfaBuilder> cache) {
		process(b, repeated.process(cache));
	}

	private void process(DfaBuilder b, DfaBuilder repeated) {
		b.basis = repeated.basis;
		Map<BitSet, Integer> indexByState = new HashMap<>();
		Queue<BitSet> queue = new ArrayDeque<>();

		BitSet state = BitSet.valueOf(new byte[]{1});
		indexByState.put(state, b.stateCount);
		b.terminals.set(b.stateCount);
		++b.stateCount;
		queue.add(state);

		while ((state = queue.poll()) != null) {
			int stateIndex = indexByState.get(state);
			for (int rangeIx = 0; rangeIx < b.basis.size(); ++rangeIx) {
				BitSet nextState = new BitSet(repeated.stateCount);
				boolean terminal = false;
				for (int substate = state.nextSetBit(0); substate >= 0; substate = state.nextSetBit(substate + 1)) {
					int nextSubstate = repeated.getTransition(substate, rangeIx);
					if (nextSubstate != NO_TRANSITION) {
						nextState.set(nextSubstate);
						if (repeated.terminals.get(nextSubstate)) {
							terminal = true;
							nextState.set(0);
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
