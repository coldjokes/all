package com.cnbaosi.cabinet.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cnbaosi.cabinet.entity.modal.User;

/**
 *  
 * 
 * @author Yifeng Wang  
 */
public interface UserMapper extends BaseMapper<User> {

	List<String> getEmailList();

}

