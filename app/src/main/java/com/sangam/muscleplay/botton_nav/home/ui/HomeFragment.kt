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
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentHomeBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_about_us.AboutUsActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_history.HistoryActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_refer_and_earn.ReferAndEarnActivity
import com.sangam.muscleplay.drawer_nav.drawer_nav_support.SupportActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var homeViewModel: HomeViewModel

    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var weight: String
    private lateinit var height: String

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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        autoImageList.clear()
        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
//        listener = activity
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
         callGetUserData()
        observeUserData()
        initListener()
        addImageOnAutoImageSlider()
        getAutoImageSliderImage()
        observerErrorMessageApiResponse()
        callBmiApi()
        callBodyFatApi()
        callDailyCaloriesApi()
        callIdealWeightApi()
        observerBmiApiResponse()
        observerIdealWeightApiResponse()
        observerDailyCaloriesApiResponse()
        observerBodyFatApiResponse()
        observerBmiApiProgressResponse()
        observerIdealWeightApiProgressResponse()
        observerDailyCaloriesApiProgressResponse()
        observerBodyFatApiProgressResponse()

        return root
    }

    fun callGetUserData() {
        userViewModel.getUserData()
    }

    fun observeUserData() {
        userViewModel.userDataResponse.observe(viewLifecycleOwner, Observer {
            headerViewName.text = it?.name
            headerViewEmail.text = it?.email
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
        homeViewModel.callBmiApi("25", "70", "178")
    }

    private fun callBodyFatApi() {
        homeViewModel.callBodyFatApi("25", "male", "70", "178", "50", "96", "92")
    }

    private fun callIdealWeightApi() {
        homeViewModel.callIdealWeightApi("male", "178")
    }

    private fun callDailyCaloriesApi() {
        homeViewModel.callDailyCaloriesApi("25", "male", "178", "70", "level_4")
    }

    private fun observerBmiApiResponse() {
        homeViewModel.bmiResponse.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerIdealWeightApiResponse() {
        homeViewModel.idealWeightResponse.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerDailyCaloriesApiResponse() {
        homeViewModel.dailyCaloriesResponse.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerBodyFatApiResponse() {
        homeViewModel.bodyFatResponse.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerBmiApiProgressResponse() {
        homeViewModel.showProgressBmi.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerIdealWeightApiProgressResponse() {
        homeViewModel.showProgressIdealWeight.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerDailyCaloriesApiProgressResponse() {
        homeViewModel.showProgressDalyCalories.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerBodyFatApiProgressResponse() {
        homeViewModel.showProgressBodyFat.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    private fun observerErrorMessageApiResponse() {
        homeViewModel.errorMessage.observe(requireActivity(), Observer {
//            binding.textView.text = it.data?.bmi.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        binding.drawerLayout.closeDrawer(binding.navView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}