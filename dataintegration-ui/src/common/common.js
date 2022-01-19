import {
  getProjectVo,
  getModelInfo,
  getDataSourceById
} from '../api/api.js'
import store from '../vuex/store.js';
import router from '../router';
import util from './utils.js'
import {compareObj} from "../assets/common/tool";


/**
 * 打开项目信息
 * @param datas
 */
export function openFlow(datas) {
  //判断项目是否是本人创建，在画布中保存项目时先判断是否是本人创建的如果不是点击保存时默认时新增一个项目
  let createFlag = true; //createFlag为true默认是本人创建
  if (datas.projectOnlineDTO === null) {
    createFlag = true;
  } else {
    //todo
    createFlag = true;
  }
  //判断节点是否是项目，是的话跳转到对应项目的探索页面
  if (datas.children === undefined) {
    let projectId = datas.projectId;
    let projectName = datas.projectName;
    // 双击获取项目信息
    let newPath = '/project/' + projectId;
    let name = datas.projectName;
    let createFlagData = {
      key: newPath,
      value: createFlag,
    }
    store.dispatch('setCreateFlag', JSON.stringify(createFlagData));
    store.dispatch('addRouteTitle', name);
    store.dispatch('deleteExecuteLog', newPath);
    store.dispatch('setFlowMainLoading', true);
    // 添加项目信息
    let param = {
      key: newPath,
      value: {
        projectId: projectId,
        projectName: projectName,
      }
    };
    store.dispatch('addProjects', param);
    // router.push({path:newPath,query:{id:projectId}});
    getProjectVo(projectId).then(res => {
      let {
        data
      } = res;
      if (data.code === '10000') {
        let content = data.content;
        let projectId = content.projectId;
        let projectFile = JSON.parse(content.projectFile);
        let nodeList = projectFile.transformation.nodeList;
        // 动态路由打开界面
        for (let i = 0; i < nodeList.nodeList.length; i++) {
          let map = {
            key: nodeList.nodeList[i].id,
            value: nodeList.nodeList[i].label,
          }
          store.dispatch('setMap', JSON.stringify(map));
        }
        let md5Data = {
          key: newPath,
          md5: content.md5,
        }
        store.dispatch('addProjectMd5', md5Data);
        //遍历取出过滤插件信息保存 label和id映射
        nodeList.nodeList.forEach((item, index) => {
          item.dataSourceId = '';
          item.errors = 0;
          item.linesInput = 0;
          item.linesOutput = 0;
          item.linesRead = 0;
          item.linesRejected = 0;
          item.linesUpdated = 0;
          item.linesWritten = 0;
          item.speed = 0;
          item.statusDescription = '';
          item.seconds = 0;
          item.stepExecutionStatu = '';
          item.stepExecutionStatus = '';
          item.dataQuerySql = '';
          item.status = false;
          item.stepMetaExecutionStatus = '';
        })
        // 添加项目节点到store里面
        let params = {
          key: newPath,
          value: nodeList,
        }
        for (let i = 0; i < nodeList.nodeList.length; i++) {
          let map = {
            key: nodeList.nodeList[i].id,
            value: nodeList.nodeList[i].label,
          }
          store.dispatch('setMap', JSON.stringify(map));
        }

        store.dispatch('addFlowData', params)

        // 拆分步骤信息并添加
        let steps = projectFile.transformation.step;
        if (steps === undefined) {

        }else{
          for (let i = 0; i < steps.length; i++) {
            let modelType = steps[i].modelType;
            // nodeList.nodeList.forEach((item, index) => {
            //   if (item.modelName !== undefined && steps[i].modelName !== undefined && item.modelName ===
            //     steps[i].modelName) {
            //     modelType = item.modelType;
            //   }
            // })
            //获取模型数据源信息
            if (modelType === undefined || modelType === 'SCJK') {
              let stepData = {
                key: newPath,
                step: steps[i],
              };
              store.dispatch('addSteps', stepData);
            } else {
              let requestParam = {
                modelName: steps[i].modelName,
                modelType: modelType
              }
              getModelInfo(requestParam).then(res => {
                steps[i].connection = res.data.content.datasourceName;
                let stepData = {
                  key: newPath,
                  step: steps[i],
                };
                store.dispatch('addSteps', stepData);
              })
            }
          }
        }

        // 拆分数据源信息并添加
        let lines = projectFile.transformation.order.hop;
        if (lines !== undefined) {
          for (let i = 0; i < lines.length; i++) {
            let lineData = {
              key: newPath,
              lines: lines[i],
            };
            store.dispatch('addLines', lineData);
          }
        }

        //拆分数据源信息
        let connection = projectFile.transformation.connection;
        // for (let i = 0; i < connection.length; i++) {
        //   let connectionData = {
        //     key: newPath,
        //     connection: connection[i],
        //   };
        //

        let connectData = {
          key: newPath,
          connection: connection
        }
        // store.dispatch('addConnection', connectData);

        // 添加项目执行文件
        projectFile.transformation.step = [];
        projectFile.transformation.order.hop = [];
        projectFile.transformation.connection = [];
        let parameter = {
          key: newPath,
          value: projectFile,
        }
        store.dispatch('addDataJson', parameter);
        store.dispatch('setFlowMainLoading', false);
        router.push(newPath);

        let connectionDatas = [];
        if(steps!==undefined && steps.length>0){
          steps.forEach(item => {
            if (item.connection !== '' && item.connection !== null && item.connection !== undefined) {
              getDataSourceById(item.connection).then(res => {
                let {
                  data
                } = res;
                if (data.code === '10000') {
                  let conn = spliceDataSource(data.content);
                  connectionDatas.push(conn);
                }
              })
            }
          })
        }

        let connectParams = {
          key: newPath,
          connection: connectionDatas,
        }
        store.dispatch('addConnection', connectParams)
        // util.$emit('changeLoadingFalse');
      }
    })
  }
}

export function createPro() {
  let count = 0;
  if (localStorage.getItem('count') === null) {
    count = 0;
  } else {
    count = localStorage.getItem('count');
  }
  count++;
  let itemList = {
    path: '',
    name: '数据集成' + count,
  };
  localStorage.setItem("count", count);
  let createFlag = true;
  let path = '/SJTS' + getRandomId();
  let newPath = '/project' + path;
  let name = '数据集成' + count;

  let createFlagData = {
    key: newPath,
    value: true,
  };
  store.dispatch('setCreateFlag', JSON.stringify(createFlagData));
  store.dispatch('setFlowMainLoading', false);
  store.dispatch('addRouteTitle', name);
  // this.$router.addRoutes(routers);
  let executeFlag = {
    key: path,
    value: false,
  };
  store.dispatch('setExecuFlag', executeFlag);
  router.push(newPath);
}


/**
 * 修改数据源结构
 * @param dataSource
 */
export function spliceDataSource(dataSource) {
  let connec = {
    name: null,
    server: "",
    type: "",
    access: "Native",
    database: dataSource.databse,
    port: dataSource.port,
    username: "",
    password: "",
    servername: "",
    data_tablespace: "",
    index_tablespace: "",
    attributes: {
      attribute: []
    }

  };
  connec.database = dataSource.databse;
  connec.port = dataSource.port;
  connec.server = dataSource.dbhost;
  connec.dataSourceName = dataSource.dsName;
  connec.name = dataSource.datasourceId;
  connec.dsName = dataSource.dsName;
  connec.dataSourceId = dataSource.datasourceId;
  connec.username = dataSource.dsUsername;
  connec.password = dataSource.dsPassword;
  connec.type = dataSource.dsType;

  let settings = JSON.parse(dataSource.dsConectorSetting);
  for (let key in settings) {
    let attribute = settings[key];
    if (attribute !== null) {
      attribute.forEach(item => {
        connec.attributes.attribute.push({
          code: item.code,
          attribute: item.attribute
        })
      })
    }

  }
  let paramsDTOS = dataSource.paramsDTOS;
  if (paramsDTOS !== null && paramsDTOS !== undefined) {
    paramsDTOS.forEach(sub => {
      connec.attributes.attribute.push({
        code: sub.key,
        attribute: sub.value
      })
    })
  }
  return connec;
}

/**
 * 获取随机码
 * @returns {string}
 */
export function getRandomId() {
  var s = [];
  var hexDigits = "0123456789abcdefhijklmnopqrstuzwxyz";
  for (var i = 0; i < 5; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }

  var uuid = s.join("");
  return uuid;
}


/**
 * 添加数据源信息
 */
export function addConnection(dataSources, dataSourceId, key) {
  getDataSourceById(dataSourceId).then(res=>{
    let dataSource = res.data.content;
    console.info("dataSource",dataSource);
    let connec = {
      name: null,
      server: "",
      type: "",
      access: "Native",
      database: dataSource.databse,
      port: dataSource.port,
      username: "",
      password: "",
      servername: "",
      data_tablespace: "",
      index_tablespace: "",
      attributes: {
        attribute: []
      }

    };
    connec.database = dataSource.databse;
    connec.port = dataSource.port;
    connec.server = dataSource.dbhost;
    connec.dataSourceName = dataSource.dsName;
    connec.name = dataSource.datasourceId;
    connec.dsName = dataSource.dsName;
    connec.dataSourceId = dataSource.datasourceId;
    connec.username = dataSource.dsUsername;
    connec.password = dataSource.dsPassword;
    connec.type = dataSource.dsType;

    let settings = JSON.parse(dataSource.dsConectorSetting);
    for (let key in settings) {
      let attribute = settings[key];
      if (attribute !== null) {
        attribute.forEach(item => {
          connec.attributes.attribute.push({
            code: item.code,
            attribute: item.attribute
          })
        })
      }

    }
    let paramsDTOS = dataSource.paramsDTOS;
    if (paramsDTOS !== null && paramsDTOS !== undefined) {
      paramsDTOS.forEach(sub => {
        connec.attributes.attribute.push({
          code: sub.key,
          attribute: sub.value
        })
      })
    }
    let connections = [];
    connections.push(connec)
    let connectionData = {
      key: key,
      connection: connections,
    };
    console.info("-=-",connectionData);
    store.dispatch('addConnection', connectionData)
  })
}

//拼装可以运行的报文
export function spliceDataJson(path, curStep) {
  let steps = JSON.parse(JSON.stringify(store.state.flowMain.stepList[path]));
  let lines = store.state.flowMain.lines[path];
  let dataJson = JSON.parse(JSON.stringify(store.state.flowMain.flowDataJson[path]));
  let tmpStep = [];
  let tmpLines = [];
  let nodeLists = store.state.flowMain.flowData[path].nodeList;
  nodeLists.forEach((item, index) => {
    if (steps !== undefined) {
      steps.forEach((step, i) => {
        if (step.name === item.label) {
          tmpStep.push(step);
        }
      })
    }
    if (lines !== undefined) {
      lines.forEach((line, j) => {
        if (line.to === item.label || line.form === item.label) {
          tmpLines.push(line);
        }
      })
    }
  });
  if (dataJson.transformation.info.parameters.parameter !== undefined) {
    dataJson.transformation.info.parameters.parameter.forEach((item, index) => {
      if (item.name === '' || item.default_value === '') {
        dataJson.transformation.info.parameters.parameter.splice(index, 1);
      }
    })
  } else if (dataJson.transformation.info.parameters === '') {
    dataJson.transformation.info.parameters = {
      parameter: []
    }
  }
  steps = tmpStep.length === 0 ? undefined : tmpStep;
  steps.push(curStep);
  lines = tmpLines;
  let connection = store.state.flowMain.connections[path];
  if (curStep.type === 'TableInput') {
    dataJson.transformation.order.hop = [];
    dataJson.transformation.step.push(curStep);
  } else {
    dataJson.transformation.order.hop = lines ? lines : [];
    dataJson.transformation.step = steps;
  }

  if (connection !== undefined) {
    let ss = dataJson.transformation.connection.concat(connection);
    let newConnection = new Set(ss);
    let connections = Array.from(newConnection);
    compareObj(connections);
    dataJson.transformation.connection = connections;
  } else {
    dataJson.transformation.connection = [];
  }
  return dataJson;
}

//表格数据重名
export function getChecktableName(arg, outFields, row, filed) {
  let ret = 1;
  let val = row[filed];
  console.log(outFields);
  outFields.forEach(item => {
    if (item.name === row[filed]) {
      // arg.push({
      // 	name:item.name+'_'+ret,
      // })
      row[filed] = row[filed] + '_' + ret;
      console.log(row[filed]);
      getChecktableName(arg, outFields, name, filed);
    }
  });
  arg.forEach(item => {
    if (item.name === row[filed]) {
      row[filed] = row[filed] + '_' + ret;
      getChecktableName(arg, outFields, name, filed);
    }
  })
}

/**
 * 更新控件状态
 * @param pluginData 控件数据
 * @param runSteps 运行步骤信息
 * @param content 运行步骤日志
 * @param datasourceId 数据源信息
 */
export function updatePluginState(pluginData, runStep, content, datasourceId) {
  pluginData.nodeList.forEach((item, index) => {
    if (item.label === runStep.stepName) {
      pluginData.nodeList[index].projectError = content.errors;
      pluginData.nodeList[index].dataSourceId = datasourceId;
      pluginData.nodeList[index].errors = runStep.errors;
      pluginData.nodeList[index].linesInput = runStep.linesInput;
      pluginData.nodeList[index].linesOutput = runStep.linesOutput;
      pluginData.nodeList[index].linesRead = runStep.linesRead;
      pluginData.nodeList[index].linesRejected = runStep.linesRejected;
      pluginData.nodeList[index].linesUpdated = runStep.linesUpdated;
      pluginData.nodeList[index].linesWritten = runStep.linesWritten;
      pluginData.nodeList[index].speed = runStep.speed;
      pluginData.nodeList[index].statusDescription = runStep.statusDescription;
      pluginData.nodeList[index].stepName = runStep.stepName;
      pluginData.nodeList[index].seconds = runStep.seconds;
      pluginData.nodeList[index].stepMetaExecutionStatus = runStep.stepMetaExecutionStatus === undefined ? '' : runStep.stepMetaExecutionStatus;
      pluginData.nodeList[index].stepExecutionStatus = runStep.stepExecutionStatus;
      pluginData.nodeList[index].dataQuerySql = runStep.dataQuerySql;
      if (runStep.stepExecutionStatus === 'STATUS_FINISHED') {
        pluginData.nodeList[index].status = false;
      }
    }
  })
}
