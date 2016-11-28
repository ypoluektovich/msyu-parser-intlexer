package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RangeSetBasisTest {

	@Test
	public void same() {
		int[] array = {0, 9, 20, 29};
		assertEquals(RangeSet.basis(new RangeSet(array), new RangeSet(array)), new RangeSet(array));
	}

	@Test
	public void addEmpty() {
		int[] array = {0, 9, 20, 29};
		assertEquals(RangeSet.basis(new RangeSet(array), new RangeSet(new int[0])), new RangeSet(array));
	}

	@Test
	public void addToEmpty() {
		int[] array = {0, 9, 20, 29};
		assertEquals(RangeSet.basis(new RangeSet(new int[0]), new RangeSet(array)), new RangeSet(array));
	}

	@Test
	public void doubleEmpty() {
		int[] array = {};
		assertEquals(RangeSet.basis(new RangeSet(array), new RangeSet(array)), new RangeSet(array));
	}

	@Test
	public void properIntersect() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 9}),
						new RangeSet(new int[]{5, 14})
				),
				new RangeSet(new int[]{0, 4, 5, 9, 10, 14})
		);
	}

	@Test
	public void edgeContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 9}),
						new RangeSet(new int[]{5, 9})
				),
				new RangeSet(new int[]{0, 4, 5, 9})
		);
	}

	@Test
	public void properContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 14}),
						new RangeSet(new int[]{5, 9})
				),
				new RangeSet(new int[]{0, 4, 5, 9, 10, 14})
		);
	}

	@Test
	public void doubleProperContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{5, 9, 15, 19}),
						new RangeSet(new int[]{0, 24})
				),
				new RangeSet(new int[]{0, 4, 5, 9, 10, 14, 15, 19, 20, 24})
		);
	}

	@Test
	public void doubleEdgeContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 19}),
						new RangeSet(new int[]{0, 4, 15, 19})
				),
				new RangeSet(new int[]{0, 4, 5, 14, 15, 19})
		);
	}

	@Test
	public void noIntersections() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 0, 2, 2}),
						new RangeSet(new int[]{4, 4, 6, 6})
				),
				new RangeSet(new int[]{0, 0, 2, 2, 4, 4, 6, 6})
		);
	}

	@Test
	public void bricks() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 1, 2, 3, 4, 5, 6, 7}),
						new RangeSet(new int[]{1, 2, 3, 4, 5, 6, 7, 8})
				),
				new RangeSet(new int[]{0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8})
		);
	}

}
