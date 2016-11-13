package org.msyu.parser.intlexer.dfa;

import org.msyu.parser.intlexer.RangeSet;
import org.msyu.parser.intlexer.def.ADef;
import org.msyu.parser.intlexer.def.SimpleSeq;

import java.util.Arrays;
import java.util.BitSet;

public final class DfaBuilder {

	public static DFA dfaFor(ADef def) {
		DfaBuilder builder = new DfaBuilder();
		if (def instanceof SimpleSeq) {
			builder.process((SimpleSeq) def);
			return builder.build();
		} else {
			throw new UnsupportedOperationException("defs other than SimpleSeq are not supported yet");
		}
	}


	private int stateCount = 0;

	private RangeSet basis = null;

	private int[][] transitionTable = new int[0][];
	private static final int NO_TRANSITION = DFA.NO_TRANSITION;

	private final BitSet terminals = new BitSet();


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


	private void process(SimpleSeq def) {
		for (RangeSet rs : def.elements) {
			basis = basis == null ? rs : RangeSet.basis(basis, rs);
		}
		int transitionCount = def.elements.size();
		stateCount = transitionCount + 1;
		transitionTable = new int[stateCount][];
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


	private DFA build() {
		return new DFA(basis, transitionTable, terminals);
	}

}
