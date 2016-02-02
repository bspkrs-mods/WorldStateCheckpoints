package bspkrs.worldstatecheckpoints.fml;

import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerClientTicker()
    {
        if (!WSCKeyHandler.isRegistered(WSCSettings.bindKey))
            MinecraftForge.EVENT_BUS.register(new WSCKeyHandler(WSCSettings.bindKey, false));
    }
}
