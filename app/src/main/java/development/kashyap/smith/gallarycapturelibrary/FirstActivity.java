package development.kashyap.smith.gallarycapturelibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import development.kashyap.smith.gallarycapturelibrary.callback.GetAllImages;
import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

/**
 * Created by hi on 08-01-2017.
 */


public class FirstActivity extends AppCompatActivity implements GetAllImages{


    @BindView(R.id.getTextCardView)
    CardView getTextCardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.getTextCardView)
    void getBottom(View v){
        final BottomDialog mBottomDialog=BottomDialog.getInstance(FirstActivity.this,this);
        mBottomDialog .show(getSupportFragmentManager(),mBottomDialog.getTag());
    }

    @Override
    public void getCaptureImage(ImageData image) {

        Toast.makeText(getApplicationContext(),image.getRealPath(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getSingleImage(ImageData image) {
        Toast.makeText(getApplicationContext(),image.getRealPath(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMultipleImage(ArrayList<ImageData> image) {
        Toast.makeText(getApplicationContext(),image.size()+"",Toast.LENGTH_SHORT).show();
    }
}
