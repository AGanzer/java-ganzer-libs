package de.ganzer.dv;

import de.ganzer.core.Services;
import de.ganzer.dv.services.DVNavigationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicDVManagerTest {

    private static class MemoryDocument extends Document {
        public MemoryDocument(DocumentCreationInfo<MemoryDocument> info) throws DVLoadException {
            super(info);
        }

        @Override
        protected void doCreateData() {
        }
    }

    private static class MemoryView implements View<MemoryDocument> {
        private final ViewTemplate<MemoryDocument, MemoryView> template;
        private final MemoryDocument document;
        private boolean toFrontCalled = false;
        private boolean forceClosed = false;

        public MemoryView(ViewCreationInfo<MemoryDocument, MemoryView> info) {
            this.template = info.getTemplate();
            this.document = info.getDocument();
        }

        @Override
        public ViewTemplate<MemoryDocument, ? extends View<MemoryDocument>> getTemplate() {
            return template;
        }

        @Override
        public MemoryDocument getDocument() {
            return document;
        }

        @Override
        public void updateTitle() {
        }

        @Override
        public void documentDataChanged(Object context) {
        }

        @Override
        public void toFront() {
            toFrontCalled = true;
            BasicDVManager.setActiveView(this);
        }

        @Override
        public void forceClose() {
            forceClosed = true;
        }
    }

    private static class MockNavigationService implements DVNavigationService {
        Collection<String> queryLocationsResult = List.of();
        Boolean querySaveResult = true;
        String querySaveLocationResult = "/memory/new.txt";

        @Override
        public Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter) {
            return queryLocationsResult;
        }

        @Override
        public Boolean querySave(String name) {
            return querySaveResult;
        }

        @Override
        public String querySaveLocation(String initialLocation, String filter) {
            return querySaveLocationResult;
        }

        @Override
        public void showError(String message, Throwable cause) {
        }

        @Override
        public DocumentTemplate<?> chooseDocumentTemplate(List<DocumentTemplate<?>> templates) {
            return templates.isEmpty() ? null : templates.get(0);
        }

        @Override
        public ViewTemplate<?, ?> chooseViewTemplate(List<ViewTemplate<?, ?>> templates) {
            return templates.isEmpty() ? null : templates.get(0);
        }
    }

    private MockNavigationService navService;

    @BeforeEach
    void setUp() {
        navService = new MockNavigationService();
        Services.register(DVNavigationService.class, navService);

        for (var doc : new ArrayList<>(BasicDVManager.getOpenDocuments())) {
            doc.close();
        }
        var manager = new BasicDVManager();
        for (var tpl : new ArrayList<>(BasicDVManager.getDocumentTemplates())) {
            manager.removeDocumentTemplate(tpl);
        }
        BasicDVManager.setActiveView(null);
    }

    @AfterEach
    void tearDown() {
        for (var doc : new ArrayList<>(BasicDVManager.getOpenDocuments())) {
            doc.close();
        }
        var manager = new BasicDVManager();
        for (var tpl : new ArrayList<>(BasicDVManager.getDocumentTemplates())) {
            manager.removeDocumentTemplate(tpl);
        }
        BasicDVManager.setActiveView(null);
    }

    @Test
    void testTemplateRegistrationAndRemoval() {
        assertEquals(0, BasicDVManager.getDocumentTemplates().size());

        var tpl1 = new DocumentTemplate<>(
                "Tpl1",
                s -> true,
                MemoryDocument::new,
                "Doc1",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var tpl2 = new DocumentTemplate<>(
                "Tpl2",
                s -> true,
                MemoryDocument::new,
                "Doc2",
                "*.xml",
                "xml",
                DocumentTemplate.NONE
        );

        BasicDVManager.registerDocumentTemplate(tpl1);
        BasicDVManager.registerDocumentTemplate(tpl2);

        assertEquals(2, BasicDVManager.getDocumentTemplates().size());
        assertTrue(BasicDVManager.getDocumentTemplates().contains(tpl1));
        assertTrue(BasicDVManager.getDocumentTemplates().contains(tpl2));

        var manager = new BasicDVManager();
        assertTrue(manager.removeDocumentTemplate(tpl2));
        assertEquals(1, BasicDVManager.getDocumentTemplates().size());
        assertFalse(manager.removeDocumentTemplate(tpl2));
    }

    @Test
    void testRegisterDuplicateDefaultTemplateThrows() {
        var tpl1 = new DocumentTemplate<>(
                "Tpl1",
                s -> true,
                MemoryDocument::new,
                "Doc1",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var tpl2 = new DocumentTemplate<>(
                "Tpl2",
                s -> true,
                MemoryDocument::new,
                "Doc2",
                "*.xml",
                "xml",
                DocumentTemplate.DEFAULT
        );

        BasicDVManager.registerDocumentTemplate(tpl1);
        assertThrows(IllegalArgumentException.class, () -> BasicDVManager.registerDocumentTemplate(tpl2));
    }

    @Test
    void testCreateDocument() {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var doc = BasicDVManager.createDocument(null);
        assertNotNull(doc);
        assertTrue(BasicDVManager.getOpenDocuments().contains(doc));
    }

    @Test
    void testCreateView() {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT | DocumentTemplate.NO_AUTO_VIEW
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var doc = BasicDVManager.createDocument(null);
        assertNotNull(doc);
        assertEquals(0, doc.getViews().size());

        var view = BasicDVManager.createView(doc);
        assertNotNull(view);
        assertEquals(1, doc.getViews().size());
    }

    @Test
    void testOpenDocument() throws Exception {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> s != null && s.endsWith(".txt"),
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var doc = BasicDVManager.openDocument(null, "/path/to/file.txt", false);
        assertNotNull(doc);
        assertEquals("/path/to/file.txt", doc.getName());
        assertTrue(BasicDVManager.getOpenDocuments().contains(doc));

        // Opening the same document path returns the existing instance and activates it
        var docSame = BasicDVManager.openDocument(null, "/path/to/file.txt", false);
        assertSame(doc, docSame);
    }

    @Test
    void testOpenDocumentsCollection() throws Exception {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var docs = BasicDVManager.openDocuments(null, List.of("/doc1.txt", "/doc2.txt"), false);
        assertEquals(2, docs.size());
        assertEquals(2, BasicDVManager.getOpenDocuments().size());
    }

    @Test
    void testActiveViewAndActiveDocument() throws Exception {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var events = new ArrayList<PropertyChangeEvent>();
        BasicDVManager.addPropertyChangeListener(events::add);

        var doc = BasicDVManager.openDocument(null, "/path/to/test.txt", false);
        var view = doc.getViews().get(0);

        BasicDVManager.setActiveView(view);
        assertSame(view, BasicDVManager.getActiveView());
        assertSame(doc, BasicDVManager.getActiveDocument());

        assertTrue(events.stream().anyMatch(e -> BasicDVManager.ACTIVE_VIEW_PROPERTY.equals(e.getPropertyName())));
        assertTrue(events.stream().anyMatch(e -> BasicDVManager.ACTIVE_DOCUMENT_PROPERTY.equals(e.getPropertyName())));

        BasicDVManager.activateDocument(doc);
        assertSame(doc, BasicDVManager.getActiveDocument());
    }

    @Test
    void testSaveActiveDocumentAndAllDocuments() throws Exception {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var doc1 = (MemoryDocument) BasicDVManager.openDocument(null, "/path/to/doc1.txt", false);
        var doc2 = (MemoryDocument) BasicDVManager.openDocument(null, "/path/to/doc2.txt", false);

        doc1.setModified(true);
        doc2.setModified(true);

        BasicDVManager.setActiveView(doc1.getViews().get(0));
        BasicDVManager.saveActiveDocument();
        assertFalse(doc1.isModified());
        assertTrue(doc2.isModified());

        BasicDVManager.saveAllDocuments();
        assertFalse(doc2.isModified());
    }

    @Test
    void testCanClose() throws Exception {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        var doc1 = (MemoryDocument) BasicDVManager.openDocument(null, "/path/to/doc1.txt", false);

        assertTrue(BasicDVManager.canClose());

        doc1.setModified(true);
        navService.querySaveResult = null; // User cancelled
        assertFalse(BasicDVManager.canClose());

        navService.querySaveResult = false; // User discarded changes
        assertTrue(BasicDVManager.canClose());
    }

    @Test
    void testOpenLocationsViaNavigationService() throws Exception {
        var tpl = new DocumentTemplate<>(
                "Tpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT
        );
        var viewTpl = new ViewTemplate<>(
                "ViewTpl",
                MemoryView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        tpl.registerViewTemplate(viewTpl);
        BasicDVManager.registerDocumentTemplate(tpl);

        navService.queryLocationsResult = List.of("/path/to/docA.txt", "/path/to/docB.txt");
        var docs = BasicDVManager.openDocuments(null, false);

        assertEquals(2, docs.size());
        assertEquals(2, BasicDVManager.getOpenDocuments().size());
    }
}
