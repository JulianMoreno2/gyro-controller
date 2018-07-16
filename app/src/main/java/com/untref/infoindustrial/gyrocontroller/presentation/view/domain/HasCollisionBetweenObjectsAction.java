package com.untref.infoindustrial.gyrocontroller.presentation.view.domain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

public class HasCollisionBetweenObjectsAction {

    public boolean execute(View object, View obstacle, View obstacle2) {
        int x1 = Math.round(object.getX());
        int y1 = Math.round(object.getY());
        int x2 = Math.round(obstacle.getX());
        int y2 = Math.round(obstacle.getY());
        int x3 = Math.round(obstacle2.getX());
        int y3 = Math.round(obstacle2.getY());

        Bitmap objectBitmap = getViewBitmap(object);
        Bitmap obstacle1Bitmap = getViewBitmap(obstacle);
        Bitmap obstacle2Bitmap = getViewBitmap(obstacle2);

        if (objectBitmap == null || obstacle1Bitmap == null) {
            throw new IllegalArgumentException("bitmaps cannot be null");
        }

        Rect objectBounds = new Rect(x1, y1, x1 + objectBitmap.getWidth(), y1 + objectBitmap.getHeight());
        Rect obstacle1Bounds = new Rect(x2, y2, x2 + obstacle1Bitmap.getWidth(), y2 + obstacle1Bitmap.getHeight());
        Rect obstacle2Bounds = new Rect(x3, y3, x3 + obstacle2Bitmap.getWidth(), y3 + obstacle2Bitmap.getHeight());

        if (Rect.intersects(objectBounds, obstacle1Bounds)) {
            Rect collisionBounds = getCollisionBounds(objectBounds, obstacle1Bounds);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = objectBitmap.getPixel(i - x1, j - y1);
                    int bitmap2Pixel = obstacle1Bitmap.getPixel(i - x2, j - y2);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap2Pixel)) {
                        objectBitmap.recycle();
                        obstacle1Bitmap.recycle();
                        return true;
                    }
                }
            }
        }

        if (Rect.intersects(objectBounds, obstacle2Bounds)) {
            Rect collisionBounds = getCollisionBounds(objectBounds, obstacle2Bounds);
            for (int i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (int j = collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int bitmap1Pixel = objectBitmap.getPixel(i - x1, j - y1);
                    int bitmap3Pixel = obstacle2Bitmap.getPixel(i - x3, j - y3);
                    if (isFilled(bitmap1Pixel) && isFilled(bitmap3Pixel)) {
                        objectBitmap.recycle();
                        objectBitmap = null;
                        obstacle2Bitmap.recycle();
                        obstacle2Bitmap = null;
                        return true;
                    }
                }
            }
        }

        objectBitmap.recycle();
        objectBitmap = null;
        obstacle1Bitmap.recycle();
        obstacle1Bitmap = null;
        obstacle2Bitmap.recycle();
        obstacle2Bitmap = null;
        return false;
    }

    private static Bitmap getViewBitmap(View v) {
        if (v.getMeasuredHeight() <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left = Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right = Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }
}
