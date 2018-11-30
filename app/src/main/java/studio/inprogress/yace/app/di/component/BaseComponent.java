package studio.inprogress.yace.app.di.component;

import dagger.Component;
import studio.inprogress.yace.app.di.module.*;

import javax.inject.Singleton;

@Component(modules = {
        BaseModule.class,
        NetworkModule.class})

@Singleton
public interface BaseComponent {

    SchedulerProvider getSchedulerProvider();

    UserComponent plus(UserModule userModule);
}
