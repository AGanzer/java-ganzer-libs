package com.example.uitests.swingdv.doc.ol;

import de.ganzer.core.util.Strings;

import java.util.*;

public final class OLSystem implements Comparable<OLSystem> {
    private static final List<OLSystem> predefinedSystems = new ArrayList<>();

    private final boolean predefined;

    private String name;
    private String axiom;
    private int angle;
    private int preferredCycles;
    private List<String> replacements;

    public OLSystem(String name, String axiom, int angle, int numCycles, String... replacements) {
        this(false, name, axiom, angle, numCycles, replacements);
    }

    private OLSystem(boolean predefined, String name, String axiom, int angle, int preferredCycles, String... replacements) {
        if (angle < 0)
            throw new IllegalArgumentException("angle must be greater than or equal to 0.");

        if (preferredCycles < 1)
            throw new IllegalArgumentException("preferredCycles must be greater than 0.");

        this.predefined = predefined;
        this.name = name;
        this.axiom = axiom;
        this.angle = angle;
        this.preferredCycles = preferredCycles;
        this.replacements = replacements != null ? List.of(replacements) : List.of();
    }

    public boolean isPredefined() {
        return predefined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (predefined)
            throw new IllegalStateException("Cannot change predefined OLSystem.");

        this.name = name;
    }

    public String getAxiom() {
        return axiom;
    }

    public void setAxiom(String axiom) {
        if (predefined)
            throw new IllegalStateException("Cannot change predefined OLSystem.");

        this.axiom = axiom;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        if (predefined)
            throw new IllegalStateException("Cannot change predefined OLSystem.");

        this.angle = angle;
    }

    public int getPreferredCycles() {
        return preferredCycles;
    }

    public void setPreferredCycles(int preferredCycles) {
        if (preferredCycles < 1)
            throw new IllegalArgumentException("preferredCycles must be greater than 0.");

        if (predefined)
            throw new IllegalStateException("Cannot change predefined OLSystem.");

        this.preferredCycles = preferredCycles;
    }

    public List<String> getReplacements() {
        return replacements;
    }

    public void setReplacements(String... replacements) {
        this.replacements = replacements != null ? List.of(replacements) : List.of();
    }

    public String createFigure(int cycles) {
        if (cycles < 1)
            throw new IllegalArgumentException("cycles must be greater than 0.");

        if (Strings.isNullOrEmpty(axiom))
            throw new IllegalStateException("axiom must not be null or empty.");

        var replacementsMap = getReplacementsMap();

        String result = axiom;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < cycles; i++) {
            for (int c = 0; c < result.length(); c++) {
                var r = replacementsMap.get(result.charAt(c));

                if (r != null)
                    current.append(r);
                else
                    current.append(result.charAt(c));
            }

            result = current.toString();
            current.setLength(0);
        }

        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OLSystem olSystem))
            return false;

        return Objects.equals(name, olSystem.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(OLSystem o) {
        return name.compareTo(o.name);
    }

    public static List<OLSystem> getPredefinedSystems() {
        if (predefinedSystems.isEmpty()) {
            predefinedSystems.add(new OLSystem(true,
                                               "Koch Curve",
                                               "F",
                                               60,
                                               4,
                                               "F:F-F++F-F"));
            predefinedSystems.add(new OLSystem(true,
                                               "Koch Curve Variation 1",
                                               "F-F-F",
                                               60,
                                               4,
                                               "F:F-F++F-F"));
            predefinedSystems.add(new OLSystem(true,
                                               "Koch Curve Variation 2",
                                               "F+F+F+F",
                                               90,
                                               4,
                                               "F:FF+F+F+F+F+F-F"));
            predefinedSystems.add(new OLSystem(true,
                                               "Koch Curve Variation 3",
                                               "F+F+F+F",
                                               90,
                                               4,
                                               "F:FF+F+F+F+FF"));
            predefinedSystems.add(new OLSystem(true,
                                               "Koch Curve Variation 4",
                                               "F+F+F+F",
                                               90,
                                               4,
                                               "F:FF+F++F+F"));
            predefinedSystems.add(new OLSystem(true,
                                               "Koch Curve Variation 5",
                                               "F+F+F+F",
                                               90,
                                               4,
                                               "F:F+FF++F+F"));
            predefinedSystems.add(new OLSystem(true,
                                               "Kochean Snowflake",
                                               "F++F++F",
                                               60,
                                               4,
                                               "F:F-F++F-F"));
            predefinedSystems.add(new OLSystem(true,
                                               "Drake Curve",
                                               "Fl",
                                               90,
                                               11,
                                               "l:l+rF+", "r:-Fl-r"));
            predefinedSystems.add(new OLSystem(true,
                                               "Sierpinsky Triangle",
                                               "FXF-FF-FF",
                                               120,
                                               6,
                                               "F:FF", "X:-FXF+FXF+FXF-"));
            predefinedSystems.add(new OLSystem(true,
                                               "World of Islands",
                                               "F-F-F-F",
                                               90,
                                               2,
                                               "f:ffffff" , "F:F-F+FF-F-FF-FF-FF+F-FF+F+FF+Ff+FFF"));
            predefinedSystems.add(new OLSystem(true,
                                               "Peano Curve",
                                               "X",
                                               90,
                                               4,
                                               "X:XFYFX+F+YFXFY-F-XFYFX", "Y:YFXFY-F-XFYFX+F+YFXFY"));
            predefinedSystems.add(new OLSystem(true,
                                               "Hilbert Curve",
                                               "X",
                                               90,
                                               6,
                                               "X:-YF+XFX+FY-", "Y:+XF-YFY-FX+"));
            predefinedSystems.add(new OLSystem(true,
                                               "Gosper Curve",
                                               "XF",
                                               60,
                                               4,
                                               "X:X+YF++YF-FX--FXFX-YF+", "Y:-FX+YFYF++YF+FX--FX-Y"));

            Collections.sort(predefinedSystems);
        }

        return predefinedSystems;
    }

    private Map<Character, String> getReplacementsMap() {
        if (replacements.isEmpty())
            throw new IllegalStateException("replacements must not be emoty.");

        var map = new HashMap<Character, String>();

        for (String replacement : replacements) {
            String[] parts = replacement.split(":");

            if (parts.length != 2)
                throw new IllegalArgumentException("Invalid replacement format: " + replacement);

            if (parts[0].trim().length() != 1)
                throw new IllegalArgumentException("Invalid replacement format: " + replacement);

            map.put(parts[0].trim().charAt(0), parts[1].trim());
        }

        return map;
    }
}
