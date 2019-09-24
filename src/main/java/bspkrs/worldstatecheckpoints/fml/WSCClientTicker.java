package bspkrs.worldstatecheckpoints.fml;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WSCClientTicker
{
    private static boolean isRegistered = false;

    public WSCClientTicker()
    {
        isRegistered = true;
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        boolean keepTicking = !(WSCSettings.mc != null && WSCSettings.mc.player != null && WSCSettings.mc.world != null);

        if (!keepTicking && isRegistered)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && WorldStateCheckpointsMod.instance.versionChecker != null)
                if (!WorldStateCheckpointsMod.instance.versionChecker.isCurrentVersion())
                    for (String msg : WorldStateCheckpointsMod.instance.versionChecker.getInGameMessage())
                        WSCSettings.mc.player.sendMessage(new TextComponentString(msg));

            MinecraftForge.EVENT_BUS.unregister(this);
            isRegistered = false;
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
