package studio.inprogress.yace.app.ui.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import studio.inprogress.yace.app.model.api.response.CurrencyResponse;

public interface MainMvpView extends MvpView {

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgress();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showError(Throwable e);

    void showCurrencies(CurrencyResponse currencyResponse);

    @StateStrategyType(SingleStateStrategy.class)
    void clearData();
}
