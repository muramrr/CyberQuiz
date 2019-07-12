package com.mmdev.cyberquiz.data;

public class InteractionContent
{
    private char[] user_submit_answer;
    private int[] positions;
    private char[] suggested_chars;
    private int isFull;

    public InteractionContent (char[] user_submit_answer,int[] positions, char[] suggested_chars,int isFull)
    {
        this.user_submit_answer = user_submit_answer;
        this.positions = positions;
        this.suggested_chars = suggested_chars;
        this.isFull = isFull;
    }

    public char[] getUser_submit_answer ()
    { return user_submit_answer; }

    public void setUser_submit_answer (char[] user_submit_answer)
    { this.user_submit_answer = user_submit_answer; }

    public int[] getPositions ()
    { return positions; }

    public void setPositions (int[] positions)
    { this.positions = positions; }

    public char[] getSuggested_chars ()
    { return suggested_chars; }

    public void setSuggested_chars (char[] suggested_chars)
    { this.suggested_chars = suggested_chars; }

    public int getIsFull ()
    { return isFull; }

    public void setIsFull (int isFull)
    { this.isFull = isFull; }

    public void setUserSubmitAnswerItem(int position, char letter)
    { this.user_submit_answer[position] = letter; }

    public void setSuggestedCharsItem(int position, char letter)
    { this.suggested_chars[position] = letter; }

    public void incrementIsFull(int step)
    { this.isFull += step; }

    public void setPostitionsItem (int postition, int value)
    { this.positions[postition] = value; }
}
