package de.ganzer.core;

/**
 * A class for operating system specific queries.
 *
 * @since 5.3.0
 */
@SuppressWarnings("unused")
public final class OS {
    /**
     * The operating system types.
     */
    public enum Type {
        /**
         * Any Windows.
         */
        WINDOWS,
        /**
         * Any Linux.
         */
        LINUX,
        /**
         * Any Mac.
         */
        MAC,
        /**
         * Sun Solaris.
         */
        SOLARIS,
        /**
         * AIX.
         */
        AIX,
        /**
         * FreeBSD
         */
        FREE_BSD,
        /**
         * OpenBSD
         */
        OPEN_BSD,
        /**
         * HP-UX
         */
        HP_UX,
        /**
         * Any unknown.
         */
        OTHER
    }

    /**
     * Gets the operating system where the application runs on.
     *
     * @return The operating system.
     */
    public static Type getType() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("windows"))
            return Type.WINDOWS;

        if (osName.contains("linux"))
            return Type.LINUX;

        if (osName.contains("sunos"))
            return Type.SOLARIS;

        if (osName.contains("mac"))
            return Type.MAC;

        if (osName.contains("aix"))
            return Type.AIX;

        if (osName.contains("freebsd"))
            return Type.FREE_BSD;

        if (osName.contains("openbsd"))
            return Type.OPEN_BSD;

        if (osName.contains("hp-ux"))
            return Type.HP_UX;

        return Type.OTHER;
    }

    /**
     * Gets the full name of the operating system where the application runs on.
     *
     * @return The full name of the operating system.
     */
    public static String getName() {
        return System.getProperty("os.name");
    }

    /**
     * Convenience function fo {@code getType() == Type.WINDOWS}.
     *
     * @return {@code true} if the operating systen where the application is
     *         running on is a Windows operating system.
     */
    public static boolean isWindows() {
        return getType() == Type.WINDOWS;
    }

    /**
     * Convenience function fo {@code getType() == Type.LINUX}.
     *
     * @return {@code true} if the operating systen where the application is
     *         running on is a Linux operating system.
     */
    public static boolean isLinux() {
        return getType() == Type.LINUX;
    }

    /**
     * Convenience function fo {@code getType() == Type.MAC}.
     *
     * @return {@code true} if the operating systen where the application is
     *         running on is a Mac operating system.
     */
    public static boolean isMac() {
        return getType() == Type.MAC;
    }

    /**
     * Convenience function fo {@code getType() == Type.SOLARIS}.
     *
     * @return {@code true} if the operating systen where the application is
     *         running on is a Solaris operating system.
     */
    public static boolean isSolaris() {
        return getType() == Type.SOLARIS;
    }

    /**
     * Convenience function fo {@code getType() == Type.OTHER}.
     *
     * @return {@code true} if the operating systen where the application is
     *         running on is an unknown operating system.
     */
    public static boolean isOther() {
        return getType() == Type.OTHER;
    }
}
