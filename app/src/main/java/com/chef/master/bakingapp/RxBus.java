package com.chef.master.bakingapp;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by Petya Marinova on 6/19/2018.
 */
public class RxBus {
    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    public void send(Object o) {
        bus.accept(o);
    }

    public Flowable<Object> asFlowable() {
        return bus.toFlowable(BackpressureStrategy.LATEST);
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
