package com.yimeng.baolingtong.adpater;

import java.util.List;

import com.ab.image.AbImageLoader;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.andbase.demo.model.Article;
import com.yimeng.baolingtong.R;
import com.yimeng.baolingtong.adpater.ArticleListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddChildListAdapter extends BaseAdapter {
	  
		private Context mContext;
	    //列表展现的数据
	    private List<Article> mList;
	    //图片下载器
	    private AbImageLoader mAbImageLoader = null;
	    
	   /**
	    * 构造方法
	    * @param context
	    * @param list 列表展现的数据
	    */
	    public AddChildListAdapter(Context context, List<Article> list){
	    	 mContext = context;
	    	 mList = list;
	         //图片下载器
	         mAbImageLoader = AbImageLoader.getInstance(context);
	    }   
	    
	    @Override
	    public int getCount() {
	    	/*if(mList==null){
	    		return 0;
	    	}
	        return mList.size();*/
	    	 return 3;
	    }
	    
	    @Override
	    public Object getItem(int position) {
	        return mList.get(position);
	    }

	    @Override
	    public long getItemId(int position){
	      return position;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent){
	    	  final ViewHolder holder;
	          if(convertView == null){
		          //使用自定义的list_items作为Layout
		          convertView = LayoutInflater.from(mContext).inflate(R.layout.children_list_item, parent, false);
		          //使用减少findView的次数
				  holder = new ViewHolder();
				  holder.itemsIcon = ((ImageView) convertView.findViewById(R.id.children_img)) ;
				  holder.horizontalProgressBar = (AbHorizontalProgressBar) ( convertView.findViewById(R.id.horizontalProgressBar));
				  //设置标记
				  convertView.setTag(holder);
	          }else{
	        	  holder = (ViewHolder) convertView.getTag();
	          }
	          if(position==getCount()-1){
	        	  holder.itemsIcon.setImageResource(R.drawable.map_add_obj_normal_);
	          }
	         /* //获取该行数据
	          Article mArticle = (Article)mList.get(position);
	          
	          //设置数据到View
	          String imageUrl = (String)mArticle.getImageUrl();
	          //设置加载中的View
	          View loadingView = convertView.findViewById(R.id.progressBar);
	          //图片的下载
	          mAbImageLoader.display(holder.itemsIcon,loadingView,imageUrl,500,500);*/
	          
	          return convertView;
	    }
	    
	    /**
		 * ViewHolder类
		 */
		static class ViewHolder {
			ImageView itemsIcon;
			AbHorizontalProgressBar horizontalProgressBar;
		}
}
