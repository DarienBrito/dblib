PerformMaker {
	var name, path, file;

	*new { | name, path |
		^super.new.init(name, path);
	}

	init { |name_, path_|
		name = name_;
		path = path_;
		this.createFile;
		this.setHeader;
		this.setSetup;
		this.setOSCTest;
		this.setScenes;
		this.finalizeFile;
	}

	createFile {
		var formatedPath;
		formatedPath = path ++ name ++ "_Perform.scd";
		file = File(formatedPath,"w"); // A write file
	}

	setHeader{
		var d = Date.getDate;
		file.write("/*_________________________________\n");
		file.write("\n");
		file.write("<<< " ++ name ++ ">>>\n");
		file.write("\n");
		file.write("Darien Brito\n");
		file.write(d.year.asString ++ "\n");
		file.write("darienbrito.com\n");
		file.write("_________________________________*/\n");
		file.write("\n");
	}

	setSetup{
		file.write("/*____________\n");
		file.write("\n");
		file.write("SETUP\n");
		file.write("____________*/\n");
		file.write("\n");
		file.write("(\n");
		file.write("~pathSets = " ++ $\" ++ path ++ name ++ "_LoadSets.scd" ++ $\" ++ ";\n");
		file.write("~pathSynths = " ++ $\" ++ path ++ name ++ "_Synths.scd" ++ $\" ++ ";\n");
		file.write("this.compileFile(~pathSets).value;\n");
		file.write("this.compileFile(~pathSynths).value;\n");
		file.write(")\n");
	}

	setOSCTest{
		file.write("\n");
		file.write("/*____________\n");
		file.write("\n");
		file.write("OSC test\n");
		file.write("____________*/\n");
		file.write("\n");
		file.write("~oscTest.()");
		file.write("\n");
	}

	setScenes{
		file.write("\n");
		file.write("/*____________\n");
		file.write("\n");
		file.write("SCENE 1\n");
		file.write("____________*/\n");
		file.write("\n");
		file.write($\" ++ "code scenes here... " ++ $\" ++ "\n");
	}

	finalizeFile {
		file.close;
		(name.toLower ++ "_perform" ++ " created at: %").format(path).postln;
	}

}

