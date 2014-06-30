package bspkrs.worldstatecheckpoints.fml;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CommandWSC;
import bspkrs.worldstatecheckpoints.WSCSettings;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class WorldStateCheckpointsMod
{
    public ModVersionChecker               versionChecker;
    private String                         versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/worldStateCheckpointsForge.version";
    private String                         mcfTopic   = "http://www.minecraftforum.net/topic/1548243-";
    
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
        metadata = event.getModMetadata();
        WSCSettings.initConfig(event.getSuggestedConfigurationFile());
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerClientTicker();
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
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
