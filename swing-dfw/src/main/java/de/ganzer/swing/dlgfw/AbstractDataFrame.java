package de.ganzer.swing.dlgfw;

import de.ganzer.swing.dialogs.DataSupport;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

/**
 * The basic frame for all non-modal dialogs that accept external data.
 * <p>
 * This frame invokes {@link Initializer<Data>#initControls} on the center
 * panel (as well on the button panel if any is set) if the panel implements
 * {@link Initializer<Data>}.
 *
 * @param <Data> The type of the accepted data.
 */
public abstract class AbstractDataFrame<Data> extends AbstractFrame implements DataSupport<Data>, Initializer<Data> {
    private Data data;

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame() throws HeadlessException {
    }

    /**
     * Creates a <code>Frame</code> in the specified
     * <code>GraphicsConfiguration</code> of
     * a screen device and a blank title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>Frame</code>;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    /**
     * Creates a new, initially invisible <code>Frame</code> with the
     * specified title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title for the frame
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(String title) throws HeadlessException {
        super(title);
    }

    /**
     * Creates a <code>JFrame</code> with the specified title and the
     * specified <code>GraphicsConfiguration</code> of a screen device.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title to be displayed in the
     *         frame's border. A <code>null</code> value is treated as
     *         an empty string, "".
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>JFrame</code> with;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }

    /**
     * Constructs a new frame that is initially invisible.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(Data data) throws HeadlessException {
        if (data != null)
            initControls(data);
    }

    /**
     * Creates a <code>Frame</code> in the specified
     * <code>GraphicsConfiguration</code> of
     * a screen device and a blank title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>Frame</code>;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(GraphicsConfiguration gc, Data data) {
        super(gc);

        if (data != null)
            initControls(data);
    }

    /**
     * Creates a new, initially invisible <code>Frame</code> with the
     * specified title.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title for the frame
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *         returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see Component#setSize
     * @see Component#setVisible
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(String title, Data data) throws HeadlessException {
        super(title);

        if (data != null)
            initControls(data);
    }

    /**
     * Creates a <code>JFrame</code> with the specified title and the
     * specified <code>GraphicsConfiguration</code> of a screen device.
     * <p>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param title the title to be displayed in the
     *         frame's border. A <code>null</code> value is treated as
     *         an empty string, "".
     * @param gc the <code>GraphicsConfiguration</code> that is used
     *         to construct the new <code>JFrame</code> with;
     *         if <code>gc</code> is <code>null</code>, the system
     *         default <code>GraphicsConfiguration</code> is assumed
     * @param data The data to set. This may be null if the controls shall
     *         be initialized by a later call to {@link #initControls}.
     *
     * @throws IllegalArgumentException if <code>gc</code> is not from
     *         a screen device.  This exception is always thrown when
     *         GraphicsEnvironment.isHeadless() returns true.
     *
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     */
    public AbstractDataFrame(String title, GraphicsConfiguration gc, Data data) {
        super(title, gc);

        if (data != null)
            initControls(data);
    }

    /**
     * Gets the data the frame was initialized with.
     *
     * @return The data or {@code null} if no data is set.
     *
     * @see #initControls
     */
    @Override
    public Data getData() {
        return data;
    }

     /**
      * Called to initialize the controls from the specified data.
      * <p>
      * This implementation invokes {@link Initializer<Data>#initControls} on
      * the center panel (as well on the button panel if any is set) if the
      * panel implements {@link Initializer<Data>}.
      *
      * @param data The data where to initialize the controls with. This may be
      *         {@code null} if the controls shall be reset.
      */
    @SuppressWarnings("unchecked")
    @Override
    public void initControls(Data data) {
        this.data = data;

        if (getCenterPanel() instanceof Initializer)
            ((Initializer<Data>) getCenterPanel()).initControls(data);

        if (getButtonPanel() instanceof Initializer)
            ((Initializer<Data>) getButtonPanel()).initControls(data);
    }
}
