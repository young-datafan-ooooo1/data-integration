import axios from 'axios';
import store from '../vuex/store';
import router from '../router';
import cookies from 'vue-cookies'

import $ from 'jquery';
import qs from 'qs'
// let BASEOUTAPI = location.origin
// let BASE = location.origin + '/api'
const locationUrl = `${location.protocol}//${location.hostname}`
let BASEOUTAPI = `${locationUrl}:10200`
let BASE = `${locationUrl}:10200/api`
//websocket连接地址
let wsUrl = `ws://${location.hostname}:10200/ws/dataintegration-di-run-management-provider`

if (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === undefined) {
  BASE = 'http://192.168.10.160:10200/api'
  BASEOUTAPI = 'http://192.168.10.160:10200'
  wsUrl = `ws://192.168.10.160:10200/ws/dataintegration-di-run-management-provider`
} else if(process.env.NODE_ENV === 'production') {
  wsUrl = `ws://${locationUrl}:10200/ws/dataintegration-di-run-management-provider`
}

let projectUrl = `${BASE}/dataintegration-project-provider`;
//模型管理 dataintegration-model-provider
let modelManageUrl = `${BASE}/dataintegration-portal-model-management-provider`
//分组管理 dataintegration-group-provider
let groupUrl = `${BASE}/dataintegration-group-provider`
//系统管理 dataintegration-portal-system-management-provider
let systemUrl = `${BASE}/dataintegration-portal-system-management-provider`
//sso dataintegration-common-sso-provider
let ssoUrl = `${BASE}/dataintegration-common-sso-provider`
//文件管理 dataintegration-file-management-provider
let fileUrl = `${BASE}/dataintegration-file-management-provider`
//集成平台管理地址 dataintegration-di-run-management-provider
let dataDiscoveryUrl = `${BASE}/dataintegration-di-run-management-provider`
//websocket连接地址
let base = BASE;
const exceldownloadUrl = `${dataDiscoveryUrl}/preview/excelDownload?access_token=`
let downloadPro = `${projectUrl}/project/download?access_token=`

export {
  base,
  exceldownloadUrl,
  wsUrl,
  downloadPro,
}
let temp = 'http://10.242.10.109:10208'
axios.interceptors.request.use(
  config => {
    //排除登录方法
    if (config.url.indexOf("token") < 0) {
      if (cookies.get('token')) { // 判断是否存在token，如果存在的话，则每个http header都加上token
        config.headers.Authorization = cookies.get('token');
      }
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  });


// axios响应拦截器
axios.interceptors.response.use(
  response => {
    if (response.data.code !== undefined && response.data.code === '10007') {
      store.dispatch('logOut');
      router.replace({
        path: '/'
      });
    }
    return Promise.resolve(response);
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          store.dispatch('logOut');
          router.replace({
            path: '/'
          });
          break;
          // case 404:
          // 	router.replace({
          // 		path: '/404'
          // 	});
          // 	break;
          // case 500:
          // 	router.replace({
          // 		path: '/500'
          // 	});
          // 	break;

      }
    }
    return Promise.reject(error); //返回接口的错误信息
  },
);


//登出接口
export const loginOut = params => {
  return axios.get(`${ssoUrl}/oauth/logout`);
}
//查询已授权角色id
export const selectGrantedRoles = params => {
  return axios.get(`${systemUrl}/user/selectGrantedRoles`, params);
}

// 新增用户
export const addUser = params => {
  return axios.post(`${systemUrl}/user/add`, params);
}

// 重置用户密码
export const resetPass = params => {
  return axios.put(`${systemUrl}/user/resetPassword`, qs.stringify(params)).then(res =>
    res.data);
}
// 获取所有用户信息
export const getUser = params => {
  return axios.get(`${systemUrl}/user/selectUsersPage`, {
    params: params
  })
}

// 修改用户信息
export const updateUser = params => {
  return axios.put(`${systemUrl}/user/updateUser`, params);
}

// 删除用户
export const deleteUser = params => {
  return axios.delete(`${systemUrl}/user/deleteByUserIds`, {
    params: params
  });
}

// 角色赋权
export const grantUserRole = params => {
  return axios.post(`${systemUrl}/user/grantRoleToUser`, qs.stringify(params)).then(
    res => res.data);
}

//获取用户角色信息
export const getUserRole = params => {
  return axios.get(`${systemUrl}/user/selectGrantedRoles`, {
    params: params
  })
}

// 获取所有角色
export const getAllRoles = params => {
  return axios.get(`${systemUrl}/role/selectAllRoles`)
}

// 获取所有角色 分页
export const getAllRoleByPage = params => {
  return axios.get(`${systemUrl}/role/selectAllRolesPage`, {
    params: params
  })
}
// 添加角色
export const addRole = params => {
  return axios.post(`${systemUrl}/role/add`, params);
}
//修改角色
export const updateRole = params => {
  return axios.put(`${systemUrl}/role/update`, params)
}

// 菜单资源授权
export const grantRoleResources = params => {
  return axios.post(`${systemUrl}/role/grantResouces`, qs.stringify(params)).then(res =>
    res.data);
}

// 删除角色
export const deleteRoles = params => {
  return axios.delete(`${systemUrl}/role/deleteRoles`, {
    params: params
  });
}

// 获取已授权菜单资源
export const grantResources = params => {
  return axios.get(`${systemUrl}/resource/selectGrantedResources`, {
    params: params
  });
}

//获取所有菜单资源
export const getResources = params => {
  return axios.get(`${systemUrl}/resource/selectAllResource`, params);
}

//添加菜单资源
export const addResource = params => {
  return axios.post(`${systemUrl}/resource/add`, params);
}

// 修改资源信息
export const updateResource = params => {
  return axios.put(`${systemUrl}/resource/update`, params);
}


//分页获取日志数据

export const getLog = params => {
  return axios.get(`${systemUrl}/log/getLogsByReqTimePage`, {
    params: params
  });
}

// 检查资源名称
export const checkResourceName = params => {
  return axios.get(`${systemUrl}/resource/checkResourceName`, {
    params: params
  });
}

// 删除资源
export const deleteResource = params => {
  return axios.put(`${systemUrl}/resource/deleteResource`, qs.stringify(params)).then(
    res => res.data);
}

export const updatePassWord = params => {
  return axios.put(`${systemUrl}/user/updatePassword`, params)

}

// 分组管理
// 获取所有分组
export const getGroup = params => {
  return axios.get(`${groupUrl}/group/selectLikeBy/JCJB`, {
    params: params
  });
}

//查询分组是否存在
export const queryExistsGroup = params => {
  return axios.post(`${groupUrl}/group/queryGroupExists`, params)
}

// 根据分组类型获取分组信息
export const getGroups = params => {
  return axios.get(`${groupUrl}/group/selectBy/` + params.groupType, {
    params: params
  });
}

//新增分组
export const addGroup = params => {
  return axios.post(`${groupUrl}/group/add`, params)
}

//查询分组名称是否重复
export const checkGroupName = params => {
  return axios.post(`${groupUrl}/group/queryGroupExists`, params)
}

// 删除分组
export const deleteGroup = params => {
  return axios.delete(`${groupUrl}/group/delete/` + params);
}
// 批量删除分组
export const deleteGroups = params => {
  return axios.delete(`${groupUrl}/group/deleteBatch`, {
    params: {
      groupIds: params,
    },
    paramsSerializer: params => {
      return qs.stringify(params, {
        indices: false
      })
    }
  })
}
// 修改分组

export const updateGroup = params => {
  return axios.put(`${groupUrl}/group/update`, params);
}

// 根据分组查询分组下面的项目信息

export const selectProjectByGroupName = params => {
  return axios.get(`${projectUrl}/project/selectLikeByProjectName/` + params.groupId, {
    params: params
  });
}


// 项目管理模块

// 新增项目
export const addProject = params => {
  return axios.post(`${projectUrl}/project/add`, params);
}

// 根据项目名称模糊查询wode项目信息
export const selectProjectByProjectName = params => {
  return axios.get(`${projectUrl}/project/selectMyProject`, {
    params: params
  });
}
// 获取他人授予我的项目
export const selectGrantProject = params => {
  return axios.get(`${projectUrl}/project/selectGrantMyProject`, {
    params: params
  })
}
// 获取项目文件信息
export const getProjectVo = params => {
  return axios.get(`${projectUrl}/project/getProjectFile/` + params)
}
// 修改项目
export const updateProject = params => {
  return axios.put(`${projectUrl}/project/update`, params);
}
// 选择性修改项目
export const updateSelectProject = params => {
  return axios.put(`${projectUrl}/project/updateSelective`, params);
}
// 删除项目
export const deleteProject = params => {
  return axios.delete(`${projectUrl}/project/delete/` + params);
}

// 根据项目名称模糊查询项目信息
export const selectProjectAllProject = params => {
  return axios.get(`${projectUrl}/project/selectLikeByProjectName`, {
    params: params
  });
}

//根据分组和项目名称查询项目信息
export const selectAllProjectBygroupId = params => {
  if (params.groupId === '' || params.groupId === undefined) {
    return axios.get(`${projectUrl}/project/selectLikeByProjectName`, {
      params: params
    });
  } else {
    return axios.get(`${projectUrl}/project/selectLikeByProjectName/` + params.groupId, {
      params: params
    });
  }

}
// 批量删除项目信息
export const deletePrjects = params => {
  return axios.delete(`${projectUrl}/project/deleteBatch/`, {
    params: params
  });
}

// 获取项目已授权的用户
export const selectGrantProjectUser = params => {
  return axios.get(`${projectUrl}/projectUserGrant/selectUserGrant/` + params.projectId, {
    params: params
  });
}

// 项目授权
export const reGrant = params => {
  return axios.post(`${projectUrl}/projectUserGrant/reGrant`, params);
}

// 获取项目未授权的用户
export const selectNotGrantUser = params => {
  return axios.get(`${projectUrl}/projectUserGrant/selectNotGrantedUser/` + params.projectId,{params:params});
}

// 获取基本插件
export const getBasicPlugin = params => {
  return axios.get(`${base}/dataintegration-di-run-management-provider/pluginManage/getAllBasicPluginInfoByUserId`);
}
export const getBusinessPlugin = params => {
  return axios.get(`${base}/dataintegration-di-run-management-provider/pluginManage/getAllBasicPluginInfoByUserId`,{params:params})
}
// 获取数据模型
export const getAllBusinessModelPluginInfoByUserId = params =>{
  return axios.get(`${base}/dataintegration-di-run-management-provider/pluginManage/getAllBusinessModelPluginInfoByUserId`)
}

export const getCreateTableDDL = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/pluginManage/getCreateTableDDL`, params)
}

export const executeCreateDDL = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/pluginManage/executeCreateDDL`, params)
}

export const getFieldSumInfo = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/pluginManage/getFieldSumInfo`, params)
}

// 获取执行sql
export const getPreStepSql = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/pluginManage/getPreStepSql`, params);
}

//获取最大最小值
export const getFieldMaxMinInfo = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/pluginManage/getFieldMaxMinInfo`, params);
}

// 获取公式信息
export const getFuncDescribe = params => {
  return axios.get(`${base}/dataintegration-di-run-management-provider/pluginManage/getFuncDescribe`);
}

export const selectBusinessModel = params => {
  return axios.get(`${modelManageUrl}/businessModel/select/businessModel`, {
    params: params
  })
}
export const getDatasources = params => {
  return axios.get(`${modelManageUrl}/basicModel/getDatasources`)
}
export const getDatasourcesByUserName = params => {
  return axios.get(`${modelManageUrl}/datasource/selectAllNoPage`)
}
export const getSchema = params => {
  return axios.get(`${modelManageUrl}/basicModel/getSchema`, {
    params: params
  })
}
export const getTables = params => {
  return axios.get(`${modelManageUrl}/basicModel/getTables`, {
    params: params
  })
}

export const getColumns = params => {
  return axios.get(`${modelManageUrl}/basicModel/getColumns`, {
    params: params
  })
}

export const selectBusinessMetaData = params => {
  return axios.get(`${modelManageUrl}/businessModel/select/businessMetaData`, {
    params: params
  })
}

export const selectDatasource = params => {
  return axios.get(`${modelManageUrl}/businessModel/select/datasource`, {
    params: params
  })
}


// 数据预览
export const previewData = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/preview/previewData`, params);
}

// 字段统计
export const fieldStats = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/stats/fieldStats`, params);
}
//字段重复性检查

export const repeatedStats = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/stats/repeatedStats`, params);
}

export const repeatedQuery = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/stats/repeatedQuery`, params);
}
export const downloadFile = params => {
  axios.post(`${base}/dataintegration-di-run-management-provider/preview/excelDownload`, params, {
      responseType: 'blob' // 设置响应数据类型
    })
    .then(res => {
      if (res.status == 200) {
        let url = window.URL.createObjectURL(new Blob([res.data]))
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', params.fileName + '.xlsx') // 自定义下载文件名（如exemple.txt）
        document.body.appendChild(link)
        link.click()
      }
    })
}

//查询正在运行的项目
export const getStatus = params => {
  return axios.get(`${base}/dataintegration-di-run-management-provider/status/selectRunningProject`, params)
}
//查询运行历史

export const getStatusHistory = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/status/selectUserHistoryExecute?pageNum=` + params.pageNum +
    "&pageSize=" + params.pageSize, params.projectHistoryExecuteVO)
}

// 保存为模型
export const saveModel = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/saveModel/save`, params);
}
// 查询正在保存的模型
export const savingModel = params => {
  return axios.get(`${base}/dataintegration-di-run-management-provider/saveModel/selectRunning`, params);
}
// 查询保存模型的历史
export const modelHistory = params => {
  return axios.post(`${base}/dataintegration-di-run-management-provider/saveModel/selectHistory?pageNum=` + params.pageNum +
    "&pageSize=" + params.pageSize, params.saveModelVO);
}
// 查询自定义模型
export const getBasciModel = params => {
  return axios.get(`${modelManageUrl}/basicModel/select/customModel`, {
    params: params
  });
}

// 查询输出类模型信息
export const getOutModel = params => {
  return axios.get(`${modelManageUrl}/outModel/select/outModelAndMetaData`, {
    params: params
  });
}

//查询模型信息
export const getModelInfo = params => {
  return axios.get(`${modelManageUrl}/basicModel/select/modelsByModelName`, {
    params: params
  })
}

//项目上线
export const projectOnLine = params => {
  return axios.post(`${base}/dp-schedule-online-audit-provider/dpPortalProjectOnline`, params);
}

// 撤回项目上线
export const cancelProjectLine = params => {
  return axios.put(`${base}/dp-schedule-online-audit-provider/dpPortalProjectOnline`, params);
}

// 获取运行配置
export const getFrequency = params => {
  return axios.get(`${base}/dp-schedule-online-audit-provider/dpPortalFrequency`, {
    params: params
  });
}

// 获取审核用户
export const getAudingUser = params => {
  return axios.get(`${systemUrl}/user/selectAuditingUser`)
}

// 数据平台跳转接口
export const skipPlat = params => {
  return axios.post(`${ssoUrl}/oauth/authorize`, {
    params: params
  })
}

// 获取token
export const getToken = params => {
  return axios.post(`${ssoUrl}/oauth/token`, qs.stringify(params)).then(res =>
    res.data);
}

//续约接口
export default {
  refreshToken(params) {
    return axios.post(`${ssoUrl}/oauth/token`, qs.stringify(params)).then(res =>
      res.data);
  }
}
//高级跳转保存
export const saveAnalyzeData = params => {
  return axios.post(`${base}/dp-report-portal-provider/dpPortalReportRecord/save/dm`, params);
}

// 获取文件列表
export const selectFileListByType = params => {
  return axios.get(`${fileUrl}/fileOperation/selectFileListByType`, {
    params: params
  })
}
// 获取文件列表
export const getSheetList = params => {
  return axios.get(`${fileUrl}/fileContentDetailGetter/getSheetList`, {
    params: params
  })
}

// 获取头部信息
export const getCsvHeader = params => {
  return axios.get(`${fileUrl}/fileContentDetailGetter/fileHeader`, {
    params: params
  })
}

// 新增文件
export const addFileOperation = params => {
  return axios.post(`${fileUrl}/fileOperation/add`, params, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};
// 获取文件跟组
export const getFileGroup = params => {
  return axios.get(`${groupUrl}/group/selectLikeBy/WJ`, {
    params: params
  });
}

export const selectAllProject = params =>{
  return axios.get(`${projectUrl}/project/selectAllProject`,  {
    params: params
  })
}

//文件管理
//查看文件是否存在
export const checkFileName = params =>{
  return axios.post(`${fileUrl}/fileOperation/checkFileName`, qs.stringify(params)).then(res =>
    res.data);
}

// 修改文件
export const updateFileOperation = params => {
  return axios.put(`${fileUrl}/fileOperation/update`, params, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

//根据文件编号获取文件信息
export const getFileByFileId = params =>{
  return axios.get(`${fileUrl}/fileOperation/selectFileById`,{
		params:params})
}

//获取数据库连接配置
export const getDataSourceConfig = params =>{
  return axios.post(`${dataDiscoveryUrl}/database/dbTypeJson`)
}

//获取getJavaScriptFunctions
export const getJavaScriptFunctions = params =>{
  return axios.post(`${dataDiscoveryUrl}/javaScript/getJavaScriptFunctions`)
}

//获取数据库扩展信息
export const getStatement = params =>{
  return axios.get(`${modelManageUrl}/datasource/getStatement`,{params:params})
}

//获取数据库的扩展sql
export const getStatementSql = params =>{
  return axios.get(`${modelManageUrl}/datasource/getStatement_sql`,{params:params})
}

export const getMoreSheetList = params =>{
  return axios.post(`${fileUrl}/fileContentDetailGetter/getMoreSheetList`,params)
}


//测试数据库连接
export const testConnection = params =>{
  return axios.post(`${dataDiscoveryUrl}/database/testConnect`,params);
}

//获取工作表名称(根据步骤信息)
export const getSheetNameByStep = params =>{
  return axios.post(`${dataDiscoveryUrl}/excelinput/getSheetName`,params);
}

//excel文件输入 获取来自头部数据的字段
export const getSheetHeaderColumn = params =>{
  return axios.post(`${dataDiscoveryUrl}/excelinput/getSheetHeaderColumn`,params)
}

export const addConnection = params =>{
  return axios.post(`${modelManageUrl}/datasource/JCAdd`,params)
}
export const updateConnection = params =>{
  return axios.post(`${modelManageUrl}/datasource/JCUpdate`,params)
}
//获取数据库连接
export const getConnection = params =>{
  return axios.get(`${modelManageUrl}/datasource/getDatasourceNoPageBySourcePlat`,{params:params})
}

//表输入预览数据
export const previewTable = params =>{
  return axios.post(`${dataDiscoveryUrl}/database/previewData`,params)
}

//csv获取字段
export const getCsvHeaderColumn = params =>{
  return axios.post(`${dataDiscoveryUrl}/csvinput/getColumns`,params)
}

//txt获取字段
export const getTxtCsvHeaderColumn = params =>{
  return axios.post(`${dataDiscoveryUrl}/textfileinput/getColumns`,params)
}

//表输入获取字段
export const getTableInputFields = params =>{
  return axios.post(`${dataDiscoveryUrl}/database/getFieldInfo`,params)
}
//输入插件预览数据
export const executePreviewByFile = params =>{
  return axios.post(`${dataDiscoveryUrl}/previewExecutor/executePreviewByFile`,params)
}

//javaScript获取变量
export const getVariate = params =>{
  return axios.post(`${dataDiscoveryUrl}/javaScript/getVariate`,params);
}

//上传文件
export const uploadProject = params =>{
  return axios.post(`${projectUrl}/project/upload`,params,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
}

export const previewDataByFile = params =>{
  return axios.post(`${dataDiscoveryUrl}/preparedExecutor/executeByFile`,params)
}

//获取存储过程信息
export const getProcedure = params =>{
  return axios.post(`${dataDiscoveryUrl}/database/getProcedure`,qs.stringify(params)).then(
    res => res.data);
}

//运行SSH命令-测试连接
export const testSSHConnect = params =>{
  return axios.post(`${dataDiscoveryUrl}/ssh/testSSHConnect`,params)
}

//数据库查询获取字段
export const getTableColumns = params =>{
  return axios.post(`${dataDiscoveryUrl}/dblookup/getTableColumns?tableName=`+params.tableName+'&connectName='+params.connectName+"&schema="+params.schema,
    params.step);
}


//正则分组
export const groupCount = params =>{
  return axios.post(`${dataDiscoveryUrl}/regex/groupCount`, params, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });}
//正则匹配
export const ismatch = params =>{
  return axios.post(`${dataDiscoveryUrl}/regex/ismatch`, params, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });}

//新增文件夹
export const addDir = params=>{
  return axios.post(`${fileUrl}/fileOperation/addDir`,params,{
    headers: {
      'Content-Type': 'multipart/form-data'
    },
  })
}
//校验文件夹名称是否存在
export const checkDir = fileName =>{
  return axios.post(`${fileUrl}/fileOperation/checkDirName?fileName=${fileName}`).then(res =>
    res.data);
}

//修改文件夹
export const updateDir = params =>{
  return axios.put(`${fileUrl}/fileOperation/updateDir`,params)
}



// 删除文件
export const deleteFileOPeration = params => {
  return axios.delete(`${fileUrl}/fileOperation/delete`, {
    params: params
  })
}
/**
 * 根据数据源id获取数据源信息
 * @param params
 * @returns {Promise<AxiosResponse<any>>}
 */
export const getDataSourceById = params =>{
  return axios.get(`${modelManageUrl}/datasource/datasourceId/`+params);
}

/**
 * 获取所有文件夹
 * @param params
 * @returns {Promise<AxiosResponse<any>>}
 */
export const getAllFilFolderList = params =>{
  return axios.get(`${fileUrl}/fileOperation/selectFileFolderList`)
}

// 文件下载获取文件列表
export const getFileByFileFolder = params => {
  return axios.get(`${fileUrl}/fileOperation/getFolderFileList`, {
    params: params
  });
}

