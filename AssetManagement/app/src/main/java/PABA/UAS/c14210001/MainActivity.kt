package PABA.UAS.c14210001

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

//    val db = Firebase.firestore
//    var dataAssets = ArrayList<DcAssets>()

//    override fun onStart() {
//        super.onStart()
////        checkData()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mFragmentManager = supportFragmentManager
        val mfList = fList()
        val mfFav = fFav()

//        checkData()

        mFragmentManager.findFragmentByTag(fList::class.java.simpleName)
        mFragmentManager.beginTransaction().apply{
            replace(R.id.frameContainer, mfList, fList::class.java.simpleName)
            commit()
        }

        findViewById<ImageView>(R.id.listBtn).setOnClickListener {
            mFragmentManager.findFragmentByTag(fList::class.java.simpleName)
            mFragmentManager.beginTransaction().apply{
                replace(R.id.frameContainer, mfList, fList::class.java.simpleName)
                commit()
            }
        }

        findViewById<ImageView>(R.id.favBtn).setOnClickListener {
            mFragmentManager.findFragmentByTag(fFav::class.java.simpleName)
            mFragmentManager.beginTransaction().apply{
                replace(R.id.frameContainer, mfFav, fFav::class.java.simpleName)
                commit()
            }
        }

//        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
//            val intent = Intent(this@MainActivity,AddDataPage::class.java)
//            startActivity(intent)
//        }
    }

//    fun checkData() {
//        db.collection("tbAssets")
//            .get()
//            .addOnSuccessListener {
//                    result ->
//                dataAssets.clear()
//                for (document in result) {
//                    var readData = DcAssets(
//                        document.data.get("no").toString(),
//                        document.data.get("nama").toString(),
//                        document.data.get("jumlah").toString().toInt(),
//                        document.data.get("berat").toString().toFloat(),
//                        document.data.get("ruangan").toString(),
//                        document.data.get("idKota").toString(),
//                        document.data.get("namaKota").toString(),
//                        document.data.get("fav").toString().toInt()
//                    )
//                    dataAssets.add(readData)
//                }
//            }
//    }
}