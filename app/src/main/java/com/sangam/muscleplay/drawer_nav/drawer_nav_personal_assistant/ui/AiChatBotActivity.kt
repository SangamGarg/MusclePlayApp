package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.databinding.ActivityAiChatBotBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.MessageModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.ui.adapter.AiChatBotAdapter
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.viewModel.AiChatBotViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AiChatBotActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAiChatBotBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: AiChatBotViewModel
//    private val autho = "Bearer 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"
    var list = ArrayList<MessageModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var adapter: AiChatBotAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
        viewModel = ViewModelProvider(this)[AiChatBotViewModel::class.java]
//        observeProgress()
//        observerAiChatBotApiResponse()
//        observerErrorMessageApiResponse()
        initListener()
    }


    private fun initListener() {

        val harassmentSafety = SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH)

        val hateSpeechSafety =
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)
        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyCfWRYeFeo025edkColjrYVgRqjPClFjVk",
            safetySettings = listOf(harassmentSafety, hateSpeechSafety)
        )


        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.stackFromEnd = true
        binding.rvShowChat.layoutManager = mLayoutManager
        adapter = AiChatBotAdapter(this, list)
        binding.rvShowChat.adapter = adapter
//
        binding.buttonSubmit.setOnClickListener {
            val prompt = binding.EtQuesion.text.toString()
            binding.EtQuesion.text.clear()
            if (prompt.isEmpty()) {
                ToastUtil.makeToast(this, "Please Input Message")
            } else {


                list.add(MessageModel(true, prompt))
                adapter.notifyItemChanged(list.size - 1)
                binding.rvShowChat.recycledViewPool.clear()
                binding.rvShowChat.smoothScrollToPosition(list.size - 1)
                CoroutineScope(Dispatchers.Main).launch {
                    val chat = generativeModel.startChat(
                        history = emptyList()
//            history = listOf(content(role = "user") { text("Hello, I have 2 dogs in my house.") },
//                content(role = "model") { text("Great to meet you. What would you like to know?") })
                    )
                    val chat1 = chat.sendMessage(prompt).text
                    Log.d("AICHATBOTRESPONSE", chat1.toString())
                    list.add(MessageModel(false, chat1.toString()))
                    adapter.notifyItemChanged(list.size - 1)
                    binding.rvShowChat.recycledViewPool.clear()
                    binding.rvShowChat.smoothScrollToPosition(list.size - 1)
                }

//                callAIChatBotApi(
//                    autho, "application/json", AiChatBotRequestBodyModel(
//                        250, "gpt-3.5-turbo-instruct", question, 0.7
//                    )
//                )
            }
        }
    }

//    private fun callAIChatBotApi(
//        authorization: String,
//        contentType: String,
//        aiChatBotRequestBodyModel: AiChatBotRequestBodyModel
//    ) {
//        viewModel.callAiChatBotApi(authorization, contentType, aiChatBotRequestBodyModel)
//    }
//
//    private fun observerAiChatBotApiResponse() {
//        viewModel.aiChatBotResponse.observe(this, Observer {
//
//        })
//    }
//
//    private fun observeProgress() {
//        viewModel.showProgress.observe(this, Observer {
////            if (it) {
////                binding.progressBar.visibility = View.VISIBLE
////                binding.mainView.visibility = View.GONE
////            } else {
////                binding.progressBar.visibility = View.GONE
////                binding.mainView.visibility = View.VISIBLE
////            }
//        })
//    }
//
//    private fun observerErrorMessageApiResponse() {
//        viewModel.errorMessage.observe(this, Observer {
//            ToastUtil.makeToast(this, it)
//        })
//    }
}