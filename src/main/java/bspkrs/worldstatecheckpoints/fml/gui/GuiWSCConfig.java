package bspkrs.worldstatecheckpoints.fml.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import bspkrs.worldstatecheckpoints.fml.Reference;

public class GuiWSCConfig extends GuiConfig
{
    public GuiWSCConfig(GuiScreen parent)
    {
        super(parent, (new ConfigElement(Reference.config.getCategory(Reference.CTGY))).getChildElements(),
                Reference.MODID, false, false, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
}
