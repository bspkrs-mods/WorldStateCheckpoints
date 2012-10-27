package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CheckpointManager;
import bspkrs.worldstatecheckpoints.GuiLoadCheckpoint;
import bspkrs.worldstatecheckpoints.GuiSaveCheckpoint;

public class mod_WorldStateCheckpoints extends BaseMod
{
    @MLProp(info="Set to true to allow checking for mod updates, false to disable")
    public static boolean allowUpdateCheck = true;
    
    public static KeyBinding saveKey = new KeyBinding("SaveCheckpoint", Keyboard.KEY_F6);
    public static KeyBinding loadKey = new KeyBinding("LoadCheckpoint", Keyboard.KEY_F7);
    
    private boolean checkUpdate;
    private ModVersionChecker versionChecker;
    private String versionURL = "https://dl.dropbox.com/u/20748481/Minecraft/1.3.1/worldStateCheckpoints.version";
    private String mcfTopic = "http://www.minecraftforum.net/topic/???-";
    
    private Minecraft mc;
    private CheckpointManager cpm;
    
    @Override
    public String getName()
    {
        return "WorldStateCheckpoints";
    }

    @Override
    public String getVersion()
    {
        return "ML 1.3.2.r01";
    }
    
    public mod_WorldStateCheckpoints()
    {
        checkUpdate = allowUpdateCheck;
        versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic, ModLoader.getLogger());
        
        mc = ModLoader.getMinecraftInstance();
    }

    @Override
    public void load()
    {
        versionChecker.checkVersionWithLogging();
        
        ModLoader.registerKey(this, saveKey, false);
        ModLoader.registerKey(this, loadKey, false);
        ModLoader.addLocalization(saveKey.keyDescription, "Save Checkpoint");
        ModLoader.addLocalization(loadKey.keyDescription, "Load Checkpoint");
        ModLoader.setInGameHook(this, true, false);
    }

    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if(checkUpdate)
        {
            if(!versionChecker.isCurrentVersion())
                for(String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            checkUpdate = false;
        }
        
        if(cpm == null)
            cpm = new CheckpointManager(mc);
        
        return true;
    }

    @Override
    public void clientConnect(NetClientHandler nch) 
    {
        cpm = new CheckpointManager(mc);        
    }

    /*@Override
    public void clientDisconnect(NetClientHandler nch) 
    {
        cpm = null;
    }*/
    
    @Override
    public void keyboardEvent(KeyBinding event)
    {
        if(event.equals(loadKey))
        {
            if(cpm != null && cpm.getHasCheckpoints())
                mc.displayGuiScreen(new GuiLoadCheckpoint(mc.currentScreen instanceof GuiGameOver));
        }
        else if(event.equals(saveKey))
        {
            mc.displayGuiScreen(new GuiSaveCheckpoint());
        }
    }
}
