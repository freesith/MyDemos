package com.example.mvpdemo.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：ZhouYou
 * 日期：2016/9/3.
 * 贴纸的使用控件
 */
public class StickerLayout extends FrameLayout {

    private Context context;
    // 贴纸的集合
    private List<Stickerable> stickerViews;
    // 贴纸的View参数
    private LayoutParams stickerParams;
    // 背景图片控件
    private ImageView ivImage;

    private Stickerable mOnEditStick;

    public StickerLayout(Context context) {
        this(context, null);
    }

    public StickerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        stickerViews = new ArrayList<>();
        stickerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setFocusableInTouchMode(true);
        addBackgroundImage();
    }

    /**
     * 初始化背景图片控件
     */
    private void addBackgroundImage() {
        LayoutParams bgParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ivImage = new ImageView(context);
        ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
        ivImage.setLayoutParams(bgParams);
        addView(ivImage);
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = 0;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        return MeasureSpec.makeMeasureSpec(childDimension, MeasureSpec.UNSPECIFIED);
    }

    /**
     * 设置背景图片
     */
    public void setBackgroundImage(int resource) {
        ivImage.setImageResource(resource);
    }

    /**
     * 新增贴纸
     */
    public void addSticker(int resource) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource);
        addSticker(bitmap);
    }

    public void addStickerText() {
        final StickerEditText editText = new StickerEditText(context);
        editText.setText("哈哈哈哈");
        mOnEditStick = editText;
        editText.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setOnStickerActionListener(new StickerView.OnStickerActionListener() {
            @Override
            public void onDelete() {
                // 处理删除操作
                removeView(editText);
                stickerViews.remove(editText);
                redraw();
            }

            @Override
            public void onEdit(Stickerable stickerView) {
                int position = stickerViews.indexOf(stickerView);
                stickerView.setEdit(true);
                stickerView.bringFront();
                mOnEditStick = stickerView;

                int size = stickerViews.size();
                for (int i = 0; i < size; i++) {
                    Stickerable item = stickerViews.get(i);
                    if (item == null) continue;
                    if (position != i) {
                        item.setEdit(false);
                    }
                }
            }
        });
        addView(editText);
        stickerViews.add(editText);
        redraw();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("StickerLayout", "onTouchEvent     action = " + MotionEvent.actionToString(event.getAction()));
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clearEdit();
        }
        return super.onTouchEvent(event);
    }

    private void clearEdit() {
        int size = stickerViews.size();
        for (int i = 0; i < size; i++) {
            Stickerable item = stickerViews.get(i);
            if (item != null) {
                item.setEdit(false);
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("StickerLayout", "onDispatch     action = " + MotionEvent.actionToString(ev.getAction()));
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            if (mOnEditStick != null) {
                mOnEditStick.multi(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 新增贴纸
     */
    public void addSticker(Bitmap bitmap) {
        final StickerView sv = new StickerView(context);
        sv.setImageBitmap(bitmap);
        sv.setLayoutParams(stickerParams);
        sv.setOnStickerActionListener(new StickerView.OnStickerActionListener() {
            @Override
            public void onDelete() {
                // 处理删除操作
                removeView(sv);
                stickerViews.remove(sv);
                redraw();
            }

            @Override
            public void onEdit(Stickerable stickerView) {
                int position = stickerViews.indexOf(stickerView);
                stickerView.setEdit(true);
                stickerView.bringFront();

                int size = stickerViews.size();
                for (int i = 0; i < size; i++) {
                    Stickerable item = stickerViews.get(i);
                    if (item == null) continue;
                    if (position != i) {
                        item.setEdit(false);
                    }
                }
            }
        });
        addView(sv);
        stickerViews.add(sv);
        redraw();
    }

    /**
     * 查看贴纸的预览操作
     */
    public void getPreview() {
        for (Stickerable item : stickerViews) {
            if (item == null) continue;
            item.setEdit(false);
        }
    }

    /**
     * 重置贴纸的操作列表
     */
    private void redraw() {
        redraw(true);
    }

    /**
     * 重置贴纸的操作列表
     */
    private void redraw(boolean isNotGenerate) {
        int size = stickerViews.size();
        if (size <= 0) return;
        for (int i = size - 1; i >= 0; i--) {
            Stickerable item = stickerViews.get(i);
            if (item == null) continue;
            if (i == size - 1) {
                item.setEdit(isNotGenerate);
            } else {
                item.setEdit(false);
            }
            stickerViews.set(i, item);
        }
    }

    /**
     * 生成合成图片
     *
     * @return
     */
    public Bitmap generateCombinedBitmap() {
        redraw(false);
        Bitmap dst = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        draw(canvas);
        return dst;
    }


}
