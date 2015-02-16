package bspkrs.worldstatecheckpoints.fml;

import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class WSCServerTicker
{
    private static boolean isRegistered = false;

    public WSCServerTicker()
    {
        FMLCommonHandler.instance().bus().register(this);
        isRegistered = true;
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        if (!WSCSettings.onGameTick())
            unregister();
    }

    public void unregister()
    {
        FMLCommonHandler.instance().bus().unregister(this);
        isRegistered = false;
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
