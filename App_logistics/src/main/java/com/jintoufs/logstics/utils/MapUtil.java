package com.jintoufs.logstics.utils;

import android.content.Context;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.NaviPara;

public class MapUtil {
	public static void openAmap(Double longitude, Double latitude, Context context) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置,先纬度后经度
		LatLng latLng = new LatLng(latitude, longitude);
		naviPara.setTargetPoint(latLng);
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
		try {
			// 调起高德地图导航
			AMapUtils.openAMapNavi(naviPara, context);
		} catch (Exception e) {
			// 如果没安装会进入异常，调起下载页面
			AMapUtils.getLatestAMapApp(context);
		}
	}
}
