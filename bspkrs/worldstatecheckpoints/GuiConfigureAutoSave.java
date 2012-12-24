package bspkrs.worldstatecheckpoints;

import java.util.Properties;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiConfigureAutoSave extends GuiScreen
{
    String                          guiTitle    = "Configure Checkpoint Auto-Save";
    final static String             ENABLE_TEXT = "Auto-Save Checkpoints: ";
    private final CheckpointManager cpm;
    private GuiButton               back, save, enable, periodUnit;
    private GuiTextField            periodValue;
    private final Properties        localConfig;
    
    public GuiConfigureAutoSave(CheckpointManager cpm)
    {
        this.cpm = cpm;
        localConfig = new Properties();
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
        controlList.clear();
        byte byte0 = -16;
        
        int row1, row2, row4;
        row1 = height / 4 + 24 + byte0;
        row2 = height / 4 + 24 * 2 + byte0;
        row4 = height / 4 + 24 * 4 + byte0;
        
        enable = new GuiButton(-1, width / 2 - 100, row1, ENABLE_TEXT + (cpm.autoSaveEnabled ? "On" : "Off"));
        periodValue = new GuiTextField(fontRenderer, width / 2 - 62, row2, 60, 20);
        periodValue.setText(localConfig.getProperty(cpm.AUTO_SAVE_PERIOD));
        periodUnit = new GuiButton(-2, width / 2 + 2, row2, 60, 20, localConfig.getProperty(cpm.PERIOD_UNIT));
        periodUnit.enabled = cpm.autoSaveEnabled;
        save = new GuiButton(-3, width / 2 - 62, row4, 60, 20, "Save");
        back = new GuiButton(-4, width / 2 + 2, row4, 60, 20, "Cancel");
        
        controlList.add(enable);
        controlList.add(periodUnit);
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
                if (localConfig.getProperty(cpm.ENABLED).equalsIgnoreCase("on"))
                {
                    localConfig.setProperty(cpm.ENABLED, "off");
                    enable.displayString = ENABLE_TEXT + "Off";
                    periodUnit.enabled = false;
                }
                else
                {
                    localConfig.setProperty(cpm.ENABLED, "on");
                    enable.displayString = ENABLE_TEXT + "On";
                    periodUnit.enabled = true;
                }
                break;
            
            case -2:
                if (localConfig.getProperty(cpm.PERIOD_UNIT).equalsIgnoreCase(cpm.UNIT_TICKS))
                {
                    localConfig.setProperty(cpm.PERIOD_UNIT, cpm.UNIT_HOURS);
                    periodUnit.displayString = cpm.UNIT_HOURS;
                }
                else if (localConfig.getProperty(cpm.PERIOD_UNIT).equalsIgnoreCase(cpm.UNIT_HOURS))
                {
                    localConfig.setProperty(cpm.PERIOD_UNIT, cpm.UNIT_MINUTES);
                    periodUnit.displayString = cpm.UNIT_MINUTES;
                }
                else if (localConfig.getProperty(cpm.PERIOD_UNIT).equalsIgnoreCase(cpm.UNIT_MINUTES))
                {
                    localConfig.setProperty(cpm.PERIOD_UNIT, cpm.UNIT_SECONDS);
                    periodUnit.displayString = cpm.UNIT_SECONDS;
                }
                else if (localConfig.getProperty(cpm.PERIOD_UNIT).equalsIgnoreCase(cpm.UNIT_SECONDS))
                {
                    localConfig.setProperty(cpm.PERIOD_UNIT, cpm.UNIT_TICKS);
                    periodUnit.displayString = cpm.UNIT_TICKS;
                }
                break;
            
            case -3:
                cpm.autoSaveConfig.setProperty(cpm.ENABLED, localConfig.getProperty(cpm.ENABLED));
                cpm.autoSaveConfig.setProperty(cpm.AUTO_SAVE_PERIOD, localConfig.getProperty(cpm.AUTO_SAVE_PERIOD));
                cpm.autoSaveConfig.setProperty(cpm.PERIOD_UNIT, localConfig.getProperty(cpm.PERIOD_UNIT));
                cpm.saveAutoConfig(cpm.autoSaveConfig);
                cpm.loadAutoConfig();
                mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
                break;
            
            case -4:
                mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
                break;
        }
    }
    
    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = "0123456789";
        if (validChars.contains(String.valueOf(c)) || i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_HOME || i == Keyboard.KEY_END)
            periodValue.textboxKeyTyped(c, i);
        
        save.enabled = periodValue.getText().trim().length() > 0 && Integer.valueOf(periodValue.getText().trim()) > 0;
        
        localConfig.setProperty(cpm.AUTO_SAVE_PERIOD, periodValue.getText().trim());
        
        if (c == '\r' && save.enabled)
            actionPerformed(save);
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
