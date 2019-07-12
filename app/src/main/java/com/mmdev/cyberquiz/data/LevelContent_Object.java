package com.mmdev.cyberquiz.data;

public class LevelContent_Object
{
    private String [] playersNames;
    private int[] playersImgs;

    public LevelContent_Object (String[] playersNames, int[] playersImgs)
    {
        this.playersNames = playersNames;
        this.playersImgs = playersImgs;
    }

    public String[] getPlayersNames(){return playersNames;}

    public int[] getPlayersImgs () { return playersImgs; }
}
