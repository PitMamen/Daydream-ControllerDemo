package com.Szmygt.app.vr.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class ImageProvider {
    private final String TAG = getClass().getSimpleName();
    private final String BIG = "haha";
    private Context context;
    int mTotalPage = 1;
    int mCurrPage = 1;
    public   boolean  isProviderReady  = false;
    public AtomicBoolean listReady = new AtomicBoolean(false);
    CopyOnWriteArrayList<ImageInfo> imageList;


    private InputStream fileInputStream;
    private boolean isBMP = false;


    public ImageProvider(Context context) {
        this.context = context;
        imageList = new CopyOnWriteArrayList<>();

    }
    //返回list集合
    public int getSize() {
        return imageList.size();
    }


    public ImageInfo get(int index) {
        return imageList.get(index);
    }
   //获取最大页
    public int getMaxPageIndex(int pageImageCount) {
        int imageCount = imageList.size();
        int maxPage = imageCount / pageImageCount + ((imageCount % pageImageCount == 0) ? 0 : 1) - 1;

        return maxPage;
    }


    public ImageInfo[] get(int pageIndex, int pageImageCount) {
        ImageInfo[] imageInfos;
        synchronized (imageList) {
            int index;
            int count;
            int imageCount = imageList.size();

            index = pageIndex * pageImageCount;
            if (index >= imageCount) {
                return new ImageInfo[0];
            }
            if (pageIndex == getMaxPageIndex(pageImageCount)) {
                count = imageCount % pageImageCount;
            } else {
                count = pageImageCount;
            }
            imageInfos = new ImageInfo[count];
            for (int i = 0; i < count; i++) {
                imageInfos[i] = imageList.get(index + i);
            }
        }
        return imageInfos;

    }


    /**
     * 检查页，保证无误
     *
     * @param page
     * @return
     */
    private int checkPage(int page) {

        if (page <= 0) {
            return 1;
        } else if (page >= mTotalPage) {
            return mTotalPage;
        }
        return 1;
    }


    /**
     * 获取总页数
     * 公式：（总数据量+每页显示的数量-1）/每页显示的数量
     *
     * @param totalSize
     * @return
     */
    private int getTotalPage(int totalSize) {
        return (totalSize + 12 - 1) / 12;
    }


    /**
     * 需要在initializeView方法之前调用,如果没有设置，默认为1
     *
     * @param currPage
     */
    public void setCurrPage(int currPage) {
        mCurrPage = checkPage(currPage);
    }

    /**
     * 下一页
     */
    public void nextPage() {
        mCurrPage++;
        mCurrPage = checkPage(mCurrPage);
    }

    //获得系统图片资源
    private void scanAllList() {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{"_id", "_display_name", "_data", "_size"}
                , null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ImageInfo image = new ImageInfo();

                image.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)));
                Log.d(BIG, "imageId==: " + image.getId());
                image.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                image.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                image.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                image.setData(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                imageList.add(image);
                Log.d(BIG, "imageList.size()==: " + imageList.size());
            }
            cursor.close();
        }
    }


    private void scanAllThumbnail() {
        for (ImageInfo info : imageList) {
            try {
//                String path = isSdcard() + "/Pictures";
//                List<String> docFileNamelist = getDocFileName(path);
//                Log.d(TAG, "docFileName==: " + docFileNamelist.size());
//
//                if (docFileNamelist.size() > 0) {
//                    for (int i = 0; i < docFileNamelist.size(); i++) {
//                        if (info.getPath().equals(docFileNamelist.get(i))) {
//
//                            Log.d(TAG, "path==: "+docFileNamelist.get(i));
////                            Bitmap GlideBitmap = Glide.with(context).load(docFileNamelist.get(i)).asBitmap().centerCrop().into(140, 140).get();
////                            info.thumbnail = ThumbnailUtils.extractThumbnail(GlideBitmap, 140, 140, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//                        }
//                    }
//                }

//                InputStream stream = new FileInputStream(new File(info.getPath()));
//                BitmapFactory.Options option = new BitmapFactory.Options();
//                option.inSampleSize = 1;
//                Bitmap imageBitmap = BitmapFactory.decodeStream(stream, null, option);
               // Bitmap GlideBitmap = Glide.with(context).load(info.getPath()).asBitmap().centerCrop().into(140, 140).get();
//                info.thumbnail = Glide.with(context).load(info.getPath()).asBitmap().centerCrop().into(140, 140).get();;//ThumbnailUtils.extractThumbnail(GlideBitmap, 140, 140, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

               // imageBitmap.recycle();


            } catch (Exception e) {
                e.printStackTrace();
                imageList.remove(info);

            }
        }
    }


    private void scanImageIcon() {
        for (ImageInfo i : imageList) {
            try {
                i.loadIcon();
            } catch (Exception e) {
                e.printStackTrace();
                imageList.remove(i);
            }
        }
    }

    public void doScan() {
        new ScanImageThread().start();
    }


    private class ScanImageThread extends Thread {
        @Override
        public void run() {
            super.run();

            Log.i(TAG, "scan image start");
            scanAllList();
            Log.i(TAG, "scan image end get list");
            listReady.set(true);
            scanAllThumbnail();
            Log.i(TAG, "scan image end get thumbnail");
            scanImageIcon();
            isProviderReady = true;



        }
    }

    public void ReScanSd(Context context, String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(new File(path));
            Log.d(TAG, "Environment.getExternalStorageDirectory=== " + contentUri);

            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
    }

    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(context,
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                      //  ReScanSd(context,path);
                        Log.d("ExternalStorage", "Scanned=== " + path + ":");
                        Log.d("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    private void saveBitmap(String path) {

        Bitmap bitmap = null;
       // try {
          //  bitmap = Glide.with(context).load(path).asBitmap().centerCrop().into(500, 500).get();
            String bitname = path.substring(0, path.lastIndexOf("."))+ ".png";
            saveMyBitmap(bitmap, bitname);
            deleteBMP(path);
            updateGallery(bitname);

      //  } //catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }


    private void saveMyBitmap(Bitmap mBitmap, String bitName) {

        File f = new File(bitName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            updateGallery(bitName);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteBMP(String path) {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        }
    }

    private List<String> picList;

    private List<String> getDocFileName(String dirPath) {
        Log.i(TAG, "dirPath===>" + dirPath);
        if (dirPath != null) {
            picList = new ArrayList<>();
            File file = new File(dirPath);
            final File[] listFiles = file.listFiles();

            for (int i = 0; i < listFiles.length; i++) {
                // 判断是否为文件夹
                if (!listFiles[i].isDirectory()) {
                    // 获取文件名
                    String fileName = listFiles[i].getName();
                    // 获取文件绝对路径
                    String filePath = listFiles[i].getAbsolutePath();
                    // 获取文件后缀名
                    String lastName = fileName.substring(fileName
                            .lastIndexOf(".") + 1);
                    // 如果文件后缀为bmp，添加到list
                    if (fileName.trim().toLowerCase().endsWith(".bmp")||fileName.trim().toLowerCase().endsWith(".gif")) {
                        picList.add(filePath);
                        Log.i(TAG, "picList.size==" + picList.size());
                    }

                }
            }
            return picList;
        }

        return null;
    }

    public String isSdcard() {

        File sdCardDir = null;
        String sdPath = "";
        // 判断sd卡是否存在
        boolean isSdExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (isSdExist) {
            // 如果存在sd卡，就找到根目录
            sdCardDir = Environment.getExternalStorageDirectory();
            sdPath = sdCardDir.getAbsolutePath();

            return sdPath;
        } else {
            return null;
        }
    }



    public String currenImage;

    public String getNextImagePath(String currentPath) {
        String FileName = "";
        if (imageList != null) {
            for (int i = 0; i < imageList.size(); i++) {
                ImageInfo Infos = imageList.get(i);
                if (Infos.getData().equals(currentPath)) {
                    if (i == imageList.size() - 1) {
                        FileName = "END";
                    } else {
                        FileName = imageList.get(i + 1).getData();
                        currenImage = imageList.get(i + 1).getPath();
                    }

                }

            }

        }

        return FileName;
    }

    public String getPrevImagePath(String currentPath) {
        String FileName = "";
        if (null != imageList) {
            for (int i = 0; i < imageList.size(); i++) {
                ImageInfo Infos = imageList.get(i);
                if (Infos.getData().equals(currentPath)) {
                    if (i == 0) {
                        FileName = "START";
                    } else {
                        FileName = imageList.get(i - 1).getData();
                        currenImage = imageList.get(i - 1).getPath();
                    }
                }

            }
        }
        return FileName;
    }


}

