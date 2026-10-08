package com.example.uitests.swingdv.doc.ol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class OLSystem {
    private static final List<OLSystem> predefinedSystems = new ArrayList<>();

    private final boolean predefined;
    private final String name;
    private final String axiom;
    private final int angle;
    private final int preferredCycles;
    private final Map<String, String> replacements;

    public OLSystem(String name, String axiom, int angle, int preferredCycles, List<String> replacements) {
        this(false, name, axiom, angle, preferredCycles, replacements.toArray(new String[0]));
    }

    public OLSystem(String name, String axiom, int angle, int preferredCycles, String... replacements) {
        this(false, name, axiom, angle, preferredCycles, replacements);
    }

    private OLSystem(boolean predefined, String name, String axiom, int angle, int preferredCycles, String... replacements) {
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

    public Map<String, String> getReplacementsMap() {
        return replacements;
    }

    public List<String> getReplacementsList() {
        return null;
    }

    public String createFigure() {
        return null;
    }

    public static List<OLSystem> getPredefinedSystems() {
        return predefinedSystems;
    }

    private static Map<String, String> toReplacementsMap(String... replacements) {
        return null;
    }
}
