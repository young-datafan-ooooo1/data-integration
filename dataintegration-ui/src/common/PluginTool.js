import store from '../vuex/store.js';
import router from '../router';
import util from './utils.js'

/**
 * 初始化步骤信息
 * @param nodeData
 * @param key
 * @param step
 */
export function initStep(nodeData,key,step) {
  step.oldStepName = nodeData.label;
  let  stepName = step.name;
  let param = {
    key: key, //用于标记画布，这里用的是画布路径
    value: stepName
  };
  //获取当前步骤信息
  let curStep = store.getters.getCurrentStep(param);
  console.info("curStep-=-=-",curStep);
  if(curStep!==undefined){
    for (let key in curStep) {
      if (curStep[key] === 'Y' && key !== 'type') {
        step[key] = true;
      }else if(curStep[key] === 'N' && key!=='type'){
        step[key] = false;
      }else{
        if(key ==='fields'){
        }else if(key === 'file'){
          for(let subKey in curStep.file){
            if(curStep[subKey] === 'Y'){
              step.file[subKey] =true;
            }else if(curStep[subKey]==='N'){
              step.file[subKey]=false;
            }else{
              step.file[subKey] = curStep.file[subKey];
            }
          }
        }
        else{
          step[key] = curStep[key];
        }
      }
    }
    if(step.fields!==undefined){
      step.fields.field=[];
      let fields = curStep.fields.field;
      fields.forEach(item => {
        step.fields.field.push(item);
      })
    }

  }
  return step;
}
