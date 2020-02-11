package org.stackwizards.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.stackwizards.coreengine.R;

import java.util.HashMap;
import java.util.Map;

public class BitmapBank {

    static Resources resources ;
    static Map<String, Bitmap> bitmapMap ;

    private static BitmapBank instance;

    public static void InitInstance(Resources resources){
        if(instance == null){
            instance = new BitmapBank(resources);
        }
    }

    public static void AddBitmapToBank(String name, int resourceId){
       Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        bitmap = scaleImage(bitmap,120,120);
        bitmapMap.put(name,bitmap);
    }

    private BitmapBank(Resources resources) {
        this.resources = resources;
        bitmapMap = new HashMap<>();
    }

    public static Bitmap scaleImage(Bitmap bitmap, int width, int height){
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return  resized;
    }

    public static Bitmap GetBitmapWithName(String name){
       if( !bitmapMap.containsValue(name)){
           return null;
       }else {
           return bitmapMap.get(name);
       }
    }

    public static  Map<String, Bitmap> GetLibrary(){
        return bitmapMap;
    }
}
