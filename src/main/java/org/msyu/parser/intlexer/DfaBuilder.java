package org.msyu.parser.intlexer;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.msyu.parser.intlexer.DFA.NO_TRANSITION;

final class DfaBuilder {

	int stateCount = 0;

	RangeSet basis = null;

	int[][] transitionTable = new int[0][];

	final BitSet terminals = new BitSet();


	/**
	 * Create an empty DfaBuilder, to be filled by a concrete definition.
	 */
	DfaBuilder() {
	}


	/**
	 * Create a new DfaBuilder with minimized DFA of the supplied builder.
	 */
	DfaBuilder(DfaBuilder dfa) {
		basis = dfa.basis;

		BitSet[] unmergeable = Stream.generate(() -> new BitSet(dfa.stateCount)).limit(dfa.stateCount).toArray(BitSet[]::new);
		for (int i = 0; i < dfa.stateCount - 1; ++i) {
			boolean firstIsTerminal = dfa.terminals.get(i);
			for (int j = i + 1; j < dfa.stateCount; ++j) {
				if (firstIsTerminal != dfa.terminals.get(j)) {
					unmergeable[i].set(j);
				}
			}
		}

		boolean madeChanges;
		do {
			madeChanges = false;
			for (int i = 0; i < dfa.stateCount - 1; ++i) {
				for (int j = i + 1; j < dfa.stateCount; ++j) {
					if (unmergeable[i].get(j)) {
						continue;
					}
					for (int rangeIx = 0; rangeIx < basis.size(); ++rangeIx) {
						int transitionI = dfa.getTransition(i, rangeIx);
						int transitionJ = dfa.getTransition(j, rangeIx);
						if (transitionI == NO_TRANSITION || transitionJ == NO_TRANSITION) {
							unmergeable[i].set(j);
							continue;
						}
						if (transitionI > transitionJ) {
							int t = transitionI;
							transitionI = transitionJ;
							transitionJ = t;
						}
						if (unmergeable[transitionI].get(transitionJ)) {
							unmergeable[i].set(j);
							madeChanges = true;
						}
					}
				}
			}
		} while (madeChanges);

		BitSet[] clusterByState = new BitSet[dfa.stateCount];
		for (int i = 0; i < dfa.stateCount - 1; ++i) {
			for (int j = i + 1; j < dfa.stateCount; ++j) {
				if (!unmergeable[i].get(j)) {
					BitSet clusterI = clusterByState[i];
					BitSet clusterJ = clusterByState[j];
					if (clusterI == null || clusterI != clusterJ) {
						BitSet newCluster = new BitSet(dfa.stateCount);
						if (clusterI == null) {
							newCluster.set(i);
						} else {
							newCluster.or(clusterI);
						}
						if (clusterJ == null) {
							newCluster.set(j);
						} else {
							newCluster.or(clusterJ);
						}
						for (int k = newCluster.nextSetBit(0); k >= 0; k = newCluster.nextSetBit(k + 1)) {
							clusterByState[k] = newCluster;
						}
					}
				}
			}
		}

		Map<BitSet, Integer> indexByCluster = new HashMap<>();
		for (int i = 0; i < clusterByState.length; ++i) {
			BitSet cluster = clusterByState[i];
			if (cluster == null) {
				cluster = clusterByState[i] = new BitSet();
				cluster.set(i);
			}
			Integer clusterIx = indexByCluster.computeIfAbsent(cluster, c -> stateCount++);
			if (dfa.terminals.get(i)) {
				terminals.set(clusterIx);
			}
		}

		for (int from = 0; from < dfa.transitionTable.length; ++from) {
			int[] row = dfa.transitionTable[from];
			if (row != null) {
				for (int rangeIx = 0; rangeIx < row.length; rangeIx++) {
					int to = row[rangeIx];
					if (to != NO_TRANSITION) {
						setTransition(
								indexByCluster.get(clusterByState[from]),
								rangeIx,
								indexByCluster.get(clusterByState[to])
						);
					}
				}
			}
		}
	}


	final void setTransition(int from, int by, int to) {
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

	final int getTransition(int from, int by) {
		if (from >= transitionTable.length) {
			return NO_TRANSITION;
		}
		int[] transitionRow = transitionTable[from];
		return transitionRow == null ? NO_TRANSITION : transitionRow[by];
	}

}
