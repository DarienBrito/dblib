/*_____________________________________________________________

dbLib [additions to SuperCollider]
Copyright (C) <2015>

by Darien Brito
http://www.darienbrito.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

________________________________________________________________*/

+ EZRanger {

	//Control alignments
	setStringAlign_{ arg align;
		labelView.notNil.if{labelView.align = align;};
		unitView.notNil.if{unitView.align = align;};
	}

	setNumbersAlign_{ arg align;
		loBox.align_(align);
		hiBox.align_(align);
	}

}

+ EZSlider {

	//Control alignments
	setStringAlign_{ arg align;
		labelView.notNil.if{labelView.align = align;};
		unitView.notNil.if{unitView.align = align;};
	}

	setNumbersAlign_{ arg align;
		numberView.align_(align);
	}

}