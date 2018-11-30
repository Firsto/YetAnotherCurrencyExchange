package studio.inprogress.yace.app.di.module;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import javax.inject.Singleton;

@Module
public class BaseModule {
    @Provides
    @Singleton
    public SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider(Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
