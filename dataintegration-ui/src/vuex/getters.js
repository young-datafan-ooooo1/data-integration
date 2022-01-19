import router from '../router';
import {
	renameNode
} from '../assets/common/tool.js'

const whiteList = ['/login', '/bankSystem', '/404', '/500', '/403', '/detailData', '/commonParams',
	'/offLineInvokeList', '/loanReportQuery', '/setTimeLiness', '/selectCycle', '/PortInstituSummaryExpense',
	'/PortUserSummaryExpense', '/InstituSummaryExpense', '/test'
];
//获取token
export const getToken = state => {
	return state.token
};

export const getUserName = state => {
	return state.userName;
};

export const getCurrentItem = state => {
	return state.items;
};

export const getConnect = state => {
	return state.connect;
};

export const getSteps = state => {
	return state.stepList;
};

export const getUserId = state => {
	return state.userId;
};

// 获取画布数据
export const getNodes = state => {
	return state.nodes;
};

// 获取模型数据
export const getModelData = state => {
	return state.datas;
};

export const getStepByName = state => stepName => {

	let returnStep = null;
	let steps = JSON.parse(state.nodes).transformation.step;
	steps.forEach((item, index) => {
		if (item.name == stepName) {
			returnStep = item;
		}
	});
	return returnStep;
};

/**
 * nodeName 原先的节点名称
 * stepName 修改后的节点名称
 */
export const getCheckNodeName = state => (path, nodeName, stepName) => {
	let steps = state.flowMain.stepList[path];
	let name = "";
	// 判断名称是否修改
	if (nodeName === stepName) {
		name = nodeName
	} else {
		name = renameNode(steps, nodeName, stepName, 0);
	}
	return name;
};


// 获取值映射
export const getMappingData = state => (key) => {
	let mapping = state.dataMap;
	let value = mapping[key];
	return value;
	// return state.dataMap;
};

export const getMappingkey = state => (value) => {
	let mapping = state.dataMap;
	let nodeId = '';
	for (let key in mapping) {
		if (mapping[key] === value) {
			nodeId = key;
		}
	}
	return nodeId;
};

// 获取当前步骤信息 data格式为{key:'画布路径',value:'步骤名称'}
export const getCurrentStep = state => (data) => {
	let currentStep = {};
	let key = data.key;
	let stepName = data.value;
	let steps = state.flowMain.stepList[key];

	steps.forEach((item, index) => {
		if (item.name === stepName) {
			currentStep = item;
		}
	});
	//
	return currentStep;
};

// 获取上一步骤信息 data格式为 {key:'画布标志',value:"当前步骤名称"}
export const getLastStep = state => (data) => {
	let lastStep = '';
	let lastStepName = '';
	let key = data.key;
	let stepName = data.value;
	let steps = state.flowMain.stepList[key];
	let lines = state.flowMain.lines[key];
	let lastStepNames = [];
	let lastSteps = [];
	if (lines !== undefined) {
		lines.forEach((item) => {
			if (item.to === stepName) {
				lastStepNames.push(item.from);
			}
		});
		lastStepNames.forEach((lastName, index) => {
			steps.forEach((item) => {
				if (item.name === lastName) {
					lastSteps.push(item);
				}
			});
		})
	}
	return lastSteps;
};

// 根据步骤名称删除步骤信息
export const deleteStepByName = state => (stepName) => {
	let steps = JSON.parse(state.nodes).transformation.step;
	steps.forEach((item, index) => {
		if (item.name === stepName) {
			steps.splice(index, 1);
		}
	});
	let nodes = JSON.parse(state.nodes);
	nodes.transformation.step = steps;
	state.nodes = nodes;
};
export const getPath = state => {
	return state.pathFlag;
};

// 获取动态路由信息
export const getRoutePath = state => {
	return state.addRouterList;
};


// 获取项目编号
export const getPeojectId = state => {
	return state.projectId;
};
// 获取项目运行编号
export const getExecuteId = state => {
	return state.executorId;
};

//获取项目运行信息
export const getRunningProject = state => (projectId) => {
	return state.flowMain.projectsRun[projectId];
};

// 获取画布加载标识
export const getFlowMainLoading = state => {
	return state.flowMainLoading;
};

//获取pluginId和控件类型映射
export const getPluginMap = state => (pluginId) => {
	return state.pluginMap[pluginId];
};

//获取登录参数
export const getLoginParams = state => {
	return state.loginParams;
};

// 获取token过期时间
export const getTokenTime = state => {
	return state.tokenTime;
};

// 获取token刷新标志
export const getTokenReFlag = state => {
	return state.refreshLogin;
};

// 获取执行结果弹窗标志
export const getExecuteFlag = state => (path) => {
	return state.flowMain.executeShow[path];
};

// 获取执行结果日志
export const getExecuteLog = state => (path) => {
	return state.flowMain.executeLogs[path];
};

// 获取路由标题名称
export const getRouteTitle = state => {
	return state.routeTilte;
};

// 根据起始节点获取连接线信息
export const getLineBystep = state => (path, fromId, toId) => {
	let lines = state.flowMain.lines[path];
	let line = {};
	lines.forEach((item, index) => {
		if (item.fromId === fromId && item.toId === toId) {
			line = item;
		}
	});
	return line;
};

// 获取用户账号
export const getLoginUserName = state => {
	return state.loginUserName;
};
// 获取用户密码
export const getLoginPassword = state => {
	return state.loginPassword;
};


// 根据路径获取项目编号
export const getProjectBypath = state => (path) => {
	return state.pathMap[path];
};


//获取是否是他人分享项目的标志
export const getCreateFlag = state => (path) => {
	return state.createFlag[path];
};

//获取前端配置
export const getUrlConfig = state => {
	return state.urlConfig;
};

//获取资源id集合
export const getResourceIds = state => {
	return state.resoueceIds;
};

//获取插件id
export const getPluginIds = state => {
	return state.pluginIds;
};

export const getDeleteProjects = state => {
	return state.deleteProject;
};

//生成输出字段
export const generateOutputFields = state => (data) => {
	// let lastStep = '';
	// let lastStepName = '';
	let key = data.key;
	let stepName = data.value;
	let steps = state.flowMain.stepList[key];
	let lines = state.flowMain.lines[key];
	let lastStepNames = [];
	// let lastSteps = [];
	let fields = [];
	if (lines !== undefined) {
		lines.forEach((item) => {
			if (item.to === stepName) {
				lastStepNames.push(item.from);
			}
		});
		console.info("lastStepNames",lastStepNames);
		lastStepNames.forEach((lastName, index) => {
			steps.forEach((item) => {
				if (item.name === lastName) {
					if (item.outFields !== undefined && item.outFields.length > 0) {
						item.outFields.forEach(item1 => {
							fields.push(item1);
						})
					} else {
						item.fields.field.forEach(item1 => {
							fields.push(item1);
						});
					}

				}
			});
		})
	}
	return fields;
};


//记录集获取字段
export const generateMergeJoin = state => (data) => {

	let key = data.key;
	let stepName = data.value;
	let steps = state.flowMain.stepList[key];
	let lines = state.flowMain.lines[key];
	let lastStepNames = [];
	let fields = [];
	if (lines !== undefined) {
		lines.forEach((item) => {
			if (item.to === stepName) {
				lastStepNames.push(item.from);
			}
		});
		lastStepNames.forEach((lastName, index) => {
			let filed = [];
			steps.forEach((item) => {
				if (item.name === lastName) {
					if (item.outFields !== undefined && item.outFields.length > 0) {
						filed = item.outFields;
					} else {
						filed = item.fields.field;
					}
				}
			});
			fields.push({
				name: lastName,
				fileds: filed
			})
		})
	}
	return fields;
};



export const getWebSocketTime = state=>{
  console.info("获取websocket连接时间",state.webSocketTime);
  return state.webSocketTime;
}
