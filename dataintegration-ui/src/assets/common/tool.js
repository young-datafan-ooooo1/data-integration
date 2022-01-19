// 去除数组中的重复对象
let newName ='';
function compareObj(Obj) {
  for (let i = 0; i < Obj.length; i++) {
    for (let j = i + 1; j < Obj.length; j++) {
      if (Obj[i].name === Obj[j].name) {
        Obj.splice(j, 1);
      }
    }
  }
}

// 去除步骤信息重复项
function spliceSteps(steps) {
  for (let i = 0; i < steps.length; i++) {
    for (let j = i + 1; j < steps.length; j++) {
      if (steps[i].name === steps[j].name) {
        steps.splice(j, 1);
      }
    }
  }
}

// 去除连接线信息重复项
function sliceLines(lines) {
  for (let i = 0; i < lines.length; i++) {
    for (let j = i + 1; j < lines.length; j++) {
      if (lines[i].from === lines[j].from && lines[i].to === lines[j].to) {
        lines.splice(j, 1);
      }
    }
  }
}

// 去除项目信息重复项
function spliceProjectInfo(projects) {
  for (let i = 0; i < projects.length; i++) {
    for (let j = i + 1; j < projects.length; j++) {
      projects.splice(j, 1);
    }
  }
}

/**
 * 去除重复字段
 * @param fields
 */
function compareFields(fields) {
  for(let i =0;i<fields.length;i++){
    for(let j = i+1;j<fields.length;j++){
      if(fields[i].name ===fields[j].name || fields[i].name ===''){
        fields.splice(j,1);
      }
    }
  }
}


/**
 * @param {List} steps 步骤列表
 * @param {Object} nodeName 原先的步骤名
 * @param {Object} stepName 新步骤名
 * @param {Object} i 标志
 */
function renameNode(steps, nodeName, stepName, i) {
  let val = steps.find((item) => {
    return item.name === stepName;
  })
  if(stepName === nodeName){
    val = undefined;
  }
  if (val !== undefined) {
    i++;
    let name = stepName + ' ' + i;
    renameNode(steps, nodeName, name, i);
  } else {
    newName = stepName;
  }
  return newName;
}




export {
  compareObj,
  spliceSteps,
  spliceProjectInfo,
  sliceLines,
  renameNode,
  compareFields
}
