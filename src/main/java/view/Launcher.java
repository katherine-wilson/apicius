package view;

import javafx.application.Application;

/**
 * This class launches the <i>Apicius</i> application. It must be run with
 * the following VM arguments in order for the GUI to load properly:<br>
 * <pre>
 * 		<code>--module-path</code> "[path to JavaFX 17.0.1]"<code>
 * 		--add-modules javafx.controls,javafx.fxml 
 * 		--add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED</code>
 * </pre>
 * 
 * @see <code><a href="../view/GUI.html">GUI</a></code>
 * 
 * @author Katherine Wilson
 */
public class Launcher {
	/**
	 * Launches <i>Apicius</i> as a JavaFX Application.
	 * 
	 * @param args No program arguments expected.
	 */
	public static void main(String[] args) {
		Application.launch(GUI.class, args);
	}
}