CVPal {
	var <midi;

	*new {
		^super.new.init();
	}

	init {
		MIDIClient.init;
		midi = MIDIOut.newByName("CVpal", "CVpal");
		^midi
	}


}