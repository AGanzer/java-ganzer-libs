package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("resource")
class LoggerTest {
    private static class MyTarget extends LogTarget {
        public final List<LogInfo> logs = new ArrayList<>();

        public MyTarget() {
            super(1);
        }

        @Override
        protected void write(LogInfo[] info) {
            logs.add(info[0]);
        }
    }

    @Test
    void create() {
        var logger = new Logger();
        assertFalse(logger.isClosed());
    }

    @Test
    void addTarget() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target);

        assertEquals(target, logger.getTarget(targetID));
        assertEquals(target.getOwner(), logger);
        assertTrue(logger.isTargetActive(targetID));
    }

    @Test
    void addTargetTwice() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target);

        assertThrows(IllegalArgumentException.class, () -> logger.addTarget(targetID + "1", target));
    }

    @Test
    void addTargetActive() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target, false);

        assertTrue(logger.isTargetActive(targetID));
    }

    @Test
    void addTargetInactive() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target, true);

        assertFalse(logger.isTargetActive(targetID));
    }

    @Test
    void removeTarget() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target);
        logger.removeTarget(targetID);

        assertNull(logger.getTarget(targetID));
        assertNull(target.getOwner());
    }

    @Test
    void activateTarget() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target, true);
        logger.activateTarget(targetID, true);

        assertTrue(logger.isTargetActive(targetID));
    }

    @Test
    void deactivateTarget() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target, false);
        logger.activateTarget(targetID, false);

        assertFalse(logger.isTargetActive(targetID));
    }

    @Test
    void write() {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";
        var level1 = 0;
        var message1 = "m1";
        var level2 = 1;
        var message2 = "m2";

        logger.addTarget(targetID, target);
        logger.write(level1, message1);
        logger.write(level2, message2);

        assertEquals(2, target.logs.size());
        assertEquals(level1, target.logs.get(0).getLevel());
        assertEquals(message1, target.logs.get(0).getMessage());
        assertEquals(level2, target.logs.get(1).getLevel());
        assertEquals(message2, target.logs.get(1).getMessage());
    }

    @Test
    void close() throws Exception {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target);
        logger.close();

        assertTrue(logger.isClosed());
        assertTrue(target.isClosed());
        assertNull(target.getOwner());
    }

    @Test
    void closedAddTarget() throws Exception {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target);
        logger.close();

        assertThrows(IllegalStateException.class, () -> logger.addTarget(targetID, target));
    }

    @Test
    void closeWrite() throws Exception {
        var logger = new Logger();
        var target = new MyTarget();
        var targetID = "1";

        logger.addTarget(targetID, target);
        logger.close();

        assertThrows(IllegalStateException.class, () -> logger.write(0, ""));
    }
}