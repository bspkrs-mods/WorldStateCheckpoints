package bspkrs.worldstatecheckpoints;

public class GuiReplaceCheckpoint extends GuiLoadCheckpoint
{    
    public GuiReplaceCheckpoint()
    {
        guiTitle = "Overwrite Checkpoint";
        guiSubTitle = "Old data will be lost.";
    }
    
    public GuiReplaceCheckpoint(int page)
    {
        this();
        startPage = page;
    }

    @Override
    protected void checkpointButtonClicked(int index)
    {
        String dirname = dirNames[index];   
        mc.displayGuiScreen(new GuiReplaceCheckpointChangeName(dirname, currentPage));
    }

    @Override
    protected void backButtonClicked()
    {
        mc.displayGuiScreen(new GuiIngameMenu()); 
    }
}
