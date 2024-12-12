package PABA.UAS.c14210001

import PABA.UAS.c14210001.apiCity.DcResponseCity
import PABA.UAS.c14210001.apiCity.apiCity
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

class AddDataPage : AppCompatActivity() {

    private lateinit var _inpNo : EditText
    private lateinit var _inpNama : EditText
    private lateinit var _inpJumlah : EditText
    private lateinit var _inpBerat : EditText
    private lateinit var _inpRuangan : EditText
    private lateinit var _spinnerKota : Spinner
    private lateinit var _btnAdd : Button
    private lateinit var adapterKota : ArrayAdapter<String>
    private var chooseId : String = ""
    private lateinit var chooseKota : String
    private lateinit var list_idKota : ArrayList<String>
    private lateinit var list_namaKota : ArrayList<String>
    var isValid = false
//    private lateinit var list_noDB : ArrayList<String>

    val db= Firebase.firestore

    var dataAssets = ArrayList<DcAssets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data_page)

        _inpNo = findViewById(R.id.inpNo)
        _inpNama = findViewById(R.id.inpNama)
        _inpJumlah = findViewById(R.id.inpJumlah)
        _inpBerat = findViewById(R.id.inpBerat)
        _inpRuangan = findViewById(R.id.inpRuangan)
        _spinnerKota = findViewById(R.id.inpKota)
        _btnAdd = findViewById(R.id.btnAdd)

        _btnAdd.isClickable = true

        list_idKota = arrayListOf()
        list_namaKota = arrayListOf()
//        list_noDB = arrayListOf()

        checkData()
        getDataCity()

        _spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
                // For example, log it or update a variable
                Log.d("SpinnerSelection", "Selected item: $selectedItem")
                chooseId = list_idKota[position]
                chooseKota = selectedItem
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
        _btnAdd.setOnClickListener{
            Log.d("dbCount", dataAssets.size.toString())
            if (_inpNo.text.toString() != "" && _inpNama.text.toString() != "" && _inpJumlah.text.toString() != "" && _inpBerat.text.toString() != "" && _inpRuangan.text.toString() != "") {
                if (dataAssets.size < 1) {
                    if (chooseId != "") {
                        addData(
                            _inpNo.text.toString(),
                            _inpNama.text.toString(),
                            _inpJumlah.text.toString().toInt(),
                            _inpBerat.text.toString().toFloat(),
                            _inpRuangan.text.toString(),
                            chooseId,
                            chooseKota,
                            0
                        )
                        _btnAdd.isClickable = false
                    } else {
                        Toast.makeText(
                            this, "Input tidak boleh kosong", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    for (i in dataAssets) {
                        if (_inpNo.text.toString() != i.no) {
                            isValid = true
                        } else {
                            isValid = false
                            Toast.makeText(
                                this, "Nomor telah digunakan asset lain", Toast.LENGTH_SHORT
                            ).show()
                            break
                        }
                    }
                    Log.d("dcCount", isValid.toString())
                    if (chooseId != "" && isValid == true) {
                        addData(
                            _inpNo.text.toString(),
                            _inpNama.text.toString(),
                            _inpJumlah.text.toString().toInt(),
                            _inpBerat.text.toString().toFloat(),
                            _inpRuangan.text.toString(),
                            chooseId,
                            chooseKota,
                            0
                        )
                        _btnAdd.isClickable = false
                    } else if (chooseId == "") {
                        Toast.makeText(
                            this, "Input tidak boleh kosong", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this, "Input tidak boleh kosong", Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    fun addData(no: String, nama: String, jumlah: Int, berat: Float, ruangan: String, idKota: String, namaKota: String, fav: Int) {
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
                    adapterKota = ArrayAdapter(this@AddDataPage, android.R.layout.simple_spinner_dropdown_item, list_namaKota)
                    _spinnerKota.adapter = adapterKota
                }
            }

            override fun onFailure(call: Call<DcResponseCity>, t: Throwable) {
                Log.d("gagal","hoyyy")
            }

        })
    }

}