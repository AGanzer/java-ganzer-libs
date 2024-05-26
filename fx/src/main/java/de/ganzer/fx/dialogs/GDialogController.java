package de.ganzer.fx.dialogs;

import java.util.function.Consumer;

public interface GDialogController<Data> {
    void initControls(Data data);

    default void setApplyDataConsumer(Consumer<Data> applyDataConsumer) {
    }

    default boolean autoApplyDataAfterClose() {
        return true;
    }

    default int getModalResult() {
        return ModalResult.CANCEL;
    }
}
