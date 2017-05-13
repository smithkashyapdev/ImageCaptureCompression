package development.kashyap.smith.gallarycapturelibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import development.kashyap.smith.gallarycapturelibrary.callback.CompressorThread;
import development.kashyap.smith.gallarycapturelibrary.callback.GetAllImages;
import development.kashyap.smith.gallarycapturelibrary.model.GalleryMode;
import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hi on 07-01-2017.
 */

public class BottomDialog extends BottomSheetDialogFragment  {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    @BindView(R.id.cameraCardView)
    CardView cameraCardView;

    @BindView(R.id.singleCardView)
    CardView singleCardView;

    @BindView(R.id.multipleCardView)
    CardView multipleCardView;

    @BindView(R.id.cancelCardView)
    CardView cancelCardView;



    Intent mIntent;
    ImageData singleCaptureImageData;
    private Uri photoURI;

    public  static BottomDialog getInstance(Context mContext, GetAllImages mGetAllImages){
        CompatUtils.mGetAllImages= mGetAllImages;
        return new BottomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this,v);
        return v;
    }


    @OnClick (R.id.cameraCardView)
    void openCamera(View v){
        CompatUtils.mode=GalleryMode.CAMERA;
        dispatchTakePictureIntent();
    }

    @OnClick (R.id.singleCardView)
    void openSingleGallery(View v){
        CompatUtils.mode=GalleryMode.SINGLE_CHOICE;
        sendAnotherGallery();
    }

    @OnClick (R.id.multipleCardView)
    void openMultiple(View v){
        CompatUtils.mode=GalleryMode.MULTIPLE_CHOICE;
        sendAnotherGallery();
    }

    @OnClick (R.id.cancelCardView)
    void cancelDialog(View v){
        this.dismiss();
    }





    void sendAnotherGallery(){
        mIntent=new Intent(getActivity(),MainActivity.class);
        startActivity(mIntent);
        this.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            singleCaptureImageData=new ImageData("",photoURI,false);
            singleCaptureImageData.setRealPath(mCurrentPhotoPath);
            singleLocking(singleCaptureImageData);
        }
    }


    void singleLocking(final ImageData mImageData){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mImageData){
                    Looper.prepare();
                    CompressionUtil.getInstance().compressLockingImage(getActivity(),mImageData.getsFileUri().toString(),mImageData,(AppCompatActivity)getActivity(),BottomDialog.this);
                    Looper.loop();
                }


            }
        }).start();


    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    public CardView setCameraCardViewVisibility(){

       return cameraCardView;
    }

    public CardView setSingleChooserCardViewVisibility(){

        return singleCardView;
    }


    public CardView setMultipleCardViewVisibility(){

        return multipleCardView;
    }


}
