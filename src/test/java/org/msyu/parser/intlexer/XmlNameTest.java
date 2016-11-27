package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

public class XmlNameTest {

	private static final DFA dfa = Xml.NAME.buildDFA();

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
