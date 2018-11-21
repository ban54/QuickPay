package com.ban54.paylib.wx

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.ban54.paylib.*
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信支付工具
 */
class WXPayUtil(context: Activity, payParameter: PayParameter, payResultCallback: PayResultCallback) :
    PaymentUtil(context, payParameter, payResultCallback) {

    private val mResultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
            if (ACTION_WX_PAY_RETURN == intent.action) {
                val code = intent.getIntExtra(EXTRA_RESULT_CODE, BaseResp.ErrCode.ERR_SENT_FAILED)
                val desc = intent.getStringExtra(EXTRA_RESULT_DESC)
                mPayResultCallback?.apply {
                    when (code) {
                        BaseResp.ErrCode.ERR_OK -> onSuccess(
                            mPayParameter,
                            PayResult(PayResult.SUCCESS, desc)
                        )
                        BaseResp.ErrCode.ERR_USER_CANCEL -> onCancel(
                            mPayParameter,
                            PayResult(PayResult.CANCEL, desc)
                        )
                        BaseResp.ErrCode.ERR_COMM -> onFail(
                            mPayParameter,
                            PayResult(PayResult.FAILED, desc, PayResult.SDK_FAILED)
                        )
                        else -> onFail(
                            mPayParameter,
                            PayResult(PayResult.FAILED, desc, PayResult.SDK_FAILED)
                        )
                    }
                }
            }
        }
    }

    override fun pay() {
        val parameter = mPayParameter as WXPayParameter
        parameter.extData = WX_PAY_START_STRING
        val api = WXAPIFactory.createWXAPI(mContext, parameter.appId)
        if (!api.isWXAppInstalled) { // 微信未安装
            mPayResultCallback?.onFail(
                mPayParameter,
                PayResult(
                    PayResult.FAILED, "微信未安装", PayResult.INTERNAL_FAILED
                )
            )
        } else if (api.wxAppSupportAPI < Build.PAY_SUPPORTED_SDK_INT) { //当前微信版本不支持支付
            mPayResultCallback?.onFail(
                mPayParameter,
                PayResult(PayResult.FAILED, "当前微信版本不支持支付功能", PayResult.INTERNAL_FAILED)
            )
        } else {
            LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(mResultReceiver, IntentFilter(ACTION_WX_PAY_RETURN))
            val req = PayReq()
            parameter.fillWXPayReq(req)
            if (!api.sendReq(req)) {
                mPayResultCallback?.onFail(
                    mPayParameter, PayResult(
                        PayResult.FAILED, "微信启动失败",
                        PayResult.INTERNAL_FAILED
                    )
                )
            }
        }
    }

    companion object {
        /**
         * 微信支付回调结果广播ACTION
         */
        val ACTION_WX_PAY_RETURN = BuildConfig.APPLICATION_ID + ".ACTION_WX_PAY_RETURN"
        /**
         * 微信支付结果码
         */
        val EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE"
        /**
         * 微信支付结果描述信息
         */
        val EXTRA_RESULT_DESC = "EXTRA_RESULT_DESC"
        /**
         * 等待支付结果超时时间（毫秒）
         */
        val TIMEOUT = 10000

        val WX_PAY_START_STRING = "wx_pay_start_string"
    }
}
