package wuliao.in.contact.view;

import java.util.Date;

import wuliao.in.contact.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyExpendListView extends ExpandableListView implements OnScrollListener {
	
	private int firstVisibleIndex;//listView��ҳ���е�һ������item��λ��
	private ImageView arrow;
	private View headView;
	private ProgressBar progressBar;
	private TextView title;
	private TextView last_update;
	private int headcontentWidth;
	private int headcontentHeight;
	
	private static final int PULL_TO_REFRESH = 0;
	private static final int RELEASE_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int DONE = 3;
	private int state;//��ǰ����ˢ�¿ؼ���״̬
	private OnRefreshListener refreshListener;//ˢ�¼�����
	private boolean isRefreshable;//��¼�Ƿ��ܹ�ˢ��
	private boolean isRecored;//��¼startY ��ʼλ��  ���������������У�ֻ��¼һ��
	private float startY;
	private boolean isBack;
	private Animation animation;
	private Animation receverAnimation;
	
	private static final int RATIO = 3;//ʵ�ʾ����� �����Ͼ���֮��ı���ֵ
	

	public MyExpendListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyExpendListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		// Ҫ��adapter��ʹ�� ���ڵ�ǰʹ�ûᱨ�� ��ָ��
		//View headView = View.inflate(context, R.layout.header, null);
		LayoutInflater inflater = LayoutInflater.from(context);
		headView = inflater.inflate(R.layout.header, null);
		
		arrow = (ImageView) headView.findViewById(R.id.arrow);
		progressBar = (ProgressBar) headView.findViewById(R.id.progressBar);
		title = (TextView) headView.findViewById(R.id.title);
		last_update = (TextView) headView.findViewById(R.id.last_update);
		
		arrow.setMinimumWidth(70);
		arrow.setMinimumHeight(50);
		measureView(headView);
		headcontentWidth = headView.getMeasuredWidth();
		headcontentHeight = headView.getMeasuredHeight();
		
		//����headView ������ϱ߾�ľ���
		headView.setPadding(0, -1 * headcontentHeight, 0, 0);
		
		headView.invalidate();//headView �ػ�
		
		addHeaderView(headView);
		
		setOnScrollListener(this);
		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(250);
		animation.setFillAfter(true);
		animation.setInterpolator(new LinearInterpolator());//���ö����ٶ�
		/*
			Interpolator �����˶����ı仯�ٶȣ�����ʵ�����١������١������١��޹������ٵȣ�
			
			AccelerateDecelerateInterpolator���ӳټ��٣��ڶ���ִ�е��м��ʱ���ִ�и���Ч��
			AccelerateInterpolator, ��ʹ������(float)�Ĳ��������ٶȡ�
			LinearInterpolator��ƽ�Ȳ����
			DecelerateInterpolator�����м����,��ͷ��
			CycleInterpolator�������˶���Ч��Ҫ����float�͵Ĳ�����
		 */
		
		receverAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		receverAnimation.setDuration(200);
		receverAnimation.setFillAfter(true);
		receverAnimation.setInterpolator(new LinearInterpolator());//���ö����ٶ�  ����
		
		state = DONE;
		isRefreshable = false;
	}

	
	//����Headview ���
	private void measureView(View child) {
		// TODO Auto-generated method stub
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		if(lp == null){
			lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		
		int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
		int childMeasureHeight;
		if(lp.height > 0){
			childMeasureHeight = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);//�ʺϡ�ƥ��
		} else {
			childMeasureHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);//δָ��
		}
		child.measure(childMeasureWidth, childMeasureHeight);
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		firstVisibleIndex = firstVisibleItem;
	}
	
	public interface OnRefreshListener{
		abstract void onRefresh();
	}
	
	//�ṩһ��������ʵ�ˢ�·���
	public void setOnRefreshListener(OnRefreshListener listener){
		refreshListener = listener;
		isRefreshable = true;
		
	}

	//����ˢ����ɺ�ִ�з���  
	//����ˢ�� ģʽ�ĸı�   ʱ��ĸ���
	public void onRefreshComplete() {
		// TODO Auto-generated method stub
		state = DONE;
		changeHeadViewOfState();
		
		last_update.setText("������£� " + new Date().toLocaleString());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(isRefreshable){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = event.getY();
				if(firstVisibleIndex == 0 && !isRecored){
					isRecored = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				float tempY = event.getY();
//				if(firstVisibleIndex == 0 && !isRecored){
//					startY = tempY;
//					
//					isRecored = true;
//				}
				
				if(state != REFRESHING){
					if(state == PULL_TO_REFRESH){
						if((tempY - startY) / RATIO > headcontentHeight && tempY - startY > 0){
							// ����ˢ�� --�� �ɿ�ˢ��
							state = RELEASE_TO_REFRESH;
							changeHeadViewOfState();
							
						} else if(tempY - startY <= 0){
							// ����ˢ�� --�� �ص� ˢ�����
							state = DONE;
							changeHeadViewOfState();
						}
					}
					
					if(state == RELEASE_TO_REFRESH){
						
						if((tempY - startY) / RATIO < headcontentHeight && tempY - startY > 0){
							//�ɿ�ˢ�� --���ص�����ˢ��
							state = PULL_TO_REFRESH;
							isBack = true;// ���ɿ�ˢ�� �ص�������ˢ��
							changeHeadViewOfState();
						} else if(tempY - startY <= 0){
							// �ɿ�ˢ�� --�� �ص� ˢ�����
							state = DONE;
							
							changeHeadViewOfState();
						}
					}
					
					if(state == DONE){
						if(tempY - startY > 0){
							// ˢ����� --�� ���� ����ˢ��
							state = PULL_TO_REFRESH;
							
							changeHeadViewOfState();	
						}
					}
					
					if(state == PULL_TO_REFRESH || state == RELEASE_TO_REFRESH){
						headView.setPadding(0, (int) ((tempY - startY) / RATIO - headcontentHeight), 0, 0);
					}
				}
				
				
				break;
			case MotionEvent.ACTION_UP:
				if(state != REFRESHING){
					if(state == DONE){
						//����Ҫ����
					}
					
					if(state == PULL_TO_REFRESH){
						state = DONE;
						
						changeHeadViewOfState();	
					}
					
					if(state == RELEASE_TO_REFRESH){
						state = REFRESHING;

						changeHeadViewOfState();
						
						onRefresh();//ˢ��  �õ�����������
					}
				}
				break;
			}
			
			
			
			
			
		}
		return super.onTouchEvent(event);
	}

	//headview״̬�ı�
	private void changeHeadViewOfState() {
		// TODO Auto-generated method stub
		switch (state) {
		case PULL_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);
			
			title.setText("����ˢ��");
			arrow.clearAnimation();
			
			if(isBack){//���ɿ�ˢ�� �ص� ����ˢ��
				arrow.startAnimation(animation);
				isBack = false;
			}
			break;
		case RELEASE_TO_REFRESH:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);
			
			title.setText("�ɿ�ˢ��");
			arrow.clearAnimation();
			arrow.startAnimation(receverAnimation);
			
			break;
		case REFRESHING:
			arrow.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);
			
			title.setText("����ˢ����...");
			arrow.clearAnimation();
			
			headView.setPadding(0, 0, 0, 0);
			break;
		case DONE:
			arrow.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			title.setVisibility(View.VISIBLE);
			last_update.setVisibility(View.VISIBLE);
			
			title.setText("����ˢ��");
			arrow.clearAnimation();
			
			headView.setPadding(0, -1 * headcontentHeight, 0, 0);
			break;

		}
	}
	
	//ˢ��   �õ�������������
	private void onRefresh() {

		if(refreshListener != null){
			refreshListener.onRefresh();
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		last_update.setText("������£� " + new Date().toLocaleString());
		super.setAdapter(adapter);
	}
}
