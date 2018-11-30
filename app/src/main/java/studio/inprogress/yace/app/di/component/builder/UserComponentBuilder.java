package studio.inprogress.yace.app.di.component.builder;

import studio.inprogress.yace.app.di.ComponentBuilder;
import studio.inprogress.yace.app.di.component.BaseComponent;
import studio.inprogress.yace.app.di.component.UserComponent;
import studio.inprogress.yace.app.di.module.UserModule;

public class UserComponentBuilder extends ComponentBuilder<UserComponent> {

    private BaseComponent baseComponent;

    public UserComponentBuilder(BaseComponent baseComponent) {
        this.baseComponent = baseComponent;
    }

    @Override
    public UserComponent create() {
        return baseComponent.plus(new UserModule());
    }
}
