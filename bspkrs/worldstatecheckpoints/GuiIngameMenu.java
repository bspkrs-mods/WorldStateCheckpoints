package bspkrs.worldstatecheckpoints;

import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiStats;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatList;

public class GuiIngameMenu extends GuiScreen
{
    /** Also counts the number of updates, not certain as to why yet. */
    private int updateCounter2;

    /** Counts the number of screen updates. */
    private int updateCounter;
    
    private CheckpointManager chpmgr;

    public GuiIngameMenu()
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
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 24*5 + 24 + byte0, StatCollector.translateToLocal("menu.returnToMenu")));

        if (!mc.isSingleplayer())
        {
            ((GuiButton)controlList.get(0)).displayString = StatCollector.translateToLocal("menu.disconnect");
        }

        controlList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24*1 - 24 + byte0, StatCollector.translateToLocal("menu.returnToGame")));
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 24*4 + 25 + byte0, StatCollector.translateToLocal("menu.options")));
        controlList.add(new GuiButton(5, width / 2 - 100, height / 4 + 24*2 - 24 + byte0, 98, 20, StatCollector.translateToLocal("gui.achievements")));
        controlList.add(new GuiButton(6, width / 2 + 2, height / 4 + 24*2 - 24 + byte0, 98, 20, StatCollector.translateToLocal("gui.stats")));
        
        GuiButton load = new GuiButton(-1, width / 2 + 36, height / 4 + 24*3 + byte0, 64, 20, "Load from");
        GuiButton store = new GuiButton(-2, width / 2 - 100, height / 4 + 24*3 + byte0, 64, 20, "Save new");
        GuiButton replace = new GuiButton(-3, width / 2 - 32, height / 4 + 24*3 + byte0, 64, 20, "Overwrite");
        
        if (!mc.isSingleplayer())
            replace.enabled = load.enabled = store.enabled = false;    
        else
            replace.enabled = load.enabled = chpmgr.getHasCheckpoints();
        
        controlList.add(load);
        controlList.add(store);
        controlList.add(replace);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
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

            case 0:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;

            case 1:
                mc.statFileWriter.readStat(StatList.leaveGameStat, 1);

                if (!mc.isSingleplayer())
                    mc.theWorld.sendQuittingDisconnectingPacket();

                mc.loadWorld(null);
                mc.displayGuiScreen(new GuiMainMenu());
                break;

            case 4:
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
                break;

            case 5:
                mc.displayGuiScreen(new GuiAchievements(mc.statFileWriter));
                break;

            case 6:
                mc.displayGuiScreen(new GuiStats(this, mc.statFileWriter));
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
        boolean flag = false;//!mc.theWorld.saveAllChunks(true, null);

        if (flag || updateCounter < 20)
        {
            float f = ((float)(updateCounter % 10) + par3) / 10F;
            f = MathHelper.sin(f * (float)Math.PI * 2.0F) * 0.2F + 0.8F;
            int i = (int)(255F * f);
            drawString(fontRenderer, "Saving level..", 8, height - 16, i << 16 | i << 8 | i);
        }

        drawCenteredString(fontRenderer, "Game menu", width / 2, 40 - 8, 0xffffff);
        drawCenteredString(fontRenderer, "Checkpoints", width / 2, 40 + 24*3 - 8, 0xffffff);
        
        float f = ((float)(updateCounter % 60) + par3) / 60F;
        f = MathHelper.sin(f * (float)Math.PI * 2.0F) * 0.2F + 0.8F;
        int r = (int)(0xFF * f);
        int g = (int)(0xBA * f);
        int b = (int)(0x00 * f);
        
        drawCenteredString(fontRenderer, "Checkpoints by MightyPork!", width / 2, height / 4 + 24*5 + 24 -16 + 24+12, r << 16 | g << 8 | b);
        
        super.drawScreen(par1, par2, par3);
    }
}
