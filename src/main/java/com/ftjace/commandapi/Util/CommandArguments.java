package com.ftjace.commandapi.Util;

import com.ftjace.commandapi.Annotations.ScheduledForRemoval;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import org.bukkit.command.CommandSender;

/**
 * Just holds command arguments to simplify passing through parameters
 * @param sender the command sender
 * @param label the command label
 * @param args the command args
 */
public record CommandArguments (CommandSender sender, String label, String[] args){

    /**
     * While not required, it is very useful to
     * prevent ArrayIndexOutOfBounds exceptions.
     * This is the API preferred way to access arguments directly if needed.
     * @param index the argument index
     * @return a pair that holds the argument text (if valid) and a boolean stating validity
     */
    @ScheduledForRemoval(inVersion = "1.2.0")
    public Pair<String, Boolean> getArg(int index){
        Pair<String, Boolean> isPresent;
        try { isPresent = new MutablePair<>(args[index], true);}
        catch (ArrayIndexOutOfBoundsException ignored){isPresent = new MutablePair<>("", false);}
        return isPresent;
    }

}
