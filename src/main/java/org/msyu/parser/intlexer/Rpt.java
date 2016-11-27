package org.msyu.parser.intlexer;

import static java.util.Objects.requireNonNull;

public final class Rpt extends ADef {

	final ADef repeated;

	public Rpt(ADef repeated) {
		this.repeated = requireNonNull(repeated, "repeated def must be nonnull");
	}

}
