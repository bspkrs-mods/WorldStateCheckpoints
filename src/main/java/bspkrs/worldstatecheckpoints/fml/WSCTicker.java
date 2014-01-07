package bspkrs.worldstatecheckpoints.fml;

import java.util.EnumSet;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.worldstatecheckpoints.WSCSettings;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WSCTicker implements ITickHandler
{
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    private boolean           allowUpdateCheck;
    
    public WSCTicker(EnumSet<TickType> tickTypes)
    {
        this.tickTypes = tickTypes;
        allowUpdateCheck = bspkrsCoreMod.instance.allowUpdateCheck;
    }
    
    public void addTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (!this.tickTypes.contains(tt))
                this.tickTypes.add(tt);
    }
    
    public void removeTicks(EnumSet<TickType> tickTypes)
    {
        for (TickType tt : tickTypes)
            if (this.tickTypes.contains(tt))
                this.tickTypes.remove(tt);
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
        
        if (allowUpdateCheck && WSCSettings.mc != null && WSCSettings.mc.thePlayer != null)
        {
            if (bspkrsCoreMod.instance.allowUpdateCheck && WorldStateCheckpointsMod.versionChecker != null)
                if (!WorldStateCheckpointsMod.versionChecker.isCurrentVersion())
                    for (String msg : WorldStateCheckpointsMod.versionChecker.getInGameMessage())
                        WSCSettings.mc.thePlayer.addChatMessage(msg);
            
            allowUpdateCheck = false;
        }
        
        return WSCSettings.onGameTick();
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return this.tickTypes;
    }
    
    @Override
    public String getLabel()
    {
        return "WSCTicker";
    }
    
}
