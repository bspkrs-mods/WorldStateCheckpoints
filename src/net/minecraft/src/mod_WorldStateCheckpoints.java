package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import bspkrs.util.CommonUtils;
import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CheckpointManager;
import bspkrs.worldstatecheckpoints.GuiCheckpointsMenu;
import bspkrs.worldstatecheckpoints.GuiLoadCheckpoint;

public class mod_WorldStateCheckpoints extends BaseMod
{
    @MLProp(info = "Default enabled state of auto-saving when starting a new world.  Valid values are on/off.")
    public static String      autoSaveEnabledDefault    = "off";
    @MLProp(info = "Default maximum number of auto-saves to keep per world. This value is used when starting a new world. Use 0 for no limit.", min = 0)
    public static int         maxAutoSavesToKeepDefault = 10;
    @MLProp(info = "Default auto-save period to use in a new world's auto-save config.")
    public static int         autoSavePeriodDefault     = 20;
    @MLProp(info = "Default auto-save period unit to use in a new world's auto-save config.  Valid values are " +
            CheckpointManager.UNIT_HOURS + "/" + CheckpointManager.UNIT_MINUTES + "/" + CheckpointManager.UNIT_SECONDS + ".")
    public static String      periodUnitDefault         = CheckpointManager.UNIT_MINUTES;
    
    public static String      menuKeyStr                = "F6";
    public static String      saveKeyStr                = "F7";
    
    public static KeyBinding  menuKey                   = new KeyBinding("CheckpointsMenu", Keyboard.getKeyIndex(menuKeyStr));
    public static KeyBinding  saveKey                   = new KeyBinding("CheckpointsSave", Keyboard.getKeyIndex(saveKeyStr));
    public static boolean     justLoadedCheckpoint      = false;
    public static boolean     justLoadedWorld           = false;
    public static String      loadMessage               = "";
    public CheckpointManager  cpm;
    
    private ModVersionChecker versionChecker;
    private boolean           allowUpdateCheck;
    private final String      versionURL                = "http://192.210.150.163/Minecraft/1.5.1/worldStateCheckpoints.version";
    private final String      mcfTopic                  = "http://www.minecraftforum.net/topic/1548243-";
    
    private final Minecraft   mc;
    
    @Override
    public String getName()
    {
        return "WorldStateCheckpoints";
    }
    
    @Override
    public String getVersion()
    {
        return "ML 1.5.1.r01";
    }
    
    @Override
    public String getPriorities()
    {
        return "required-after:mod_bspkrsCore";
    }
    
    public mod_WorldStateCheckpoints()
    {
        allowUpdateCheck = mod_bspkrsCore.allowUpdateCheck;
        if (allowUpdateCheck)
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic);
        
        mc = ModLoader.getMinecraftInstance();
        
        /*
         * for (int i = 0; i < Keyboard.getKeyCount(); i++) System.out.println("Key Index " + i + " : " + Keyboard.getKeyName(i));
         */
    }
    
    @Override
    public void load()
    {
        if (allowUpdateCheck && versionChecker != null)
            versionChecker.checkVersionWithLogging();
        
        ModLoader.registerKey(this, menuKey, false);
        ModLoader.registerKey(this, saveKey, false);
        ModLoader.addLocalization(menuKey.keyDescription, "Checkpoints Menu");
        ModLoader.addLocalization(saveKey.keyDescription, "Checkpoints Quick Save");
    }
    
    @Override
    public void clientConnect(NetClientHandler var1)
    {
        if (mc.isSingleplayer())
        {
            justLoadedWorld = true;
            ModLoader.setInGameHook(this, true, true);
        }
    }
    
    @Override
    public void clientDisconnect(NetClientHandler var1)
    {
        if (mc.isSingleplayer())
        {
            ModLoader.setInGameHook(this, false, true);
            cpm = null;
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
        
        if (justLoadedWorld)
        {
            cpm = null;
            justLoadedWorld = false;
        }
        
        if (cpm == null)
            cpm = new CheckpointManager(mc);
        
        if (justLoadedCheckpoint)
        {
            mc.thePlayer.addChatMessage(loadMessage);
            loadMessage = "";
            
            if (cpm.autoSaveEnabled)
                cpm.tickCount = 0;
            
            justLoadedCheckpoint = false;
        }
        
        if (cpm.autoSaveEnabled &&
                (mc.currentScreen == null || !(mc.currentScreen instanceof GuiGameOver || CommonUtils.isGamePaused(mc))))
            cpm.incrementTickCount();
        
        return true;
    }
    
    @Override
    public void keyboardEvent(KeyBinding event)
    {
        if (event.equals(menuKey) && mc.isSingleplayer())
        {
            if (mc.currentScreen instanceof GuiGameOver && cpm.getHasCheckpoints(false))
                mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, true, false));
            else if (mc.currentScreen instanceof GuiGameOver && cpm.getHasCheckpoints(true))
                mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, true, true));
            else
                mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
        }
        else if (event.equals(saveKey) && mc.isSingleplayer() && !(mc.currentScreen instanceof GuiGameOver) &&
                !(mc.currentScreen instanceof GuiIngameMenu))
        {
            cpm.tickCount = 0;
            cpm.setCheckpoint("", true);
        }
    }
}
