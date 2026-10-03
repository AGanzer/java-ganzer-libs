package com.example.uitests.swingdv.dialogs;

import java.util.List;

public class ChooseFromListData<E> {
    public final String title;
    public final List<E> items;

    public E chosen;

    public ChooseFromListData(String title, List<E> items) {
        this.title = title;
        this.items = items;
    }
}
