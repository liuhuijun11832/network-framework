package org.hoey.network;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/4/25 22:53
 */
public final class State {

    private final Mutex mutex = new Mutex();

    private int state;

    public State(int state) {
        this.state = state;
    }

    public State() {
        this(0);
    }

    public Mutex withMutex() {
        return mutex.acquire();
    }

    public int get() {
        return state;
    }

    public void set(int state) {
        this.state = state;
    }

    public void register(int mask) {
        this.state |= mask;
    }

    public boolean unregister(int mask) {
        boolean r = (state & mask) > 0;
        state &= ~mask;
        return r;
    }

    public boolean cas(int expectedVal, int newVal) {
        if (state == expectedVal) {
            state = newVal;
            return true;
        } else {
            return false;
        }
    }
}
