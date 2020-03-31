package com.how_about_now.app.fragment.home_phase

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.how_about_now.app.R
import com.how_about_now.app.adapter.EditProfileAdapter
import com.how_about_now.app.data.profile_data.*
import com.how_about_now.app.fragment.BaseFragment
import com.how_about_now.app.fragment.BottomSheetFragment
import com.how_about_now.app.retrofit.ApiInterface
import com.how_about_now.app.retrofit.ServiceGenerator
import com.how_about_now.app.utils.AppConstants
import com.how_about_now.app.utils.BottomSheetImageCallBack
import com.how_about_now.app.utils.ItemMoveCallback
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.answerOneTV
import kotlinx.android.synthetic.main.fragment_profile.answerTV
import kotlinx.android.synthetic.main.fragment_profile.answerTwoTV
import kotlinx.android.synthetic.main.fragment_profile.dobTV
import kotlinx.android.synthetic.main.fragment_profile.headerLL
import kotlinx.android.synthetic.main.fragment_profile.profileRV
import kotlinx.android.synthetic.main.fragment_profile.questionOneTV
import kotlinx.android.synthetic.main.fragment_profile.questionTV
import kotlinx.android.synthetic.main.fragment_profile.questionTwoTV
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

    private var questionList = ArrayList<QuestionListMsg>()
    private var gender: String? = ""
    private var isHide: Boolean = true
    private var isHideOne: Boolean = true
    private var isHideTwo: Boolean = true
    private var profileImagesUriArrayList = ArrayList<String>()
    private var editProfileAdapter: EditProfileAdapter? = null
    private var calender: Calendar? = null
    private var getUserInfoMsgArrayList = ArrayList<GetUserInfoMsg>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            getUserInfoMsgArrayList =
                arguments!!.getParcelableArrayList<GetUserInfoMsg>("profileData")!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileData()
        init()
    }

    private fun getProfileData() {
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
        } else {
            maleRB.isChecked = false
            femaleRB.isChecked = true
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
        profileRV.layoutManager = GridLayoutManager(baseActivity!!, 3, RecyclerView.VERTICAL, false)

        editProfileAdapter = EditProfileAdapter(baseActivity!!, this, profileImagesUriArrayList)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(editProfileAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(profileRV)
        profileRV.adapter = editProfileAdapter

        questionTV.setOnClickListener(this)
        questionOneTV.setOnClickListener(this)
        questionTwoTV.setOnClickListener(this)
        dobTV.setOnClickListener(this)
        doneTV.setOnClickListener(this)

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
                    answerTV.visibility = View.VISIBLE
                    questionTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHide = false
                    answerTV.visibility = View.GONE
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
                    answerOneTV.visibility = View.VISIBLE
                    questionOneTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideOne = false
                    answerOneTV.visibility = View.GONE
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
                    answerTwoTV.visibility = View.VISIBLE
                    questionTwoTV.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.hide_shape,
                        0
                    )
                } else {
                    isHideTwo = false
                    answerTwoTV.visibility = View.GONE
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

    override fun onImageCallBackCallBack(file: File?, filePath: String?, uri: Uri?) {
        baseActivity?.showMessage(uri.toString())
        profileImagesUriArrayList.add(uri!!.toString())
        editProfileAdapter = EditProfileAdapter(baseActivity!!, this, profileImagesUriArrayList)
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(editProfileAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(profileRV)
        profileRV.adapter = editProfileAdapter
    }

    private fun hitEditProfileApi() {
        var msg = baseActivity!!.getProfileData()
        var selectGender: String = ""
        if (gender.equals("1")) {
            selectGender = "male"
        } else {
            selectGender = "female"
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


}
