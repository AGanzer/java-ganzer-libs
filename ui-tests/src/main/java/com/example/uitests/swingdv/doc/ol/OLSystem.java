package com.example.uitests.swingdv.doc.ol;

import java.util.*;

public final class OLSystem {
    private static final List<OLSystem> predefinedSystems = new ArrayList<>();

    private final boolean predefined;
    private final String name;
    private final String axiom;
    private final int angle;
    private final int preferredCycles;
    private final Map<Character, String> replacements;

    public OLSystem(String name, String axiom, int angle, List<String> replacements) {
        this(false, name, axiom, angle, 2, replacements.toArray(new String[0]));
    }

    public OLSystem(String name, String axiom, int angle, String... replacements) {
        this(false, name, axiom, angle, 2, replacements);
    }

    private OLSystem(boolean predefined, String name, String axiom, int angle, int preferredCycles, String... replacements) {
        Objects.requireNonNull(name, "name must not be null.");
        Objects.requireNonNull(axiom, "axiom must not be null.");

        this.predefined = predefined;
        this.name = name;
        this.axiom = axiom;
        this.angle = angle;
        this.preferredCycles = preferredCycles;
        this.replacements = toReplacementsMap(replacements);
    }

    public boolean isPredefined() {
        return predefined;
    }

    public String getName() {
        return name;
    }

    public String getAxiom() {
        return axiom;
    }

    public int getAngle() {
        return angle;
    }

    public int getPreferredCycles() {
        return preferredCycles;
    }

    public Map<Character, String> getReplacementsMap() {
        return Collections.unmodifiableMap(replacements);
    }

    public List<String> getReplacementsList() {
        List<String> list = new ArrayList<>();

        for (var entry : replacements.entrySet())
            list.add(entry.getKey() + ":" + entry.getValue());

        return list;
    }

    public String createFigure(int cycles) {
        String result = axiom;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < cycles; i++) {
            for (int c = 0; c < result.length(); c++) {
                var r = replacements.get(result.charAt(c));

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

    public static List<OLSystem> getPredefinedSystems() {
        if (predefinedSystems.isEmpty()) {
            predefinedSystems.add(new OLSystem(true,
                                               "Koch curve",
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
        }

        return predefinedSystems;
    }

    private static Map<Character, String> toReplacementsMap(String... replacements) {
        Objects.requireNonNull(replacements, "replacements must not be null.");

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
