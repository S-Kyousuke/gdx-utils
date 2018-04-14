package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.files.FileHandle;

public class FileUtils {

    private FileUtils() {
    }

    public static String getRelativePath(FileHandle parent, FileHandle child) {
        String parentPath = parent.path();
        String childPath = child.path();
        return childPath.substring(parentPath.length() + 1, childPath.length());
    }
}
