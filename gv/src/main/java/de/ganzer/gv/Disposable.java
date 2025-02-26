package de.ganzer.gv;

/**
 * This interface should be implemented by all classes that have to free
 * resources.
 */
public interface Disposable {
    /**
     * Called to free used resources.
     */
    void dispose();
}
