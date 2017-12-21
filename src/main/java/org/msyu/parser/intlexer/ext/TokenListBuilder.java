package org.msyu.parser.intlexer.ext;

import java.util.ArrayList;
import java.util.List;

public abstract class TokenListBuilder<T> implements LexerStateManager.Callback {

	private final List<T> tokens = new ArrayList<>();

	@Override
	public void processToken(int tokenStart, int lexemeIndex) {
		tokens.add(createToken(tokenStart, lexemeIndex));
	}

	protected abstract T createToken(int tokenStart, int lexemeIndex);

	public List<T> getTokens() {
		return tokens;
	}

}
