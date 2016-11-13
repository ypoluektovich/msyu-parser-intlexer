package org.msyu.parser.intlexer;

import java.util.Arrays;

import static java.util.Arrays.copyOf;

public final class RangeSet {

	private final int[] starts;
	private final int[] ends;


	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != RangeSet.class) {
			return false;
		}
		RangeSet that = (RangeSet) obj;
		return Arrays.equals(starts, that.starts) && Arrays.equals(ends, that.ends);
	}

	@Override
	public final int hashCode() {
		return Arrays.hashCode(starts) * 31 + Arrays.hashCode(ends);
	}


	public final int size() {
		return starts.length;
	}

	public final int getStart(int index) {
		return starts[index];
	}

	public final int getEnd(int index) {
		return ends[index];
	}


	public RangeSet(int[] ranges) {
		if (ranges.length % 2 != 0) {
			throw new IllegalArgumentException("ranges array length is not even");
		}
		int n = ranges.length / 2;
		starts = new int[n];
		ends = new int[n];
		int prevEnd = 0;
		for (int i = 0; i < n; ++i) {
			int start = ranges[i * 2];
			int end = ranges[i * 2 + 1];
			if (start < prevEnd) {
				throw new IllegalArgumentException("range start is too low at position " + i * 2);
			}
			if (end <= start) {
				throw new IllegalArgumentException("empty/inverted range at position " + i * 2);
			}
			starts[i] = start;
			ends[i] = end;
			prevEnd = end;
		}
	}

	private RangeSet(int[] starts, int[] ends) {
		assert starts.length == ends.length;
		this.starts = starts;
		this.ends = ends;
	}

	public static RangeSet basis(RangeSet a, RangeSet b) {
		// todo: overflow checks
		int nA = a.starts.length;
		int nB = b.starts.length;
		int cap = Math.max(nA, nB) * 2;
		int[] starts = new int[cap];
		int[] ends = new int[cap];
		int n = 0;

		int ixA = 0;
		int ixB = 0;
		int start = 0;
		while (ixA < nA && ixB < nB) {
			if (n == starts.length) {
				cap *= 2;
				starts = copyOf(starts, cap);
				ends = copyOf(ends, cap);
			}
			int sA = a.starts[ixA];
			int sB = b.starts[ixB];
			if (start < sA && start < sB) {
				start = Math.min(sA, sB);
			}
			int end;
			if (start < sA) {
				int eB = b.ends[ixB];
				if (eB <= sA) {
					end = eB;
					++ixB;
				} else {
					end = sA;
				}
			} else if (start < sB) {
				int eA = a.ends[ixA];
				if (eA <= sB) {
					end = eA;
					++ixA;
				} else {
					end = sB;
				}
			} else {
				int eA = a.ends[ixA];
				int eB = b.ends[ixB];
				end = Math.min(eA, eB);
				if (eA <= end) {
					++ixA;
				}
				if (eB <= end) {
					++ixB;
				}
			}
			starts[n] = start;
			start = ends[n] = end;
			++n;
		}
		if (ixA < nA) {
			int remaining = nA - ixA;
			if (n + remaining > cap) {
				cap = n + remaining;
				starts = copyOf(starts, cap);
				ends = copyOf(ends, cap);
			}
			while (ixA < nA) {
				starts[n] = Math.max(start, a.starts[ixA]);
				start = ends[n] = a.ends[ixA];
				++n;
				++ixA;
			}
		}
		if (ixB < nB) {
			int remaining = nB - ixB;
			if (n + remaining > cap) {
				cap = n + remaining;
				starts = copyOf(starts, cap);
				ends = copyOf(ends, cap);
			}
			while (ixB < nB) {
				starts[n] = Math.max(start, b.starts[ixB]);
				start = ends[n] = b.ends[ixB];
				++n;
				++ixB;
			}
		}
		return n == cap ?
				new RangeSet(starts, ends) :
				new RangeSet(copyOf(starts, n), copyOf(ends, n));
	}

}
