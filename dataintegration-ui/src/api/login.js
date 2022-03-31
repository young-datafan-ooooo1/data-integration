import axios from 'axios';
import store from '../vuex/store';
import router from '../router';
import cookies from 'vue-cookies'

import {
  Message
} from 'element-ui';
import $ from 'jquery';
import qs from 'qs'
import globalWs from "../common/webSocket";
import { base, wsUrl } from './api.js'

export {
  base,
  wsUrl
}
axios.interceptors.request.use(
  config => {
    //排除登录方法
    if (config.url.indexOf("/token") < 0) {
      if (cookies.get('token')) { // 判断是否存在token，如果存在的话，则每个http header都加上token
        config.headers.Authorization = cookies.get('token');
      }
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  });
// axios响应拦截器
axios.interceptors.response.use(

  response => {
    if (response.data.code !== undefined && response.data.code === '10007') {
      Message({
        message: '用户在其他地方登录',
        iconClass: 'el-message__icon el-icon-circle-close',
        customClass: 'el-message--error'
      });
      store.dispatch('logOut');
      router.replace({
        path: '/'
      });
    }else if (response.data.code === '10001' || response.data.code === '10002' || response.data.code ==='10003' ||response.data.code ==='10004' || response.data.code ==='10005'
      || response.data.code ==='10006' || response.data.code ==='10008' || response.data.code ==='10010'  || response.data.code ==='99999') { //异常情况
      Message({
        message: response.data.msg.substr(0,1000)+"......",
        iconClass: 'el-message__icon el-icon-circle-close',
        customClass: 'el-message--error',
        showClose: true
      });
    }
    return Promise.resolve(response);
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          store.dispatch('logOut');
          cookies.remove('token',null,null);
          cookies.remove('userName',null,null);
          router.replace({
            path: '/'
          });
          break;
        case 404:

          break;
        case 500:

          break;
        case 400:
          break;

      }
    }
    return Promise.reject(error); //返回接口的错误信息
  },
);

//设置请求超时时间
// axios.defaults.timeout = 10000;

//获取前端配置
export const getVueConfig = params => {
  return axios.get(`${base}/dataintegration-portal-system-management-provider/configure/getVueConfig`)
}

//登录接口
export const login = params => {
  return axios.post(`${base}/dataintegration-common-sso-provider/oauth/token`, qs.stringify(params)).then(res =>
    res.data);
}

// 获取用户已授权菜单
export const getPlatResource = params => {
  return axios.get(`${base}/dataintegration-portal-system-management-provider/resource/selectResourceTreeForSubPlatform`, {params: params})
}

// 获取项目文件信息
export const getProjectVo = params => {
  return axios.get(`${base}/dataintegration-project-provider/project/getProjectFile/` + params)
}

// 获取token
export const getToken = params => {
  return axios.post(`${base}/dataintegration-common-sso-provider/oauth/token`, qs.stringify(params)).then(res =>
    res.data);
}

/**
 * 校验token
 * @param params
 * @returns {Promise<AxiosResponse<any>>}
 */
export const checkToken = params => {
  return axios.get(`${base}/dataintegration-common-sso-provider/oauth/check_token`, {params: params}).catch(e => {
  })
}


//获取集成平台菜单资源
