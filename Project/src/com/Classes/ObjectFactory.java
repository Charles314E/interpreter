package com.Classes;

public class ObjectFactory<T> {
    private final Class<? extends T> clazz;

    public ObjectFactory(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    public T makeObject() {
        //System.out.println("TYPE: " + Lexer.type.getTokenFactoryType(this));
        try {
            return clazz.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }

    public Class<? extends T> getObjectClass() {
        return clazz;
    }
}
