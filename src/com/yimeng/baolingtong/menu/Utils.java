package com.yimeng.baolingtong.menu;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yimeng.baolingtong.R;

public class Utils {

    public static int getDefaultActionBarSize(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public static View getItemTextView(Context context, MenuObject menuItem, int menuItemSize) {
    	RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(
    			ViewGroup.LayoutParams.WRAP_CONTENT, menuItemSize);
    	LinearLayout parent = new LinearLayout(context);
    	parent.setPadding(0, menuItemSize / 4, 0, menuItemSize / 4);
    	parent.setLayoutParams(parentParams);
        TextView itemTextView = new TextView(context);
    	RelativeLayout.LayoutParams childParams = new RelativeLayout.LayoutParams(
    			ViewGroup.LayoutParams.WRAP_CONTENT, menuItemSize/2);
        itemTextView.setLayoutParams(childParams);
//        itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.menu_text_size));
        itemTextView.setText(menuItem.getTitle());
        itemTextView.setPadding(0, 0, (int) context.getResources().getDimension(R.dimen.text_right_padding), 0);
        itemTextView.setGravity(Gravity.CENTER_VERTICAL);
        int textColor = menuItem.getTextColor() == 0 ?
                context.getResources().getColor(android.R.color.white) :
                menuItem.getTextColor();
//                textColor = Color.YELLOW;
        itemTextView.setTextColor(textColor);
        itemTextView.setBackgroundColor(Color.CYAN);
        itemTextView.setTextAppearance(context, menuItem.getMenuTextAppearanceStyle() > 0
                ? menuItem.getMenuTextAppearanceStyle()
                : R.style.TextView_DefaultStyle);
        parent.addView(itemTextView);
        return parent;
    }

    public static ImageView getItemImageButton(Context context, MenuObject item,boolean isSelected) {
        ImageView imageView = new ImageButton(context);
        imageView.setId(item.getId());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lp);
        imageView.setPadding((int) context.getResources().getDimension(R.dimen.menu_item_padding),
                (int) context.getResources().getDimension(R.dimen.menu_item_padding),
                (int) context.getResources().getDimension(R.dimen.menu_item_padding),
                (int) context.getResources().getDimension(R.dimen.menu_item_padding));
        imageView.setClickable(false);
        imageView.setFocusable(false);
        imageView.setBackgroundColor(Color.TRANSPARENT);

        if (item.getColor() != 0) {
            Drawable color = new ColorDrawable(item.getColor());
            imageView.setImageDrawable(color);
        } else if (item.getResource() != 0) {
            imageView.setImageResource(item.getResource());
        } else if (item.getBitmap() != null) {
            imageView.setImageBitmap(item.getBitmap());
        } else if (item.getDrawable() != null) {
            imageView.setImageDrawable(item.getDrawable());
        }
        imageView.setScaleType(item.getScaleType());
        
        return imageView;
    }

    public static View getDivider(Context context, MenuObject menuItem) {
        View dividerView = new View(context);
        RelativeLayout.LayoutParams viewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.divider_height));
        viewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dividerView.setLayoutParams(viewLayoutParams);
        dividerView.setClickable(true);
        int dividerColor = menuItem.getDividerColor() == Integer.MAX_VALUE ?
                context.getResources().getColor(R.color.divider_color) :
                menuItem.getDividerColor();
        dividerView.setBackgroundColor(dividerColor);
        return dividerView;
    }

    public static RelativeLayout getImageWrapper(Context context, MenuObject menuItem, int menuItemSize,
                                                 View.OnClickListener onCLick, View.OnLongClickListener onLongClick,
                                                 boolean showDivider,boolean isSelected) {
        RelativeLayout imageWrapper = new RelativeLayout(context);
        LinearLayout.LayoutParams imageWrapperLayoutParams = new LinearLayout.LayoutParams(menuItemSize, menuItemSize);
        imageWrapper.setLayoutParams(imageWrapperLayoutParams);
        ImageView menuIcon = Utils.getItemImageButton(context, menuItem, isSelected);
        menuIcon.setOnClickListener(onCLick);
        menuIcon.setOnLongClickListener(onLongClick);
        imageWrapper.addView(menuIcon);
        if (showDivider) {
            imageWrapper.addView(getDivider(context, menuItem));
        }

        /*if (menuItem.getBgColor() != 0) {
            imageWrapper.setBackgroundColor(menuItem.getBgColor());
        } else if (menuItem.getBgDrawable() != null) {
            imageWrapper.setBackgroundDrawable(menuItem.getBgDrawable());
        } else if (menuItem.getBgResource() != 0) {
            imageWrapper.setBackgroundResource(menuItem.getBgResource());
        } else {
            imageWrapper.setBackgroundColor(context.getResources().getColor(R.color.menu_item_background));
        }*/
        return imageWrapper;
    }

}
