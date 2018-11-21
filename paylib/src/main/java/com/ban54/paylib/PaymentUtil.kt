package com.ban54.paylib

import android.app.Activity

/**
 * 支付工具
 */
abstract class PaymentUtil(context: Activity, payParameter: PayParameter, payResultCallback: PayResultCallback) {
    protected var mContext: Activity = context
    protected var mPayParameter: PayParameter = payParameter
    protected var mPayResultCallback: PayResultCallback = payResultCallback

    /**
     * 支付
     */
    abstract fun pay()
}
