package org.stackwizards;

import android.content.Context;

import org.stackwizards.soundbank.SoundBank;

public class GameCore {
    public static SoundBank soundBank;

    public static void initialisation(Context context){
        soundBank = new SoundBank(context);
    }

}
