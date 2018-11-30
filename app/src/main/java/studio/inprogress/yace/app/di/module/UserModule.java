package studio.inprogress.yace.app.di.module;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import studio.inprogress.yace.app.di.UserScope;
import studio.inprogress.yace.app.model.CurrencyDataManager;
import studio.inprogress.yace.app.model.api.CurrenciesApi;

@Module
public class UserModule {
    @Provides
    @UserScope
    public CurrenciesApi provideCurrenciesApi(Retrofit retrofit) {
        return retrofit.create(CurrenciesApi.class);
    }

    @Provides
    @UserScope
    public CurrencyDataManager provideCurrencyDataManager(CurrenciesApi api) {
        return new CurrencyDataManager(api);
    }
}
