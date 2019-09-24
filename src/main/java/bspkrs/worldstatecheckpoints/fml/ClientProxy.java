package bspkrs.worldstatecheckpoints.fml;

import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerClientTicker()
    {
        if (!WSCClientTicker.isRegistered())
            MinecraftForge.EVENT_BUS.register(new WSCClientTicker());

        if (!WSCKeyHandler.isRegistered(WSCSettings.bindKey))
            MinecraftForge.EVENT_BUS.register(new WSCKeyHandler(WSCSettings.bindKey, false));
    }
}
