package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui.adapter.CaloriesInFoodAdapter
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityAiChatBotBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotRequestBodyModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.MessageModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui.adapter.AiChatBotAdapter
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.viewModel.AiChatBotViewModel
import okhttp3.MediaType
import okhttp3.RequestBody

class AiChatBotActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAiChatBotBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: AiChatBotViewModel
    private val autho = "Bearer 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"
    var list = ArrayList<MessageModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var adapter: AiChatBotAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
        viewModel = ViewModelProvider(this)[AiChatBotViewModel::class.java]
        observeProgress()
        observerAiChatBotApiResponse()
        observerErrorMessageApiResponse()
        initListener()
    }

    private fun initListener() {

        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.stackFromEnd = true
        binding.rvShowChat.layoutManager = mLayoutManager
        adapter = AiChatBotAdapter(this, list)
        binding.rvShowChat.adapter = adapter

        binding.buttonSubmit.setOnClickListener {
            val question = binding.EtQuesion.text.toString()
            binding.EtQuesion.text.clear()
            if (question.isEmpty()) {
                ToastUtil.makeToast(this, "Please Input Message")
            } else {
                list.add(MessageModel(true, question))
                adapter.notifyItemChanged(list.size - 1)
                binding.rvShowChat.recycledViewPool.clear()
                binding.rvShowChat.smoothScrollToPosition(list.size - 1)
                callAIChatBotApi(
                    autho, "application/json", AiChatBotRequestBodyModel(
                        250, "gpt-3.5-turbo-instruct", "What is Apple", 0.7
                    )
                )
            }
        }
    }

    private fun callAIChatBotApi(
        authorization: String,
        contentType: String,
        aiChatBotRequestBodyModel: AiChatBotRequestBodyModel
    ) {
        viewModel.callAiChatBotApi(authorization, contentType, aiChatBotRequestBodyModel)
    }

    private fun observerAiChatBotApiResponse() {
        viewModel.aiChatBotResponse.observe(this, Observer {
            list.add(MessageModel(false, it.choices.first().text))
            adapter.notifyItemChanged(list.size - 1)
            binding.rvShowChat.recycledViewPool.clear()
            binding.rvShowChat.smoothScrollToPosition(list.size - 1)
        })
    }

    private fun observeProgress() {
        viewModel.showProgress.observe(this, Observer {
//            if (it) {
//                binding.progressBar.visibility = View.VISIBLE
//                binding.mainView.visibility = View.GONE
//            } else {
//                binding.progressBar.visibility = View.GONE
//                binding.mainView.visibility = View.VISIBLE
//            }
        })
    }

    private fun observerErrorMessageApiResponse() {
        viewModel.errorMessage.observe(this, Observer {
            ToastUtil.makeToast(this, it)
        })
    }
}