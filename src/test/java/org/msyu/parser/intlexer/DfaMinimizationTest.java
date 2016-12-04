package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.msyu.parser.intlexer.Defs.alt;
import static org.msyu.parser.intlexer.Defs.rpt;
import static org.msyu.parser.intlexer.Defs.sseq;

public class DfaMinimizationTest {

	private static final RangeSet ZERO = new RangeSet(new int[]{0, 0});
	private static final RangeSet ONE = new RangeSet(new int[]{1, 1});
	private static final RangeSet TWO = new RangeSet(new int[]{2, 2});
	private static final RangeSet ALL = RangeSet.basis(ZERO, ONE, TWO);

	@Test
	public void notTracking() {
		Map<ADef, DfaBuilder> cache = new HashMap<>();
		DfaBuilder builder;

		Rpt allDef = rpt(sseq(ALL));
		builder = new DfaBuilder(false);
		allDef.process(builder, cache);
		cache.put(allDef, new DfaBuilder(builder));

		SimpleSeq specDef = sseq(ZERO, ONE, TWO);
		builder = new DfaBuilder(false);
		specDef.process(builder, cache);
		cache.put(specDef, new DfaBuilder(builder));

		ADef def = alt(allDef, specDef);
		builder = new DfaBuilder(false);
		def.process(builder, cache);
		builder = new DfaBuilder(builder);
		assert builder.stateCount == 1;
	}

	@Test
	public void tracking() {
		Map<ADef, DfaBuilder> cache = new HashMap<>();
		DfaBuilder builder;

		Rpt allDef = rpt(sseq(ALL));
		builder = new DfaBuilder(false);
		allDef.process(builder, cache);
		cache.put(allDef, new DfaBuilder(builder));

		SimpleSeq specDef = sseq(ZERO, ONE, TWO);
		builder = new DfaBuilder(false);
		specDef.process(builder, cache);
		cache.put(specDef, new DfaBuilder(builder));

		ADef def = alt(allDef, specDef);
		builder = new DfaBuilder(true);
		def.process(builder, cache);
		builder = new DfaBuilder(builder);
		assert builder.stateCount == 5;
	}

}
