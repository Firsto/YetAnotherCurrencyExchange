package studio.inprogress.yace.app.di;

import studio.inprogress.yace.app.di.component.BaseComponent;
import studio.inprogress.yace.app.di.component.UserComponent;
import studio.inprogress.yace.app.di.component.builder.BaseComponentBuilder;
import studio.inprogress.yace.app.di.component.builder.UserComponentBuilder;

public class ComponentManager {

    private ComponentHandler<BaseComponent> baseComponentHandler;
    private ComponentHandler<UserComponent> userComponentHandler;

    public ComponentManager() {
        initBaseComponentHandler();
    }

    private void initBaseComponentHandler() {
        baseComponentHandler = new ComponentHandler<>(new BaseComponentBuilder());
        try {
            baseComponentHandler.createComponent();
        } catch (CreateComponentException ex) {
            ex.printStackTrace();
        }
    }

    public BaseComponent getBaseComponent() {
        return baseComponentHandler.get();
    }

    public UserComponent createUserComponent() throws CreateComponentException {
        if (userComponentHandler == null) {
            userComponentHandler = new ComponentHandler<>(new UserComponentBuilder(getBaseComponent()));
        }
        return userComponentHandler.createComponent();
    }

    public UserComponent getUserComponent() {
        return userComponentHandler.get();
    }

    public void releaseUserComponent() {
        userComponentHandler.release();
    }
}
