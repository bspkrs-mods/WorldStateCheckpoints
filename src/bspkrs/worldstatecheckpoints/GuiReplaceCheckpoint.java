package bspkrs.worldstatecheckpoints;

import net.minecraft.util.StatCollector;

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
        mc.displayGuiScreen(new GuiReplaceCheckpointChangeName(cpm, dirname, currentPage));
    }
    
    @Override
    protected void backButtonClicked()
    {
        mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
    }
}
