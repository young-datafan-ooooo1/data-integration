package com.youngdatafan.portal.model.management.outmodel.service;

import com.youngdatafan.portal.model.management.common.entity.ModelDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataListDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelAndMetaDataWithPageDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelGroupDTO;
import com.youngdatafan.portal.model.management.outmodel.dto.OutModelProjectIdAndNamesDTO;
import com.youngdatafan.portal.model.management.outmodel.vo.AddOutModelVO;
import com.youngdatafan.portal.model.management.outmodel.vo.UpdateOutModelVO;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <ul style="margin:15px;">
 * <li>Description : 模型授权组接口</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2020/2/12 11:06 PM</li>
 * <li>Author      : ksice_xt</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public interface OutModelService {


    Boolean delete(String userId, List<String> outModelId);

    String add(String userId, AddOutModelVO addOutModelVO);

    List<OutModelGroupDTO> getGroups(String userId);

    OutModelAndMetaDataWithPageDTO selectAllOutModel(String userId, Integer curPage,
                                                     Integer pageSize, String groupId, String projectId,
                                                     String modelName, String enabled);


    List<OutModelProjectIdAndNamesDTO> getAllProject(String userId);


    OutModelAndMetaDataListDTO selectOutModelAndMetaData(String userId, String modelName);

    ModelDTO selectModel(String userId, String modelName);


    Boolean updateAll(String userId, UpdateOutModelVO updateOutModelVO);

    Boolean updateSelective(String userId, UpdateOutModelVO updateOutModelVO);

    Boolean outModelIsExist(String userId, String modelId);
}
