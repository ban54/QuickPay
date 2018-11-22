package com.ban54.quickpay.alipay

import android.app.Activity
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.ban54.quickpay.PayParameter
import com.ban54.quickpay.PayResult
import com.ban54.quickpay.PayResultCallback
import com.ban54.quickpay.PaymentUtil
import java.lang.Error

/**
 * 支付宝支付工具
 */
class AlipayUtil(context: Activity, payParameter: PayParameter, payResultCallback: PayResultCallback) :
    PaymentUtil(context, payParameter, payResultCallback) {

    override fun pay() {
        Thread(Runnable {
            var payResult: PayResult
            try {
                val payTask = PayTask(mContext)
                val parameter = mPayParameter as AlipayParameter
                val result = payTask.pay(parameter.parameters, true)
                payResult = parsePayResult(result)
            } catch (e: Exception) {
                payResult = PayResult(PayResult.FAILED, e.localizedMessage, PayResult.SDK_FAILED)
            } catch (e1: Error) {
                payResult = PayResult(PayResult.FAILED, e1.localizedMessage, PayResult.SDK_FAILED)
            }
            mPayResultCallback?.apply {
                mContext.runOnUiThread {
                    when (payResult.code) {
                        PayResult.SUCCESS -> onSuccess(mPayParameter, payResult)
                        PayResult.FAILED -> onFail(mPayParameter, payResult)
                        PayResult.CANCEL -> onCancel(mPayParameter, payResult)
                        PayResult.WAITING -> onWait(mPayParameter, payResult)
                    }
                }
            }
        }).start()
    }

    private fun parsePayResult(result: String): PayResult {
        val payResult = PayResult()
        if (!TextUtils.isEmpty(result)) {
            val resultParams = result.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            var resultStatus: String? = null
            var resultDes: String? = null
            var memo: String? = null
            for (resultParam in resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus")
                } else if (resultParam.startsWith("result")) {
                    resultDes = gatValue(resultParam, "result")
                } else if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo")
                }
            }
            payResult.description = resultDes
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                payResult.code = PayResult.SUCCESS
            } else {
                // 判断resultStatus 为非“9000”则代表可能支付失败
                // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    payResult.code = PayResult.WAITING
                } else if (TextUtils.equals(resultStatus, "6001")) { // 用户取消支付
                    payResult.code = PayResult.CANCEL
                } else { // 其他值就可以判断为支付失败，或者系统返回的错误
                    payResult.code = PayResult.FAILED
                    payResult.subCode = PayResult.SDK_FAILED
                }
            }
        }
        return payResult
    }

    private fun gatValue(content: String, key: String): String {
        val prefix = "$key={"
        return content.substring(
            content.indexOf(prefix) + prefix.length,
            content.lastIndexOf("}")
        )
    }
}
