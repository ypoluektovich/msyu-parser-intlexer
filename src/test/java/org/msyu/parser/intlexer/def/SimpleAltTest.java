package org.msyu.parser.intlexer.def;

import org.msyu.parser.intlexer.RangeSet;
import org.msyu.parser.intlexer.dfa.DFA;
import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.def.Defs.alt;
import static org.msyu.parser.intlexer.def.Defs.sseq;
import static org.msyu.parser.intlexer.dfa.DfaBuilder.dfaFor;

public class SimpleAltTest {

	private static final RangeSet RANGE_0 = new RangeSet(new int[]{0, 1});
	private static final RangeSet RANGE_1 = new RangeSet(new int[]{1, 2});
	private static final DFA dfa = dfaFor(alt(sseq(RANGE_0), sseq(RANGE_1, RANGE_1)));

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
