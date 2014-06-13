package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;

import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.GenericAction;

@SuppressWarnings("serial")
public class RedoActionButton extends GenericAction {

    private final RenderFrame mFrame;

    public RedoActionButton(RenderFrame frame) {
        mFrame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        mFrame.getClient().redo();
    }
}
