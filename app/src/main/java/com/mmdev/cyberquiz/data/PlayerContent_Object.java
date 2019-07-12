package com.mmdev.cyberquiz.data;


public class PlayerContent_Object
{
    private String playerName;
    private int playerImg;
    private int position;
    private int playersNameList_length;
    private boolean already_guessed;

    public PlayerContent_Object (
            String playerName,int playerImg,int position,int playersNameList_length,boolean already_guessed)
    {
        this.playerName = playerName;
        this.playerImg = playerImg;
        this.position = position;
        this.playersNameList_length = playersNameList_length;
        this.already_guessed = already_guessed;
    }

    //setters
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setPlayerImg(int playerImg) { this.playerImg = playerImg; }
    public void setPosition(int position) { this.position = position; }
    public void setPlayersNameList_length(int playersNameList_length) { this.playersNameList_length = playersNameList_length; }
    public void setalready_guessed(boolean already_guessed) { this.already_guessed = already_guessed; }

    //getters

    public String getPlayerName() { return playerName; }

    public int getPlayerImg() { return playerImg; }

    public int getPosition() { return position; }

    public int getPlayersNameList_length() { return playersNameList_length; }

    public boolean getAlready_guessed() { return already_guessed; }
}
