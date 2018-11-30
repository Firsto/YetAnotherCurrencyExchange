package studio.inprogress.yace.app.ui;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscription;
import studio.inprogress.yace.app.di.module.SchedulerProvider;

public abstract class RxPresenter<V extends MvpView> extends MvpPresenter<V> {

    private SchedulerProvider schedulerProvider;

    public RxPresenter(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    public void unsubscribe(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }

    protected <T> Observable.Transformer<T, T> applyObservableSchedulers() {
        return observable -> observable.subscribeOn(schedulerProvider.getSubscribeOn())
                .observeOn(schedulerProvider.getObserveOn());
    }

    protected <T> Single.Transformer<T, T> applySingleSchedulers() {
        return single -> single.subscribeOn(schedulerProvider.getSubscribeOn())
                .observeOn(schedulerProvider.getObserveOn());
    }

    protected Completable.Transformer applyCompletableSchedulers() {
        return completable -> completable.subscribeOn(schedulerProvider.getSubscribeOn())
                .observeOn(schedulerProvider.getObserveOn());
    }

    public void setSchedulerProvider(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }
}