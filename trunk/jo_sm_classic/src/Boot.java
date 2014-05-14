/*
 * 2014 SMEdit development team
 * http://lazygamerz.org
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser Gereral Public Licence as published by the Free
 * Software Foundation; either version 3 of the Licence, or (at your opinion) any
 * later version.
 *
 * This library is distributed in the hope that it will be usefull, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of merchantability or fitness for a
 * particular purpose. See the GNU Lesser General Public Licence for more details.
 *
 * You should have received a copy of the GNU Lesser General Public Licence along
 * with this library; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, Ma 02111-1307 USA.
 *
 * http://www.gnu.org/licenses/lgpl.html (English)
 * http://gugs.sindominio.net/gnu-gpl/lgpl-es.html 
 *
 */

import java.io.IOException;
import java.net.URLDecoder;

public class Boot {

	public static void main(final String[] args) throws IOException {
		String location = Boot.class.getProtectionDomain().getCodeSource()
		.getLocation().getPath();
		location = URLDecoder.decode(location, "UTF-8").replaceAll("\\\\", "/");
		final String os = System.getProperty("os.name").toLowerCase();
		final String flags = "-Xmx1326m";

		if (os.contains("windows")) {
			Runtime.getRuntime().exec(
					"javaw " + flags + " -classpath \"" + location
					+ "\" jo.sm.ui.RenderFrame");
		} else if (os.contains("mac")) {
			Runtime.getRuntime().exec(
					new String[] {
							"/bin/sh",
							"-c",
							"java " + flags + " -Xdock:name=\"SMEdit_Classic\""
							+ " -Xdock:icon=jo/sm/ui/utils/icon64.png"
							+ " -classpath \"" + location
							+ "\" jo.sm.ui.RenderFrame" });
		} else {
			Runtime.getRuntime().exec(
					new String[] {
							"/bin/sh",
							"-c",
							"java " + flags + " -classpath \"" + location
							+ "\" jo.sm.ui.RenderFrame" });
		}
	}
}
