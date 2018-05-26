package com.kubekbreha.watsonchatbot.authentication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.kubekbreha.watsonchatbot.R
import com.kubekbreha.watsonchatbot.glide.GlideApp
import com.kubekbreha.watsonchatbot.util.FirestoreUtil
import com.kubekbreha.watsonchatbot.util.StorageUtil
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream


class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private val GALLERY_REQUEST = 1
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        act_settings_btn_back_from_settings.setOnClickListener(this)
        act_settings_profile_photo.setOnClickListener(this)
        act_settings_save.setOnClickListener(this)
        act_settings_btn_profile_photo.setOnClickListener(this)


        this.act_settings_user_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                this@SettingsActivity.act_settings_user_credentials_username_limit
                        .text = act_settings_user_name.text.length.toString()+"/25"

            }
        })

        this.act_settings_user_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                this@SettingsActivity.act_settings_user_credentials_description_limit
                        .text = act_settings_user_description.text.length.toString()+"/100"
            }
        })

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.act_settings_btn_back_from_settings -> {
                onBackPressed()
            }

            R.id.act_settings_profile_photo -> {
                val galleryIntent = Intent()
                galleryIntent.action = Intent.ACTION_GET_CONTENT
                galleryIntent.type = "image/*"
                startActivityForResult(galleryIntent, GALLERY_REQUEST)
            }

            R.id.act_settings_btn_profile_photo -> {
                val galleryIntent = Intent()
                galleryIntent.action = Intent.ACTION_GET_CONTENT
                galleryIntent.type = "image/*"
                startActivityForResult(galleryIntent, GALLERY_REQUEST)
            }

            R.id.act_settings_save -> {
                if (::selectedImageBytes.isInitialized) {
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        FirestoreUtil.updateCurrentUser(this, act_settings_user_name.text.toString(),
                                act_settings_user_description.text.toString(), imagePath)
                    }
                } else {
                    FirestoreUtil.updateCurrentUser(this, act_settings_user_name.text.toString(),
                            act_settings_user_description.text.toString(), null)
                }
            }

            else -> {
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data.data

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                val selectedImageBmp = MediaStore.Images.Media
                        .getBitmap(contentResolver, result.uri)

                val outputStream = ByteArrayOutputStream()
                selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                selectedImageBytes = outputStream.toByteArray()


                act_settings_profile_photo.setImageURI(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }

            pictureJustChanged = true
        }
    }


    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser { user ->
            act_settings_user_name.setText(user.name)
            act_settings_user_description.setText(user.bio)
            if (!pictureJustChanged && user.profilePicturePath != null)
                GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicturePath))
                        .placeholder(R.drawable.setup_profile)
                        .into(act_settings_profile_photo)
        }
    }
}
