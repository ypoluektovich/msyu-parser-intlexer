package org.msyu.parser.intlexer;

import static java.util.Objects.requireNonNull;

public final class Rpt extends ADef {

	final ADef repeated;

	public Rpt(ADef repeated) {
		this.repeated = requireNonNull(repeated, "repeated def must be nonnull");
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != Rpt.class) {
			return false;
		}
		return repeated.equals(((Rpt) obj).repeated);

	}

	@Override
	public final int hashCode() {
		return repeated.hashCode() + 1;
	}

}
