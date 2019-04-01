package com.sahana.horizontalcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sahana.horizontalcalendarview.model.DateModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by SahanaB on 09/09/18.
 */
public class CustomHorizontalCalendar extends RelativeLayout {
    private RecyclerView mRecyclerView;
    private HorizontalDateAdapter mHorizontalDateAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView mLeftArrowImageView;
    private ImageView mRightArrowImageView;
    private TextView mLableTextView;
    private TextView mMonthAndDateTextView;
    private List<DateModel> mInputDates;
    private int mCenterChildPosition = -1;
    private LayoutClickListener mLayoutClickListener;
    private OnHorizontalDateSelectListener mOnHorizontalDateSelectListener;
    private int mNoOfDays;
    private String mLabel;
    protected int mBgResourceId;
    protected int mSelectedBgResourceId;
    protected int mTextColorResourceId;
    protected int mSelectedTextColorResourceId;
    private int mScroolSpeed;

    public void setOnDateSelectListener(OnHorizontalDateSelectListener onHorizontalDateSelectListener) {
        mOnHorizontalDateSelectListener = onHorizontalDateSelectListener;
    }

    protected void setLayoutClickListener(LayoutClickListener layoutClickListener) {
        mLayoutClickListener = layoutClickListener;
        if (mHorizontalDateAdapter != null)
            mHorizontalDateAdapter.setOnHorizontalDateSelectListener(new OnHorizontalDateSelectListener() {
                @Override
                public void onDateClick(DateModel dateModel) {
                    setMonthAndYear(dateModel);
                    if (mOnHorizontalDateSelectListener != null)
                        mOnHorizontalDateSelectListener.onDateClick(dateModel);
                }
            });
    }

    public CustomHorizontalCalendar(Context context) {
        super(context);
        init(context, null);
    }

    public CustomHorizontalCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomHorizontalCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_horizontal_calendar, this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLeftArrowImageView = findViewById(R.id.leftArrow);
        mRightArrowImageView = findViewById(R.id.rightArrow);
        mLableTextView = findViewById(R.id.labelTextView);
        mMonthAndDateTextView = findViewById(R.id.monthAndDateTextView);
        if (attrs == null) return;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomHorizontalCalendar, 0, 0);
        mScroolSpeed = typedArray.getInteger(R.styleable.CustomHorizontalCalendar_setScrollSpeed, 30);
        mNoOfDays = typedArray.getInteger(R.styleable.CustomHorizontalCalendar_numOfDays, 60);
        mLabel = typedArray.getString(R.styleable.CustomHorizontalCalendar_setLabel);
        mBgResourceId = typedArray.getResourceId(R.styleable.CustomHorizontalCalendar_setBgColor, R.drawable.rect_dark_gray);
        mTextColorResourceId = typedArray.getResourceId(R.styleable.CustomHorizontalCalendar_setTextColor, R.color.dark_gray);
        mSelectedBgResourceId = typedArray.getResourceId(R.styleable.CustomHorizontalCalendar_setSelectedBgColor, R.drawable.rect_sky_blue);
        mSelectedTextColorResourceId = typedArray.getResourceId(R.styleable.CustomHorizontalCalendar_setSelectedTextColor, R.color.white);
        setCalender(mNoOfDays);
        setLabel(mLabel);
        if (typedArray.hasValue(R.styleable.CustomHorizontalCalendar_setLabelColor))
            mLableTextView.setTextColor(getResources().getColor(typedArray.getResourceId(R.styleable.CustomHorizontalCalendar_setLabelColor, R.color.dark_gray)));
        mMonthAndDateTextView.setTextColor(getResources().getColor(typedArray.getResourceId(R.styleable.CustomHorizontalCalendar_setMonthColor, R.color.black)));
        if (typedArray.hasValue(R.styleable.CustomHorizontalCalendar_setLabelTextSize))
            mLableTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, typedArray.getInteger(R.styleable.CustomHorizontalCalendar_setLabelTextSize, 13));
        if (typedArray.hasValue(R.styleable.CustomHorizontalCalendar_setMonthTextSize))
            mMonthAndDateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, typedArray.getInteger(R.styleable.CustomHorizontalCalendar_setMonthTextSize, 15));
        if (typedArray.hasValue(R.styleable.CustomHorizontalCalendar_setLabelFontStyle))
            mLableTextView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), typedArray.getString(R.styleable.CustomHorizontalCalendar_setLabelFontStyle)));
        if (typedArray.hasValue(R.styleable.CustomHorizontalCalendar_setMonthFontStyle))
            mMonthAndDateTextView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), typedArray.getString(R.styleable.CustomHorizontalCalendar_setMonthFontStyle)));
    }

    private void setCalender(int noOfDays) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        mInputDates = new ArrayList<>();
        mInputDates.clear();

        for (int index = 0; index < noOfDays; index++) {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, index);
            DateModel dateModel = new DateModel();
            dateModel.day = calendar.get(Calendar.DAY_OF_MONTH) + "";
            dateModel.dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            dateModel.month = getMonth(calendar.get(Calendar.MONTH));
            dateModel.year = calendar.get(Calendar.YEAR) + "";
            mInputDates.add(dateModel);
        }
        setData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setData() {
        if (mHorizontalDateAdapter == null)
            mHorizontalDateAdapter = new HorizontalDateAdapter();
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mHorizontalDateAdapter.setData(mInputDates, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mHorizontalDateAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        final SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setHorizontalScrollBarEnabled(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() != null) {
                    View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                    if (centerView != null)
                        mCenterChildPosition = recyclerView.getLayoutManager().getPosition(centerView);
                }
                if (mLayoutClickListener != null)
                    mLayoutClickListener.onLayoutClick(mCenterChildPosition);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() != null) {
                    View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                    if (centerView != null)
                        mCenterChildPosition = recyclerView.getLayoutManager().getPosition(centerView);
                }
                if (mLayoutClickListener != null)
                    mLayoutClickListener.onLayoutClick(mCenterChildPosition);

            }
        });
        mRightArrowImageView.setOnClickListener(new OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (mHorizontalDateAdapter.row_index == mLinearLayoutManager.findFirstVisibleItemPosition() || mHorizontalDateAdapter.row_index == mLinearLayoutManager.findFirstVisibleItemPosition() + 1 || mHorizontalDateAdapter.row_index == mLinearLayoutManager.findLastVisibleItemPosition() - 1) {
                    mCenterChildPosition = mHorizontalDateAdapter.row_index + 1;
                } else if (mHorizontalDateAdapter.row_index == mLinearLayoutManager.findLastVisibleItemPosition()) {
                    if (((LinearLayoutManager) mRecyclerView.getLayoutManager()) != null)
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mHorizontalDateAdapter.row_index + 1, 0);
                } else {
                    if (mCenterChildPosition >= 2)
                        mRecyclerView.scrollToPosition(mLinearLayoutManager.findLastVisibleItemPosition() + 1);
                    if (mCenterChildPosition != (mHorizontalDateAdapter.getItemCount() - 1))
                        mCenterChildPosition = mCenterChildPosition + 1;
                }
                if (mLayoutClickListener != null)
                    mLayoutClickListener.onLayoutClick(mCenterChildPosition);
                mHorizontalDateAdapter.notifyItemChanged(mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);

            }
        });
        mLeftArrowImageView.setOnClickListener(new OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (mHorizontalDateAdapter.row_index == mLinearLayoutManager.findLastVisibleItemPosition() || mHorizontalDateAdapter.row_index == mLinearLayoutManager.findLastVisibleItemPosition() - 1 || mHorizontalDateAdapter.row_index == mLinearLayoutManager.findFirstVisibleItemPosition() + 1) {
                    mCenterChildPosition = mHorizontalDateAdapter.row_index - 1;
                } else if (mHorizontalDateAdapter.row_index == mLinearLayoutManager.findFirstVisibleItemPosition()) {
                    if (((LinearLayoutManager) mRecyclerView.getLayoutManager()) != null)
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mHorizontalDateAdapter.row_index - 3, 0);
                } else {
                    if (mCenterChildPosition <= (mHorizontalDateAdapter.getItemCount() - 3))
                        mRecyclerView.scrollToPosition(mLinearLayoutManager.findFirstVisibleItemPosition() - 1);
                    if (mCenterChildPosition != 0)
                        mCenterChildPosition = mCenterChildPosition - 1;
                }
                if (mLayoutClickListener != null)
                    mLayoutClickListener.onLayoutClick(mCenterChildPosition);
                mHorizontalDateAdapter.notifyItemChanged(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() - 1);


            }
        });
        mRightArrowImageView.setLongClickable(true);
        mLeftArrowImageView.setLongClickable(true);
        final Handler handler = new Handler();
        final Runnable longPressedLeft = new Runnable() {
            public void run() {
                mRecyclerView.scrollBy(-mScroolSpeed, 0);
                handler.post(this);
            }
        };
        mLeftArrowImageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handler.post(longPressedLeft);
                return false;
            }
        });
        mLeftArrowImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               /* if (event.getAction() == MotionEvent.ACTION_DOWN)
                 handler.postDelayed(longPressedLeft, 500);*/
                if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
                    handler.removeCallbacks(longPressedLeft);
                return false;
            }
        });

        final Runnable longPressedRight = new Runnable() {
            public void run() {
                mRecyclerView.scrollBy(mScroolSpeed, 0);
                handler.post(this);
            }
        };
        mRightArrowImageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                handler.post(longPressedRight);
                return false;
            }
        });
        mRightArrowImageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if (event.getAction() == MotionEvent.ACTION_DOWN)
                    handler.postDelayed(longPressedRight, 500);*/
                if ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_UP))
                    handler.removeCallbacks(longPressedRight);
                return false;
            }

        });

    }

    protected interface LayoutClickListener {
        void onLayoutClick(int position);
    }

    private String getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2:
                return "Mon";
            case 3:
                return "Tue";

            case 4:
                return "Wed";

            case 5:
                return "Thurs";

            case 6:
                return "Fri";

            case 7:
                return "Sat";

            case 1:
                return "Sun";

        }
        return "";
    }

    private static String getMonth(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    private void setMonthAndYear(DateModel dateModel) {
        if (dateModel != null)
            mMonthAndDateTextView.setText(dateModel.month + " " + dateModel.year);
    }

    public void setLabel(String label) {
        mLableTextView.setText(label);
    }
}


