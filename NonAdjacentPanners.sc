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

