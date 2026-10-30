package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTemplateTest {

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
    void testConstructorsAndOptionGetters() {
        var template = new DocumentTemplate<>(
                "TextDoc",
                s -> s != null && s.endsWith(".txt"),
                TestDocument::new,
                "NewDocument",
                "*.txt",
                "txt",
                DocumentTemplate.DEFAULT | DocumentTemplate.HIDDEN | DocumentTemplate.NO_AUTO_VIEW
                        | DocumentTemplate.NO_AUTO_CLOSE | DocumentTemplate.NO_NEW_NUMBER
                        | DocumentTemplate.READ_ONLY_DOCUMENTS | DocumentTemplate.USE_PARENT_VIEWS
                        | DocumentTemplate.NOTIFY_CHILD_VIEWS_ON_CHANGE,
                "%s #%d"
        );

        assertEquals("TextDoc", template.getDisplayName());
        assertEquals("TextDoc", template.toString());
        assertEquals("*.txt", template.getFilter());
        assertEquals("txt", template.getDefaultExtension());
        assertTrue(template.isDefault());
        assertTrue(template.isHidden());
        assertFalse(template.autoCreateView());
        assertFalse(template.isAutoClose());
        assertFalse(template.hasNewNumber());
        assertTrue(template.documentsAreReadOnly());
        assertTrue(template.useParentViews());
        assertTrue(template.notifyChildViewsOnChange());
        assertTrue(template.canHandleDataSource("file.txt"));
        assertFalse(template.canHandleDataSource("file.pdf"));
    }

    @Test
    void testDefaultOptionFlags() {
        var template = new DocumentTemplate<>(
                "TextDoc",
                s -> true,
                TestDocument::new,
                "NewDocument",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        assertFalse(template.isDefault());
        assertFalse(template.isHidden());
        assertTrue(template.autoCreateView());
        assertTrue(template.isAutoClose());
        assertTrue(template.hasNewNumber());
        assertFalse(template.documentsAreReadOnly());
        assertFalse(template.useParentViews());
        assertFalse(template.notifyChildViewsOnChange());
    }

    @Test
    void testRegisterAndRemoveViewTemplates() {
        var docTemplate = new DocumentTemplate<>(
                "TextDoc",
                s -> true,
                TestDocument::new,
                "NewDocument",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        var viewTemplate1 = new ViewTemplate<>(
                "View1",
                TestView::new,
                v -> {
                },
                ViewTemplate.DEFAULT
        );
        var viewTemplate2 = new ViewTemplate<>(
                "View2",
                TestView::new,
                v -> {
                },
                ViewTemplate.NONE
        );

        assertEquals(0, docTemplate.getViewTemplates().size());
        docTemplate.registerViewTemplate(viewTemplate1);
        docTemplate.registerViewTemplate(viewTemplate2);

        assertEquals(2, docTemplate.getViewTemplates().size());
        assertTrue(docTemplate.getViewTemplates().contains(viewTemplate1));
        assertTrue(docTemplate.getViewTemplates().contains(viewTemplate2));

        assertTrue(docTemplate.removeViewTemplate(viewTemplate2));
        assertEquals(1, docTemplate.getViewTemplates().size());
        assertFalse(docTemplate.removeViewTemplate(viewTemplate2));
    }

    @Test
    void testDuplicateDefaultOrMandatoryViewTemplateThrows() {
        var docTemplate = new DocumentTemplate<>(
                "TextDoc",
                s -> true,
                TestDocument::new,
                "NewDocument",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        var default1 = new ViewTemplate<>("Def1", TestView::new, v -> {
        }, ViewTemplate.DEFAULT);
        var default2 = new ViewTemplate<>("Def2", TestView::new, v -> {
        }, ViewTemplate.DEFAULT);
        docTemplate.registerViewTemplate(default1);
        assertThrows(IllegalArgumentException.class, () -> docTemplate.registerViewTemplate(default2));

        var mandatory1 = new ViewTemplate<>("Man1", TestView::new, v -> {
        }, ViewTemplate.MANDATORY);
        var mandatory2 = new ViewTemplate<>("Man2", TestView::new, v -> {
        }, ViewTemplate.MANDATORY);
        docTemplate.registerViewTemplate(mandatory1);
        assertThrows(IllegalArgumentException.class, () -> docTemplate.registerViewTemplate(mandatory2));
    }

    @Test
    void testCreateDocumentWithViews() {
        var docTemplate = new DocumentTemplate<>(
                "TextDoc",
                s -> true,
                TestDocument::new,
                "NewDocument",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );
        var viewTemplate = new ViewTemplate<>("View", TestView::new, v -> {
        }, ViewTemplate.DEFAULT);
        docTemplate.registerViewTemplate(viewTemplate);

        var doc = docTemplate.createDocument(null);
        assertNotNull(doc);
        assertTrue(doc.isNewData());
        assertEquals(1, doc.getViews().size());
        assertSame(viewTemplate, doc.getViews().get(0).getTemplate());
    }

    @Test
    void testCreateDocumentThrowsWhenNoViewTemplates() {
        var docTemplate = new DocumentTemplate<>(
                "TextDoc",
                s -> true,
                TestDocument::new,
                "NewDocument",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        assertThrows(IllegalStateException.class, () -> docTemplate.createDocument(null));
    }

    @Test
    void testNullArgumentsThrowException() {
        assertThrows(NullPointerException.class,
                () -> new DocumentTemplate<>(null, s -> true, TestDocument::new, "Doc", "*.txt", "txt", 0));
        assertThrows(NullPointerException.class,
                () -> new DocumentTemplate<>("Name", null, TestDocument::new, "Doc", "*.txt", "txt", 0));
        assertThrows(NullPointerException.class,
                () -> new DocumentTemplate<TestDocument>("Name", s -> true, null, "Doc", "*.txt", "txt", 0));
        assertThrows(NullPointerException.class,
                () -> new DocumentTemplate<>("Name", s -> true, TestDocument::new, null, "*.txt", "txt", 0));
    }
}
