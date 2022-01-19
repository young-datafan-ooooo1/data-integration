import Vue from 'vue'
import Vuex from 'vuex'
import * as actions from './actions'
import * as getters from './getters'
import * as types from './types'
import tagsView from './modules/tagsView'
import flowMain from './modules/flowMain'

Vue.use(Vuex)

// 应用初始状态
const state = {
  token: sessionStorage.getItem("token"),
  loginParams: sessionStorage.getItem("loginParam"), //登录参数
  freshToken: sessionStorage.getItem("freshToken"), //刷新的token
  tokenTime: sessionStorage.getItem("tokenTime"), //token过期时间
  userName: localStorage.getItem("userName"),
  items: localStorage.getItem("items"),
  connect: localStorage.getItem("connect"),
  userId: localStorage.getItem("userId"),
  nodeName: localStorage.getItem("nodeName"),
  steps: localStorage.getItem("steps"),
  nodes: localStorage.getItem("nodes"),
  datas: localStorage.getItem("datas"),
  flowPath: localStorage.getItem("flowPath"), //打开画布的步骤信息
  dataMap: {}, //画布节点名称映射
  lastStep: localStorage.getItem("lastStep"), //上一步骤信息
  pathFlag: [],
  stepList: {},
  addRouterList: [],
  loginUserName: sessionStorage.getItem('loginUserName'), //用户登录账号
  loginPassword: sessionStorage.getItem('loginPassword'), //用户登录密码
  // 项目信息
  projectId: localStorage.getItem("projectId"), //项目编号
  exeutorId: localStorage.getItem("executorId"), //项目运行编号
  flowMainLoading: false, //项目加载标志
  pluginMap: {}, //pluginid-控件类型集合
  refreshLogin: true, //刷新token标志,使刷新token的方法只执行一次
  routeTilte: localStorage.getItem("routeTilte"),
  pathMap: {},
  createFlag:{},//项目是否是本人创建的标志
  urlConfig:sessionStorage.getItem("urlConfig"),//前端地址配置
  resoueceIds:sessionStorage.getItem('resourceIds'),//资源id
  resources: sessionStorage.getItem("resources"), //授权菜单
  pluginIds:sessionStorage.getItem('pluginIds'),//控件id
  deleteProject:[],//删除的项目信息
  webSocketTime:sessionStorage.getItem('time'),
}
// 定义所需的 mutations
const mutations = {

  [types.SET_WEBSOCKET]:(state,data)=>{
    sessionStorage.setItem('time',data);
    state.webSocketTime = data;
  },

  //控件id集合
  [types.SET_PLUGINIDS]:(state,data)=>{
    sessionStorage.setItem('pluginIds',data);
    state.pluginIds = data;
  },

  [types.SET_RESOURCES]: (state, data) => {
    sessionStorage.setItem("resources", data);
    state.resources = data;
  },

  //设置资源编号集合
  [types.SET_RESOURCE_ID]:(state,data)=>{
    sessionStorage.setItem('resourceIds',JSON.stringify(data));
    state.resoueceIds = JSON.stringify(data);
  },

  //获取前端地址配置
  [types.SET_URL_CONFIG]:(state,data)=>{
    sessionStorage.setItem('urlConfig',JSON.stringify(data));
    state.urlConfig = JSON.stringify(data);
  },

  // 设置是否本人创建项目的标志
  [types.SET_CREATE_FLAG]:(state,data)=>{
    let json = JSON.parse(data);
    state.createFlag[json.key]=json.value;
  },

  [types.SET_PATH_PROJECT]: (state, data) => {
    let key = data.key;
    let value = data.value;
    state.pathMap[key] = value;
  },

  // 设置用户账号
  [types.SET_LOGIN_USER_NAME]: (state, data) => {
    sessionStorage.setItem("loginUserName", data);
    state.loginUserName = data;
  },

  // 设置用户密码
  [types.SET_LOGIN_PASSWORD]: (state, data) => {
    sessionStorage.setItem('loginPassword', data);
    state.loginPassword = data;
  },
  // 添加路由标题
  [types.ADD_ROUTE_TITLE]: (state, data) => {
    localStorage.setItem("routeTilte", data);
    state.routeTilte = data;
  },

  [types.ADD_TOKEN_REFRESH]: (state, data) => {
    state.refreshLogin = data;
  },

  // 添加token过期时间
  [types.ADDTOKENTIME]: (state, data) => {
    sessionStorage.setItem("tokenTime", data);
    state.tokenTime = data;
  },

  // 添加登录参数
  [types.ADDLOGINPARAM]: (state, data) => {
    sessionStorage.setItem("loginParam", JSON.stringify(data));
    state.loginParams = JSON.stringify(data);
  },

  // 添加pluginId:控件类型映射
  [types.ADDPLUGINMAP]: (state, data) => {
    state.pluginMap[data.pluginId] = data.pluginType;
  },

  //设置画布加载标志
  [types.SETFLOWMAINLOADING]: (state, data) => {
    state.flowMainLoading = data;
  },

  // 增加项目信息
  [types.ADDPROJECTINFO]: (state, data) => {
    localStorage.setItem("projectId", data.projectId);
    localStorage.setItem("executorId", data.executorId);
    state.projectId = data.projectId;
    state.exeutorId = data.executorId;
  },

  [types.ADDROUTELIST]: (state, data) => {
    if (state.addRouterList.length > 0) {
      for (let i = 0; i < state.addRouterList.length; i++) {
        if (state.addRouterList[i].path !== data.path) {
          state.addRouterList.push(data);
        }
      }
    } else {
      state.addRouterList.push(data);
    }
  },
  //设置画布数据
  [types.SETNODES]: (state, data) => {
    localStorage.setItem("nodes", data);
    state.nodes = data;
  },

  [types.SETUSERID]: (state, data) => {
    localStorage.setItem("userId", data);
    state.userId = data;
  },

  [types.TOKEN]: (state, data) => {
    sessionStorage.setItem("token", data);
    state.token = data;
  },

  [types.SETUSER]: (state, data) => {
    localStorage.setItem("userName", data);
    state.userName = data;
  },

  [types.SETNODENAME]: (state, data) => {
    localStorage.setItem("nodeName")
  },

  [types.SETITEM]: (state, data) => {
    localStorage.setItem("items", data);
    state.items = data;
  },
  [types.LOGOUT]: (state) => {
    sessionStorage.setItem('token', null);
    sessionStorage.clear();
    localStorage.clear();
    state.token = null;
    state.userName = null;
    state.tagsView.visitedViews = [];
  },

  [types.CONNECT]: (state, data) => {
    localStorage.setItem("connect", data);
    state.connect = data;
  },

  [types.STEPS]: (state, data) => {
    localStorage.setItem("steps", data);
    state.steps = data;
  },

  [types.SETDATA]: (state, data) => {
    localStorage.setItem("steps", data);
    state.datas = data;
  },

  [types.UPDATA]: (state, data) => {
    let step = JSON.parse(data);
    let trans = JSON.parse(state.nodes);
    let steps = trans.transformation.step;
    steps.forEach((item, index) => {
      if (item.name === step.oldStepName) {
        steps.splice(index, 1);
      }
    });

    steps.push(step);
    trans.transformation.step = steps;
    localStorage.setItem('nodes', trans);
    state.nodes = JSON.stringify(trans);
  },


  // 创建值映射
  [types.SETMAP]: (state, data) => {
    let map = JSON.parse(data);
    let datas = {};
    let key = map.key;
    state.dataMap[key] = map.value;
    //
  },

  // 根据步骤名称删除步骤信息
  [types.DELTESTEPBYNAME]: (state, data) => {
    let steps = JSON.parse(state.nodes).transformation.step;
    steps.forEach((item, index) => {
      if (item.name === data) {
        steps.splice(index, 1);
      }
    })
    let nodes = JSON.parse(state.nodes);
    nodes.transformation.step = steps;
    localStorage.setItem('nodes', JSON.stringify(nodes));
    state.nodes = JSON.stringify(nodes);
  },

  // 删除连线数据
  [types.DELETELINE]: (state, data) => {
    let fromId = data.fromId;
    let toId = data.toId;
    let lines = JSON.parse(state.nodes).transformation.order.hop
    lines = lines.filter(function(line) {
      return line.fromId !== fromId && line.toId !== toId
    });
    let nodes = JSON.parse(state.nodes);
    nodes.transformation.order.hop = lines;
    localStorage.setItem('nodes', nodes);
    state.nodes = JSON.stringify(nodes);

  },
  // 删除节点后删除对应的线信息
  [types.DELETELINE]: (state, data) => {
    let fromId = data.fromId;
    let toId = data.toId;
    let lines = JSON.parse(state.nodes).transformation.order.hop
    lines = lines.filter(function(line) {
      return line.fromId !== fromId || line.toId !== toId
    });
    let nodes = JSON.parse(state.nodes);
    nodes.transformation.order.hop = lines;
    localStorage.setItem('nodes', nodes);
    state.nodes = JSON.stringify(nodes);

  },
  // 修改节点后修改对应的线信息
  [types.UPDATENODELINE]: (state, data) => {
    let lines = JSON.parse(state.nodes).transformation.order.hop;
    lines.forEach((item, index) => {
      if (item.fromId === data.key) {
        item.from = data.value;
      } else if (item.toId === data.key) {
        item.to = data.value;
      }
    })
    let nodes = JSON.parse(state.nodes);
    nodes.transformation.order.hop = lines;
    localStorage.setItem('nodes', nodes);
    state.nodes = JSON.stringify(nodes);
  },

  // 存放临时路由参数
  [types.SETPATH]: (state, data) => {
    state.pathFlag.push(data);
  },

  // 打开新的界面是初始化stepsList
  [types.CLEARSTEPS]: (state) => {
    state.stepList = [];
  },
  //删除的项目
  [types.SET_DELETE_PROJECTS]:(state,data)=>{
    data.forEach((item)=>{
      state.deleteProject.push(item);
    })
  },

}

// 创建 store 实例
export default new Vuex.Store({
  modules: {
    tagsView,
    flowMain
  },
  actions,
  getters,
  state,
  mutations
})
