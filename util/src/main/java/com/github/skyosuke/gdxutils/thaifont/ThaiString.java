package com.github.skyosuke.gdxutils.thaifont;


public class ThaiString implements CharSequence {

    private static char[] tempCharArray = new char[0];

    private final CharSequence fixedThaiCharSequence;

    public ThaiString(CharSequence thaiCharSequence) {
        fixedThaiCharSequence = fixCharSequence(thaiCharSequence);
    }

    @Override
    public int length() {
        return fixedThaiCharSequence.length();
    }

    @Override
    public char charAt(int index) {
        return fixedThaiCharSequence.charAt(index);
    }

    @Override
    public String toString() {
        return fixedThaiCharSequence.toString();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return fixedThaiCharSequence.subSequence(start, end);
    }

    private static String fixCharSequence(CharSequence str) {
        final int strLength = str.length();
        if (tempCharArray.length < strLength << 1) {
            tempCharArray = new char[strLength << 1];
        }

        int i = 0;
        int sbIndex = 0;
        while (i < strLength) {
            final char character = str.charAt(i);
            final char nextCharacter;
            if (i < strLength - 1)
                nextCharacter = str.charAt(i + 1);
            else
                nextCharacter = '\u0000';
            i += 2;

            final boolean firstCharIsTopChar = character == ThaiUnicode.MAI_EK
                    || character == ThaiUnicode.MAI_THO
                    || character == ThaiUnicode.MAI_TRI
                    || character == ThaiUnicode.MAI_CHATTAWA
                    || character == ThaiUnicode.THANTHAKHAT;

            if (nextCharacter == ThaiUnicode.SARA_AM && firstCharIsTopChar) {
                tempCharArray[sbIndex++] = ThaiUnicode.NIKHAHIT;
                tempCharArray[sbIndex++] = character;
                tempCharArray[sbIndex++] = ThaiUnicode.SARA_AA;
            } else if (nextCharacter == ThaiUnicode.SARA_AM && character == ThaiUnicode.SARA_AM) {
                tempCharArray[sbIndex++] = ThaiUnicode.NIKHAHIT;
                tempCharArray[sbIndex++] = ThaiUnicode.SARA_AA;
                tempCharArray[sbIndex++] = ThaiUnicode.NIKHAHIT;
                tempCharArray[sbIndex++] = ThaiUnicode.SARA_AA;
            } else if (nextCharacter == ThaiUnicode.SARA_AM) {
                tempCharArray[sbIndex++] = character;
                tempCharArray[sbIndex++] = ThaiUnicode.NIKHAHIT;
                tempCharArray[sbIndex++] = ThaiUnicode.SARA_AA;
            } else if (character == ThaiUnicode.SARA_AM) {
                tempCharArray[sbIndex++] = ThaiUnicode.NIKHAHIT;
                tempCharArray[sbIndex++] = ThaiUnicode.SARA_AA;
                tempCharArray[sbIndex++] = nextCharacter;
            } else {
                tempCharArray[sbIndex++] = character;
                tempCharArray[sbIndex++] = nextCharacter;
            }
        }
        if (tempCharArray[sbIndex - 1] == '\u0000')
            --sbIndex;

        return new String(tempCharArray, 0, sbIndex);
    }
}
