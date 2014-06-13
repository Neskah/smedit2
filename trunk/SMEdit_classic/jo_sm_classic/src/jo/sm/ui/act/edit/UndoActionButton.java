package jo.sm.ui.act.edit;

import java.awt.event.ActionEvent;
import jo.sm.ui.RenderFrame;
import jo.sm.ui.act.Base;

@SuppressWarnings("serial")
public class UndoActionButton extends Base {

    private final RenderFrame mFrame;

    public UndoActionButton(RenderFrame frame) {
        mFrame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        mFrame.getClient().undo();
    }

}
