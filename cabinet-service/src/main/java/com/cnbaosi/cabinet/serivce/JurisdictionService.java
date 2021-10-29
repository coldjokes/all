package com.cnbaosi.cabinet.serivce;

import com.cnbaosi.cabinet.entity.criteria.MaterialCriteria;
import com.cnbaosi.cabinet.entity.criteria.UserCriteria;
import com.cnbaosi.cabinet.entity.modal.Jurisdiction;
import com.cnbaosi.cabinet.entity.modal.UserMaterialMap;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface JurisdictionService {
    /**
     * @description 获取用户树
     * @return
     */
    List<TreeNodes> getUserDeptTree();


    /**
     * @return 获取取料权限-物料类别树
     */
    List<TreeNodes> getMaterialCategory();

    /**
     * 查询人员物料树
     */
    List<String> getUserMaterials(MaterialCriteria materialCriteria);

    /**
     * 保存人员取料权限
     */
    String saveAccess(UserCriteria userCriteria);

    /**
     * 判断用户是否有领取物料权限
     * @param jurisdiction
     * @return
     */
    Boolean receiveRoles(Jurisdiction jurisdiction);

}
