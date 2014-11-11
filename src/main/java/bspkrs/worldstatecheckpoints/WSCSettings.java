package bspkrs.worldstatecheckpoints;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.input.Keyboard;

import bspkrs.util.CommonUtils;
import bspkrs.worldstatecheckpoints.fml.Reference;

public class WSCSettings
{
    private final static String     autoSaveEnabledDefaultDefault    = "off";
    public static String            autoSaveEnabledDefault           = autoSaveEnabledDefaultDefault;
    private final static int        maxAutoSavesToKeepDefaultDefault = 10;
    public static int               maxAutoSavesToKeepDefault        = maxAutoSavesToKeepDefaultDefault;
    private final static int        autoSavePeriodDefaultDefault     = 20;
    public static int               autoSavePeriodDefault            = autoSavePeriodDefaultDefault;
    private final static String     periodUnitDefaultDefault         = CheckpointManager.UNIT_MINUTES;
    public static String            periodUnitDefault                = periodUnitDefaultDefault;

    public static KeyBinding        bindKey                          = new KeyBinding("worldstatecheckpoints.keybind.desc", Keyboard.KEY_F6, "key.categories.misc");
    public static boolean           justLoadedCheckpoint             = false;
    public static boolean           justLoadedWorld                  = false;
    public static String            loadMessage                      = "";
    public static CheckpointManager cpm;

    protected static int            delayedLoaderTicks               = 0;
    protected static String         delayedLoadCheckpointName        = "";
    protected static boolean        isDelayedLoadAutoSave            = false;

    public static boolean           allowDebugLogging                = false;

    public final static Minecraft   mc                               = Minecraft.getMinecraft();

    public static void initConfig(File file)
    {
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }

        Reference.config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig()
    {
        final String ctgyGen = Reference.CTGY;

        Reference.config.load();

        autoSaveEnabledDefault = Reference.config.getString("autoSaveEnabledDefault", ctgyGen, autoSaveEnabledDefaultDefault,
                "Default enabled state of auto-saving when starting a new world.  Valid values are on/off.",
                new String[] { "off", "on" }, "wsc.configureAutosave.enableAutoSave");
        maxAutoSavesToKeepDefault = Reference.config.getInt("maxAutoSavesToKeepDefault", ctgyGen, maxAutoSavesToKeepDefaultDefault, 0, 100,
                "Default maximum number of auto-saves to keep per world. This value is used when starting a new world. Use 0 for no limit.",
                "wsc.configureAutoSave.maxAutoSavesToKeep");
        autoSavePeriodDefault = Reference.config.getInt("autoSavePeriodDefault", ctgyGen, autoSavePeriodDefaultDefault, 1, Integer.MAX_VALUE,
                "Default auto-save period to use in a new world's auto-save config.");
        periodUnitDefault = Reference.config.getString("periodUnitDefault", ctgyGen, periodUnitDefaultDefault,
                "Default auto-save period unit to use in a new world's auto-save config.  Valid values are " +
                        CheckpointManager.UNIT_HOURS + "/" + CheckpointManager.UNIT_MINUTES + "/" + CheckpointManager.UNIT_SECONDS + ".");

        Reference.config.save();
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
                mc.thePlayer.addChatMessage(new ChatComponentText(loadMessage));
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
                mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, true, false));
            else if (mc.currentScreen instanceof GuiGameOver && cpm.getHasCheckpoints(true))
                mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, true, true));
            else
                mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
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
