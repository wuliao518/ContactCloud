package wuliao.in.contact.view;

import wuliao.in.contact.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class ClearEditText extends EditText implements  OnFocusChangeListener, TextWatcher {
	private Drawable mDrawable;
	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	private void initView() {
		mDrawable=getCompoundDrawables()[2];
		if(mDrawable==null){
			mDrawable=getResources().getDrawable(R.drawable.edit_clear);
			mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());
		}
		setDrawableVisible(false);
		addTextChangedListener(this);
		setOnFocusChangeListener(this);
	}

	private void setDrawableVisible(boolean visible) {
		Drawable right = visible ? mDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {	
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		 if (hasFocus) { 
			 	setDrawableVisible(getText().length() > 0); 
	        } else { 
	        	setDrawableVisible(false); 
	        }
	}
	 @Override 
    public void onTextChanged(CharSequence s, int start, int count, 
            int after) { 
		 setDrawableVisible(s.length() > 0);
    }
	public boolean onTouchEvent(MotionEvent event) { 
        if (getCompoundDrawables()[2] != null) { 
            if (event.getAction() == MotionEvent.ACTION_UP) { 
            	boolean touchable=event.getX()>(getWidth()-getTotalPaddingRight())
            			&&(event.getX() < ((getWidth() - getPaddingRight())));
            	if(touchable){
            		this.setText("");
            	}
            } 
        } 
 
        return super.onTouchEvent(event); 
    } 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
