package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import com.cnbaosi.cabinet.entity.criteria.DeptCriteria;
import com.cnbaosi.cabinet.entity.modal.Dept;
import com.cnbaosi.cabinet.serivce.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;

/**
 * 物料类别
 */
@RestController
@RequestMapping("/api/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    @LogRecord("增加部门")
    @PostMapping
    public RestFulResponse<String> addDept(@RequestBody Dept dept) {
        return actionResult(deptService.addDept(dept));
    }

    @LogRecord("删除部门")
    @DeleteMapping("/{id}")
    public RestFulResponse<String> deleteDept(@PathVariable("id") String id) {
        String msg = deptService.deleteDept(id);
        if(msg == null) {
            return success("删除成功！");
        } else {
            return error("删除失败！" + msg);
        }
    }

    @LogRecord("修改部门")
    @PutMapping
    public RestFulResponse<String> updateDept(@RequestBody Dept dept) {
        return actionResult(deptService.updateDept(dept));
    }

    @LogRecord("查询人员列表")
    @GetMapping
    public RestFulResponse getDept(DeptCriteria deptCriteria) {
        List<Dept> results = deptService.getDept(deptCriteria);
        return success(results.size(), results);
    }

    @LogRecord("查询用户部门树")
    @GetMapping("tree")
    public RestFulResponse<TreeNodes> deptTree() {
        List<TreeNodes> results = deptService.getUserDeptTree();
        return success(results.size(), results);
    }

}
