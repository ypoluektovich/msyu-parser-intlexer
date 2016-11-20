package org.msyu.parser.intlexer.def;

import org.msyu.javautil.cf.CopyList;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ComplexSeq extends ADef {

	public final List<ADef> elements;

	public ComplexSeq(List<? extends ADef> elements) {
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		this.elements = CopyList.immutable(elements, rs -> requireNonNull(rs, "all elements must be nonnull"));
	}

}
