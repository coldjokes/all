package com.cnbaosi.cabinet.serivce.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.cnbaosi.cabinet.entity.criteria.DeptCriteria;
import com.cnbaosi.cabinet.entity.criteria.DeptMapCriteria;
import com.cnbaosi.cabinet.entity.modal.Dept;
import com.cnbaosi.cabinet.entity.modal.DeptMap;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;
import com.cnbaosi.cabinet.mapper.DeptMapper;
import com.cnbaosi.cabinet.mapper.JurisdictionMapper;
import com.cnbaosi.cabinet.serivce.DeptMapService;
import com.cnbaosi.cabinet.serivce.DeptService;
import com.cnbaosi.cabinet.util.DateTimeUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    private static final Logger log = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private DeptMapService deptMapSvc;
    @Autowired
    private JurisdictionMapper jurMapper;

    //  新增部门
    @Override
    public Boolean addDept(Dept dept) {
        return super.insert(dept);
    }

    //  删除部门
    @Override
    public String deleteDept(String id) {
        String msg = null;
        if(id.equals("-1")) {
            msg = "根节点不能删除！";
        } else {

            DeptMapCriteria deptMapCriteria = new DeptMapCriteria();
            deptMapCriteria.setDeptId(id);
            List<DeptMap> mapList = deptMapSvc.getMapList(deptMapCriteria);

            if(org.apache.commons.collections.CollectionUtils.isNotEmpty(mapList)) {
                msg = "该部门下还有绑定人员，请解绑后再删除此部门！";
            } else {
                //查询是否有子部门
                DeptCriteria deptCriteria = new DeptCriteria();
                deptCriteria.setpId(id);
                List<Dept> deptList = this.getDept(deptCriteria);
                if(org.apache.commons.collections.CollectionUtils.isNotEmpty(deptList)) {
                    msg = "该部门下还有子部门，请删除后再删除此部门！";
                } else {
                    Dept dept = new Dept();
                    dept.setDeleteTime(DateTimeUtil.now());
                    dept.setId(id);
                    super.updateById(dept);
                }
            }
        }
        return msg;
    }
    //  修改部门
    @Override
    public Boolean updateDept(Dept dept) {
        dept.setUpdateTime(DateTimeUtil.now());
        return super.updateById(dept);
    }
    //  获取部门树
    @Override
    public List<TreeNodes> getUserDeptTree() {
        DeptCriteria deptCriteria = new DeptCriteria();
        List<Dept> source = this.getDept(deptCriteria);

        List<TreeNodes> result = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(source)) {

            for(Dept dept : source) {
                String id = dept.getId();
                if(id.equals("-1")) {
                    TreeNodes tn = new TreeNodes(id, dept.getpId(), dept.getText());

                    tn.setChildren(this.getChildrenNode(id, source));
                    result.add(tn);
                }
            }
        }
        return result;
    }

    /**
     * 查询部门对应人员列表
     * @param deptCriteria
     * @return
     */
    @Override
    public List<Dept> getDept(DeptCriteria deptCriteria) {
        String id = deptCriteria.getId();
        String pId = deptCriteria.getpId();
        String text = deptCriteria.getText();


        EntityWrapper<Dept> wrapper = new EntityWrapper<>();

        if(StringUtils.isNotBlank(id)) {
            wrapper.like("id", id);
        }
        if(StringUtils.isNotBlank(pId)) {
            wrapper.like("p_id", pId);
        }
        if(StringUtils.isNotBlank(text)) {
            wrapper.eq("text", text);
        }
        wrapper.isNull("delete_time");
        wrapper.orderBy("create_time", true);

        return super.selectList(wrapper);
    }
    //  获取部门子节点
    @Override
    public List<TreeNodes> getChildrenNode(String id, List<Dept> source) {
        List<TreeNodes> newTrees = Lists.newArrayList();
        for (Dept dept : source) {
            if (dept.getpId() != null && dept.getpId().equals(id) && !dept.getId().equals(id)) {
                TreeNodes tn = new TreeNodes(dept.getId(), dept.getpId(), dept.getText());
                //递归获取子节点下的子节点，即设置树控件中的children
                tn.setChildren(getChildrenNode(dept.getId(), source));
                newTrees.add(tn);
            }
        }
        return newTrees;
    }

    @Override
    public List<Dept> getAllSonDept(String id) {
        DeptCriteria deptCriteria = new DeptCriteria();
        List<Dept> sourceList = this.getDept(deptCriteria);
        List<Dept> resultList = Lists.newArrayList();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(sourceList)) {
            List<Dept> sonDeptList = sourceList.stream().filter(dept -> {
                return id.equals(dept.getpId());
            }).collect(Collectors.toList());

            resultList.addAll(sonDeptList);
            getSons(resultList, sourceList, sonDeptList);
        }
        return resultList;
    }

    private void getSons(List<Dept> resultList, List<Dept> sourceList, List<Dept> matDeptList) {
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(matDeptList)) {
            for(Dept matDept : matDeptList) {
                String sonId = matDept.getId();
                List<Dept> sonSonDeptList = sourceList.stream().filter(dept -> {
                    return sonId.equals(dept.getpId());
                }).collect(Collectors.toList());
                resultList.addAll(sonSonDeptList);
                getSons(resultList, sourceList, sonSonDeptList);
            }
        }
    }
}
