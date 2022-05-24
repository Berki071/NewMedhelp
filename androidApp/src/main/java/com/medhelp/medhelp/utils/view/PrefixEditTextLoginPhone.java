package com.medhelp.medhelp.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

public class PrefixEditTextLoginPhone extends AppCompatEditText {
    float originalLeftPadding = -1;

    public PrefixEditTextLoginPhone(Context context) {
        super(context);
    }

    public PrefixEditTextLoginPhone(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefixEditTextLoginPhone(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculatePrefix();
    }

    private void calculatePrefix() {
        if (originalLeftPadding == -1) {
            String prefix = "+7 ";
            float[] widths = new float[prefix.length()];
            getPaint().getTextWidths(prefix, widths);
            for (float w : widths) {
                textWidth += w;
            }
            originalLeftPadding = getCompoundPaddingLeft();
        }
        else
        {
            if(boo)
            {
                setPadding((int) (textWidth + originalLeftPadding), getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
            else
            {
                String s=getText().toString();
                if(s.length()>0)
                {
                    setPadding((int) (textWidth + originalLeftPadding),getPaddingTop(),  getPaddingRight(), getPaddingBottom());
                }
                else
                {
                    setPadding((int) (originalLeftPadding),getPaddingTop(), getPaddingRight(),  getPaddingBottom());
                }

            }
        }
    }

    Canvas canvas;
    Boolean boo=true;
    float textWidth = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas=canvas;

        if(boo)
        {
            //String prefix = (String) getTag();
            canvas.drawText("+7 ", originalLeftPadding, getLineBounds(0, null), getPaint());
        }
        else
        {
            String s=getText().toString();

            if(s.length()>0)
            {
                canvas.drawText("+7 ", originalLeftPadding, getLineBounds(0, null), getPaint());
            }
            else
            {
                canvas.drawText(" ", 0, getLineBounds(0, null), getPaint());
            }
        }

        calculatePrefix();
    }

    public void setVisibleDraw(Boolean boo)
    {
        this.boo=boo;
    }
}