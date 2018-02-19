SynthsFileMaker {
	var name, path, file;

	*new { |name, path|
		^super.new.init(name, path);
	}

	init {|name_, path_ |
		name = name_;
		path = path_;

		this.createFile;
		this.setSynths;
		this.finalizeFile;
	}

	createFile {
		var formatedPath;
		formatedPath = path ++ name ++ "_Synths.scd";
		file = File(formatedPath,"w"); // A write file
	}

	setSynths{
		file.write("(\n");
		file.write("// Create synths here... \n");
		5.do{file.write("\n")};
		file.write(")\n");
	}

	finalizeFile {
		file.close;
		(name.toLower ++ "_synths" ++ " created at: %").format(path).postln;
	}


}