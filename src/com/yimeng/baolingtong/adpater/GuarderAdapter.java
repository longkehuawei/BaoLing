package com.yimeng.baolingtong.adpater;

import com.yimeng.baolingtong.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GuarderAdapter extends BaseAdapter{
	private Context context;
	private String[] data = null;
	private static int selectedPosition = 0;
	
	public GuarderAdapter(Context context, String[] data) {
		super();
		this.context = context;
		this.data = data;
	}
	
	public void setSelectedPosition(int position){
		selectedPosition = position;
	}
	
	public int getSelectedPosition(){
		return selectedPosition;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	@Override
	public String getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.guarder_item, null);
			holder = new ViewHolder();
			holder.guarder = (TextView) convertView.findViewById(R.id.guarder);
			convertView.setTag(holder);
			holder.guarder.setId(position);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == selectedPosition){
			convertView.setBackgroundColor(Color.DKGRAY);
		} else{
			convertView.setBackgroundColor(context.getResources().getColor(R.color.add_photo_bg));
		}
		holder.guarder.setText(data[position]);
		/*holder.guarder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSelectedPosition(holder.guarder.getId()); 
				GuarderAdapter.this.notifyDataSetChanged();
			}
		});*/
		return convertView;
	}
	
	class ViewHolder{
		TextView guarder;
	}

}
