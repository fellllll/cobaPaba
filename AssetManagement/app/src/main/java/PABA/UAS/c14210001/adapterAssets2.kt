package PABA.UAS.c14210001

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class adapterAssets2(
    private var dataAssets: ArrayList<DcAssets>

) : RecyclerView.Adapter<adapterAssets2.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    val db= Firebase.firestore

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var _no = itemView.findViewById<TextView>(R.id.tvNo)
        var _nama = itemView.findViewById<TextView>(R.id.tvNama)
        var _jumlah = itemView.findViewById<TextView>(R.id.tvJumlah)
        var _berat = itemView.findViewById<TextView>(R.id.tvBerat)
        var _ruangan = itemView.findViewById<TextView>(R.id.tvRuangan)
        var _kota = itemView.findViewById<TextView>(R.id.tvKota)
        var _llCont = itemView.findViewById<LinearLayout>(R.id.llCont)
        var _btnFav = itemView.findViewById<ImageView>(R.id.btnFav)

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DcAssets, pos: Int, i: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.itemassets, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataAssets.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var assets = dataAssets[position]

        holder._no.setText("No: " + assets.no)
        holder._nama.setText("Nama: " + assets.nama)
        holder._jumlah.setText("Jumlah: " + assets.jumlah.toString())
        holder._berat.setText("Berat: " + assets.berat.toString())
        holder._ruangan.setText("Ruangan: " + assets.ruangan)
        holder._kota.setText("Lokasi: " + assets.namaKota)

        val drawableResId = if (assets.fav == 1) {
            R.drawable.fav_pink
        } else {
            R.drawable.favorite_border
        }
        holder._btnFav.setImageResource(drawableResId)

        holder._llCont.setOnClickListener {
            onItemClickCallback.onItemClicked(dataAssets[position], position, 0)
        }
//
//        holder._btnFav.setOnClickListener {
//            onItemClickCallback.onItemClicked(dataAssets[position], position, 1)
//        }
//
//        holder._btnFav.setOnClickListener {
//            if (assets.fav == 1) assets.fav = 0
//            else if (assets.fav == 0) assets.fav = 1
//            val drawableResId = if (assets.fav == 1) {
//                R.drawable.fav_pink
//            } else {
//                R.drawable.favorite_border
//            }
//
//            // Set the drawable for _btnFav
//            holder._btnFav.setImageResource(drawableResId)
//
//            addData(
//                assets.no.toString(),
//                assets.nama.toString(),
//                assets.jumlah.toString().toInt(),
//                assets.berat.toString().toFloat(),
//                assets.ruangan.toString(),
//                assets.idKota.toString(),
//                assets.namaKota.toString(),
//                assets.fav.toString().toInt()
//            )
//        }
    }

    fun addData(no: String, nama: String, jumlah: Int, berat: Float, ruangan: String, idKota: String, namaKota: String, fav: Int) {
        val dataToDB = DcAssets(no, nama, jumlah, berat, ruangan, idKota, namaKota, fav)
        db.collection("tbAssets")
            .document(no)
            .set(dataToDB)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                Log.e(
                    "PROJ_FIREBASE",
                    it.message.toString()
                )
            }
    }
}