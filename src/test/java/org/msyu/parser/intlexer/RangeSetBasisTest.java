package org.msyu.parser.intlexer;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RangeSetBasisTest {

	@Test
	public void same() {
		int[] array = {0, 10, 20, 30};
		assertEquals(RangeSet.basis(new RangeSet(array), new RangeSet(array)), new RangeSet(array));
	}

	@Test
	public void addEmpty() {
		int[] array = {0, 10, 20, 30};
		assertEquals(RangeSet.basis(new RangeSet(array), new RangeSet(new int[0])), new RangeSet(array));
	}

	@Test
	public void addToEmpty() {
		int[] array = {0, 10, 20, 30};
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
						new RangeSet(new int[]{0, 10}),
						new RangeSet(new int[]{5, 15})
				),
				new RangeSet(new int[]{0, 5, 5, 10, 10, 15})
		);
	}

	@Test
	public void edgeContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 10}),
						new RangeSet(new int[]{5, 10})
				),
				new RangeSet(new int[]{0, 5, 5, 10})
		);
	}

	@Test
	public void properContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 15}),
						new RangeSet(new int[]{5, 10})
				),
				new RangeSet(new int[]{0, 5, 5, 10, 10, 15})
		);
	}

	@Test
	public void doubleProperContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{5, 10, 15, 20}),
						new RangeSet(new int[]{0, 25})
				),
				new RangeSet(new int[]{0, 5, 5, 10, 10, 15, 15, 20, 20, 25})
		);
	}

	@Test
	public void doubleEdgeContain() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 20}),
						new RangeSet(new int[]{0, 5, 15, 20})
				),
				new RangeSet(new int[]{0, 5, 5, 15, 15, 20})
		);
	}

	@Test
	public void noIntersections() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 1, 2, 3}),
						new RangeSet(new int[]{4, 5, 6, 7})
				),
				new RangeSet(new int[]{0, 1, 2, 3, 4, 5, 6, 7})
		);
	}

	@Test
	public void bricks() {
		assertEquals(
				RangeSet.basis(
						new RangeSet(new int[]{0, 2, 2, 4, 4, 6, 6, 8}),
						new RangeSet(new int[]{1, 3, 3, 5, 5, 7, 7, 9})
				),
				new RangeSet(new int[]{0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9})
		);
	}

}
