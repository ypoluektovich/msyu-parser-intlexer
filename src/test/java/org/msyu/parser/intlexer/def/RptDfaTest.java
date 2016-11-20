package org.msyu.parser.intlexer.def;

import org.msyu.parser.intlexer.dfa.DFA;
import org.testng.annotations.Test;

import static javax.sound.midi.ShortMessage.START;
import static org.msyu.parser.intlexer.def.Defs.rpt;
import static org.msyu.parser.intlexer.def.Defs.sseq;
import static org.msyu.parser.intlexer.dfa.DfaBuilder.dfaFor;

public class RptDfaTest {

	private static final DFA dfa = dfaFor(rpt(sseq(Xml.NAME_CHAR, Xml.NAME_CHAR)));

	@Test()
	public void beginning() {
		assert dfa.isTerminalState(0);
	}

	@Test()
	public void good1() {
		int s = dfa.advance(0, 'A');
		assert s >= 0;
		assert !dfa.isTerminalState(s);
	}

	@Test()
	public void good2() {
		int s = 0;
		s = dfa.advance(s, 'A');
		s = dfa.advance(s, 'B');
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test()
	public void good3() {
		int s = 0;
		s = dfa.advance(s, 'A');
		s = dfa.advance(s, 'B');
		s = dfa.advance(s, 'C');
		assert s >= 0;
		assert !dfa.isTerminalState(s);
	}

	@Test()
	public void good4() {
		int s = 0;
		s = dfa.advance(s, 'A');
		s = dfa.advance(s, 'B');
		s = dfa.advance(s, 'C');
		s = dfa.advance(s, 'D');
		assert s >= 0;
		assert dfa.isTerminalState(s);
	}

	@Test()
	public void bad1transition() {
		assert dfa.advance(START, '0') == DFA.NO_TRANSITION;
	}

}
