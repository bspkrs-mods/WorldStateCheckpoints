package bspkrs.worldstatecheckpoints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandWSC extends CommandBase
{
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        try
        {
            return Minecraft.getMinecraft().isSingleplayer();
        }
        catch (Throwable e)
        {
            return false;
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, "load", "save");
        else if ((args.length == 2) && args[0].equalsIgnoreCase("load"))
        {
            List<String> list = new ArrayList<String>();
            list.addAll(WSCSettings.cpm.getCheckpointNames(true));
            list.addAll(WSCSettings.cpm.getCheckpointNames(false));
            return getListOfStringsMatchingLastWord(args, list.toArray(new String[] {}));
        }
        return new ArrayList<>();
    }

    @Override
    public int compareTo(ICommand p_compareTo_1_) {
        return 0;
    }

    @Override
    public String getName() {
        return "wsc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.wsc.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("save"))
            {
                String name = "";
                for (int i = 1; i < args.length; i++)
                    name += " " + args[i];
                if (name.trim().endsWith("!") || name.trim().endsWith("."))
                {
                    throw new WrongUsageException("commands.wsc.load.usage");
                }
                WSCSettings.cpm.setCheckpoint(name.trim(), name.trim().isEmpty() || name.contains(CheckpointManager.AUTOSAVES_PREFIX));
                return;
            }
            else if (args[0].equalsIgnoreCase("load"))
            {
                if (args.length >= 2)
                {
                    String name = "";
                    for (int i = 1; i < args.length; i++)
                        name += " " + args[i];

                    String dirName = WSCSettings.cpm.getCheckpointDirNameFromDisplayName(name.trim());
                    if (dirName != null)
                        WSCSettings.delayedLoadCheckpoint(dirName, dirName.contains(CheckpointManager.AUTOSAVES_PREFIX), 1);
                    else
                        WSCSettings.mc.player.sendMessage(new TextComponentString(I18n.format("wsc.chatMessage.invalidCheckpointNameForLoadCommand", name.trim())));

                    return;
                }
                else
                    throw new WrongUsageException("commands.wsc.load.usage");
            }
        }

        throw new WrongUsageException("commands.wsc.usage");
    }
}
