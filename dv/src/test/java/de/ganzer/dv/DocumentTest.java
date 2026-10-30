package de.ganzer.dv;

import de.ganzer.core.Services;
import de.ganzer.dv.services.DVNavigationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    private static class MemoryDocument extends Document {
        private byte[] memoryData = new byte[0];
        public boolean childAddedCalled = false;
        public boolean childRemovedCalled = false;

        public MemoryDocument(DocumentCreationInfo<MemoryDocument> info) throws DVLoadException {
            super(info);
        }

        @Override
        protected void doCreateData() {
            this.memoryData = "InitialData".getBytes(StandardCharsets.UTF_8);
        }

        @Override
        protected InputStream createInputStream() {
            return new ByteArrayInputStream(memoryData != null ? memoryData : new byte[0]);
        }

        @Override
        protected OutputStream createOutputStream() {
            return new ByteArrayOutputStream() {
                @Override
                public void close() throws IOException {
                    super.close();
                    memoryData = toByteArray();
                }
            };
        }

        @Override
        protected void doLoadData() throws IOException {
            InputStream in = createInputStream();
            if (in != null) {
                memoryData = in.readAllBytes();
            }
        }

        @Override
        protected void doSaveData() throws IOException {
            OutputStream out = createOutputStream();
            if (out != null) {
                out.write(memoryData);
                out.close();
            }
        }

        public byte[] getMemoryData() {
            return memoryData;
        }

        public void setMemoryData(byte[] memoryData) {
            this.memoryData = memoryData;
            setModified(true);
        }

        @Override
        protected void childAdded(Document child) {
            childAddedCalled = true;
        }

        @Override
        protected void childRemoved(Document child) {
            childRemovedCalled = true;
        }

        public void exposedNotifyDataChange(View<?> originator, Object context) {
            notifyDataChange(originator, context);
        }
    }

    private static class TestView implements View<MemoryDocument> {
        private final ViewTemplate<MemoryDocument, TestView> template;
        private final MemoryDocument document;
        private boolean forceClosed = false;
        private Object dataChangedContext = null;
        private boolean titleUpdated = false;

        public TestView(ViewCreationInfo<MemoryDocument, TestView> info) {
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
            titleUpdated = true;
        }

        @Override
        public void documentDataChanged(Object context) {
            this.dataChangedContext = context;
        }

        @Override
        public void toFront() {
        }

        @Override
        public void forceClose() {
            forceClosed = true;
        }
    }

    private static class MockNavigationService implements DVNavigationService {
        Boolean querySaveResult = true;
        String querySaveLocationResult = "/memory/saved.txt";
        boolean errorShown = false;

        @Override
        public Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter) {
            return List.of();
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
            errorShown = true;
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
    }

    @Test
    void testDocumentCreationAndProperties() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        var info = new DocumentCreationInfo<>(docTemplate, null, "/path/to/SampleDocument.txt", false, false);
        var doc = new MemoryDocument(info);

        assertSame(docTemplate, doc.getTemplate());
        assertNull(doc.getParent());
        assertEquals("/path/to/SampleDocument.txt", doc.getName());
        assertEquals("SampleDocument.txt", doc.getTitle());
        assertFalse(doc.isClosed());
        assertFalse(doc.isModified());
    }

    @Test
    void testParentAndChildDocuments() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        var parentInfo = new DocumentCreationInfo<>(docTemplate, null, "ParentDoc", false, true);
        var parentDoc = new MemoryDocument(parentInfo);

        var childInfo = new DocumentCreationInfo<>(docTemplate, parentDoc, "ChildDoc", false, true);
        var childDoc = new MemoryDocument(childInfo);

        assertSame(parentDoc, childDoc.getParent());
        assertEquals(1, parentDoc.getChildren().size());
        assertSame(childDoc, parentDoc.getChildren().get(0));
        assertTrue(parentDoc.childAddedCalled);

        parentDoc.close();
        assertTrue(parentDoc.isClosed());
        assertTrue(childDoc.isClosed());
    }

    @Test
    void testViewsManagement() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new MemoryDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc", false, true));

        var viewTemplate = new ViewTemplate<>(
                "ViewTpl",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        var view = viewTemplate.createView(doc);
        assertEquals(1, doc.getViews().size());
        assertSame(view, doc.getViews().get(0));

        doc.removeView(view);
        assertEquals(0, doc.getViews().size());
    }

    @Test
    void testCanCloseView() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new MemoryDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc", false, true));

        var viewTemplate1 = new ViewTemplate<>("View1", TestView::new, v -> {
        }, ViewTemplate.NONE);
        var viewTemplate2 = new ViewTemplate<>("View2", TestView::new, v -> {
        }, ViewTemplate.NONE);

        var view1 = viewTemplate1.createView(doc);
        var view2 = viewTemplate2.createView(doc);

        assertTrue(doc.canCloseView(view1));
        assertTrue(doc.canCloseView(view2));
    }

    @Test
    void testSaveDataAndSaveDataAs() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new MemoryDocument(new DocumentCreationInfo<>(docTemplate, null, "NewDoc", false, true));

        doc.setMemoryData("SavedData".getBytes(StandardCharsets.UTF_8));
        assertTrue(doc.isModified());

        navService.querySaveLocationResult = "/memory/myNewSave.txt";
        doc.saveData();

        assertEquals("/memory/myNewSave.txt", doc.getName());
        assertFalse(doc.isModified());
        assertFalse(doc.isNewData());
    }

    @Test
    void testCanCloseWhenModifiedAndQuerySaveUserResponses() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new MemoryDocument(new DocumentCreationInfo<>(docTemplate, null, "/path/doc.txt", false, false));

        assertTrue(doc.canClose());

        doc.setModified(true);
        navService.querySaveResult = null; // User clicked Cancel
        assertFalse(doc.canClose());
        assertTrue(doc.isModified());

        navService.querySaveResult = false; // User clicked Discard / Don't Save
        assertTrue(doc.canClose());

        navService.querySaveResult = true; // User clicked Save
        assertTrue(doc.canClose());
        assertFalse(doc.isModified());
    }

    @Test
    void testCloseClosesAllViews() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new MemoryDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc", false, true));

        var viewTemplate = new ViewTemplate<>("View", TestView::new, v -> {
        }, ViewTemplate.NONE);
        var view = viewTemplate.createView(doc);

        doc.close();

        assertTrue(doc.isClosed());
        assertTrue(view.forceClosed);
    }

    @Test
    void testNotifyDataChange() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                MemoryDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new MemoryDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc", false, true));

        var viewTemplate = new ViewTemplate<>("View", TestView::new, v -> {
        }, ViewTemplate.NONE);
        var view1 = viewTemplate.createView(doc);
        var view2 = viewTemplate.createView(doc);

        doc.exposedNotifyDataChange(view1, "custom-context");
        assertNull(view1.dataChangedContext); // Originator is not notified
        assertEquals("custom-context", view2.dataChangedContext);
    }
}
