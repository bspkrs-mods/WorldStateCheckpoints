package bspkrs.worldstatecheckpoints.fml;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CommandWSC;
import bspkrs.worldstatecheckpoints.WSCSettings;
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

@Mod(name = "WorldStateCheckpoints", modid = "WorldStateCheckpoints", version = WSCSettings.VERSION_NUMBER, dependencies = "required-after:bspkrsCore", useMetadata = true)
public class WorldStateCheckpointsMod
{
    public ModVersionChecker               versionChecker;
    private String                         versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/worldStateCheckpointsForge.version";
    private String                         mcfTopic   = "http://www.minecraftforum.net/topic/1548243-";
    
    @SidedProxy(clientSide = "bspkrs.worldstatecheckpoints.fml.ClientProxy", serverSide = "bspkrs.worldstatecheckpoints.fml.CommonProxy")
    public static CommonProxy              proxy;
    
    @Metadata(value = "WorldStateCheckpoints")
    public static ModMetadata              metadata;
    
    @Instance(value = "WorldStateCheckpoints")
    public static WorldStateCheckpointsMod instance;
    
    protected WSCServerTicker              ticker;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        WSCSettings.loadConfig(event.getSuggestedConfigurationFile());
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
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
}
