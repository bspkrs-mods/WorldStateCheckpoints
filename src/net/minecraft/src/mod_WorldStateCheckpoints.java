package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.settings.KeyBinding;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CommandWSC;
import bspkrs.worldstatecheckpoints.WSCSettings;

public class mod_WorldStateCheckpoints extends BaseMod
{
    private ModVersionChecker versionChecker;
    private boolean           allowUpdateCheck;
    private final String      versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/worldStateCheckpoints.version";
    private final String      mcfTopic   = "http://www.minecraftforum.net/topic/1548243-";
    
    private boolean           enabled    = false;
    
    @Override
    public String getName()
    {
        return "WorldStateCheckpoints";
    }
    
    @Override
    public String getVersion()
    {
        return "ML " + WSCSettings.VERSION_NUMBER;
    }
    
    @Override
    public String getPriorities()
    {
        return "required-after:mod_bspkrsCore";
    }
    
    public mod_WorldStateCheckpoints()
    {
        try
        {
            Class.forName("bspkrs.worldstatecheckpoints.fml.WorldStateCheckpointsMod");
        }
        catch (ClassNotFoundException e)
        {
            enabled = true;
        }
        
        allowUpdateCheck = mod_bspkrsCore.allowUpdateCheck;
        if (allowUpdateCheck && enabled)
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic);
    }
    
    @Override
    public void load()
    {
        if (enabled)
        {
            if (allowUpdateCheck && versionChecker != null)
                versionChecker.checkVersionWithLogging();
            
            ModLoader.registerKey(this, WSCSettings.bindKey, false);
            ModLoader.addCommand(new CommandWSC());
        }
    }
    
    @Override
    public void clientConnect(NetClientHandler var1)
    {
        if (WSCSettings.mc.isSingleplayer() && enabled)
        {
            WSCSettings.justLoadedWorld = true;
            ModLoader.setInGameHook(this, true, true);
        }
    }
    
    @Override
    public void clientDisconnect(NetClientHandler var1)
    {
        if (WSCSettings.mc.isSingleplayer() && enabled)
        {
            ModLoader.setInGameHook(this, false, true);
            WSCSettings.cpm = null;
        }
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (allowUpdateCheck && versionChecker != null)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            allowUpdateCheck = false;
        }
        
        return WSCSettings.onGameTick();
    }
    
    @Override
    public void keyboardEvent(KeyBinding event)
    {
        if (enabled)
            WSCSettings.keyboardEvent(event);
    }
}
