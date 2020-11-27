DB_AudioMingler {
	var <folder;
	var <buffers;

	*new { | folder |
		^super.new.init(folder);
	}

	init { | folder_|
		var paths;
		folder = folder_;
		paths = (folder ++ "*.wav").pathMatch;
		buffers = paths.collect{|path| Buffer.read(Server.default, path)};
		this.createPlayers();
	}

	createPlayers {
		SynthDef(\AudioMinglerStereo, {| out = 0, bufnum = 1, rate = 1 pos = 0.0, sustain=1, envType = 0|
			var width = 0.5;
			var holdT = sustain * width;
			var fadeT = 1 - width * sustain * 0.5;
			var sig = PlayBuf.ar(2, bufnum,BufRateScale.kr(bufnum) * rate, 1, BufFrames.kr(bufnum)*pos, loop:0);
			var env = Select.ar(envType, [
				EnvGen.ar(Env.sine(sustain,), doneAction: 2),
				EnvGen.ar(Env([0, 1, 1, 0], [fadeT, holdT, fadeT], \sin), doneAction: 2),
				EnvGen.ar(Env.perc(0.01, 0.99, ),timeScale: sustain, doneAction: 2),
				EnvGen.ar(Env.perc(0.99, 0.01, curve: 4),timeScale: sustain, doneAction: 2)
			]);
			sig = sig * env;
			Out.ar(out, sig);
		}).add
	}

	play { | sustain, pos, envType, rate, dur |
		Pbindef(\HighTech,
			\instrument, \AudioMinglerStereo,
			\sustain, sustain,
			\pos, pos,
			\envType, envType,
			\rate, rate,
			\bufnum, Pxrand(buffers, inf),
			\dur, dur,
		).play;
	}

}
/*
DB_AudioExtruder {
	var <buffer, <numChannels;

	*new { | bufferSize=1, numChannels=1 |
		^super.new.init(bufferSize, numChannels);
	}

	init { | bufferSize = 1, numChannels_ = 1 |
		numChannels = numChannels_;
		fork{
			buffer = Buffer.alloc(s, Server.default.sampleRate * bufferSize, numChannels);
			Server.default.sync;
			this.createSynths;
		}
	}


	createSynths {

		SynthDef(\ExtruderRecorder,{ | in= 0, bufnum = 1 |
			var inp , rec;
			inp = BufDelayC.ar(buffer, SoundIn.ar(0), Server.default.latency);
			rec = RecordBuf.ar(inp, bufnum);
		}).add;

		SynthDef(\ExtruderPlayerStereo, {| out = 0, bufnum = 1, rate = 1 pos = 0.0, sustain=1, envType = 0|
			var width = 0.5;
			var holdT = sustain * width;
			var fadeT = 1 - width * sustain * 0.5;
			var sig = PlayBuf.ar(2, bufnum,BufRateScale.kr(bufnum) * rate, 1, BufFrames.kr(bufnum)*pos, loop:0);
			var env = Select.ar(envType, [
				EnvGen.ar(Env.sine(sustain,), doneAction: 2),
				EnvGen.ar(Env([0, 1, 1, 0], [fadeT, holdT, fadeT], \sin), doneAction: 2),
				EnvGen.ar(Env.perc(0.01, 0.99, ),timeScale: sustain, doneAction: 2),
				EnvGen.ar(Env.perc(0.99, 0.01, curve: 4),timeScale: sustain, doneAction: 2)
			]);
			sig = sig * env;
			Out.ar(out, sig);
		}).add
	}

}*/
