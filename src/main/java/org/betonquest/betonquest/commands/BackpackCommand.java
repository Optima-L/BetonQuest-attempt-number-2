package org.betonquest.betonquest.commands;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.betonquest.betonquest.Backpack;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The backpack command. It opens profile's backpack.
 */
public class BackpackCommand implements CommandExecutor {
    /**
     * Custom {@link BetonQuestLogger} instance for this class.
     */
    private final BetonQuestLogger log;

    /**
     * Registers a new executor of the /backpack command
     */
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public BackpackCommand(final BetonQuestLogger log) {
        this.log = log;
        BetonQuest.getInstance().getCommand("backpack").setExecutor(this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if ("backpack".equalsIgnoreCase(cmd.getName())) {
            // command sender must be a player, console can't have a backpack
            if (sender instanceof Player) {
                final OnlineProfile onlineProfile = PlayerConverter.getID((Player) sender);
                log.debug("Executing /backpack command for " + onlineProfile);
                new Backpack(onlineProfile);
            }
            return true;
        }
        return false;
    }
}
