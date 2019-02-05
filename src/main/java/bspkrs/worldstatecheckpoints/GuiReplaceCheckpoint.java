package bspkrs.worldstatecheckpoints;

import net.minecraft.client.resources.I18n;

public class GuiReplaceCheckpoint extends GuiLoadCheckpoint
{
    public GuiReplaceCheckpoint(CheckpointManager cpm)
    {
        this.cpm = cpm;
        guiTitle = I18n.format("wsc.overwriteCheckpoint.title");
        guiSubTitle = I18n.format("wsc.overwriteCheckpoint.title2");
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
        WSCSettings.mc.displayGuiScreen(new GuiReplaceCheckpointChangeName(cpm, dirname, currentPage));
    }

    @Override
    protected void backButtonClicked()
    {
        WSCSettings.mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
    }
}
