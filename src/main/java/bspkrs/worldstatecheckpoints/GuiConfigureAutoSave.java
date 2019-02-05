package bspkrs.worldstatecheckpoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Properties;

import static bspkrs.worldstatecheckpoints.CheckpointManager.*;

public class GuiConfigureAutoSave extends GuiScreen
{
    String                          guiTitle           = I18n.format("wsc.configureAutoSave.title");
    String[]                        maxAutoSaves       = { I18n.format("wsc.configureAutoSave.maxAutoSavesToKeep"),
                                                       I18n.format("wsc.configureAutoSave.use0ForNoLimit") };
    String                          enableAutosaveText = I18n.format("wsc.configureAutosave.enableAutoSave") + ": ";
    private final CheckpointManager cpm;
    private GuiButton               back, save, enable, periodUnit;
    private GuiTextField            periodValue;
    private GuiTextField            maxToKeep;
    private final Properties        localConfig;

    public GuiConfigureAutoSave(CheckpointManager cpm)
    {
        this.cpm = cpm;
        localConfig = new Properties();
        localConfig.setProperty(ENABLED, cpm.autoSaveConfig.getProperty(ENABLED));
        localConfig.setProperty(MAX_AUTO_SAVES_TO_KEEP, cpm.autoSaveConfig.getProperty(MAX_AUTO_SAVES_TO_KEEP));
        localConfig.setProperty(AUTO_SAVE_PERIOD, cpm.autoSaveConfig.getProperty(AUTO_SAVE_PERIOD));
        localConfig.setProperty(PERIOD_UNIT, cpm.autoSaveConfig.getProperty(PERIOD_UNIT));
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        buttonList.clear();
        byte byte0 = -16;

        int row1, row2, row3, row5;
        row1 = (height / 4) + 24 + byte0;
        row2 = (height / 4) + (24 * 2) + byte0;
        row3 = (height / 4) + (24 * 3) + byte0;
        row5 = (height / 4) + (24 * 5) + byte0;

        enable = new GuiButton(-1, (width / 2) - 100, row1, enableAutosaveText + (cpm.autoSaveEnabled ? I18n.format("options.on") : I18n.format("options.off")));
        periodValue = new GuiTextField(1, fontRenderer, (width / 2) - 62, row2, 60, 20);
        periodValue.setText(localConfig.getProperty(AUTO_SAVE_PERIOD));
        maxToKeep = new GuiTextField(2, fontRenderer, (width / 2) + 2, row3, 60, 20);
        maxToKeep.setText(localConfig.getProperty(MAX_AUTO_SAVES_TO_KEEP));
        periodUnit = new GuiButton(-2, (width / 2) + 2, row2, 60, 20, I18n.format("wsc.configureAutosave.period." + localConfig.getProperty(PERIOD_UNIT)));
        periodUnit.enabled = cpm.autoSaveEnabled;
        save = new GuiButton(-3, (width / 2) - 62, row5, 60, 20, I18n.format("wsc.saveCheckpoint.save"));
        back = new GuiButton(-4, (width / 2) + 2, row5, 60, 20, I18n.format("gui.cancel"));

        buttonList.add(enable);
        buttonList.add(periodUnit);
        buttonList.add(save);
        buttonList.add(back);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case -1:
                if (localConfig.getProperty(ENABLED).equalsIgnoreCase("on"))
                {
                    localConfig.setProperty(ENABLED, "off");
                    enable.displayString = enableAutosaveText + I18n.format("options.off");
                    periodUnit.enabled = false;
                }
                else
                {
                    localConfig.setProperty(ENABLED, "on");
                    enable.displayString = enableAutosaveText + I18n.format("options.on");
                    periodUnit.enabled = true;
                }
                break;

            case -2:
                if (localConfig.getProperty(PERIOD_UNIT).equalsIgnoreCase(UNIT_HOURS))
                    localConfig.setProperty(PERIOD_UNIT, UNIT_MINUTES);
                else if (localConfig.getProperty(PERIOD_UNIT).equalsIgnoreCase(UNIT_MINUTES))
                    localConfig.setProperty(PERIOD_UNIT, UNIT_SECONDS);
                else if (localConfig.getProperty(PERIOD_UNIT).equalsIgnoreCase(UNIT_SECONDS))
                    localConfig.setProperty(PERIOD_UNIT, UNIT_HOURS);

                periodUnit.displayString = I18n.format("wsc.configureAutosave.period." + localConfig.getProperty(PERIOD_UNIT));
                break;

            case -3:
                cpm.autoSaveConfig.setProperty(ENABLED, localConfig.getProperty(ENABLED));
                cpm.autoSaveConfig.setProperty(AUTO_SAVE_PERIOD, localConfig.getProperty(AUTO_SAVE_PERIOD));
                cpm.autoSaveConfig.setProperty(PERIOD_UNIT, localConfig.getProperty(PERIOD_UNIT));
                cpm.autoSaveConfig.setProperty(MAX_AUTO_SAVES_TO_KEEP, localConfig.getProperty(MAX_AUTO_SAVES_TO_KEEP));
                cpm.saveAutoConfig(cpm.autoSaveConfig);
                cpm.loadAutoConfig();
                WSCSettings.mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
                break;

            case -4:
                WSCSettings.mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
                break;
        }
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = "0123456789";
        if (validChars.contains(String.valueOf(c)) || (i == Keyboard.KEY_BACK) || (i == Keyboard.KEY_DELETE) || (i == Keyboard.KEY_LEFT) || (i == Keyboard.KEY_RIGHT) || (i == Keyboard.KEY_HOME) || (i == Keyboard.KEY_END))
            if (maxToKeep.isFocused())
                maxToKeep.textboxKeyTyped(c, i);
            else if (periodValue.isFocused())
                periodValue.textboxKeyTyped(c, i);

        save.enabled = (periodValue.getText().trim().length() > 0) && (Integer.valueOf(periodValue.getText().trim()) > 0)
                && (maxToKeep.getText().trim().length() > 0) && (Integer.valueOf(maxToKeep.getText().trim()) >= 0);

        localConfig.setProperty(AUTO_SAVE_PERIOD, periodValue.getText().trim());
        localConfig.setProperty(MAX_AUTO_SAVES_TO_KEEP, maxToKeep.getText().trim());

        if ((c == '\r') && save.enabled)
            actionPerformed(save);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException
    {
        super.mouseClicked(par1, par2, par3);
        periodValue.mouseClicked(par1, par2, par3);
        maxToKeep.mouseClicked(par1, par2, par3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        periodValue.updateCursorCounter();
        maxToKeep.updateCursorCounter();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        periodValue.drawTextBox();
        maxToKeep.drawTextBox();

        drawCenteredString(fontRenderer, guiTitle, width / 2, 80, 0xffffff);
        drawString(fontRenderer, maxAutoSaves[0], (width / 2) - 3 - fontRenderer.getStringWidth(maxAutoSaves[0]), (((height / 4) + (24 * 3)) - 16) + 1, 0xffffff);
        drawString(fontRenderer, maxAutoSaves[1], (width / 2) - 3 - fontRenderer.getStringWidth(maxAutoSaves[1]), (((height / 4) + (24 * 3)) - 16) + 11, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
