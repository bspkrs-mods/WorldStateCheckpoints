package bspkrs.worldstatecheckpoints.fml;

import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerClientTicker()
    {
        if (!WSCClientTicker.isRegistered())
            FMLCommonHandler.instance().bus().register(new WSCClientTicker());

        if (!WSCKeyHandler.isRegistered(WSCSettings.bindKey))
            FMLCommonHandler.instance().bus().register(new WSCKeyHandler(WSCSettings.bindKey, false));
    }
}
