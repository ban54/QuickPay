package com.ban54.quickpay.unpay

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import com.ban54.quickpay.*
import com.unionpay.UPPayAssistEx

/**
 * 银联支付
 */
class UnionPayUtil(context: Activity, payParameter: PayParameter, payResultCallback: PayResultCallback) :
    PaymentUtil(context, payParameter, payResultCallback) {

    private val mResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
            if (ACTION_UNION_PAY_RETURN == intent.action) {
                val str = intent.getStringExtra(EXTRA_PAY_RESULT)
                mPayResultCallback?.apply {
                    if (str.equals("success", ignoreCase = true)) {
                        onSuccess(mPayParameter, PayResult(PayResult.SUCCESS, "支付成功"))
                    } else if (TextUtils.isEmpty(str) || str.equals("fail", ignoreCase = true)) {
                        onFail(mPayParameter, PayResult(PayResult.FAILED, "支付失败"))
                    } else if (str.equals("cancel", ignoreCase = true)) {
                        onCancel(mPayParameter, PayResult(PayResult.CANCEL, "支付取消"))
                    }
                }
            }
        }
    }

    override fun pay() {
        if (mPayParameter == null) {
            mPayResultCallback?.onFail(mPayParameter, PayResult(PayResult.FAILED, "支付失败"))
            return
        }
        val parameter = mPayParameter as UnionPayParameter?
        LocalBroadcastManager.getInstance(mContext)
            .registerReceiver(mResultReceiver, IntentFilter(ACTION_UNION_PAY_RETURN))

        val intent =Intent(mContext, UnionPayCallbackActivity::class.java)
        intent.putExtra(EXTRA_PAY_PARAMS, mPayParameter as UnionPayParameter)
        mContext.startActivity(intent)
    }

    companion object {

        /**
         * 银联支付结果
         */
        internal const val EXTRA_PAY_RESULT = "pay_result"
        /**
         * 银联支付参数
         */
        internal const val EXTRA_PAY_PARAMS = "pay_params"

        /**
         * 银联支付回调结果广播ACTION
         */
        internal const val ACTION_UNION_PAY_RETURN = BuildConfig.APPLICATION_ID + ".ACTION_UNION_PAY_RETURN"

        internal const val TEST_MODE = "01"

        internal const val REAL_MODE = "00"

        internal fun startRealPay(context: Activity, tn: String?, phonePayType: String?) {
            var mode = REAL_MODE
            /*
                 SEName          seType
                 Samsung Pay      02
                 Huawei Pay       04
                 Meizu Pay        27
                 Le Pay           30
                 ZTE Pay          21
                 Mi Pay           25
                 vivo Pay         33
                 Smartisan Pay    32 锤子 坚果手机
                  */

            //目前sdk只有三星特殊些
            if (!TextUtils.isEmpty(phonePayType)) {
                if (phonePayType == "02") {
                    UPPayAssistEx.startSamsungPay(context, javaClass, null, null, tn, mode)
                } else {
                    UPPayAssistEx.startSEPay(context, null, null, tn, mode, phonePayType)
                }
            } else {
                UPPayAssistEx.startPay(context, null, null, tn, mode)
            }
        }
    }
}


