package com.sangam.muscleplay.botton_nav.home.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.codebyashish.autoimageslider.Enums.ImageAnimationTypes
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Interfaces.ItemsListener
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.Calculators.AllCalculatorsActivity
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentHomeBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_about_us.AboutUsActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_feedback.FeedbackActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_history.HistoryActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui.AiChatBotActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_refer_and_earn.ReferAndEarnActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_support.SupportActivity
import com.sangam.muscleplay.stepCounter.StepCounterActivity
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel

    private var age: String? = null
    private var gender: String? = null
    private var weight: String? = null
    private var height: String? = null
    private var hip: String? = null
    private var waist: String? = null
    private var neck: String? = null
    private var activity_level: String? = null
    private var goal: String? = null

    private val database by lazy {
        Firebase.firestore
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val realTimeDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    lateinit var databaseReference: DatabaseReference
    lateinit var headerViewName: TextView
    lateinit var headerViewEmail: TextView
    lateinit var headerViewImageProfile: CircleImageView
    private lateinit var userViewModel: UserViewModel
    private var listener: ItemsListener? = null
    private val autoImageList by lazy {
        ArrayList<ImageSlidesModel>()
    }

    private var dataFilled: Boolean? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestForPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Toast.makeText(
                    requireContext(), "Notification Permission Granted", Toast.LENGTH_SHORT
                ).show()
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                    showRationaleDialog()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
//        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
//        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
//        listener = activity
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        callGetUserData()
        callGetUserExtraData()
        observeUserData()
        observeUserExtraData()
        initListener()
        getAutoImageSliderImage()
        getViewFlipperImage()
        observerErrorMessageApiResponse()
        observerBmiApiResponse()
        observerIdealWeightApiResponse()
        observerDailyCaloriesApiResponse()
        observerBodyFatApiResponse()
        observerProgressResponse()

        @RequiresApi(Build.VERSION_CODES.TIRAMISU) if (!checkPermission()) {
            requestForPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    private fun showRationaleDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Notification Permission").setMessage(
            "This app requires notification permission to keep you updated. " + "If you deny this time you have to manually go to app setting to allow permission."
        ).setPositiveButton("Ok") { _, _ ->
            requestForPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        builder.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission(): Boolean {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        return ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun categoryInitListener() {
        binding.includeYoga.apply {
            ivCategoryImage.setImageResource(R.drawable.yoga)
            tvCategoryName.text = "Yoga"
        }
        binding.includeWarmUp.apply {
            ivCategoryImage.setImageResource(R.drawable.warmup)
            tvCategoryName.text = "WarmUp"
        }
        binding.includeStrength.apply {
            ivCategoryImage.setImageResource(R.drawable.strength)
            tvCategoryName.text = "Strength"
        }
    }

    fun callGetUserData() {
        userViewModel.getUserData()
    }

    fun callGetUserExtraData() {
        userViewModel.getUserDataExtra()
    }

    fun observeUserData() {
        userViewModel.userDataResponse.observe(viewLifecycleOwner, Observer {
            headerViewName.text = it?.name
            headerViewEmail.text = it?.email
            binding.tvUserName.text = it?.name

            if (it?.profileImageUrl != null) {
                Glide.with(requireContext()).load(it.profileImageUrl)
                    .placeholder(R.drawable.baseline_person_24).into(headerViewImageProfile)
                Glide.with(requireContext()).load(it.profileImageUrl)
                    .placeholder(R.drawable.baseline_person_24).into(binding.ivProfileHome)
            }
        })
    }

    fun observeUserExtraData() {
        userViewModel.userDataExtraResponse.observe(viewLifecycleOwner, Observer {
            Log.d("USEREXTRADETAIL", it.toString())
            dataFilled = it.datafilled
            age = it.age.toString()
            gender = it.gender.toString()
            height = it.height.toString()
            weight = it.weight.toString()
            waist = it.waist.toString()
            neck = it.neck.toString()
            hip = it.hip.toString()
            activity_level = it.activity_level.toString()
            goal = it.goal.toString()

//            if (gender == "male") {
//                binding.imageViewMAleFemale.setBackgroundResource(R.drawable.male)
//            } else if (gender == "female") {
//                binding.imageViewMAleFemale.setBackgroundResource(R.drawable.female)
//            }


//            if (dataFilled == null) {
//                IntentUtil.startIntent(requireActivity(), UserDetailsActivity())
//            }
            if (dataFilled == true) {
                callBmiApi()
                callBodyFatApi()
                callDailyCaloriesApi()
                callIdealWeightApi()
            }
            binding.swiperefresh.isRefreshing = false
        })
    }

    private fun initListener() {
        binding.viewFlipper.setOnClickListener {
            IntentUtil.startIntent(requireContext(), StepCounterActivity())
        }
        categoryInitListener()
        binding.swiperefresh.setOnRefreshListener {
            callGetUserData()
            callGetUserExtraData()
        }

        binding.fabCalculator.setOnClickListener {

            IntentUtil.startIntent(requireActivity(), AllCalculatorsActivity())
        }

        binding.openDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_about_us -> IntentUtil.startIntent(requireActivity(), AboutUsActivity())
                R.id.nav_support -> IntentUtil.startIntent(requireActivity(), SupportActivity())
                R.id.nav_history -> IntentUtil.startIntent(requireActivity(), HistoryActivity())
                R.id.nav_feedback -> IntentUtil.startIntent(requireActivity(), FeedbackActivity())
                R.id.nav_rate_us -> ToastUtil.makeToast(requireContext(), "Soon..When Uploaded")
                //openPlayStoreForRating()
                R.id.nav_ai_chat_bot -> IntentUtil.startIntent(
                    requireActivity(), AiChatBotActivity()
                )

                R.id.nav_check_my_pose -> ToastUtil.makeToast(requireContext(), "Soon..")
                R.id.nav_refer_and_earn -> IntentUtil.startIntent(
                    requireActivity(), ReferAndEarnActivity()
                )
            }
            true
        }
        val headerView = binding.navView.getHeaderView(0)
        headerViewName = headerView.findViewById(R.id.nav_drawer_tv_name)
        headerViewEmail = headerView.findViewById(R.id.nav_drawer_tv_email_id)
        headerViewImageProfile = headerView.findViewById(R.id.nav_drawer_iv_profile)


    }

    private fun openPlayStoreForRating() {
        val uri = Uri.parse("market://details?id=com.sangam.quonote")
        val playStoreIntent = Intent(Intent.ACTION_VIEW, uri)
        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            // If Play Store app is not available, open the link in the browser
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.sangam.quonote")
            )
            startActivity(webIntent)
        }
    }


//    private fun fetchUserData() {
//        val userId = auth.currentUser!!.uid
//        database.collection("users").orderBy("userId") // Order the documents by the userId field
//            .whereEqualTo("userId", userId) // Filter to get only the current user's data
//            .limit(1) // Limit to 1 because we're fetching the current user's data
//            .get().addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val name = document.getString("name") ?: ""
//                    val email = document.getString("email") ?: ""
//                    Log.d("NAMEANDEMAIL", "${name}  AND   ${email}   ")
//                    userViewModel.setUserData(name, email)
//
//                    headerViewName.text = name
//                    headerViewEmail.text = email
//                }
//            }.addOnFailureListener { exception ->
//                Toast.makeText(
//                    context, "Error fetching user data: ${exception.message}", Toast.LENGTH_SHORT
//                ).show()
//            }
//    }

    //
    private fun addImageOnAutoImageSlider() {
        // add some images or titles (text) inside the imagesArrayList


        // set the added images inside the AutoImageSlider
        binding.autoImageSlider.setImageList(autoImageList, ImageScaleType.FIT)

        // set any default animation or custom animation (setSlideAnimation(ImageAnimationTypes.ZOOM_IN))
        binding.autoImageSlider.setSlideAnimation(ImageAnimationTypes.DEPTH_SLIDE)

        // handle click event on item click
        binding.autoImageSlider.onItemClickListener(listener)
    }

    //
    private fun getAutoImageSliderImage() {
        userViewModel.getImageSliderImages()
        userViewModel.imageSliderResponse.observe(viewLifecycleOwner, Observer {
            Log.d("AutoImageSlider", it.toString())
            autoImageList.clear()
            for (image in it) {
                Log.d("AutoImageSlidersss", image)
                autoImageList.add(ImageSlidesModel(image, ""))
            }
            addImageOnAutoImageSlider()
        })
    }

    private fun getViewFlipperImage() {
        userViewModel.getViewFlipperImages()
        userViewModel.viewFlipperResponse.observe(viewLifecycleOwner, Observer {
            for (image in it) {
                val imageView = AppCompatImageView(requireContext())
                imageView.scaleType = ImageView.ScaleType.FIT_XY

                Glide.with(requireContext()).load(image)
                    .placeholder(com.google.android.material.R.drawable.mtrl_ic_error)
                    .into(imageView)

                binding.viewFlipper.addView(imageView)
            }
        })
    }


    private fun callBmiApi() {
        homeViewModel.callBmiApi(age, weight, height)
    }

    private fun callBodyFatApi() {
        homeViewModel.callBodyFatApi(age, gender, weight, height, neck, waist, hip)
    }

    private fun callIdealWeightApi() {
        homeViewModel.callIdealWeightApi(gender, height)
    }

    private fun callDailyCaloriesApi() {
        homeViewModel.callDailyCaloriesApi(age, gender, height, weight, activity_level)
    }

    private fun observerBmiApiResponse() {
        homeViewModel.bmiResponse.observe(requireActivity(), Observer {
            binding.tvBmi.text = it.data?.bmi.toString()
        })
    }

    private fun observerIdealWeightApiResponse() {
        homeViewModel.idealWeightResponse.observe(requireActivity(), Observer {
            binding.tvIdealWeight.text = "${it.data?.hamwi.toString()} kg"
        })
    }

    private fun observerDailyCaloriesApiResponse() {
        homeViewModel.dailyCaloriesResponse.observe(requireActivity(), Observer {
            when (goal) {
                "Extreme WG" -> binding.tvDailyCalories.text =
                    it.data?.goals?.extremeWeightGain?.calory.toString()

                "Normal WG" -> binding.tvDailyCalories.text =
                    it.data?.goals?.weightGain?.calory.toString()

                "Mild WG" -> binding.tvDailyCalories.text =
                    it.data?.goals?.mildWeightGain?.calory.toString()

                "Extreme WL" -> binding.tvDailyCalories.text =
                    it.data?.goals?.extremeWeightLoss?.calory.toString()

                "Normal WL" -> binding.tvDailyCalories.text =
                    it.data?.goals?.weightLoss?.calory.toString()

                "Mild WL" -> binding.tvDailyCalories.text =
                    it.data?.goals?.mildWeightLoss?.calory.toString()

            }
        })
    }


    private fun observerBodyFatApiResponse() {
        homeViewModel.bodyFatResponse.observe(requireActivity(), Observer {
            binding.tvBodyFatPercentage.text = it.data?.bodyFatBMIMethod.toString()
        })
    }

    private fun observerProgressResponse() {
        userViewModel.showProgress.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.constraintLayout.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerLayout.startShimmer()
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()
            }
        })

        userViewModel.showProgressExtra.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.constraintLayout.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerLayout.startShimmer()

            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()

            }
        })


        homeViewModel.showProgressBmi.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.constraintLayout.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerLayout.startShimmer()

            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()

            }
        })
        homeViewModel.showProgressIdealWeight.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.constraintLayout.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE

                binding.shimmerLayout.startShimmer()

            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()

            }
        })

        homeViewModel.showProgressDalyCalories.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.constraintLayout.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerLayout.startShimmer()

            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()

            }
        })
        homeViewModel.showProgressBodyFat.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.constraintLayout.visibility = View.GONE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.shimmerLayout.startShimmer()

            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.constraintLayout.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()

            }
        })
    }

    private fun observerErrorMessageApiResponse() {
        homeViewModel.errorMessage.observe(requireActivity(), Observer {
            ToastUtil.makeToast(requireContext(), it)
        })
    }

    override fun onPause() {
        super.onPause()
        binding.drawerLayout.closeDrawer(binding.navView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}