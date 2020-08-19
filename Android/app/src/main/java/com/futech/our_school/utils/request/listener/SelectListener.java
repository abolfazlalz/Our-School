package com.futech.our_school.utils.request.listener;

public interface SelectListener<T> {

    void onSelect(T data, boolean isOnline);

    void onError(String msg);

}
