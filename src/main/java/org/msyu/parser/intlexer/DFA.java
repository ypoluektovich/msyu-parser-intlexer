package org.msyu.parser.intlexer;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.BitSet;

import static java.util.Objects.requireNonNull;

public final class DFA implements Serializable {

	public static final int NO_TRANSITION = -1;
	public static final int OUT_OF_RANGES = -2;

	private final transient RangeSet ranges;
	private final transient int[][] transitionTable;
	private final transient BitSet terminals;


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


	private static final long serialVersionUID = 1L;

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("serialization proxy required");
	}

	private Object writeReplace() {
		return new SerialForm1(this);
	}

	private static final class SerialForm1 implements Serializable {
		private final RangeSet ranges;
		private final int[][] transitionTable;
		private final BitSet terminals;

		SerialForm1(DFA dfa) {
			ranges = dfa.ranges;
			transitionTable = dfa.transitionTable;
			terminals = dfa.terminals;
		}

		private Object readResolve() throws InvalidObjectException {
			return new DFA(
					requireNonNull(ranges, "ranges"),
					checkAndCopyTransitionTable(),
					requireNonNull(terminals, "terminals")
			);
		}

		private int[][] checkAndCopyTransitionTable() throws InvalidObjectException {
			requireNonNull(transitionTable, "transition table");
			int[][] tableCopy = new int[transitionTable.length][];
			int rangeCount = ranges.size();
			for (int from = 0; from < transitionTable.length; ++from) {
				int[] row = transitionTable[from];
				if (row != null) {
					if (row.length != rangeCount) {
						throw new InvalidObjectException(String.format(
								"length of transition table row %d is %d, not %d",
								from, row.length, rangeCount
						));
					}
					int[] rowCopy = new int[row.length];
					for (int by = 0; by < row.length; ++by) {
						int to = row[by];
						if (to < 0 && to != NO_TRANSITION) {
							throw new InvalidObjectException(String.format(
									"bad transition from %d by %d: %d",
									from, by, to
							));
						}
						rowCopy[by] = to;
					}
					tableCopy[from] = rowCopy;
				}
			}
			return tableCopy;
		}
	}

}
