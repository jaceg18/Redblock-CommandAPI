package com.ftjace.commandapi.Execution;

import com.ftjace.commandapi.Util.CommandArguments;

import java.util.function.Consumer;

/**
 * A record that holds the execution consumer
 * @param executor the execution consumer
 */
public record Executor(Consumer<CommandArguments> executor) {
    /**
     * runs the executor
     * @param commandArguments String[] args from command executor.
     */
    void accept(CommandArguments commandArguments){executor.accept(commandArguments);}
}
