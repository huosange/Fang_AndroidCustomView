package com.custom.fang_androidcustomview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.custom.fang_androidcustomview.adapter.HoverAdapter;
import com.custom.fang_androidcustomview.widget.IndexView;

public class HoverItemActivity extends AppCompatActivity{

    private IndexView indexView;
    private TextView showTextDialog;
    private RecyclerView recyclerView;
    private HoverAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hover_item);

        indexView=findViewById(R.id.index_view);
        showTextDialog=findViewById(R.id.show_text_dialog);
        recyclerView=findViewById(R.id.recycler_view);
        initIndexView();
    }

    /**
     * 初始化右边字母索引view
     */
    private void initIndexView(){
        indexView.setmShowTextDialog(showTextDialog);
        indexView.setOnTouchingLetterChangedListener(new IndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {

            }
        });
    }
}
