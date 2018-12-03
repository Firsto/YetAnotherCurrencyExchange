package studio.inprogress.yace.app.ui.main;

import com.arellomobile.mvp.InjectViewState;
import rx.Subscription;
import studio.inprogress.yace.app.di.module.SchedulerProvider;
import studio.inprogress.yace.app.model.CurrencyDataManager;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;
import studio.inprogress.yace.app.ui.RxPresenter;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@InjectViewState
public class MainPresenter extends RxPresenter<MainMvpView> {

    private Subscription loadCurrenciesSubscription;
    private CurrencyDataManager dataManager;

    @Inject
    public MainPresenter(CurrencyDataManager dataManager, SchedulerProvider schedulerProvider) {
        super(schedulerProvider);
        this.dataManager = dataManager;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        loadCurrencies(null);
    }

    public void loadCurrencies(String base) {
        unsubscribe(loadCurrenciesSubscription);

        getViewState().showProgress();

        loadCurrenciesSubscription = dataManager.getCurrencies(base)
                .delay(1, TimeUnit.SECONDS)
                .repeat()
                .compose(applyObservableSchedulers())
                .subscribe(this::loadCurrenciesSuccess, this::loadCurrenciesError);
    }

    private void loadCurrenciesError(Throwable throwable) {
        getViewState().showError(throwable);
    }

    private void loadCurrenciesSuccess(CurrencyResponse currencyResponse) {
        getViewState().showCurrencies(currencyResponse);
    }

    public void refresh() {
        getViewState().clearData();
        loadCurrencies(null);
    }

    public void changeAmount(double amount) {

    }
}
