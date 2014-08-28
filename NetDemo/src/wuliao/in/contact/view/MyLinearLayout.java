package wuliao.in.contact.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	 @Override  
	    public boolean onTouchEvent(MotionEvent event) {  
		 switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("down");
				break;
			case MotionEvent.ACTION_MOVE:
				System.out.println("move");
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("up");
				break;
			default:
				break;
			}
			return true;
	    }  

}
