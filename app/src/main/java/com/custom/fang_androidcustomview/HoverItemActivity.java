package com.custom.fang_androidcustomview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.custom.fang_androidcustomview.adapter.HoverAdapter;
import com.custom.fang_androidcustomview.bean.UserBean;
import com.custom.fang_androidcustomview.utils.PinyinComparator;
import com.custom.fang_androidcustomview.widget.HoverItemDecoration;
import com.custom.fang_androidcustomview.widget.IndexView;
import com.github.promeg.pinyinhelper.Pinyin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HoverItemActivity extends AppCompatActivity {

    private IndexView indexView;
    private TextView showTextDialog;
    private RecyclerView recyclerView;
    private HoverAdapter adapter;
    private List<UserBean> userBeans;
    private LinearLayoutManager layoutManager;

    private String[] names = new String[]{"阿妹", "打黑牛", "张三", "李四", "王五", "田鸡", "孙五"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hover_item);

        userBeans = filledData(getData());

        indexView = findViewById(R.id.index_view);
        showTextDialog = findViewById(R.id.show_text_dialog);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HoverAdapter(userBeans);
        recyclerView.addItemDecoration(new HoverItemDecoration(this, new HoverItemDecoration.BindItemTextCallback() {
            @Override
            public String getItemText(int position) {
                return userBeans.get(position).getSortLetters();
            }
        }));
        recyclerView.setAdapter(adapter);
        initIndexView();

    }

    /**
     * 初始化右边字母索引view
     */
    private void initIndexView() {
        indexView.setmShowTextDialog(showTextDialog);
        indexView.setOnTouchingLetterChangedListener(new IndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                //该字母首次出现的位置
                int position = getPositionForSection(letter);
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position,0);
                }
            }
        });
    }

    public int getPositionForSection(String section) {
        for (int i = 0; i < userBeans.size(); i++) {
            String sortStr = userBeans.get(i).getSortLetters();
            if (sortStr.equals(section)) {
                return i;
            }
        }
        return -1;
    }

    private List<UserBean> getData() {
        List<UserBean> userBeans = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UserBean userBean = new UserBean();
            userBean.setUserName(names[i % 7]);
            userBeans.add(userBean);
        }
        return userBeans;
    }

    private List<UserBean> filledData(List<UserBean> sortList) {
        for (int i = 0; i < sortList.size(); i++) {
            if ("".equals(sortList.get(i).getUserName())) {
                sortList.get(i).setSortLetters("#");
            } else {
                //汉字转换成拼音
                String pinyin = Pinyin.toPinyin(sortList.get(i).getUserName(), "");
                String sortString = pinyin.substring(0, 1).toUpperCase();
                if (sortString.matches("[A-Z]")) {
                    sortList.get(i).setSortLetters(sortString);
                } else {
                    sortList.get(i).setSortLetters("#");
                }
            }
        }
        Collections.sort(sortList, new PinyinComparator());
        return sortList;
    }
}
