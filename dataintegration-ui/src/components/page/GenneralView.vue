<template>
  <div class="genenerView" style="text-align: center;">
    <div class="home_page_class">
      <div style="text-align: center;color: #a5a5a5; font-family: 华康圆体W5;font-size: 30px;">
        <p>
          欢迎使用数据集成工具
        </p>
      </div>

      <div class="bt" style="margin-top: 20px;">
        <el-button type="primary" @click="addProject" icon="el-icon-plus">新建集成</el-button>
      </div>
      <div style="text-align: center;color:#a5a5a5; font-family: 'microsoft yahei'; font-size: 16px; margin-top: 30px;">
        <p>点击“新建项目”，开启您的信息集成分析之旅</p>
      </div>
      <div class="img_css">
        <img src="../../../static/img/addDataExplore.png" style="width: 80%;">
      </div>
    </div>

  </div>

</template>

<script>
  import HomePage from '../HomePage.vue'
  import util from '../../common/utils.js'
  import ss from '../jsplumpVue/flowMain.vue'
  import store from '../../vuex/store.js'
  import router from 'vue-router'
  import {
    routerMap
  } from '@/router'
  export default {

    data() {
      return {
        i: 1,
        count: 0, //鼠标点击次数
      }
      components: {
        HomePage
      }
    },
    methods: {
      addProject() {
        this.count++;
        let itemList = {
          path: '',
          name: '数据集成' + this.count,
        }
        localStorage.setItem("count", this.count);
        util.$emit('changeControl', 'test');
        let createFlag = true;
        let path = '/SJTS' + this.getUUID();
        let newPath = '/project' + path;
        let name = '数据集成' + this.count;

        let createFlagData = {
          key: newPath,
          value: true,
        };
        this.$store.dispatch('setCreateFlag',JSON.stringify(createFlagData));
        this.$store.dispatch('setFlowMainLoading', false);
        this.$store.dispatch('addRouteTitle', name);
        // this.$router.addRoutes(routers);
        let executeFlag = {
          key: path,
          value: false,
        }
        this.$store.dispatch('setExecuFlag', executeFlag);
        this.$router.push(newPath);
      },
      getUUID() {
        var s = [];
        var hexDigits = "0123456789abcdefhijklmnopqrstuzwxyz";
        for (var i = 0; i < 5; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }

        var uuid = s.join("");
        return uuid;
      },
    }
  }
</script>

<style scoped>
  .el-button--primary {
    color: #1eaafb;
    margin-top: 10px;
    background: #1a3754;
    border-radius: 20px;
    height: 35px;
    padding: 0px 20px;
  }

  .home_page_class {
    text-align: center;
    margin-top: 8%;
  }

  .genenerView {
    margin: 0 auto;
    height: 100%;
    /* width: 100%; */
    position: absolute;
    overflow: auto;
  }
</style>

<style>
  @media screen and (max-width: 300px) {
    body {
      background-color: lightblue;
    }
  }

  .bt .el-button--primary {
    color: #1eaafb;
    background: #1a3754;
    margin-top: 10px;
    border-radius: 20px;
    height: 35px;
    padding: 0px 20px;
  }

  .bt {
    text-align: center;
  }

  .home_page_class {
    text-align: center;
    margin-top: 10%;
  }

  .genenerView {
    height: 100%;
    width: 100%;
    margin: 0 auto;
    position: absolute;
  }

  .img_css {
    text-align: center;
    margin-top: 100px;
  }
</style>
