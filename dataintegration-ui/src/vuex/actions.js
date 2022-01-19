//test
import * as types from './types'
//中间处理
export const setToken = ({
  commit
}, data) => {
  commit(types.TOKEN, data);
}

export const setUserName = ({
  commit
}, data) => {
  commit(types.SETUSER, data);
}

export const setItem = ({
  commit
}, data) => {
  commit(types.SETITEM, data);
}

export const setContent = ({
  commit
}, data) => {
  commit(types.CONNECT, data);
}

export const setUserId = ({
  commit
}, data) => {
  commit(types.SETUSERID, data);
}

export const setNodes = ({
  commit
}, data) => {
  commit(types.SETNODES, data);
}

export const steps = ({
  commit
}, data) => {
  commit(types.STEPS, data);
}

export const upData = ({
  commit
}, data) => {
  commit(types.UPDATA, data);
}
export const setMap = ({
  commit
}, data) => {
  commit(types.SETMAP, data);
}
// 根据步骤名删除步骤信息
export const deleteStepByName = ({
  commit
}, data) => {
  commit(types.DELTESTEPBYNAME, data);
}

// 删除连线数据
export const deleteLine = ({
  commit
}, data) => {
  commit(types.DELETELINE, data);
}

// 删除节点后删除对应的信息
export const deleteNodeLine = ({
  commit
}, data) => {
  commit(types.DELETENODELINE, data);
}

// 修改节点后修改对应的信息
export const upDateNodeLine = ({
  commit
}, data) => {
  commit(types.UPDATENODELINE, data);
}

export const setSteps = ({
  commit
}, data) => {
  commit(types.SETSTEPS, data);
}

export const addRouteList = ({
  commit
}, data) => {
  commit(types.ADDROUTELIST, data);
}

export const addProjectInfo = ({
  commit
}, data) => {
  commit(types.ADDPROJECTINFO, data);
}

export const setFlowMainLoading = ({
  commit
}, data) => {
  commit(types.SETFLOWMAINLOADING, data);
}

export const addPluginMap = ({
  commit
}, data) => {
  commit(types.ADDPLUGINMAP, data);
}
// 添加登录参数
export const addLoginParam = ({
  commit
}, data) => {
  commit(types.ADDLOGINPARAM, data);
}

// 退出登录
export const logOut = ({
  commit
}, data) => {
  commit(types.LOGOUT, data);
}

// 添加token过期时间
export const addTokenTime = ({
  commit
}, data) => {
  commit(types.ADDTOKENTIME, data);
}

// 添加刷新token方法的标志
export const addTokenRefresh=({
  commit
},data)=>{
  commit(types.ADD_TOKEN_REFRESH);
}

// 添加路由标题
export const addRouteTitle=({
  commit
},data)=>{
  commit(types.ADD_ROUTE_TITLE,data);
}

// 设置登录账号
export const setLoginUserName = ({
  commit
}, data) => {
  commit(types.SET_LOGIN_USER_NAME, data);
}

// 设置登录密码
export const setLoginPassword = ({
  commit
}, data) => {
  commit(types.SET_LOGIN_PASSWORD, data);
}

//绑定画布路径和项目编号data数据结构为{key:'路径',value:'编号'}
export const setPathProject =({
  commit
},data)=>{
  commit(types.SET_PATH_PROJECT,data);
}

// 设置项目是否本人创建的标志
export const setCreateFlag =({
  commit
},data)=>{
  commit(types.SET_CREATE_FLAG,data);
}
export const setUrlConfig =({
                              commit
                            },data)=>{
  commit(types.SET_URL_CONFIG,data);
}

export const setResourceIds=({commit},data)=>{
  commit(types.SET_RESOURCE_ID,data)
}

// 存储菜单资源信息
export const setResources =({
                              commit
                            },data) =>{
  commit(types.SET_RESOURCES,data);
}

//存储插件id信息
export const setPluginds=({commit},data)=>{
  commit(types.SET_PLUGINIDS,data);
}

export const serDeleteProject=({commit},data)=>{
  commit(types.SET_DELETE_PROJECTS,data)
}

export const setWebSocket = ({commit},data)=>{
  commit(types.SET_WEBSOCKET,data);
}

