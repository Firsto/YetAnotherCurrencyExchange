package studio.inprogress.yace.app.di.component;

import dagger.Subcomponent;
import studio.inprogress.yace.app.di.UserScope;
import studio.inprogress.yace.app.di.module.PresenterModule;
import studio.inprogress.yace.app.di.module.UserModule;
import studio.inprogress.yace.app.ui.main.MainPresenter;

@Subcomponent(modules = {
        UserModule.class,
        PresenterModule.class
})
@UserScope
public interface UserComponent {
    MainPresenter provideMainPresenter();
}
