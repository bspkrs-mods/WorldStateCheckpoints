package bspkrs.worldstatecheckpoints;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiDeleteCheckpointYesNo extends GuiScreen
{
    private GuiButton         no, yes;

    private CheckpointManager chpmgr;

    private GuiLoadCheckpoint parentScreen;
    private String            name, dirname;
    private int               page;
    private boolean           isAutoCheckpoint;

    public GuiDeleteCheckpointYesNo(GuiLoadCheckpoint parent, String dirname, int page, boolean isAutoCheckpoint)
    {
        parentScreen = parent;
        this.page = page;
        this.dirname = dirname;
        name = dirname.split("!", 2)[1];
        this.isAutoCheckpoint = isAutoCheckpoint;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        chpmgr = new CheckpointManager(mc);

        byte byte0 = -16;

        controlList.add(new GuiButton(-2, width / 2 - 62, height / 4 + 24 + 24 * 3 + byte0, 60, 20, "Yes"));
        controlList.add(new GuiButton(-1, width / 2 + 2, height / 4 + 24 + 24 * 3 + byte0, 60, 20, "No"));
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled) { return; }

        switch (par1GuiButton.id)
        {
            case -1:
                mc.displayGuiScreen(parentScreen);
                return;

            case -2:
                chpmgr.deleteCheckpoint(dirname, isAutoCheckpoint);

                mc.displayGuiScreen(parentScreen);
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

        drawCenteredString(fontRenderer, "Really delete checkpoint?", width / 2, 100, 0xffffff);
        drawCenteredString(fontRenderer, "\"" + name + "\"", width / 2, 100 + 20, 0xffff00);
        super.drawScreen(par1, par2, par3);
    }
}
