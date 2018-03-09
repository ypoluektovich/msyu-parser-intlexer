package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.msyu.parser.intlexer.Defs.rpt;
import static org.msyu.parser.intlexer.Defs.sseq;

@Test(groups = "RptDefTest", dependsOnGroups = "SimpleSeqDefTest")
public class RptDefTest {

	private static final Rpt A = rpt(sseq(new RangeSet(new int[]{'A', 'A'})));

	@Test
	public void length() {
		assert A.getLength() == -1;
	}

	@Test
	public void nullable() {
		assert A.isNullable();
	}

}
