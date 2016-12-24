package org.meruvian.workshop.service;

/**
 * Created by Enrico_Didan on 24/12/2016.
 */

public interface TaskService<R> {
    void onExecute(int code);
    void onSuccess(int code, R result);
    void onCancel(int code, String message);
    void onError(int code, String message);
}
