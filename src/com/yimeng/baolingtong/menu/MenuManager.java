package com.yimeng.baolingtong.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.yimeng.baolingtong.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MenuManager {
	
	private Context context;
	public ContextMenuDialogFragment mMenuDialogFragment;
	
	public final static int CURRENT_LOCATION = 0, LOCATION_FREQUENCY = 1,
			SAVE_AREA = 2, TAKE_OFF_ALERT = 3, BLT_FOLLOW = 4, HIT_ALERT = 5;
	
	//menu dialog与屏幕的上边距和右边距
	private int x,y;
	
	private static boolean[] menuStatus;
	private static HashMap<Integer, Integer> selIcons;
	private static HashMap<Integer, Integer> unSelIcons;
	
	static {
		menuStatus = new boolean[]{true,false,false,false,false,false};
		unSelIcons = new HashMap<Integer, Integer>();
		selIcons = new HashMap<Integer, Integer>();
		selIcons.put(MenuManager.CURRENT_LOCATION, R.drawable.current_location);
		selIcons.put(MenuManager.LOCATION_FREQUENCY,
				R.drawable.history_location);
		selIcons.put(MenuManager.SAVE_AREA, R.drawable.love_circle);
		selIcons.put(MenuManager.TAKE_OFF_ALERT, R.drawable.fall_sos);
		selIcons.put(MenuManager.BLT_FOLLOW, R.drawable.ble_enable);
		selIcons.put(MenuManager.HIT_ALERT, R.drawable.hit_sos);

		unSelIcons.put(MenuManager.CURRENT_LOCATION,
				R.drawable.current_location_disable);
		unSelIcons.put(MenuManager.LOCATION_FREQUENCY,
				R.drawable.history_location_enable);
		unSelIcons.put(MenuManager.SAVE_AREA, R.drawable.love_circle_enable);
		unSelIcons.put(MenuManager.TAKE_OFF_ALERT, R.drawable.fall_sos_enable);
		unSelIcons.put(MenuManager.BLT_FOLLOW, R.drawable.ble_disable);
		unSelIcons.put(MenuManager.HIT_ALERT, R.drawable.hit_sos_press_enable);
	}
	
	public MenuManager(Activity activity,int x,int y){
		this.context = activity;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
//		int screenHeight = dm.heightPixels;
		int xOffset = (int) context.getResources().getDimension(R.dimen.x_offset);
		int YOffset = (int) context.getResources().getDimension(R.dimen.y_offset);
		this.x = screenWidth - x + xOffset;
		this.y = y + YOffset;
		initMenuFragment();
	}
	
	public static int getMenuIconResource(int id){
    	if((menuStatus[id])){
    		return selIcons.get(id);
        } else{
        	return unSelIcons.get(id);
        }
	}
	
	public static void changeItemStatus(int id){
		menuStatus[id] = !menuStatus[id];
	}
	
	public static boolean getMenuItemStatus(int id){
		return menuStatus[id];
	}
	
	public void show(FragmentManager fragmentManager){
		if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
        }
	}
	
	private void initMenuFragment() { 
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) context.getResources().getDimension(R.dimen.menu_item_size));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setPadding(0, y,x, 0);
        mMenuDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    private List<MenuObject> getMenuObjects() {
		MenuObject currentLocation = new MenuObject(CURRENT_LOCATION,
				getString(R.string.click_to_location),
				getMenuIconResource(CURRENT_LOCATION));
		MenuObject locationFrequency = new MenuObject(LOCATION_FREQUENCY,
				getString(R.string.location_frequency),
				getMenuIconResource(LOCATION_FREQUENCY));
		MenuObject saveArea = new MenuObject(SAVE_AREA,
				getString(R.string.safety_area), getMenuIconResource(SAVE_AREA));
		MenuObject takeOffAlert = new MenuObject(TAKE_OFF_ALERT,
				getString(R.string.take_off_switch), getMenuIconResource(TAKE_OFF_ALERT));
		MenuObject bltFollow = new MenuObject(BLT_FOLLOW,
				getString(R.string.blt_follow), getMenuIconResource(BLT_FOLLOW));
		MenuObject hitAlert = new MenuObject(HIT_ALERT,
				getString(R.string.collision_switch), getMenuIconResource(HIT_ALERT));

		List<MenuObject> menuObjects = new ArrayList<MenuObject>();
        menuObjects.add(currentLocation);
        menuObjects.add(locationFrequency);
        menuObjects.add(saveArea);
        menuObjects.add(takeOffAlert);
        menuObjects.add(bltFollow);
        menuObjects.add(hitAlert);
        return menuObjects;
    }
    
    private String getString(int resId){
    	return context.getResources().getString(resId);
    }
}
