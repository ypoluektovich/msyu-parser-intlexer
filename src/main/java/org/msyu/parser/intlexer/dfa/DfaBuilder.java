package org.msyu.parser.intlexer.dfa;

import org.msyu.parser.intlexer.RangeSet;
import org.msyu.parser.intlexer.def.ADef;
import org.msyu.parser.intlexer.def.ComplexSeq;
import org.msyu.parser.intlexer.def.Rpt;
import org.msyu.parser.intlexer.def.SimpleSeq;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public final class DfaBuilder {

	/**
	 * Build a Deterministic Finite Automaton that accepts the language defined by the specified {@code def}.
	 */
	public static DFA dfaFor(ADef def) {
		return process(def, new HashMap<>()).build();
	}

	private static DfaBuilder process(ADef def, Map<ADef, DfaBuilder> cache) {
		DfaBuilder builder = cache.get(def);
		if (builder == null) {
			builder = new DfaBuilder();
			// todo: think about Strategy pattern
			if (def instanceof SimpleSeq) {
				builder.processSimple((SimpleSeq) def);
			} else if (def instanceof Rpt) {
				builder.processRpt(process(((Rpt) def).repeated, cache));
			} else if (def instanceof ComplexSeq) {
				builder.processComplex(
						((ComplexSeq) def).elements.stream()
								.map(innerDef -> process(innerDef, cache))
								.collect(Collectors.toList())
				);
			} else {
				throw new UnsupportedOperationException("defs other than SimpleSeq are not supported yet");
			}
			cache.put(def, builder);
		}
		return builder;
	}


	private int stateCount = 0;

	private RangeSet basis = null;

	private int[][] transitionTable = new int[0][];
	private static final int NO_TRANSITION = DFA.NO_TRANSITION;

	private final BitSet terminals = new BitSet();


	private DfaBuilder() {
	}


	private void setTransition(int from, int by, int to) {
		if (from >= transitionTable.length) {
			transitionTable = Arrays.copyOf(transitionTable, stateCount);
		}
		int[] transitionRow = transitionTable[from];
		if (transitionRow == null) {
			transitionTable[from] = transitionRow = new int[basis.size()];
			Arrays.fill(transitionRow, NO_TRANSITION);
		}
		transitionRow[by] = to;
	}

	private int getTransition(int from, int by) {
		if (from > transitionTable.length) {
			return NO_TRANSITION;
		}
		int[] transitionRow = transitionTable[from];
		return transitionRow == null ? NO_TRANSITION : transitionRow[by];
	}


	private void processSimple(SimpleSeq def) {
		for (RangeSet rs : def.elements) {
			basis = basis == null ? rs : RangeSet.basis(basis, rs);
		}
		int transitionCount = def.elements.size();
		stateCount = transitionCount + 1;
		int basisSize = basis.size();
		for (int i = 0; i < transitionCount; ++i) {
			RangeSet rs = def.elements.get(i);
			for (int j = 0; j < basisSize; ++j) {
				if (rs.contains(basis.getStart(j))) {
					setTransition(i, j, i + 1);
				}
			}
		}
		terminals.set(transitionCount);
	}


	private void processRpt(DfaBuilder repeated) {
		basis = repeated.basis;
		Map<BitSet, Integer> indexByState = new HashMap<>();
		Queue<BitSet> queue = new ArrayDeque<>();

		BitSet state = BitSet.valueOf(new byte[]{1});
		indexByState.put(state, stateCount);
		terminals.set(stateCount);
		++stateCount;
		queue.add(state);

		while ((state = queue.poll()) != null) {
			int stateIndex = indexByState.get(state);
			for (int rangeIx = 0; rangeIx < basis.size(); ++rangeIx) {
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
						nextIndex = stateCount++;
						indexByState.put(nextState, nextIndex);
						terminals.set(nextIndex, terminal);
						queue.add(nextState);
					}
					setTransition(stateIndex, rangeIx, nextIndex);
				}
			}
		}
	}


	private void processComplex(List<DfaBuilder> elements) {
		int[] stateCountSums = new int[elements.size()];
		int jointStateCount = 0;
		{
			for (int i = 0; i < elements.size(); ++i) {
				DfaBuilder element = elements.get(i);
				basis = basis == null ? element.basis : RangeSet.basis(basis, element.basis);
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
			terminals.set(0, terminal);
		}
		indexByState.put(state, stateCount);
		++stateCount;
		queue.add(state);

		while ((state = queue.poll()) != null) {
			int stateIndex = indexByState.get(state);
			for (int rangeIx = 0; rangeIx < basis.size(); ++rangeIx) {
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
					int elementRangeIx = element.basis.find(basis.getStart(rangeIx));
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
						nextIndex = stateCount++;
						indexByState.put(nextState, nextIndex);
						terminals.set(nextIndex, terminal);
						queue.add(nextState);
					}
					setTransition(stateIndex, rangeIx, nextIndex);
				}
			}
		}

	}


	private DFA build() {
		return new DFA(basis, transitionTable, terminals);
	}

}
