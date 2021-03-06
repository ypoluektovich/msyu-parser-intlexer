package org.msyu.parser.intlexer.test_serialization;

import org.msyu.parser.intlexer.RangeSet;
import org.testng.annotations.Test;

import java.io.IOException;

public class RangeSetSerializationTest extends SerializationTestBase<RangeSet> {

	public RangeSetSerializationTest() {
		super(createSample(), "RangeSet", 1);
	}

	static RangeSet createSample() {
		return new RangeSet(new int[]{2, 3, 5, 7, 11, 13});
	}

	@Test
	public void deserialize1() throws IOException, ClassNotFoundException {
		deserialize(1);
	}

	@Override
	protected boolean checkDeserialized(RangeSet actual) {
		return sample.equals(actual);
	}

}
