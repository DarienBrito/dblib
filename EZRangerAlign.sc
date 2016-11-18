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