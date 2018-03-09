package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.Defs.sseq;

@Test(groups = "SimpleSeqDefTest")
public class SimpleSeqDefTest {

	private static final RangeSet A = new RangeSet(new int[]{'A', 'A'});

	@Test
	public void length1() {
		assert sseq(A).getLength() == 1;
	}

	@Test
	public void length2() {
		assert sseq(A, A).getLength() == 2;
	}

	@Test
	public void nullable1() {
		assert !sseq(A).isNullable();
	}

	@Test
	public void nullable2() {
		assert !sseq(A, A).isNullable();
	}

}
