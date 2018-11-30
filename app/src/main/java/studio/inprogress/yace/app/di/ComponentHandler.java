package studio.inprogress.yace.app.di;

import timber.log.Timber;

public class ComponentHandler<T> {

    private T component;
    private ComponentBuilder<T> componentBuilder;
    private volatile int ownerCount = 0;

    public ComponentHandler(ComponentBuilder componentBuilder) {
        this.componentBuilder = componentBuilder;
    }

    public T get() {
        return component;
    }

    public T createComponent() throws CreateComponentException {
        if (componentBuilder == null)  {
            throw new CreateComponentException("Unable to create component "
                    + component.getClass().getSimpleName() + "." + " ComponentBuilder is null");
        }
        if (component == null) {
            component = componentBuilder.create();
            Timber.d("Component %s created", component.getClass().getSimpleName());
        }
        ownerCount++;
        Timber.d("Component %s owner count %d", component.getClass().getSimpleName(), ownerCount);
        return component;
    }

    public void release() {
        if (component == null) return;
        ownerCount--;
        Timber.d("Component %s owner count after release %d",
                component.getClass().getSimpleName(), ownerCount);
        if (ownerCount <= 0) {
            releaseComponent();
        }
    }

    private void releaseComponent() {
        Timber.d("Component %s released", component.getClass().getSimpleName());
        component = null;
    }

    public void setComponentBuilder(ComponentBuilder<T> componentBuilder) {
        this.componentBuilder = componentBuilder;
    }
}
