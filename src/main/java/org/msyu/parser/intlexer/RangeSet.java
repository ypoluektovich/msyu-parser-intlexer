package org.msyu.parser.intlexer;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;

import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;

/**
 * Set of disjoint intervals within [0; {@link Integer#MAX_VALUE}].
 */
public final class RangeSet implements Serializable {

	private final transient int[] starts;
	private final transient int[] ends;


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

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (int i = 0; i < starts.length; ++i) {
			sb.append('[');
			sb.append(starts[i]);
			sb.append(',');
			sb.append(ends[i]);
			sb.append(']');
		}
		sb.append('}');
		return sb.toString();
	}

	/**
	 * @return the number of ranges in this set.
	 */
	public final int size() {
		return starts.length;
	}

	/**
	 * @return the first number in the range with the specified index.
	 */
	public final int getStart(int index) {
		return starts[index];
	}

	/**
	 * @return the last number in the range with the specified index, plus 1.
	 */
	public final int getEnd(int index) {
		return ends[index];
	}

	/**
	 * @return a non-negative index of a range in this set that contains the specified number {@code n},
	 * or, if {@code n} is not in any range, {@code (-(insertion point) - 1)},
	 * where {@code insertion point} is the index of a range consisting of only {@code n},
	 * were it inserted in this set.
	 */
	public final int find(int n) {
		int ix = Arrays.binarySearch(starts, n);
		if (ix >= 0) {
			return ix;
		}
		int insertionPoint = ix;
		ix = -(ix + 1) - 1;
		return ix >= 0 && n <= ends[ix] ? ix : insertionPoint;
	}

	/**
	 * @return {@code true} if a range in this set contains the specified number, {@code false} otherwise.
	 */
	public final boolean contains(int n) {
		return find(n) >= 0;
	}


	/**
	 * @param ranges an array containing pairs of (range start; range end), both values inclusive.
	 * For example, to construct a range set that contains numbers 0 and 1, use {@code new RangeSet(new int[]{0, 1})}.
	 * Ranges must already be sorted.
	 *
	 * @throws IllegalArgumentException
	 */
	public RangeSet(int[] ranges) {
		if (ranges.length % 2 != 0) {
			throw new IllegalArgumentException("ranges array length is not even");
		}
		int n = ranges.length / 2;
		starts = new int[n];
		ends = new int[n];
		int prevEnd = -1;
		for (int i = 0; i < n; ++i) {
			prevEnd = checkAndAssign(i, ranges[i * 2], ranges[i * 2 + 1], prevEnd);
		}
	}

	public RangeSet(int[] starts, int[] ends) {
		if (starts.length != ends.length) {
			throw new IllegalArgumentException("starts and ends arrays are of different length");
		}
		int n = starts.length;
		this.starts = new int[n];
		this.ends = new int[n];
		int prevEnd = -1;
		for (int i = 0; i < n; ++i) {
			prevEnd = checkAndAssign(i, starts[i], ends[i], prevEnd);
		}
	}

	private int checkAndAssign(int ix, int start, int end, int prevEnd) {
		if (start <= prevEnd) {
			throw new IllegalArgumentException("range " + ix + " overlaps the previous one");
		}
		if (end < start) {
			throw new IllegalArgumentException("range " + ix + " is inverted");
		}
		starts[ix] = start;
		ends[ix] = end;
		return end;
	}

	/**
	 * For use only in {@link #basis(RangeSet, RangeSet) basis()}. <strong>Do not use in SerialForm classes!</strong>
	 */
	private RangeSet(int[] starts, int[] ends, int length) {
		this.starts = starts.length == length ? starts : copyOf(starts, length);
		this.ends = ends.length == length ? ends : copyOf(ends, length);
	}

	/**
	 * Builds a range set that is the basis of the two specified sets.
	 * <p>Example: basis of sets {[0, 3]} and {[2, 5]} is {[0, 1], [2, 3], [4, 5]}.</p>
	 */
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
				if (eB < sA) {
					end = eB;
					++ixB;
				} else {
					end = sA - 1;
				}
			} else if (start < sB) {
				int eA = a.ends[ixA];
				if (eA < sB) {
					end = eA;
					++ixA;
				} else {
					end = sB - 1;
				}
			} else {
				int eA = a.ends[ixA];
				int eB = b.ends[ixB];
				end = Math.min(eA, eB);
				if (eA == end) {
					++ixA;
				}
				if (eB == end) {
					++ixB;
				}
			}
			starts[n] = start;
			ends[n] = end;
			start = end + 1;
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
		return new RangeSet(starts, ends, n);
	}

	public static RangeSet basis(RangeSet first, RangeSet... other) {
		RangeSet basis = requireNonNull(first, "all range sets must be nonnull");
		for (RangeSet rangeSet : other) {
			basis = basis(first, requireNonNull(rangeSet, "all range sets must be nonnull"));
		}
		return basis;
	}


	private static final long serialVersionUID = 1L;

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("serialization proxy required");
	}

	private Object writeReplace() {
		return new SerialForm1(this);
	}

	private static final class SerialForm1 implements Serializable {
		private static final long serialVersionUID = 1L;

		private final int[] starts;
		private final int[] ends;

		SerialForm1(RangeSet rs) {
			starts = rs.starts;
			ends = rs.ends;
		}

		private Object readResolve() {
			return new RangeSet(starts, ends);
		}
	}

}
