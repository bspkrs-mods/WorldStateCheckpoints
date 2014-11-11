package bspkrs.worldstatecheckpoints.fml;

import net.minecraft.client.settings.KeyBinding;
import bspkrs.fml.util.InputEventListener;
import bspkrs.worldstatecheckpoints.WSCSettings;

public class WSCKeyHandler extends InputEventListener
{

    public WSCKeyHandler(KeyBinding keyBinding, boolean allowRepeats)
    {
        super(keyBinding, allowRepeats);
    }

    @Override
    public void keyDown(KeyBinding kb, boolean isRepeat)
    {
        if (!isRepeat)
            WSCSettings.keyboardEvent(kb);
    }

    @Override
    public void keyUp(KeyBinding kb)
    {}
}
