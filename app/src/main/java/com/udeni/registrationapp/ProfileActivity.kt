package com.udeni.registrationapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udeni.registrationapp.databinding.ActivityProfileBinding
import com.udeni.registrationapp.model.User

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //get user object from intent
        val user = intent.getSerializableExtra("USER") as User

        //start data binding
        val binding: ActivityProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.fullname = user.fullname
        binding.email = user.email
        binding.age = user.age
        binding.phone = user.phone
        user.let {
            //preview profile image if user has uploaded image in registration page
            if (it.photoByteArray.isNotEmpty()) {
                user.photoByteArray?.let {
                    val bitmap = BitmapFactory.decodeByteArray(
                        user.photoByteArray,
                        0,
                        user.photoByteArray.size
                    )
                    //photo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 10, 10, false));
                    binding.imgProfile.setImageBitmap(bitmap)
                }
            }

            //open email client only if user has entered email in registration page
            when {
                it.email.isNotBlank() -> {
                    var emailSubject = "Registration confirmation";
                    var emailBody = "Hi ${user.fullname}, You have successfully registered."
                    binding.previewEmail.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        val data: Uri = Uri.parse(
                            "mailto:${user.email}?subject=" + Uri.encode(emailSubject)
                                .toString() + "&body=" + Uri.encode(emailBody)
                        )
                        intent.data = data
                        startActivity(intent)
                    }
                }
                else -> {
                    binding.email = "You have not entered Email"
                }
            }

            //open dialer only if user has entered phone number in registration page
            when {
                it.phone.isNotBlank() -> {
                    binding.previewPhone.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:${user.phone}")
                        startActivity(intent)
                    }
                }
                else -> {
                    binding.phone = "You have not entered Phone Number"
                }
            }
        }

    }
}