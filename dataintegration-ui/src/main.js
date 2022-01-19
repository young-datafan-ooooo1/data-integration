import Vue from 'vue'
import '../theme/index.css'
import '../static/css/index.css'
import App from './App'
import ElementUI from 'element-ui'
import $ from 'jquery'
import 'bootstrap3/dist/css/bootstrap.css'
import 'bootstrap3/dist/js/bootstrap.js'
import router from './router'
import Vuex from 'vuex';
import store from './vuex/store.js'
import globalWs from './common/webSocket.js'
import './assets/iconfont/iconfont.css';
import VueCodemirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import VueResource from 'vue-resource'
import sqlFormatter from "sql-formatter"; //sql美化插件
import Icon from 'vue-svg-icon/Icon.vue'
import {getVueConfig,getPlatResource} from './api/login'
import Blob from './excel/Blob'
import Export2Excel from './excel/Blob'
Vue.component('icon', Icon)
Vue.use(sqlFormatter);
Vue.use(VueResource)
Vue.use(VueCodemirror)

import './common/dialog.js'

import {
  Message
} from 'element-ui';


Vue.prototype.globalWs = globalWs;
import VueCookies from 'vue-cookies'

import contentmenu from 'v-contextmenu'
import 'v-contextmenu/dist/index.css'
Vue.use(contentmenu)
Vue.use(VueCookies);



router.beforeEach((to, from, next) => {
  //拦截门户跳转
  //如果是登录界面 首先判断是否
  if (VueCookies.get('token') !== null && VueCookies.get('token') !== '' && VueCookies.get('token') !== undefined) {
    if (to.path === '/') {
      next()
    } else {
      if (store.getters.getUrlConfig && store.getters.getPluginIds && store.getters.getResourceIds && from.path !== '/') {
        if (to.path.indexOf('preProject') >= 0) {
          let projectId = to.path.substr(12);
          redirectLoad(projectId).then(function (resolve) {
            next();
          })
        }else{
          if(!(store.getters.getUserName === VueCookies.get('userName'))){
            store.dispatch('setUserName',VueCookies.get('userName'));
            store.dispatch('clearViews');
            router.push('/genneralView');
          }else{
            next();
          }
        }

      } else {
        store.dispatch('setUserName',VueCookies.get('userName'));
        //获取vueConfig和resourceTree
        getVueConfig().then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            store.dispatch('setUrlConfig', data.content);
          }
        })

        let queryParams = {
          parentResId: 'dataIntegration',
        }
        // 获取授权菜单资源
        getPlatResource(queryParams).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            let resources = data.content;
            let pluginIds = [];
            let resourceIds = [];
            resources.forEach((item, index) => {
              resourceIds.push(item.resId);
              if (item.childResources !== null && item.childResources !== undefined && item.childResources.length > 0) {
                let childs = item.childResources;
                childs.forEach((item1, index1) => {
                  resourceIds.push(item1.resId);
                  if (item1.resType === '2') {
                    pluginIds.push(item1.resId);
                  }
                  if (item1.childResources !== null && item1.childResources !== undefined && item1.childResources.length > 0) {
                    item1.childResources.forEach((thirdChild, k) => {
                      resourceIds.push(thirdChild.resId);
                      if (thirdChild.resType === '2') {
                        pluginIds.push(thirdChild.resId);
                      }
                    })
                  }
                })
              }
            })
            store.dispatch('setPluginds', pluginIds.join(','));
            store.dispatch('setResourceIds', resourceIds);
            store.dispatch('setResources', JSON.stringify(data.content));
            //判断是否是打开项目
            if (to.path.indexOf('preProject') >= 0) {
              let projectId = to.path.substr(12);
              redirectLoad(projectId).then(function (resolve) {
                next();
              })
            }else{
              if(!(store.getters.getUserName === VueCookies.get('userName'))){
                store.dispatch('clearViews');
                store.dispatch('setUserName',VueCookies.get('userName'));
                router.push('/genneralView');
              }else{
                next();
              }

            }

          } else {
            Message({
              message: '获取资源失败',
              iconClass: 'el-message__icon el-icon-circle-close',
              customClass: 'el-message--error'
            });
          }
        })

      }
    }
  } else {
    if (to.path === '/') {
      next();
    } else {
      router.push('/')
    }
  }
});

import './common/Premission.js'
Vue.use(ElementUI)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {
    App
  },
  template: '<App/>'
})
