package PABA.UAS.c14210001

//import android.annotation.SuppressLint
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoveKota : AppCompatActivity() {


    private lateinit var _inpKota : EditText
    private lateinit var _spinnerKota : Spinner
    private lateinit var _btnCalc : Button
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
        setContentView(R.layout.activity_move_kota)

        val intentItem = intent.getParcelableExtra(DetailsDataPage.dataItem,DcAssets::class.java)

        _inpKota = findViewById(R.id.inpKota)
        _spinnerKota = findViewById(R.id.inpKotaTujuan)
        _btnCalc = findViewById(R.id.btnCalc)

        list_idKota = arrayListOf()
        list_namaKota = arrayListOf()

        checkData()
        getDataCity()

        _inpKota.setText(intentItem?.namaKota.toString())
        chooseId = intentItem?.idKota.toString()
        chooseKota = intentItem?.namaKota.toString()

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
                    adapterKota = ArrayAdapter(this@MoveKota, android.R.layout.simple_spinner_dropdown_item, list_namaKota)
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