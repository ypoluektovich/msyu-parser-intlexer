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

}
