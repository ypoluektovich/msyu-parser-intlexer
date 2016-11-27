package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.List;

import static java.util.Objects.requireNonNull;

public final class Alt extends ADef {

	final List<ADef> alternatives;

	public Alt(List<? extends ADef> alternatives) {
		if (alternatives.isEmpty()) {
			throw new IllegalArgumentException("alternatives list must not be empty");
		}
		this.alternatives = CopyList.immutable(alternatives, rs -> requireNonNull(rs, "all alternatives must be nonnull"));
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != Alt.class) {
			return false;
		}
		return alternatives.equals(((Alt) obj).alternatives);

	}

	@Override
	public final int hashCode() {
		return alternatives.hashCode();
	}

}
