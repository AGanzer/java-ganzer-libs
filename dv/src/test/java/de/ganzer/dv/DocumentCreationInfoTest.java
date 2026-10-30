package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentCreationInfoTest {

    private static class TestDocument extends Document {
        protected TestDocument(DocumentCreationInfo<TestDocument> info) throws DVLoadException {
            super(info);
        }

        @Override
        protected void doCreateData() {
        }
    }

    @Test
    void testConstructorsAndGetters() throws Exception {
        var template = new DocumentTemplate<>(
                "test",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        var parentInfo = new DocumentCreationInfo<>(template, null, "ParentDoc", false, true);
        var parentDoc = new TestDocument(parentInfo);

        var info1 = new DocumentCreationInfo<>(template, parentDoc, "ChildDoc", true, true);
        assertSame(template, info1.getTemplate());
        assertSame(parentDoc, info1.getParent());
        assertEquals("ChildDoc", info1.getName());
        assertTrue(info1.isReadOnly());
        assertTrue(info1.isNewData());

        var info2 = new DocumentCreationInfo<>(template, null, "ExistingDoc", false, false);
        assertSame(template, info2.getTemplate());
        assertNull(info2.getParent());
        assertEquals("ExistingDoc", info2.getName());
        assertFalse(info2.isReadOnly());
        assertFalse(info2.isNewData());
    }

    @Test
    void testNullTemplateThrowsException() {
        assertThrows(NullPointerException.class,
                () -> new DocumentCreationInfo<>(null, null, "Doc", false, true));
    }

    @Test
    void testBlankNameWithNewDataThrowsException() {
        var template = new DocumentTemplate<>(
                "test",
                s -> true,
                TestDocument::new,
                "NewDoc",
                "*.txt",
                "txt",
                DocumentTemplate.NONE
        );

        assertThrows(IllegalArgumentException.class,
                () -> new DocumentCreationInfo<>(template, null, null, false, true));
        assertThrows(IllegalArgumentException.class,
                () -> new DocumentCreationInfo<>(template, null, "", false, true));
        assertThrows(IllegalArgumentException.class,
                () -> new DocumentCreationInfo<>(template, null, "   ", false, true));
    }
}
