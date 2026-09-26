package de.ganzer.dv;

import java.util.function.Consumer;

/**
 * This interface is used to provide additional functionality to the DVManager.
 */
public interface DVManagerSupport {
    /**
     * Installs a consumer that has to be notified if the active view has changed.
     *
     * @param consumer The consumer to be notified.
     */
    void setActiveViewChangedListener(Consumer<View<?>> consumer);
}
