package com.sangam.muscleplay.appUtils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.sangam.muscleplay.R
import com.yalantis.ucrop.UCrop
import java.io.File

object CropImage {
    fun cropImage(
        cropResultLauncher: ActivityResultLauncher<Intent>, context: Context, sourceUri: Uri
    ) {
        val destinationUri = Uri.fromFile(File(context.cacheDir, "cropped_image.png"))
        val options = UCrop.Options()
        options.setToolbarColor(ContextCompat.getColor(context, R.color.app_main_color))
        options.setStatusBarColor(
            ContextCompat.getColor(
                context, R.color.app_main_color
            )
        )
        options.setActiveControlsWidgetColor(
            ContextCompat.getColor(
                context, R.color.white
            )
        )

        val uCrop =
            UCrop.of(sourceUri, destinationUri).withAspectRatio(1f, 1f).withMaxResultSize(450, 450)
                .withOptions(options)
        cropResultLauncher.launch(uCrop.getIntent(context))
    }
}