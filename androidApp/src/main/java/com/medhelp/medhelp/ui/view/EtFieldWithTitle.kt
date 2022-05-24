package com.medhelp.medhelp.ui.view

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.medhelp.medhelp.R


class EtFieldWithTitle : LinearLayout {
    //region constructor
    constructor(context: Context?) : super(context!!) {
        context?.let { init() }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        context?.let { init() }
        attrs?.let { initAttrs(it) }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        context?.let { init() }
        attrs?.let { initAttrs(it) }
    }
    //endregion

    lateinit var title : TextView
    lateinit var text : EditText
    fun init() {
        val mainView= inflate(context, R.layout.edit_text_field_with_title_dark, this)
        title=mainView.findViewById(R.id.title)
        text=mainView.findViewById(R.id.text)
    }

    private fun initAttrs(attrs: AttributeSet){
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.EdTextFieldWithTitleAttr, 0, 0)

        title.text = typedArray.getString(R.styleable.EdTextFieldWithTitleAttr_titleAttrEt)

        val promptAttr = typedArray.getString(R.styleable.EdTextFieldWithTitleAttr_promptAttrEt)
        if(promptAttr!=null && promptAttr.isNotEmpty()){
            setPromptTitle(promptAttr)
        }

        val textAttr = typedArray.getString(R.styleable.EdTextFieldWithTitleAttr_textAttrEt)
        if(textAttr!=null && textAttr.isNotEmpty()){
            setText(textAttr)
        }else{
            setText("")
        }

        val textMaxLengthSim=typedArray.getInt(R.styleable.EdTextFieldWithTitleAttr_maxLengthSim,-1)
        if(textMaxLengthSim!=null && textMaxLengthSim>-1){
            text.filters += InputFilter.LengthFilter(textMaxLengthSim)
        }
        val textLines=typedArray.getInt(R.styleable.EdTextFieldWithTitleAttr_myLines,-1)
        if(textLines!=null && textLines>-1){
            text.setLines(textLines)
        }


        val textInputTypeSim=typedArray.getString(R.styleable.EdTextFieldWithTitleAttr_inputTypeSimMy)
        if(textInputTypeSim!=null && textInputTypeSim.isNotEmpty()){
            when (textInputTypeSim) {
                "1" -> text.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                "2" -> {
                    text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                    text.isSingleLine=true
                }
                "3" -> {
                    text.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    text.isSingleLine=true
                    text.setKeyListener(DigitsKeyListener.getInstance("0123456789"))
                }
                "4" -> {
                    text.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                    text.isSingleLine=true
                    text.setKeyListener(DigitsKeyListener.getInstance("0123456789"))
                    text.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                }
                "10" -> {
                    text.setKeyListener(DigitsKeyListener.getInstance("0123456789"))
                    text.setInputType(InputType.TYPE_CLASS_NUMBER)
                    text.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI or EditorInfo.IME_ACTION_DONE)
                }
                "5" -> {
                    text.isCursorVisible = false
                    text.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                }
                "6" -> {
                    text.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                }
                "7" -> {
                    text.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                }
                "8" -> {
                    text.setInputType(InputType.TYPE_CLASS_TEXT)
                }
                "9" -> {
                    text.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    text.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI)
                }

            }

        }
    }

    private fun setText(str : String){
        text.setText(str)
    }
    private fun setPromptTitle(str : String){
        text.setHint(str)
    }
}