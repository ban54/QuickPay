package com.ban54.quickpay

/**
 * 支付结果
 */
class PayResult {

    /**
     * 支付结果码
     */
    var code = FAILED
    /**
     * 支付结果描述
     */
    var description: String? = ""
    /**
     * 支付结果码的细化
     */
    var subCode = 0//默认值

    constructor(code: Int, description: String) {
        this.code = code
        this.description = description
    }

    constructor(code: Int, description: String, subCode: Int) {
        this.code = code
        this.description = description
        this.subCode = subCode
    }

    constructor() {}

    companion object {
        /**
         * 支付失败
         */
        const val FAILED = -1
        /**
         * 支付取消
         */
        const val CANCEL = 0
        /**
         * 支付成功
         */
        const val SUCCESS = 1
        /**
         * 等待支付结果
         */
        const val WAITING = 2
        //subCode
        const val SDK_FAILED = -10000
        const val INTERNAL_FAILED = -10001
    }
}
