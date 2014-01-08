package bspkrs.worldstatecheckpoints;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;

import org.lwjgl.input.Keyboard;

import bspkrs.helpers.client.MinecraftHelper;
import bspkrs.helpers.entity.player.EntityPlayerHelper;
import bspkrs.util.BSConfiguration;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;

public class WSCSettings
{
    public static final String      VERSION_NUMBER            = Const.MCVERSION + ".r01";
    
    public static String            autoSaveEnabledDefault    = "off";
    public static int               maxAutoSavesToKeepDefault = 10;
    public static int               autoSavePeriodDefault     = 20;
    public static String            periodUnitDefault         = CheckpointManager.UNIT_MINUTES;
    
    public static KeyBinding        bindKey                   = new KeyBinding("worldstatecheckpoints.keybind.desc", Keyboard.KEY_F6, "key.categories.misc");
    public static boolean           justLoadedCheckpoint      = false;
    public static boolean           justLoadedWorld           = false;
    public static String            loadMessage               = "";
    public static CheckpointManager cpm;
    
    protected static int            delayedLoaderTicks        = 0;
    protected static String         delayedLoadCheckpointName = "";
    protected static boolean        isDelayedLoadAutoSave     = false;
    
    public static BSConfiguration   config;
    public static boolean           allowDebugLogging         = false;
    
    public final static Minecraft   mc                        = Minecraft.getMinecraft();
    
    public static void loadConfig(File file)
    {
        final String ctgyGen = "autosave_new_world_defaults";
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new BSConfiguration(file);
        
        config.load();
        
        autoSaveEnabledDefault = config.getString("autoSaveEnabledDefault", ctgyGen, autoSaveEnabledDefault,
                "Default enabled state of auto-saving when starting a new world.  Valid values are on/off.");
        maxAutoSavesToKeepDefault = config.getInt("maxAutoSavesToKeepDefault", ctgyGen, maxAutoSavesToKeepDefault, 0, 100,
                "Default maximum number of auto-saves to keep per world. This value is used when starting a new world. Use 0 for no limit.");
        autoSavePeriodDefault = config.getInt("autoSavePeriodDefault", ctgyGen, autoSavePeriodDefault, 1, Integer.MAX_VALUE,
                "Default auto-save period to use in a new world's auto-save config.");
        periodUnitDefault = config.getString("periodUnitDefault", ctgyGen, periodUnitDefault,
                "Default auto-save period unit to use in a new world's auto-save config.  Valid values are " +
                        CheckpointManager.UNIT_HOURS + "/" + CheckpointManager.UNIT_MINUTES + "/" + CheckpointManager.UNIT_SECONDS + ".");
        
        config.save();
    }
    
    public static void delayedLoadCheckpoint(String checkpointName, boolean isAutoSave, int delayTicks)
    {
        delayedLoadCheckpointName = checkpointName;
        isDelayedLoadAutoSave = isAutoSave;
        delayedLoaderTicks = delayTicks;
    }
    
    public static boolean onGameTick()
    {
        if (mc.theWorld != null && mc.thePlayer != null)
        {
            if (justLoadedWorld)
            {
                cpm = null;
                justLoadedWorld = false;
            }
            
            if (cpm == null)
                cpm = new CheckpointManager(mc);
            
            if (justLoadedCheckpoint)
            {
                EntityPlayerHelper.addChatMessage(mc.thePlayer, new ChatComponentText(loadMessage));
                loadMessage = "";
                
                if (cpm.autoSaveEnabled)
                    cpm.tickCount = 0;
                
                justLoadedCheckpoint = false;
            }
            
            if (cpm.autoSaveEnabled &&
                    (mc.currentScreen == null || !(mc.currentScreen instanceof GuiGameOver || CommonUtils.isGamePaused(mc))))
                cpm.incrementTickCount();
            
            if (delayedLoaderTicks > 0)
            {
                if (--delayedLoaderTicks == 0)
                {
                    cpm.loadCheckpoint(delayedLoadCheckpointName, isDelayedLoadAutoSave);
                }
            }
        }
        
        return true;
    }
    
    public static void keyboardEvent(KeyBinding event)
    {
        if (event.equals(bindKey) && mc.isSingleplayer() && !(Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)))
        {
            if (mc.currentScreen instanceof GuiGameOver && cpm.getHasCheckpoints(false))
                MinecraftHelper.displayGuiScreen(mc, new GuiLoadCheckpoint(cpm, true, false));
            else if (mc.currentScreen instanceof GuiGameOver && cpm.getHasCheckpoints(true))
                MinecraftHelper.displayGuiScreen(mc, new GuiLoadCheckpoint(cpm, true, true));
            else
                MinecraftHelper.displayGuiScreen(mc, new GuiCheckpointsMenu(cpm));
        }
        else if (event.equals(bindKey) && mc.isSingleplayer() && !(mc.currentScreen instanceof GuiGameOver) &&
                !(mc.currentScreen instanceof GuiIngameMenu) && (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)))
        {
            if (!cpm.isSaving)
                cpm.tickCount = 0;
            
            cpm.setCheckpoint("", true);
        }
    }
}
