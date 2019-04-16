package com.sjl.idcard.listener;

import com.sjl.idcard.entity.IdentityCard;

/**
 * 合成监听
 *
 * @author Kelly
 * @version 1.0.0
 * @filename CompoundListener.java
 * @time 2019/4/15 16:46
 * @copyright(C) 2019 song
 */
public interface CompoundListener {
    /**
     * 开始，可用于显示加载条
     */
    void onStart();

    void onSuccess(IdentityCard identityCard);

    void onFailed(Throwable t);
}
