package bspkrs.worldstatecheckpoints;

import java.util.Properties;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiConfigureAutoSave extends GuiScreen
{
    String                    guiTitle = "Configure Checkpoint Auto-Save";
    
    private CheckpointManager cpm;
    private GuiButton         back, save, enable, periodType;
    private GuiTextField      periodValue;
    private Properties        localConfig;
    
    public GuiConfigureAutoSave(CheckpointManager cpm)
    {
        this.cpm = cpm;
        localConfig.setProperty(cpm.ENABLED, cpm.autoSaveConfig.getProperty(cpm.ENABLED));
        localConfig.setProperty(cpm.AUTO_SAVE_PERIOD, cpm.autoSaveConfig.getProperty(cpm.AUTO_SAVE_PERIOD));
        localConfig.setProperty(cpm.PERIOD_UNIT, cpm.autoSaveConfig.getProperty(cpm.PERIOD_UNIT));
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        // cpm = new CheckpointManager(mc);
        
        controlList.clear();
        byte byte0 = -16;
        
        int row1, row2, row3, row4, row5, row6;
        row1 = height / 4 + 24 + byte0;
        row2 = height / 4 + 24 * 2 + byte0;
        row3 = height / 4 + 24 * 3 + byte0;
        row4 = height / 4 + 24 * 4 + byte0;
        row5 = height / 4 + 24 * 5 + byte0;
        row6 = height / 4 + 24 * 6 + byte0;
        
        enable = new GuiButton(-1, width / 2 - 100, row1, "Auto-Save Checkpoints: " + (cpm.autoSaveEnabled ? "On" : "Off"));
        periodValue = new GuiTextField(fontRenderer, width / 2 - 100, row2, 99, 20);
        periodValue.setText(localConfig.getProperty(cpm.AUTO_SAVE_PERIOD));
        periodType = new GuiButton(-2, width / 2 + 1, row2, 99, 20, localConfig.getProperty(cpm.PERIOD_UNIT));
        save = new GuiButton(-3, width / 2 - 62, row5, 60, 20, "Save");
        back = new GuiButton(-4, width / 2 + 2, row5, 60, 20, "Cancel");
        
        controlList.add(enable);
        controlList.add(periodValue);
        
        controlList.add(save);
        controlList.add(back);
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case -1:
                mc.displayGuiScreen(new GuiReplaceCheckpoint(cpm));
                break;
            
            case -2:
                mc.displayGuiScreen(new GuiSaveCheckpoint(cpm));
                break;
            
            case -3:
                // save config and go back
                break;
            
            case -4:
                // cancel changes and go back
                break;
            
            case -5:
                mc.displayGuiScreen(new GuiConfigureAutoSave(cpm));
                break;
        }
    }
    
    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = "0123456789";
        if (validChars.contains(String.valueOf(c)) || i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_HOME || i == Keyboard.KEY_END)
            periodValue.textboxKeyTyped(c, i);
        
        save.enabled = periodValue.getText().trim().length() > 0;
        
        if (c == '\r')
            actionPerformed((GuiButton) controlList.get(2));
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        periodValue.mouseClicked(par1, par2, par3);
    }
    
    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        periodValue.updateCursorCounter();
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        periodValue.drawTextBox();
        
        drawCenteredString(fontRenderer, guiTitle, width / 2, 80, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
    
}
