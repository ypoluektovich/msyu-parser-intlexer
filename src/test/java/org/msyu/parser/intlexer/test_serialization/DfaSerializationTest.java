package org.msyu.parser.intlexer.test_serialization;

import org.msyu.parser.intlexer.DFA;
import org.msyu.parser.intlexer.Xml;
import org.testng.annotations.Test;

import java.io.IOException;

public class DfaSerializationTest extends SerializationTestBase<DFA> {

	public DfaSerializationTest() {
		super(createSample(), "DFA", 1);
	}

	static DFA createSample() {
		return Xml.NAME.buildDFA();
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
