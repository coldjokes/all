package com.cnbaosi.cabinet.serivce.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cnbaosi.cabinet.entity.modal.*;
import com.cnbaosi.cabinet.mapper.DeptMapMapper;
import com.cnbaosi.cabinet.mapper.DeptMapper;
import com.cnbaosi.cabinet.serivce.*;
import com.cnbaosi.cabinet.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.DeptMapCriteria;
import com.cnbaosi.cabinet.entity.modal.DeptMap;
import com.google.common.collect.Lists;

/**
 * 部门映射方法实现类
 */
@Service
public class DeptMapServiceImpl extends ServiceImpl<DeptMapMapper, DeptMap> implements DeptMapService {

    private static final Logger log = LoggerFactory.getLogger(DeptServiceImpl.class);

    @Autowired
    private UserService userSvc;

    @Autowired
    private DeptService deptSvc;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 更新部门人员关系
     * @param deptMapCriteria
     * @return
     */
    @Override
    public String updateDeptMap(DeptMapCriteria deptMapCriteria) {

        String msg = null;

        String deptId = deptMapCriteria.getDeptId();
        List<String> userIdList = deptMapCriteria.getUserIdList();

        if(StringUtils.isNotBlank(deptId)) {

            boolean isSonReleated = false;
            List<String> sonReleatedList = Lists.newArrayList();

            //解绑前，先确定旗下子部门有没有绑定相同的人员，如果有，则不能解绑。
            //1.获取所有子分类，包括子子分类
            List<Dept> sonDeptList = deptSvc.getAllSonDept(deptId);
            if(CollectionUtils.isNotEmpty(sonDeptList)) {

                //2.通过所有子分类id查找出子分类关联的人员信息
                List<String> sonUserDeptList = sonDeptList.stream().map(Dept :: getId).collect(Collectors.toList());
                DeptMapCriteria sonDeptMapCriteria = new DeptMapCriteria();
                sonDeptMapCriteria.setDeptIdList(sonUserDeptList);
                List<DeptMap> sonDeptMapList = this.getMapList(sonDeptMapCriteria);


                if(CollectionUtils.isNotEmpty(sonDeptMapList)) {
                    Set<String> sonUserIdList = sonDeptMapList.stream().map(DeptMap :: getUserId).collect(Collectors.toSet());

                    //3.如果子分类下的人员信息不为空，则就需要开始比较了。获取选中分类下原来的人员信息
                    DeptMapCriteria oldDeptMapCriteria = new DeptMapCriteria();
                    oldDeptMapCriteria.setDeptId(deptId);
                    List<DeptMap> oldDeptMapList = this.getMapList(oldDeptMapCriteria);
                    List<String> oldUserIdList = oldDeptMapList.stream().map(DeptMap :: getUserId).collect(Collectors.toList());

                    //4.比较本次页面上保存的人员id和原来该分类下的人员id，获取本次保存时去除的人员id
                    List<String> removedIdList = oldUserIdList.stream().filter(oldId -> {
                        String i = userIdList.stream().filter(newId -> newId.equals(oldId)).findFirst().orElse(null);
                        return i == null;
                    }).collect(Collectors.toList());


                    //5.查找子分类下，有没有此次去除的人员id，如果有，则说明子分类下也绑定了此人员。因此不能解绑
                    sonReleatedList = sonUserIdList.stream().filter(sonUserId -> {
                        String i = removedIdList.stream().filter(removedId -> removedId.equals(sonUserId)).findFirst().orElse(null);
                        return i != null;
                    }).collect(Collectors.toList());

                    //6. 如果子分类下有关联，则直接提示信息解绑失败。若无关联，则继续更新操作
                    isSonReleated = CollectionUtils.isNotEmpty(sonReleatedList);
                }
            }
            if(!isSonReleated) {
                // deptId|userIdList
                // update&&delete之前先判断是否有用户绑定在其他部门下
                List<String> oldDeptIds = new ArrayList<>();
                // 判断是否存在（上级或）下级部门,存在则获取部门ID
                List<String> depts = deptMapper.selectByDeptId(deptId);
                // 判断人员是否已经有其他部门绑定，是：提示错误，否：继续绑定
                boolean bools = true;                                                    //  确定用户是否已经与其他部门绑定
                String users = "";
                if(CollectionUtils.isNotEmpty(userIdList)){
                    for(String userId: userIdList) {
                        // 获取用户已绑定部门idList
                        oldDeptIds = deptMapper.selectByUserId(userId);
                        for (String dept1 : oldDeptIds) {
                            if (!depts.contains(dept1) && !dept1.equals(deptId)) {
                                bools = false;
                                users = userId;
                                break;
                            }
                        }
                        if (bools == true) {
                            //删除当前部门所绑定的所有人员
                            EntityWrapper<DeptMap> wrapper = new EntityWrapper<>();
                            wrapper.eq("dept_id", deptId);
                            super.delete(wrapper);

                            //插入人员部门关系
                            if (CollectionUtils.isNotEmpty(userIdList)) {
                                List<DeptMap> mapList = Lists.newArrayList();
                                for (String userId1 : userIdList) {
                                    DeptMap map = new DeptMap();
                                    map.setDeptId(deptId);
                                    map.setUserId(userId1);
                                    mapList.add(map);
                                }
                                this.addDeptMaps(mapList);
                            }
                        } else {
                            User lastUsers = userSvc.selectById(users);
                            msg = "人员【" + lastUsers.getUsername() + "】已与其他部门相关联，请先解绑再操作！";
                        }
                    }
                }else{
                    //删除当前部门所绑定的所有人员
                    EntityWrapper<DeptMap> wrapper = new EntityWrapper<>();
                    wrapper.eq("dept_id", deptId);
                    super.delete(wrapper);
                }

            } else {
                String userId = sonReleatedList.get(0);
                User releatedMa = userSvc.selectById(userId);
                msg = "人员【" + releatedMa.getUsername() + "】在子分类中也被关联了，请先解绑子分类中的人员信息后再操作";
            }
        }
        return msg;
    }

    /**
     * 点击部门获取右侧人员列表
     * @param deptMapCriteria
     * @return
     */
    @Override
    public List<DeptMap> getMapList(DeptMapCriteria deptMapCriteria) {

        String deptId = deptMapCriteria.getDeptId();
        List<String> deptIdList = deptMapCriteria.getDeptIdList();

        EntityWrapper<DeptMap> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotBlank(deptId)) {
            wrapper.eq("dept_id", deptId);
        }
        if(CollectionUtils.isNotEmpty(deptIdList)) {
            wrapper.in("dept_id", deptIdList);
        }
        wrapper.orderBy("create_time", true);
        return super.selectList(wrapper);
    }

    @Override
    public boolean addDeptMaps(List<DeptMap> maps) {
        return super.insertBatch(maps);
    }



}

