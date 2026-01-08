package de.ganzer.core.logging;

/**
 * Interface to a filter function that is used by {@link LogTarget} to filter
 * its messages.
 * <p>
 * Logged messages are filtered before they are written into the physical
 * target. A function that filters the message must implement this interface.
 */
public interface LogFilter {
    /**
     * Called to determine whether a message can be written into a target.
     *
     * @param messageLevel The level of the message to write.
     * @param targetLevel The level of the target where to write the message
     *        into.
     *
     * @return {@code true} to wirte the message into the target.
     */
    boolean shouldWrite(int messageLevel, int targetLevel);
}
