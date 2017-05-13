package development.kashyap.smith.gallarycapturelibrary;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import development.kashyap.smith.gallarycapturelibrary.adapter.GalleryAdapter;
import development.kashyap.smith.gallarycapturelibrary.callback.ClickChecked;
import development.kashyap.smith.gallarycapturelibrary.callback.CompressorThread;
import development.kashyap.smith.gallarycapturelibrary.model.GalleryMode;
import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

public class MainActivity extends AppCompatActivity {

    ArrayList<ImageData> mImageDataArrayList;
    GalleryAdapter mGalleryAdapter;
    Runnable mRunnable;
    Handler mHandler;

    ArrayList<ImageData> checkedArrayList;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recyclerImageView)
    RecyclerView recyclerImageView;
    private GridLayoutManager linearLayoutManager;

    @BindView(R.id.textCount)
    TextView textCount;

    @BindView(R.id.checkBlack)
    ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_choice_activity);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayoutManager=new GridLayoutManager(MainActivity.this,2);
        recyclerImageView.setLayoutManager(linearLayoutManager);
        recyclerImageView.setHasFixedSize(true);
        init();
    }



    void init(){
        mImageDataArrayList=new ArrayList<>();
        checkedArrayList=new ArrayList<>();
        mGalleryAdapter=new GalleryAdapter(MainActivity.this, mImageDataArrayList, new ClickChecked() {
            @Override
            public void checked(ImageData mImageData) {

                if (CompatUtils.mode== GalleryMode.SINGLE_CHOICE) {
                     checkedArrayList.clear();
                    checkedArrayList.add(mImageData);


                }
                else if (CompatUtils.mode== GalleryMode.MULTIPLE_CHOICE){
                    if (checkedArrayList.contains(mImageData)) {
                        checkedArrayList.remove(mImageData);

                    }
                    else {
                        checkedArrayList.add(mImageData);
                    }
                }



                textCount.post(new Runnable() {
                    @Override
                    public void run() {
                        textCount.setText(checkedArrayList.size()+" item Selected");
                    }
                });
            }
        });
        recyclerImageView.setAdapter(mGalleryAdapter);
        Thread mThread= new GalleryThread(mImageDataArrayList,MainActivity.this);
        mThread.start();



        mRunnable=new Runnable() {
            @Override
            public void run() {

                synchronized (mImageDataArrayList){

                    if (mImageDataArrayList.size()==0) {
                        try {
                            mImageDataArrayList.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mGalleryAdapter.notifyDataSetChanged();

                    }
                }

            }
        };

        mHandler=new Handler(getMainLooper());
        mHandler.post(mRunnable);

    }


    @OnClick(R.id.checkBlack)
    void getImages(View  view){

        if (checkedArrayList.size()>0) {

           final ExecutorService executor = Executors.newFixedThreadPool(5);

            for (ImageData mImageData:checkedArrayList){

                executor.execute(new CompressorThread(mImageData,MainActivity.this));
            }

            executor.shutdown();
            try {

              boolean exit=  executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

                if (exit&&  CompatUtils.mGetAllImages!=null) {
                    if (CompatUtils.mode== GalleryMode.SINGLE_CHOICE) {
                        CompatUtils.mGetAllImages.getSingleImage(checkedArrayList.get(0));
                    }
                    else if (CompatUtils.mode== GalleryMode.MULTIPLE_CHOICE){
                        CompatUtils.mGetAllImages.getMultipleImage(checkedArrayList);
                        finish();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }

        Toast.makeText(getApplicationContext(),checkedArrayList.size()+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
               finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
