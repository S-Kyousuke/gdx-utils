package com.github.skyousuke.gdxutils.batchmonitor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

class FieldObject {
    private static Array<FieldObject> retainedFieldObject = new Array<FieldObject>();

    private FieldObject parent;
    private Field field;
    private Object fieldValue;

    private FieldObject()  {
    }

    public static FieldObject obtain() {
        FieldObject fieldObject = Pools.obtain(FieldObject.class);
        retainedFieldObject.add(fieldObject);
        return fieldObject;
    }

    public static void freeAll() {
        Pools.freeAll(retainedFieldObject);
        retainedFieldObject.clear();
    }

    public void init(FieldObject parent, Field field, Object fieldValue) {
        this.parent = parent;
        this.field = field;
        this.fieldValue = fieldValue;
    }

    public void setFieldValue(Object value) {
        field.setAccessible(true);
        try {
            field.set(parent.fieldValue, value);
            fieldValue = value;
        } catch (ReflectionException e) {
            throw new Error(e);
        }
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    @Override
    public String toString() {
        if (fieldValue == null)
            return "";
        if (parent == null)
            return ClassReflection.getSimpleName(fieldValue.getClass()) + '@'
                    + Integer.toHexString(fieldValue.hashCode());
        else {
            StringBuilder sb = new StringBuilder();
            int depth = 0;
            for (FieldObject fo = this; fo != null; fo = fo.parent, depth++) {
                if (depth != 0) {
                    sb.append('\n');
                }
                for (int i = 0; i < depth; i++) {
                    sb.append("     ");
                }
                sb.append(ClassReflection.getSimpleName(fo.fieldValue.getClass())).append(" @ ")
                        .append(Integer.toHexString(fo.fieldValue.hashCode()));
            }
            return sb.toString();
        }
    }

    public FieldObject getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldObject that = (FieldObject) o;

        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        if (field != null ? !field.equals(that.field) : that.field != null) return false;
        return fieldValue != null ? fieldValue.equals(that.fieldValue) : that.fieldValue == null;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (fieldValue != null ? fieldValue.hashCode() : 0);
        return result;
    }
}