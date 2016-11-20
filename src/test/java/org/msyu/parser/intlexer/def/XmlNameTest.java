package org.msyu.parser.intlexer.def;

import org.msyu.parser.intlexer.dfa.DFA;
import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.dfa.DfaBuilder.dfaFor;

public class XmlNameTest {

	private static final DFA dfa = dfaFor(Xml.NAME);

	@Test
	public void test() {
		String in = "ns:element-name";
		int s = 0;
		for (int i = 0; i < in.length(); ++i) {
			s = dfa.advance(s, in.charAt(i));
			assert s >= 0;
		}
		assert dfa.isTerminalState(s);
	}

}
