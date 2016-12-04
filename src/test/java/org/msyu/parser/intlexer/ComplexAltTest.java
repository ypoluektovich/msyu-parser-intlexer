package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;
import static org.msyu.parser.intlexer.Defs.alt;
import static org.msyu.parser.intlexer.Defs.cseq;
import static org.msyu.parser.intlexer.Defs.rpt;
import static org.msyu.parser.intlexer.Defs.sseq;

public class ComplexAltTest {

	private static final RangeSet RANGE_0 = new RangeSet(new int[]{0, 0});
	private static final RangeSet RANGE_1 = new RangeSet(new int[]{1, 1});
	private static final RangeSet RANGE_2 = new RangeSet(new int[]{2, 2});
	private static final DFA dfa = alt(
			cseq(sseq(RANGE_0), rpt(sseq(RANGE_1))),
			cseq(rpt(sseq(RANGE_1)), rpt(sseq(RANGE_2)))
	).buildDFA();

	private static final int START = 0;

	private int advance(int[] input) {
		int s = START;
		for (int symbol : input) {
			s = dfa.advance(s, symbol);
		}
		return s;
	}

	private void test(int[] input, boolean expectValidState, boolean expectTerminalState, List<Integer> expectTerminatedElements) {
		int s = advance(input);
		if (expectValidState) {
			assert s >= 0;
			assert dfa.isTerminalState(s) == expectTerminalState;
			if (expectTerminalState) {
				assert Objects.equals(dfa.getTerminatedElements(s), expectTerminatedElements);
			}
		} else {
			assert s == DFA.NO_TRANSITION;
		}
	}

	@Test
	public void beginning() {
		test(new int[0], true, true, singletonList(1));
	}

	@Test
	public void test0() {
		test(new int[]{0}, true, true, singletonList(0));
	}

	@Test
	public void test00() {
		test(new int[]{0, 0}, false, false, null);
	}

	@Test
	public void test01() {
		test(new int[]{0, 1}, true, true, singletonList(0));
	}

	@Test
	public void test011() {
		test(new int[]{0, 1, 1}, true, true, singletonList(0));
	}

	@Test
	public void test1() {
		test(new int[]{1}, true, true, singletonList(1));
	}

	@Test
	public void test11() {
		test(new int[]{1, 1}, true, true, singletonList(1));
	}

	@Test
	public void test2() {
		test(new int[]{2}, true, true, singletonList(1));
	}

	@Test
	public void test22() {
		test(new int[]{2, 2}, true, true, singletonList(1));
	}

	@Test
	public void test12() {
		test(new int[]{1, 2}, true, true, singletonList(1));
	}

	@Test
	public void test1122() {
		test(new int[]{1, 1, 2, 2}, true, true, singletonList(1));
	}

	@Test
	public void test121() {
		test(new int[]{1, 2, 1}, false, false, null);
	}

}
