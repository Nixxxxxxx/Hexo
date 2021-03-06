package org.stackwizards.soundbank;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.net.Uri;

import org.stackwizards.coreengine.R;

import java.net.URI;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class SoundBank  {
    Context context;
    Map<String, Integer> soundRecords ;
    public SoundBank(Context context) {
        this.context = context;
        soundRecords = new HashMap<>();
        LoadSound("hit", R.raw.hit);
        LoadSound("appear", R.raw.appear_sound);
    }

    public void LoadSound(String title, int resourceId){
        soundRecords.put(title,resourceId);
    }

    public void Play(String title){
//        MediaPlayer sound =  soundRecords.get(title);
        MediaPlayer sound =  MediaPlayer.create(context,soundRecords.get(title));


        if(sound ==  null){
            return;
        }
        if(sound.isPlaying()){
            sound.stop();
        }
        sound.start();
    }
}
