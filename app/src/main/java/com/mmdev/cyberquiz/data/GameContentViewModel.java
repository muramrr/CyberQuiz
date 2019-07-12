package com.mmdev.cyberquiz.data;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameContentViewModel extends ViewModel
{
    private MutableLiveData<PlayerContent_Object> playerContent;

    public void init () {
        if (playerContent == null)
            playerContent = new MutableLiveData<>();
    }

    public MutableLiveData<PlayerContent_Object> getPlayerContent () {
        return playerContent;
    }

    public void setPlayerContent (PlayerContent_Object playerContent_object) {
        playerContent.setValue(playerContent_object);
    }
}
