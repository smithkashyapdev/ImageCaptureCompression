package development.kashyap.smith.gallarycapturelibrary.callback;

import java.util.ArrayList;

import development.kashyap.smith.gallarycapturelibrary.model.ImageData;

/**
 * Created by hi on 07-01-2017.
 */

public interface GetAllImages {


    void getCaptureImage(ImageData image);

    void getSingleImage(ImageData image);

    void getMultipleImage(ArrayList<ImageData> image);

}
