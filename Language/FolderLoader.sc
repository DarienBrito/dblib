+ Dialog {

	*openFolder { arg okFunc, cancelFunc;
		^FileDialog.new( okFunc, cancelFunc, 2, 0,stripResult:true );
	}
}