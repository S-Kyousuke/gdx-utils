package com.github.skyousuke.gdxutils;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class TypingStringTest {

    @Test
    public void notifyListener_CountingInvocation_CorrectInvocationCount() {
        TypingString typingString = new TypingString("text", 1.0f);
        TypingStringListener listener = Mockito.mock(TypingStringListener.class);
        typingString.setListener(listener);
        Mockito.verify(listener, Mockito.times(0)).onTyping(Mockito.anyChar());
        typingString.update(0.5f);
        Mockito.verify(listener, Mockito.times(0)).onTyping(Mockito.anyChar());
        typingString.update(0.5f);
        Mockito.verify(listener, Mockito.times(1)).onTyping(Mockito.anyChar());
        typingString.update(1.0f);
        Mockito.verify(listener, Mockito.times(2)).onTyping(Mockito.anyChar());
        typingString.update(1.0f);
        Mockito.verify(listener, Mockito.times(3)).onTyping(Mockito.anyChar());
        typingString.update(1.0f);
        Mockito.verify(listener, Mockito.times(4)).onTyping(Mockito.anyChar());
    }


    @Test
    public void toString_atStartTime_ReturnEmptyString() {
        TypingString typingString = new TypingString("text", 1.0f);
        assertEquals("", typingString.toString());
    }

    @Test
    public void toString_OneCharPerSecondAtZeroSecond_ReturnEmptyString() {
        TypingString typingString = new TypingString("text", 1.0f);
        typingString.update(0f);
        typingString.update(0f);
        assertEquals("", typingString.toString());
    }

    @Test
    public void toString_OneCharPerSecondAtHalfSecond_ReturnEmptyString() {
        TypingString typingString = new TypingString("text", 1.0f);
        typingString.update(0.25f);
        typingString.update(0.25f);
        assertEquals("", typingString.toString());
    }

    @Test
    public void toString_OneCharPerSecondAtOneSecond_ReturnOneCharString() {
        TypingString typingString = new TypingString("text", 1.0f);
        typingString.update(0.5f);
        typingString.update(0.5f);
        assertEquals("t", typingString.toString());
    }

    @Test
    public void toString_OneCharPerSecondAtOnePointFiveSecond_ReturnOneCharString() {
        TypingString typingString = new TypingString("text", 1.0f);
        typingString.update(0.75f);
        typingString.update(0.75f);
        assertEquals("t", typingString.toString());
    }

    @Test
    public void toString_TwoCharPerSecondAtHalfSecond_ReturnOneCharString() {
        TypingString typingString = new TypingString("text", 2.0f);
        typingString.update(0.25f);
        typingString.update(0.25f);
        assertEquals("t", typingString.toString());
    }

    @Test
    public void toString_TwoCharPerSecondAtTwoSecond_ReturnFourCharString() {
        TypingString typingString = new TypingString("text", 2.0f);
        typingString.update(1.0f);
        typingString.update(1.0f);
        assertEquals("text", typingString.toString());
    }

    @Test
    public void toString_StringWithWhiteSpace_ReturnCorrectString() {
        TypingString typingString = new TypingString("evil text", 2.0f);
        typingString.update(0.5f);
        assertEquals("e", typingString.toString());
        typingString.update(0.5f);
        assertEquals("ev", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evi", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evil", typingString.toString());
        typingString.update(0.5f);
        typingString.update(0.5f);
        typingString.update(0.5f);
        typingString.update(0.5f);
        assertEquals("evil ", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evil t", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evil te", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evil tex", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evil text", typingString.toString());
    }


    @Test
    public void setCharacterPerSecond_SetBeforeUpdate_ReturnCorrectString() {
        TypingString typingString = new TypingString("evil", 1234.0f);
        typingString.setCharacterPerSecond(2.0f);

        assertEquals("", typingString.toString());
        typingString.update(0.5f);
        assertEquals("e", typingString.toString());
        typingString.update(0.5f);
        assertEquals("ev", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evi", typingString.toString());
        typingString.update(0.5f);
        assertEquals("evil", typingString.toString());
    }

    @Test
    public void setCharacterPerSecond_SetBetweenUpdate_ReturnCorrectString() {
        TypingString typingString = new TypingString("scratch!", 1234.0f);
        typingString.setCharacterPerSecond(2.0f);

        assertEquals("", typingString.toString());
        typingString.update(0.5f);
        assertEquals("s", typingString.toString());
        typingString.update(0.5f);
        assertEquals("sc", typingString.toString());

        typingString.setCharacterPerSecond(1.0f);
        typingString.update(0.5f);
        typingString.update(0.5f);
        assertEquals("scr", typingString.toString());
        typingString.update(0.5f);
        assertEquals("scr", typingString.toString());
        typingString.update(0.5f);
        assertEquals("scra", typingString.toString());

        typingString.setCharacterPerSecond(4.0f);
        typingString.update(0.5f);
        assertEquals("scratc", typingString.toString());
        typingString.update(0.5f);
        assertEquals("scratch!", typingString.toString());
    }

    @Test
    public void setWhitespaceSpeedScale_SetBeforeUpdate_ReturnCorrectString() {
        TypingString typingString = new TypingString("evil text scratch!", 2.0f);
        typingString.setWhitespaceSpeedScale(1.0f);
        typingString.update(2f);
        typingString.update(0.5f);
        typingString.update(0.5f);
        assertEquals("evil t", typingString.toString());
    }

    @Test
    public void setWhitespaceSpeedScale_SetBetweenUpdate_ReturnCorrectString() {
        TypingString typingString = new TypingString("evil text scratch! end", 2.0f);
        typingString.setWhitespaceSpeedScale(1.0f);
        typingString.update(2f);
        typingString.update(0.5f);
        typingString.update(0.5f);
        assertEquals("evil t", typingString.toString());

        typingString.setWhitespaceSpeedScale(0.5f);
        typingString.update(1.5f);
        typingString.update(1.0f);
        typingString.update(0.5f);
        assertEquals("evil text s", typingString.toString());

        typingString.setWhitespaceSpeedScale(2.0f);
        typingString.update(3.5f);
        typingString.update(0.25f);
        typingString.update(0.5f);
        assertEquals("evil text scratch! e", typingString.toString());
    }

    @Test
    public void charAt_FullString_ReturnCorrectChar() {
        TypingString typingString = new TypingString("evil", 1.0f);
        typingString.update(9999999f);
        assertEquals('e', typingString.charAt(0));
        assertEquals('v', typingString.charAt(1));
        assertEquals('i', typingString.charAt(2));
        assertEquals('l', typingString.charAt(3));
    }

    @Test
    public void subSequence_FullString_ReturnCorrectString() {
        TypingString typingString = new TypingString("evil", 1.0f);
        typingString.update(9999999f);

        assertEquals("", typingString.subSequence(0, 0));
        assertEquals("e", typingString.subSequence(0, 1));
        assertEquals("ev", typingString.subSequence(0, 2));
        assertEquals("evi", typingString.subSequence(0, 3));
        assertEquals("evil", typingString.subSequence(0, 4));

        assertEquals("", typingString.subSequence(1, 1));
        assertEquals("v", typingString.subSequence(1, 2));
        assertEquals("vi", typingString.subSequence(1, 3));
        assertEquals("vil", typingString.subSequence(1, 4));

        assertEquals("", typingString.subSequence(2, 2));
        assertEquals("i", typingString.subSequence(2, 3));
        assertEquals("il", typingString.subSequence(2, 4));

        assertEquals("", typingString.subSequence(3, 3));
        assertEquals("l", typingString.subSequence(3, 4));

        assertEquals("", typingString.subSequence(4, 4));
    }

    @Test
    public void length_FullString_ReturnCorrectLength() {
        TypingString typingString = new TypingString("evil", 1.0f);
        typingString.update(9999999f);

        assertEquals(4, typingString.length());
    }

}