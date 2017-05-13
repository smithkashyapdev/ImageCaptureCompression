package development.kashyap.smith.gallarycapturelibrary;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;


import java.util.ArrayList;

import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

/**
 * Created by hi on 07-01-2017.
 */

public class GalleryThread extends Thread {

    private final ArrayList<ImageData> mImageDataArrayList;
    Context mContext;

    public GalleryThread(@NonNull ArrayList<ImageData> mImageDataArrayList, Context mContext) {
        this.mImageDataArrayList = mImageDataArrayList;
        this.mContext=mContext;
    }

    @Override
    public void run() {
        synchronized (mImageDataArrayList)

        {
            if (mImageDataArrayList.size()!= 0) {
                try {
                    Log.e("Queue is full"," waiting");
                    mImageDataArrayList.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }


            }
            else {
                 getImagesFromGallery(mContext,mImageDataArrayList);

            }



        }

            }


    public void getImagesFromGallery(Context context, ArrayList<ImageData> images) {



        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";


            imageCursor = context.getApplicationContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, null, null, orderBy);
            while (imageCursor.moveToNext()) {
                Uri uri = Uri.parse(imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA)));

                String fileName = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                images.add(new ImageData(fileName,uri,false));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
                images.notify();
            }


        }



    }

}
