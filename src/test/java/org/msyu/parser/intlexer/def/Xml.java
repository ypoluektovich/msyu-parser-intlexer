package org.msyu.parser.intlexer.def;

import org.msyu.parser.intlexer.RangeSet;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public final class Xml {

	public static final RangeSet NAME_START_CHAR = new RangeSet(new int[]{
			':', ':' + 1,
			'A', 'Z' + 1,
			'_', '_' + 1,
			'a', 'z' + 1,
			0xC0, 0xD7,
			0xD8, 0xF7,
			0xF8, 0x300,
			0x370, 0x37E,
			0x37F, 0x2000,
			0x200C, 0x200E,
			0x2070, 0x2190,
			0x2C00, 0x2FF0,
			0x3001, 0xD800,
			0xF900, 0xFDD0,
			0xFDF0, 0xFFFE,
			0x10000, 0xF0000
	});

	public static final RangeSet NAME_CHAR = RangeSet.basis(
			NAME_START_CHAR,
			new RangeSet(new int[]{
					'-', '-' + 1,
					'.', '.' + 1,
					'0', '9' + 1,
					0xB7, 0xB8,
					0x300, 0x370,
					0x203F, 0x2041
			})
	);

	public static final ADef NAME = new ComplexSeq(asList(
			new SimpleSeq(singletonList(NAME_START_CHAR)),
			new Rpt(new SimpleSeq(singletonList(NAME_CHAR)))
	));

}
