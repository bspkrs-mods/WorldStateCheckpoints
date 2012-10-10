package net.minecraft.src;

public class GuiReplaceCheckpoint extends GuiLoadCheckpoint
{	
    public GuiReplaceCheckpoint()
    {
    	guiTitle = "Overwrite Checkpoint";
    	guiSubTitle = "Old data will be lost.";
    }
    
    public GuiReplaceCheckpoint(int page)
    {
    	this();
    	startPage = page;
    }

 
    protected void checkpointButtonClicked(int index){
    	
    	String dirname = dirNames[index];    	
    	
    	mc.displayGuiScreen(new GuiReplaceCheckpointChangeName(dirname, currentPage));
    	
    }
    
    protected void backButtonClicked(){
    	mc.displayGuiScreen(new GuiIngameMenu()); 
    }
}
