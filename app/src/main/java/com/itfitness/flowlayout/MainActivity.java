package com.itfitness.flowlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itfitness.flowlayout.module.FlowLayoutItemModule;
import com.itfitness.flowlayout.widget.FlowLayout;
import com.itfitness.flowlayout.widget.FlowLayoutItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FlowLayout flowLayout;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = findViewById(R.id.fl);
        button = findViewById(R.id.bt);
        //给FlowLayout设置item点击事件
        flowLayout.setOnItemSelectListener(new FlowLayout.OnItemSelectListener<FlowLayoutItemModule>() {
            @Override
            public void onItemSelect(int position, FlowLayoutItemModule data) {
                Toast.makeText(MainActivity.this, data.getItemText()+"=="+position, Toast.LENGTH_SHORT).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //随机生成整数作为item数量
                int i = new Random().nextInt(20);
                Toast.makeText(MainActivity.this, i+"", Toast.LENGTH_SHORT).show();
                //给FlowLayout设置数据，其中FlowLayoutItem是一个实现了FlowLayout.ItemViewImpl接口的自定义控件
                flowLayout.setDatas(getFlowDatas(i), FlowLayoutItem.class);
            }
        });
    }
    /**
     * 获取数据
     * @return
     */
    private List<FlowLayout.ModuleImpl> getFlowDatas(int size){
        List<FlowLayout.ModuleImpl> datas = new ArrayList<>();
        for(int i = 0 ; i < size ; i++){
            FlowLayoutItemModule flowLayoutItemModule = new FlowLayoutItemModule();
            flowLayoutItemModule.setName("我是Item"+i);
            datas.add(flowLayoutItemModule);
        }
        return datas;
    }
}
