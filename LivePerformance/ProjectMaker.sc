ProjectMaker {
	var projectName, <path, numScenes , sceneName, numOps;

	*new{|projectName, numScenes = 4, numOps = 5|
		^super.new.init(projectName, numScenes, numOps);
	}

	init {|projectName_, numScenes_, numOps_|
		projectName = projectName_;
		numScenes = numScenes_;
		sceneName = projectName ++ "_Scene";
		numOps = numOps_;
		Dialog.openFolder({|path_|
			path = path_ ++ "/";
			this.createUtilities;
			this.createScenes;
		});
	}

	createUtilities {
		LoaderMaker(projectName, path, numScenes);
		SynthsFileMaker(projectName, path);
		PerformMaker(projectName, path);
	}

	createScenes{
		numScenes.do{|i|
			SceneMaker(sceneName ++ (i+1).asString, path, numOps);
		}
	}

/*	make {
		this.createUtilities;
		this.createScenes;
	}*/
}



