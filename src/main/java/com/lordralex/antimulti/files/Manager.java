package com.lordralex.antimulti.files;

public interface Manager {

    public abstract String[] getIPs(String name);

    public abstract String[] getNames(String ip);

    public abstract void addIP(String name, String ip);

    public abstract void addName(String ip, String name);

    public abstract Manager setup();

    public abstract void close();
}