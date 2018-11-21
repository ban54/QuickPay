package com.ban54.paylib

import android.app.Activity
import com.ban54.paylib.alipay.AlipayParameter
import com.ban54.paylib.alipay.AlipayUtil
import com.ban54.paylib.unpay.UnionPayParameter
import com.ban54.paylib.unpay.UnionPayUtil
import com.ban54.paylib.wx.WXPayParameter
import com.ban54.paylib.wx.WXPayUtil

/**
 * 支付工具类
 */
object PayHelper {

    fun pay(context: Activity, payParameter: PayParameter?, payResultCallback: PayResultCallback) {
        var paymentUtil: PaymentUtil? = null
        when (payParameter!!.getPaymentId()) {
            PayParameter.ALIPAY -> if (payParameter is AlipayParameter) {
                paymentUtil = AlipayUtil(context, payParameter, payResultCallback)
            }
            PayParameter.WECHAT_PAY -> if (payParameter is WXPayParameter) {
                paymentUtil = WXPayUtil(context, payParameter, payResultCallback)
            }
            PayParameter.UNION_PAY -> if (payParameter is UnionPayParameter) {
                paymentUtil = UnionPayUtil(context, payParameter, payResultCallback)
            }
        }
        paymentUtil?.pay() ?: payResultCallback?.onFail(
            payParameter,
            PayResult(PayResult.FAILED, "暂不支持此支付方式", PayResult.INTERNAL_FAILED)
        )
    }
}
