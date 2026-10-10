package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {

    private static class TestDocument extends Document {
        public TestDocument(DocumentCreationInfo<TestDocument> info) throws DVLoadException {
            super(info);
        }

        @Override
        protected void doCreateData() {
        }
    }

    private static class TestView implements View<TestDocument> {
        private final ViewTemplate<TestDocument, TestView> template;
        private final TestDocument document;
        private boolean titleUpdated = false;
        private boolean frontCalled = false;
        private boolean forceClosed = false;
        private Object lastChangedContext = null;

        public TestView(ViewCreationInfo<TestDocument, TestView> info) {
            this.template = info.getTemplate();
            this.document = info.getDocument();
        }

        @Override
        public ViewTemplate<TestDocument, ? extends View<TestDocument>> getTemplate() {
            return template;
        }

        @Override
        public TestDocument getDocument() {
            return document;
        }

        @Override
        public void updateTitle() {
            titleUpdated = true;
        }

        @Override
        public void documentDataChanged(Object context) {
            lastChangedContext = context;
        }

        @Override
        public void toFront() {
            frontCalled = true;
        }

        @Override
        public void forceClose() {
            forceClosed = true;
        }
    }

    @Test
    void testViewMethods() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NO_AUTO_VIEW
        );
        var doc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc.txt", false, true));
        var viewTemplate = new ViewTemplate<>(
                "ViewTpl",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        var view = new TestView(new ViewCreationInfo<>(viewTemplate, doc));
        assertSame(doc, view.getDocument());
        assertSame(viewTemplate, view.getTemplate());

        view.toFront();
        assertTrue(view.frontCalled);

        view.updateTitle();
        assertTrue(view.titleUpdated);

        view.documentDataChanged("ctx");
        assertEquals("ctx", view.lastChangedContext);

        view.forceClose();
        assertTrue(view.forceClosed);
    }

    @Test
    void testGetTitleFormatting() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NO_AUTO_VIEW
        );
        var doc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "/path/to/MyFile.txt", false, true));

        var viewTemplate = new ViewTemplate<>(
                "ViewTpl",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE,
                "%s*",
                "%s (R)"
        );

        var view1 = viewTemplate.createView(doc);
        assertEquals("MyFile.txt", view1.getTitle());

        doc.setModified(true);
        assertEquals("MyFile.txt*", view1.getTitle());

        var readOnlyDoc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "/path/to/ReadOnly.txt", true, false));
        var viewReadOnly = viewTemplate.createView(readOnlyDoc);
        assertEquals("ReadOnly.txt (R)", viewReadOnly.getTitle());
    }

    @Test
    void testGetTitleMultipleViewsNumbering() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NO_AUTO_VIEW
        );
        var doc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc.txt", false, true));
        var viewTemplate = new ViewTemplate<>(
                "ViewTpl",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        var view1 = viewTemplate.createView(doc);
        assertEquals("Doc.txt", view1.getTitle());

        var view2 = viewTemplate.createView(doc);
        assertEquals("Doc.txt:1", view1.getTitle());
        assertEquals("Doc.txt:2", view2.getTitle());
    }
}
