package jo.sm.ui.act.plugin;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.Map;

import jo.sm.logic.ShapeLibraryLogic;

public class ShapeLibPropertyEditor extends PropertyEditorSupport {

    private final Map<String, Object> mKeyToValue;
    private Map<Object, String> mValueToKey;
    private String[] mTags;

    public ShapeLibPropertyEditor() {
        super();
        mKeyToValue = ShapeLibraryLogic.getEntryMap();
        init();
    }

    public ShapeLibPropertyEditor(Object bean) {
        super(bean);
        mKeyToValue = ShapeLibraryLogic.getEntryMap();
        init();
    }

    private void init() {
        mTags = mKeyToValue.keySet().toArray(new String[0]);
        mValueToKey = new HashMap<>();
        for (String key : mTags) {
            mValueToKey.put(mKeyToValue.get(key), key);
        }
    }

    @Override
    public String getAsText() {
        return mValueToKey.get(getValue());
    }

    @Override
    public String[] getTags() {
        return mTags;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(mKeyToValue.get(text));
    }
}
