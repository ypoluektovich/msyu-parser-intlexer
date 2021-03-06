package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.Defs.sseq;

public class SimpleSeqDfaTest {

	private static final DFA dfa = sseq(Xml.NAME_START_CHAR, Xml.NAME_CHAR).buildDFA();

	private static final int START = 0;
	private static final int MIDDLE = 1;
	private static final int END = 2;

	@Test
	public void assumptions() {
		assert !dfa.isTerminalState(START);
		assert !dfa.isTerminalState(MIDDLE);
		assert dfa.isTerminalState(END);
	}

	@Test(dependsOnMethods = "assumptions")
	public void good1() {
		assert dfa.advance(START, 'A') == MIDDLE;
	}

	@Test(dependsOnMethods = "assumptions")
	public void bad1out() {
		assert dfa.advance(START, ',') == DFA.OUT_OF_RANGES;
	}

	@Test(dependsOnMethods = "assumptions")
	public void bad1transition() {
		assert dfa.advance(START, '0') == DFA.NO_TRANSITION;
	}

	@Test(dependsOnMethods = "assumptions")
	public void good2start() {
		assert dfa.advance(MIDDLE, 'A') == END;
	}

	@Test(dependsOnMethods = "assumptions")
	public void good2nonstart() {
		assert dfa.advance(MIDDLE, '0') == END;
	}

	@Test(dependsOnMethods = "assumptions")
	public void bad2() {
		assert dfa.advance(MIDDLE, ',') == DFA.OUT_OF_RANGES;
	}

	@Test(dependsOnMethods = "assumptions")
	public void bad3transition() {
		assert dfa.advance(END, 'A') == DFA.NO_TRANSITION;
	}

	@Test(dependsOnMethods = "assumptions")
	public void bad3out() {
		assert dfa.advance(END, ',') == DFA.OUT_OF_RANGES;
	}

}
