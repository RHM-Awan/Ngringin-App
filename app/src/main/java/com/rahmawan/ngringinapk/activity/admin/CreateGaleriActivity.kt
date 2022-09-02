package com.rahmawan.ngringinapk.activity.admin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityCreateGaleriBinding
import com.rahmawan.ngringinapk.firebase.galeri.SaveGaleri
import com.theartofdev.edmodo.cropper.CropImage
import java.io.FileNotFoundException

class CreateGaleriActivity : AppCompatActivity() {
    private val REQUEST_CODE_STORAGE = 1
    private lateinit var bind: ActivityCreateGaleriBinding
    private  var type: Int = 1
    private var img1: Uri?=null
    private var img2: Uri?=null
    private var img3: Uri?=null
    private val saveGaleri=SaveGaleri
    private lateinit var uid:String

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16,9)
                .getIntent(this@CreateGaleriActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCreateGaleriBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Buat Galeri"
        val prefs= getSharedPreferences("user", MODE_PRIVATE)
        uid = prefs.getString("uid", "-")!!
        initListener()

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it.let {it1 ->
                when(type){
                    1->{
                        img1=it1
                        Glide.with(this)
                            .load(img1)
                            .into(bind.ivFirst)
                    }
                    2->{
                        img2=it1
                        Glide.with(this)
                            .load(img2)
                            .into(bind.ivSecond)
                    }
                    3->{
                        img3=it1
                        Glide.with(this)
                            .load(img3)
                            .into(bind.ivThird)
                    }
                    else -> {}
                }

            }
        }
    }
    private fun reset(){
        bind.etJudul.text = null
        bind.etDeskripsi.text = null
        bind.ivFirst.setImageResource(R.drawable.ic_baseline_add_24)
        bind.ivSecond.setImageResource(R.drawable.ic_baseline_add_24)
        bind.ivThird.setImageResource(R.drawable.ic_baseline_add_24)
        img1=null
        img2=null
        img3=null
    }
    private fun initListener(){
        bind.ivFirst.setOnClickListener{
            type=1
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE
            )
        }
        bind.ivSecond.setOnClickListener{
            type=2
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE
            )
        }
        bind.ivThird.setOnClickListener{
            type=3
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE
            )
        }
        saveGaleri.setOnSaveGaleriListener(object:SaveGaleri.SaveGaleriListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@CreateGaleriActivity,result,Toast.LENGTH_SHORT).show()
                reset()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@CreateGaleriActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        bind.btnSimpan.setOnClickListener{
            if(validate()){
                saveGaleri.saveGaleri(
                    bind.etJudul.text.toString(),
                    bind.etDeskripsi.text.toString(),
                    img1,
                    img2,
                    img3,
                    uid
                )
            }
        }
    }
    private fun validate():Boolean{
        var valid = true
        if(bind.etJudul.text.isEmpty()){
            bind.etJudul.error="Judul Harus Diisi"
            valid=false
        }
        if(bind.etDeskripsi.text.isEmpty()){
            bind.etDeskripsi.error="Deskripsi Harus Diisi"
            valid=false
        }
        return valid
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                cropActivityResultLauncher.launch(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Buka galeri tidak diizinkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
//    private val storageActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let {
//                try {
//                    it.data?.let { it1 ->
//                        when(type){
//                            1->{
//                                img1=it1
//                                Glide.with(this)
//                                    .load(img1)
//                                    .into(bind.ivFirst)
//                            }
//                            2->{
//                                img2=it1
//                                Glide.with(this)
//                                    .load(img2)
//                                    .into(bind.ivSecond)
//                            }
//                            3->{
//                                img3=it1
//                                Glide.with(this)
//                                    .load(img3)
//                                    .into(bind.ivThird)
//                            }
//                            else -> {}
//                        }
//                    }
//                } catch (e: FileNotFoundException) {
//                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}