package com.itfitness.flowlayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itfitness.flowlayout.R;

/**
 * @ProjectName: Flowlayout
 * @Package: com.itfitness.flowlayout.widget
 * @ClassName: FlowLayoutItem
 * @Description: java类作用描述 流式布局itemView
 * @Author: 作者名
 * @CreateDate: 2020/4/19 14:52
 * @UpdateUser: 更新者：itfitness
 * @UpdateDate: 2020/4/19 14:52
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FlowLayoutItem extends FrameLayout implements FlowLayout.ItemViewImpl{
    private TextView textView;
    public FlowLayoutItem(Context context) {
        this(context,null);
    }

    public FlowLayoutItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayoutItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View.inflate(context, R.layout.item_flowlayout,this);
        textView = findViewById(R.id.item_flowlayout_tv_item);
    }

    @Override
    public void setSelect(boolean isSelect) {
        textView.setSelected(isSelect);
    }
    @Override
    public void setItemText(String text) {
        textView.setText(text);
    }
}
