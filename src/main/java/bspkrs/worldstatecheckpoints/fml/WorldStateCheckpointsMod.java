package bspkrs.worldstatecheckpoints.fml;

import bspkrs.util.Const;
import bspkrs.worldstatecheckpoints.CommandWSC;
import bspkrs.worldstatecheckpoints.WSCSettings;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY, updateJSON = Const.VERSION_URL_BASE + Reference.MODID + Const.VERSION_URL_EXT)
public class WorldStateCheckpointsMod
{
    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy              proxy;

    @Metadata(value = Reference.MODID)
    public static ModMetadata              metadata;

    @Instance(value = Reference.MODID)
    public static WorldStateCheckpointsMod instance;

    protected WSCServerTicker              ticker;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        WSCSettings.initConfig(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerClientTicker();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandWSC());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        WSCSettings.justLoadedWorld = true;
        WorldStateCheckpointsMod.proxy.registerEventHandlers();
    }

    public void serverStopping(FMLServerStoppingEvent event)
    {
        if (ticker != null)
        {
            ticker.unregister();
            ticker = null;
        }

        WSCSettings.cpm = null;
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MODID))
        {
            Reference.config.save();
            WSCSettings.syncConfig();
        }
    }
}
