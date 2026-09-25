package de.ganzer.core.dv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A singleton document-view-manager.
 *
 * @since 5.6.0
 */
public class DVManager {
    private static final List<DocumentTemplate<?>> templates = new ArrayList<>();

    /**
     * Adds a document template to the manager.
     *
     * @param template The template to add.
     */
    public static void registerDocumentTemplate(DocumentTemplate<?> template) {
        templates.add(template);
    }

    /**
     * Gets a list of all registered document templates.
     *
     * @return An unmodifiable list of registered document templates.
     *
     * @see #registerDocumentTemplate(DocumentTemplate)
     */
    public static List<DocumentTemplate<?>> getDocumentTemplates() {
        return Collections.unmodifiableList(templates);
    }
}
