package com.example.mvpdemo.sticker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.EditText;


/**
 * Created by wangchao on 16/10/13.
 */
public class StickerEditText extends EditText implements Stickerable {

    private boolean mEdit = true;

    private Paint mPaint;

    private int RADIUS = (int) (4 * 12.5);

    private static float MIN_TEXT_SIZE;

    private StickerView.OnStickerActionListener listener;

    private Context mContext;

    public StickerEditText(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public StickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public StickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StickerEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        MIN_TEXT_SIZE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,mContext.getResources().getDisplayMetrics());
        setPadding(2 * RADIUS, 2 * RADIUS, 2 * RADIUS, 2 * RADIUS);
        setPivotX(RADIUS);
        setPivotY(RADIUS);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setPathEffect(new DashPathEffect(new float[]{6, 3}, 1));
        sticker = new Sticker();

    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mPaint.setColor(color);
    }

    // 触控模式
    private int mode;
    private static final int NONE = 0; // 无模式
    private static final int TRANS = 1; // 拖拽模式
    private static final int ROTATE = 2; // 单点旋转模式
    private static final int ZOOM_SINGLE = 3; // 单点缩放模式
    private static final int ZOOM_MULTI = 4; // 多点缩放模式

    private Sticker sticker;

    // 手指按下屏幕的X坐标
    private float downX;
    // 手指按下屏幕的Y坐标
    private float downY;
    // 手指之间的初始距离
    private float oldDistance;
    // 手指之间的初始角度
    private float oldRotation;

    private float lastUpX;
    private float lastUpY;
    private float lastUpScale;
    private float lastUpRotation;
    private PointF mP0;
    private PointF mP1;

    private Matrix downMatrix = new Matrix();
    // 手指移动时图片的矩阵
    private Matrix moveMatrix = new Matrix();
    // 多点触屏时的中心点
    private PointF midPoint = new PointF();
    // 图片的中心点坐标
    private PointF imageMidPoint = new PointF();
    // 关闭按钮的中心点坐标
    private PointF closeMidPoint = new PointF();

    private boolean inRemoveAction(MotionEvent event) {
        boolean b = event.getX(0) >= 0 && event.getX(0) <= 2 * RADIUS && event.getY(0) >= 0 && event.getY(0) <= 2 * RADIUS;
        return b;
    }

    private boolean inZoomAction(MotionEvent event) {
        boolean b = event.getX(0) >= getMeasuredWidth() - 2 * RADIUS && event.getX(0) <= getMeasuredWidth() && event.getY(0) >= getMeasuredHeight() - 2 * RADIUS && event.getY(0) <= getMeasuredHeight();
        return b;
    }

    private boolean isInStickerArea(MotionEvent event) {
        boolean b = event.getX(0) >= 0 && event.getX(0) <= getMeasuredWidth() && event.getY(0) >= 0 && event.getY(0) <= getMeasuredHeight();
        return b;
    }

    public void setOnStickerActionListener(StickerView.OnStickerActionListener listener) {
        this.listener = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("StickerEditText","onTouchEvent   action = " + MotionEvent.actionToString(event.getAction()) + "  mode = " + mode);
        int action = MotionEventCompat.getActionMasked(event);
        boolean isStickerOnEdit = true;
        if (action == MotionEvent.ACTION_POINTER_DOWN && mode == ZOOM_MULTI) {
            midPoint = sticker.getMidPoint(event);
            setPivotX(midPoint.x);
            setPivotY(midPoint.y);
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                if (sticker == null) return false;
                // 删除操作
                if (inRemoveAction(event)) {
                    if (listener != null) {
                        listener.onDelete();
                    }
                }
                // 单点缩放/旋转手势验证
                else if (inZoomAction(event)) {
                    mode = ZOOM_SINGLE;
//                    downMatrix.set(sticker.getMatrix());
                    closeMidPoint = new PointF(getTranslationX() + RADIUS, getTranslationY() + RADIUS);
                    oldRotation = sticker.getSpaceRawRotation(event, closeMidPoint);
                    oldDistance = sticker.getSingleTouchRawDistance(event, closeMidPoint);

                    Log.d("StickerEditText", "onTouchEvent------单点缩放手势/旋转手势验证---oldDistance=" + oldDistance);
                }
                // 平移手势验证
                else if (isInStickerArea(event)) {
                    mode = TRANS;
//                    downMatrix.set(sticker.getMatrix());
                    Log.d("StickerEditText", "平移手势");
                } else {
                    isStickerOnEdit = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // 多点触控
//                Log.i("StickerEditText","ACTION_POINTER_DOWN");
//                mode = ZOOM_MULTI;
//                oldDistance = sticker.getMultiTouchDistance(event);
//                oldRotation = sticker.getMultiTouchRotation(event);
////                if (mP0 == null) {
////                    mP0 = new PointF();
////                    mP1 = new PointF();
////                }
//
////                mP0.set(event.getX(),event.getY());
////                mP1.set(event.getX(1),event.getY(1));
////                mPivotX = event.getX();
//                mPivotY = event.getY();
                midPoint = sticker.getMidPoint(event);
                setPivotX(midPoint.x);
                setPivotY(midPoint.y);
//                downMatrix.set(sticker.getMatrix());
                break;
            case MotionEvent.ACTION_MOVE:
                // 单点缩放/单点旋转
                if (mode == ZOOM_SINGLE) {
//                    moveMatrix.set(downMatrix);
                    float scale = sticker.getSingleTouchRawDistance(event, closeMidPoint) / oldDistance;
                    float deltaRotation = sticker.getSpaceRawRotation(event, closeMidPoint) - oldRotation;
//                    moveMatrix.postRotate(deltaRotation, closeMidPoint.x, closeMidPoint.y);
//                    moveMatrix.postScale(scale, scale, closeMidPoint.x, closeMidPoint.y);
//                    sticker.getMatrix().set(moveMatrix);
                    setPivotX(RADIUS);
                    setPivotY(RADIUS);
//                    setScaleX(lastUpScale * scale);
//                    setScaleY(lastUpScale * scale);
                    if (getTextSize() <= MIN_TEXT_SIZE && scale < 1) {

                    } else {
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, lastUpScale * scale);
                    }
                    setRotation(lastUpRotation + deltaRotation);
                    invalidate();
                }
////                // 多点缩放
//                else if (mode == ZOOM_MULTI) {
//
////                    float x1 = event.getX(1);
////                    float y1 = event.getY(1);
//
////                    if (mPivotX >= 0 && mPivotY >= 0 && x1 >= 0 && x1 <= getMeasuredWidth() && y1 >= 0 && y1 <= getMeasuredHeight()) {
////                        Log.i("sss", "onTouchEvent   event = " + MotionEvent.actionToString(event.getAction()) + "    rawx = " + event.getRawX() + "    rawy = " + event.getRawY() + "   x = " + event.getX() + "    y = " + event.getY() + "    x1 = " + x1 + "  y1 = " + y1);
//
//                    float scale = sticker.getMultiTouchDistance(event) / oldDistance;
//                    float deltaRotation = sticker.getMultiTouchRotation(event) - oldRotation;
////                    moveMatrix.postRotate(deltaRotation, midPoint.x, midPoint.y);
////                    moveMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
////                    sticker.getMatrix().set(moveMatrix);
//                    setPivotX(midPoint.x);
//                    setPivotY(midPoint.y);
//                    Log.i("fff", "多点缩放   scale = " + scale + "   rotation = " + deltaRotation);
//
////                    setScaleX(lastUpScale * scale);
////                    setScaleY(lastUpScale * scale);
//                    setTextSize(TypedValue.COMPLEX_UNIT_PX, lastUpScale * scale);
//                    setRotation(lastUpRotation + deltaRotation);
//                    invalidate();
////                    }
////                    moveMatrix.set(downMatrix);
//
//                }
                // 平移
                else if (mode == TRANS) {
//                    moveMatrix.set(downMatrix);
//                    moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
//                    sticker.getMatrix().set(moveMatrix);
//                    invalidate();
                    setTranslationX(lastUpX + event.getRawX() - downX);
                    setTranslationY(lastUpY + event.getRawY() - downY);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                if (mode == TRANS && Math.abs(getTranslationY() - lastUpY) < 10 && Math.abs(getTranslationX() - lastUpX) < 10) {
                    requestFocus();
                    int offsetForPosition = getOffsetForPosition(event.getX(), event.getY());
                    setSelection(offsetForPosition);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            StickerUtils.showKeyboard(mContext);
                        }
                    },1000);
                }
                mode = NONE;
                midPoint = null;
                imageMidPoint = null;
                closeMidPoint = null;
//                mPivotX = -1;
//                mPivotY = -1;
                lastUpX = getTranslationX();
                lastUpY = getTranslationY();
                lastUpScale = getTextSize();
                lastUpRotation = getRotation();
                break;
            default:
                break;
        }
        if (isStickerOnEdit && listener != null) {
            listener.onEdit(this);
        }
        return isStickerOnEdit;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mEdit) {
            float radius = RADIUS / getScaleX();

            int measuredWidth = getMeasuredWidth();

            canvas.drawLine(radius, radius, getMeasuredWidth() - radius, radius, mPaint);
            canvas.drawLine(radius, radius, radius, getMeasuredHeight() - radius, mPaint);
            canvas.drawLine(radius, getMeasuredHeight() - radius, getMeasuredWidth() - radius, getMeasuredHeight() - radius, mPaint);
            canvas.drawLine(getMeasuredWidth() - radius, radius, getMeasuredWidth() - radius, getMeasuredHeight() - radius, mPaint);


            canvas.drawCircle(radius, radius, radius, mPaint);
            canvas.drawCircle(getMeasuredWidth() - radius, getMeasuredHeight() - radius, radius, mPaint);
        }
    }


    @Override
    public void setEdit(boolean edit) {
        mEdit = edit;
        if (!edit) {
            clearFocus();
        }
        postInvalidate();
    }

    @Override
    public void bringFront() {
        super.bringToFront();
    }

    @Override
    public void multi(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN: // 多点触控
                Log.i("StickerEditText","ACTION_POINTER_DOWN");
                mode = ZOOM_MULTI;
                oldDistance = sticker.getMultiTouchDistance(event);
                oldRotation = sticker.getMultiTouchRotation(event);
//                if (mP0 == null) {
//                    mP0 = new PointF();
//                    mP1 = new PointF();
//                }

//                mP0.set(event.getX(),event.getY());
//                mP1.set(event.getX(1),event.getY(1));
//                mPivotX = event.getX();
//                mPivotY = event.getY();
                midPoint = sticker.getMidPoint(event);
//                setPivotX(midPoint.x);
//                setPivotY(midPoint.y);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM_MULTI) {
                    Log.i("StickerEditText","multi  x = " + event.getX() + "    y = " + event.getY() + "    x1 = " + event.getX(1) + "      y1 = " + event.getY(1));


//                    float x1 = event.getX(1);
//                    float y1 = event.getY(1);

//                    if (mPivotX >= 0 && mPivotY >= 0 && x1 >= 0 && x1 <= getMeasuredWidth() && y1 >= 0 && y1 <= getMeasuredHeight()) {
//                        Log.i("sss", "onTouchEvent   event = " + MotionEvent.actionToString(event.getAction()) + "    rawx = " + event.getRawX() + "    rawy = " + event.getRawY() + "   x = " + event.getX() + "    y = " + event.getY() + "    x1 = " + x1 + "  y1 = " + y1);

                    float scale = sticker.getMultiTouchDistance(event) / oldDistance;
                    float deltaRotation = sticker.getMultiTouchRotation(event) - oldRotation;
//                    moveMatrix.postRotate(deltaRotation, midPoint.x, midPoint.y);
//                    moveMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
//                    sticker.getMatrix().set(moveMatrix);

                    Log.i("fff", "多点缩放   scale = " + scale + "   rotation = " + deltaRotation);

//                    setScaleX(lastUpScale * scale);
//                    setScaleY(lastUpScale * scale);
                    if (getTextSize() <= MIN_TEXT_SIZE && scale < 1) {

                    } else {
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, lastUpScale * scale);
                    }
//                    setTextSize(TypedValue.COMPLEX_UNIT_PX, lastUpScale * scale);
                    setRotation(lastUpRotation + deltaRotation);
                    invalidate();
//                    }
//                    moveMatrix.set(downMatrix);

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
//                if (mode == TRANS && Math.abs(getTranslationY() - lastUpY) < 10 && Math.abs(getTranslationX() - lastUpX) < 10) {
//                    requestFocus();
//                    int offsetForPosition = getOffsetForPosition(event.getX(), event.getY());
//                    setSelection(offsetForPosition);
//                    StickerUtils.showKeyboard(mContext);
//                }
                mode = NONE;
                midPoint = null;
                imageMidPoint = null;
                closeMidPoint = null;
//                mPivotX = -1;
//                mPivotY = -1;
                lastUpX = getTranslationX();
                lastUpY = getTranslationY();
                lastUpScale = getTextSize();
                lastUpRotation = getRotation();
                break;

        }

    }
}
