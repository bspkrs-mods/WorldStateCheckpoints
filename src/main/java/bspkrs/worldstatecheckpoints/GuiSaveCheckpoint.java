package bspkrs.worldstatecheckpoints;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

public class GuiSaveCheckpoint extends GuiScreen
{
    private GuiTextField            edit;
    private GuiButton               back, save;
    private final CheckpointManager cpm;

    public GuiSaveCheckpoint(CheckpointManager cpm)
    {
        this.cpm = cpm;
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

        save = new GuiButton(-2, (width / 2) - 62, (height / 4) + 24 + (24 * 3) + byte0, 60, 20, StatCollector.translateToLocal("wsc.saveCheckpoint.save"));
        back = new GuiButton(-1, (width / 2) + 2, (height / 4) + 24 + (24 * 3) + byte0, 60, 20, StatCollector.translateToLocal("gui.cancel"));

        edit = new GuiTextField(1, fontRendererObj, (width / 2) - 100, (height / 4) + 24 + 24, 200, 20);
        edit.setText("");

        edit.setFocused(true);
        save.enabled = false;

        buttonList.add(back);
        buttonList.add(save);
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
            return;

        switch (par1GuiButton.id)
        {
            case -1:
                WSCSettings.mc.displayGuiScreen(new GuiCheckpointsMenu(cpm));
                return;

            case -2:
                cpm.setCheckpoint(edit.getText(), false);
                WSCSettings.mc.displayGuiScreen(null);
                WSCSettings.mc.setIngameFocus();
                return;
        }
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = " !.,+_-";
        if (Character.isLetterOrDigit(c) || validChars.contains(String.valueOf(c)) || (i == Keyboard.KEY_BACK) || (i == Keyboard.KEY_DELETE) || (i == Keyboard.KEY_LEFT) || (i == Keyboard.KEY_RIGHT) || (i == Keyboard.KEY_HOME) || (i == Keyboard.KEY_END))
            edit.textboxKeyTyped(c, i);

        save.enabled = (edit.getText().trim().length() > 0) && !edit.getText().trim().endsWith(".");

        if (c == '\r')
            actionPerformed(buttonList.get(1));
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) throws IOException
    {
        super.mouseClicked(par1, par2, par3);
        edit.mouseClicked(par1, par2, par3);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        edit.updateCursorCounter();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        edit.drawTextBox();

        drawCenteredString(fontRendererObj, StatCollector.translateToLocal("wsc.saveCheckpoint.setCheckpoint"), width / 2, 80, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
