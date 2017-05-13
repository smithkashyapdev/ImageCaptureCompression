package development.kashyap.smith.gallarycapturelibrary.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;

/**
 * Created by hi on 07-01-2017.
 */

public class ImageData implements Parcelable {

    String sFileName;
    Uri sFileUri;
    boolean isChecked;

    String realPath;


    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public ImageData(String sFileName, Uri sFileUri, boolean isChecked) {
        this.sFileName = sFileName;
        this.sFileUri = sFileUri;
        this.isChecked = isChecked;
    }

    protected ImageData(Parcel in) {
        sFileName = in.readString();
        isChecked = in.readByte() != 0;
    }

    public String getsFileName() {
        return sFileName;
    }

    public void setsFileName(String sFileName) {
        this.sFileName = sFileName;
    }

    public Uri getsFileUri() {
        return sFileUri;
    }

    public void setsFileUri(Uri sFileUri) {
        this.sFileUri = sFileUri;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sFileName);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
