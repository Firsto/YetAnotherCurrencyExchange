package studio.inprogress.yace.app.di.component.builder;

import studio.inprogress.yace.app.di.ComponentBuilder;
import studio.inprogress.yace.app.di.component.BaseComponent;
import studio.inprogress.yace.app.di.component.DaggerBaseComponent;
import studio.inprogress.yace.app.di.module.BaseModule;
import studio.inprogress.yace.app.di.module.NetworkModule;

public class BaseComponentBuilder extends ComponentBuilder<BaseComponent> {

    @Override
    public BaseComponent create() {
        return DaggerBaseComponent.builder()
                .baseModule(new BaseModule())
                .networkModule(new NetworkModule())
                .build();
    }
}
