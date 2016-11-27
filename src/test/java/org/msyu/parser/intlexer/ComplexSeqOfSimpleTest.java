package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.Defs.cseq;
import static org.msyu.parser.intlexer.Defs.sseq;
import static org.msyu.parser.intlexer.DfaBuilder.dfaFor;

public class ComplexSeqOfSimpleTest {

	private static final RangeSet RANGE_0 = new RangeSet(new int[]{0, 1});
	private static final RangeSet RANGE_1 = new RangeSet(new int[]{1, 2});

	private static final DFA dfa = dfaFor(cseq(sseq(RANGE_0), sseq(RANGE_1)));

	private static final int START = 0;

	@Test
	public void beginning() {
		assert !dfa.isTerminalState(START);
	}

	@Test
	public void good1() {
		int s = dfa.advance(START, 0);
		assert s >= 0;
		assert !dfa.isTerminalState(s);
	}

	@Test
	public void bad1() {
		int s = dfa.advance(START, 1);
		assert s < 0;
	}

	@Test
	public void good2() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 1);
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test
	public void bad2() {
		int s = START;
		s = dfa.advance(s, 0);
		s = dfa.advance(s, 0);
		assert s < 0;
	}

}
