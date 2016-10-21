package com.example.mvpdemo.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;



/**
 * 作者：ZhouYou
 * 日期：2016/8/31.
 * http://www.apkbus.com/forum.php?mod=viewthread&tid=268082&extra=page%3D5%26filter%3Dsortid%26orderby%3Ddateline%26sortid%3D12
 */
public class StickerView extends ImageView implements Stickerable{

    private Context context;
    // 被操作的贴纸对象
    private Sticker sticker;
    // 手指按下时图片的矩阵
    private Matrix downMatrix = new Matrix();
    // 手指移动时图片的矩阵
    private Matrix moveMatrix = new Matrix();
    // 多点触屏时的中心点
    private PointF midPoint = new PointF();
    // 图片的中心点坐标
    private PointF imageMidPoint = new PointF();
    // 关闭按钮的中心点坐标
    private PointF closeMidPoint = new PointF();
    //    // 旋转操作图片
//    private StickerActionIcon rotateIcon;
    // 缩放操作图片
    private StickerActionIcon zoomIcon;
    // 缩放操作图片
    private StickerActionIcon removeIcon;
    // 绘制图片的边框
    private Paint paintEdge;
    private Paint paintDot;

    // 触控模式
    private int mode;
    private static final int NONE = 0; // 无模式
    private static final int TRANS = 1; // 拖拽模式
    private static final int ROTATE = 2; // 单点旋转模式
    private static final int ZOOM_SINGLE = 3; // 单点缩放模式
    private static final int ZOOM_MULTI = 4; // 多点缩放模式

    // 是否正在处于编辑
    private boolean isEdit = true;

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setScaleType(ScaleType.MATRIX);
//        rotateIcon = new StickerActionIcon(context);
//        rotateIcon.setSrcIcon(R.mipmap.ic_rotate);
        zoomIcon = new StickerActionIcon(context);
//        zoomIcon.setSrcIcon(R.drawable.edit_drag);
        removeIcon = new StickerActionIcon(context);
//        removeIcon.setSrcIcon(R.drawable.edit_close);
        paintEdge = new Paint();
        PathEffect effect = new DashPathEffect(new float[]{6,2},1);
        paintEdge.setColor(Color.WHITE);
        paintEdge.setAntiAlias(true);

        paintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDot.setColor(Color.WHITE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            sticker.getMatrix().postTranslate((getWidth() - sticker.getStickerWidth()) / 2, (getHeight() - sticker.getStickerHeight()) / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (sticker == null) return;
        sticker.draw(canvas);
        float[] points = StickerUtils.getBitmapPoints(sticker.getSrcImage(), sticker.getMatrix());
        float x1 = points[0];
        float y1 = points[1];
        float x2 = points[2];
        float y2 = points[3];
        float x3 = points[4];
        float y3 = points[5];
        float x4 = points[6];
        float y4 = points[7];

        if (isEdit) {
            Log.d("lp-test", "StickerView----------onDraw  inside----(x1,y1)=(" + x1 + "," + y1 + ")" + "  (x2,y2)=(" + x2 + "," + y2 + ")" + "  (x3,y3)=(" + x3 + "," + y3 + ")" + "  (x4,y4)=(" + x4 + "," + y4 + ")");
//            if (x1 < x2) {
//                // 画边框
//                canvas.drawLine(x1 - PADDING * ONE_DP, y1 - PADDING * ONE_DP, x2 + PADDING * ONE_DP, y2 - PADDING * ONE_DP, paintEdge);
//                canvas.drawLine(x2 + PADDING * ONE_DP, y2 - PADDING * ONE_DP, x4 + PADDING * ONE_DP, y4 + PADDING * ONE_DP, paintEdge);
//                canvas.drawLine(x4 + PADDING * ONE_DP, y4 + PADDING * ONE_DP, x3 - PADDING * ONE_DP, y3 + PADDING * ONE_DP, paintEdge);
//                canvas.drawLine(x3 - PADDING * ONE_DP, y3 + PADDING * ONE_DP, x1 - PADDING * ONE_DP, y1 - PADDING * ONE_DP, paintEdge);
//                // 画操作按钮图片
//                removeIcon.draw(canvas, x1 - PADDING * ONE_DP, y1 - PADDING * ONE_DP);
//                zoomIcon.draw(canvas, x4 + PADDING * ONE_DP, y4 + PADDING * ONE_DP);
//            } else {
//                canvas.drawLine(x1 + PADDING * ONE_DP, y1 + PADDING * ONE_DP, x2 - PADDING * ONE_DP, y2 + PADDING * ONE_DP, paintEdge);
//                canvas.drawLine(x2 - PADDING * ONE_DP, y2 + PADDING * ONE_DP, x4 - PADDING * ONE_DP, y4 - PADDING * ONE_DP, paintEdge);
//                canvas.drawLine(x4 - PADDING * ONE_DP, y4 - PADDING * ONE_DP, x3 + PADDING * ONE_DP, y3 - PADDING * ONE_DP, paintEdge);
//                canvas.drawLine(x3 + PADDING * ONE_DP, y3 - PADDING * ONE_DP, x1 + PADDING * ONE_DP, y1 + PADDING * ONE_DP, paintEdge);
//                // 画操作按钮图片
//                removeIcon.draw(canvas, x1 + PADDING * ONE_DP, y1 + PADDING * ONE_DP);
//                zoomIcon.draw(canvas, x4 - PADDING * ONE_DP, y4 - PADDING * ONE_DP);
//            }

            canvas.drawLine(x1, y1, x2, y2, paintEdge);
            canvas.drawLine(x2, y2, x4, y4, paintEdge);
            canvas.drawLine(x4, y4, x3, y3, paintEdge);
            canvas.drawLine(x3, y3, x1, y1, paintEdge);
            removeIcon.draw(canvas, x1, y1,paintDot);
            zoomIcon.draw(canvas, x4, y4,paintDot);
        }
    }

    // 手指按下屏幕的X坐标
    private float downX;
    // 手指按下屏幕的Y坐标
    private float downY;
    // 手指之间的初始距离
    private float oldDistance;
    // 手指之间的初始角度
    private float oldRotation;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean isStickerOnEdit = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if (sticker == null) return false;
                // 删除操作
                if (removeIcon.isInActionCheck(event)) {
                    if (listener != null) {
                        listener.onDelete();
                    }
                }
                // 单点缩放/旋转手势验证
                else if (zoomIcon.isInActionCheck(event)) {
                    mode = ZOOM_SINGLE;
                    downMatrix.set(sticker.getMatrix());
                    closeMidPoint = sticker.getCloseMidPoint(downMatrix);
                    oldRotation = sticker.getSpaceRotation(event, closeMidPoint);
                    oldDistance = sticker.getSingleTouchDistance(event, closeMidPoint);

                    Log.d("lp-test", "onTouchEvent------单点缩放手势/旋转手势验证---oldDistance=" + oldDistance);
                }
                // 平移手势验证
                else if (isInStickerArea(sticker, event)) {
                    mode = TRANS;
                    downMatrix.set(sticker.getMatrix());
                    Log.d("onTouchEvent", "平移手势");
                } else {
                    isStickerOnEdit = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // 多点触控
                mode = ZOOM_MULTI;
                oldDistance = sticker.getMultiTouchDistance(event);
                oldRotation = sticker.getMultiTouchRotation(event);
                midPoint = sticker.getMidPoint(event);
                downMatrix.set(sticker.getMatrix());
                break;
            case MotionEvent.ACTION_MOVE:
                // 单点缩放/单点旋转
                if (mode == ZOOM_SINGLE) {
                    moveMatrix.set(downMatrix);
                    float scale = sticker.getSingleTouchDistance(event, closeMidPoint) / oldDistance;
                    float deltaRotation = sticker.getSpaceRotation(event, closeMidPoint) - oldRotation;
                    moveMatrix.postRotate(deltaRotation, closeMidPoint.x, closeMidPoint.y);
                    moveMatrix.postScale(scale, scale, closeMidPoint.x, closeMidPoint.y);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                // 多点缩放
                else if (mode == ZOOM_MULTI) {
                    moveMatrix.set(downMatrix);
                    float scale = sticker.getMultiTouchDistance(event) / oldDistance;
                    float deltaRotation = sticker.getMultiTouchRotation(event) - oldRotation;
                    moveMatrix.postRotate(deltaRotation, midPoint.x, midPoint.y);
                    moveMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                // 平移
                else if (mode == TRANS) {
                    moveMatrix.set(downMatrix);
                    moveMatrix.postTranslate(event.getX() - downX, event.getY() - downY);
                    sticker.getMatrix().set(moveMatrix);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mode = NONE;
                midPoint = null;
                imageMidPoint = null;
                closeMidPoint = null;
                break;
            default:
                break;
        }
        if (isStickerOnEdit && listener != null) {
            listener.onEdit(this);
        }
        return isStickerOnEdit;
    }

    /**
     * 判断手指是否在操作区域内
     *
     * @param sticker
     * @param event
     * @return
     */
    private boolean isInStickerArea(Sticker sticker, MotionEvent event) {
        RectF dst = sticker.getSrcImageBound();
        return dst.contains(event.getX(), event.getY());
    }

    /**
     * 添加贴纸
     *
     * @param resId
     */
    @Override
    public void setImageResource(int resId) {
        sticker = new Sticker(BitmapFactory.decodeResource(context.getResources(), resId));
    }

    /**
     * 获取贴纸对象
     *
     * @return
     */
    public Sticker getSticker() {
        return sticker;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        sticker = new Sticker(bm);
    }

    /**
     * 设置是否贴纸正在处于编辑状态
     *
     * @param edit
     */
    @Override
    public void setEdit(boolean edit) {
        isEdit = edit;
        postInvalidate();
    }

    @Override
    public void bringFront() {
        super.bringToFront();
    }

    @Override
    public void multi(MotionEvent event) {

    }

    private OnStickerActionListener listener;

    public void setOnStickerActionListener(OnStickerActionListener listener) {
        this.listener = listener;
    }

    public interface OnStickerActionListener {
        /*删除贴纸*/
        void onDelete();

        /*编辑贴纸*/
        void onEdit(Stickerable stickerView);
    }
}
