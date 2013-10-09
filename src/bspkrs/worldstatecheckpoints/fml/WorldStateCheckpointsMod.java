package bspkrs.worldstatecheckpoints.fml;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import bspkrs.fml.util.bspkrsCoreProxy;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.worldstatecheckpoints.CommandWSC;
import bspkrs.worldstatecheckpoints.WSCSettings;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(name = "WorldStateCheckpoints", modid = "WorldStateCheckpoints", version = "Forge " + WSCSettings.VERSION_NUMBER, dependencies = "required-after:mod_bspkrsCore", useMetadata = true)
@NetworkMod(connectionHandler = WorldStateCheckpointsMod.class)
public class WorldStateCheckpointsMod implements IConnectionHandler
{
    public static ModVersionChecker        versionChecker;
    private String                         versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/worldStateCheckpointsForge.version";
    private String                         mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @Metadata(value = "WorldStateCheckpoints")
    public static ModMetadata              metadata;
    
    @Instance(value = "WorldStateCheckpoints")
    public static WorldStateCheckpointsMod instance;
    
    private static WSCTicker               ticker;
    
    public WorldStateCheckpointsMod()
    {
        new bspkrsCoreProxy();
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        WSCSettings.loadConfig(event.getSuggestedConfigurationFile());
        
        if (bspkrsCoreProxy.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLoggingBySubStringAsFloat(metadata.version.length() - 1, metadata.version.length());
        }
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        KeyBinding[] keys = { WSCSettings.bindKey };
        boolean[] repeats = { false };
        KeyBindingRegistry.registerKeyBinding(new WSCKeyHandler(keys, repeats));
        
        ticker = new WSCTicker(EnumSet.noneOf(TickType.class));
        TickRegistry.registerTickHandler(ticker, Side.CLIENT);
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandWSC());
    }
    
    /**
     * 2) Called when a player logs into the server SERVER SIDE
     * 
     * @param player
     * @param netHandler
     * @param manager
     */
    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {   
        
    }
    
    /**
     * If you don't want the connection to continue, return a non-empty string here If you do, you can do other stuff here- note no FML
     * negotiation has occured yet though the client is verified as having FML installed
     * 
     * SERVER SIDE
     * 
     * @param netHandler
     * @param manager
     */
    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        return null;
    }
    
    /**
     * 1) Fired when a remote connection is opened CLIENT SIDE
     * 
     * @param netClientHandler
     * @param server
     * @param port
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
    {   
        
    }
    
    /**
     * 
     * 1) Fired when a local connection is opened
     * 
     * CLIENT SIDE
     * 
     * @param netClientHandler
     * @param servers
     */
    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {   
        
    }
    
    /**
     * 4) Fired when a connection closes
     * 
     * ALL SIDES
     * 
     * @param manager
     */
    @Override
    public void connectionClosed(INetworkManager manager)
    {
        ticker.removeTicks(EnumSet.of(TickType.CLIENT));
        WSCSettings.cpm = null;
    }
    
    /**
     * 3) Fired when the client established the connection to the server
     * 
     * CLIENT SIDE
     * 
     * @param clientHandler
     * @param manager
     * @param login
     */
    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        if (WSCSettings.mc.isSingleplayer())
        {
            WSCSettings.justLoadedWorld = true;
            ticker.addTicks(EnumSet.of(TickType.CLIENT));
        }
    }
}
