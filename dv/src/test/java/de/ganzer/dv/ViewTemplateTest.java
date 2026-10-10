package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ViewTemplateTest {

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
        private boolean forceClosed = false;

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
        }

        @Override
        public void toFront() {
        }

        @Override
        public void forceClose() {
            forceClosed = true;
        }
    }

    @Test
    void testConstructorsAndOptionGetters() {
        var template = new ViewTemplate<>(
                "DefaultView",
                TestView::new,
                v -> {
                },
                ViewTemplate.DEFAULT | ViewTemplate.MANDATORY | ViewTemplate.NOT_CLOSABLE
                        | ViewTemplate.NO_TITLE_NUMBER | ViewTemplate.NO_READ_ONLY_HINT
                        | ViewTemplate.NO_MODIFICATION_HINT | ViewTemplate.IGNORE_ON_OTHER_VIEW_CLOSED
                        | ViewTemplate.AUTO_VIEW,
                "%s modified",
                "%s readonly"
        );

        assertEquals("DefaultView", template.getDisplayName());
        assertEquals("DefaultView", template.toString());
        assertFalse(template.isHidden());
        assertTrue(template.isDefault());
        assertTrue(template.isMandatory());
        assertFalse(template.isClosable());
        assertFalse(template.hasTitleNumber());
        assertFalse(template.showReadOnlyHint());
        assertFalse(template.showModificationHint());
        assertTrue(template.ignoreOnOtherViewClosed());
        assertTrue(template.isAutoView());
        assertEquals("%s modified", template.getModificationHintFormat());
        assertEquals("%s readonly", template.getReadOnlyHintFormat());
    }

    @Test
    void testDefaultOptions() {
        var template = new ViewTemplate<>(
                "StandardView",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        assertEquals("StandardView", template.getDisplayName());
        assertFalse(template.isHidden());
        assertFalse(template.isDefault());
        assertFalse(template.isMandatory());
        assertTrue(template.isClosable());
        assertTrue(template.hasTitleNumber());
        assertTrue(template.showReadOnlyHint());
        assertTrue(template.showModificationHint());
        assertFalse(template.ignoreOnOtherViewClosed());
        assertFalse(template.isAutoView());
        assertEquals("%s*", template.getModificationHintFormat());
        assertEquals("%s (R)", template.getReadOnlyHintFormat());
    }

    @Test
    void testCreateView() throws Exception {
        var shown = new AtomicBoolean(false);
        var viewTemplate = new ViewTemplate<>(
                "StandardView",
                TestView::new,
                v -> shown.set(true),
                ViewTemplate.NONE
        );

        var docTemplate = new DocumentTemplate<>(
                "DocTpl",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NO_AUTO_VIEW
        );
        var doc = new TestDocument(new DocumentCreationInfo<>(docTemplate, null, "Doc1", false, true));

        var view = viewTemplate.createView(doc);
        assertNotNull(view);
        assertSame(doc, view.getDocument());
        assertSame(viewTemplate, view.getTemplate());
        assertTrue(doc.getViews().contains(view));
        assertTrue(shown.get());
        assertTrue(view.titleUpdated);
    }

    @Test
    void testNullArgumentsThrowException() {
        assertThrows(NullPointerException.class,
                () -> new ViewTemplate<>(null, TestView::new, v -> {
                }, ViewTemplate.NONE));
        assertThrows(NullPointerException.class,
                () -> new ViewTemplate<TestDocument, TestView>("View", null, v -> {}, ViewTemplate.NONE));
        assertThrows(NullPointerException.class,
                () -> new ViewTemplate<>("View", TestView::new, null, ViewTemplate.NONE));
    }
}
