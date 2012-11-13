package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CheckpointManager;
import bspkrs.worldstatecheckpoints.GuiCheckpointsMenu;
import bspkrs.worldstatecheckpoints.GuiLoadCheckpoint;

public class mod_WorldStateCheckpoints extends BaseMod
{
    @MLProp(info = "Set to true to allow checking for mod updates, false to disable")
    public static boolean     allowUpdateCheck     = true;

    public static String      menuKeyStr           = "F6";
    public static String      saveKeyStr           = "F7";

    public static KeyBinding  menuKey              = new KeyBinding("CheckpointsMenu", Keyboard.getKeyIndex(menuKeyStr));
    public static KeyBinding  saveKey              = new KeyBinding("CheckpointsSave", Keyboard.getKeyIndex(saveKeyStr));
    public static boolean     justLoadedCheckpoint = false;
    public static boolean     justLoadedWorld      = false;
    public static String      loadMessage          = "";
    public CheckpointManager  cpm;

    private boolean           checkUpdate;
    private ModVersionChecker versionChecker;
    private String            versionURL           = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.4/worldStateCheckpoints.version";
    private String            mcfTopic             = "http://www.minecraftforum.net/topic/1548243-";

    private Minecraft         mc;

    @Override
    public String getName()
    {
        return "WorldStateCheckpoints";
    }

    @Override
    public String getVersion()
    {
        return "ML 1.4.4.r01";
    }

    public mod_WorldStateCheckpoints()
    {
        checkUpdate = allowUpdateCheck;
        versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic, ModLoader.getLogger());

        mc = ModLoader.getMinecraftInstance();

        /*
         * for (int i = 0; i < Keyboard.getKeyCount(); i++)
         * System.out.println("Key Index " + i + " : " +
         * Keyboard.getKeyName(i));
         */

    }

    @Override
    public void load()
    {
        versionChecker.checkVersionWithLogging();

        ModLoader.registerKey(this, menuKey, false);
        ModLoader.registerKey(this, saveKey, false);
        ModLoader.addLocalization(menuKey.keyDescription, "Checkpoints Menu");
        ModLoader.addLocalization(saveKey.keyDescription, "Checkpoints Quick Save");
        ModLoader.setInGameHook(this, true, true);
    }

    @Override
    public void clientConnect(NetClientHandler var1)
    {
        justLoadedWorld = true;
    }

    @Override
    public void clientDisconnect(NetClientHandler var1)
    {

    }

    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (checkUpdate)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            checkUpdate = false;
        }

        if (justLoadedWorld)
        {
            cpm = null;
            justLoadedWorld = false;
        }

        if (cpm == null)
        {
            cpm = new CheckpointManager(mc);
        }

        if (justLoadedCheckpoint)
        {
            mc.thePlayer.addChatMessage(loadMessage);
            loadMessage = "";

            if (cpm.autoSaveEnabled)
                cpm.tickCount = 0;

            justLoadedCheckpoint = false;
        }

        if (cpm.autoSaveEnabled)
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
        else if (event.equals(saveKey) && mc.isSingleplayer())
            cpm.setCheckpoint("", true);
    }
}
