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
import com.rahmawan.ngringinapk.databinding.ActivityEditGaleriBinding
import com.rahmawan.ngringinapk.firebase.galeri.EditGaleri
import com.rahmawan.ngringinapk.firebase.galeri.GetGaleri
import com.rahmawan.ngringinapk.model.Galeri
import com.theartofdev.edmodo.cropper.CropImage
import java.io.FileNotFoundException

class EditGaleriActivity : AppCompatActivity() {
    private val REQUEST_CODE_STORAGE = 1
    private  var type: Int = 1
    private lateinit var bind: ActivityEditGaleriBinding
    private val getGaleri=GetGaleri
    private val editGaleri= EditGaleri
    private lateinit var id:String
    private lateinit var galeri:Galeri
    private var img1: Uri?=null
    private var img2: Uri?=null
    private var img3: Uri?=null

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16,9)
                .getIntent(this@EditGaleriActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditGaleriBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.extras?.getString("id").toString()
        initListener()
        getGaleri.getGaleri(id)

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

    private fun initListener(){
        getGaleri.setOnGaleriListener(object:GetGaleri.GetGaleriListener{
            override fun onSuccess(result: Galeri?) {
                result?.let {
                    galeri=it
                    initData()
                }?:run{
                    Toast.makeText(this@EditGaleriActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditGaleriActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
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
        editGaleri.setOnEditGaleriListener(object :EditGaleri.EditGaleriListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@EditGaleriActivity,result,Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditGaleriActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        bind.btnSimpan.setOnClickListener{
            if(validate()){
                editGaleri.editGaleri(galeri, img1, img2, img3)
            }
        }
    }

    private fun initData(){
        title=galeri.judul
        bind.etJudul.setText(galeri.judul)
        bind.etDeskripsi.setText(galeri.deskripsi)
        if(galeri.gambar1!=null){
            Glide.with(this)
                .load(galeri.gambar1)
                .into(bind.ivFirst)
        }
        if(galeri.gambar2!=null){
            Glide.with(this)
                .load(galeri.gambar2)
                .into(bind.ivSecond)
        }
        if(galeri.gambar3!=null){
            Glide.with(this)
                .load(galeri.gambar3)
                .into(bind.ivThird)
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}