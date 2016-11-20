package org.msyu.parser.intlexer.def;

import org.msyu.parser.intlexer.RangeSet;
import org.msyu.parser.intlexer.dfa.DFA;
import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.def.Defs.cseq;
import static org.msyu.parser.intlexer.def.Defs.rpt;
import static org.msyu.parser.intlexer.def.Defs.sseq;
import static org.msyu.parser.intlexer.dfa.DfaBuilder.dfaFor;

public class ComplexSeqOfRptTest {

	private static final RangeSet RANGE_0 = new RangeSet(new int[]{0, 1});
	private static final RangeSet RANGE_1 = new RangeSet(new int[]{1, 2});

	private static final DFA dfa = dfaFor(cseq(rpt(sseq(RANGE_0)), rpt(sseq(RANGE_1))));

	private static final int START = 0;

	@Test
	public void beginning() {
		assert dfa.isTerminalState(START);
	}

	@Test
	public void test0() {
		int s = dfa.advance(START, 0);
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test
	public void test1() {
		int s = dfa.advance(START, 1);
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test
	public void test00() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 1);
		assert s >= 0;
		assert dfa.isTerminalState(s);
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
	public void test01() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 1);
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test
	public void test00011() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 1);
		s = dfa.advance(s, 1);
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test
	public void test010() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 1);
		s = dfa.advance(s, 0);
		assert s < 0;
	}

}
