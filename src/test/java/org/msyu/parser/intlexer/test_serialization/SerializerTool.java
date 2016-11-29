package org.msyu.parser.intlexer.test_serialization;

import org.msyu.parser.intlexer.Xml;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

class SerializerTool {

	public static void main(String[] args) throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Paths.get("DFA_1.bin")))) {
			out.writeObject(Xml.NAME.buildDFA());
		}
	}

}
