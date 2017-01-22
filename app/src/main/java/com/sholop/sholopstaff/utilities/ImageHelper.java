package com.sholop.sholopstaff.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sholop.sholopstaff.R;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public static void loadNetworkImage(Context ctx, NetworkImageView iv, String url){
        ImageLoader mImageLoader;
        url = (url == null || url.equals("")) ? null : url;
        mImageLoader = CustomVolleyRequestQueue.getInstance(ctx.getApplicationContext())
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
//        url = "http://eskipaper.com/images/beautiful-girls-5.jpg";
//        url = new SessionManager(ctx).getCurrentUserImageURL();
        try {
            mImageLoader.get(url, ImageLoader.getImageListener(iv,
                    R.drawable.image_loading, R.drawable.ic_error_black_24dp));
            iv.setImageUrl(url, mImageLoader);
        }catch (NullPointerException e){
            iv.setDefaultImageResId(R.drawable.ic_black_person_24dp);
        }catch (Exception e){
            iv.setDefaultImageResId(R.drawable.ic_error_black_24dp);
        }
    }

    public static void loadPostNetworkImage(Context ctx, NetworkImageView iv, String url){
        ImageLoader mImageLoader;
        url = (url == null || url.equals("")) ? null : url;
        mImageLoader = CustomVolleyRequestQueue.getInstance(ctx.getApplicationContext())
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
//        url = "http://eskipaper.com/images/beautiful-girls-5.jpg";
//        url = new SessionManager(ctx).getCurrentUserImageURL();
        try {
            mImageLoader.get(url, ImageLoader.getImageListener(iv,
                    R.drawable.ic_timelapse_black_24dp, R.drawable.ic_error_black_24dp));
            iv.setImageUrl(url, mImageLoader);
        }catch (NullPointerException e){
            iv.setVisibility(View.GONE);
        }catch (Exception e){
            iv.setDefaultImageResId(R.drawable.ic_error_black_24dp);
        }
    }
}