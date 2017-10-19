////////////////////////////////////////////////////////////////////////////////////////////////////
//
// GUIStyler
//
// * Minimal fancy looks for GUIs
//
// Copyright (C) <2016>
//
// by Darien Brito
// http://www.darienbrito.com
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.

GUIStyler {
	var <master, <skin, skinObj;
	var <alpha, <backgroundColor, <stringColor;
	var <buttonSize, <textSize, <sliderSize;
	var <gadgetWidth, <gadgetHeight, <font;
	var window, <marginW, <marginH, <decorator;
	var <stdFactor; //standard factor for spacing
	var <highLightColor, <knobSize, <thumbSize;
	var <backgroundSl, <onColor, <flowLayout;
	var <buttonWidth, <ezSizeVert, <ezSizeHorz;
	var <ezSizeW, <ezSizeH, fontType;

	*new {|master, skin = \black|
		^super.newCopyArgs(master, skin).init;
	}

	init {
		skinObj = GUISkins(skin);
		alpha = skinObj.alpha;
		backgroundColor = skinObj.backgroundColor;
		stringColor = skinObj.stringColor;
		fontType = skinObj.fontType;
		font = skinObj.font;
		highLightColor = skinObj.highLightColor;
		backgroundSl = skinObj.backgroundSl;
		onColor = skinObj.onColor;
		this.makeDefaultSizes();
	}

	makeDefaultSizes {
		gadgetWidth = 25;
		gadgetHeight = 20;
		buttonWidth = gadgetWidth * 2;
		buttonSize = buttonWidth@gadgetHeight;
		textSize = gadgetWidth@gadgetHeight;
		ezSizeW = 280;
		ezSizeH = 220;
		sliderSize = gadgetWidth@180;
		ezSizeVert = gadgetWidth@ezSizeH;
		ezSizeHorz = ezSizeW@gadgetHeight;
		marginW = 2;
		marginH = 2;
		stdFactor = 32;
		knobSize = 0;
		thumbSize = 10;
	}

	getWindow { | name = "My window", bounds, border = false, scroll = false |
		var window;
		if(scroll)
		{ window = ScrollView(master, bounds) }
		{ window = View(master, bounds) };
		window
		.alpha_(alpha)
		.alwaysOnTop_(true)
		.background = backgroundColor;
		^window;
	}

	getButton { |parent, labelOn, labelOff = nil|
		var button = Button(parent, buttonSize)
		.font_(font);
		if(labelOff.isNil){
			^button.states_([ [labelOn, stringColor, backgroundColor] ]);
		}{
			^button.states_([ [labelOn, stringColor, backgroundColor], [labelOff,stringColor, onColor] ])
		}
		^button
	}

	getSizableButton { |parent, labelOn, labelOff = nil, size|
		var button = Button(parent, size)
		.font_(font);
		if(labelOff.isNil){
			^button.states_([ [labelOn, stringColor, backgroundColor] ]);
		}{
			^button.states_([ [labelOn, stringColor, backgroundColor], [labelOff,stringColor, onColor] ])
		}
		^button
	}

	getSubtitleText { |parent, text, decorator, fontSize = 10, bold = true,align=\center,width = nil|
		if(width == nil){width = decorator.indentedRemaining.width};
		^StaticText(parent, (width)@gadgetHeight )
		.align_(align)
		.font_(Font(fontType, fontSize, bold))
		.string_(text)
		.stringColor_(stringColor)
		.background_(backgroundColor);
	}

	getText { |parent, text|
		^StaticText(parent, textSize)
		.align_(\center)
		.font_(font)
		.string_( text )
		.stringColor_(stringColor)
		.background_(backgroundColor);
	}

	getMultiLineText {|parent, bounds, fontSize = 10|
		^TextView(parent, bounds)
		.font_(Font(fontType, fontSize))
		.stringColor_(stringColor)
		.background_(backgroundColor)
		.hasVerticalScroller_(true)
		.editable_(false)
	}

	getSizableText { |parent, text, width, align = \center, fontSize = 10, bold = false|
		^StaticText(parent, width@gadgetHeight)
		.align_(align)
		.font_(Font(fontType, fontSize, bold))
		.string_( text )
		.stringColor_(stringColor)
		.background_(backgroundColor);
	}

	getSlider { |parent, orientation = \v|
		^SmoothSlider( parent, sliderSize)
		.orientation_(orientation)
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\left)
		.font_(font);
	}

	getSizableSlider { |parent, bounds, orientation = \v|
		^SmoothSlider( parent, bounds)
		.orientation_(orientation)
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\left)
		.font_(font);
	}

	getNumberBox { |parent, step|
		^NumberBox(parent, buttonWidth@gadgetHeight)
		.background_(backgroundSl)
		.align_(\left)
		.step_(step)
		.font_(font);
	}

	getSizableNumberBox { |parent, size, step|
		^NumberBox(parent, size)
		.background_(backgroundSl)
		.align_(\left)
		.step_(step)
		.font_(font);
	}

	getRangeSlider { |parent|
		^SmoothRangeSlider(parent, sliderSize)
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\left)
		.value_([0, 0])
	}

	getEZSlider { |parent, label, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothSliderAntialias(parent, ezSizeVert, label, spec, layout: orientation)
		} {
			ezSmooth = EZSmoothSliderAntialias(parent, ezSizeHorz, label, spec, layout: orientation)
		};
		ezSmooth.sliderView
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.numberView
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor);
		^ezSmooth;
	}

	getSizableEZSlider { |parent, label, bounds, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothSliderAntialias(parent, bounds, label, spec, layout: orientation)
		} {
			ezSmooth = EZSmoothSliderAntialias(parent, bounds, label, spec, layout: orientation)
		};
		ezSmooth.sliderView
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.numberView
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor)
		^ezSmooth;
	}

	getEZRanger { |parent, label, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothRangerAntialias(parent, ezSizeVert, label, spec, layout: orientation)
		} {
			ezSmooth = EZSmoothRangerAntialias(parent, ezSizeHorz, label, spec, layout: orientation)
		};
		ezSmooth.rangeSlider
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.loBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.hiBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor);
		^ezSmooth;
	}

	getSizableEZRanger { |parent, label, bounds, spec, orientation = \vert|
		var ezSmooth;
		if(orientation == \vert) {
			ezSmooth = EZSmoothRangerAntialias(parent, bounds, label, spec, layout: orientation)
		} {
			ezSmooth = EZSmoothRangerAntialias(parent, bounds, label, spec, layout: orientation)
		};
		ezSmooth.rangeSlider
		.knobSize_(knobSize).thumbSize_(thumbSize)
		.hilightColor_(highLightColor)
		.value_(0.5)
		.background_(backgroundSl)
		.align_(\center)
		.value_([0, 0]);
		ezSmooth.loBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.hiBox
		.font_(font)
		.stringColor_(stringColor)
		.normalColor_(stringColor)
		.align_(\center)
		.background_(backgroundColor);
		ezSmooth.labelView
		.align_(\center)
		.font_(font)
		.stringColor_(stringColor)
		.background_(backgroundColor);
		^ezSmooth;
	}

	getPopUpMenu { |parent, width|
		var pop = PopUpMenu(parent, width@gadgetHeight)
		.background_(backgroundColor)
		.stringColor_(stringColor)
		.font_(font);
		^pop
	}

	getButtonGrid {| num, labels, offsetX, offsetY |
	}

	getCheckBox{ |parent,text, boundX=20, boundY=20|
		var check = CheckBox(parent,boundX@boundY,text)
		.background_(backgroundColor)
		.font_(font);
		^check;
	}

	getColoredRect{|parent,color,align=\left|
		^StaticText(parent, 2@20)
		.align_(align)
		.string_( "" )
		.background_(color);
	}

	drawLine{|master, bounds, startPoints, endPoints, color|
		UserView(master, bounds)
		.drawFunc = {
			Pen.strokeColor = color;
			Pen.width = 1;
			Pen.moveTo(startPoints);
			Pen.lineTo(endPoints);
			Pen.stroke;
		};
	}
}