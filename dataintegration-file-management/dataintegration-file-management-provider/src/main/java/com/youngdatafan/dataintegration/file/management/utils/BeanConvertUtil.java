package com.youngdatafan.dataintegration.file.management.utils;

import com.google.common.collect.Lists;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptUserFolderVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptUserVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptVO;
import java.util.List;
import java.util.Optional;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

@ApiModel("对象转换工具类")
public class BeanConvertUtil {

    /**
     * 将文件夹信息转换成三级树结构.
     * @param folderInfoDTOS 文件夹信息.
     * @return 文件夹信息树结构
     */
    public static List<GrantDeptVO> convertFolderToTree(List<FolderInfoDTO> folderInfoDTOS) {
        List<GrantDeptVO> deptVOList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(folderInfoDTOS)) {
            return deptVOList;
        }
        for (FolderInfoDTO item : folderInfoDTOS) {
            GrantDeptVO grantDeptVO = null;
            Integer deptId = Optional.ofNullable(item.getDeptId()).orElse(-1);
            String deptName = Optional.ofNullable(item.getDeptName()).orElse("未分配部门");
            item.setDeptId(deptId);
            item.setDeptName(deptName);
            for (GrantDeptVO dept : deptVOList) {
                if (dept.getDeptId().equals(item.getDeptId())) {
                    grantDeptVO = dept;
                    break;
                }
            }
            if (grantDeptVO == null) {
                grantDeptVO = new GrantDeptVO();
                grantDeptVO.setDeptId(item.getDeptId());
                grantDeptVO.setDeptName(item.getDeptName());
                deptVOList.add(grantDeptVO);
            }
            List<GrantDeptUserVO> userList = grantDeptVO.getUserList();
            if (CollectionUtils.isEmpty(userList)) {
                userList = Lists.newArrayList();
                grantDeptVO.setUserList(userList);
            }
            GrantDeptUserVO userVO = null;
            for (GrantDeptUserVO userVOItem : userList) {
                if (userVOItem.getUserId().equals(item.getUserId())) {
                    userVO = userVOItem;
                    break;
                }
            }
            if (userVO == null) {
                userVO = new GrantDeptUserVO();
                userVO.setUserId(item.getUserId());
                if (StringUtils.isBlank(item.getUserName()) && StringUtils.isBlank(item.getAccount())) {
                    userVO.setAccount(item.getAccountOld());
                    userVO.setUserName("用户已删除(" + item.getAccountOld() + ")");
                } else {
                    userVO.setAccount(item.getAccount());
                    userVO.setUserName(item.getUserName());
                }
                userList.add(userVO);
            }
            List<GrantDeptUserFolderVO> grantDeptUserFolderVOList = userVO.getGrantDeptUserFolderVOList();
            if (CollectionUtils.isEmpty(grantDeptUserFolderVOList)) {
                grantDeptUserFolderVOList = Lists.newArrayList();
                userVO.setGrantDeptUserFolderVOList(grantDeptUserFolderVOList);
            }
            GrantDeptUserFolderVO grantDeptUserFolderVO = new GrantDeptUserFolderVO();
            grantDeptUserFolderVO.setFolderId(item.getFolderId());
            grantDeptUserFolderVO.setFolderName(item.getFolderName());
            grantDeptUserFolderVOList.add(grantDeptUserFolderVO);
        }
        return deptVOList;
    }
}
