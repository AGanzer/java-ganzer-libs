package de.ganzer.swing.controls;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import java.util.Vector;

/**
 * The same as {@link JComboBox} except the {@link #getSelectedItem()} returns
 * an object of type {@code E}.
 *
 * @param <E> The type of the elements of this combo box.
 */
@SuppressWarnings("unused")
public class GComboBox<E> extends JComboBox<E> {
    /**
     * Creates a <code>JComboBox</code> with a default data model.
     * The default data model is an empty list of objects.
     * Use <code>addItem</code> to add items.  By default the first item
     * in the data model becomes selected.
     */
    public GComboBox() {
    }

    /**
     * Creates a <code>JComboBox</code> that takes its items from an
     * existing <code>ComboBoxModel</code>.  Since the
     * <code>ComboBoxModel</code> is provided, a combo box created using
     * this constructor does not create a default combo box model and
     * may impact how the insert, remove and add methods behave.
     *
     * @param aModel the <code>ComboBoxModel</code> that provides the
     *         displayed list of items
     */
    public GComboBox(ComboBoxModel<E> aModel) {
        super(aModel);
    }

    /**
     * Creates a <code>JComboBox</code> that contains the elements
     * in the specified array.  By default, the first item in the array
     * (and therefore the data model) becomes selected.
     *
     * @param items an array of objects to insert into the combo box
     */
    public GComboBox(E[] items) {
        super(items);
    }

    /**
     * Creates a <code>JComboBox</code> that contains the elements
     * in the specified Vector.  By default, the first item in the vector
     * (and therefore the data model) becomes selected.
     *
     * @param items an array of vectors to insert into the combo box
     */
    public GComboBox(Vector<E> items) {
        super(items);
    }

    /**
     * Returns the current selected item.
     * <p>
     * If the combo box is editable, then this value may not have been added
     * to the combo box with <code>addItem</code>, <code>insertItemAt</code>
     * or the data constructors.
     *
     * @return the current selected Object
     *
     * @see #setSelectedItem
     */
    @Override
    public E getSelectedItem() {
        //noinspection unchecked
        return (E) super.getSelectedItem();
    }
}
