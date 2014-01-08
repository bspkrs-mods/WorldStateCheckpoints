package bspkrs.worldstatecheckpoints.fml;


public class CommonProxy
{
    public void registerEventHandlers()
    {
        if (!WSCServerTicker.isRegistered())
            WorldStateCheckpointsMod.instance.ticker = new WSCServerTicker();
    }
    
    public void registerClientTicker()
    {}
}
