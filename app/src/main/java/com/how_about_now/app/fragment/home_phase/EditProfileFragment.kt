package com.how_about_now.app.fragment.home_phase

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.how_about_now.app.R
import com.how_about_now.app.adapter.EditProfileAdapter
import com.how_about_now.app.data.profile_data.*
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.fragment.BottomSheetFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import com.how_about_now.app.utils.BottomSheetImageCallBack
import com.how_about_now.app.utils.FileUtils
import com.how_about_now.app.utils.ItemMoveCallback
import kotlinx.android.synthetic.main.fragment_edit_profile_new.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : BaseFragment(), View.OnClickListener,
    EditProfileAdapter.AddImageCallBack, BottomSheetImageCallBack.ImageCallBackListener {

    val ONE = 301
    val TWO = 302
    val THREE = 303
    val FOUR = 304
    val FIVE = 305
    val SIX = 306

    private var requestNumber: Int? = 0

    private var imageOne: Uri? = null
    private var imageTwo: Uri? = null
    private var imageThree: Uri? = null
    private var imageFour: Uri? = null
    private var imageFive: Uri? = null
    private var imageSix: Uri? = null

    private var questionList = ArrayList<QuestionListMsg>()
    private var gender: String? = ""
    private var isHide: Boolean = true
    private var isHideOne: Boolean = true
    private var isHideTwo: Boolean = true
    private var profileImagesUriArrayList = ArrayList<String>()
    private var updateImageUriArrayList = ArrayList<Uri>()
    private var editProfileAdapter: EditProfileAdapter? = null
    private var calender: Calendar? = null
    private var getUserInfoMsgArrayList = ArrayList<GetUserInfoMsg>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (!baseActivity!!.store.getBoolean("isTrue",false))
//            if (arguments != null) {
//                getUserInfoMsgArrayList =
//                    arguments!!.getParcelableArrayList<GetUserInfoMsg>("profileData")!!
//
//            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getProfileData()
        hitGetUserInfoApi()
        init()
    }

    private fun getProfileData() {
        if (getUserInfoMsgArrayList.size > 0) {
            profileImagesUriArrayList.add(getUserInfoMsgArrayList.get(0).image1)
            profileImagesUriArrayList.add(getUserInfoMsgArrayList.get(0).image2)
            profileImagesUriArrayList.add(getUserInfoMsgArrayList.get(0).image3)
            profileImagesUriArrayList.add(getUserInfoMsgArrayList.get(0).image4)
            profileImagesUriArrayList.add(getUserInfoMsgArrayList.get(0).image5)
            profileImagesUriArrayList.add(getUserInfoMsgArrayList.get(0).image6)

            questionTV.setText(getUserInfoMsgArrayList.get(0).user_answer.get(0).question)
            answerET.setText(getUserInfoMsgArrayList.get(0).user_answer.get(0).answer)

            questionOneTV.setText(getUserInfoMsgArrayList.get(0).user_answer.get(1).question)
            answerOneET.setText(getUserInfoMsgArrayList.get(0).user_answer.get(1).answer)

            questionTwoTV.setText(getUserInfoMsgArrayList.get(0).user_answer.get(2).question)
            answerTwoET.setText(getUserInfoMsgArrayList.get(0).user_answer.get(2).answer)

            questionThreeTV.setText(getUserInfoMsgArrayList.get(0).user_answer.get(3).question)
            answerThreeET.setText(getUserInfoMsgArrayList.get(0).user_answer.get(3).answer)

            aboutET.setText(getUserInfoMsgArrayList.get(0).about_me)
            dobTV.setText(getUserInfoMsgArrayList.get(0).birthday)
            if (getUserInfoMsgArrayList.get(0).gender.equals("male")) {
                maleRB.isChecked = true
                femaleRB.isChecked = false
                gender = "1"
            } else {
                if (!getUserInfoMsgArrayList.get(0).gender.isEmpty()) {
                    maleRB.isChecked = false
                    femaleRB.isChecked = true
                    gender = "2"
                }
            }
        }
    }

    private fun init() {
        hitQuestionListApi()
        calender = Calendar.getInstance()

        val c = Calendar.getInstance().time
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MMM-yyyy")
        val formattedDate = df.format(c)
        baseActivity!!.log("DateTest>>>>>" + formattedDate)
        BottomSheetImageCallBack.getInstance(baseActivity).setImageCallBackListener(this)
//        profileRV.layoutManager = GridLayoutManager(baseActivity!!, 3, RecyclerView.VERTICAL, false)


        questionTV.setOnClickListener(this)
        questionOneTV.setOnClickListener(this)
        questionTwoTV.setOnClickListener(this)
        dobTV.setOnClickListener(this)
        doneTV.setOnClickListener(this)

        addOneIV.setOnClickListener(this)
        addTwoIV.setOnClickListener(this)
        addThreeIV.setOnClickListener(this)
        addFourIV.setOnClickListener(this)
        addFiveIV.setOnClickListener(this)
        addSixIV.setOnClickListener(this)

        radioGroupRG.setOnCheckedChangeListener { group, checkedId ->
            val checkedRadioButton = group.findViewById(checkedId) as RadioButton
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                var genderType = checkedRadioButton.text.toString().trim()
                if (genderType.equals("Male")) {
                    gender = "1"
                } else {
                    gender = "2"
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            questionTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHide) {
                    isHide = true
                    answerET.visibility = View.VISIBLE
                    questionTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHide = false
                    answerET.visibility = View.GONE
                    questionTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
            questionOneTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHideOne) {
                    isHideOne = true
                    answerOneET.visibility = View.VISIBLE
                    questionOneTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideOne = false
                    answerOneET.visibility = View.GONE
                    questionOneTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
            questionTwoTV -> {
                TransitionManager.beginDelayedTransition(headerLL, AutoTransition())
                if (!isHideTwo) {
                    isHideTwo = true
                    answerTwoET.visibility = View.VISIBLE
                    questionTwoTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideTwo = false
                    answerTwoET.visibility = View.GONE
                    questionTwoTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.add_shape,
                        0
                    )
                }
            }
            dobTV -> {
                getDate()
            }

            doneTV -> {
                hitEditProfileApi()
            }
            addOneIV -> {
                requestNumber = ONE

                var bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    bottomSheetFragment.getTag()
                )
            }
            addTwoIV -> {
                requestNumber = TWO

                var bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    bottomSheetFragment.getTag()
                )
            }
            addThreeIV -> {
                requestNumber = THREE

                var bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    bottomSheetFragment.getTag()
                )
            }
            addFourIV -> {
                requestNumber = FOUR
                var bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    bottomSheetFragment.getTag()
                )
            }
            addFiveIV -> {
                requestNumber = FIVE
                var bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    bottomSheetFragment.getTag()
                )
            }
            addSixIV -> {
                requestNumber = SIX
                var bottomSheetFragment = BottomSheetFragment()
                bottomSheetFragment.show(
                    baseActivity!!.getSupportFragmentManager(),
                    bottomSheetFragment.getTag()
                )
            }
        }
    }

    private fun getDate() {
        var calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog = DatePickerDialog(
            baseActivity!!,
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

                dobTV.setText((month + 1).toString() + "/" + day.toString() + "/" + year.toString())
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.getDatePicker().maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    override fun onAddImageListener(position: Int) {
        var bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(
            baseActivity!!.getSupportFragmentManager(),
            bottomSheetFragment.getTag()
        )
    }

    override fun onPause() {
        super.onPause()
        baseActivity!!.store.setBoolean("isTrue", false)
    }

    override fun onImageCallBackCallBack(file: File?, filePath: String?, uri: Uri?) {
        baseActivity?.showMessage(uri.toString())
        profileImagesUriArrayList.add(uri!!.toString())
//        updateImageUriArrayList.add(uri)
        editProfileAdapter = EditProfileAdapter(baseActivity!!, this, profileImagesUriArrayList)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(editProfileAdapter)
        val touchHelper = ItemTouchHelper(callback)
//        touchHelper.attachToRecyclerView(profileRV)
//        profileRV.adapter = editProfileAdapter

        when (requestNumber) {
            ONE -> {
                imageOne = uri
                profileOneCIV.setImageURI(uri)
            }
            TWO -> {
                imageTwo = uri
                profileTwoCIV.setImageURI(uri)
            }
            THREE -> {
                imageThree = uri
                profileThreeCIV.setImageURI(uri)
            }
            FOUR -> {
                imageFour = uri
                profileFourCIV.setImageURI(uri)
            }
            FIVE -> {
                imageFive = uri
                profileFiveCIV.setImageURI(uri)

            }
            SIX -> {
                imageSix = uri
                profileSixCIV.setImageURI(uri)
            }
        }

    }

    private fun hitEditProfileApi() {
        var msg = baseActivity!!.getProfileData()
        var selectGender: String = ""
        if (gender.equals("1")) {
            selectGender = "male"
        } else if (gender.equals("2")) {
            selectGender = "female"
        } else {
            baseActivity!!.showMessage("Please select your gender")
            return
        }
        var aboutUs = aboutET.text.toString().trim()
        var dob = dobTV.text.toString().trim()
        var questionOne = questionTV.text.toString().trim()
        var questionTwo = questionOneTV.text.toString().trim()
        var questionThree = questionTwoTV.text.toString().trim()
        var questionFour = questionThreeTV.text.toString().trim()

        var questionDataArrayList = ArrayList<QuestionData>()
        for (i in 0 until 3) {
            when (i) {
                0 -> {
                    var questionData = QuestionData("", 0)
                    questionData.ques_id = questionList.get(i).ques_id.toInt()
                    questionData.answer = answerET.text.toString().trim()
                    questionDataArrayList.add(questionData)
                }
                1 -> {
                    var questionData = QuestionData("", 0)
                    questionData.ques_id = questionList.get(i).ques_id.toInt()
                    questionData.answer = answerOneET.text.toString().trim()
                    questionDataArrayList.add(questionData)
                }
                2 -> {
                    var questionData = QuestionData("", 0)
                    questionData.ques_id = questionList.get(i).ques_id.toInt()
                    questionData.answer = answerTwoET.text.toString().trim()
                    questionDataArrayList.add(questionData)
                }
                3 -> {
                    var questionData = QuestionData("", 0)
                    questionData.ques_id = questionList.get(i).ques_id.toInt()
                    questionData.answer = answerThreeET.text.toString().trim()
                    questionDataArrayList.add(questionData)
                }
            }
        }
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.editProfileApi(
                EditProfileEntity(
                    aboutUs, dob, selectGender, questionDataArrayList, msg.user_id.toString()
                )
            )
            call.enqueue(object : Callback<EditProfileWrapper> {
                override fun onResponse(
                    call: Call<EditProfileWrapper>?,
                    response: Response<EditProfileWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var editProfileWrapper = response?.body()

                    if (editProfileWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        hitProfileImageApi()
                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<EditProfileWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.log(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun hitQuestionListApi() {
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")
            val call = apiInterface.questionListApi(
                UserIdEntity(0)
            )
            call.enqueue(object : Callback<QuestionListWrapper> {
                override fun onResponse(
                    call: Call<QuestionListWrapper>?,
                    response: Response<QuestionListWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var questionListWrapper = response?.body()

                    if (questionListWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        questionList.addAll(questionListWrapper!!.msg)
                        setQuestion(questionListWrapper!!.msg)

                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<QuestionListWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.showMessage(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun setQuestion(questionList: List<QuestionListMsg>) {
        questionTV.setText(questionList.get(0).question)
        questionOneTV.setText(questionList.get(1).question)
        questionTwoTV.setText(questionList.get(2).question)
        questionThreeTV.setText(questionList.get(3).question)

    }

    private fun hitProfileImageApi() {
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val call = editImage()

            call.enqueue(object : Callback<EditProfileWrapper> {
                override fun onResponse(
                    call: Call<EditProfileWrapper>?,
                    response: Response<EditProfileWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var editProfileWrapper = response?.body()

                    if (editProfileWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        baseActivity?.supportFragmentManager?.popBackStack()


                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<EditProfileWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.showMessage(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun editImage(): Call<EditProfileWrapper> {
        val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "")

        var msg = baseActivity!!.getProfileData()
        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        var image4: MultipartBody.Part? = null
        var image5: MultipartBody.Part? = null
        var image6: MultipartBody.Part? = null

        var user_id = RequestBody.create(
            okhttp3.MultipartBody.FORM, msg.user_id.toString()
        )

        if (imageOne != null) {
            var file = FileUtils.getFile(baseActivity, imageOne)
            var requestFile1 = RequestBody.create(MediaType.parse("image/*"), file)
            image1 =
                MultipartBody.Part.createFormData(
                    "image1",
                    file.getName(),
                    requestFile1
                )
        }
        if (imageTwo != null) {
            var file1 = FileUtils.getFile(baseActivity, imageTwo)
            var requestFile2 = RequestBody.create(MediaType.parse("image/*"), file1)
            image2 =
                MultipartBody.Part.createFormData(
                    "image2",
                    file1.getName(),
                    requestFile2
                )
        }
        if (imageThree != null) {
            var file2 = FileUtils.getFile(baseActivity, imageThree)
            var requestFile3 = RequestBody.create(MediaType.parse("image/*"), file2)
            image3 =
                MultipartBody.Part.createFormData(
                    "image3",
                    file2.getName(),
                    requestFile3
                )
        }

        if (imageFour != null) {
            var file3 = FileUtils.getFile(baseActivity, imageFour)
            var requestFile4 = RequestBody.create(MediaType.parse("image/*"), file3)
            image4 =
                MultipartBody.Part.createFormData(
                    "image4",
                    file3.getName(),
                    requestFile4
                )
        }
        if (imageFive != null) {
            var file4 = FileUtils.getFile(baseActivity, imageFive)
            var requestFile5 = RequestBody.create(MediaType.parse("image/*"), file4)
            image5 =
                MultipartBody.Part.createFormData(
                    "image5",
                    file4.getName(),
                    requestFile5
                )
        }
        if (imageSix != null) {
            var file5 = FileUtils.getFile(baseActivity, imageSix)
            var requestFile6 = RequestBody.create(MediaType.parse("image/*"), file5)
            image6 =
                MultipartBody.Part.createFormData(
                    "image6",
                    file5.getName(),
                    requestFile6
                )
        }


        return apiInterface.editImageApi(
            user_id,
            image1!!,
            image2!!,
            image3!!,
            image4!!,
            image5!!,
            image6!!
        )
    }

    private fun hitGetUserInfoApi() {
        var msg = baseActivity!!.getProfileData()
        baseActivity?.showLoading()
        baseActivity?.hideSoftKeyBoard()
        if (baseActivity?.isNetworkConnected()!!) {
            val apiInterface = ServiceGenerator.createService(ApiInterface::class.java, "abcTest")
            val call = apiInterface.getUserInfoApi(
                UserIdEntity(msg.user_id)
            )
            call.enqueue(object : Callback<GetUserInfoWrapper> {
                override fun onResponse(
                    call: Call<GetUserInfoWrapper>?,
                    response: Response<GetUserInfoWrapper>?
                ) {
                    baseActivity?.hideLoading()

                    var getUserInfoWrapper = response?.body()

                    if (getUserInfoWrapper?.code.equals(AppConstants.STATUS_OK.toString())) {
                        getUserInfoMsgArrayList.add(getUserInfoWrapper!!.msg[0])
                        setUserData(getUserInfoWrapper!!.msg[0])

                    } else {
//                        baseActivity?.showMessage(signUpWrapper!!.message)
                    }
                }

                override fun onFailure(call: Call<GetUserInfoWrapper>?, t: Throwable?) {
                    baseActivity?.hideLoading()
                    baseActivity?.showMessage(t!!.localizedMessage)
                }
            })
        } else {
            baseActivity?.hideLoading()
            baseActivity?.showMessage(getString(R.string.no_int_connection))
        }

    }

    private fun setUserData(getUserInfoMsg: GetUserInfoMsg) {
        profileImagesUriArrayList.clear()
        aboutET.setText(getUserInfoMsg.about_me)
        if (getUserInfoMsgArrayList.get(0).gender.equals("male")) {
            maleRB.isChecked = true
            femaleRB.isChecked = false
            gender = "1"
        } else {
            if (!getUserInfoMsgArrayList.get(0).gender.isEmpty()) {
                maleRB.isChecked = false
                femaleRB.isChecked = true
                gender = "2"
            }
        }
        dobTV.setText(getUserInfoMsg.birthday)

        questionTV.setText(getUserInfoMsg.user_answer[0].question)
        answerET.setText(getUserInfoMsg.user_answer[0].answer.toString())

        questionOneTV.setText(getUserInfoMsg.user_answer[1].question)
        answerOneET.setText(getUserInfoMsg.user_answer[1].answer.toString())

        questionTwoTV.setText(getUserInfoMsg.user_answer[2].question)
        answerTwoET.setText(getUserInfoMsg.user_answer[2].answer.toString())

        questionThreeTV.setText(getUserInfoMsg.user_answer[3].question)
        answerThreeET.setText(getUserInfoMsg.user_answer[3].answer.toString())

        if (getUserInfoMsg.image1 != null && !getUserInfoMsg.image1.isEmpty()) {
            Glide.with(baseActivity!!).load(getUserInfoMsg.image1).into(profileOneCIV)
        } else {
            profileOneCIV.setImageResource(R.drawable.ic_default)
        }
        if (getUserInfoMsg.image2 != null && !getUserInfoMsg.image2.isEmpty()) {
            Glide.with(baseActivity!!).load(getUserInfoMsg.image2).into(profileOneCIV)
        } else {
            profileOneCIV.setImageResource(R.drawable.ic_default)
        }
        if (getUserInfoMsg.image3 != null && !getUserInfoMsg.image3.isEmpty()) {
            Glide.with(baseActivity!!).load(getUserInfoMsg.image3).into(profileTwoCIV)
        } else {
            profileTwoCIV.setImageResource(R.drawable.ic_default)
        }
        if (getUserInfoMsg.image4 != null && !getUserInfoMsg.image4.isEmpty()) {
            Glide.with(baseActivity!!).load(getUserInfoMsg.image4).into(profileFourCIV)
        } else {
            profileFourCIV.setImageResource(R.drawable.ic_default)
        }
        if (getUserInfoMsg.image5 != null && !getUserInfoMsg.image5.isEmpty()) {
            Glide.with(baseActivity!!).load(getUserInfoMsg.image5).into(profileFiveCIV)
        } else {
            profileFiveCIV.setImageResource(R.drawable.ic_default)
        }
        if (getUserInfoMsg.image6 != null && !getUserInfoMsg.image6.isEmpty()) {
            Glide.with(baseActivity!!).load(getUserInfoMsg.image6).into(profileSixCIV)
        } else {
            profileSixCIV.setImageResource(R.drawable.ic_default)
        }

//        profileImagesUriArrayList.add(getUserInfoMsg.image1)
//        profileImagesUriArrayList.add(getUserInfoMsg.image2)
//        profileImagesUriArrayList.add(getUserInfoMsg.image3)
//        profileImagesUriArrayList.add(getUserInfoMsg.image4)
//        profileImagesUriArrayList.add(getUserInfoMsg.image5)
//        profileImagesUriArrayList.add(getUserInfoMsg.image6)
//        editProfileAdapter = EditProfileAdapter(baseActivity!!, this, profileImagesUriArrayList)
//        val callback: ItemTouchHelper.Callback = ItemMoveCallback(editProfileAdapter)
//        val touchHelper = ItemTouchHelper(callback)
//        touchHelper.attachToRecyclerView(profileRV)
//        profileRV.adapter = editProfileAdapter
    }
}
