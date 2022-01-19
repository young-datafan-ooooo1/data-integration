
export const LOGIN = 'login';

export const TOKEN = 'setToken';

export const LOGOUT = 'logOut';

export const SETUSER ='setUserName'


export const SETITEM = "setItem";

export const CONNECT='setContent';

export const SETUSERID='setUserId';

export const SETNODENAME='setNodeName';

export const STEPS='steps'

// 设置画布数据
export const SETNODES ='setNodes';

export const SETDATA = 'setDatas';

export const UPDATA = 'upData';


export const GETLASTNODE='getLastNode';

// 生成临时映射表
export const SETMAP = 'setMap'

// 根据步骤名称删除步骤信息
export const DELTESTEPBYNAME = 'deleteStepByName'

// 删除连线数据
export const DELETELINE = 'deleteLine'

// 删除节点后删除对应的线信息
export const DELETENODELINE = 'deleteNodeLine'

// 修改节点名称修改对应的线信息
export const UPDATENODELINE = 'upDateNodeLine'

// 存放临时路由参数 做数据隔离
export const SETPATH  ='setPath'

// 存放步骤信息列表
export const SETSTEPS= 'setSteps';

// 清除步骤信息
export const CLEARSTEPS='clearSteps';

// 增加路由信息
export const ADDROUTELIST = "addRouteList";

// 增加项目信息
export const ADDPROJECTINFO="addProjectInfo";

// 增加画布加载标志
export const SETFLOWMAINLOADING="setFlowMainLoading";

// 添加pluginId:控件类型映射
export const ADDPLUGINMAP = "addPluginMap";
// 添加登录参数
export const ADDLOGINPARAM = "addLoginParam";

// 添加token过期时间
export const ADDTOKENTIME="addTokenTime";

// 添加刷新token的请求标志
export const ADD_TOKEN_REFRESH = "addTokenRefresh";

// 添加路由标题名称
export const ADD_ROUTE_TITLE = 'addRouteTitle';

// 设置用户账号
export const SET_LOGIN_USER_NAME ='setLoginUserName';

// 设置登录密码
export const SET_LOGIN_PASSWORD='setLoginPassword';

// 绑定画布路径-项目编号
export const SET_PATH_PROJECT='setPathProject';

// 设置项目是否本人创建的标志
export const SET_CREATE_FLAG='setCreateFlag';

//设置前端地址缓存
export const SET_URL_CONFIG = 'setUrlConfig';

//设置所有的资源编号
export const SET_RESOURCE_ID = 'setResourceIds';

// 添加菜单资源信息
export const SET_RESOURCES = "setResources";

//添加控件id信息
export const SET_PLUGINIDS = "setPluginIds";
//删除的项目id
export const SET_DELETE_PROJECTS = 'serDeleteProject';


export const SET_WEBSOCKET = 'setWebSocket'
