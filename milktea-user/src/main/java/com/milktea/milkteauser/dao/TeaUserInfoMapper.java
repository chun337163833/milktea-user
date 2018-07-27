package com.milktea.milkteauser.dao;

import com.milktea.milkteauser.domain.TeaUserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeaUserInfoMapper {
    int deleteByPrimaryKey(String userNo);

    int insert(TeaUserInfo record);

    int insertSelective(TeaUserInfo record);

    TeaUserInfo selectByPrimaryKey(String userNo);

    int updateByPrimaryKeySelective(TeaUserInfo record);

    int updateByPrimaryKey(TeaUserInfo record);
}