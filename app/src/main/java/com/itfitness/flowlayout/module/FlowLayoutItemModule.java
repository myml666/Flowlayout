package com.itfitness.flowlayout.module;

import com.itfitness.flowlayout.widget.FlowLayout;

/**
 * @ProjectName: Flowlayout
 * @Package: com.itfitness.flowlayout.module
 * @ClassName: FlowLayoutItemModule
 * @Description: java类作用描述 流式布局item实体类
 * @Author: 作者名
 * @CreateDate: 2020/4/19 14:50
 * @UpdateUser: 更新者：itfitness
 * @UpdateDate: 2020/4/19 14:50
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FlowLayoutItemModule implements FlowLayout.ModuleImpl {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getItemText() {
        return name;
    }
}
