package com.youngdatafan.dataintegration.file.management.service.impl;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.youngdatafan.dataintegration.file.management.dto.FileGrantDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderGrantDTO;
import com.youngdatafan.dataintegration.file.management.dto.FolderInfoDTO;
import com.youngdatafan.dataintegration.file.management.dto.GrantUserDTO;
import com.youngdatafan.dataintegration.file.management.dto.UserGrantDTO;
import com.youngdatafan.dataintegration.file.management.enums.GrantTypeEnum;
import com.youngdatafan.dataintegration.file.management.model.DpPortalFileUserGrant;
import com.youngdatafan.dataintegration.file.management.utils.BeanConvertUtil;
import com.youngdatafan.dataintegration.file.management.utils.PageInfoUtil;
import com.youngdatafan.dataintegration.file.management.vo.DpProtalFileUserGrantBatchAddVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantDeptVO;
import com.youngdatafan.dataintegration.file.management.vo.GrantFolderUserVO;
import com.youngdatafan.dataintegration.file.management.vo.UserGrantQueryVO;
import com.youngdatafan.dataintegration.file.management.service.DpPortalFileUserGrantService;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.youngdatafan.dataintegration.file.management.mapper.DpPortalFileUserGrantMapper;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantQueryVO;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantAddVO;
import com.youngdatafan.dataintegration.file.management.vo.DpPortalFileUserGrantUptVO;
import com.youngdatafan.dataintegration.file.management.dto.DpPortalFileUserGrantDTO;
import java.util.List;
import java.util.Set;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


/**
 * 文件管理表 业务逻辑.
 *
 * @author lizihao
 * @date 2021-12-27 14:53:37
 */
@Slf4j
@Service("dpPortalFileUserGrantService")
public class DpPortalFileUserGrantServiceImpl implements DpPortalFileUserGrantService {

    @Resource
    private DpPortalFileUserGrantMapper dpPortalFileUserGrantmapper;

    @Override
    public int insert(String userId, DpPortalFileUserGrantAddVO addVO) {

        DpPortalFileUserGrant addEntity = new DpPortalFileUserGrant();
        BeanUtils.copyProperties(addVO, addEntity);
        addEntity.setCreateUser(Integer.parseInt(userId));
        addEntity.setUpdateUser(Integer.parseInt(userId));
        Date date = new Date();
        addEntity.setCreateTime(date);
        addEntity.setUploadTime(date);
        return this.dpPortalFileUserGrantmapper.insert(addEntity);
    }

    @Override
    public int delete(Integer id) {
        return this.dpPortalFileUserGrantmapper.delete(id);
    }

    @Override
    public int batchDel(Set<Integer> ids) {
        return this.dpPortalFileUserGrantmapper.batchDel(ids);
    }

    @Override
    @Transactional
    public int update(String userId, DpPortalFileUserGrantUptVO updateVO) {

        List<FolderGrantDTO> folderGrantDTOS = updateVO.getFolderGrantDTOS();
        //删除调之间的授权关系，添加新的授权
        for (FolderGrantDTO item: folderGrantDTOS) {
            dpPortalFileUserGrantmapper.deleteByFolderId(item.getPid());
        }
        DpProtalFileUserGrantBatchAddVO batchAddVO = new DpProtalFileUserGrantBatchAddVO();
        BeanUtils.copyProperties(updateVO, batchAddVO);
        return this.batchInsert(batchAddVO, userId);
    }

    @Override
    public List<DpPortalFileUserGrantDTO> queryListByWrapper(DpPortalFileUserGrantQueryVO queryVo) {
        return this.dpPortalFileUserGrantmapper.queryListByWrapper(queryVo);
    }

    @Override
    public DpPortalFileUserGrantDTO getDetail(Integer id) {
        return this.dpPortalFileUserGrantmapper.getDetail(id);
    }

    @Override
    public PageInfo<DpPortalFileUserGrantDTO> queryPageByWrapper(DpPortalFileUserGrantQueryVO queryVo, Page page) {

        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<DpPortalFileUserGrantDTO> list = this.dpPortalFileUserGrantmapper.queryPageByWrapper(queryVo);
        // 根据条件获取结果
        PageInfo<DpPortalFileUserGrantDTO> pageInfo = new PageInfo<>(list);
        // 根据条件获取总数
        return PageInfoUtil.pageInfo2PageInfoDTO(pageInfo, DpPortalFileUserGrantDTO.class);
    }

    @Override
    public int deleteByUserId(Integer userId) {
        return this.dpPortalFileUserGrantmapper.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public int batchInsert(DpProtalFileUserGrantBatchAddVO batchAddVO, String createUser) {
        List<Integer> userIdList = batchAddVO.getUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return 0;
        }
        List<FolderGrantDTO> folderList = Optional.ofNullable(batchAddVO.getFolderGrantDTOS()).orElse(Lists.newArrayList());
        List<FileGrantDTO> fileList = Optional.ofNullable(batchAddVO.getFileGrantDTOS()).orElse(Lists.newArrayList());
        List<DpPortalFileUserGrantAddVO> dpPortalFileUserGrantAddVOS = Lists.newArrayList();
        for (Integer userId : userIdList) {
            for (FileGrantDTO file : fileList) {
                DpPortalFileUserGrantAddVO addVO = new DpPortalFileUserGrantAddVO();
                addVO.setUserId(userId);
                addVO.setFileId(file.getFileId());
                addVO.setGrantType(GrantTypeEnum.FILE.getCode());
                dpPortalFileUserGrantAddVOS.add(addVO);
            }

            for (FolderGrantDTO folder : folderList) {
                DpPortalFileUserGrantAddVO addVO = new DpPortalFileUserGrantAddVO();
                addVO.setUserId(userId);
                addVO.setPid(folder.getPid());
                addVO.setGrantType(GrantTypeEnum.FOLDER.getCode());
                dpPortalFileUserGrantAddVOS.add(addVO);
            }
        }
        return this.batchInsert(dpPortalFileUserGrantAddVOS, createUser);
    }

    /**
     * 批量新增授权.
     * @param dpPortalFileUserGrantAddVOS 授权列表
     * @param createUser 创建人
     * @return 返回数据
     */
    public int batchInsert(List<DpPortalFileUserGrantAddVO> dpPortalFileUserGrantAddVOS, String createUser) {
        List<DpPortalFileUserGrant> dpPortalFileUserGrants = dpPortalFileUserGrantAddVOS.stream().map(t -> {
            DpPortalFileUserGrant addEntity = new DpPortalFileUserGrant();
            BeanUtils.copyProperties(t, addEntity);
            addEntity.setCreateUser(Integer.parseInt(createUser));
            addEntity.setUpdateUser(Integer.parseInt(createUser));
            Date date = new Date();
            addEntity.setCreateTime(date);
            addEntity.setUploadTime(date);
            return addEntity;
        }).collect(Collectors.toList());
        return this.dpPortalFileUserGrantmapper.batchInsert(dpPortalFileUserGrants);
    }

    @Override
    public PageInfo<UserGrantDTO> queryUserGrant(UserGrantQueryVO queryVO, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<UserGrantDTO> userGrantDTOS = this.dpPortalFileUserGrantmapper.queryUserGrant(queryVO);
        // 根据条件获取结果
        PageInfo<DpPortalFileUserGrantDTO> pageInfo = new PageInfo(userGrantDTOS);
        // 根据条件获取总数
        return PageInfoUtil.pageInfo2PageInfoDTO(pageInfo, UserGrantDTO.class);
    }

    @Override
    public DpPortalFileUserGrantUptVO getUserGrant(Integer userId) {
        //查询文件授权
        List<FileGrantDTO> fileGrantDTOS = this.dpPortalFileUserGrantmapper.queryFileGrantByUserId(userId);
        //查询文件夹授权
        final List<FolderGrantDTO> folderGrantDTOS = this.dpPortalFileUserGrantmapper.queryFolderGrantByUserId(userId);

        DpPortalFileUserGrantUptVO dpPortalFileUserGrantUptVO = new DpPortalFileUserGrantUptVO();
        List<Integer> userIdList = Lists.newArrayList();
        userIdList.get(userId);
        dpPortalFileUserGrantUptVO.setUserIdList(userIdList);
        dpPortalFileUserGrantUptVO.setFileGrantDTOS(fileGrantDTOS);
        dpPortalFileUserGrantUptVO.setFolderGrantDTOS(folderGrantDTOS);

        return dpPortalFileUserGrantUptVO;
    }

    @Override
    public List<FolderInfoDTO> queryGrantFolder(String userId) {
        return this.dpPortalFileUserGrantmapper.queryGrantFolder(userId);
    }

    @Override
    public List<GrantDeptVO> queryGrantDeptFolder(String userId) {
        List<FolderInfoDTO> folderInfoDTOS = this.queryGrantFolder(userId);
        return BeanConvertUtil.convertFolderToTree(folderInfoDTOS);
    }

    @Override
    public GrantFolderUserVO queryGrantUser(String folderId) {
        List<GrantUserDTO> grantUserDTOS = this.dpPortalFileUserGrantmapper.queryGrantUser(folderId);
        List<GrantUserDTO> grantAllUserDTOS = this.dpPortalFileUserGrantmapper.queryAllUser();
        GrantFolderUserVO grantFolderUserVO = new GrantFolderUserVO();
        grantFolderUserVO.setAllUser(grantAllUserDTOS);
        grantFolderUserVO.setGrantUser(grantUserDTOS);
        return grantFolderUserVO;
    }

}

