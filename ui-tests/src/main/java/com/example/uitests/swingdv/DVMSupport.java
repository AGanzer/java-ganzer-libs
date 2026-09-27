package com.example.uitests.swingdv;

import de.ganzer.dv.DVManagerSupport;
import de.ganzer.dv.View;

import java.util.function.Consumer;

public class DVMSupport implements DVManagerSupport {
    private static DVMSupport instance;

    private Consumer<View<?>> consumer;

    public DVMSupport() {
        instance = this;
    }

    public static void viewChanged(View<?> view) {
        instance.consumer.accept(view);
    }

    @Override
    public void setActiveViewChangedListener(Consumer<View<?>> consumer) {
        this.consumer = consumer;
    }
}
