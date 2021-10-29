package com.cnbaosi.cabinet.controller.api;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.DeptMapCriteria;
import com.cnbaosi.cabinet.entity.modal.DeptMap;
import com.cnbaosi.cabinet.serivce.DeptMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dept/map")
public class DeptMapController  extends BaseController {

    @Autowired
    private DeptMapService deptMapService;

    @LogRecord("更新人员部门关系")
    @PostMapping
    public RestFulResponse<String> updateDeptMap(@RequestBody DeptMapCriteria deptMapCriteria) {

        String msg = deptMapService.updateDeptMap(deptMapCriteria);
        if(msg == null) {
            return success("保存成功！");
        } else {
            return error("保存失败！" + msg);
        }
    }

    @LogRecord("查询人员部门关系")
    @GetMapping
    public RestFulResponse<DeptMap> deptMaterial( DeptMapCriteria deptMapCriteria) {
        List<DeptMap> results = deptMapService.getMapList(deptMapCriteria);
        return success(results.size(), results);
    }

}
