package com.ban54.paylib.alipay


import com.ban54.paylib.PayParameter

/**
 * 支付宝支付参数
 */
class AlipayParameter(var parameters: String) : PayParameter() {
    override fun getPaymentId(): Int {
        return ALIPAY
    }
}
