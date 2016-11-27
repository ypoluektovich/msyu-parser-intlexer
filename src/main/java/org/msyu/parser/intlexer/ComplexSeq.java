package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.List;

import static java.util.Objects.requireNonNull;

public final class ComplexSeq extends ADef {

	final List<ADef> elements;

	public ComplexSeq(List<? extends ADef> elements) {
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		this.elements = CopyList.immutable(elements, rs -> requireNonNull(rs, "all elements must be nonnull"));
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != ComplexSeq.class) {
			return false;
		}
		return elements.equals(((ComplexSeq) obj).elements);

	}

	@Override
	public final int hashCode() {
		return elements.hashCode();
	}

}
