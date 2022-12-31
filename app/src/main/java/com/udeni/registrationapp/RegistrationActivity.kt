package com.udeni.registrationapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.udeni.registrationapp.databinding.ActivityRegistrationBinding
import com.udeni.registrationapp.model.User
import java.io.ByteArrayOutputStream

class RegistrationActivity : AppCompatActivity() {
    lateinit var photo: ImageView
    private val pickImage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        //start data binding
        val binding: ActivityRegistrationBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_registration)
        photo = binding.imgProfile
        val fullname = binding.txtFullname
        val email = binding.txtEmail
        val age = binding.txtAge
        val phone = binding.txtPhone

        //open photo gallery to upload profile image
        binding.btnImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.btnSubmit.setOnClickListener {
            //create User object
            val user = User(
                fullname.text.toString(),
                email.text.toString(),
                age.text.toString(),
                phone.text.toString(),
                imageToByteArray(photo)
            )

            //pass User object to ProfileActivity
            val intent = Intent(this@RegistrationActivity, ProfileActivity::class.java).apply {
                putExtra("USER", user)
            }
            startActivity(intent)
        }

    }

    //preview selected photo from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            photo.setImageURI(data?.data)
        }
    }

    //convert Bitmap image to byte array and return
    private fun imageToByteArray(photo: ImageView): ByteArray {
        val bitmap = (photo.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }
}
