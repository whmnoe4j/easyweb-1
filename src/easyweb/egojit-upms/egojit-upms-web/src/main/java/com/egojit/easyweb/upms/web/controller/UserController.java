package com.egojit.easyweb.upms.web.controller;

import com.alibaba.fastjson.JSON;
import com.egojit.easyweb.common.base.BaseResult;
import com.egojit.easyweb.common.base.BaseResultCode;
import com.egojit.easyweb.common.base.BaseWebController;
import com.egojit.easyweb.common.base.Page;
import com.egojit.easyweb.common.utils.MD5Util;
import com.egojit.easyweb.common.utils.StringUtils;
import com.egojit.easyweb.upm.service.SysUserService;
import com.egojit.easyweb.upms.common.utils.UserUtils;
import com.egojit.easyweb.upms.model.SysUser;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by egojit on 2017/10/29.
 */
@Controller
@RequestMapping("/admin/user")
@Api(value = "用户管理", description = "用户管理")
public class UserController extends BaseWebController {

    @Autowired
    private SysUserService userService;

    @RequestMapping("/index")
    @ApiOperation(value = "用户管理首页")
    public String index(){
        return "/user/index";
    }

    @ResponseBody
    @PostMapping("/index")
    @ApiOperation(value = "用户管理接口")
    public Page<SysUser> index(HttpServletRequest request,
                               HttpServletResponse response,SysUser user){
        Page<SysUser> pg=new Page<SysUser>(request,response);
        Example example=new Example(SysUser.class);
        Example.Criteria criteria= example.createCriteria();
        if(!StringUtils.isEmpty(user.getLoginName())) {
            criteria.andLike("loginName", "%"+user.getLoginName()+"%");
        }
        if(!StringUtils.isEmpty(user.getName())) {
            criteria.andLike("name", "%"+user.getName()+"%");
        }
        pg= userService.selectPageByExample(example,pg);
        return pg;
    }

    @ResponseBody
    @PostMapping("/delete")
    @ApiOperation(value = "用户管理接口")
    public BaseResult delete(String ids){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"删除成功");
        List<String> idList= JSON.parseArray(ids,String.class);
        int count= userService.deleteByIds(idList);
        _log.info("删除了："+count+"数据");
        return result;
    }

    @RequestMapping("/edit")
    @ApiOperation(value = "用户添加界面")
    public String add(){
        return "/user/edit";
    }

    @ApiOperation(value = "用户添加界面")
    @PostMapping("/edit")
    @ResponseBody
    public BaseResult edit(SysUser user){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser curentUser= UserUtils.getUser();
        if(StringUtils.isEmpty(user.getId())){
            user.setCreateBy(curentUser.getId());
            user.setUpdateBy(curentUser.getId());
            String ecPwd= MD5Util.shiroPwd(user.getPassword(),user.getLoginName());
            user.setPassword(ecPwd);
            userService.insert(user);
        }else {
            user.setUpdateBy(curentUser.getId());
            userService.updateByPrimaryKeySelective(user);
        }
        return result;
    }

    @ApiOperation(value = "用户详情")
    @PostMapping("/detail")
    @ResponseBody
    public BaseResult detail(String id){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
        SysUser model=  userService.selectByPrimaryKey(id);
        result.setData(model);
        return result;
    }

    @RequestMapping("/power")
    @ApiOperation(value = "用户管理-权限设置界面")
    public String power(){
        return "/user/power";
    }

    @ApiOperation(value = "用户管理-权限设置接口")
    @PostMapping("/power")
    @ResponseBody
    public BaseResult power(String roleId,String menusIds){
        BaseResult result=new BaseResult(BaseResultCode.SUCCESS,"成功");
//        service.setPower(roleId,menusIds);
        return result;
    }
}
