package com.system.animals.shared.base;

import java.lang.reflect.Field;

public final class MapperSupport {

    private MapperSupport() {
    }

    public static <T extends BaseEntity> T withId(T entity, Long id) {
        if (entity == null || id == null) {
            return entity;
        }
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
            return entity;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("No fue posible asignar el id a la entidad", ex);
        }
    }

}
