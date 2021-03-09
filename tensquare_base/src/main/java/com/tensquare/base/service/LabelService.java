package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: tensquare68
 * @description:
 **/
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Label label) {
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label) {
        labelDao.save(label);
    }

    public void delete(String labelId) {
        labelDao.deleteById(labelId);
    }

    public Label findById(String labelId) {
        return labelDao.findById(labelId).get();
    }

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 条件查询
     * @param map
     * @return
     */
    public List<Label> search(Map map) {
        return labelDao.findAll(getSpecification(map));
    }

    /**
     * 条件分页查询
     * @param map
     * @param pageable
     * @return
     */
    public Page<Label> search(Map map, Pageable pageable) {
        return labelDao.findAll(getSpecification(map), pageable);
    }

    /**
     * 提取方法：根据map中查询条件封装Specification
     * 目的：通过java代码动态设置最终执行sql语句
     */

    public Specification<Label> getSpecification(Map map){
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                criteriaBuilder
                List<Predicate> list = new ArrayList<>();
                String labelname = (String) map.get("labelname");
                if(StringUtils.isNotBlank(labelname)){
                    //sql = labelname like %?%
                    //参数一：查询实体中属性名称  参数二：查询值
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%"+labelname+"%");
                    list.add(p1);
                }
                String state = (String) map.get("state");
                if(StringUtils.isNotBlank(state)){
                    //sql+= state = ?
                    Predicate p2 = criteriaBuilder.equal(root.get("state").as(String.class), state);
                    list.add(p2);
                }
                //组合条件 采用and  or
//                Predicate[] predicates = new Predicate[list.size()];
                //将list转为数组
//                predicates = list.toArray(predicates);
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
    }
}
