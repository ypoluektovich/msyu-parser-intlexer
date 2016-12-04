package org.msyu.parser.intlexer.test_serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

class SerializerTool {

	public static void main(String[] args) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get("RangeSet_1.bin")))) {
			out.writeObject(RangeSetSerializationTest.createSample());
		}
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get("DFA_1.bin")))) {
			out.writeObject(DfaSerializationTest.createSample());
		}
	}

}
