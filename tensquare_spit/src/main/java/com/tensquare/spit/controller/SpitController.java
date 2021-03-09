package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: tensquare68
 * @description:
 **/
@RequestMapping("/spit")
@RestController
@CrossOrigin
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 吐槽
     * @param spit
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "吐槽成功");
    }

    /**
     * 查询单个吐槽记录
     * @param spitId
     * @return
     */
    @GetMapping("/{spitId}")
    public Result findById(@PathVariable String spitId){
        Spit spit = spitService.findById(spitId);
        return new Result(true, StatusCode.OK, "查询成功", spit);
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Spit> list = spitService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    /**
     * 删除
     * @param spitId
     * @return
     */
    @DeleteMapping("/{spitId}")
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 根据上级吐槽id查询评论
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result findByParendId(@PathVariable String parentid, @PathVariable int page, @PathVariable int size){
        Pageable pageable = PageRequest.of(page-1,size);
        Page<Spit> pageData = spitService.findByParendId(parentid, pageable);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Spit>(pageData.getTotalElements(), pageData.getContent()));
    }

    /**
     * 吐槽记录点赞
     * @param spitId
     * @return
     */
    @PutMapping("/thumbup/{spitId}")
    public Result updateThumbup(@PathVariable String spitId){
        spitService.updateThumbup(spitId);
        return new Result(true, StatusCode.OK, "点赞成功");
    }
}
