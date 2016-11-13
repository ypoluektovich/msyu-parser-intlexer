package org.msyu.parser.intlexer.def;

import org.msyu.parser.intlexer.dfa.DFA;
import org.testng.annotations.Test;

import static java.util.Collections.singletonList;
import static org.msyu.parser.intlexer.dfa.DfaBuilder.dfaFor;

public class SimpleSeqDfaTest {

	private static final DFA dfa = dfaFor(new SimpleSeq(singletonList(Xml.NAME_START_CHAR)));

	private static final int START = 0;
	private static final int END = 1;

	@Test
	public void assumptions() {
		assert !dfa.isTerminalState(START);
		assert dfa.isTerminalState(END);
	}

	@Test(dependsOnMethods = "assumptions")
	public void good() {
		assert dfa.advance(START, 'A') == END;
	}

	@Test(dependsOnMethods = "assumptions")
	public void bad() {
		assert dfa.advance(START, ',') == DFA.OUT_OF_RANGES;
	}

	@Test(dependsOnMethods = "assumptions")
	public void afterGood() {
		assert dfa.advance(END, 'A') == DFA.NO_TRANSITION;
	}

}
