package com.ban54.paylib

/**
 * 支付结果回调
 */
interface PayResultCallback {

    fun onSuccess(payParameter: PayParameter, payResult: PayResult)
    fun onFail(payParameter: PayParameter, payResult: PayResult)
    fun onCancel(payParameter: PayParameter, payResult: PayResult)
    fun onWait(payParameter: PayParameter, payResult: PayResult)
}
