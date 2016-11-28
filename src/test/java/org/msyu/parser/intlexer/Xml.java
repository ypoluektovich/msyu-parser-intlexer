package org.msyu.parser.intlexer;

import static org.msyu.parser.intlexer.Defs.cseq;
import static org.msyu.parser.intlexer.Defs.rpt;
import static org.msyu.parser.intlexer.Defs.sseq;

public final class Xml {

	public static final RangeSet NAME_START_CHAR = new RangeSet(new int[]{
			':', ':',
			'A', 'Z',
			'_', '_',
			'a', 'z',
			0xC0, 0xD6,
			0xD8, 0xF6,
			0xF8, 0x2FF,
			0x370, 0x37D,
			0x37F, 0x1FFF,
			0x200C, 0x200D,
			0x2070, 0x218F,
			0x2C00, 0x2FEF,
			0x3001, 0xD7FF,
			0xF900, 0xFDCF,
			0xFDF0, 0xFFFD,
			0x10000, 0xEFFFF
	});

	public static final RangeSet NAME_CHAR = RangeSet.basis(
			NAME_START_CHAR,
			new RangeSet(new int[]{
					'-', '-',
					'.', '.',
					'0', '9',
					0xB7, 0xB7,
					0x300, 0x36F,
					0x203F, 0x2040
			})
	);

	public static final ADef NAME = cseq(sseq(NAME_START_CHAR), rpt(sseq(NAME_CHAR)));

}
