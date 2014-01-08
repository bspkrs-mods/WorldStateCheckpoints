package bspkrs.worldstatecheckpoints.fml;

import java.util.HashMap;

import net.minecraft.client.settings.KeyBinding;
import bspkrs.fml.util.InputEventListener;
import bspkrs.worldstatecheckpoints.WSCSettings;
import cpw.mods.fml.common.FMLCommonHandler;

public class WSCKeyHandler extends InputEventListener
{
    private static HashMap<KeyBinding, WSCKeyHandler> instances = new HashMap();
    
    public WSCKeyHandler(KeyBinding keyBinding, boolean allowRepeats)
    {
        super(keyBinding, allowRepeats);
        instances.put(keyBinding, this);
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
    
    public static boolean isRegistered(KeyBinding kb)
    {
        return instances.containsKey(kb);
    }
    
    public static void unRegister(KeyBinding kb)
    {
        if (isRegistered(kb))
        {
            FMLCommonHandler.instance().bus().unregister(instances.get(kb));
            instances.remove(kb);
        }
    }
}
