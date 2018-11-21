package com.ban54.quickpay.unpay


import android.os.Parcel
import android.os.Parcelable
import com.ban54.quickpay.PayParameter

/**
 * 银联支付参数
 */
class UnionPayParameter() : PayParameter(), Parcelable {
    var tn: String? = null

    var phonePayType: String? = null

    constructor(parcel: Parcel) : this() {
        tn = parcel.readString()
        phonePayType = parcel.readString()
    }

    override fun getPaymentId(): Int {
        return UNION_PAY
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(tn)
        dest?.writeString(phonePayType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnionPayParameter> {
        override fun createFromParcel(parcel: Parcel): UnionPayParameter {
            return UnionPayParameter(parcel)
        }

        override fun newArray(size: Int): Array<UnionPayParameter?> {
            return arrayOfNulls(size)
        }
    }
}
