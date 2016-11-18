package org.msyu.parser.intlexer.def;

import static java.util.Objects.requireNonNull;

public class Rpt extends ADef {

	public final ADef repeated;

	public Rpt(ADef repeated) {
		this.repeated = requireNonNull(repeated, "repeated def must be nonnull");
	}

}
