package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.Defs.alt;
import static org.msyu.parser.intlexer.Defs.rpt;
import static org.msyu.parser.intlexer.Defs.sseq;

@Test(groups = "AltDefTest", dependsOnGroups = {"SimpleSeqDefTest", "RptDefTest"})
public class AltDefTest {

	private static final RangeSet RANGE_SET_A = new RangeSet(new int[]{'A', 'A'});

	private static final SimpleSeq A = sseq(RANGE_SET_A);

	private static final SimpleSeq AA = sseq(RANGE_SET_A, RANGE_SET_A);

	private static final Rpt R = rpt(A);

	@Test
	public void lengthA() {
		assert alt(A).getLength() == 1;
	}

	@Test
	public void lengthAA() {
		assert alt(A, A).getLength() == 1;
	}

	@Test
	public void lengthAAA() {
		assert alt(A, AA).getLength() == -1;
	}

	@Test
	public void lengthAAAA() {
		assert alt(AA, AA).getLength() == 2;
	}

	@Test
	public void lengthAR() {
		assert alt(A, R).getLength() == -1;
	}

	@Test
	public void nullableA() {
		assert !alt(A).isNullable();
	}

	@Test
	public void nullableAA() {
		assert !alt(A, A).isNullable();
	}

	@Test
	public void nullableR() {
		assert alt(R).isNullable();
	}

	@Test
	public void nullableAR() {
		assert alt(A, R).isNullable();
	}

}
