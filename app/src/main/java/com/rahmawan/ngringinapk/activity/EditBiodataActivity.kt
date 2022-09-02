package com.rahmawan.ngringinapk.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityEditBiodataBinding
import com.rahmawan.ngringinapk.firebase.ImageUpload
import com.rahmawan.ngringinapk.firebase.user.EditUser
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.User
import com.theartofdev.edmodo.cropper.CropImage
import java.text.SimpleDateFormat
import java.util.*

class EditBiodataActivity : AppCompatActivity() {

    private val REQUEST_CODE_STORAGE = 1

    private lateinit var bind: ActivityEditBiodataBinding
    private lateinit var prefs: SharedPreferences

    lateinit var uid:String
    lateinit var user:User
    private lateinit var uri: Uri

    private val getUser = GetUser
    private val editUser = EditUser
    private val imageUpload = ImageUpload
    private var calendar: Calendar = Calendar.getInstance()

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(9,9)
                .getIntent(this@EditBiodataActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditBiodataBinding.inflate(layoutInflater)
        setContentView(bind.root)
        title="Edit Profil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initListener()
        prefs= getSharedPreferences("user", MODE_PRIVATE)
        uid = prefs.getString("uid", "-")!!
        getUser.getUser(uid)

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it.let {it1 ->
                if (it1 != null) {
                    uri = it1
                }
                bind.ivProfil.setImageURI(uri)

            }
        }

    }

    private fun initListener(){
        getUser.setOnUserListener(object: GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                result?.let {
                    user=it
                    initData(it)
                }?:run{
                    Toast.makeText(this@EditBiodataActivity,"Terjadi Kesalahan, Coba Lagi",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditBiodataActivity,error,Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        imageUpload.setOnUploadImageListener(object : ImageUpload.UploadImageListener{
            override fun onSuccess(result: String) {
                user.image = result
                editUser.editUser(user)
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditBiodataActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        editUser.setOnEditUserListener(object : EditUser.EditUserListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@EditBiodataActivity,result,Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditBiodataActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        bind.ivProfil.setOnClickListener{
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE
            )
        }
        bind.etTglLahir.setOnClickListener{
            showCalendar()
        }
        bind.btnSimpan.setOnClickListener{
            if(validate()) {
                user.name=bind.etNama.text.toString()
                user.email=bind.etEmail.text.toString()
                user.birthday=stringDateToLong(bind.etTglLahir.text.toString())
                user.phone=bind.etTelepon.text.toString()
                user.gender=bind.spJk.selectedItem.toString()
                user.address= bind.etAlamat.text.toString()
                if(this::uri.isInitialized) {
                    imageUpload.uploadImage(uri, user.idUser!!)
                }else{
                    editUser.editUser(user)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    }


//    private val storageActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let {
//                try {
//                    it.data?.let { it1 ->
//                        uri=it1
//                        Glide.with(this)
//                            .load(uri)
//                            .into(bind.ivProfil)
//                    }
//                } catch (e: FileNotFoundException) {
//                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    @SuppressLint("SimpleDateFormat")
    private fun initData(user: User){
        bind.etNama.setText(user.name)
        bind.etEmail.setText(user.email)
        bind.spJk.setSelection(if(user.gender.equals("Laki-laki")) 0 else 1)
        bind.etTglLahir.setText(SimpleDateFormat("dd MMMM yyyy").format(Date(user.birthday!!)))
        bind.etTelepon.setText(user.phone)
        bind.etAlamat.setText(user.address)
        Glide.with(this)
            .load(if(user.image.equals("-")) R.drawable.nophoto else user.image)
            .into(bind.ivProfil)
    }

    private fun validate():Boolean{
        var valid = true
        if(bind.etNama.text.isEmpty()){
            valid=false
            bind.etNama.error="Nama Harus Diisi"
        }
        if(bind.etEmail.text.isEmpty()){
            valid=false
            bind.etEmail.error="Email Harus Diisi"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(bind.etEmail.text.toString()).matches()){
            valid=false
            bind.etEmail.error="Email tidak valid"
        }
        if(bind.etTelepon.text.isEmpty()){
            valid=false
            bind.etTelepon.error="Telepon Harus Diisi"
        }
        if(bind.etAlamat.text.isEmpty()){
            valid=false
            bind.etAlamat.error="Alamat Harus Diisi"
        }
        return valid
    }

    @SuppressLint("SimpleDateFormat")
    private fun showCalendar() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                bind.etTglLahir.setText(SimpleDateFormat("dd MMMM yyyy").format(GregorianCalendar(year,month,dayOfMonth).time))
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        if (!datePickerDialog.isShowing) {
            datePickerDialog.show()
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun stringDateToLong(stringDate:String):Long{
        return SimpleDateFormat("dd MMMM yyyy").parse(stringDate).time
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}