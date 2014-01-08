package bspkrs.worldstatecheckpoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import bspkrs.helpers.client.MinecraftHelper;
import bspkrs.helpers.client.gui.GuiScreenWrapper;
import bspkrs.helpers.client.gui.GuiTextFieldWrapper;

public class GuiSaveCheckpoint extends GuiScreenWrapper
{
    private GuiTextFieldWrapper     edit;
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
        buttonList().clear();
        Keyboard.enableRepeatEvents(true);
        
        byte byte0 = -16;
        
        save = new GuiButton(-2, width() / 2 - 62, height() / 4 + 24 + 24 * 3 + byte0, 60, 20, StatCollector.translateToLocal("wsc.saveCheckpoint.save"));
        back = new GuiButton(-1, width() / 2 + 2, height() / 4 + 24 + 24 * 3 + byte0, 60, 20, StatCollector.translateToLocal("gui.cancel"));
        
        edit = new GuiTextFieldWrapper(field_146289_q, width() / 2 - 100, height() / 4 + 24 + 24, 200, 20);
        edit.setText("");
        
        edit.setFocused(true);
        save.field_146124_l = false;
        
        field_146292_n.add(back);
        field_146292_n.add(save);
    }
    
    @Override
    public void func_146281_b()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void func_146284_a(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.field_146124_l)
            return;
        
        switch (par1GuiButton.field_146127_k)
        {
            case -1:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiCheckpointsMenu(cpm));
                return;
                
            case -2:
                cpm.setCheckpoint(edit.getText(), false);
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, null);
                WSCSettings.mc.setIngameFocus();
                return;
        }
    }
    
    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = " !.,+_-";
        if (Character.isLetterOrDigit(c) || validChars.contains(String.valueOf(c)) || i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_HOME || i == Keyboard.KEY_END)
            edit.textboxKeyTyped(c, i);
        
        save.field_146124_l = edit.getText().trim().length() > 0 && !edit.getText().trim().endsWith(".");
        
        if (c == '\r')
            actionPerformed((GuiButton) field_146292_n.get(1));
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
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
        
        drawCenteredString(field_146289_q, StatCollector.translateToLocal("wsc.saveCheckpoint.setCheckpoint"), width() / 2, 80, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
