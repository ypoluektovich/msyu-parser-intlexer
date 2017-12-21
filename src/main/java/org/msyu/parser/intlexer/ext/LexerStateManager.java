package org.msyu.parser.intlexer.ext;

import org.msyu.parser.intlexer.DFA;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public final class LexerStateManager {

	private final DFA lexer;

	private final Map<Integer, Integer> stateByTokenStart = new HashMap<>();

	public LexerStateManager(DFA lexer) {
		this.lexer = lexer;
		stateByTokenStart.put(0, 0);
	}

	public final boolean advance(int input, int newPosition, Callback callback) {
		boolean haveTerminatedTokens = false;
		for (Iterator<Map.Entry<Integer, Integer>> sasItr = stateByTokenStart.entrySet().iterator(); sasItr.hasNext(); ) {
			Map.Entry<Integer, Integer> startAndState = sasItr.next();
			int tokenStart = startAndState.getKey();
			int lexerState = startAndState.getValue();
			lexerState = lexer.advance(lexerState, input);
			switch (lexerState) {
				case DFA.OUT_OF_RANGES:
					throw new IllegalArgumentException("unexpected input");
				case DFA.NO_TRANSITION:
					sasItr.remove();
					continue;
				default:
					startAndState.setValue(lexerState);
			}
			if (lexer.isTerminalState(lexerState)) {
				haveTerminatedTokens = true;
				for (int lexemeIx : lexer.getTerminatedElements(lexerState)) {
					callback.processToken(tokenStart, lexemeIx);
				}
			}
		}
		if (haveTerminatedTokens) {
			stateByTokenStart.put(newPosition, 0);
		}
		return haveTerminatedTokens;
	}

	public final Set<Integer> viewGrowingPositions() {
		return unmodifiableSet(stateByTokenStart.keySet());
	}

	public final void retainGrowingPositions(Collection<?> positions) {
		stateByTokenStart.keySet().retainAll(positions);
	}


	public interface Callback {
		void processToken(int tokenStart, int lexemeIndex);
	}

}
