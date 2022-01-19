<template>
  <div id="app" v-loading="loading">
    <router-view v-if="isRouterAlive"/>
  </div>
</template>

<script>
  import {
    wsUrl,
    checkToken
  } from './api/login.js';

  import store from "./vuex/store";
  import {getPlatResource, getVueConfig} from "./api/login";
  import VueCookies from "vue-cookies";
  import router from "./router";
  import {Message} from "element-ui";

  export default {

    name: 'App',
    provide() {
      return {
        reload: this.reload,
        initWs: this.initWebSocket,
      }
    },
    watch: {},
    data() {
      return {
        loading: false,
        isRouterAlive: true,
        stompClient: '',
      }
    },
    computed: {},
    beforeCreate() {

    },
    created() {
      if (sessionStorage.getItem("state")) {
        this.$store.replaceState(Object.assign(this.$store.state, JSON.parse(sessionStorage.getItem("state"))));
      }
      // 页面刷新前
      this.initWebSocket();
      // this.init();
      this.getResources();//获取菜单资源
    },

    mounted() {
      // this.initRouter();
      // this.initWebSocket();
      window.addEventListener('beforeunload', e => this.beforeunloadHandler(e));
    },
    destroyed() {

      //刷新项目 关闭websocket链接
      let ws = this.stompClient.ws;
      this.stompClient.disconnect({}, function (frame) {
      });
      window.removeEventListener('beforeunload', e => this.beforeunloadHandler(e))
    },
    methods: {
      getResources() {
        if (this.$route.path !== '/') {
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
                if (item.status === '0') {
                  resourceIds.push(item.resId);
                }
                if (item.childResources !== null && item.childResources !== undefined && item.childResources.length > 0) {
                  let childs = item.childResources;
                  childs.forEach((item1, index1) => {
                    if (item1.status === '0') {
                      resourceIds.push(item1.resId);
                      if (item1.resType === '2') {
                        pluginIds.push(item1.resId);
                      }
                    }

                    if (item1.childResources !== null && item1.childResources !== undefined && item1.childResources.length > 0) {
                      item1.childResources.forEach((thirdChild, k) => {
                        if (thirdChild.status === '0') {
                          resourceIds.push(thirdChild.resId);
                          if (thirdChild.resType === '2') {
                            pluginIds.push(thirdChild.resId);
                          }
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
            } else {

            }
          })
        }

      },

      //浏览器刷新提醒事件
      beforeunloadHandler(e) {
        e = e || window.event

        if (e) {
          e.returnValue = '关闭提示'
        }
        // 保存项目信息
        //获取flowMain数据
        let state = this.$store.state;
        sessionStorage.setItem("state", JSON.stringify(state));
        // 保存路由标签信息

        // 保存动态路由信息
        return '关闭提示'
      },
      reload() {
        this.isRouterAlive = false;
        this.$nextTick(function () {
          this.isRouterAlive = true;
        })
      },
      initWebSocket() {
        // this.stompClient = this.$store.getters.getWs;
        if (this.$cookies.isKey('token')) {
          if (this.stompClient === '' || this.stompClient.connected === false || this.stompClient.ws.readyState === 2 ||
            this.stompClient.ws.readyState === 3) {
            console.info("重新连接");
            let token = this.$cookies.get('token').substr(7);
            let params={
              token:token
            };
            checkToken(params).then(res=>{
              let{
                data
              }=res;
              if(data.active){
                this.connectWebScoket();
              }
            }).catch(e=>{

            })
          }
        }else{
          this.globalWs.setWs(this.stompClient)
        }
      },
      connectWebScoket() {
        let _this = this;
        let userId = this.$cookies.get('userId');
        let token = this.$cookies.get('token');
        this.stompClient = Stomp.client(wsUrl + '/websocket?access_token=' + token.substr(7, token.length));
        this.stompClient.ws_fn(function (frame) {
        })
        // console.info( this.stompClient.errorCallback())
        this.stompClient.heartbeat.outgoing = 10000;
        this.stompClient.heartbeat.incoming = 10000;
        this.stompClient.reconnect_delay = 5000;

        this.stompClient.connect({}, function (frame) {
          _this.globalWs.setWs(_this.stompClient);
        },(err) => {
          console.info("err-=-=",err);
          let token = _this.$cookies.get('token').substr(7);
          let params={
            token:token,
            // access_token:token,
          };
          checkToken(params).then(res=>{
            let{
              data
            }=res;
            if(data.active){
              // _this.connectWebScoket();
            }else{
              _this.$cookies.remove('token',null,null);
              _this.$cookies.remove('userName',null,null)
              _this.$router.push('/');
              _this.stompClient.disconnect({},function (frame) {
              })
            }
          }).catch(e=>{
            _this.$cookies.remove('token',null,null);
            _this.$cookies.remove('userName',null,null)
            _this.$router.push('/');
            _this.stompClient.disconnect({},function (frame) {
            })
          })
        });
        let ws = this.stompClient.ws;
      }
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
  }

  /* 页面上内容不能选中 */
  body {
    /* -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none; */
  }

  html,
  body,
  #app {
    height: 100%;
  }

  #app .el-table th, .el-table td {
    padding: 0;
    min-width: 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    text-overflow: ellipsis;
    vertical-align: middle;
    position: relative;
    text-align: left;
  }

  body .el-table th.gutter {
    display: table-cell !important
  }

  div .el-table th.gutter {
    display: table-cell !important
  }

  el-dialog .el-table th.gutter {
    display: table-cell !important
  }

  div::-webkit-scrollbar {
    width: 5px;
    height: 8px;
  }

  div::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: rgba(0, 0, 0, 0.2);
  }

  div::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    /* -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2); */
    /* border-radius: 10px; */
    /* background: #EDEDED; */
  }


  form::-webkit-scrollbar {
    width: 5px;
    height: 8px;
  }

  form::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
    background: rgba(0, 0, 0, 0.2);
  }

  form::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    /* -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2); */
    /* border-radius: 10px; */
    /* background: #EDEDED; */
  }

  /* .el-table--enable-row-transition .el-table__body td {
    padding: 0;
    -webkit-transition: background-color .25s ease;
    transition: background-color .25s ease;
  }

  .el-table td,
  .el-table th.is-leaf {
    padding: 0;
    border-bottom: 1px solid #ebeef5;
  }

  .el-table td, .el-table th {
      padding: 0px 0px;
      min-width: 0;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      text-overflow: ellipsis;
      vertical-align: middle;
      position: relative;
      text-align: left;
  }

  .el-table .cell {
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
    word-break: break-all;
    line-height: 23px;
    padding-right: 0px;
  }

  .el-table td,
  .el-table th {
    padding: 0;
    min-width: 0;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    text-overflow: ellipsis;
    vertical-align: middle;
    position: relative;
    text-align: left;
  } */
</style>
