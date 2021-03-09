package com.tensquare.qa.controller;
import com.tensquare.qa.clients.LabelClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

	@Autowired
	private ProblemService problemService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",problemService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",problemService.findSearch(searchMap));
    }

    @Autowired
	private HttpServletRequest request;
	
	/**
	 * 需求：发布文章，必须登陆（token有效），角色是普通用户“user”
	 * @param problem
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody Problem problem  ){
		Claims user_claims = (Claims) request.getAttribute("user_claims");
		if(user_claims!=null){
			//token有效 ，角色是普通用户
			problemService.add(problem);
			return new Result(true,StatusCode.OK,"增加成功");
		}
		return new Result(false,StatusCode.ACCESS_ERROR,"无权操作！");
	}
	
	/**
	 * 修改
	 * @param problem
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody Problem problem, @PathVariable String id ){
		problem.setId(id);
		problemService.update(problem);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		problemService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}

	/**
	 * 根据标签id查询最新问题列表
	 * @param labelId 标签id
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/newlist/{label}/{page}/{size}")
	public Result newList(@PathVariable("label") String labelId, @PathVariable int page, @PathVariable int size){
		Pageable pageable = PageRequest.of(page-1, size);
		Page pageData = problemService.findNewList(labelId,pageable);
		return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}



	/**
	 * 等待回答问题列表
	 * @param label
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/waitlist/{label}/{page}/{size}")
	public Result waitList(@PathVariable String label, @PathVariable int page, @PathVariable int size){
		Pageable pageable = PageRequest.of(page-1, size);
		Page pageData = problemService.findWaitList(label,pageable);
		return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageData.getTotalElements(), pageData.getContent()));
	}

	@Autowired
	private LabelClient client;

	/**
	 * 调用基础微服务中RestApi
	 * @param labelId
	 * @return
	 */
	@GetMapping("/label/{labelId}")
	public Result findLabelByFeign(@PathVariable String labelId){
		return client.findById(labelId);
	}
}
