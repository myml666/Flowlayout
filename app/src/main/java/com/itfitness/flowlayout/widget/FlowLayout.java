package com.itfitness.flowlayout.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itfitness.flowlayout.R;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Flowlayout
 * @Package: com.itfitness.flowlayout.widget
 * @ClassName: FlowLayout
 * @Description: java类作用描述 流式布局
 * @Author: 作者名
 * @CreateDate: 2020/4/19 10:09
 * @UpdateUser: 更新者：itfitness
 * @UpdateDate: 2020/4/19 10:09
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FlowLayout extends ViewGroup {
    private int mHItemSpec = 20;//Item横向的间距
    private int mVItemSpec = 10;//Item竖向的间距
    private OnItemSelectListener onItemSelectListener;
    private ArrayList<ArrayList<View>> allLines = new ArrayList<>();
    private ArrayList<Integer> allHeight = new ArrayList<>();

    public OnItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置数据
     */
    public void setDatas(List<? extends ModuleImpl> datas,Class itemViewClazz){
        removeAllViews();
        for(int i = 0 ; i < datas.size() ; i ++ ){
            final ModuleImpl module = datas.get(i);
            final View itemView = getClazzView(itemViewClazz);
            if(itemView instanceof ItemViewImpl){
                ItemViewImpl itemImpl = (ItemViewImpl) itemView;
                itemImpl.setItemText(module.getItemText());
            }
            final int finalI = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemSelectListener!=null){
                        onItemSelectListener.onItemSelect(finalI,module);
                    }
                    updateItemStatus(finalI);
                }
            });
            addView(itemView);
        }
    }

    /**
     * 更新itemView的状态
     */
    private void updateItemStatus(int selectPos){
        for(int  i = 0 ; i < getChildCount() ; i++){
            View child = getChildAt(i);
            if(child instanceof ItemViewImpl){
                ItemViewImpl itemImpl = (ItemViewImpl) child;
                if(selectPos == i){
                    itemImpl.setSelect(true);
                }else {
                    itemImpl.setSelect(false);
                }
            }
        }
    }
    /**
     * 通过反射实例化View
     * @param clazz
     */
    private View getClazzView(Class clazz){
        View itemView = null;
        try {
            Constructor c = clazz.getConstructor(Context.class);//获取有参构造
            itemView = (View) c.newInstance(getContext());    //通过有参构造创建对象
        }catch (Exception e){
        }
        return itemView;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        allLines.clear();
        allHeight.clear();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);//当前流式布局最大的宽度
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);//当前流式布局最大的高度

        int needWidth = 0;//需要的宽度
        int needHeight = 0;//需要的高度

        int lineWidth = 0;//记录每一行的宽度
        int lineHeight = 0;//记录每一行的高度

        ArrayList<View> lineViews = new ArrayList<>();//存储每一行的View
        //度量子View
        for(int i = 0 ; i < getChildCount() ; i ++){
            View childView = getChildAt(i);
            LayoutParams layoutParams = childView.getLayoutParams();
            //获取子控件的MeasureSpec
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, layoutParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, layoutParams.height);
            childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);//对子控件进行度量
            //获取子控件度量后的宽高
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            //如果一行的宽度超过了本布局的宽度则需要换行
            if(lineWidth + childMeasuredWidth + mHItemSpec > selfWidth){
                needWidth = Math.max(needWidth,lineWidth);//计算出所有行中最大的宽度来作为需要的宽度
                needHeight += lineHeight+mVItemSpec;//需要的高度为所有行的高度和
                allLines.add(lineViews);//存储每一行的View方便布局时使用
                allHeight.add(lineHeight);//存储每一行的高度方便布局的时候使用
                lineWidth = 0;
                lineHeight = 0;
                lineViews = new ArrayList<>();
            }
            lineWidth += childMeasuredWidth + mHItemSpec;
            //每一行的高度取每一行最高控件的高度
            lineHeight = Math.max(childMeasuredHeight,lineHeight);
            lineViews.add(childView);
        }
        //加入最后一行
        if(lineViews.size()>0){
            allLines.add(lineViews);
            allHeight.add(lineHeight);
            needHeight += lineHeight+mVItemSpec;
        }
        //获取FlowLayout自身的MeasureSpec Mode
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //根据FlowLayout自身的MeasureSpec Mode来判断使用哪个宽高
        int realWidth = modeWidth == MeasureSpec.EXACTLY ? selfWidth : needWidth;
        int realHeight = modeHeight == MeasureSpec.EXACTLY ? selfHeight : needHeight;
        setMeasuredDimension(realWidth,realHeight);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //开始的left根据本布局的padding来计算的
        int curtL = getPaddingLeft();
        int curtT = getPaddingTop();
        for(ArrayList<View> widhViews:allLines){
            for(View childView:widhViews){
                int right = childView.getMeasuredWidth() + curtL;
                int bottom = childView.getMeasuredHeight() + curtT;
                childView.layout(curtL,curtT,right,bottom);
                curtL += childView.getMeasuredWidth() + mHItemSpec;
            }
            //每一行结束后需要增加行高
            curtT += allHeight.get(allLines.indexOf(widhViews)) + mVItemSpec;
            //每一行结束需要left要重置
            curtL = getPaddingLeft();
        }
    }

    /**
     * 实体类需要继承的接口
     */
    public interface ModuleImpl{
        /**
         * 返回想要设置的文字
         * @return
         */
        String getItemText();
    }
    /**
     * ItemView选择或未选择的状态和文字
     */
    public interface ItemViewImpl{
        /**
         * 设置选中状态(实现的控件可根据此方法进行相应的状态切换)
         * @param isSelect
         */
        void setSelect(boolean isSelect);

        /**
         * 给实现此接口的子控件设置文字的方法
         * @param text
         */
        void setItemText(String text);
    }
    /**
     * 当item选择的回调
     * @param <T>
     */
    public interface OnItemSelectListener<T extends ModuleImpl>{
        void onItemSelect(int position,T data);
    }
}
