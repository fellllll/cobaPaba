package PABA.UAS.c14210001

import android.os.Parcel
import android.os.Parcelable

data class DcAssets(
    var no : String?,
    var nama : String?,
    var jumlah : Int?,
    var berat : Float?,
    var ruangan : String?,
    var idKota : String?,
    var namaKota : String?,
    var fav : Int?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(no)
        parcel.writeString(nama)
        parcel.writeInt(jumlah.toString().toInt())
        parcel.writeFloat(berat.toString().toFloat())
        parcel.writeString(ruangan)
        parcel.writeString(idKota)
        parcel.writeString(namaKota)
        parcel.writeInt(fav.toString().toInt())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DcAssets> {
        override fun createFromParcel(parcel: Parcel): DcAssets {
            return DcAssets(parcel)
        }

        override fun newArray(size: Int): Array<DcAssets?> {
            return arrayOfNulls(size)
        }
    }
}
