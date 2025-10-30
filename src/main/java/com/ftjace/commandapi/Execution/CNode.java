package com.ftjace.commandapi.Execution;

import com.ftjace.commandapi.Annotations.ScheduledForRemoval;
import com.ftjace.commandapi.Util.CommandArguments;
import com.ftjace.commandapi.Util.Detail;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Command node class, absolutely necessary if you plan to use the API as intended.
 * Allows for chaining command execution, tab completion, and sub commands.
 */
public class CNode {
    private CNode parent;
    private final String data;
    private final List<CNode> children = new ArrayList<>();
    private TabSupplier<List<String>> supplier;
    private Executor executor;

    /**
     * Creates a root node for the string command
     * @param data command text
     */
    public CNode(String data){
        this.data = data;
    }

    /**
     * This method is unnecessary to call if your chaining children to a root node.
     * Ensure you are setting root = attachParent(p); If calling after initialization.
     * @param parent The parent node
     * @return builder design reassignment
     */
    @SuppressWarnings("UnusedReturnValue")
    public CNode attachParent(CNode parent){
        this.parent = parent;
        return this;
    }

    /**
     * Automatically sets parent if chained.
     * Ensure you are setting root = addChildren(c); If calling after initialization.
     * @param children children to add
     * @return builder design reassignment
     */
    public CNode addChildren(CNode... children){
        Arrays.stream(children).forEach(c -> c.attachParent(this));
        this.children.addAll(List.of(children));
        return this;
    }

    private CNode getNodeByName(String data){
        return children.stream().filter(n -> n.data.equals(data)).findFirst().orElse(null);
    }

    /**
     * Not typically used from API, responsible for the backend matching of command args
     * @param args The command arguments
     * @return returns the target node for arg matching
     */
    @ApiStatus.Experimental
    protected CNode nearestMatch(String[] args){
        if (args.length == 0) return this;
        CNode current = this;
        for (String arg : args) {
            CNode next = current.getNodeByName(arg);
            if (next == null) break;
            current = next;
        }
        return current == this ? null : current;
    }

    /**
     * @return The string data (command text)
     */
    @ScheduledForRemoval(inVersion = "1.1.0")
    @Deprecated(since = "1.0.0-SNAPSHOT", forRemoval = true)
    public String getData(){
        return data;
    }

    /**
     * Attachs a tab supplier
     * @param supplier The Tab Supplier
     * @return Builder design reassignment
     */
    @ApiStatus.Experimental
    public CNode attachTabSupplier(TabSupplier<List<String>> supplier){
        this.supplier = supplier;
        return this;
    }

    /**
     * Attempts to collect tab completion from the supplier
     * @return tab completions from a provided supplier, node children otherwise.
     */
    @ApiStatus.Experimental
    protected List<String> pullSupplyIfPresent(){
        return (supplier == null) ? children.stream().map(c -> c.data).collect(Collectors.toList()) : supplier.get();
    }

    /**
     * Attach's the consumer that executes commands
     * @param executor API provided consumer class
     * @return Builder design reassignment
     */
    @ApiStatus.Experimental
    public CNode attachExecutor(Executor executor){
        this.executor = executor; return this;
    }

    /**
     * If there is a provider executor, we will invoke the command, otherwise warn user.
     * @param commandArguments The command arguments
     * @return always true, either way the command was 'resolved'.
     */
    @ApiStatus.Experimental
    protected boolean executeIfPresent(CommandArguments commandArguments){
        if (executor == null) {
            commandArguments.sender().sendMessage(Detail.NO_EXECUTOR_COMMAND);
            return true;
        }
        executor.accept(commandArguments);
        return true;
    }

    /**
     * Simply registers the command to the plugin.
     * @param plugin the main plugin instance.
     * @param handler the command extending BaseCommand.
     */
    @ApiStatus.Experimental
    protected void registerIfRoot(JavaPlugin plugin, BaseCommand handler){
        if (parent != null) return;
        PluginCommand pc = plugin.getCommand(data);
        if (pc == null){
            plugin.getLogger().severe("Command not found in plugin.yml: " + data);
            return;
        }
        pc.setExecutor(handler);
        pc.setTabCompleter(handler);
    }

    /**
     * @return the command text
     */
    @Override
    public String toString(){
        return data;
    }

}
