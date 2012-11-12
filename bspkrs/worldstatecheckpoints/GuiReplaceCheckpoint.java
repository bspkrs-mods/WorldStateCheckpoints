package bspkrs.worldstatecheckpoints;

public class GuiReplaceCheckpoint extends GuiLoadCheckpoint
{
    CheckpointManager cpm;
    
    public GuiReplaceCheckpoint(CheckpointManager cpm)
    {
        this.cpm = cpm;
        guiTitle = "Overwrite Checkpoint";
        guiSubTitle = "Old data will be lost.";
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
