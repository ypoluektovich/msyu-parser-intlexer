package org.msyu.parser.intlexer;

import org.msyu.javautil.cf.CopyList;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public final class SimpleSeq extends ADef {

	private final List<RangeSet> elements;

	public SimpleSeq(List<RangeSet> elements) {
		if (elements.isEmpty()) {
			throw new IllegalArgumentException("element list must not be empty");
		}
		this.elements = CopyList.immutable(elements, rs -> requireNonNull(rs, "all elements must be nonnull"));
	}


	@Override
	protected final void process(DfaBuilder b, Map<ADef, DfaBuilder> cache) {
		process(b);
	}

	private void process(DfaBuilder b) {
		for (RangeSet rs : elements) {
			b.basis = b.basis == null ? rs : RangeSet.basis(b.basis, rs);
		}
		int transitionCount = elements.size();
		b.stateCount = transitionCount + 1;
		int basisSize = b.basis.size();
		for (int i = 0; i < transitionCount; ++i) {
			RangeSet rs = elements.get(i);
			for (int j = 0; j < basisSize; ++j) {
				if (rs.contains(b.basis.getStart(j))) {
					b.setTransition(i, j, i + 1);
				}
			}
		}
		b.terminals.set(transitionCount);
	}


	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass() != SimpleSeq.class) {
			return false;
		}
		return elements.equals(((SimpleSeq) obj).elements);

	}

	@Override
	public final int hashCode() {
		return elements.hashCode();
	}

}
