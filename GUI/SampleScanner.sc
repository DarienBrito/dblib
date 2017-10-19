SampleScanner {
	var path, <files;

	*new{|path|
		^super.new.init(path);
	}

	init{|path_|
		path = path_;
		files = (path ++ "*").pathMatch;
		"there are % fules".format(files.size).postln;
		this.createInterface();
	}

	createInterface{
		var width = 200, height = 600;
		var w = Window("Samples", width@height);
		var g = GUIStyler(w);
		var win = g.getWindow("samples", width@height, scroll: true);
		var decorator;
		win.decorator = FlowLayout(win.bounds);
		decorator = win.decorator;

		g.getSizableText(win, "List of samples", width, \left, 8);
		decorator.nextLine;

		files.do{|f, i|
			var pathName = PathName(f);
			g.getSizableText(win, "%: %".format(i, pathName.fileName), width, \left, 8);
			decorator.nextLine;
		};
		w.front;
	}
}

