package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    //推荐职位列表  查询前四条记录；状态为2；根据创建日期排序
    //Top: limit 0,4
    public List<Recruit> findTop4ByStateOrderByCreatetimeDesc(String state);

    //最值职位列表 查询12条记录；状态不为0 根据创建日期排序
    public List<Recruit> findTop12ByStateNotOrderByCreatetimeDesc(String state);

}
