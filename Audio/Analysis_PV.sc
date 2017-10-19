////////////////////////////////////////////////////////////////////////////////////////////////////
//
// Analysis for phase vocoders
//
// * A convenience class to handle files for phase vocoders
//
// Copyright (C) <2017>
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

Analysis_PV {
	var <file, <outPath, soundBuf, analysisBuf, nChans;
	var <>hopsize;

	*new { | file, outPath, hopsize = 0.25 |
		^super.new.init(file, outPath, hopsize)
	}

	init { | file_, outPath_, hopsize_ |
		file = file_;
		outPath = outPath_;
		hopsize = hopsize_;
		this.analysisSynths();
		this.listener();
		this.getFileData();
	}

	getFileData {
		var soundFile = SoundFile(file);
		soundFile.openRead;
		soundFile.close;
		fork {
			//soundBuf = Buffer.read(Server.default, file);
			soundBuf = Buffer.readChannel(Server.default, file, channels: 0); // For now just mono
			(Server.default).sync;
			nChans = soundBuf.numChannels;
			analysisBuf = Buffer.alloc(Server.default, soundFile.duration.calcPVRecSize(1024, hopsize), nChans);
			"Analysis buffer for target file allocated".postln;
			(Server.default).sync;
			this.analyze();
			"Analyzing..."
		};
	}

	analyze {
		// Check number of channels for analysis and proceed...
		if(nChans > 1) {
			Synth(\pvrecStereo, [ \soundBuf, soundBuf, \analysisBuf, analysisBuf ] );
			"Stereo".postln;
		} {
			Synth(\pvrecMono, [ \soundBuf, soundBuf, \analysisBuf, analysisBuf ] );
			"Mono".postln;
		};
		"Running analysis...".postln;
	}

	export { | path |
		analysisBuf.write(path ++ ".scpv", "aiff", "float32");
	}

	analysisSynths {
		SynthDef(\pvrecStereo, { |soundBuf=0, analysisBuf=0, frameSize=1024|
			// Number of channels must match (not possible to pass as argument)
			var sig = PlayBuf.ar(2, soundBuf, BufRateScale.kr(soundBuf), doneAction: 2);
			var fft = FFT(LocalBuf(frameSize, 2), sig, 0.25, 1);
			PV_RecordBuf.new(fft, analysisBuf, 0, 1, 0, 0.25, 1);
			SendReply.kr(Done.kr(sig), '/pv_analysis', 1);
			// No output, this Synth just does analysis
		}).add;
		SynthDef(\pvrecMono, { |soundBuf=0, analysisBuf=0, frameSize=1024|
			// Number of channels must match (not possible to pass as argument)
			var sig = PlayBuf.ar(1, soundBuf, BufRateScale.kr(soundBuf), doneAction: 2);
			var fft = FFT(LocalBuf(frameSize, 1), sig, 0.25, 1);
			PV_RecordBuf.new(fft, analysisBuf, 0, 1, 0, 0.25, 1);
			SendReply.kr(Done.kr(sig), '/pv_analysis', 1);
			// No output, this Synth just does analysis
		}).add;
	}

	listener {
		var counter = 0; // Done sends me 2 trigs, this is a trick to solve that
		OSCdef(\pv_analysis_listener, { | msg |
			counter = counter + 1;
			if(counter == 1){
				analysisBuf.write(outPath ++ ".scpv", "aiff", "float32");
				("Done. Analysis file exported to %".format(outPath)).postln;
				Buffer.freeAll;
				OSCdef(\pv_analysis_listener).free;
			};
		}, '/pv_analysis').fix;
	}
}