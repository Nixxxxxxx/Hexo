package org.stackwizards.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.stackwizards.coreengine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapBank {

    static Resources resources ;
    static List<MyBitMap> bitmapMap ;

    private static BitmapBank instance;

    public static void InitInstance(Resources resources){
        if(instance == null){
            instance = new BitmapBank(resources);
        }
    }

    public static void AddBitmapToBank(String name, int resourceId){
       Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        bitmap = scaleImage(bitmap,120,120);
        bitmapMap.add(new MyBitMap(name,bitmap));
    }

    public static Bitmap GetBitmapFromResourceID(int resourceId){
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);
        return scaleImage(bitmap,120,120);
    }

    private BitmapBank(Resources resources) {
        this.resources = resources;
        bitmapMap = new ArrayList<>();
    }

    public static Bitmap scaleImage(Bitmap bitmap, int width, int height){
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return  resized;
    }

//    public static Bitmap GetBitmapWithName(String name){
//       if( !bitmapMap.containsValue(name)){
//           return null;
//       }else {
//           return bitmapMap.get(name);
//       }
//    }

    public static  List<MyBitMap> GetLibrary(){
        return bitmapMap;
    }

    public static class MyBitMap{
        public String name;
        public Bitmap bitmap;

        public MyBitMap(String name, Bitmap bitmap) {
            this.name = name;
            this.bitmap = bitmap;
        }
    }
}
