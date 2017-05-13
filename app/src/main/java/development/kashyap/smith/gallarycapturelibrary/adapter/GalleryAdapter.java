package development.kashyap.smith.gallarycapturelibrary.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import development.kashyap.smith.gallarycapturelibrary.CircleCheckBox;
import development.kashyap.smith.gallarycapturelibrary.CompatUtils;
import development.kashyap.smith.gallarycapturelibrary.CompressionUtil;
import development.kashyap.smith.gallarycapturelibrary.R;
import development.kashyap.smith.gallarycapturelibrary.callback.ClickChecked;
import development.kashyap.smith.gallarycapturelibrary.callback.CompressorThread;
import development.kashyap.smith.gallarycapturelibrary.model.GalleryMode;
import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

/**
 * Created by hi on 07-01-2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    Context mContext;
    ArrayList<ImageData> mImageDataArrayList;
    ClickChecked mClickChecked;
    AppCompatActivity mAppCompatActivity;

    public GalleryAdapter(Context mContext, ArrayList<ImageData> mImageDataArrayList,ClickChecked mClickChecked) {
        this.mContext = mContext;
        mAppCompatActivity=(AppCompatActivity)mContext;
        this.mImageDataArrayList = mImageDataArrayList;
        this.mClickChecked=mClickChecked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_select, parent, false);
        return new GalleryAdapter.ViewHolder(v,mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        CompatUtils.getInstance().runEnterAnimation(holder.itemView,position);
       final ImageData mImageData=mImageDataArrayList.get(position);

        if (mImageData.getsFileUri() != null) {


            Glide.with(mContext)
                    .load(mImageData.getsFileUri().toString())
                    .thumbnail(0.1f)
                    //.fit()
                    .dontAnimate()
                    //   .override(holder.mThumbnail.getWidth(), holder.mThumbnail.getWidth())
                    //  .override(holder.root.getWidth(), holder.root.getWidth())
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                     .into(holder.mAppCompatImageView);


        }


        holder.mCircleCheckBox.setTag(mImageData);
        holder.mCircleCheckBox.setChecked(mImageData.isChecked());

        holder.mCircleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ImageData mImageData=(ImageData)view.getTag();
                if (CompatUtils.mode== GalleryMode.SINGLE_CHOICE){

                    mImageData.setChecked(!mImageData.isChecked()?true:false);
                    holder.mCircleCheckBox.setChecked(mImageData.isChecked());
                    singleLocking(mImageData);


                } else if (CompatUtils.mode==GalleryMode.MULTIPLE_CHOICE) {
                    mImageData.setChecked(!mImageData.isChecked()?true:false);
                    holder.mCircleCheckBox.setChecked(mImageData.isChecked());
                    mClickChecked.checked(mImageData);
                }

            }
        });



    }

    void singleLocking(final ImageData mImageData){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mImageData){
                    Looper.prepare();
                    CompressionUtil.getInstance().compressLockingImage(mContext,mImageData.getsFileUri().toString(),mImageData,mAppCompatActivity,null);
                    Looper.loop();
                }
            }
        }).start();


    }

    @Override
    public int getItemCount() {
        return mImageDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       @BindView(R.id.cardCircleCheckBox) CircleCheckBox mCircleCheckBox;
       @BindView(R.id.cardAppCompatImageView) AppCompatImageView mAppCompatImageView;

        public ViewHolder(View itemView,final Context mCont) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
