package development.kashyap.smith.gallarycapturelibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.io.File;

import development.kashyap.smith.gallarycapturelibrary.callback.GetAllImages;
import development.kashyap.smith.gallarycapturelibrary.model.GalleryMode;

/**
 * Created by hi on 07-01-2017.
 */

public class CompatUtils {


    public  static GetAllImages mGetAllImages;
    public static GalleryMode mode;


    public static final String DIRECTORY_NAME = "Compress";
    public static String INTERNAL_PATH = Environment.getExternalStorageDirectory().toString() +  File.separator + DIRECTORY_NAME;

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);


    }



    public static CompatUtils getInstance(){

        return new CompatUtils();
    }


    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private int lastAnimatedPosition = -1;
    public  void runEnterAnimation(View view, int position) {
        if (animationsLocked) return;

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    })
                    .start();
        }
    }

}
