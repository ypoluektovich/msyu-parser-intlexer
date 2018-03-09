package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.Defs.cseq;
import static org.msyu.parser.intlexer.Defs.rpt;
import static org.msyu.parser.intlexer.Defs.sseq;

@Test(groups = "ComplexSeqDefTest", dependsOnGroups = {"SimpleSeqDefTest", "RptDefTest"})
public class ComplexSeqDefTest {

	private static final SimpleSeq A = sseq(new RangeSet(new int[]{'A', 'A'}));

	private static final Rpt R = rpt(A);

	@Test
	public void lengthA() {
		assert cseq(A).getLength() == 1;
	}

	@Test
	public void lengthR() {
		assert cseq(R).getLength() == -1;
	}

	@Test
	public void lengthAA() {
		assert cseq(A, A).getLength() == 2;
	}

	@Test
	public void lengthAR() {
		assert cseq(A, R).getLength() == -1;
	}

	@Test
	public void nullableA() {
		assert !cseq(A).isNullable();
	}

	@Test
	public void nullableR() {
		assert cseq(R).isNullable();
	}

	@Test
	public void nullableAA() {
		assert !cseq(A, A).isNullable();
	}

	@Test
	public void nullableAR() {
		assert !cseq(A, R).isNullable();
	}

}
