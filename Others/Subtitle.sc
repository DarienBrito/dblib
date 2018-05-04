Subtitle {
	var lenght;

	*new{|length = 25|
		^super.new.init(length);
	}

	init{|n_|
		lenght = n_;
	}

	standard {|s_|
		"/*%\n".format(("_" ! lenght).join).postln;
		s_.postln;
		"%*/\n".format(("_" ! lenght).join).postln;
	}

	synthADay{
		"/*%\n".format(("_" ! lenght).join).postln;
		"Synth-a-day".postln;
		"\n".postln;
		"%".format(Date.getDate.format("%d of %B, %Y")).postln;
		"%*/\n".format(("_" ! lenght).join).postln;
	}
}

// Date.getDate.format("Today is %A. It is around %I o'clock (%p), in %B.");