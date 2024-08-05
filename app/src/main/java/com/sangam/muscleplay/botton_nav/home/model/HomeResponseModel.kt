package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class HomeResponseModel(
    @SerializedName("request_result") var requestResult: String? = null,
    @SerializedName("bodyStatsData") var bodyStatsData: BodyStatsData? = BodyStatsData(),
    @SerializedName("fitnessFacts") var fitnessFacts: ArrayList<FitnessFacts> = arrayListOf(),
    @SerializedName("showImages") var showImages: ShowImages? = ShowImages()
)

data class ShowImages(
    @SerializedName("viewFlipperImages") var viewFlipperImages: ArrayList<String> = arrayListOf(),
    @SerializedName("sliderImages") var sliderImages: ArrayList<String> = arrayListOf()

)

data class Goals(

    @SerializedName("maintain weight") var maintainWeight: String? = null,
    @SerializedName("Mild weight loss") var mildWeightLoss: WeightLossGoal? = WeightLossGoal(),
    @SerializedName("Weight loss") var weightLoss: WeightLossGoal? = WeightLossGoal(),
    @SerializedName("Extreme weight loss") var extremeWeightLoss: WeightLossGoal? = WeightLossGoal(),
    @SerializedName("Mild weight gain") var mildWeightGain: WeightGainGoal? = WeightGainGoal(),
    @SerializedName("Weight gain") var weightGain: WeightGainGoal? = WeightGainGoal(),
    @SerializedName("Extreme weight gain") var extremeWeightGain: WeightGainGoal? = WeightGainGoal()

)

data class WeightGainGoal(

    @SerializedName("gain_weight") var gainWeight: String? = null,
    @SerializedName("calory") var calory: String? = null

)

data class WeightLossGoal(
    @SerializedName("loss_weight") var lossWeight: String? = null,
    @SerializedName("calory") var calory: String? = null
)

data class BodyStatsData(
    @SerializedName("bmi") var bmi: String? = null,
    @SerializedName("idealWeight") var idealWeight: IdealWeight? = IdealWeight(),
    @SerializedName("dailyCalories") var dailyCalories: DailyCalories? = DailyCalories(),
    @SerializedName("bodyFat") var bodyFat: BodyFat? = BodyFat()
)

data class DailyCalories(

    @SerializedName("BMR") var BMR: String? = null,
    @SerializedName("goals") var goals: Goals? = Goals()

)

data class IdealWeight(

    @SerializedName("Hamwi") var hamwi: String? = null,
    @SerializedName("Devine") var devine: String? = null,
    @SerializedName("Miller") var miller: String? = null,
    @SerializedName("Robinson") var robinson: String? = null

)

data class BodyFat(

    @SerializedName("BodyFatPercentage (U.S.NAVY)") var usNavy: String? = null,
    @SerializedName("BodyFatMass") var bodyFatMass: String? = null,
    @SerializedName("LeanBodyMass") var leanBodyMass: String? = null,
    @SerializedName("BodyFatFromBMI") var bodyFatFromBMI: String? = null

)

data class FitnessFacts(

    @SerializedName("factsHeadline") var factsHeadline: String? = null,
    @SerializedName("factsSummary") var factsSummary: String? = null,
    @SerializedName("imageUrl") var imageUrl: String? = null

)
