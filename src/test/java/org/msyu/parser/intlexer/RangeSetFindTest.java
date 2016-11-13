package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

public class RangeSetFindTest {

	private static final RangeSet RS = new RangeSet(new int[]{1, 5, 11, 15});

	@Test
	public void before() {
		assert RS.find(0) == -1;
	}

	@Test
	public void firstStart() {
		assert RS.find(1) == 0;
	}

	@Test
	public void firstMiddle() {
		assert RS.find(3) == 0;
	}

	@Test
	public void firstEnd() {
		assert RS.find(4) == 0;
	}

	@Test
	public void firstNext() {
		assert RS.find(5) == -2;
	}

	@Test
	public void gap() {
		assert RS.find(7) == -2;
	}

	@Test
	public void secondStart() {
		assert RS.find(11) == 1;
	}

	@Test
	public void secondMiddle() {
		assert RS.find(13) == 1;
	}

	@Test
	public void secondEnd() {
		assert RS.find(14) == 1;
	}

	@Test
	public void secondNext() {
		assert RS.find(15) == -3;
	}

	@Test
	public void after() {
		assert RS.find(20) == -3;
	}

}
