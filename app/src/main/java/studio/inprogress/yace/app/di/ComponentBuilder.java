package studio.inprogress.yace.app.di;

public abstract class ComponentBuilder<T> {

    private T component;

    public abstract T create();
}
