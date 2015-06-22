package com.yimeng.baolingtong.menu;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yimeng.baolingtong.R;
import com.yimeng.baolingtong.menu.interfaces.OnItemClickListener;
import com.yimeng.baolingtong.menu.interfaces.OnItemLongClickListener;
import com.yimeng.baolingtong.menu.interfaces.OnMenuItemClickListener;
import com.yimeng.baolingtong.menu.interfaces.OnMenuItemLongClickListener;

@SuppressLint("NewApi")
public class ContextMenuDialogFragment extends DialogFragment implements OnItemClickListener, OnItemLongClickListener {

    public static final String TAG = ContextMenuDialogFragment.class.getSimpleName();
    private static final String BUNDLE_MENU_PARAMS = "BUNDLE_MENU_PARAMS";

    private LinearLayout mWrapperButtons;
    private LinearLayout mWrapperText;
    private MenuAdapter mDropDownMenuAdapter;
    private OnMenuItemClickListener mItemClickListener;
    private OnMenuItemLongClickListener mItemLongClickListener;
    private MenuParams mMenuParams;

    @Deprecated
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        return newInstance(params);
    }

    @Deprecated
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects, int animationDelay) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        params.setAnimationDelay(animationDelay);
        return newInstance(params);
    }

    @Deprecated
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects, int animationDelay, int animationDuration) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        params.setAnimationDelay(animationDelay);
        params.setAnimationDuration(animationDuration);
        return newInstance(params);
    }

    @Deprecated
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ContextMenuDialogFragment newInstance(int actionBarSize, List<MenuObject> menuObjects,
                                                        int animationDelay, int animationDuration,
                                                        boolean fitsSystemWindow, boolean clipToPadding) {
        MenuParams params = new MenuParams();
        params.setActionBarSize(actionBarSize);
        params.setMenuObjects(menuObjects);
        params.setAnimationDelay(animationDelay);
        params.setAnimationDuration(animationDuration);
        params.setFitsSystemWindow(fitsSystemWindow);
        params.setClipToPadding(clipToPadding);
        return newInstance(params);
    }

    public static ContextMenuDialogFragment newInstance(MenuParams menuParams) {
        ContextMenuDialogFragment fragment = new ContextMenuDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_MENU_PARAMS, menuParams);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mItemClickListener = (OnMenuItemClickListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, activity.getClass().getSimpleName() +
                    " should implement " + OnMenuItemClickListener.class.getSimpleName());
        }
        try {
            mItemLongClickListener = (OnMenuItemLongClickListener) activity;
        } catch (ClassCastException e) {
            Log.e(TAG, activity.getClass().getSimpleName() +
                    " should implement " + OnMenuItemLongClickListener.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MenuFragmentStyle);
        if (getArguments() != null) {
            mMenuParams = getArguments().getParcelable(BUNDLE_MENU_PARAMS);
        }
    }

    private int left, top, right, bottom;
    
    public void setPadding(int left, int top, int right, int bottom){
    	this.left = left;
    	this.top = top;
    	this.right = right;
    	this.bottom = bottom;
    }
    
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        
        View root = rootView.findViewById(R.id.root);
        root.setPadding(left, top, right, bottom);
        rootView.setFitsSystemWindows(mMenuParams.isFitsSystemWindow());
        ((ViewGroup) rootView).setClipToPadding(mMenuParams.isClipToPadding());

        initViews(rootView);
        
        initDropDownMenuAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDropDownMenuAdapter.menuToggle();
            }
        }, mMenuParams.getAnimationDelay());

        if (mMenuParams.isClosableOutside()) {
            rootView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });
        }
        return rootView;
    }
    
    private void initViews(View view) {
        mWrapperButtons = (LinearLayout) view.findViewById(R.id.wrapper_buttons);
        mWrapperText = (LinearLayout) view.findViewById(R.id.wrapper_text);
    }

    private void initDropDownMenuAdapter() {
        mDropDownMenuAdapter = new MenuAdapter(getActivity(), mWrapperButtons, mWrapperText,
                mMenuParams.getMenuObjects(), mMenuParams.getActionBarSize());
        mDropDownMenuAdapter.setOnItemClickListener(this);
        mDropDownMenuAdapter.setOnItemLongClickListener(this);
        mDropDownMenuAdapter.setAnimationDuration(mMenuParams.getAnimationDuration());
    }

    /*private void close() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, mMenuParams.getAnimationDelay());
    }*/

    /**
     * Menu item click method
     */
    @Override
    public void onClick(View v, int position, boolean selected) {
        if (mItemClickListener != null) {
            mItemClickListener.onMenuItemClick(v, position, selected);
        }
//        close();
    }

    @Override
    public void onLongClick(View v, int position, boolean selected) {
        if (mItemLongClickListener != null) {
            mItemLongClickListener.onMenuItemLongClick(v, position, selected);
        }
//        close();
    }
    
    @Override
    public void show(FragmentManager manager, String tag) {
    	// TODO Auto-generated method stub
    	super.show(manager, tag);
    	 new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
            	 mWrapperText.setVisibility(View.GONE);
             }
         }, 3000);
    }
    
    private static boolean isClosing = false;
	public void closeMenu() {
		if(isClosing){
			return;
		}
		isClosing = true;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getActivity().onBackPressed();
				mDropDownMenuAdapter.closeMenu();
				// dismiss();
			}
		}, 0);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
				isClosing = false;
			}
		}, 500);
	}
    
}