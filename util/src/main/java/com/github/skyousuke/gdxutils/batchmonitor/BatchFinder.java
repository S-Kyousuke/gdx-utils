package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;

import java.util.Collection;
import java.util.Iterator;

class BatchFinder {

    private static final Array<FieldObject> newObjects = new Array<FieldObject>();
    private static final Array<FieldObject> unvisitedObjects = new Array<FieldObject>();
    private static final Array<FieldObject> visitedObjects = new Array<FieldObject>();
    private static final Array<FieldObject> childFieldObjects = new Array<FieldObject>();

    private static final Array<Field> fields = new Array<Field>();

    private BatchFinder() {
    }

    public static void findBatchObjects(Object object, Array<FieldObject> outBatchObjects) {
        FieldObject.freeAll();
        newObjects.clear();
        unvisitedObjects.clear();
        visitedObjects.clear();

        FieldObject fieldObject = FieldObject.obtain();
        fieldObject.init(null, null, object);
        unvisitedObjects.addAll(findChildFieldObjects(fieldObject));

        while (unvisitedObjects.size > 0) {
            for (FieldObject unvisitedObject : unvisitedObjects) {
                newObjects.addAll(findChildFieldObjects(unvisitedObject));
                visitedObjects.add(unvisitedObject);
            }
            unvisitedObjects.clear();
            unvisitedObjects.addAll(newObjects);
            newObjects.clear();
        }
        for (FieldObject visitedObject : visitedObjects) {
            if (!isUnwantedFieldObject(visitedObject)) {
                outBatchObjects.add(visitedObject);
            }
        }
    }

    private static Array<FieldObject> findChildFieldObjects(FieldObject parentFieldObject) {
        childFieldObjects.clear();
        Object parentValue = parentFieldObject.getFieldValue();
        if (parentValue != null) {
            Array<Field> childFields = removeUnwantedField(getFields(parentValue.getClass()));
            for (Field childField : childFields) {
                try {
                    childField.setAccessible(true);
                    Object childValue = childField.get(parentValue);
                    if (!isParentValue(parentFieldObject, childValue)) {
                        FieldObject fieldObject = FieldObject.obtain();
                        fieldObject.init(parentFieldObject, childField, childValue);
                        childFieldObjects.add(fieldObject);
                    }
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
        return childFieldObjects;
    }

    private static Array<Field> getFields(Class<?> type) {
        fields.clear();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(ClassReflection.getDeclaredFields(c));
        }
        return fields;
    }

    private static Array<Field> removeUnwantedField(Array<Field> fields) {
        Iterator<Field> it = fields.iterator();
        while (it.hasNext()) {
            Field field = it.next();
            if (isUnwantedType(field.getType())) {
                it.remove();
                Pools.free(field);
            }
        }
        return fields;
    }

    private static boolean isParentValue(FieldObject parentFieldObject, Object value) {
        for (FieldObject parentField = parentFieldObject; parentField != null; parentField = parentField.getParent()) {
            if (parentField.getFieldValue() == value) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUnwantedType(Class<?> type) {
        return ClassReflection.isPrimitive(type)
                || ClassReflection.isArray(type)
                || ClassReflection.isEnum(type)
                || ClassReflection.isAssignableFrom(CharSequence.class, type)
                || ClassReflection.isAssignableFrom(Collection.class, type)
                || (isBadLogicPackage(type) && !isWantedType(type))
                || ClassReflection.isAssignableFrom(BatchMeter.class, type);
    }

    private static boolean isBadLogicPackage(Class<?> type) {
        return type.getCanonicalName().indexOf("com.badlogic.gdx") == 0;
    }

    private static boolean isWantedType(Class<?> type) {
        return ClassReflection.isAssignableFrom(Batch.class, type)
                || ClassReflection.isAssignableFrom(Stage.class, type)
                || ClassReflection.isAssignableFrom(BatchTiledMapRenderer.class, type)
                || ClassReflection.isAssignableFrom(Screen.class, type);
    }

    private static boolean isUnwantedFieldObject(FieldObject fieldObject) {
        if (fieldObject == null || fieldObject.getFieldValue() == null)
            return true;

        for (FieldObject object = fieldObject; object != null; object = object.getParent()) {
            if (ClassReflection.isAssignableFrom(BatchMonitor.class, object.getFieldValue().getClass())
                    || ClassReflection.isAssignableFrom(FieldObject.class, object.getFieldValue().getClass())) {
                return true;
            }
        }
        return fieldObject.getFieldValue().getClass() != SpriteBatch.class;
    }

}
