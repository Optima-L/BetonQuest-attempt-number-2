package org.betonquest.betonquest.quest.event;

import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.api.quest.event.ComposedEvent;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.quest.PrimaryServerThreadData;
import org.betonquest.betonquest.quest.PrimaryServerThreadType;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for {@link ComposedEvent}s to be executed on the primary server thread.
 */
public class PrimaryServerThreadComposedEvent extends PrimaryServerThreadType<ComposedEvent, Void> implements ComposedEvent {
    /**
     * Wrap the given {@link ComposedEvent} for execution on the primary server thread.
     * The {@link Server}, {@link BukkitScheduler} and {@link Plugin} are used to
     * determine if the current thread is the primary server thread and to
     * schedule the execution onto it in case it isn't.
     *
     * @param syncedEvent event to synchronize
     * @param server      server for primary thread identification
     * @param scheduler   scheduler for primary thread scheduling
     * @param plugin      plugin to associate with the scheduled task
     * @deprecated use constructor with {@link PrimaryServerThreadData}
     */
    @Deprecated
    public PrimaryServerThreadComposedEvent(final ComposedEvent syncedEvent, final Server server,
                                            final BukkitScheduler scheduler, final Plugin plugin) {
        super(syncedEvent, server, scheduler, plugin);
    }

    /**
     * Wrap the given {@link ComposedEvent} for execution on the primary server thread.
     * The {@link Server}, {@link BukkitScheduler} and {@link Plugin} are used to
     * determine if the current thread is the primary server thread and to
     * schedule the execution onto it in case it isn't.
     *
     * @param synced event to synchronize
     * @param data   the data containing server, scheduler and plugin used for primary thread access
     */
    public PrimaryServerThreadComposedEvent(final ComposedEvent synced, final PrimaryServerThreadData data) {
        super(synced, data);
    }

    @Override
    public void execute(@Nullable final Profile profile) throws QuestRuntimeException {
        call(() -> {
            synced.execute(profile);
            return null;
        });
    }

    @Override
    public void execute() throws QuestRuntimeException {
        call(() -> {
            synced.execute();
            return null;
        });
    }
}
