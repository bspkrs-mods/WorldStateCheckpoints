package bspkrs.worldstatecheckpoints.fml;

import net.minecraftforge.common.config.Configuration;

public class Reference {

    public static final String  MODID        = "worldstatecheckpoints";
    public static final String  NAME         = "WorldStateCheckpoints";
    public static final String  MOD_VERSION  = "@MOD_VERSION@";
    public static final String  DEPENDENCIES = "required-after:bspkrscore@["+ bspkrs.bspkrscore.fml.Reference.MOD_VERSION +",)";
    public static final String  PROXY_COMMON = "bspkrs.worldstatecheckpoints.fml.CommonProxy";
    public static final String  PROXY_CLIENT = "bspkrs.worldstatecheckpoints.fml.ClientProxy";
    public static final String  GUI_FACTORY  = "bspkrs.worldstatecheckpoints.fml.gui.ModGuiFactoryHandler";
    public static final String  CTGY         = "autosave_new_world_defaults";

    public static Configuration config       = null;
}