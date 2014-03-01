package bspkrs.worldstatecheckpoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import bspkrs.helpers.client.MinecraftHelper;

public class GuiDeleteCheckpointYesNo extends GuiScreen
{
    private final CheckpointManager cpm;
    
    private final GuiLoadCheckpoint parentScreen;
    private final String            name, dirname;
    private final int               page;
    private final boolean           isAutoCheckpoint;
    
    public GuiDeleteCheckpointYesNo(CheckpointManager cpm, GuiLoadCheckpoint parent, String dirname, int page, boolean isAutoCheckpoint)
    {
        this.cpm = cpm;
        parentScreen = parent;
        this.page = page;
        this.dirname = dirname;
        name = dirname.split("!", 2)[1];
        this.isAutoCheckpoint = isAutoCheckpoint;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        
        byte byte0 = -16;
        
        buttonList.add(new GuiButton(-2, width / 2 - 62, height / 4 + 24 + 24 * 3 + byte0, 60, 20, StatCollector.translateToLocal("gui.yes")));
        buttonList.add(new GuiButton(-1, width / 2 + 2, height / 4 + 24 + 24 * 3 + byte0, 60, 20, StatCollector.translateToLocal("gui.no")));
    }
    
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }
        
        switch (par1GuiButton.id)
        {
            case -1:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, parentScreen);
                return;
                
            case -2:
                cpm.deleteCheckpoint(dirname, isAutoCheckpoint);
                
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, parentScreen);
                parentScreen.showPage(page);
                
                return;
        }
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        
        drawCenteredString(fontRendererObj, StatCollector.translateToLocal("wsc.deleteCheckpoint"), width / 2, 100, 0xffffff);
        drawCenteredString(fontRendererObj, "\"" + name + "\"", width / 2, 100 + 20, 0xffff00);
        super.drawScreen(par1, par2, par3);
    }
}
