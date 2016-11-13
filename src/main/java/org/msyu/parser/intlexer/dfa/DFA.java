package org.msyu.parser.intlexer.dfa;

import org.msyu.parser.intlexer.RangeSet;

import java.util.BitSet;

public final class DFA {

	public static final int NO_TRANSITION = -1;
	public static final int OUT_OF_RANGES = -2;

	private final RangeSet ranges;

	private final int[][] transitionTable;

	private final BitSet terminals;

	DFA(RangeSet ranges, int[][] transitionTable, BitSet terminals) {
		this.ranges = ranges;
		this.transitionTable = transitionTable;
		this.terminals = terminals;
	}

	public final int advance(int from, int by) {
		int rangeIx = ranges.find(by);
		if (rangeIx < 0) {
			return OUT_OF_RANGES;
		}
		if (from < 0 || from >= transitionTable.length) {
			return NO_TRANSITION;
		}
		int[] row = transitionTable[from];
		return row != null ? row[rangeIx] : NO_TRANSITION;
	}

	public final boolean isTerminalState(int state) {
		return terminals.get(state);
	}

}
