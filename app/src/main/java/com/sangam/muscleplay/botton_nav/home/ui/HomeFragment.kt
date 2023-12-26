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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.UserViewModel
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
//        val userData = userViewModel.userData.value
//        listener = activity
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initListener()
        addImageOnAutoImageSlider()
        getAutoImageSliderImage()
        callBmiApi()
        observerBmiApiResponse()





        return root
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
        database.collection("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val name = document.getString("name")
                    val email = document.getString("email")

                    userViewModel.setUserData(name, email)

                    headerViewName.text = name
                    headerViewEmail.text = email
                }
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Error getting user data: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val headerView = binding.navView.getHeaderView(0)
        headerViewName = headerView.findViewById(R.id.nav_drawer_tv_name)
        headerViewEmail = headerView.findViewById(R.id.nav_drawer_tv_email_id)

//        headerViewName.text = userData?.name
//        headerViewEmail.text = userData?.email


    }

    private fun addImageOnAutoImageSlider() {
        // add some images or titles (text) inside the imagesArrayList
        val a = "https://picsum.photos/id/239/200/300"
        autoImageList.add(ImageSlidesModel(a, ""))
        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))
        autoImageList.add(ImageSlidesModel("https://picsum.photos/id/239/200/300", ""))

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
//        homeViewModel.callBmiApi()
    }

    private fun observerBmiApiResponse() {
        homeViewModel.bmiResponse.observe(requireActivity(), Observer {
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