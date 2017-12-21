package org.msyu.parser.intlexer.ext;

public final class Token<T> {

	public final int start;
	public final int end;
	public final T value;

	Token(int start, int end, T value) {
		this.start = start;
		this.end = end;
		this.value = value;
	}

	public static abstract class ListBuilder<T> extends TokenListBuilder<Token<T>> {

		protected final int end;

		public ListBuilder(int end) {
			this.end = end;
		}

		@Override
		protected Token<T> createToken(int tokenStart, int lexemeIndex) {
			return new Token<>(tokenStart, end, createToken(tokenStart, end, lexemeIndex));
		}

		protected abstract T createToken(int start, int end, int lexemeIndex);

	}

}
