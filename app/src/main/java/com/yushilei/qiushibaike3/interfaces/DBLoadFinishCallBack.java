package com.yushilei.qiushibaike3.interfaces;

import com.yushilei.qiushibaike3.entitys.SuggestResponse;

import java.util.List;

/**
 * Created by yushilei on 2016/1/1.
 */
public interface DBLoadFinishCallBack {
    void dbLoadFinishCallBack(List<SuggestResponse.ItemsEntity> entityList);
}
