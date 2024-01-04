package com.sangam.muscleplay.botton_nav.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codebyashish.autoimageslider.Enums.ImageAnimationTypes
import com.codebyashish.autoimageslider.Enums.ImageScaleType
import com.codebyashish.autoimageslider.Interfaces.ItemsListener
import com.codebyashish.autoimageslider.Models.ImageSlidesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.ExtraDetailsScreen.UserDetailsActivity
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentHomeBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_about_us.AboutUsActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_history.HistoryActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_refer_and_earn.ReferAndEarnActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_support.SupportActivity

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

    private val database by lazy {
        Firebase.firestore
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    lateinit var headerViewName: TextView
    lateinit var headerViewEmail: TextView
    private lateinit var userViewModel: UserViewModel
    private var listener: ItemsListener? = null
    private val autoImageList by lazy {
        ArrayList<ImageSlidesModel>()
    }
    var url: String? = null
    var url1: String? = null
    private var dataFilled: Boolean? = null

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        autoImageList.clear()
        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
//        listener = activity
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        callGetUserData()
        callGetUserExtraData()
        observeUserData()
        observeUserExtraData()
        initListener()
        addImageOnAutoImageSlider()
        getAutoImageSliderImage()
        observerErrorMessageApiResponse()
        observerBmiApiResponse()
        observerIdealWeightApiResponse()
        observerDailyCaloriesApiResponse()
        observerBodyFatApiResponse()
        observerProgressResponse()


        return root
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
//            if (dataFilled == null) {
//                IntentUtil.startIntent(requireActivity(), UserDetailsActivity())
//            }
            callBmiApi()
            callBodyFatApi()
            callDailyCaloriesApi()
            callIdealWeightApi()
        })
    }

    private fun initListener() {


        binding.openDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_about_us -> IntentUtil.startIntent(requireActivity(), AboutUsActivity())
                R.id.nav_support -> IntentUtil.startIntent(requireActivity(), SupportActivity())
                R.id.nav_history -> IntentUtil.startIntent(requireActivity(), HistoryActivity())
                R.id.nav_refer_and_earn -> IntentUtil.startIntent(
                    requireActivity(), ReferAndEarnActivity()
                )
            }
            true
        }
        val headerView = binding.navView.getHeaderView(0)
        headerViewName = headerView.findViewById(R.id.nav_drawer_tv_name)
        headerViewEmail = headerView.findViewById(R.id.nav_drawer_tv_email_id)


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


    private fun addImageOnAutoImageSlider() {
        // add some images or titles (text) inside the imagesArrayList


        // set the added images inside the AutoImageSlider
        binding.autoImageSlider.setImageList(autoImageList, ImageScaleType.FIT)

        // set any default animation or custom animation (setSlideAnimation(ImageAnimationTypes.ZOOM_IN))
        binding.autoImageSlider.setSlideAnimation(ImageAnimationTypes.DEPTH_SLIDE)

        // handle click event on item click
        binding.autoImageSlider.onItemClickListener(listener)
    }

    private fun getAutoImageSliderImage() {
        database.collection("imageSliderImage").get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    url = document.getString("url").toString()
                    url1 = document.getString("url1").toString()
                    Log.d("imageSlider", url1!!)
                    Log.d("imageSlider", url!!)
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Error getting user data: ${it.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
            binding.tvIdealWeight.text = it.data?.hamwi.toString()
        })
    }

    private fun observerDailyCaloriesApiResponse() {
        homeViewModel.dailyCaloriesResponse.observe(requireActivity(), Observer {
            binding.tvDailyCalories.text = it.data?.BMR.toString()
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
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })

        userViewModel.showProgressExtra.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })


        homeViewModel.showProgressBmi.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })
        homeViewModel.showProgressIdealWeight.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })

        homeViewModel.showProgressDalyCalories.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
            }
        })
        homeViewModel.showProgressBodyFat.observe(requireActivity(), Observer {
            if (it) {
                binding.mainView.visibility = View.GONE
                binding.progreessBar.visibility = View.VISIBLE
            } else {
                binding.mainView.visibility = View.VISIBLE
                binding.progreessBar.visibility = View.GONE
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