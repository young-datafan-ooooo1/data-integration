//去除重复项的方法
import {
  compareObj,
  spliceSteps,
  spliceProjectInfo,
  sliceLines
} from '../../assets/common/tool.js'

const flowMain = {
  state: {
    flowData: {}, //节点信息
    projectMd5: {},//项目MD5信息
    btnStatus: {},//按钮状态信息
    webData: {},//websocket运行信息
    runStps: {},//节点执行信息
    flowDataJson: {}, //运行项目信息
    stepList: {}, //画布上控件信息
    lines: {}, //画布上线信息
    connections: {}, //数据源连接信息
    projects: {}, //画布上项目信息
    projectsRun: {}, //项目运行信息
    executeShow: {}, //执行结果弹窗{key:'路径',value:{flag:true}}
    executeLogs: {}, //执行结果日志
    executeResult: {}, //执行结果日志(项目编号:日志)
    executeData: {}, //项目执行结果数据{path:'路径',data:{executeFlag(弹窗状态):false,dataPre('数据预览数据'):{},logs(日志信息):'',columnData:{}}}
    stepRunData: {},//项目运行的数据 用于数据预览
    preViewData: {},//项目预览数据
    runParam:{},//命名参数信息
  },
  mutations: {
    //保存命名参数信息
    ADD_RUN_PARAM:(state,data)=>{
      let key = data.key;
      state.runParam[key] = data.value;
    },

    ADD_PROJECT_MD5: (state, data) => {
      let key = data.key;
      state.projectMd5[key] = data.md5;
    },

    //保存项目运行数据
    ADD_STEP_RUN_DATA: (state, data) => {
      let key = data.key;
      state.stepRunData[key] = data.value;
    },


    /**
     * 项目运行:预览数据
     * @param state
     * @param data
     * @constructor
     */
    ADD_PREVIEW_DATA: (state, data) => {
      let key = data.key;
      let value = data.value;
      let preData = {
        [data.value.stepName]: data.value.data
      }
      let stepKey = key + '-' + value.stepName;
      state.preViewData[stepKey] = value.data;
    },

    //节点运行信息
    ADD_RUN_STEPS: (state, data) => {
      let key = data.key;
      state.runStps[key] = data.value;
    },
    ADD_BTN_STATUS: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.btnStatus[key] = value;
    },
    //websocket监听信息
    ADD_WEBSOCKET_DATA: (state, data) => {
      let key = data.key;
      state.webData[key] = data.value;
    },

    // 新增画布节点数据
    ADD_FLOW_DATA: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.flowData[key] = value;
    },

    //添加执行结果弹窗
    SET_EXECUFLAG: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.executeShow[key] = value;
    },

    //添加执行结果日志 data{path:logs}
    SET_EXECUTE_LOG: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.executeLogs[key] = value;
    },
    // 更新节点连接信息
    UPDATE_LINES: (state, data) => {
      let key = data.key;
      let lines = state.lines[key];
      let value = data.value;
      lines.forEach((item, index) => {
        if (item.fromId === value.fromId && item.toId === value.toId) {
          item.enabled = value.enabled;
        }
      });
      state.lines[key] = lines;
    },
    // 根据路径删除执行结果
    DELETE_EXECUTE_LOG: (state, data) => {
      let executeData = state.executeLogs;
      delete executeData[data];
      state.executeLogs = executeData;
    },

    // 添加项目信息 (data数据结构为:{key:'画布标志,这里为路由路径',value:{projectId:'',projectName:''})
    ADD_PROJECTS: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.projects[key] = value;
    },

    // 添加项目运行信息(data数据结构为:{projectId:'项目编号',executorId:'项目运行编号'})
    ADD_RUNNING_PROJECT: (state, data) => {
      let projectId = data.projectId;
      let executorId = data.executorId;
      state.projectsRun[projectId] = data.executorId;
    },

    // 新增krt数据
    ADD_FLOW_DATA_JSON: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.flowDataJson[key] = value;
    },
    // 新增数据源连接信息
    ADD_CONNECTION: (state, data) => {
      if (state.connections[data.key] === undefined) {
        let connections = [];
        state.connections[data.key] =[];
        connections = data.connection;
        state.connections[data.key] = connections;
      } else {
        if(state.connections[data.key].length ===0){
          state.connections[data.key] = data.connection;
        }else{
          if(state.connections[data.key].length>0){
            state.connections[data.key].forEach((item,index)=>{
              data.connection.forEach((item1,index1)=>{
                if(item.name === item1.name){
                  state.connections[data.key].splice(index,1);
                  state.connections[data.key].push(item1);
                }else{
                  state.connections[data.key].push(item1);
                }
              })
            })
          }else{
            state.connections[data.key] = data.connection;
          }

        }

      }
      compareObj(state.connections[data.key]);
    },
    // 新增画布上步骤信息
    ADD_STEPS: (state, data) => {
      if (state.stepList[data.key] == undefined) {
        let steps = [];
        steps.push(data.step);
        state.stepList[data.key] = steps;
      } else {
        state.stepList[data.key].push(data.step);
      }
      spliceSteps(state.stepList[data.key]);
    },
    // 新增画布上线信息
    ADD_LINES: (state, data) => {
      if (state.lines[data.key] === undefined) {
        let lines = [];
        lines.push(data.lines);
        state.lines[data.key] = lines;
      } else {
        state.lines[data.key].push(data.lines);
      }
      sliceLines(state.lines[data.key]);
    },
    // 修改步骤信息并修改对应的线的线信息 :data结构为{key'画布标识',value:{oldStepName:'',step:{}}},
    UPDATE_STEP_INFO: (state, data) => {
      let key = data.key;
      let value = data.value;
      let steps = state.stepList[key];
      steps.forEach((item, index) => {
        if (item.name === value.oldStepName) {
          steps.splice(index, 1);
        }
      });
      steps.push(value.step);
      state.stepList[key] = steps;
      spliceSteps(state.stepList[key]);
      return false;
    },

    // 删除线信息 data格式为{key:'画布标识',value:{fromId:'源节点编号',toId:'目标节点编号'}}
    DELETEE_LINE: (state, data) => {
      let fromId = data.value.fromId;
      let key = data.key
      let toId = data.value.toId;
      let lines = state.lines[key]
      lines.forEach((item, index) => {
        if (item.fromId === fromId && item.toId === toId) {
          lines.splice(index, 1);
        }
      })
      state.lines[key] = lines;
    },

    //页面初始化时清空step和线信息 data为画布标志
    CLEAR_NODE_LINE: (state, data) => {
      state.stepList[data] = {};
      state.lines[data] = {};
      state.connections = {};
    },

    // 修改节点后修改对应的线的信息 数据结构为{key:'画布标志',value:{nodeId:'节点id',nodeLable:'节点名称'}}
    UPDATE_LINE_NODE: (state, data) => {
      let key = data.key;
      let id = data.value.nodeId;
      let name = data.value.nodeLabel;
      let lines = state.lines[key];
      if (lines !== undefined) {
        lines.forEach((item, index) => {
          if (item.fromId === id) {
            item.from = name;
          } else if (item.toId === id) {
            item.to = name;
          }
        })
        state.lines[key] = lines;
      }
    },

    //新增画布上项目信息 数据结构为{key:'画布标志',project:{}项目信息}
    ADD_PEOJECT: (state, data) => {
      let key = data.key;
      let project = data.project;
      state.project[key] = project;
    },

    // 删除画布节点后 data格式为{key:'画布标识',value:'步骤名称'}
    DELETE_LINE_NODE: (state, data) => {
      let key = data.key;
      let name = data.value;
      let steps = state.stepList[key];

      steps.forEach((item, index) => {
        if (item.name === name) {
          steps.splice(index, 1);
        }
      });
      state.stepList[key] = steps;
    },
    // data格式为(key:项目编号,value:日志)
    SET_EXECUTE_RESULT: (state, data) => {
      let key = data.key;
      let value = data.value;
      state.executeResult[key] = value;
    }
  },

  actions: {

    addRunParam({commit},data){
      commit('ADD_RUN_PARAM',data)
    },

    addProjectMd5({
                    commit
                  }, data) {
      commit('ADD_PROJECT_MD5', data);
    },
    // 添加节点信息
    addFlowData({
                  commit
                }, data) {
      commit('ADD_FLOW_DATA', data);
    },
    addDataJson({
                  commit
                }, data) {
      // 判断路由是否已经存在
      //判断是否是弹窗路由 如果是不加到tags标签页
      commit('ADD_FLOW_DATA_JSON', data);
    },
    // 修改步骤信息
    updateStepInfo({
                     commit
                   }, data) {
      commit('UPDATE_STEP_INFO', data);
    },
    // 删除线信息
    deleteFlowLine({
                     commit
                   }, data) {
      commit('DELETEE_LINE', data);
    },
    updateFlowNodeLine({
                         commit
                       }, data) {
      commit('UPDATE_LINE_NODE', data);
    },
    // 删除节点
    deleteFlowNodeLine({
                         commit
                       }, data) {
      commit('DELETE_LINE_NODE', data);
    },
    //塞入步骤信息
    addSteps({
               commit
             }, data) {
      commit('ADD_STEPS', data);
    },
    // 新增线信息
    addLines({
               commit
             }, data) {
      commit('ADD_LINES', data);
    },
    clearNodeLine({
                    commit
                  }, data) {
      commit('CLEAR_NODE_LINE', data);
    },
    // 新增数据源信息
    addConnection({
                    commit
                  }, data) {
      commit('ADD_CONNECTION', data);
    },
    //新增画布上项目信息
    addProject({
                 commit
               }, data) {
      commit('ADD_PEOJECT', data)
    },
    // 添加项目信息
    addProjects({
                  commit
                }, data) {
      commit('ADD_PROJECTS', data);
    },

    // 添加项目运行信息
    addProjectRunning({
                        commit
                      }, data) {
      commit('ADD_RUNNING_PROJECT', data);
    },
    setExecuFlag({
                   commit
                 }, data) {
      commit('SET_EXECUFLAG', data);
    },
    //添加执行结果日志
    setExecuteLog({
                    commit
                  }, data) {
      commit('SET_EXECUTE_LOG', data);
    },
    //根据路径删除对应的执行结果信息
    deleteExecuteLog({
                       commit
                     }, data) {
      commit('DELETE_EXECUTE_LOG', data);
    },
    // 更改节点连接数据
    updateLine({
                 commit
               }, data) {
      commit('UPDATE_LINES', data);
    },

    setExecuteResult({
                       commit
                     }, data) {
      commit('SET_EXECUTE_RESULT', data);
    },
    setBtnStatus({commit}, data) {
      commit('ADD_BTN_STATUS', data);
    },

    addRunSteps({commit}, data) {
      commit('ADD_RUN_STEPS', data);
    },


    addWebData({commit}, data) {
      commit('ADD_WEBSOCKET_DATA', data);
    },

    addStepRunData({commit}, data) {
      commit(`ADD_STEP_RUN_DATA`, data);
    },

    addPreViewData({commit}, data) {
      commit('ADD_PREVIEW_DATA', data);
    }

  },
}

export default flowMain
