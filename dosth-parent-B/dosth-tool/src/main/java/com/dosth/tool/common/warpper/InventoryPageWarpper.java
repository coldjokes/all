package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.state.InventoryStatus;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.Inventory;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.vo.ViewUser;

/**
* @description 盘点分页封装对象
* @author guozhidong
*
*/
public class InventoryPageWarpper extends PageWarpper<Inventory> {

   public InventoryPageWarpper(Page<Inventory> page) {
       super(page);
   }

   @Override
   protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
       if (obj instanceof EquDetailSta) {
           EquDetailSta sta = ((EquDetailSta) obj);
           map.put(key, sta.getEquDetail().getEquSetting().getEquSettingName() + "" + "[" + sta.getEquDetail().getRowNo() + "-" + sta.getColNo() + "]");
       } else if (obj instanceof SubBox) {
           SubBox box = ((SubBox) obj);
           map.put(key, box.getEquSetting().getEquSettingName() + "[" + box.getRowNo() + "-" + box.getColNo() + "]");
       } else if (obj instanceof MatEquInfo) {
           map.put(key, ((MatEquInfo) obj).getMatEquName());
       } else if (obj instanceof ViewUser) {
           map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
       } else if (obj instanceof InventoryStatus) {
    	  if (Integer.valueOf(String.valueOf(map.get("inventoryNum"))) > Integer.valueOf(String.valueOf(map.get("storageNum")))) {
    		  map.put(key, InventoryStatus.SURPLUS.getMessage());
    	  } else if (Integer.valueOf(String.valueOf(map.get("storageNum"))) > Integer.valueOf(String.valueOf(map.get("inventoryNum")))) {
    		  map.put(key, InventoryStatus.LOSS.getMessage());
    	  } else {
    		  map.put(key, InventoryStatus.PING.getMessage());
    	  }
       }
   }
}