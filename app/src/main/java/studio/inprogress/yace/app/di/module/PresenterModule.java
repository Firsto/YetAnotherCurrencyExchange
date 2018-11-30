package studio.inprogress.yace.app.di.module;

import dagger.Module;
import dagger.Provides;
import studio.inprogress.yace.app.model.CurrencyDataManager;
import studio.inprogress.yace.app.ui.main.MainPresenter;

@Module
public class PresenterModule {
    @Provides
    public MainPresenter provideMainPresenter(CurrencyDataManager dataManager,
                                                       SchedulerProvider schedulerProvider) {
        return new MainPresenter(dataManager, schedulerProvider);
    }

}
