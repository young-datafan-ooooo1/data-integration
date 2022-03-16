<template>
  <div class="login-container" :style="{backgroundImage:'url(static/img/back'+backCount+'.png)'}">
    <div class='login_form'>
      <div class="login_logo">
        <img src="static/img/login-logo.png" style="width: 50px;height: 50px">
        一站式数据集成工具
      </div>
      <div class="login_body">
        <el-form :model="ruleForm" :rules="rules" status-icon ref="ruleForm" label-position="left" label-width="0px"
                 class="demo-ruleForm login-page">
          <el-form-item prop="userName">
            <el-input placeholder="请输入用户名" v-model="ruleForm.userName" @keyup.enter.native="handleSubmit">
              <i slot="prefix" class="iconfont icon-yonghu"></i>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input placeholder="请输入密码" type="password" v-model="ruleForm.password" show-password
                      @keyup.enter.native="handleSubmit">
              <i slot="prefix" class="iconfont icon-mima1"></i>
            </el-input>
          </el-form-item>
          <el-form-item style="width:100%; padding-top: 20px;">
            <el-button class="button_btn" @click="handleSubmit" :loading="logining">登录</el-button>
          </el-form-item>
        </el-form>
      </div>

    </div>
  </div>


</template>

<script>
  import {
    login,
    getVueConfig,
    getPlatResource
  } from '../../api/login.js'

  import bg1 from '../../../static/img/back1.png'
  import bg2 from '../../../static/img/back2.png'
  import cookies from "vue-cookies";

  export default {
    inject: ['initWs'],
    data() {
      return {
        logining: false,
        ruleForm: {
          userName: '',
          password: '',
        },
        rules: {
          userName: [{
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }],
          password: [{
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }]
        },
        checked: false,
        allBackgroundImages: [bg1, bg2]
      }
    },
    mounted() {
    },
    computed: {
      backCount() {
        // 根据背景图数组的长度随机选择索引
        const randIndex = Math.floor(Math.random() * 2) + 1;
        return randIndex;
      }
    },
    methods: {
      handleSubmit(event) {

        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            this.logining = true;
            let loginParam = {
              username: this.ruleForm.userName,
              password: this.ruleForm.password,
              grant_type: 'password',
              client_id: 'dataintegration',
              client_secret: 'dataintegration',
            };

            sessionStorage.clear();
            localStorage.clear();
            login(loginParam).then(res => {
              // if (status === '200') {
              //计算token过期时间
              if (res.access_token !== undefined) {
                this.$store.dispatch('setUserId',res.user_id);
                this.$cookies.set('token', 'Bearer ' + res.access_token, '0', '/');
                this.$cookies.set("userName", res.user_name_cn, '0', '/');
                this.$cookies.set("loginUserName", this.ruleForm.userName, '0', '/');
                this.$store.dispatch('clearViews');
                this.$router.push('/genneralView')
                this.logining = false;
                this.initWs();
              }
            }).catch(e => {
              this.logining = false;
              this.$message({
                message: e.response.data.error_description,
                type: 'error'
              })
            })

          } else {
            return false;
          }
        })
      }
    }
  };
</script>

<style>
  input:-internal-autofill-previewed,
  input:-internal-autofill-selected {
    -webkit-text-fill-color: black !important;
    transition: background-color 5000s ease-in-out 0s !important;
  }

  .login-container {
    width: 100%;
    height: 100%;
    position: relative;
    /* background:  no-repeat center; */
    background-size: 100% 100%;
    /* background-size: cover;
  }

  .login-page {
    border-radius: 5px;
    margin: 3% auto;
    width: 400px;
    padding: 35px 35px 15px;
    background: transparent;
    /*border: 1px solid #eaeaea;*/
    -webkit-box-shadow: 0 0 25px #cac6c6;
    box-shadow: 0 0 25px #cac6c6;
  }


  .login-container .el-button--primary1 {
    color: #FFFFFF;
    background: rgba(255, 158, 66, 1);
    border-radius: 4px;
  }

  .login_body .iconfont {
    font-family: "iconfont" !important;
    font-size: 16px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    height: 100%;
    width: 25px;
    text-align: center;
    -webkit-transition: all .3s;
    transition: all .3s;
    line-height: 40px;
  }


  .login_body .el-input--prefix .el-input__inner {
    padding-left: 40px;
  }

  .button_btn {
    color: #FFFFFF;
    background: #FF9E42;
    border: 1px solid #FF9E42;
    border-radius: 5px;
    width: 100%;
  }

  .button_btn:hover {
    color: #FFFFFF;
    background: rgba(255, 158, 66, 0.65);
    border: 1px solid rgba(255, 158, 66, 0.65);
    border-radius: 5px;
    width: 100%;
  }

  .button_btn:active {
    color: #FFFFFF;
    background: rgba(255, 158, 66, 0.65);
    border: 1px solid rgba(255, 158, 66, 0.65);
    border-radius: 5px;
    width: 100%;
  }

  .button_btn:after {
    color: #FFFFFF;
    background: rgba(255, 158, 66, 0.65);
    border: 1px solid rgba(255, 158, 66, 0.65);
    border-radius: 5px;
    width: 100%;
  }

  .el-button--primary.is-plain {
    color: #409EFF;
    background: #FF9E42;
    border-color: #FF9E42
  }
</style>
