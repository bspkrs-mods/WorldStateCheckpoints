package bspkrs.worldstatecheckpoints;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StatCollector;

public class GuiCheckpointsMenu extends GuiScreen
{
    /** Also counts the number of updates, not certain as to why yet. */
    private int               updateCounter2;

    /** Counts the number of screen updates. */
    private int               updateCounter;

    private CheckpointManager chpmgr;

    public GuiCheckpointsMenu()
    {
        updateCounter2 = 0;
        updateCounter = 0;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {

        chpmgr = new CheckpointManager(mc);

        updateCounter2 = 0;
        controlList.clear();
        byte byte0 = -16;

        controlList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 * 5 + byte0, StatCollector.translateToLocal("menu.returnToGame")));

        GuiButton save = new GuiButton(-2, width / 2 - 100, height / 4 + 24 + byte0, "Save new");
        GuiButton replace = new GuiButton(-3, width / 2 - 100, height / 4 + 24 * 2 + byte0, "Overwrite");
        GuiButton load = new GuiButton(-1, width / 2 - 100, height / 4 + 24 * 3 + byte0, "Load from");

        if (!mc.isSingleplayer())
            replace.enabled = load.enabled = save.enabled = false;
        else
            replace.enabled = load.enabled = chpmgr.getHasCheckpoints(false);

        controlList.add(load);
        controlList.add(save);
        controlList.add(replace);
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
            case 2:
            case 3:
            default:
                break;

            case 4:
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
                break;

            case -3:
                mc.displayGuiScreen(new GuiReplaceCheckpoint());
                break;

            case -2:
                mc.displayGuiScreen(new GuiSaveCheckpoint());
                break;

            case -1:
                mc.displayGuiScreen(new GuiLoadCheckpoint(false));
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
            drawString(fontRenderer, "Saving level..", 8, height - 16, i << 16 | i << 8 | i);
        }

        drawCenteredString(fontRenderer, "World State Checkpoints", width / 2, 40, 0xffffff);

        float f = ((updateCounter % 60) + par3) / 60F;
        f = MathHelper.sin(f * (float) Math.PI * 2.0F) * 0.2F + 0.8F;
        int r = (int) (0xFF * f);
        int g = (int) (0xBA * f);
        int b = (int) (0x00 * f);

        drawCenteredString(fontRenderer, "Original by MightyPork!", width / 2, height / 4 + 24 * 6 + 12 - 16, r << 16 | g << 8 | b);
        drawCenteredString(fontRenderer, "Forked/reworked by bspkrs with permission", width / 2, height / 4 + 24 * 7 - 16, r << 16 | g << 8 | b);

        super.drawScreen(par1, par2, par3);
    }
}
