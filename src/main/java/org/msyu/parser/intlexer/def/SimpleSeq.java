package org.msyu.parser.intlexer.def;

import org.msyu.javautil.cf.CopyList;
import org.msyu.parser.intlexer.RangeSet;

import java.util.List;

import static java.util.Objects.requireNonNull;

public final class SimpleSeq extends ADef {

	public final List<RangeSet> elements;

	public SimpleSeq(List<RangeSet> elements) {
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		this.elements = CopyList.immutable(elements, rs -> requireNonNull(rs, "all elements must be nonnull"));
	}

}
