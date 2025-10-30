package com.ftjace.commandapi.Execution;

import com.ftjace.commandapi.Util.CommandArguments;
import com.ftjace.commandapi.Util.Detail;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract class required for CNode implementation.
 * All command classes should extend this class.
 */
public abstract class BaseCommand implements CommandExecutor, TabCompleter {

    protected final @NotNull CNode root;

    /**
     * required super constructor
     * @param plugin main plugin instance
     * @param root command root node
     */
    public BaseCommand(JavaPlugin plugin, CNode root){
        this.root = root;
        root.registerIfRoot(plugin, this);
    }

    /**
     * @return string permission required to run command.
     */
    protected abstract String permission();

    /**
     * @return whether the command can only be invoked by players
     */
    protected abstract boolean playerOnly();


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if ((playerOnly() && !(commandSender instanceof Player)) || (!commandSender.hasPermission(permission()) && !commandSender.isOp())) { commandSender.sendMessage(playerOnly() && !(commandSender instanceof Player) ? Detail.PLAYER_ONLY_COMMAND : Detail.NO_PERMISSION_COMMAND); return true; }
        CNode target = strings.length == 0 ? root : root.nearestMatch(strings);
        return (target != null ? target : root).executeIfPresent(new CommandArguments(commandSender, s, strings));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 0) return root.pullSupplyIfPresent();
        String[] matchedArgs = strings.length > 1 ? Arrays.copyOf(strings, strings.length - 1) : new String[0];
        CNode target = matchedArgs.length == 0 ? root : root.nearestMatch(matchedArgs);
        return target != null ? target.pullSupplyIfPresent() : root.pullSupplyIfPresent();
    }
}
