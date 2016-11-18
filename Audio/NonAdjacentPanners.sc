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

// <Pseudo Ugens for non-adjacent panning>

Pan2_8 {
	*ar { |in, from = 0, to = 1, pos = 0, mul = 1|
		var delta_from, delta_to, sig, pan, mapPos;
		delta_from = Array.fill(8, { |i| ((from - i).abs < 1e-5) });
		delta_to = Array.fill(8, { |i| ((to - i).abs < 1e-5) });
		mapPos = pos.linlin(0.0, 1.0, -1, 1);
		pan = Pan2.ar(in, mapPos, mul);
		sig = (pan[0] * delta_from) + (pan[1] * delta_to);
		^sig
	}

	*kr { |in, from = 0, to = 1, pos = 0, mul = 1|
		var delta_from, delta_to, sig, pan;
		delta_from = Array.fill(8, { |i| ((from - i).abs < 1e-5) });
		delta_to = Array.fill(8, { |i| ((to - i).abs < 1e-5) });
		pos = pos.linlin(0.0, 1.0, -1, 1);
		pan = Pan2.kr(in, pos.linlin(0.0, 1.0, -1, 1), mul);
		sig = (pan[0] * delta_from) + (pan[1] * delta_to);
		^sig;
	}

}

