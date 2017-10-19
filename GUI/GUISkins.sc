////////////////////////////////////////////////////////////////////////////////////////////////////
//
// GUISkins
//
// * Color combinations for GUIStyler
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

GUISkins {
	var <skin;
	var <alpha, <backgroundColor, <stringColor, <fontType, <font;
	var <highLightColor, <backgroundSl, <onColor;

	*new{|skin|
		^super.new.init(skin);
	}

	init{ |skin_|
		skin = skin_;
		switch(skin)
		{\gray} {this.graySkin()}
		{\black} {this.blackSkin()};
	}

	graySkin{
		alpha = 0.95;
		backgroundColor = Color.gray(0.2, alpha);
		stringColor = Color.white;
		fontType = "Helvetica";
		font = Font(fontType, 10);
		highLightColor = Color.blue(0.5,0.2);
		backgroundSl = Color.white;
		onColor = Color.gray(0.4, 1);

	}

	blackSkin{
		alpha = 0.98;
		backgroundColor = Color(0.1,0.1,0.1);
		stringColor = Color.white();
		fontType = "Helvetica";
		font = Font(fontType, 10);
		highLightColor = Color.blue(0.5,0.2);
		backgroundSl = Color.white;
		onColor = Color(0.2, 0.2 ,0.5);
	}

}
