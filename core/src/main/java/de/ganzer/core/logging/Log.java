package de.ganzer.core.logging;

/**
 * A static global logger that writes messages into its targets.
 * <p>
 * This class encapsulates a single {@link Logger} instance to provide an
 * application global logger.
 * <p>
 * Log targets are inserted into the logger by {@link #addTarget(String, LogTarget)}
 * Log targets are inserted into the logger by {@link #addTarget(String, LogTarget)}
 * or {@link #addTarget(String, LogTarget, boolean)} and removed by
 * {@link #removeTarget(String)}.
 * <p>
 * A log message is written by write(). The message is pushed to all active
 * targets. A target can be activated or suspended by activateTarget().
 * <p>
 * All methods of this class are thread safe.
 *
 * @since 5.5.0
 */
public final class Log {
    private static final Logger logger = new Logger();

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
    public static void addTarget(String id, LogTarget target) {
        logger.addTarget(id, target);
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
     * @param inactive {@code true} to deactivate the target by default.
     *
     * @throws NullPointerException {@code id} or {@code target} is {@code null}.
     * @throws IllegalArgumentException {@code target} is already used by another
     *         logger instance or is added twice to this instance.
     *
     * @see #getTarget(String)
     */
    public static void addTarget(String id, LogTarget target, boolean inactive) {
        logger.addTarget(id, target, inactive);
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
    public static LogTarget removeTarget(String id) {
        return logger.removeTarget(id);
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
    public static LogTarget getTarget(String id) {
        return logger.getTarget(id);
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
    public static void activateTarget(String id, boolean activate) {
        logger.activateTarget(id, activate);
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
    public static boolean isTargetActive(String id) {
        return logger.isTargetActive(id);
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
    public static void write(int level, String message) {
        logger.write(level, message);
    }
}
