package de.ganzer.core.logging;

/**
 * A default implementation for a log filter.
 * <p>
 * An instance of this class is used by each target if no other filter function
 * is specified for that.
 */
public class DefaultLogFilter implements LogFilter {
    /**
     * Called to determine whether a message can be written into a target.
     *
     * @param messageLevel The level of the message to write.
     * @param targetLevel The level of the target where to write the message
     *         into.
     *
     * @return {@code true} to wirte the message into the target. This
     *         implementation returns {@code messageLevel <= targetLevel}.
     */
    @Override
    public boolean shouldWrite(int messageLevel, int targetLevel) {
        return messageLevel <= targetLevel;
    }
}
