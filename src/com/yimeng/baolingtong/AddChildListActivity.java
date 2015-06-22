package com.yimeng.baolingtong;

import com.amap.api.mapcore2d.ad;
import com.yimeng.baolingtong.adpater.AddChildListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class AddChildListActivity extends Activity {
	private GridView child_grid;
	private AddChildListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_child_list_activiy);
		child_grid=(GridView) findViewById(R.id.child_grid);
		adapter=new AddChildListAdapter(AddChildListActivity.this, null);
		child_grid.setAdapter(adapter);
	}

}
