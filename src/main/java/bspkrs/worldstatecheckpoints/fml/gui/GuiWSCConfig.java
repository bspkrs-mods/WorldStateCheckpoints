package bspkrs.worldstatecheckpoints.fml.gui;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.worldstatecheckpoints.fml.Reference;

public class GuiWSCConfig extends GuiConfig
{
    public GuiWSCConfig(GuiScreen parent)
    {
        super(parent, (new ConfigProperty(Reference.config.getCategory(Reference.CTGY))).getConfigPropertiesList(true),
                true, Reference.MODID, true, GuiConfig.getAbridgedConfigPath(Reference.config.toString()));
    }
}
