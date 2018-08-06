package com.custom.fang_androidcustomview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.custom.fang_androidcustomview.adapter.MainAdapter;
import com.custom.fang_androidcustomview.bean.TypeBean;
import com.custom.fang_androidcustomview.widget.SuperDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener{

    private RecyclerView recyclerView;

    private MainAdapter adapter;

    private List<TypeBean> typeBeans=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_view);

        adapter=new MainAdapter(getData());
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SuperDividerItemDecoration.Builder(this)
                .setDividerWidth(5)
                .setDividerColor(getResources().getColor(R.color.colorAccent))
                .build());
        recyclerView.setAdapter(adapter);
    }

    private List<TypeBean> getData(){
        typeBeans.add(new TypeBean("吸顶效果--一行代码实现", 1));
        typeBeans.add(new TypeBean("测试",2));
        return typeBeans;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (typeBeans.get(position).getType()){
            case 1:
                startActivity(new Intent(MainActivity.this,HoverItemActivity.class));
                break;
            case 2:
                break;
        }
    }
}
