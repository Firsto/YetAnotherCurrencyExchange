package studio.inprogress.yace.app.di.module;

import rx.Scheduler;

public class SchedulerProvider {

    private final Scheduler subscribeOn;
    private final Scheduler observeOn;

    public SchedulerProvider(Scheduler subscribeOn, Scheduler observeOn) {
        this.subscribeOn = subscribeOn;
        this.observeOn = observeOn;
    }

    public Scheduler getSubscribeOn() {
        return subscribeOn;
    }

    public Scheduler getObserveOn() {
        return observeOn;
    }
}
