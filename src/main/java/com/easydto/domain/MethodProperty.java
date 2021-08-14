package com.easydto.domain;

import com.easydto.enums.PropertyType;

import java.lang.reflect.Method;

public abstract class MethodProperty implements Property{

    protected final Method method;

    public MethodProperty(Method method){
        this.method = method;
    }

    @Override
    public PropertyType propertyType() {
        return PropertyType.METHOD;
    }
}