package model.base;

public class ChangeableProperty<T> {
    private final T _base;
    private T _value;

    public ChangeableProperty(T value) {
        _base = value;
        this._value = value;
    }

    public ChangeableProperty(ChangeableProperty<T> c) {
        _value = c._value;
        _base = c._base;
    }

    public T get_base() {
        return _base;
    }

    public void reset() {
        _value = _base;
    }

    public boolean isChanged() {
        if (_base instanceof String) return !_value.equals(_base);
        return _value != _base;
    }

    @Override
    public String toString() {
        return _value.toString();
    }

    public T get_value() {
        return _value;
    }

    public void set_value(T _value) {
        this._value = _value;
    }
}
