package bspkrs.worldstatecheckpoints;

import java.io.File;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.mod_WorldStateCheckpoints;

public class GuiLoadCheckpoint extends GuiScreen
{
    String                      guiTitle       = "Load Checkpoint";
    String                      guiSubTitle    = "Current changes will be lost!";
    
    protected boolean           showDelButtons = true;
    private boolean             gameOverScreen = false;
    
    private GuiButton[]         buttons;
    private GuiButton[]         delButtons;
    protected String[]          dirNames;
    private int[]               pageNums;
    private int                 pages          = 0;
    protected int               currentPage    = 0;
    protected int               startPage      = 0;
    
    protected CheckpointManager cpm;
    
    private GuiButton           back, prev, next, switchLoad;
    protected boolean           isAutoCheckpointsLoad;
    
    public GuiLoadCheckpoint(CheckpointManager cpm, boolean gameover, boolean isAutoCheckpointsLoad)
    {
        this.cpm = cpm;
        gameOverScreen = gameover;
        this.isAutoCheckpointsLoad = isAutoCheckpointsLoad;
    }
    
    public GuiLoadCheckpoint()
    {
        gameOverScreen = false;
        this.isAutoCheckpointsLoad = false;
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        controlList.clear();
        // cpm = new CheckpointManager(mc);
        byte byte0 = -16;
        
        int prevX, backX, nextX, switchX;
        
        prevX = width / 2 - 70 - 60 - 3;
        backX = width / 2 - 70 - 1;
        switchX = width / 2 + 1;
        nextX = width / 2 + 70 + 3;
        
        if (this.isAutoCheckpointsLoad)
        {
            guiTitle = "Load Auto-saves";
            switchLoad = new GuiButton(-4, switchX, height / 4 + 24 + byte0, 70, 20, "Checkpoints");
        }
        else
        {
            switchLoad = new GuiButton(-4, switchX, height / 4 + 24 + byte0, 70, 20, "Auto-Saves");
        }
        
        switchLoad.enabled = cpm.getHasCheckpoints(!isAutoCheckpointsLoad);
        
        prev = new GuiButton(-2, prevX, height / 4 + 24 + byte0, 60, 20, "<<<");
        back = new GuiButton(-1, backX, height / 4 + 24 + byte0, 70, 20, "Back");
        next = new GuiButton(-3, nextX, height / 4 + 24 + byte0, 60, 20, ">>>");
        
        controlList.add(back);
        controlList.add(switchLoad);
        controlList.add(prev);
        controlList.add(next);
        
        File[] files = cpm.getCheckpoints(isAutoCheckpointsLoad);
        
        dirNames = new String[files.length];
        pageNums = new int[files.length];
        buttons = new GuiButton[files.length];
        delButtons = new GuiButton[files.length];
        
        int page = 0;
        int pagecounter = -1; // will be turned to 0 in first cycle
        int index = -1; // will be turned to 0 in first cycle
        
        for (File file : files)
        {
            if (!file.isDirectory())
                continue;
            
            String label;
            
            try
            {
                label = file.getName().split("!", 2)[1];
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                label = file.getName();
            }
            
            index++;
            pagecounter++;
            
            if (pagecounter >= 5)
            {
                page++;
                pagecounter = 0;
            }
            
            GuiButton btn = new GuiButton(index, width / 2 - 100, height / 4 + 24 * 2 + 6 + 23 * pagecounter + byte0, label);
            controlList.add(btn);
            
            GuiButton delbtn = new GuiButton(index + 1000, width / 2 + 100 + 4, height / 4 + 24 * 2 + 6 + 23 * pagecounter + byte0, 20, 20, "X");
            controlList.add(delbtn);
            
            dirNames[index] = file.getName();
            pageNums[index] = page;
            buttons[index] = btn;
            delButtons[index] = delbtn;
        }
        
        pages = page;
        currentPage = startPage;
        showPage(currentPage);
    }
    
    void showPage(int page)
    {
        currentPage = page;
        
        if (currentPage < 0)
            currentPage = 0;
        if (currentPage > pages)
            currentPage = pages;
        
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].drawButton = (pageNums[i] == page);
            delButtons[i].drawButton = (pageNums[i] == page && showDelButtons);
        }
        
        prevNextDisableIfNeeded();
    }
    
    private void prevNextDisableIfNeeded()
    {
        prev.enabled = currentPage > 0;
        next.enabled = currentPage < pages;
    }
    
    private void goPrev()
    {
        if (currentPage > 0)
        {
            currentPage--;
            showPage(currentPage);
        }
    }
    
    private void goNext()
    {
        if (currentPage < pages)
        {
            currentPage++;
            showPage(currentPage);
        }
    }
    
    protected void checkpointButtonClicked(int index)
    {
        String dirname = dirNames[index];
        cpm.loadCheckpoint(dirname, isAutoCheckpointsLoad);
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
        mod_WorldStateCheckpoints.justLoadedCheckpoint = true;
        mod_WorldStateCheckpoints.loadMessage = "Loaded checkpoint \"" + dirname.split("!", 2)[1] + "\".";
    }
    
    protected void delButtonClicked(int index)
    {
        mc.displayGuiScreen(new GuiDeleteCheckpointYesNo(this, dirNames[index], currentPage, isAutoCheckpointsLoad));
    }
    
    protected void backButtonClicked()
    {
        mc.displayGuiScreen(gameOverScreen ? new GuiGameOver() : new GuiCheckpointsMenu(cpm));
    }
    
    protected void switchButtonClicked()
    {
        mc.displayGuiScreen(new GuiLoadCheckpoint(cpm, gameOverScreen, !isAutoCheckpointsLoad));
    }
    
    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled)
            return;
        
        switch (guibutton.id)
        {
            case -1:
                backButtonClicked();
                return;
                
            case -2:
                goPrev();
                return;
                
            case -3:
                goNext();
                return;
                
            case -4:
                switchButtonClicked();
                return;
                
            default:
                if (guibutton.id >= 1000)
                    delButtonClicked(guibutton.id - 1000);
                else
                    checkpointButtonClicked(guibutton.id);
                
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
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        if (guiSubTitle == null || (gameOverScreen && mc.theWorld.getWorldInfo().isHardcoreModeEnabled()))
            drawCenteredString(fontRenderer, guiTitle, width / 2, 50 + 5, 0xffffff);
        else
        {
            drawCenteredString(fontRenderer, guiSubTitle, width / 2, 50 + 5, 0xee0000);
            drawCenteredString(fontRenderer, guiTitle, width / 2, 50 + 5 - 16, 0xffffff);
        }
        super.drawScreen(par1, par2, par3);
    }
}
