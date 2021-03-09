package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}

	@Autowired
	private HttpServletRequest request;
	
	/**
	 * 需求：必须是管理员角色才有权限删除普通用户
	 * 实现：获取前端提交token,解析token是否有效
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		/*//从reqeust请求对象中获取jwt
		String header = request.getHeader("Authorization");
		if(StringUtils.isNotBlank(header) && header.startsWith("Bearer ")){
			//获取token
			String token = header.substring(7);
			Claims claims = jwtUtil.parseJwt(token);
			if(claims!=null){
				String role = (String) claims.get("role");
				if("admin".equals(role)){
					userService.deleteById(id);
					return new Result(true,StatusCode.OK,"删除成功");
				}
			}
		}*/
		Claims admin_claims = (Claims) request.getAttribute("admin_claims");
		if(admin_claims!=null){
			//token有效，一定管理员角色
			userService.deleteById(id);
			return new Result(true,StatusCode.OK,"删除成功");
		}
		return new Result(true,StatusCode.ACCESS_ERROR,"您无权限操作！");
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * 发送短信验证码
	 * @param mobile
	 * @return
	 */
	@PostMapping("/sendsms/{mobile}")
	public Result sendCheckcode(@PathVariable String mobile){
		//生成随机验证码  将验证码存放redis中
		String checkcode = RandomStringUtils.randomNumeric(4);
		System.out.println("验证码："+checkcode);

		redisTemplate.opsForValue().set(mobile, checkcode, 5, TimeUnit.MINUTES);
		//调用rabbitTemplate对象向短信队列中发送消息
		Map<String, String> map = new HashMap<>();
		map.put("mobile", mobile);
		map.put("templateCode", "SMS_119087143");
		//模板参数格式为json
		Map<String, String> mapParmas = new HashMap<>();
		mapParmas.put("code", checkcode);
		//{code:1234}
		String tempateParam = JSON.toJSONString(mapParmas);
		map.put("tempateParam", tempateParam);
		rabbitTemplate.convertAndSend("", "tensquare-sms", map);
		return new Result(true, StatusCode.OK, "短信验证码已发送，请查收！");
	}

	/**
	 * 用户注册
	 * @param code 短信验证码
	 * @param user 用户信息
	 * @return
	 */
	@PostMapping("/register/{code}")
	public Result register(@PathVariable String code, @RequestBody User user){
		//获取正确验证码 判断是否一致
		String realCheckcode = (String) redisTemplate.opsForValue().get(user.getMobile());
		if(StringUtils.isNotBlank(realCheckcode) && realCheckcode.equals(code)){
			userService.add(user);
			//添加成功
			redisTemplate.delete(user.getMobile());
			return new Result(true, StatusCode.OK, "注册成功");
		}
		return new Result(false, StatusCode.ERROR, "注册失败");
	}

	@Autowired
	private JwtUtil jwtUtil;
	/**
	 * 用户登陆
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public Result login(@RequestBody User user){
		User loginUser = userService.login(user.getMobile(), user.getPassword());
		if(loginUser!=null){
			//用户登陆成功,签发token
			String token = jwtUtil.createJwt(loginUser.getId(), "user");
			Map<String, String> map = new HashMap<>();
			map.put("token", token);
			map.put("avatar", user.getAvatar());
			map.put("name", user.getNickname());
			return new Result(true, StatusCode.OK, "登陆成功", map);
		}
		return new Result(false, StatusCode.ERROR, "登陆失败");
	}

	/**
	 * 变革粉丝数方法
	 * @param userid 用户id
	 * @param x 变更数量
	 */
	@PutMapping("/incfans/{userid}/{x}")
	public void incFansCount(@PathVariable String userid, @PathVariable int x){
		userService.incFansCount(userid, x);
	}


	/**
	 * 变革关注数方法
	 * @param userid 用户id
	 * @param x 变更数量
	 */
	@PutMapping("/incfollow/{userid}/{x}")
	public void incFollowsount(@PathVariable String userid, @PathVariable int x){
		userService.incFollowsount(userid, x);
	}

}
