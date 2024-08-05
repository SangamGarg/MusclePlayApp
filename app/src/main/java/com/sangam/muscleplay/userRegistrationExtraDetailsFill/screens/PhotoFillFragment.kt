package com.sangam.muscleplay.userRegistrationExtraDetailsFill.screens

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sangam.muscleplay.R
import com.sangam.muscleplay.appUtils.CropImage
import com.sangam.muscleplay.databinding.FragmentPhotoFillBinding
import com.yalantis.ucrop.UCrop
import java.io.File


class PhotoFillFragment : Fragment() {
    private lateinit var binding: FragmentPhotoFillBinding
    private lateinit var auth: FirebaseAuth
    private var photoUrl: String? = ""
    private var storageRef = Firebase.storage
    private var uri: Uri? = null
    private lateinit var imageUri: Uri
    private val contract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                CropImage.cropImage(cropResultLauncher, requireContext(), imageUri)
            } else {
                binding.ivProfile.visibility = View.VISIBLE
                binding.ivProfile.setImageURI(uri)
                uri = null
            }
        }

    private val requestForPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                contract.launch(imageUri)
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showRationaleDialog()
                } else {
                    val message =
                        "You've denied camera permission twice. To enable it, open app settings."
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        }
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private val cropResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = result.data?.let { UCrop.getError(it) }
                Toast.makeText(
                    requireContext(),
                    "Crop error: ${cropError?.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (result.data != null) {
                val croppedUri = UCrop.getOutput(result.data!!)
                if (croppedUri != null) {
                    binding.ivProfile.setImageDrawable(null)
                    binding.ivProfile.visibility = View.VISIBLE
                    binding.ivProfile.setImageURI(croppedUri)
                    uri = croppedUri
                } else {
                    Toast.makeText(requireContext(), "Crop failed", Toast.LENGTH_SHORT).show()
                }
            } else {
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoFillBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
//        inflater.inflate(R.layout.fragment_photo_fill, container, false)
        activity?.window?.statusBarColor = Color.WHITE

        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance()

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                CropImage.cropImage(cropResultLauncher, requireContext(), uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        imageUri = createImageUri()!!
        initListener()

        return binding.root
    }

    private fun initListener() {

        binding.chooseImage.setOnClickListener {
            chooseImage()
        }
        binding.tvNext.setOnClickListener {
            uploadImage()
        }
        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_photoFillFragment_to_genderFragment,
                Bundle().apply {
                    putString("photoUrl", "")
                })
        }

    }


    private fun uploadImage() {
        if (uri == null) {
            Toast.makeText(requireContext(), "Choose an image", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Wait while uploading image...")
        progressDialog.show()
        val imageRef = storageRef.reference.child("profile_images")
            .child("${System.currentTimeMillis()} ${auth.currentUser!!.uid}")
        imageRef.putFile(uri!!).addOnSuccessListener { task ->
            task.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                photoUrl = uri.toString()
                progressDialog.dismiss()
                findNavController().navigate(R.id.action_photoFillFragment_to_genderFragment,
                    Bundle().apply {
                        putString("photoUrl", photoUrl ?: "")
                    })
            }?.addOnFailureListener {
                Toast.makeText(
                    requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT
                ).show()
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT
            ).show()
            progressDialog.dismiss()
        }
    }

    private fun chooseImage() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_choose_profile_image, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        val gallery = view.findViewById<TextView>(R.id.chooseGallery)
        gallery.setOnClickListener {
            dialog.dismiss()
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        val camera = view.findViewById<TextView>(R.id.chooseCamera)
        camera.setOnClickListener {
            dialog.dismiss()
            if (checkPermission()) {
                contract.launch(imageUri)
            } else {
                requestForPermission.launch(android.Manifest.permission.CAMERA)
            }

        }
    }

    private fun showRationaleDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Camera Permission")
            .setMessage("This app requires camera permission to take profile photos. If you deny this time you have to manually go to app setting to allow permission.")
            .setPositiveButton("Ok") { _, _ ->
                requestForPermission.launch(android.Manifest.permission.CAMERA)
            }
        builder.create().show()
    }

    private fun checkPermission(): Boolean {
        val permission = android.Manifest.permission.CAMERA
        return ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir, "profile_photos.png")
        return FileProvider.getUriForFile(
            requireContext(), "com.sangam.muscleplay.fileProvider", image
        )
    }


}