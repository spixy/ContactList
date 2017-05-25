package contactlist.spixy.android.uib.contactlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;

/**
 * Created by spixy on 24.5.2017.
 */

public class Utility
{
    public static byte[] BitmapToBytes(Bitmap bitmap)
    {
        if (bitmap == null)
            return null;

        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(byteBuffer);
        return byteBuffer.array();
    }

    public static Bitmap BytesToBitmap(byte[] byteArray)
    {
        if (byteArray == null)
            return null;

        return BitmapFactory.decodeByteArray(byteArray, 0 , byteArray.length);
    }
}
