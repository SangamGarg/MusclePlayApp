package com.sangam.muscleplay.botton_nav.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.EditProfileActivity
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity
import com.sangam.muscleplay.databinding.FragmentNotificationsBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database by lazy {
        Firebase.firestore
    }
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        callGetUserData()
        observeUserData()
        initListener()

        return root
    }

    private fun initListener() {
        binding.tvEditProfile.setOnClickListener {
            IntentUtil.startIntent(requireContext(), EditProfileActivity())
        }
        binding.tvLogout.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("339910902548-8pjamj4qhoc5ogfrpptd42uqsnkvt1b4.apps.googleusercontent.com")
                .requestEmail().build()
            GoogleSignIn.getClient(requireContext(), gso).signOut()
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "Logged Out Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.putExtra("FromLogout", true)
            startActivity(intent)
        }
    }

    fun callGetUserData() {
        userViewModel.getUserData()
    }

    fun observeUserData() {
        userViewModel.userDataResponse.observe(viewLifecycleOwner, Observer {
            binding.tvName.text = it?.name

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}