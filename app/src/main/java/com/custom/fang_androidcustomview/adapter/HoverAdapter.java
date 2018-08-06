package com.custom.fang_androidcustomview.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.custom.fang_androidcustomview.R;
import com.custom.fang_androidcustomview.bean.UserBean;

import java.util.List;

public class HoverAdapter extends BaseQuickAdapter<UserBean,BaseViewHolder>{

    public HoverAdapter(List<UserBean> data){
        super(R.layout.adapter_item_hover_user,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        helper.setText(R.id.user_name_tv,item.getUserName());
    }
}
