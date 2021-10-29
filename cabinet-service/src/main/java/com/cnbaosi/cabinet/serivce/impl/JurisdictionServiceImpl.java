package com.cnbaosi.cabinet.serivce.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.DeptCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialCriteria;
import com.cnbaosi.cabinet.entity.criteria.UserCriteria;
import com.cnbaosi.cabinet.entity.modal.*;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;
import com.cnbaosi.cabinet.mapper.JurisdictionMapper;
import com.cnbaosi.cabinet.mapper.UserMaterialMapMapper;
import com.cnbaosi.cabinet.serivce.DeptService;
import com.cnbaosi.cabinet.serivce.JurisdictionService;
import com.cnbaosi.cabinet.serivce.MaterialCategoryService;
import com.cnbaosi.cabinet.serivce.UserService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *  人员物料权限
 */
@Service
public class JurisdictionServiceImpl extends ServiceImpl<UserMaterialMapMapper, UserMaterialMap> implements JurisdictionService {

    private  static final String userId = "-1";

    @Autowired
    DeptService deptService;
    @Autowired
    UserService userService;
    @Autowired
    MaterialCategoryService materialCateorySvc;
    @Autowired
    JurisdictionMapper jurMapper;

    /**
     * 查询用户树
     * @return
     */
    @Override
    public List<TreeNodes> getUserDeptTree() {
//        UserDeptTreeCriteria userDeptTree = new UserDeptTreeCriteria();
//        DeptCriteria deptCriteria = new DeptCriteria();
//        List<Dept> sourceDept = deptService.getDept(deptCriteria);//部门
//        List<User> sourceUser = userService.getUserByDept(userId);//人员
//
//        for(User user :sourceUser){
//            Dept depts  = new Dept();
//            depts.setId(user.getId());
//            depts.setText(user.getUsername());
//            depts.setType("user");
//            sourceDept.add(depts);
//        }
//        /**
//         * TODO：拼接人员列表+部门列表为TreeNodes格式
//         * 1，人员list拼接到部门list中
//         * 2，部门list根据pId拼接为树形结构
//         */
//        List<TreeNodes> result = Lists.newArrayList();
//        if(CollectionUtils.isNotEmpty(sourceDept)){
//            for(Dept dept : sourceDept) {
//                String id = dept.getId();
//                if(id.equals("-1")) {
//                    TreeNodes tn = new TreeNodes(id, dept.getpId(), dept.getText(),dept.getType());
//                    tn.setChildren(this.getChildrenNode(id, sourceDept));
//                    result.add(tn);
//                }
//            }
//        }
//        return result;

        DeptCriteria deptCriteria = new DeptCriteria();
        List<Dept> depts = deptService.getDept(deptCriteria);
        List<Dept> users = jurMapper.getUsers(depts);
        for(Dept dept:depts){
            dept.setType("dept");
            users.add(dept);
        }
        List<TreeNodes> result = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(users)){
            for(Dept dept : users) {
                String id = dept.getId();
                if(id.equals("-1")) {
                    TreeNodes tn = new TreeNodes(id, dept.getpId(), dept.getText(),dept.getType());
                    tn.setChildren(this.getChildrenNode(id, users));
                    result.add(tn);
                }
            }
        }
        return result;
    }

    /**
     * 查询物料类别树
     * @return
     */
    @Override
    public List<TreeNodes> getMaterialCategory(){
        MaterialCategoryCriteria categoryCriteria = new MaterialCategoryCriteria();
        List<MaterialCategory> categories = materialCateorySvc.getMaterialCategory(categoryCriteria);   // 物料类别
        List<Dept> materials = jurMapper.getMaterials(categories);
        for(MaterialCategory category:categories){
            Dept depts  = new Dept();
            depts.setId(category.getId());
            depts.setpId(category.getpId());
            depts.setText(category.getText());
            depts.setType("dept");
            materials.add(depts);
        }

        List<TreeNodes> result = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(materials)){
            for(Dept dept : materials) {
                String id = dept.getId();
                if(id.equals("-1")) {
                    TreeNodes tn = new TreeNodes(id, dept.getpId(), dept.getText(),dept.getType());
                    tn.setChildren(this.getChildrenNode(id, materials));
                    result.add(tn);
                }
            }
        }

        return result;
    }

    /**
     * 点击人员树获取物料分类树
     * @param materialCriteria
     * @return
     */
    @Override
    public List<String> getUserMaterials(MaterialCriteria materialCriteria) {
        String type = materialCriteria.getText();

        if(type.equals("user")){
            type = "0";
        }else{
            type = "1";
        }
//        selectId = UserMaterialMap.getSelectId();
        List<String> selectIdList = materialCriteria.getIdList();
        EntityWrapper<UserMaterialMap> wrapper = new EntityWrapper<>();
        if(CollectionUtils.isNotEmpty(selectIdList)){
            wrapper.in("select_id",selectIdList);
        }
//        if (StringUtils.isNotBlank(type)){
//            wrapper.eq("type",type);
//        }
        wrapper.isNull("delete_time");
        wrapper.orderBy("update_time",true);
        List<UserMaterialMap> userMaterialMaps = super.selectList(wrapper);

        System.out.println(userMaterialMaps);
        List materialLists = new ArrayList();
        for(UserMaterialMap userMaterialMap : userMaterialMaps){
            materialLists.add(userMaterialMap.getValue());
        }
        return materialLists;
    }

    /**
     * 保存人员取料权限
     * @param userCriteria
     * @return
     */
    @Override
    public String saveAccess(UserCriteria userCriteria) {
        String msg = null;
        List<String> idList = userCriteria.getIdList();
        String type = userCriteria.getUsername();
        String userDept = userCriteria.getText();
        // 删除用户部门取料权限
        EntityWrapper<UserMaterialMap> wrapper = new EntityWrapper<>();
        wrapper.eq("select_id",userDept);
        super.delete(wrapper);
        // 插入用户部门取料权限
        if(CollectionUtils.isNotEmpty(idList)){
            List<UserMaterialMap> idMapList = Lists.newArrayList();
            for(String value :idList){
                UserMaterialMap map = new UserMaterialMap();
                map.setSelectId(userDept);
                map.setValue(value);
                map.setType(type);
                idMapList.add(map);
            }
            super.insertBatch(idMapList);

        }
        return msg;
    }


    /**
     * 判断人员是否有权限领取物料
     * @param jurisdiction
     * @return
     */
    @Override
    public Boolean receiveRoles(Jurisdiction jurisdiction) {
        Boolean result = false;
        // text:userId
        // pId:materialId
        String userId = jurisdiction.getText();
        String materialId = jurisdiction.getpId();
        // 1,获取这个人上级所有部门ID
        List<String> ids = new ArrayList<>();
        ids.add(userId);
        ids.add("-1");
        String deptId = jurMapper.getPids(userId);
        if(deptId != null){
            if(!deptId.equals("-1")){
                ids.add(deptId);
                String depts = jurMapper.getDeptPId(deptId);
                if(depts != null){
                    if(!depts.equals("-1")) {
                        ids.add(depts);
                    }
                }
            }
        }

        // 整合完成id及上级Id后进行物料权限筛选
        List<String> count =jurMapper.getRoles(ids);

        // 判断物料ID是否在权限内
        for(String id:count){
            if(id.equals(materialId)){
                result = true;
            }
        }
        return result;
    }

    // 组合树形结构
    public List<TreeNodes> getChildrenNode(String id, List<Dept> source) {
        List<TreeNodes> newTrees = Lists.newArrayList();
        for (Dept dept : source) {
            if (dept.getpId() != null && dept.getpId().equals(id) && !dept.getId().equals(id)) {
                TreeNodes tn = new TreeNodes(dept.getId(), dept.getpId(), dept.getText(),dept.getType());
                //递归获取子节点下的子节点，即设置树控件中的children
                tn.setChildren(getChildrenNode(dept.getId(), source));
                newTrees.add(tn);
            }
        }

        return newTrees;
    }


}
