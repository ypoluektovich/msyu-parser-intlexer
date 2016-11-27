package org.msyu.parser.intlexer;

import static java.util.Arrays.asList;

public class Defs {

	public static SimpleSeq sseq(RangeSet... elements) {
		return new SimpleSeq(asList(elements));
	}

	public static ComplexSeq cseq(ADef... elements) {
		return new ComplexSeq(asList(elements));
	}

	public static Rpt rpt(ADef repeated) {
		return new Rpt(repeated);
	}

	public static Alt alt(ADef... elements) {
		return new Alt(asList(elements));
	}

}
