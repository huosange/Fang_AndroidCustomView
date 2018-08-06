package com.custom.fang_androidcustomview.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.custom.fang_androidcustomview.R;
import com.custom.fang_androidcustomview.bean.TypeBean;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<TypeBean,BaseViewHolder>{

    public MainAdapter(List<TypeBean> data){
        super(R.layout.adapter_item_main,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeBean item) {
        helper.setText(R.id.title_tv,item.getTitle());
    }
}
