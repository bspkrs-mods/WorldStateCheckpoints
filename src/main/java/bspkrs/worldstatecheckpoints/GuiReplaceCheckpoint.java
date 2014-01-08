package bspkrs.worldstatecheckpoints;

import net.minecraft.util.StatCollector;
import bspkrs.helpers.client.MinecraftHelper;

public class GuiReplaceCheckpoint extends GuiLoadCheckpoint
{
    public GuiReplaceCheckpoint(CheckpointManager cpm)
    {
        this.cpm = cpm;
        guiTitle = StatCollector.translateToLocal("wsc.overwriteCheckpoint.title");
        guiSubTitle = StatCollector.translateToLocal("wsc.overwriteCheckpoint.title2");
    }
    
    public GuiReplaceCheckpoint(CheckpointManager cpm, int page)
    {
        this(cpm);
        startPage = page;
        isAutoCheckpointsLoad = false;
    }
    
    @Override
    protected void checkpointButtonClicked(int index)
    {
        String dirname = dirNames[index];
        MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiReplaceCheckpointChangeName(cpm, dirname, currentPage));
    }
    
    @Override
    protected void backButtonClicked()
    {
        MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiCheckpointsMenu(cpm));
    }
}
