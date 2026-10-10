package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewCreationInfoTest {

    private static class TestDocument extends Document {
        protected TestDocument(DocumentCreationInfo<TestDocument> info) throws DVLoadException {
            super(info);
        }

        @Override
        protected void doCreateData() {
        }
    }

    private static class TestView implements View<TestDocument> {
        private final ViewTemplate<TestDocument, TestView> template;
        private final TestDocument document;

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
        }

        @Override
        public void documentDataChanged(Object context) {
        }

        @Override
        public void toFront() {
        }

        @Override
        public void forceClose() {
        }
    }

    @Test
    void testConstructorsAndGetters() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "testDoc",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc", false, true));
        var viewTemplate = new ViewTemplate<>(
                "testView",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        var info = new ViewCreationInfo<>(viewTemplate, doc);
        assertSame(viewTemplate, info.getTemplate());
        assertSame(doc, info.getDocument());
    }

    @Test
    void testNullArgumentsThrowException() throws Exception {
        var docTemplate = new DocumentTemplate<>(
                "testDoc",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var doc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc", false, true));
        var viewTemplate = new ViewTemplate<>(
                "testView",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        assertThrows(NullPointerException.class, () -> new ViewCreationInfo<>(null, doc));
        assertThrows(NullPointerException.class, () -> new ViewCreationInfo<>(viewTemplate, null));
    }
}
