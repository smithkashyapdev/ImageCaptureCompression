package development.kashyap.smith.gallarycapturelibrary.callback;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import development.kashyap.smith.gallarycapturelibrary.CompressionUtil;
import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

/**
 * Created by hi on 07-01-2017.
 */

public class CompressorThread extends Thread {

  private final   ImageData mImageData;
    Context mContext;

    public CompressorThread(ImageData mImageData, Context mContext) {
        this.mImageData = mImageData;
        this.mContext = mContext;


    }


    @Override
    public void run() {
                CompressionUtil
                .getInstance()
                .compressImage(mContext,mImageData.getsFileUri().toString(),mImageData);



    }




}
