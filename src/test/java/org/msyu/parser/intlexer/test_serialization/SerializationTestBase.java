package org.msyu.parser.intlexer.test_serialization;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

abstract class SerializationTestBase<T> {

	protected final T sample;
	private final String resourcePrefix;
	private final int latestSerialFormVersion;

	protected SerializationTestBase(T sample, String resourcePrefix, int latestSerialFormVersion) {
		this.sample = sample;
		this.resourcePrefix = resourcePrefix;
		this.latestSerialFormVersion = latestSerialFormVersion;
	}

	@Test
	public final void serialize() throws IOException {
		serialize(sample);
	}

	protected final void serialize(T sample) throws IOException {
		byte[] expected;
		try (InputStream in = this.getClass().getResourceAsStream(
				"/serial/" + resourcePrefix + "_" + latestSerialFormVersion + ".bin"
		)) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int b;
			while ((b = in.read()) != -1) {
				baos.write(b);
			}
			expected = baos.toByteArray();
		}

		byte[] actual;
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutputStream out = new ObjectOutputStream(baos)) {
				out.writeObject(sample);
			}
			actual = baos.toByteArray();
		}

		assert Arrays.equals(actual, expected);
	}

	@SuppressWarnings("unchecked")
	protected final void deserialize(int serialFormVersion) throws IOException, ClassNotFoundException {
		T actual;
		try (ObjectInputStream in = new ObjectInputStream(
				this.getClass().getResourceAsStream("/serial/" + resourcePrefix + "_" + serialFormVersion + ".bin")
		)) {
			actual = (T) in.readObject();
		}

		assert checkDeserialized(actual);
	}

	protected abstract boolean checkDeserialized(T actual) throws IOException;

}
