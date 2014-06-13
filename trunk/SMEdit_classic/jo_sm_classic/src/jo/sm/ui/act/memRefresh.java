package jo.sm.ui.act;


/**
 * @author Robert Barefoot
 */
public class memRefresh extends Base {

	private static final long serialVersionUID = 4455772293454372123L;

	public memRefresh() {
	}

	@Override
	public void actionPerformed(final java.awt.event.ActionEvent e) {
		System.gc();
	}
}
