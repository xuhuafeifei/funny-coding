package com.xhf.csdn.model;

/**
 * @author feigebuge
 * @email 2508020102@qq.com
 */
public class Pair<T, T1> {
    public T first;
    public T1 second;

    public Pair(T key, T1 value) {
        this.first = key;
        this.second = value;
    }
}
