package bspkrs.worldstatecheckpoints;

import net.minecraft.src.GuiScreen;

public class GuiConfigureAutoSave extends GuiScreen
{
    String guiTitle    = "Configure Checkpoint Auto-Save";
    
    private CheckpointManager cpm;
    
    public GuiConfigureAutoSave(CheckpointManager cpm)
    {
        this.cpm = cpm;
    }
    
}
