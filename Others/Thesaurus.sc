Thesaurus {
	classvar <synthdefs, <>path, <types, <synthsFolderPath, <synthsFullPath, <fileNames;
	classvar <allSynthDefsNames;

	*load { | synthDefsPath |
		var sp;
		if (synthDefsPath.isNil) {
			path = "/Users/darienbrito/Documents/SuperCollider/SCThesaurus/"; // ad yours here
		} {
			path = synthDefsPath;
		};
		fileNames = List();
		synthsFolderPath = path +/+ "SynthDefs";
		synthsFullPath = ( synthsFolderPath +/+ "*").pathMatch;
		synthsFullPath.do{|p| 	thisProcess.interpreter.compileFile(p).value; fileNames.add(PathName.new(p).fileName) };
		synthdefs = ~synthDefs; // hacky, but works. This object is defined as an even on startup.scd
		// Every document is prepared with sub-events that organize the Thesaurus, this is because SyntDefs
		// are declared by the user every time and cannot be known before they are added, unless stored in disk,
		// which we do not want to do as it is not recommended in the new SC system.
		allSynthDefsNames = List();
		synthdefs.do{|i| i.do{|j| if(j.class == SynthDef) { allSynthDefsNames.add(j.name) }} };
	}

	// Opens a window to navigate file locations
	*inspect {
		var w, decorator, styler, subStyler, childView, childDecorator, substyler;
		var searchText, searchList;
		var findFunc;
		if(synthdefs.isNil) { this.load };

		// Master window
		w = Window("Synths", 160@800);
		styler = GUIStyler(w);
		w.view.decorator = FlowLayout(w.view.bounds);
		decorator = w.view.decorator;
		decorator.margin = Point(0,0);

		// Child window
		childView = styler.getWindow("SynthDefs", w.view.bounds, scroll: true);
		childView.decorator = FlowLayout(childView.bounds);
		childDecorator = decorator;

		styler.getSizableText(childView, "THESAURUS ", 150, \center, 12);

		// Search
		styler.getSizableText(childView, "search", 90, \left, 10);

		searchText = TextField(childView, 150@20);

		searchList = ListView(childView, 150@60)
		.items_(allSynthDefsNames.asArray)
		.stringColor_(Color.white)
		.background_(Color.clear)
		.hiliteColor_(Color.new(0.3765, 0.5922, 1.0000, 0.5));

		findFunc = { |name|
		if ( name == "", {
			searchList.items_(allSynthDefsNames.asArray);
			},
		{
			var filtered = allSynthDefsNames.selectAs({ | item, i |
				item.asString.containsi(name);
				}, Array);
			searchList.items_(filtered)
			});
		};

		searchText.action = { |field |
			var search = field.value; findFunc.(search)
		};
		searchText.keyUpAction = {| view| view.doAction; };

		searchList.action_({ |sbs|
			var synthdef = sbs.items[sbs.value];
			synthdef.postln;
			Synth(synthdef); // Play it!
			//synthdef.class.postln;
			//b.at(v.items[sbs.value]).postln; // .value returns the integer
			//setSynthDef.(synthdef); //--- set SynthDef function you should implement it
		});

		/* What use could this have?
		a.asView.keyDownAction = { arg view, char, mod, unicode, keycode, key;
		[view, char, mod, unicode, keycode, key].postln;
		if ( keycode == 125, { l.focus(true )});
	};
		*/

		subStyler = GUIStyler(childView);
		synthdefs.keys.do{|k|
			var subView;
			styler.getSizableText(childView, k.asString.toUpper, 90, \left, 10);
			styler.getSizableButton(childView, "source", size: 50@20).
			action = {|btn|
				Document.open(synthsFolderPath +/+ synthdefs[k][\path]);
			};

			// Child of child window
			subView = subStyler.getWindow(k, 150@(synthdefs[k].size * 25));
			subView.decorator = FlowLayout(subView.bounds);
			synthdefs[k].do{|i|
				if(i.class == SynthDef) {
					var name = i.name;
					var btn = subStyler.getSizableButton(subView, name, size: 140@20);
					btn.action = { |btn|
						Synth(name);
					}
				}
			};
		};
		w.front;
	}

	*browseSynths{
		SynthDescLib.global.browse;
	}

	*getTypes {
		if(synthdefs.isNil) { this.load };
		^synthdefs.keys;
	}

	*count {
		var total, wordSize;
		if(synthdefs.isNil) { this.load };
		total = 0;
		wordSize = synthdefs.keys.maxItem.asString.size;
		synthdefs.keys.do{|k|
			if(k != \path) {
				var subgroup = synthdefs[k];
				var n = subgroup.size;
				var spacing = (" " ! (k.asString.size - wordSize).abs).join;
				"% |---- % synths".format(k ++ spacing, n).postln;
				//"\t %\n".format(subgroup).postln;
				total = total + n
				("-" ! 25).join.postln;
				"Total SynthDefs %".format(total).postln;
			}
		};
	}
}
