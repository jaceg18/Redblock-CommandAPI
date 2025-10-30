package com.ftjace.commandapi.Util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Detail {
    public static Component NO_PERMISSION_COMMAND = Component.text("You do not have permission to run this command!", NamedTextColor.RED);
    public static Component PLAYER_ONLY_COMMAND = Component.text("You must be a player to run this command!", NamedTextColor.RED);
    public static Component NO_EXECUTOR_COMMAND = Component.text("There is not a executor assigned to this command!", NamedTextColor.RED);
    public static Component NO_USAGE_WARNING = Component.text("There was an error, ensure you are using the correct usage.", NamedTextColor.RED);
}
