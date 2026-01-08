package de.ganzer.core.logging;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A logger that writes messages into its targets.
 * <p>
 * Log targets are inserted into the logger by {@link #addTarget(String, LogTarget)}
 * or {@link #addTarget(String, LogTarget, boolean)} and removed by
 * {@link #removeTarget(String)}.
 * <p>
 * A log message is written by write(). The message is pushed to all active
 * targets. A target can be activated or suspended by activateTarget().
 * <p>
 * All methods of this class are thread safe.
 */
public class Logger {
    private static class TargetInfo {
        public final LogTarget target;
        public boolean active;

        public TargetInfo(LogTarget target, boolean active) {
            this.target = target;
            this.active = active;
        }
    }

    private final Map<String, TargetInfo> targets = new HashMap<>();

    /**
     * Adds the specified target to this logger instance.
     * <p>
     * A target should never be inserted into several Logger instances because
     * the target itself is not thread safe and several loggers may be used in
     * several threads.
     *
     * @param id A unique ID that identifies the target within this instance.
     *        If there is already another target with this ID added, the
     *        previous one is replaced.
     * @param target The target to add.
     *
     * @throws NullPointerException {@code id} or {@code target} is {@code null}.
     * @throws IllegalArgumentException {@code target} is already used by another
     *         logger instance or is added twice to this instance.
     *
     * @see #getTarget(String)
     */
    synchronized public void addTarget(String id, LogTarget target) {
        addTarget(id, target, true);
    }

    /**
     * Adds the specified target to this logger instance.
     * <p>
     * A target should never be inserted into several Logger instances because
     * the target itself is not thread safe and several loggers may be used in
     * several threads.
     *
     * @param id A unique ID that identifies the target within this instance.
     *        If there is already another target with this ID added, the
     *        previous one is replaced.
     * @param target The target to add.
     * @param inactive {@code true} to deactivate the target be default.
     *
     * @throws NullPointerException {@code id} or {@code target} is {@code null}.
     * @throws IllegalArgumentException {@code target} is already used by another
     *         logger instance or is added twice to this instance.
     *
     * @see #getTarget(String)
     */
    synchronized public void addTarget(String id, LogTarget target, boolean inactive) {
        Objects.requireNonNull(id, "id must not be null.");
        Objects.requireNonNull(target, "target must not be null.");

        if (target.getOwner() != null)
            throw new IllegalArgumentException("target is already owned by a logger.");

        var prev =  this.targets.put(id, new TargetInfo(target, !inactive));

        target.setOwner(this);

        if (prev != null)
            prev.target.setOwner(null);
    }

    /**
     * Removes the target with the specified ID.
     *
     * @param id The ID of the target to remove.
     *
     * @return The removed target or {@code null} if no target with the ID
     *         {@code id} was added.
     *
     * @see #getTarget(String)
     */
    synchronized public LogTarget removeTarget(String id) {
        var prev = targets.remove(id);

        if (prev == null)
            return null;

        prev.target.setOwner(null);

        return prev.target;
    }

    /**
     * Gets the target with the specified ID.
     *
     * @param id The ID of the target to get.
     *
     * @return The target with the specified ID or {@code null} if no target
     *         with the ID {@code id} was added.
     *
     * @see #getTarget(String)
     */
    synchronized public LogTarget getTarget(String id) {
        var info = targets.get(id);
        return info != null ? info.target : null;
    }

    /**
     * Activates or deactivates the target with the specified ID.
     * <p>
     * If no target with the specified ID is found, nothing is done.
     *
     * @param id The ID of the target to activate.
     * @param activate {@code true} to activate the target, {@code false} to
     *        deactivate it.
     *
     * @see #getTarget(String)
     */
    synchronized public void activateTarget(String id, boolean activate) {
        var info = targets.get(id);

        if (info != null)
            info.active = activate;
    }

    /**
     * Gets a value that indicates whether the target with the specified ID is
     * activated.
     *
     * @param id The ID of the target to query.
     *
     * @return {@code true} if the target is activated; {@code false} is returned
     *         if the target is not activated or if no target with the specified
     *         ID was added.
     *
     * @see #getTarget(String)
     */
    synchronized boolean isTargetActive(String id) {
        var info = targets.get(id);
        return info != null && info.active;
    }

    /**
     * Writes the specified message with the specified level into all active
     * targets.
     * <p>
     * The meaning of the level is implementation defined. It depends on the
     * filter that is used by a target to filter messages.
     *
     * @param level The level of the message to write.
     * @param message The message to write. An emtpy string is written if this
     *        is {@code null}.
     */
    synchronized void write(int level, String message) {
        var time = LocalDateTime.now();

        for (var info : this.targets.values()) {
            if (info.active)
                info.target.write(level, time, message);
        }
    }
}
