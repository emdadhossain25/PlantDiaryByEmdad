package com.example.plantdiarybyemdad.ui.main

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.plantdiarybyemdad.Manifest
import com.example.plantdiarybyemdad.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainFragment : Fragment() {
    private val IMAGE_GALLERY_REQUEST_CODE: Int = 2001
    private val SAVE_IMAGE_REQUEST_CODE = 1999
    private lateinit var currentPhotoPath: String
    private val CAMERA_REQUEST_CODE: Int = 1998
    val CAMERA_PERMISSION_REQUEST_CODE = 1997
    private val LOCATION_PERMISSION_REQUEST_CODE = 2000

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var locationViewModel: LocationViewModel
    var actPlantName: AutoCompleteTextView? = null
    var btnTakePhoto: ImageButton? = null
    var btnLogon: ImageButton? = null
    var imgPlant: ImageView? = null
    var lbllongitudeValue: TextView? = null
    var lblLatitudeValue: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        var root = inflater.inflate(R.layout.fragment_main, container, false)
        actPlantName = root.findViewById(R.id.actPlantName)
        btnTakePhoto = root.findViewById(R.id.btnTakePhoto)
        btnLogon = root.findViewById(R.id.btnLogon)
        imgPlant = root.findViewById(R.id.imgPlant)
        lblLatitudeValue = root.findViewById(R.id.lblLatitudeValue)
        lbllongitudeValue = root.findViewById(R.id.lbllongitudeValue)
        return root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.plants.observe(
            viewLifecycleOwner,
            Observer { plants ->
                actPlantName?.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,
                        plants,
                    ),
                )
            },
        )
        btnTakePhoto?.setOnClickListener {
            prepTakePhoto()
        }

        btnLogon?.setOnClickListener {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                type = "image/*"
                startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
            }
        }

        prepRequestLocationUpdates()


    }

    private fun prepRequestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationUpdates()
        } else {
            val permissionRequest = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_PERMISSION_REQUEST_CODE)
        }

    }

    fun requestLocationUpdates() {
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(viewLifecycleOwner, Observer {
            lblLatitudeValue?.text = it.latitude
            lbllongitudeValue?.text = it.longitude
        })
    }

    /**
     * See if we have permission or not
     */
    private fun prepTakePhoto() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePhoto()
        } else {
            val permissionRequest = arrayOf(android.Manifest.permission.CAMERA)
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted, let's do stuff.
                    takePhoto()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable to take photo without permission",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }

            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable to update location without permission",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
        }
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)
            if (takePictureIntent == null) {
                Toast.makeText(requireContext(), "Unable to save photo", Toast.LENGTH_LONG).show()
            } else {
                //if we are here we have a valid intent
                val photoFile: File = createImageFile()
                photoFile?.also {
                    val photoUri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.plantdiarybyemdad",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)

                    startActivityForResult(takePictureIntent, SAVE_IMAGE_REQUEST_CODE)
                }
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                // now we can get the thumbnail
                val imageBitmap = data!!.extras!!.get("data") as Bitmap
                imgPlant?.setImageBitmap(imageBitmap)
            } else if (requestCode == SAVE_IMAGE_REQUEST_CODE) {
                Toast.makeText(requireContext(), "Image Saved", Toast.LENGTH_LONG).show()
            } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
                if (data != null && data.data != null) {
                    //data.data is the uri
                    val image = data.data
                    val source =
                        ImageDecoder.createSource(requireActivity().contentResolver, image!!)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    imgPlant?.setImageBitmap(bitmap)
                }
            }
        }
    }


    private fun createImageFile(): File {
        //generate a unique filename with date.
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // get access to the directory hwere we can write pictures.
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PlantDiary${timeStamp}", "jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }
}
