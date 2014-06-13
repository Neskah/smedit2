package jo.sm.ui.act;

import jo.util.URLs;
import jo.util.io.HttpClient;

/**
 * @author Robert Barefoot
 */
public class GoToTwitter extends Base{

    private static final long serialVersionUID = 4455772293454372123L;

	public GoToTwitter() {
	}


    @Override
	public void actionPerformed(final java.awt.event.ActionEvent e) {
		HttpClient.openURL(URLs.TWIT);
	}

    
}
