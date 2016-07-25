package com.example.mvpdemo.holder;

import android.view.View;
import android.widget.TextView;

import com.example.mvpdemo.R;
import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.view.ICollectionView;

/**
 * Created by wangchao on 16/6/27.
 */
public class BHolder extends BaseRecyclerViewHolder<UserInfo> {

    private TextView mTextView;

    public BHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initHolderViews(View v) {
        mTextView = (TextView) v.findViewById(R.id.text);
    }

    @Override
    public void bindViewHolder(View v, final UserInfo userInfo, final ICollectionView iCollectionView) {
        mTextView.setText(userInfo.getName());
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iCollectionView != null) {
                    iCollectionView.onItemClick(v,userInfo,0);
                }
            }
        });
    }
}
