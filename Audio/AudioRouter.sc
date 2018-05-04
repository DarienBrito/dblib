/*_____________________________________________________________

dbLib [additions to SuperCollider]
Copyright (C) <2015>

by Darien Brito
http://www.darienbrito.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

________________________________________________________________*/

// <Convenience class for routing audio>

AudioRouter {
	var in, out;

	*new { |input =  "Built-in Microph", output = "Built-in Output"|
		^super.new.init(input, output)
	}

	init {|in_, out_|
		in = in_;
		out = out_;
		this.setRoutes(in, out);
	}

	*devices {
		var ins, outs;
		ins = ServerOptions.inDevices;
		outs = ServerOptions.outDevices;
		"In devices: ".postln;
		ins.postln;
		"Out devices:".postln;
		outs.postln;
	}

	*getIns {
		^ServerOptions.inDevices;
	}

	*getOuts {
		^ServerOptions.outDevices;
	}

	setRoutes {|in, out|
		Server.default.options.inDevice_(in);
		Server.default.options.outDevice_(out);

	}

}