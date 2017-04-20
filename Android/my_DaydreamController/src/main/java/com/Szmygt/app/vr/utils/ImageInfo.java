package com.Szmygt.app.vr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageInfo implements Parcelable {
    private static final String TAG = "bibi";
    private int id;
    private String imageName;
    private String path;
    private long size;
    public String mimeType;
    private String data;
    public Bitmap thumbnail;
    private InputStream fileInputStream;
    public Context context;
    private byte[] bytesAYSN;

    public byte[] iconBytes;
    public boolean isIconReady = false;
    public boolean isChange = false;

    private ImageInfo(Parcel in) {
        readFromParcel(in);
    }

    public ImageInfo() {

    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        imageName = in.readString();
        path = in.readString();
        size = in.readLong();
    }

    public String getData() {
        return data;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return imageName;
    }

    public void setName(String name) {
        this.imageName = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", name='" + imageName + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageName);
        dest.writeString(path);
        dest.writeLong(size);
    }

    private List<String> picList;


    public void loadIcon() {
        try {

            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, outputstream);
            iconBytes = outputstream.toByteArray();
            isIconReady = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isBMP() {

        boolean isbmp = Uri.encode(getPath()).toString().trim().toLowerCase().endsWith(".bmp")
                     || Uri.encode(getPath()).toString().trim().toUpperCase().endsWith(".bmp")
                     || Uri.encode(getPath()).toString().trim().toLowerCase().endsWith(".gif");

        Log.d(TAG, "isbmp===: " + isbmp);

        return isbmp;
    }


    public byte[] BitmapBytes(String path) throws IOException {
//        Glide.with(context).load(path).asBitmap().toBytes().centerCrop().into(new SimpleTarget<byte[]>(150,150) {
//            @Override
//            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
//
//                bytesAYSN = resource;
//
//                Log.d(TAG, "BitmapBytes===: "+ bytesAYSN.length);
//                isChange = true;
//
//            }
//        });

        InputStream stream = new FileInputStream(new File(path));

        Log.d(TAG, "path--: " + path);
//        BitmapFactory.Options option = new BitmapFactory.Options();
//        option.inSampleSize = 1;


        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, bao);
        bitmap.recycle();

        Log.d(TAG, "BitmapBytes===: " + bao.toByteArray().length);
        return bao.toByteArray();
    }


    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {

        @Override
        public ImageInfo createFromParcel(Parcel source) {
            return new ImageInfo(source);
        }

        @Override
        public ImageInfo[] newArray(int size) {

            return new ImageInfo[size];
        }
    };

}

