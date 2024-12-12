package PABA.UAS.c14210001

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fList.newInstance] factory method to
 * create an instance of this fragment.
 */
class fList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var _rvAssets : RecyclerView
    lateinit var adapterAssets : adapterAssets

    val db = Firebase.firestore
    var dataAssets = ArrayList<DcAssets>()

    override fun onStart() {
        super.onStart()
        Log.d("cek", "onStart: ")
        checkData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d("cek", "onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("cek", "onCreateView: ")
        return inflater.inflate(R.layout.fragment_f_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _rvAssets = view.findViewById(R.id.rvAssets)

        view.findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            val intent = Intent(requireContext(),AddDataPage::class.java)
            startActivity(intent)
        }
        Log.d("cek", "onViewCreated: ")
        checkData()
    }

    private fun detailShow() {
        _rvAssets.layoutManager = LinearLayoutManager(this.context)

        val adapterP = adapterAssets(dataAssets!!)
        _rvAssets.adapter = adapterP

        adapterP.setOnItemClickCallback(object: adapterAssets.OnItemClickCallback {
            override fun onItemClicked(data : DcAssets, pos: Int) {
                val alertDialog = AlertDialog.Builder(activity!!)
                alertDialog.setTitle("Asset Details")
                val _no = "No Asset :  " + data.no
                val _nama = "Nama Asset : " + data.nama
                alertDialog.setMessage(_no + "\n" + _nama)
                alertDialog.setPositiveButton(
                    "Delete",
                    DialogInterface.OnClickListener { dialog, which ->
                        deleteData(data.no.toString())
                        checkData()
                    }
                )
                alertDialog.setNegativeButton(
                    "Edit",
                    DialogInterface.OnClickListener { dialog, which ->
                        val setDataIntent = DcAssets(data.no, data.nama, data.jumlah, data.berat, data.ruangan, data.idKota, data.namaKota, data.fav)
                        val intentWithData = Intent(requireContext(),DetailsDataPage::class.java).apply {
                            putExtra(DetailsDataPage.dataItem,setDataIntent)
                        }
                        startActivity(intentWithData)
                    }
                )
                alertDialog.setNeutralButton(
                    "Close",
                    DialogInterface.OnClickListener { dialog, which ->
                    }
                )
                alertDialog.show()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun deleteData(no: String) {
        val documentReference = db.collection("tbAssets").document(no)

        documentReference
            .delete()
            .addOnSuccessListener {
                // Handle successful deletion
                showToast("Data berhasil dihapus")
            }
            .addOnFailureListener {
                // Handle failure
                Log.e("PROJ_FIREBASE", it.message.toString())
            }
    }

    fun checkData() {
        db.collection("tbAssets")
            .get()
            .addOnSuccessListener {
                    result ->
                dataAssets.clear()
                for (document in result) {
                    var readData = DcAssets(
                        document.data.get("no").toString(),
                        document.data.get("nama").toString(),
                        document.data.get("jumlah").toString().toInt(),
                        document.data.get("berat").toString().toFloat(),
                        document.data.get("ruangan").toString(),
                        document.data.get("idKota").toString(),
                        document.data.get("namaKota").toString(),
                        document.data.get("fav").toString().toInt()
                    )
                    dataAssets.add(readData)
                    adapterAssets = adapterAssets(dataAssets)
                    detailShow()
                }
            }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}