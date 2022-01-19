import store from '../vuex/store'
import Vue from 'vue'
Vue.directive('has', {
  inserted: function (el, binding) {
    if (!permissionJudge(binding.value)) {
      el.parentNode.removeChild(el);
    }

    function permissionJudge(value) {

      // 此处store.getters.getMenuBtnList代表vuex中储存的按钮菜单数据
      let list = JSON.parse(store.getters.getResourceIds);
      let flag = false;
      list.forEach((item,index)=>{
        if(item === value){
          flag = true;
        }
      })
      return flag;
    }
  }
});
