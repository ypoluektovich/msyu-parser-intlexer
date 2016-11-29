package org.msyu.parser.intlexer.test_serialization;

import org.msyu.parser.intlexer.DFA;
import org.msyu.parser.intlexer.Xml;
import org.testng.annotations.Test;

import java.io.IOException;

public class DfaSerializationTest extends SerializationTestBase<DFA> {

	public DfaSerializationTest() {
		super(Xml.NAME.buildDFA(), "DFA", 1);
	}

	@Test
	public void deserialize1() throws IOException, ClassNotFoundException {
		deserialize(1);
	}

	@Override
	protected boolean checkDeserialized(DFA actual) throws IOException {
		serialize(actual);
		return true;
	}

}
