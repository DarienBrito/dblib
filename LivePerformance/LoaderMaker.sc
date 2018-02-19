LoaderMaker {
	var name, path, file, numScenes;

	*new{ | name, path, numScenes = 4  |
		^super.new.init(name, path, numScenes);
	}

	init { |name_, path_, numScenes_|
		name = name_;
		path = path_;
		numScenes = numScenes_;
		this.createFile;
		this.setCorpus;
		this.finalizeFile;

	}

	setCorpus{

		file.write("(\n"); //Global Parenthesis

		this.setLocationFunc;
		file.write("\n");
		this.setMIDIwarnFunc;
		file.write("\n");
		this.setOSCTest;
		file.write("\n");
		this.setSetup;
		file.write("\n");
		this.setNetAddress;
		file.write("\n");
		this.setSceneLoader;
		file.write("\n");
		this.setNotify;
		file.write("\n");

		file.write(")\n");

	}

	createFile {
		var formatedPath;
		formatedPath = path++name++"_LoadSets.scd";
		file = File(formatedPath,"w"); // A write file
	}

	setLocationFunc {
		file.write("~getLocation = {\n");
		file.write($\t ++ "var location = " ++ $\" ++ $\" ++ ".resolveRelative;\n");
		file.write($\t ++ "location;\n");
		file.write("};\n")
	}

	setMIDIwarnFunc {
		file.write("~midiWarn = {\n");
		file.write($\t ++ $\" ++ $\" ++ ".postln;\n");
		file.write($\t ++ $\" ++ "You are running without MIDI controller: " ++ $\" ++ ".warn;\n");
		file.write($\t ++ $\" ++ $\" ++ ".postln;\n");
		file.write("};\n")
	}

	setSetup {
		file.write("~setup = {\n");
		file.write($\t ++ "s.waitForBoot {\n" );
		file.write($\t ++ $\t ++ "MIDIdef.freeAll;\n");
		file.write($\t ++ $\t ++ "MIDIClient.init;\n");
		file.write($\t ++ $\t ++ "s.sync;\n");
		file.write($\t ++ $\t ++ "MIDIIn.connectAll;\n");
		file.write($\t ++ $\t ++ "s.sync;\n");
		file.write($\t ++ $\t ++ "if( MIDIClient.sources.size < 1 ) { ~midiwarn.() };\n");
		file.write($\t ++ $\t ++ "StageLimiter.activate;\n");
		file.write($\t ++ $\t ++ $\" ++ "----------------------> [ " ++ name ++ " ready ]" ++ $\" ++ ".postln;\n");
		file.write($\t ++ "};\n");
		file.write("};\n");
		file.write("\n");
		file.write("// Start setup: \n"); //Comment
		file.write("~setup.();\n");
	}

	setOSCTest{
		file.write("~oscTest = {\n");
		file.write($\t ++ "Routine({\n");
		file.write($\t ++ $\t ++ "1000.do{|i|\n");
		file.write($\t ++ $\t ++ "n.sendMsg(" ++ $\" ++ "/test" ++ $\" ++ ", i );\n");
		file.write($\t ++ $\t ++ "(1/60).wait;\n");
		file.write($\t ++ $\t ++ "};\n");
		file.write($\t ++ "}).play;\n");
		file.write("};\n");
	}

	setNetAddress {
		file.write("// Set OSC output: \n"); // Comment
		file.write("n = NetAddr(" ++ $\" ++ "localhost" ++ $\" ++ ", 9999);\n");
	}

	setSceneLoader{
		file.write("// Load Scenes: \n"); // Comment
		file.write("~scenePaths = " ++ numScenes ++ ".collect{ |i| ~getLocation.()" ++ "++" ++ $\" ++ name ++ $\" ++ "++" ++
			$\" ++ "_Scene" ++ $\"++ "++" ++ "(i+1).asString" ++ "++" ++ $\" ++ ".scd" ++ $\" ++ "};\n");
		file.write("~scenePaths.do{ |scene| this.compileFile(scene).value };\n");
	}

	setNotify{
		file.write($\" ++ name.toUpper ++ " loaded" ++ $\" ++ ".postln;\n");
	}


	finalizeFile {
		file.close;
		(name.toLower ++ "_loader" ++ " created at: %").format(path).postln;
	}

}