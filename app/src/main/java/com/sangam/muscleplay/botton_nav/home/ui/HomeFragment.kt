package com.sangam.muscleplay.botton_nav.home.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codebyashish.autoimageslider.Enums.ImageAnimationTypes
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Interfaces.ItemsListener
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.sangam.muscleplay.AppUtils.ApiCallsConstant
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.Calculators.AllCalculatorsActivity
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.model.FitnessFactsResponseModel
import com.sangam.muscleplay.botton_nav.home.ui.adapter.FitnessFactsAdapter
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentHomeBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_about_us.AboutUsActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_feedback.FeedbackActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_history.HistoryActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui.AiChatBotActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.ui.RecipesActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_refer_and_earn.ReferAndEarnActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_support.SupportActivity
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailData
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var userData: UserDetailsResponseModel
    private var age: String? = null
    private var gender: String? = null
    private var weight: String? = null
    private var height: String? = null
    private var hip: String? = null
    private var waist: String? = null
    private var neck: String? = null
    private var activityLevel: String? = null
    private var goal: String? = null
    private lateinit var headerViewName: TextView
    private lateinit var headerViewEmail: TextView
    private lateinit var headerViewImageProfile: CircleImageView
    private var listener: ItemsListener? = null
    private lateinit var autoImageList: ArrayList<ImageSlidesModel>


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
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        getUserData()
        autoImageList = ArrayList()
        if (!ApiCallsConstant.apiCallsOnceHome) {
            ApiCallsConstant.apiCallsOnceHome = true
            ApiCallsConstant.apiCallsOnceCalculators = false

        }
        getAutoImageSliderImage()
        getViewFlipperImage()
        initListener()
        observerErrorMessageApiResponse()
        observerApiResponses()
        observerProgressResponse()

        return binding.root
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

    private fun getUserData() {
        userData = UserDetailData.userDetailsResponseModel!!

        binding.tvUserName.text = userData.name

        age = userData.age.toString()
        gender = userData.gender.toString()
        height = userData.measurements?.height.toString()
        weight = userData.measurements?.weight.toString()
        waist = userData.measurements?.waist.toString()
        neck = userData.measurements?.neck.toString()
        hip = userData.measurements?.hip.toString()
        activityLevel = userData.measurements?.activityLevel.toString()
        goal = userData.measurements?.goal.toString()

        if (userData.profileImageUrl != null) {
            Glide.with(requireContext()).load(userData.profileImageUrl)
                .placeholder(R.drawable.baseline_person_24).into(binding.ivProfileHome)
        }


        @RequiresApi(Build.VERSION_CODES.TIRAMISU) if (!checkPermission()) {
            requestForPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        if (!ApiCallsConstant.apiCallsOnceCalculators) {
            callBmiApi()
            callBodyFatApi()
            callDailyCaloriesApi()
            callIdealWeightApi()
            ApiCallsConstant.apiCallsOnceCalculators = true
        }


    }

    private fun initListener() {
        val listOfData = arrayListOf(
            FitnessFactsResponseModel(
                "Healthy Eating Habits",
                "Eating a balanced diet rich in fruits, vegetables, lean proteins, and whole grains is essential for overall health.",
                "https://images.pexels.com/photos/841130/pexels-photo-841130.jpeg?cs=srgb&dl=pexels-victor-freitas-841130.jpg&fm=jpg"
            ), FitnessFactsResponseModel(
                "Importance of Hydration",
                "Staying hydrated is crucial for maintaining bodily functions, regulating body temperature, and supporting cognitive function.",
                "https://images.pexels.com/photos/414029/pexels-photo-414029.jpeg?cs=srgb&dl=pexels-pixabay-414029.jpg&fm=jpg"
            ), FitnessFactsResponseModel(
                "Benefits of Regular Exercise",
                "Regular exercise can improve your mood, boost energy levels, and reduce the risk of chronic diseases.",
                "https://images.pexels.com/photos/414029/pexels-photo-414029.jpeg?cs=srgb&dl=pexels-pixabay-414029.jpg&fm=jpg"
            )
        )

        val adapter = FitnessFactsAdapter(requireContext(), listOfData)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.viewFlipper.setOnClickListener {
            IntentUtil.startIntent(requireContext(), AllCalculatorsActivity())
        }
        categoryInitListener()
        binding.swiperefresh.setOnRefreshListener {
            swipeRefreshCalls()
        }
//        binding.includeCaloriesInFood.apply {
//            tvNameOfCalculator.text = "Calories In Food Calculator"
//            ivImageOfCalculator.setImageResource(R.drawable.caloriesinfood)
//            calculatorLayout.setOnClickListener {
//                findNavController().navigate(R.id.action_navigation_home_to_caloriesInFoodCalculatorFragment2)
//
//            }
//        }
//
//        binding.includeBmi.apply {
//            tvNameOfCalculator.text = "Bmi Calculator"
//            ivImageOfCalculator.setImageResource(R.drawable.bmicalculator)
//            calculatorLayout.setOnClickListener {
//                findNavController().navigate(R.id.action_navigation_home_to_bmiCalculatorFragment2)
//
//            }
//        }
//        binding.includeDailyCalories.apply {
//            tvNameOfCalculator.text = "Daily Calories Calculator"
//            ivImageOfCalculator.setImageResource(R.drawable.dailycalories)
//            calculatorLayout.setOnClickListener {
//                findNavController().navigate(R.id.action_navigation_home_to_dailyCaloriesCalculatorFragment2)
//
//            }
//        }
//
//        binding.includeBurnedCaloriesFromActivity.apply {
//            tvNameOfCalculator.text = "Burned Calories From An Activity Calculator"
//            ivImageOfCalculator.setImageResource(R.drawable.burnedcaloriesfromactivity)
//            calculatorLayout.setOnClickListener {
//                findNavController().navigate(R.id.action_navigation_home_to_burnedCaloriesFromActivityFragment2)
//
//            }
//        }


        binding.tvViewAllCalculator.setOnClickListener {

            IntentUtil.startIntent(requireActivity(), AllCalculatorsActivity())
        }

        binding.openDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_about_us -> IntentUtil.startIntent(requireActivity(), AboutUsActivity())
                R.id.nav_support -> IntentUtil.startIntent(requireActivity(), SupportActivity())
                R.id.nav_recipe -> IntentUtil.startIntent(requireActivity(), RecipesActivity())
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
        headerViewName.text = userData.name
        headerViewEmail.text = userData.email
        if (userData.profileImageUrl != null) {
            Glide.with(requireContext()).load(userData.profileImageUrl)
                .placeholder(R.drawable.baseline_person_24).into(headerViewImageProfile)
        }


    }

    private fun swipeRefreshCalls() {
        getAutoImageSliderImage()
        getViewFlipperImage()
        callBmiApi()
        callBodyFatApi()
        callDailyCaloriesApi()
        callIdealWeightApi()
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
            autoImageList.clear()
            for (image in it) {
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
        homeViewModel.callDailyCaloriesApi(age, gender, height, weight, activityLevel)
    }

    private fun observerApiResponses() {
        homeViewModel.bmiResponse.observe(requireActivity(), Observer {
            binding.tvBmi.text = it.data?.bmi.toString()
            binding.swiperefresh.isRefreshing = false

        })

        homeViewModel.idealWeightResponse.observe(requireActivity(), Observer {
            binding.tvIdealWeight.text = "${it.data?.hamwi.toString()} kg"
            binding.swiperefresh.isRefreshing = false

        })
        homeViewModel.bodyFatResponse.observe(requireActivity(), Observer {
            binding.tvBodyFatPercentage.text = it.data?.bodyFatBMIMethod.toString()
            binding.swiperefresh.isRefreshing = false

        })

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
            binding.swiperefresh.isRefreshing = false

        })
    }

    private fun observerProgressResponse() {
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