package com.sangam.muscleplay.onBoardingScreen.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.NetworkConnection
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.UserExtraDetailsFill.UserDetailsActivity
import com.sangam.muscleplay.databinding.ErrorBottomDialogLayoutBinding
import com.sangam.muscleplay.databinding.FragmentSplashScreenBinding
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailsViewModel


class SplashScreenFragment : Fragment() {
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var userDetailsViewModel: UserDetailsViewModel

    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        userDetailsViewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        observersForApiCalls()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                observeConnectionInternet()
            }, 500
        )
        return binding.root
    }

    private fun observeConnectionInternet() {
        NetworkConnection(requireContext()).observe(viewLifecycleOwner, Observer {

            if (it) {
                if (onBoardCheck()) {
                    if (firebaseAuth.currentUser != null) {
                        callGetUserDetailsApi(firebaseAuth.currentUser!!.uid)
                    } else {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_signInActivity)
                    }
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
                }
            } else {
                binding.textViewInternet.visibility = View.VISIBLE
                binding.imageViewInternet.visibility = View.VISIBLE
                binding.textViewOpenSetting.visibility = View.VISIBLE
                binding.splashImage.visibility = View.GONE
                binding.textViewOpenSetting.setOnClickListener {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
            }

        })
    }

    private fun callGetUserDetailsApi(userId: String?) {
        userDetailsViewModel.callGetUserDetails(userId)
    }


    private fun observersForApiCalls() {

        userDetailsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showErrorBottomDialog(
                it
            )
        })
        userDetailsViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        userDetailsViewModel.userDetailsResponse.observe(viewLifecycleOwner, Observer {
            userDetailsViewModel.setUserDetailsResponse(it)

            if (it.gender.isNullOrEmpty()) {
                IntentUtil.startIntent(requireContext(), UserDetailsActivity())
                activity?.finish()
            } else {
                IntentUtil.startIntent(requireContext(), MainActivity())
                activity?.finish()
            }
        })
    }

    private fun showErrorBottomDialog(message: String) {
        val bottomDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val dialogBinding = ErrorBottomDialogLayoutBinding.inflate(layoutInflater)
        bottomDialog.setContentView(dialogBinding.root)
        bottomDialog.setCancelable(false)
        bottomDialog.show()
        dialogBinding.messageTv.text = message
        dialogBinding.continueButton.setOnClickListener {
            bottomDialog.dismiss()
            activity?.finishAffinity()
        }
    }

    private fun onBoardCheck(): Boolean {
        val sharedPref = requireContext().getSharedPreferences("OnBoardShow", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Done", false)
    }
}