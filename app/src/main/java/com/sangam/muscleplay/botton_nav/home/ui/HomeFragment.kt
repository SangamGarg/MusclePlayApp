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
import com.sangam.muscleplay.calculators.AllCalculatorsActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.appUtils.ApiCallsConstant
import com.sangam.muscleplay.appUtils.IntentUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.botton_nav.home.model.HomeRequestBody
import com.sangam.muscleplay.botton_nav.home.ui.adapter.FitnessFactsAdapter
import com.sangam.muscleplay.botton_nav.home.viewModel.HomePageViewModel
import com.sangam.muscleplay.databinding.FragmentHomeBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_about_us.ui.AboutUsActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_feedback.FeedbackActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_history.HistoryActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui.AiChatBotActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.ui.RecipesActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_refer_and_earn.ReferAndEarnActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_support.ui.SupportActivity
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailData
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomePageViewModel
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
        homeViewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        getUserData()

        @RequiresApi(Build.VERSION_CODES.TIRAMISU) if (!checkPermission()) {
            requestForPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        autoImageList = ArrayList()
        if (!ApiCallsConstant.apiCallsOnceHome) {
            callHomeApi(
                HomeRequestBody(
                    age, gender, weight, height, waist, hip, neck, activityLevel
                )
            )
            ApiCallsConstant.apiCallsOnceHome = true
        }

        initListener()
        apiResponseOservers()

        return binding.root
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


    }

    private fun initListener() {

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.viewFlipper.setOnClickListener {
            IntentUtil.startIntent(requireContext(), AllCalculatorsActivity())
        }
        categoryInitListener()
        binding.swiperefresh.setOnRefreshListener {
            swipeRefreshCalls()
        }

        binding.tvViewAllCalculator.setOnClickListener {
            IntentUtil.startIntent(requireActivity(), AllCalculatorsActivity())
        }

        binding.openDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_about_us -> IntentUtil.startIntent(
                    requireActivity(), AboutUsActivity()
                )

                R.id.nav_support -> IntentUtil.startIntent(requireActivity(), SupportActivity())
                R.id.nav_recipe -> IntentUtil.startIntent(requireActivity(), RecipesActivity())
                R.id.nav_history -> IntentUtil.startIntent(requireActivity(), HistoryActivity())
                R.id.nav_feedback -> IntentUtil.startIntent(
                    requireActivity(), FeedbackActivity()
                )

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
        callHomeApi(
            HomeRequestBody(
                age, gender, weight, height, waist, hip, neck, activityLevel
            )
        )
    }

    private fun openPlayStoreForRating() {
        val uri = Uri.parse("market://details?id=com.sangam.muscleplay")
        val playStoreIntent = Intent(Intent.ACTION_VIEW, uri)
        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(playStoreIntent)
        } catch (e: ActivityNotFoundException) {
            // If Play Store app is not available, open the link in the browser
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.sangam.muscleplay")
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

    private fun callHomeApi(homeRequestBody: HomeRequestBody?) {
        homeViewModel.callHomeDetails(homeRequestBody)
    }


    private fun apiResponseOservers() {
        homeViewModel.homeDetailsResponse.observe(requireActivity(), Observer {

            val adapter = FitnessFactsAdapter(requireContext(), it.fitnessFacts)
            binding.recyclerView.adapter = adapter
            val sliderImages = it.showImages?.sliderImages
            val viewFlipperImages = it.showImages?.viewFlipperImages

            autoImageList.clear()
            if (sliderImages != null) {
                for (image in sliderImages) {
                    autoImageList.add(ImageSlidesModel(image, ""))
                }
            }
            addImageOnAutoImageSlider()


            if (viewFlipperImages != null) {
                for (image in viewFlipperImages) {
                    val imageView = AppCompatImageView(requireContext())
                    imageView.scaleType = ImageView.ScaleType.FIT_XY

                    Glide.with(requireContext()).load(image)
                        .placeholder(com.google.android.material.R.drawable.mtrl_ic_error)
                        .into(imageView)

                    binding.viewFlipper.addView(imageView)
                }
            }

            if (waist?.toDoubleOrNull() != 0.0) {
                binding.tvBodyFatPercentage.text =
                    it.bodyStatsData?.bodyFat?.bodyFatFromBMI.toString()
            } else {
                binding.cardView12.visibility = View.GONE
            }
            binding.tvIdealWeight.text = "${it.bodyStatsData?.idealWeight?.hamwi?.toString()} kg"
            binding.tvBmi.text = it.bodyStatsData?.bmi.toString()

            val dailyCaloriesData = it.bodyStatsData?.dailyCalories?.goals

            when (goal) {
                "Extreme WG" -> binding.tvDailyCalories.text =
                    dailyCaloriesData?.extremeWeightGain?.calory.toString()

                "Normal WG" -> binding.tvDailyCalories.text =
                    dailyCaloriesData?.weightGain?.calory.toString()

                "Mild WG" -> binding.tvDailyCalories.text =
                    dailyCaloriesData?.mildWeightGain?.calory.toString()

                "Extreme WL" -> binding.tvDailyCalories.text =
                    dailyCaloriesData?.extremeWeightLoss?.calory.toString()

                "Normal WL" -> binding.tvDailyCalories.text =
                    dailyCaloriesData?.weightLoss?.calory.toString()

                "Mild WL" -> binding.tvDailyCalories.text =
                    dailyCaloriesData?.mildWeightLoss?.calory.toString()

            }


            binding.swiperefresh.isRefreshing = false

        })
        homeViewModel.showProgress.observe(requireActivity(), Observer {
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

        homeViewModel.errorMessage.observe(requireActivity(), Observer {
            ToastUtil.makeToast(requireContext(), it)
        })
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


    override fun onPause() {
        super.onPause()
        binding.drawerLayout.closeDrawer(binding.navView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}