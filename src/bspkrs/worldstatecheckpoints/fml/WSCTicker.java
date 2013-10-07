package bspkrs.worldstatecheckpoints.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.worldstatecheckpoints.WSCSettings;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WSCTicker implements ITickHandler
{
    private Minecraft         mcClient;
    
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    
    public WSCTicker(EnumSet<TickType> tickTypes)
    {
        this.tickTypes = tickTypes;
        mcClient = FMLClientHandler.instance().getClient();
    }
    
    public void addTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (!tickTypes.contains(tt))
                tickTypes.add(tt);
    }
    
    public void removeTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (tickTypes.contains(tt))
                tickTypes.remove(tt);
    }
    
    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, true);
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, false);
    }
    
    private void tick(EnumSet<TickType> tickTypes, boolean isStart)
    {
        for (TickType tickType : tickTypes)
        {
            if (!onTick(tickType, isStart))
            {
                tickTypes.remove(tickType);
                tickTypes.removeAll(tickType.partnerTicks());
            }
        }
    }
    
    public boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        if (mcClient != null && mcClient.thePlayer != null)
        {
            if (bspkrsCoreProxy.instance.allowUpdateCheck && WorldStateCheckpointsMod.versionChecker != null)
                if (!WorldStateCheckpointsMod.versionChecker.isCurrentVersionBySubStringAsFloatNewer(WorldStateCheckpointsMod.metadata.version.length() - 2, WorldStateCheckpointsMod.metadata.version.length()))
                    for (String msg : WorldStateCheckpointsMod.versionChecker.getInGameMessage())
                        mcClient.thePlayer.addChatMessage(msg);
        }
        
        return WSCSettings.onGameTick();
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return tickTypes;
    }
    
    @Override
    public String getLabel()
    {
        return "WSCTicker";
    }
    
}
