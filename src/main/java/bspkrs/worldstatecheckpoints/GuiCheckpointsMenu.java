package bspkrs.worldstatecheckpoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import bspkrs.helpers.client.MinecraftHelper;
import bspkrs.helpers.client.gui.GuiScreenWrapper;

public class GuiCheckpointsMenu extends GuiScreenWrapper
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
        field_146292_n.clear();
        byte byte0 = -16;
        
        field_146292_n.add(new GuiButton(4, width() / 2 - 100, height() / 4 + 24 * 5 + byte0, StatCollector.translateToLocal("menu.returnToGame")));
        
        GuiButton save = new GuiButton(-2, width() / 2 - 100, height() / 4 + 24 + byte0, 99, 20, StatCollector.translateToLocal("wsc.menu.saveNew"));
        GuiButton replace = new GuiButton(-3, width() / 2 + 1, height() / 4 + 24 + byte0, 99, 20, StatCollector.translateToLocal("wsc.menu.overwrite"));
        GuiButton load = new GuiButton(-1, width() / 2 - 100, height() / 4 + 24 * 2 + byte0, 99, 20, StatCollector.translateToLocal("wsc.menu.loadCheckpoint"));
        GuiButton loadAuto = new GuiButton(-4, width() / 2 + 1, height() / 4 + 24 * 2 + byte0, 99, 20, StatCollector.translateToLocal("wsc.menu.loadAutoSave"));
        GuiButton configAuto = new GuiButton(-5, width() / 2 - 100, height() / 4 + 24 * 3 + byte0, StatCollector.translateToLocal("wsc.menu.configureAutoSave") + ": " +
                (cpm.autoSaveEnabled ? StatCollector.translateToLocal("options.on") : StatCollector.translateToLocal("options.off")));
        
        if (!WSCSettings.mc.isSingleplayer())
            replace.field_146124_l = load.field_146124_l = save.field_146124_l = loadAuto.field_146124_l = configAuto.field_146124_l = false;
        else
        {
            replace.field_146124_l = load.field_146124_l = cpm.getHasCheckpoints(false);
            loadAuto.field_146124_l = cpm.getHasCheckpoints(true);
        }
        
        field_146292_n.add(load);
        field_146292_n.add(loadAuto);
        field_146292_n.add(save);
        field_146292_n.add(replace);
        field_146292_n.add(configAuto);
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void func_146284_a(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.field_146127_k)
        {
            case 4:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, null);
                WSCSettings.mc.setIngameFocus();
                break;
            
            case -3:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiReplaceCheckpoint(cpm));
                break;
            
            case -2:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiSaveCheckpoint(cpm));
                break;
            
            case -1:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiLoadCheckpoint(cpm, false, false));
                break;
            
            case -4:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiLoadCheckpoint(cpm, false, true));
                break;
            
            case -5:
                MinecraftHelper.displayGuiScreen(WSCSettings.mc, new GuiConfigureAutoSave(cpm));
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
            drawString(field_146289_q, StatCollector.translateToLocal("wsc.menu.savingLevel") + "..", 8, height() - 16, i << 16 | i << 8 | i);
        }
        
        drawCenteredString(field_146289_q, "World State Checkpoints", width() / 2, 40, 0xffffff);
        
        float f = ((updateCounter % 60) + par3) / 60F;
        f = MathHelper.sin(f * (float) Math.PI * 2.0F) * 0.2F + 0.8F;
        int r = (int) (0xFF * f);
        int g = (int) (0xBA * f);
        int b = (int) (0x00 * f);
        
        drawCenteredString(field_146289_q, StatCollector.translateToLocal("wsc.credit.MightyPork"), width() / 2, height() / 4 + 24 * 6 + 12 - 16, r << 16 | g << 8 | b);
        drawCenteredString(field_146289_q, StatCollector.translateToLocal("wsc.credit.bspkrs"), width() / 2, height() / 4 + 24 * 7 - 16, r << 16 | g << 8 | b);
        
        super.drawScreen(par1, par2, par3);
    }
}
