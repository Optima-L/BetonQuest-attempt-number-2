package org.betonquest.betonquest.quest.event.lever;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.quest.event.HybridEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.quest.event.PrimaryServerThreadHybridEvent;
import org.betonquest.betonquest.quest.event.StandardHybridEventFactory;
import org.betonquest.betonquest.utils.location.CompoundLocation;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Factory for {@link LeverEvent}.
 */
public class LeverEventFactory extends StandardHybridEventFactory {
    /**
     * Server to use for syncing to the primary server thread.
     */
    private final Server server;

    /**
     * Scheduler to use for syncing to the primary server thread.
     */
    private final BukkitScheduler scheduler;

    /**
     * Plugin to use for syncing to the primary server thread.
     */
    private final Plugin plugin;

    /**
     * Create a new LeverEventFactory.
     *
     * @param server    the server to use for syncing to the primary server thread
     * @param scheduler the scheduler to use for syncing to the primary server thread
     * @param plugin    the plugin to use for syncing to the primary server thread
     */
    public LeverEventFactory(final Server server, final BukkitScheduler scheduler, final Plugin plugin) {
        this.server = server;
        this.scheduler = scheduler;
        this.plugin = plugin;
    }

    @Override
    public HybridEvent parseHybridEvent(final Instruction instruction) throws InstructionParseException {
        final CompoundLocation location = instruction.getLocation();
        final StateType stateType = instruction.getEnum(StateType.class);
        return new PrimaryServerThreadHybridEvent(
                new LeverEvent(stateType, location),
                server, scheduler, plugin
        );
    }
}
