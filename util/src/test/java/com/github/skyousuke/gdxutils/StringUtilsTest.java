package com.github.skyousuke.gdxutils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void replaceEach() {
        Assert.assertEquals("B C D A", StringUtils.replaceEach("A B C D",
                new String[]{"A", "B", "C", "D"}, new String[]{"B", "C", "D", "A"}));

        Assert.assertEquals("BCDA", StringUtils.replaceEach("ABCD",
                new String[]{"A", "B", "C", "D"}, new String[]{"B", "C", "D", "A"}));

        Assert.assertEquals("BB CC DD AA", StringUtils.replaceEach("A B C D",
                new String[]{"A", "B", "C", "D"}, new String[]{"BB", "CC", "DD", "AA"}));

        Assert.assertEquals("BBCCDDAA", StringUtils.replaceEach("AABBCCDD",
                new String[]{"AA", "BB", "CC", "DD"}, new String[]{"BB", "CC", "DD", "AA"}));


        final String[] searchList = new String[] {"z"};
        final String[] replacementList = new String[] {"zzz"};
        System.out.println(StringUtils.replaceEach("HH:mm:ss z", searchList, replacementList));
        System.out.println(StringUtils.replaceEach("HH:mm:ss Z", searchList, replacementList));
        System.out.println(StringUtils.replaceEach("HH:mm:ss XXX", searchList, replacementList));
        System.out.println(StringUtils.replaceEach("HH:mm:ss zzzz", searchList, replacementList));
    }
}