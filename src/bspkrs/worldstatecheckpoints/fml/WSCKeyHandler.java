package bspkrs.worldstatecheckpoints.fml;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import bspkrs.worldstatecheckpoints.WSCSettings;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;

public class WSCKeyHandler extends KeyBindingRegistry.KeyHandler
{
    public WSCKeyHandler(KeyBinding[] keyBindings, boolean[] repeatings)
    {
        super(keyBindings, repeatings);
    }
    
    @Override
    public String getLabel()
    {
        return "WSCKeyHandler";
    }
    
    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
        for (TickType tt : types)
            if (!isRepeat)
                WSCSettings.keyboardEvent(kb);
    }
    
    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
    {   
        
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }
    
}
