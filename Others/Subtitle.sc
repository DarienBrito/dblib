Subtitle {
	var subtitle, lenght;

	*new{|subtitle, length = 25|
		^super.new.init(subtitle, length);
	}

	init{|s_, n_|
		subtitle = s_;
		lenght = n_;
		"/*%\n".format(("_" ! lenght).join).postln;
		subtitle.postln;
		"%*/\n".format(("_" ! lenght).join).postln;
	}
}

