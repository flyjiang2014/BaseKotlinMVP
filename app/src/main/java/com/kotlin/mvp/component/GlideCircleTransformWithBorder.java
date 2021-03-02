package com.kotlin.mvp.component;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import org.jetbrains.annotations.NotNull;
import java.security.MessageDigest;
import androidx.core.content.ContextCompat;

/**
 * Created by ${flyjiang} on 2019/8/23.
 * 文件说明：
 */
public class GlideCircleTransformWithBorder extends BitmapTransformation {
    private Paint mBorderPaint;
    private float mBorderWidth;

    public GlideCircleTransformWithBorder(Context context) {
    }

    public GlideCircleTransformWithBorder(Context context, int borderWidth, int borderColor) {
        mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (mBorderPaint != null) {
            float borderRadius = r - mBorderWidth / 2;
            canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NotNull MessageDigest messageDigest) {

    }

    public static BitmapDrawable transformDrawable(Context context, int resourceId, Transformation<Bitmap> transformation) {
        Drawable drawable = ContextCompat.getDrawable(context, resourceId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getMinimumWidth(), drawable.getMinimumHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable.draw(canvas);
        Resource<Bitmap> original = BitmapResource.obtain(bitmap, GlideApp.get(context).getBitmapPool());
        Resource<Bitmap> rounded = transformation.transform(context, original, bitmap.getWidth(), bitmap.getHeight());
        if (!original.equals(rounded)) {
            original.recycle();
        }
        return new BitmapDrawable(context.getResources(), rounded.get());
    }
}
