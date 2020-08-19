package com.futech.our_school.utils.request.listener;

public interface DataChangeListener {
    void onChange();

    void onError(String msg);
}
