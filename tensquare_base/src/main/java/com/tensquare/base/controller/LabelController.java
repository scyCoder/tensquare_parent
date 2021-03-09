package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: tensquare68
 * @description:
 **/
@RestController
@RequestMapping("label")
@CrossOrigin
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 保存标签数据
     * @param label
     * @return
            */
//    @RequestMapping(value = "", method = RequestMethod.POST)
    @PostMapping
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK, "保存成功");
    }

    /**
     * 修改标签
     * @param id
     * @param label
     * @return
     */
    @PutMapping("/{labelId}")
    public Result update(@PathVariable("labelId") String id, @RequestBody Label label){
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "更新成功");
    }

    /**
     * 删除
     * @param labelId
     * @return
     */
    @DeleteMapping("/{labelId}")
    public Result delete(@PathVariable String labelId){
        labelService.delete(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     *
     * @param labelId
     * @return
     */
    @GetMapping("/{labelId}")
    public Result findById(@PathVariable String labelId){
//        int i = 1/0;
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK, "查询成功", label);
    }

    /**
     * 查询全部
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Label> list = labelService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 条件查询
     * @param map 查询条件
     * @return
     */
    @PostMapping("/search")
    public Result search(@RequestBody Map map){
        List<Label> list = labelService.search(map);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 条件分页查询
     * @param page 当前页
     * @param size 页大小
     * @param map 查询条件
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result search(@PathVariable int page, @PathVariable int size, @RequestBody Map map){

        //Pageable封装当前页&页大小
        Pageable pageable = PageRequest.of(page-1, size);
        //Page对象封装总记录数&当前页记录
        Page<Label> pageData = labelService.search(map, pageable);
        PageResult pageResult = new PageResult(pageData.getTotalElements(), pageData.getContent());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

}
