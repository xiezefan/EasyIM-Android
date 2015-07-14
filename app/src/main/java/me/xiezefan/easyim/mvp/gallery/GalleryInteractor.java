package me.xiezefan.easyim.mvp.gallery;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryInteractor {



    public Map<String, List<String>> scanImage(Context context) {
        Map<String, List<String>> imageGroupMap = new HashMap<>();
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        //只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

        if(mCursor == null){
            return imageGroupMap;
        }

        while (mCursor.moveToNext()) {
            //获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();
            //根据父路径名将图片放入到mGroupMap中
            if (!imageGroupMap.containsKey(parentName)) {
                List<String> chileList = new ArrayList<String>();
                chileList.add(path);
                imageGroupMap.put(parentName, chileList);
            } else {
                imageGroupMap.get(parentName).add(path);
            }
        }
        return imageGroupMap;
    }


}
