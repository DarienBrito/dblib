////////////////////////////////////////////////////////////////////////////////////////////////////
//
// Spatializers built around VBAP
//
// * Abstract class for Elements of the NLC framework
// It handles administration of Patterns and proxies
//
// Copyright (C) <2018>
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


/*
Convenience class for the Satosphere in Montreal, Canada
*/

Dspat_SATDOME {
	var <allAzimuthsAndElevations;
	var <zenith, top, mid, bottom;

	*new {
		^super.new.init();
	}

	init {
		allAzimuthsAndElevations = [
			// ZENITH
			[0, 90],
			// TOP
			[15, 50.62], [-45, 50.62], [-105, 50.62], [-165, 50.62], [135, 50.62], [75, 50.62],
			// MID
			[0, 28.12], [-30, 28.12], [-60, 28.12], [-90, 28.12], [-120, 28.12], [-150, 28.12],
			[180, 28.12], [150, 28.12], [120, 28.12], [90, 28.12], [60, 28.12], [30, 28.12],
			// BOTTOM
			[0, -5.62], [-30, -5.62], [-60, -5.62], [-90, -5.62], [-120, -5.62], [-150, -5.62],
			[180, -5.62], [150, -5.62], [120, -5.62], [90, -5.62], [60, -5.62], [30, -5.62]
		];
		// Per ring:
		zenith = [0, 90];
		top = 	[ [15, 50.62], [-45, 50.62], [-105, 50.62], [-165, 50.62], [135, 50.62], [75, 50.62] ];
		mid = [ [0, 28.12], [-30, 28.12], [-60, 28.12], [-90, 28.12], [-120, 28.12], [-150, 28.12],
			[180, 28.12], [150, 28.12], [120, 28.12], [90, 28.12], [60, 28.12], [30, 28.12] ];
		bottom = [	[0, -5.62], [-30, -5.62], [-60, -5.62], [-90, -5.62], [-120, -5.62], [-150, -5.62],
			[180, -5.62], [150, -5.62], [120, -5.62], [90, -5.62], [60, -5.62],[30, -5.62] ];
	}

	getSATDomeVBAPBuffer {
		var dome = VBAPSpeakerArray(3, allAzimuthsAndElevations);
		^dome.loadToBuffer;
	}
}


Dspat_VBAP {
	var <synthAddresses;

	*new{
		^super.new.init();
	}

	init {
		Server.default.waitForBoot {
			synthAddresses = ();
			this.createSynths();
			// Create addresses to use when calling the synths
			[6, 7, 8, 12, 31].do{ |i| synthAddresses.put(i, "\dspat_ringVBAP%".format(i).asSymbol) };
		}
	}

	computeAngles { | n |
		var step = 360 / n;
		var angles = n.collect{|i| i * step};
		^angles;
	}

	getVBAPBuffer { | n |
		var angles = this.computeAngles(n);
		var buffer =  VBAPSpeakerArray(2, angles);
		^buffer.loadToBuffer;
	}

	createSATDomeSynths {
		var labels = [\SATfulldome, \SATring];
		var satdomebuffer = Dsapt_SATDOME().getSATDomeVBAPBuffer;

		SynthDef(\dspat_SATfullDome, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, spr = 0, amp = 1|
			var panned, n, sig, azi, ele, outs;
			n = 31;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			azi =  LFNoise2.ar(movSpeed).range(-180, 180).circleRamp;
			ele = LFNoise2.ar(movSpeed).range(-5.62, 90).circleRamp;
			panned = VBAP.ar(n, sig, satdomebuffer, [azi - (0.5 * width), azi + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;
		SynthDef(\dspat_SATRingStatic, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 12;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  180;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;
		[\dspat_SATfullDome, \dspat_SATRingStatic].do{|item, i| synthAddresses.put(labels[i], item) };
		"SAT synths loaded".postln;
	}

	createSynths {
		var labels = [\static8];
		SynthDef(\dspat_ringVBAP4, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 4;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  LFNoise2.ar(movSpeed).range(0, 360).circleRamp;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);  // This sets the order of the outputs!
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;

		SynthDef(\dspat_ringVBAP6, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 6;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  LFNoise2.ar(movSpeed).range(0, 360).circleRamp;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);  // This sets the order of the outputs!
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;

		SynthDef(\dspat_ringVBAP7, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 7;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  LFNoise2.ar(movSpeed).range(0, 360).circleRamp;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;

		SynthDef(\dspat_ringVBAP8, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0, amp = 1|
			var panned, n, sig, panner, outs;
			n = 8;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  LFNoise2.ar(movSpeed).range(0, 360).circleRamp;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;

		SynthDef(\dspat_ringVBAP12, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 12;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  LFNoise2.ar(movSpeed).range(0, 360).circleRamp;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;

		SynthDef(\dspat_ringVBAP31, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 31;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  LFNoise2.ar(movSpeed).range(0, 360).circleRamp;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;

		SynthDef(\dspat_RingStatic8, { | buf, outOffset = 0, movSpeed = 0.05, width = 60, ele = 0, spr = 0,  amp = 1|
			var panned, n, sig, panner, outs;
			n = 8;
			sig = PlayBuf.ar(2, buf, doneAction: 2);
			panner =  180;
			panned = VBAP.ar(n, sig, this.getVBAPBuffer(n), [panner - (0.5 * width), panner + (0.5 * width)], ele, spr);
			n.do{|bus, i|
				Out.ar(bus + outOffset, panned[0][i] * amp);
				Out.ar(bus + outOffset, panned[1][i] * amp);
			};
		}).add;
		[\dspat_RingStatic8].do{|item, i| synthAddresses.put(labels[i], item) };
		"VBAP synths loaded".postln;
	}
}

Dspat_Nonadjacent {
	var <synths;

	*new {
		^super.new.init();
	}

	init {
		synths = (\nonadjacent : \nonAdjacent, \specific : \specific);
		this.createSynths();
	}

	createSynths {
		// Wa can use this guy for non-adjacent panning. For instance from zenith to down
		SynthDef(\nonAdjacent, { | buf, from = 0, to = 12, time=10, outOffset = 0 |
			var player = PlayBuf.ar(2, buf, doneAction: 2);
			var sig = player.collect{|c| c }.sum;
			sig = Pan2_8.ar(sig, from, to, Line.kr(0, 1, time));
			Out.ar(outOffset, sig);
		}).add;

		// SPECIFIC PLAYBACK
		SynthDef(\specific, { | buf, busA, busB, amp|
			var player = PlayBuf.ar(2, buf, doneAction: 2);
			Out.ar(busA, player[0] * amp);
			Out.ar(busB, player[1] * amp);
		}).add;
	}

}

Dspat_BatchRecorder {
	var <bufs, <labels, <path;
	var <batchnum, <numChannels;

	*new { | path, batchnum = 1, numChannels = 2 |
		^super.new.init(path, batchnum, numChannels);
	}

	init { | path_, batchnum_, numChannels_|
		path = path_;
		batchnum = batchnum_;
		numChannels = numChannels_;
		labels = this.gatherLabels(path);
		bufs = this.gatherSamples(labels, path);
		this.generateSynthStrings();
	}

	gatherSamples { | labels, path |
		var out = Dictionary();
		var samples = (path ++ "*").pathMatch;
		samples.do{|path, i| if(path.last.asString != "d") { out.put(labels[i], Buffer.read(Server.default, path)) } };
		^out;
	}

	gatherLabels { | path |
		var labels = List();
		var paths = (path ++ "*").pathMatch;
		paths.do{|l| if(l.last.asString != "d") { labels.add(PathName(l).fileNameWithoutExtension.asSymbol) } };
		^labels;
	}

	generateSynthStrings {
		labels.do{|l|
			"Synth(~synths[n], [\\buf, ~bufs[%%], \\movSpeed, 0.05, \\spr, 0, \\width, 0, \\outOffset, ~offset, \\amp, ~amp]);".format("\\", l).postln;
		};
	}

	prepareRecording { | outputPath |
		var p;
		if(outputPath.isNil) {
			p = path ++ "Batch%.wav".format(batchnum);
		} {
			p = outputPath ++ "Batch%.wav".format(batchnum);
		};
		Server.default.prepareForRecord(p, numChannels);
	}

	freeBuffers {
		bufs.do{ | b | b.free };
	}
}

//
// Dspat_RandomSpatializer {
//
// 	*new { | |
//
// 	}
//
// 	init {
// 		this.createSynths();
// 	}
//
// 	createSynths {
// 		SynthDef(\basicPan6, { | buf, outOffset = 0, movSpeed = 0.05|
// 			var n = 6;
// 			var sig = PlayBuf.ar(2, buf, doneAction: 2);
// 			var panner =  LFNoise2.ar(movSpeed).range(0, n);
// 			var panned = PanAz.ar(n, sig[0], panner) + PanAz.ar(n, sig[1], panner).rotate(2);
// 			Out.ar(outOffset, panned);
// 		}).add;
//
// 		SynthDef(\basicPan7, { | buf, outOffset = 0, movSpeed = 0.05|
// 			var n = 7;
// 			var sig = PlayBuf.ar(2, buf, doneAction: 2);
// 			var panner =  LFNoise2.ar(movSpeed).range(0, n);
// 			var panned = PanAz.ar(n, sig[0], panner) + PanAz.ar(n, sig[1], panner).rotate(2);
// 			Out.ar(outOffset, panned);
// 		}).add;
//
// 		SynthDef(\basicPan8, { | buf, outOffset = 0, movSpeed = 0.05|
// 			var n = 8;
// 			var sig = PlayBuf.ar(2, buf, doneAction: 2);
// 			var panner =  LFNoise2.ar(movSpeed).range(0, n);
// 			var panned = PanAz.ar(n, sig[0], panner) + PanAz.ar(n, sig[1], panner).rotate(2);
// 			Out.ar(outOffset, panned);
// 		}).add;
//
// 		SynthDef(\basicPan12, { | buf, outOffset = 0, movSpeed = 0.05|
// 			var n = 12;
// 			var sig = PlayBuf.ar(2, buf, doneAction: 2);
// 			var panner =  LFNoise2.ar(movSpeed).range(0, n);
// 			var panned = PanAz.ar(n, sig[0], panner) + PanAz.ar(n, sig[1], panner).rotate(2);
// 			Out.ar(outOffset, panned);
// 		}).add;
//
// 		SynthDef(\basicPan31, { | buf, outOffset = 0, movSpeed = 0.05|
// 			var n = 31;
// 			var sig = PlayBuf.ar(2, buf, doneAction: 2);
// 			var panner =  LFNoise2.ar(movSpeed).range(0, n);
// 			var panned = PanAz.ar(n, sig[0], panner) + PanAz.ar(n, sig[1], panner).rotate(2);
// 			Out.ar(outOffset, panned);
// 		}).add;
// 	}
//
// }

