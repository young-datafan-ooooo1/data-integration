import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/login/Login.vue'

// 定义静态路由
export const routerMap = [{
    path: '/',
    name: 'HelloWorld',
    component: HelloWorld
  },
  // 项目预览
  {
    path: '/preProject/:projectId',
    name: '项目预览',
    component: resolve => require(['../components/jsplumpVue/flowMain.vue'], resolve),
  },
  {
    path:'/home',
    component:resolve=>require(['../components/page/HomePage.vue'],resolve),
    name:'首页',
  },

  {
    path: '/genneralView',
    // name: '首页',
    component: resolve => require(['../components/page/HomePage.vue'], resolve),
    children: [{
        path: '/genneralView',
        name: '概览页面',
        component: resolve => require(['../components/page/ProjectShow.vue'], resolve),
        meta: {
          title: '概览界面',
          title1: '概览页面',
        }
      },
      {
        path: '/project/:projectId',
        name: '新建集成',
        component: resolve => require(['../components/jsplumpVue/flowMain.vue'], resolve),
        children: [],
        meta: {
          title:'新建集成',
          title1:'新建集成'
        }
      }
    ]
  },
  // {
  //   path: '/home1',
  //   // name: '首页',
  //   component: resolve => require(['../components/page/HomePage.vue'], resolve),
  //   children: [{
  //       path: '/home1',
  //       name: '概览页面',
  //       component: resolve => require(['../components/page/ProjectShow.vue'], resolve),
  //       meta: {
  //         title: '概览界面',
  //         title1: '概览页面',
  //       }
  //     },
  //     {
  //       path: '/textEdit',
  //       name: '编辑器',
  //       component: resolve => require(['../components/common/TextEdit.vue'], resolve),
  //       meta: {
  //         title: '编辑器'
  //       }
  //     },
  //     {
  //       path: '/project/:projectId',
  //       name: '新增项目',
  //       component: resolve => require(['../components/jsplumpVue/flowMain.vue'], resolve),
  //       children: [],
  //       // meta: {
  //       //   title:
  //       // }
  //     }
  //   ]
  // },
]


Vue.use(Router)


export default new Router({
  routes: routerMap
})

const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}
