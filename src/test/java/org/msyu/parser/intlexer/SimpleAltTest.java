package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static java.util.Collections.singletonList;
import static org.msyu.parser.intlexer.Defs.alt;
import static org.msyu.parser.intlexer.Defs.sseq;

public class SimpleAltTest {

	private static final RangeSet RANGE_0 = new RangeSet(new int[]{0, 0});
	private static final RangeSet RANGE_1 = new RangeSet(new int[]{1, 1});
	private static final DFA dfa = alt(sseq(RANGE_0), sseq(RANGE_1, RANGE_1)).buildDFA();

	private static final int START = 0;

	@Test
	public void beginning() {
		assert !dfa.isTerminalState(START);
	}

	@Test
	public void test0() {
		int s = START;
		s = dfa.advance(s, 0);

		assert s >= 0;
		assert dfa.isTerminalState(s);
		assert singletonList(0).equals(dfa.getTerminatedElements(s));
	}

	@Test
	public void test00() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 0);

		assert s == DFA.NO_TRANSITION;
	}

	@Test
	public void test1() {
		int s = START;
		s = dfa.advance(s, 1);

		assert s >= 0;
		assert !dfa.isTerminalState(s);
	}

	@Test
	public void test11() {
		int s = START;
		s = dfa.advance(s, 1);
		s = dfa.advance(s, 1);

		assert s >= 0;
		assert dfa.isTerminalState(s);
		assert singletonList(1).equals(dfa.getTerminatedElements(s));
	}

	@Test
	public void test111() {
		int s = START;
		s = dfa.advance(s, 1);
		s = dfa.advance(s, 1);
		s = dfa.advance(s, 1);

		assert s == DFA.NO_TRANSITION;
	}

	@Test
	public void test10() {
		int s = START;
		s = dfa.advance(s, 1);
		s = dfa.advance(s, 0);

		assert s == DFA.NO_TRANSITION;
	}

}
