package com.github.skyousuke.gdxutils;

import com.badlogic.gdx.utils.StringBuilder;

public class TypingString implements CharSequence {

    private final CharSequence fullCharSequence;
    private final StringBuilder stringBuilder = new StringBuilder();
    private float characterPerSecond;
    private float whitespaceSpeedScale;
    private float timeAccumulator;
    private String stringCache;
    private int characterIndex;
    private Runnable listener;
    private float typingRate;

    public TypingString(CharSequence fullCharSequence, float characterPerSecond) {
        this.fullCharSequence = fullCharSequence;
        this.characterPerSecond = characterPerSecond;
        whitespaceSpeedScale = 0.25f;
        updateTypingRate(fullCharSequence.charAt(0));
        stringCache = "";
    }

    public void update(float deltaTime) {
        timeAccumulator += deltaTime;
        while (timeAccumulator >= typingRate && !isFinished()) {
            char character = fullCharSequence.charAt(characterIndex);
            stringBuilder.append(character);
            stringCache = stringBuilder.toString();
            timeAccumulator -= typingRate;
            characterIndex++;
            updateTypingRate(character);
            notifyListener();
        }
    }

    private void updateTypingRate(Character character) {
        if (Character.isWhitespace(character))
            typingRate = 1.0f / (characterPerSecond * whitespaceSpeedScale);
        else
            typingRate = 1.0f / characterPerSecond;
    }

    public void setCharacterPerSecond(float characterPerSecond) {
        this.characterPerSecond = characterPerSecond;
        updateTypingRate(fullCharSequence.charAt(characterIndex));
    }

    public void setWhitespaceSpeedScale(float whitespaceSpeedScale) {
        this.whitespaceSpeedScale = whitespaceSpeedScale;
        updateTypingRate(fullCharSequence.charAt(characterIndex));
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }

    public boolean isFinished() {
        return characterIndex == fullCharSequence.length();
    }

    private void notifyListener() {
        if (listener != null)
            listener.run();
    }

    @Override
    public String toString() {
        return stringCache;
    }

    @Override
    public int length() {
        return stringCache.length();
    }

    @Override
    public char charAt(int index) {
        return stringCache.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringCache.substring(start, end);
    }
}