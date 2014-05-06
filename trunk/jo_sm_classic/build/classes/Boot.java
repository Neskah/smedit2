import java.io.IOException;
import java.net.URLDecoder;

public class Boot {

	public static void main(final String[] args) throws IOException {
		String location = Boot.class.getProtectionDomain().getCodeSource()
		.getLocation().getPath();
		location = URLDecoder.decode(location, "UTF-8").replaceAll("\\\\", "/");
		final String os = System.getProperty("os.name").toLowerCase();
		final String flags = "-Xmx1326m -Dsun.java2d.d3d=false -Dsun.java2d.opengl=true";

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
