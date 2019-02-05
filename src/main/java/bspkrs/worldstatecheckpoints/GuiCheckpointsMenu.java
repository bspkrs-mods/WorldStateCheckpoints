package bspkrs.worldstatecheckpoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

public class GuiCheckpointsMenu extends GuiScreen
{
    /** Counts the number of screen updates. */
    private int                     updateCounter;

    private final CheckpointManager cpm;

    public GuiCheckpointsMenu(CheckpointManager cpm)
    {
        this.cpm = cpm;
        updateCounter = 0;
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

        buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 * 5 + byte0, I18n.format("menu.returnToGame")));

        GuiButton save = new GuiButton(-2, width / 2 - 100, height / 4 + 24 + byte0, 99, 20, I18n.format("wsc.menu.saveNew"));
        GuiButton replace = new GuiButton(-3, width / 2 + 1, height / 4 + 24 + byte0, 99, 20, I18n.format("wsc.menu.overwrite"));
        GuiButton load = new GuiButton(-1, width / 2 - 100, height / 4 + 24 * 2 + byte0, 99, 20, I18n.format("wsc.menu.loadCheckpoint"));
        GuiButton loadAuto = new GuiButton(-4, width / 2 + 1, height / 4 + 24 * 2 + byte0, 99, 20, I18n.format("wsc.menu.loadAutoSave"));
        GuiButton configAuto = new GuiButton(-5, width / 2 - 100, height / 4 + 24 * 3 + byte0, I18n.format("wsc.menu.configureAutoSave") + ": " +
                (cpm.autoSaveEnabled ? I18n.format("options.on") : I18n.format("options.off")));

        if (!WSCSettings.mc.isSingleplayer())
            replace.enabled = load.enabled = save.enabled = loadAuto.enabled = configAuto.enabled = false;
        else
        {
            replace.enabled = load.enabled = cpm.getHasCheckpoints(false);
            loadAuto.enabled = cpm.getHasCheckpoints(true);
        }

        buttonList.add(load);
        buttonList.add(loadAuto);
        buttonList.add(save);
        buttonList.add(replace);
        buttonList.add(configAuto);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 4:
                WSCSettings.mc.displayGuiScreen(null);
                WSCSettings.mc.setIngameFocus();
                break;

            case -3:
                WSCSettings.mc.displayGuiScreen(new GuiReplaceCheckpoint(cpm));
                break;

            case -2:
                WSCSettings.mc.displayGuiScreen(new GuiSaveCheckpoint(cpm));
                break;

            case -1:
                WSCSettings.mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, false, false));
                break;

            case -4:
                WSCSettings.mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, false, true));
                break;

            case -5:
                WSCSettings.mc.displayGuiScreen(new GuiConfigureAutoSave(cpm));
                break;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        updateCounter++;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();

        if (updateCounter < 20)
        {
            float f = ((updateCounter % 10) + par3) / 10F;
            f = MathHelper.sin(f * (float) Math.PI * 2.0F) * 0.2F + 0.8F;
            int i = (int) (255F * f);
            drawString(fontRenderer, I18n.format("wsc.menu.savingLevel") + "..", 8, height - 16, i << 16 | i << 8 | i);
        }

        drawCenteredString(fontRenderer, "World State Checkpoints", width / 2, 40, 0xffffff);

        float f = ((updateCounter % 60) + par3) / 60F;
        f = MathHelper.sin(f * (float) Math.PI * 2.0F) * 0.2F + 0.8F;
        int r = (int) (0xFF * f);
        int g = (int) (0xBA * f);
        int b = (int) (0x00 * f);

        drawCenteredString(fontRenderer, I18n.format("wsc.credit.MightyPork"), width / 2, height / 4 + 24 * 6 + 12 - 16, r << 16 | g << 8 | b);
        drawCenteredString(fontRenderer, I18n.format("wsc.credit.bspkrs"), width / 2, height / 4 + 24 * 7 - 16, r << 16 | g << 8 | b);

        super.drawScreen(par1, par2, par3);
    }
}
