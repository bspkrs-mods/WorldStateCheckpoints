package bspkrs.worldstatecheckpoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import bspkrs.helpers.client.MinecraftHelper;
import bspkrs.helpers.client.gui.GuiScreenWrapper;
import bspkrs.helpers.client.gui.GuiTextFieldWrapper;
import bspkrs.helpers.entity.player.EntityPlayerHelper;

public class GuiReplaceCheckpointChangeName extends GuiScreenWrapper
{
    private GuiTextFieldWrapper     edit;
    private GuiButton               back, save;
    private final CheckpointManager cpm;
    private final String            dirname_orig, dirname_prefix, name;
    int                             page = 0;
    
    public GuiReplaceCheckpointChangeName(CheckpointManager cpm, String dirname, int page)
    {
        this.cpm = cpm;
        dirname_orig = dirname;
        dirname_prefix = dirname.split("!")[0];
        name = dirname.split("!", 2)[1];
        this.page = page;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        field_146292_n.clear();
        Keyboard.enableRepeatEvents(true);
        
        byte byte0 = -16;
        
        back = new GuiButton(-1, width() / 2 + 1, height() / 4 + 24 + 24 * 3 + byte0, 60, 20, StatCollector.translateToLocal("gui.cancel"));
        save = new GuiButton(-2, width() / 2 - 61, height() / 4 + 24 + 24 * 3 + byte0, 60, 20, StatCollector.translateToLocal("wsc.saveCheckpoint.save"));
        
        edit = new GuiTextFieldWrapper(field_146289_q, width() / 2 - 100, height() / 4 + 24 + 24, 200, 20);
        edit.setText(name);
        edit.setFocused(true);
        save.field_146124_l = true;
        
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
        {
            return;
        }
        
        switch (par1GuiButton.field_146127_k)
        {
            case -1:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiReplaceCheckpoint(cpm, page));
                return;
                
            case -2:
                cpm.saveCheckpointInto(dirname_orig, dirname_prefix + "!" + edit.getText());
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, null);
                WSCSettings.mc.setIngameFocus();
                EntityPlayerHelper.addChatMessage(WSCSettings.mc.thePlayer, new ChatComponentText(StatCollector.translateToLocalFormatted("wsc.chatMessage.savedCheckpoint", edit.getText())));
                return;
        }
    }
    
    @Override
    protected void keyTyped(char c, int i)
    {
        String validChars = " !.,+_-";
        if (Character.isLetterOrDigit(Character.valueOf(c)) || validChars.contains(String.valueOf(c)) || i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_HOME || i == Keyboard.KEY_END)
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
        drawCenteredString(field_146289_q, StatCollector.translateToLocal("wsc.overwriteCheckpoint.editName"), width() / 2, 80, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
