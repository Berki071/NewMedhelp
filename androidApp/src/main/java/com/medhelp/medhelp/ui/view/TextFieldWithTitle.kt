package com.medhelp.medhelp.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.medhelp.medhelp.R

class TextFieldWithTitle : LinearLayout {
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
    lateinit var text : TextView
    fun init() {
        val mainView= inflate(context, R.layout.text_field_with_title, this)
        title=mainView.findViewById(R.id.title)
        text=mainView.findViewById(R.id.text)
    }

    private fun initAttrs(attrs: AttributeSet){
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TextFieldWithTitleAttr, 0, 0)

        title.text = typedArray.getString(R.styleable.TextFieldWithTitleAttr_titleAttr)

        val textAttr = typedArray.getString(R.styleable.TextFieldWithTitleAttr_textAttr)
        if(textAttr!=null && textAttr.isNotEmpty()){
            setText(textAttr)
        }else{
            val promptAttr = typedArray.getString(R.styleable.TextFieldWithTitleAttr_promptAttr)
            if(promptAttr!=null && promptAttr.isNotEmpty()){
                setPromptTitle(promptAttr)
            }
            else
                setText("")
        }
    }

    private fun setText(str : String){
       // text.setTextColor(resources.getColor( R.color.white))
        text.text=str
    }
    private fun setPromptTitle(str : String){
     //  text.setTextColor(resources.getColor( R.color.white10))
        text.text=str
    }
}