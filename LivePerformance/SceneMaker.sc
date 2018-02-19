SceneMaker {
	var name, path, file, scnName, numOps;

	*new{ |name, path, numOps = 5|
		^super.new.init(name, path, numOps);
	}

	init { | name_, path_, numOps_ |
		name = name_;
		path = path_;
		scnName = name.toLower;
		numOps = numOps_;
		this.createFile;
		this.setCorpus;
		this.finalizeFile;
	}

	createFile {
		var formatedPath;
		formatedPath = path++name++".scd";
		file = File(formatedPath,"w"); // A write file
	}

	setCorpus {

		file.write("(\n"); // Global Parenthesis

		file.write("~"++scnName++" = ();\n");
		file.write("\n");
		this.setLoad;
		file.write("\n");
		this.setMIDIController;
		file.write("\n");
		this.setNdefs;
		file.write("\n");
		this.setPbindefs;
		file.write("\n");
		this.setRun;
		file.write("\n");
		this.setFinished;
		file.write("\n");
		this.setNotify;

		file.write(")\n");
	}

	setLoad {
		var preClean = ["Pbindef.clear;", "Ndef.clear;", "Buffer.freeAll;", "MIDIdef.freeAll;"];
		file.write("~"++scnName++".load" ++ " = {\n");
		file.write($\t ++ "var location = ~getLocation.();\n");
		file.write("\n");
		preClean.do{|str| file.write($\t ++ str++"\n")};
		file.write("\n");
		file.write($\t ++ "~samples = [\n");
		file.write($\t ++ "];\n");
		file.write($\t ++ "~labels = [\n");
		file.write($\t ++ "];\n");
		file.write($\t ++ "~bufs = ();\n");
		file.write($\t ++ "~samples.do{|item, i| ~bufs.put(~labels[i], Buffer.read(s, item).bufnum)}\n");
		file.write("};\n");
	}

	setMIDIController {
		var forLastIter = numOps - 1;
		file.write("~" ++ scnName++".midi" ++ " = {\n");
		file.write($\t ++ "var ccNum = 45;\n");
		file.write($\t ++ "var tracker = 0;\n");
		file.write("\n");
		file.write($\t ++ "MIDIdef.cc(" ++ $\\ ++ "trig,{| val |\n");
		file.write($\t ++ $\t ++ "if (val != 0) {\n");
		file.write($\t ++ $\t ++ $\t ++ "tracker = tracker + 1;\n");
		file.write($\t ++ $\t ++ $\t ++ "switch(tracker)\n");
		numOps.do{|i|
			if(i < forLastIter) { // find last
				file.write($\t ++$\t ++ $\t ++ "{ " ++ (i+1).asString ++ " }" ++ " {\n");
				//2.do{file.write("\n")}; //add some spaces
				file.write($\t++$\t ++ $\t ++ "}\n");

			} {
				file.write($\t++$\t ++ $\t ++ "{ " ++ (i+1).asString ++ " }" ++ " {\n");
				//2.do{file.write("\n")}; //add some spaces
				file.write($\t++$\t ++ $\t ++ "};\n");
			}
		};
		file.write($\t ++ $\t ++$\t ++ "(" ++ $\" ++ "-> Now playing: " ++ $\" ++ " ++ tracker).postln;\n");
		file.write($\t++ $\t ++ "};\n");
		// MIDIFunc
		file.write($\t++"}, ccNum );\n");
		//Final function
		file.write("};\n");
	}

	setNdefs {
		file.write("~"++scnName++".ndefs" ++ " = {\n");
		file.write("\n");
		// Make Ndefs
		numOps.do{ |i|
			file.write($\t ++ "Ndef(" ++ $\\ ++ "ndef" ++ i.asString ++ ", {\n");
			file.write($\t ++ "});\n");
			file.write("\n");
		};
		file.write("};\n");
	}

	setPbindefs {
		file.write("~"++scnName++".pbindefs" ++ " = {\n");
		file.write($\t ++ "var tempo = 60/120;\n");
		file.write($\t ++ "var totalDur = 1;\n");
		file.write($\t ++ "var scale = Scale.augmented;\n");
		file.write($\t ++ "var octave = 0;\n");
		file.write("\n");
		// Make Pbinds
		numOps.do{ |i|
			file.write($\t ++ "Pbindef(" ++ $\\ ++ "pbindef" ++ i.asString ++ ",\n");
			file.write($\t ++ ");\n");
			file.write("\n");
		};
		file.write("};\n");
	}


	setRun {
		file.write("~"++scnName++".run" ++ " = {\n");
		//2.do{file.write("\n")}; //add some spaces
		file.write("};\n");
	}

	setFinished{
		file.write("~"++scnName++".finished" ++ " = {\n");
		file.write("};\n");
	}

	setNotify{
		file.write($\" ++ scnName.toUpper ++ " loaded" ++ $\" ++ ".postln;\n");
	}

	finalizeFile {
		file.close;
		(scnName ++ " created at: %").format(path).postln;
	}

}

