package com.cnbsoft.plugin.generator.interactive;

import java.util.Optional;

public enum GenerationType {

    ALL        (1, "All (Model + Controller + Service + Persistence + Mapper + Views)"),
    CONTROLLER (2, "Controller only"),
    FORM_VIEW  (3, "Form View only"),
    LIST_VIEW  (4, "List View only"),
    MODEL      (5, "Model (VO) only"),
    SERVICE    (6, "Service only"),
    PERSISTENCE(7, "Persistence (Mapper) only");

    public final int number;
    public final String label;

    GenerationType(int number, String label) {
        this.number = number;
        this.label  = label;
    }

    public static Optional<GenerationType> fromNumber(int n) {
        for (GenerationType t : values()) {
            if (t.number == n) return Optional.of(t);
        }
        return Optional.empty();
    }
}
