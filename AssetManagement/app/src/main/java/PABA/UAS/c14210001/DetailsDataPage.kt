package PABA.UAS.c14210001

import PABA.UAS.c14210001.apiCity.DcResponseCity
import PABA.UAS.c14210001.apiCity.apiCity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsDataPage : AppCompatActivity() {
    private lateinit var _inpNo : EditText
    private lateinit var _inpNama : EditText
    private lateinit var _inpJumlah : EditText
    private lateinit var _inpBerat : EditText
    private lateinit var _inpRuangan : EditText
    private lateinit var _spinnerKota : EditText
    private lateinit var _btnDel : Button
    private lateinit var _btnUp : Button
    private lateinit var _btnMv : Button
    private lateinit var adapterKota : ArrayAdapter<String>
    private var chooseId : String = ""
    private lateinit var chooseKota : String
    private lateinit var list_idKota : ArrayList<String>
    private lateinit var list_namaKota : ArrayList<String>
    var isValid = false

    val db= Firebase.firestore

    var dataAssets = ArrayList<DcAssets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_data_page)

        val intentItem = intent.getParcelableExtra(dataItem,DcAssets::class.java)

        _inpNo = findViewById(R.id.inpNo)
        _inpNama = findViewById(R.id.inpNama)
        _inpJumlah = findViewById(R.id.inpJumlah)
        _inpBerat = findViewById(R.id.inpBerat)
        _inpRuangan = findViewById(R.id.inpRuangan)
        _spinnerKota = findViewById(R.id.inpKota)
        _btnDel = findViewById(R.id.btnDel)
        _btnUp = findViewById(R.id.btnUp)
        _btnMv = findViewById(R.id.btnMove)

        _btnUp.isClickable = true
        _btnDel.isClickable = true

        list_idKota = arrayListOf()
        list_namaKota = arrayListOf()
//        list_noDB = arrayListOf()

        checkData()
        getDataCity()

        _inpNo.setText(intentItem?.no.toString())
        _inpNama.setText(intentItem?.nama.toString())
        _inpJumlah.setText(intentItem?.jumlah.toString())
        _inpBerat.setText(intentItem?.berat.toString())
        _inpRuangan.setText(intentItem?.ruangan.toString())
        _spinnerKota.setText(intentItem?.namaKota.toString())
        chooseId = intentItem?.idKota.toString()
        chooseKota = intentItem?.namaKota.toString()

        _btnUp.setOnClickListener {
            if (chooseId != "") {
                updateData(
                    intentItem?.no.toString(),
                    _inpNama.text.toString(),
                    _inpJumlah.text.toString().toInt(),
                    _inpBerat.text.toString().toFloat(),
                    _inpRuangan.text.toString(),
                    intentItem?.idKota.toString(),
                    intentItem?.namaKota.toString(),
                    intentItem?.fav.toString().toInt()
                )
                _btnUp.isClickable = false
            } else {
                Toast.makeText(
                    this, "Input tidak boleh kosong", Toast.LENGTH_SHORT
                ).show()
            }
        }

        _btnDel.setOnClickListener {
            deleteData(intentItem?.no.toString())
            _btnDel.isClickable = false
            checkData()
            finish()
        }

        _btnMv.setOnClickListener {
            val setDataIntent = DcAssets(
                intentItem?.no.toString(), intentItem?.nama.toString(), intentItem?.jumlah, intentItem?.berat, intentItem?.ruangan.toString(), intentItem?.idKota.toString(), intentItem?.namaKota.toString(), intentItem?.fav)
            val intentWithData = Intent(this@DetailsDataPage,MoveKota::class.java).apply {
                putExtra(MoveKota.dataItem,setDataIntent)
            }
            startActivity(intentWithData)
        }

    }

    fun updateData(no: String, nama: String, jumlah: Int, berat: Float, ruangan: String, idKota: String, namaKota: String, fav: Int) {
        val dataToDB = DcAssets(no, nama, jumlah, berat, ruangan, idKota, namaKota, fav)
        db.collection("tbAssets")
            .document(no)
            .set(dataToDB)
            .addOnSuccessListener {
                _inpNo.setText("")
                _inpNama.setText("")
                _inpJumlah.setText("")
                _inpBerat.setText("")
                _inpRuangan.setText("")
                checkData()
                Toast.makeText(
                    this, "Data berhasil disimpan", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .addOnFailureListener {
                Log.e(
                    "PROJ_FIREBASE",
                    it.message.toString()
                )
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@DetailsDataPage, message, Toast.LENGTH_SHORT).show()
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
                }
            }
    }

    object ApiCity {
        const val baseURL: String = "https://api.rajaongkir.com/starter/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create<apiCity>(apiCity::class.java)

    }

    fun getDataCity() {
        var callApi = ApiCity.apiService.getAllCity()
        callApi.enqueue(object : Callback<DcResponseCity> {
            override fun onResponse(
                call: Call<DcResponseCity>,
                response: Response<DcResponseCity>
            ) {
                if (response.isSuccessful){
                    val hasil_City = response.body()
                    for (x in hasil_City!!.rajaongkir!!.results!!){
                        if (x != null) {
                            x.cityId?.let { list_idKota.add(it) }
                            x.cityName?.let { list_namaKota.add(it) }
                        }
                    }
                    adapterKota = ArrayAdapter(this@DetailsDataPage, android.R.layout.simple_spinner_dropdown_item, list_namaKota)
                }
            }

            override fun onFailure(call: Call<DcResponseCity>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }
    companion object {
        val dataItem = "kirimDataAssets"
    }
}