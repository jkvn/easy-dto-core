package org.easydto.domain;

import org.easydto.annotation.DtoProperty;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class PropertyConfiguration {

    public final Property property;
    public final String targetName;
    public final String[] profiles;

    private PropertyConfiguration(Field field) {
        DtoProperty annotation = field.getAnnotation(DtoProperty.class);
        this.property = new FieldProperty(field, true);
        this.targetName = annotation.value().equals(DtoProperty.DEFAULT) ? field.getName() : annotation.value();
        this.profiles = annotation.profile();
    }

    private PropertyConfiguration(Method method) {
        DtoProperty annotation = method.getAnnotation(DtoProperty.class);

        if(method.getParameterCount() == 0) {
            this.property = new ReadMethodProperty(method);
        } else {
            this.property = new WriteMethodProperty(method);
        }

        this.targetName = annotation.value().equals(DtoProperty.DEFAULT) ? method.getName() : annotation.value();
        this.profiles = annotation.profile();
    }

    public static Optional<PropertyConfiguration> load(Field field) {
        return isAnnotated(field) ? Optional.of(new PropertyConfiguration(field)) : Optional.empty();
    }

    public static Optional<PropertyConfiguration> load(Method method) {
        return isAnnotated(method) ? Optional.of(new PropertyConfiguration(method)) : Optional.empty();
    }

    private static boolean isAnnotated(AccessibleObject accessibleObject){
        return accessibleObject.isAnnotationPresent(DtoProperty.class);
    }


    public boolean isAllowedInProfile(String profile) {
        if (profile == null || profiles.length == 0) return true;
        for (String s : profiles) {
            if (s.equals(profile))
                return true;
        }
        return false;
    }

    public void writeValue(Object target, Object value) {
        ((WriteProperty) this.property).write(target, value);
    }
}
