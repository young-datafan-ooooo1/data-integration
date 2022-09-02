<template>
  <div class="home">
    <div class="home_siderbar">
      <div class="home_logo">
        <img src="../../../static/img/logo.png">
      </div>
      <!-- 左侧导航 -->
      <div class="opera_sidebar">
        <div class="top_menue">
          <div class="project_menu" v-for="item in menus" :id="item.id">
            <div class="div_icon" @mouseover="openDetail(item)" v-has="item.resId">
              <i :class="item.icon"></i><br/>
              {{item.name}}
            </div>
            <div class="item_detail" v-show="item.itemShow" :style="{height:height+'px'}" v-if="item.id === 'project'">
              <div class="tree_css">
                <div class="tree_title" v-for="(project,index) in projects ">
                  <div class="first_level" @click="project.show = !project.show">
<!--                    <i :class="project.projectImage"></i>-->
                    <span class="node">{{project.label}}</span>
                  </div>
                  <el-collapse-transition>
                    <div class="second_level" v-show="project.show">
                      <el-tree :data="project.children" :props="defaultProps" @node-click="handleNodeClick"
                               node-key="id"
                               :ref="project.id" :default-expanded-keys="[1]" :highlight-current="true" :indent="20">
                        <span class="custom-tree-node" slot-scope="{node,data}">
                          <i :class='data.projectImage'></i>
                          <span class="node" @dblclick="openProject(data)">{{node.label}}</span>
                        </span>

                      </el-tree>
                    </div>
                  </el-collapse-transition>
                </div>
              </div>
            </div>
            <div class="item_detail" v-show="item.itemShow" :style="{height:height+'px'}"
                 v-else-if="item.id === 'data'">
              <div class="tree_css">
                <div class="tree_title" v-for="(model,index) in modelData ">
                  <div class="first_level" @click="model.show = !model.show">
                    <i :class="model.pluginImage"></i>
                    <span class="node">{{model.label}}</span>
                  </div>
                  <el-collapse-transition>
                    <div class="second_level" v-show="model.show">
                      <el-tree :data="model.children" :props="defaultProps" @node-click="handleNodeClick"
                               :ref="model.id"
                               :default-expanded-keys="[1]" :highlight-current="true" :indent="20">
                        <span class="custom-tree-node" slot-scope="{node,data}" @dragstart="drag(data)"
                              draggable="true">
                          <i :class='data.pluginImage'></i>
                          <el-tooltip class="item" effect="dark" :content="node.label" placement="right">
                            <span class="node">{{node.label}}</span>
                          </el-tooltip>
                        </span>

                      </el-tree>
                    </div>
                  </el-collapse-transition>
                </div>
              </div>
            </div>
          </div>
          <div class="bottom_menu">
            <div class="divider_menu">
              <div class="divider-line"></div>
            </div>
            <div class="project_menu" v-for="item in bottomMenus" :id="item.id">
              <div class="div_icon" @click="openDetail(item)">
                <i :class="item.icon"></i><br/>
                {{item.name}}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!--头部导航区-->
    <div class="home_content">
      <div class="content_title">
        <div class="titie_css">数据集成工具</div>
        <div class="serach_bar">
          <div class="input_bar" id="input_bar">
            <el-input v-on:input="changeInput" v-model="projectName" style="width: 100%;" @focus="changeInput"
                      placeholder="请输入数据集成项目名称" id="pro_input">
              <i slot="prefix" class="el-input__icon el-icon-search" @click="getProjectByName()"></i>
            </el-input>
            <div class="seach_div" v-show="showList" id="search_div">
              <div class="search-blank" v-if="projectLoading"><i class="el-icon-loading"></i>数据搜索中，请稍后。。。</div>
              <div class="project_name" v-if="projectData" v-for="pro in projectData" v-on:click="openProject(pro)">
                {{pro.projectName}}
              </div>
              <div class="search-blank" v-if="!projectLoading && projectData.length===0">查询无项目</div>
            </div>
          </div>
          <!--          <el-button icon="el-icon-plus" type="primary" class="add_btn" @click="addProject">新建集成</el-button>-->
        </div>
        <!-- 右侧用户信息 -->
        <div class="tools">
          <div class="notice_block"><img src="../../../static/img/notice.png"></img></div>
          <div class="help_block"><img src="../../../static/img/help.png"></div>
          <div class="dropDown">
            <el-dropdown trigger="click">
              <span class="el-dropdown-link" id="user_class">
                <div class="name_icon" id="name_icon">{{userFirstName}}</div>{{userName}}<i
                class="el-icon-arrow-down el-icon--right"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="updatePassWord">修改密码</el-dropdown-item>
                <el-dropdown-item @click.native="loginOut">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>

      <!-- 内容区 用于路由跳转-->
      <div class="route_content">
        <div class="tagsTools" v-show="tagsShow">
          <tags-view></tags-view>
        </div>
        <!-- 左侧控件信息 -->
        <div class="plugin_style" v-if="tagsShow">
          <plugin-tree></plugin-tree>
        </div>
        <keep-alive>
          <router-view :key="key"></router-view>
        </keep-alive>
      </div>
    </div>

    <!-- 项目管理 -->
    <el-dialog :visible.sync="projectManagerVisiable" class="other_dialog project_dia" width="70%"
               :close-on-click-modal='false'>
      <projectManager ref="projectManager" :key="subKey"></projectManager>
    </el-dialog>
    <!-- 保存模型 -->
    <el-dialog :visible.sync="modelVisiable" class="other_dialog project_dia" width="70%" :close-on-click-modal='false'>
      <modelManager ref="modelManager" :key="subKey"></modelManager>
    </el-dialog>

    <!-- 运行 -->
    <el-dialog :visible.sync="statusVisible" class="other_dialog project_dia" width="70%" :before-close="handleProject"
               :close-on-click-modal='false'>
      <runManager ref="runManager" :key="subKey"></runManager>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :before-close="handleClose"
               class="dialog_temp other_dialog"
               :close-on-click-modal='false'>
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="pass">
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit" :loading="updatePassLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  selectGrantProject,
  selectProjectByProjectName,
  selectAllProject,
  getAllBusinessModelPluginInfoByUserId,
  loginOut, updatePassWord,
} from '../../api/api.js'
  import {
    openFlow
  } from '../../common/common.js'
  import util from '../../common/utils.js'
  import tagsView from '../common/TagsView'
  import WidGet from '../dataExploration/WidGet'
  import projectManager from '../dataExploration/projectManager'
  import modelManager from '../dataExploration/ModelManager'
  import runManager from '../dataExploration/RunManager'
  import pluginTree from '../dataExploration/PluginTree.vue'

  export default {

    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.ruleForm.checkPass !== '') {
            this.$refs.ruleForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.ruleForm.pass) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        projectLoading: true,
        subKey: null,
        updatePassLoading: false,
        dialogVisible: false,
        statusVisible: false, //运行监控弹窗
        modelVisiable: false, //保存模型弹窗
        projectManagerVisiable: false, //项目管理弹窗
        tagsShow: false,
        showList: false,
        projectData: [],
        modelData: [], //模型数据
        projectName: '',
        show3: false,
        projects: [],
        ruleForm: {
          oldPass: '', //原密码
          pass: '', //密码
          checkPass: '', //确认密码
        },
        rules: {
          oldPass: [{
            required: true,
            message: '请输入原密码',
            trigger: 'blur'
          }],
          pass: [{
            required: true,
            validator: validatePass,
            trigger: 'blur'
          }],
          checkPass: [{
            required: true,
            validator: validatePass2,
            trigger: 'blur'
          }]
        },
        data: [{
          id: 1,
          label: '我的项目',
          projectImage: 'iconfont icon-wenjianjia1',
          children: [],
          show: false,
        }, {
          id: 2,
          label: '他人项目',
          projectImage: 'iconfont icon-wenjianjia1',
          children: [],
          show: false,
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        height: 0,
        itemShow: false,
        oldItem: {},
        menus: [{
          id: 'project',
          name: '项目',
          itemShow: false,
          icon: 'iconfont icon-project',
          resId: 'dataIntegra_project',
        }],

        bottomMenus: [{
          id: 'manager',
          name: "管理",
          itemShow: false,
          icon: 'iconfont icon-mand',
          resId: 'dataIntegra_manager'
        },
          //   {
          //   id: 'modelManage',
          //   name: "模型",
          //   itemShow: false,
          //   icon: 'iconfont icon-yanpanmoxing'
          // },
          {
            id: 'runManage',
            name: "运行",
            itemShow: false,
            icon: 'iconfont icon-runds',
            resId: 'dataIntegra_run',
          }],
        items: [],
        gutter: 24,
        userFirstName: '',
        userName: this.$cookies.get('userName'),
        isdown: true,
      }
    },
    computed: {
      key() {
        if (this.$route.path === '/genneralView' || this.$route.path === 'home1') {
          this.tagsShow = false;
          return this.$route.path;
        } else {
          this.tagsShow = true;
          return this.$route.name !== undefined ? this.$route.name + new Date() : this.$route + new Date()
        }
      },
    },
    created() {
    },
    components: {
      tagsView,
      WidGet,
      projectManager,
      modelManager,
      runManager,
      pluginTree
    },
    mounted() {
      let that = this;
      $(document).click(function (e) {

        var target = e ? e.target : window.event.srcElement;
        if (target.id != "input_bar" && target.id !== 'search_div' && target.id != 'pro_input' && target.id !== 'data' &&
          target.id !== 'project' && target.id !== 'pro' && target.id !== 'Mod' && target.id !=='pro_i' && target.id !=='Mod_i' &&target.className !=='first_level'
          && target.className!=='node' && target.className!=='tree_css') {
          that.showList = false;
          that.menus[0].itemShow=false;
          // that.menus[1].itemShow=false;
        }

      });
      this.userFirstName = this.userName.substr(0, 1);
      this.getTreeData();

      let userWidth = $('#user_class').width();
      let doc = document.getElementById('name_icon');
      doc.style.right = userWidth + 'px';
    },
    destroyed() {
    },

    methods: {
      updatePassWord() {
        this.dialogVisible = true;
        this.$nextTick(() => {
          this.$refs['ruleForm'].resetFields();
        })

      },
      submit() {
        this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            let params = {
              oldPassword: this.ruleForm.oldPass,
              password: this.ruleForm.pass,
              confirmPassword: this.ruleForm.checkPass,
            };
            this.updatePassLoading = true;
            updatePassWord(params).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                if (data.content) {
                  this.$message({
                    message: '密码修改成功',
                    type: 'success',
                  });
                  this.updatePassLoading = false;
                  this.dialogVisible = false;
                } else {
                  this.$message({
                    message: data.msg,
                    type: 'error',
                  })
                  this.updatePassLoading = false;
                  // this.dialogVisible = false;
                }
              }
            })
          } else {

          }
        })

      },
      loginOut() {
        this.$confirm('确定要退出登录吗？', '警告', {
          type: 'warning'
        }).then(() => {
          sessionStorage.clear();
          localStorage.clear();
          this.$router.push('/');
          this.$store.dispatch('logOut');
          loginOut().then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {

              //退出登录清空vuex中的数据
              this.$store.commit('setUserName', '');
            }
          })

        });

      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            this.dialogVisible = false;
          })
          .catch(_ => {
          });
      },

      drag(data) {
        this.$store.dispatch("setItem", JSON.stringify(data));
      },

      overFunction(item) {
        item.isdown = false;
        this.isdown = false;
      },
      leaveFunc(item) {
        item.isdown = true;
        this.isdown = true;
      },
      openDetail(item) {
        let docs = document.getElementsByClassName('project_menu_isActive');
        for (let i = 0; docs.length; i++) {
          docs[i].className = 'project_menu';
        }
        let clickMenu = document.getElementById(item.id);
        clickMenu.className = 'project_menu_isActive'
        let flag = item.itemShow;
        this.subKey = new Date().getTime();
        if (item.id === 'manager') {

          this.projectManagerVisiable = true;
        } else if (item.id === 'modelManage') {
          this.modelVisiable = true;
        } else if (item.id === 'runManage') {
          this.statusVisible = true;
        } else {
          var doc = document.getElementById(item.id);
          this.height = window.innerHeight - doc.getBoundingClientRect().top-10;
          if(this.oldItem.id === item.id){
            if(item.itemShow === false){
              item.itemShow = true;
            }
            // item.itemShow = true;
          }else{
            this.oldItem.itemShow = false;
            item.itemShow = true;
            if (item.id === 'data') {
              this.getModelData();
            } else {
              this.getTreeData();
            }
          }

          this.oldItem = item;

        }
        this.lastMenuId = item.id;

      },

      /**
       * 获取数据模型信息
       */
      getModelData() {
        getAllBusinessModelPluginInfoByUserId().then(res => {
          if (res.data.code === '10000') {
            let firpluginCategoryDTOs = res.data.content[0].pluginCategoryDTOs;
            let children = [];
            for (let j = 0; j < firpluginCategoryDTOs.length; j++) {
              let seconds = [];
              let secondDtos = firpluginCategoryDTOs[j].pluginCategoryDTOs;
              for (let k = 0; k < secondDtos.length; k++) {
                let third = [];
                let pluginDTOS = secondDtos[k].pluginInfoDTOS;
                for (let h = 0; h < pluginDTOS.length; h++) {
                  third.push({
                    nodeId: pluginDTOS[h].modelName,
                    id: pluginDTOS[h].pluginId,
                    label: pluginDTOS[h].modelNameCn,
                    categoryOrder: pluginDTOS[h].categoryOrder,
                    pluginCategory: pluginDTOS[h].pluginCategory,
                    pluginDescribe: pluginDTOS[h].pluginDescribe,
                    pluginId: pluginDTOS[h].pluginId,
                    pluginName: pluginDTOS[h].pluginName,
                    pluginOrder: pluginDTOS[h].pluginOrder,
                    pluginType: pluginDTOS[h].pluginType,
                    modelType: pluginDTOS[h].modelType,
                    pluginImage: pluginDTOS[h].pluginImage === '' || pluginDTOS[h].pluginImage ===
                    undefined || pluginDTOS[h].pluginImage === null ? 'el-icon-question' : pluginDTOS[h].pluginImage,
                    modelName: pluginDTOS[h].modelName,
                    modelNameCn: pluginDTOS[h].modelNameCn,
                    secondPluginType: pluginDTOS[h].secondPluginType,
                  });
                }
                seconds.push({
                  id: k,
                  nodeId: j + '_' + k,
                  label: secondDtos[k].category,
                  children: third,
                  pluginImage: 'el-icon-folder-opened'
                })
              }
              children.push({
                id: j,
                nodeId: 0 + '-' + j,
                label: firpluginCategoryDTOs[j].category,
                children: seconds,
                show: false,
                pluginImage: 'el-icon-folder-opened'
              })

            }
            this.modelData = children;
          } else {
            this.$message({
              message: '获取数据模型失败',
              type: 'error'
            })
          }
        })
      },
      handleNodeClick(data) {
        // util.$emit('changeControl','test');
      },
      getProjectByName(item) {
        let params = {
          projectName: this.projectName,
          pageNum: 1,
          pageSize: 10,
          projectType: 'JCJB'
        }
        this.projectData = [];
        this.projectLoading = true;
        selectAllProject(params).then(res => {
          let {
            data
          } = res;
          let subData = [];
          if (data.code === '10000') {
            for (let i = 0; i < data.content.list.length; i++) {
              let prChlid = data.content.list[i].projects;
              for (let j = 0; j < prChlid.length; j++) {
                subData.push({
                  id: prChlid[j].projectId,
                  label: prChlid[j].projectName,
                  projectId: prChlid[j].projectId,
                  projectName: prChlid[j].projectName,
                  opModel: prChlid[j].opModel,
                  groupId: prChlid[j].groupId,
                  groupName: prChlid[j].groupName,
                  projectOnlineDTO: prChlid[j].projectOnlineDTO,
                })
              }
            }
            this.projectData = subData;
            this.projectLoading = false;
          }
        })
      },
      getTreeData() {
        let para = {
          pageNum: 1,
          pageSize: 100,
          projectName: '',
          projectType: 'JCJB',
        }
        let parament = [];
        let children = [];
        this.treeLading = true;
        selectProjectByProjectName(para).then(res => {

          let {
            data
          } = res;
          if (data.code === '10000') {
            for (let i = 0; i < data.content.list.length; i++) {
              let projects = [];
              let prChlid = data.content.list[i].projects;
              for (let j = 0; j < prChlid.length; j++) {
                projects.push({
                  id: prChlid[j].projectId,
                  label: prChlid[j].projectName,
                  projectId: prChlid[j].projectId,
                  projectName: prChlid[j].projectName,
                  opModel: prChlid[j].opModel,
                  groupId: prChlid[j].groupId,
                  groupName: prChlid[j].groupName,
                  projectOnlineDTO: prChlid[j].projectOnlineDTO,
                })
              }
              children.push({
                id: data.content.list[i].groupId,
                label: data.content.list[i].groupName,
                children: projects,
                projectImage: 'iconfont icon-wenjianjia'
              })
            }
            parament.push({
              id: 1,
              label: '我的项目',
              projectImage: 'iconfont icon-wenjianjia',
              children: children,
            })
            this.data[0].children = children;
            this.projects = this.data;
            this.treeLading = false;
          }
        });
        selectGrantProject(para).then(res => {
          let {
            data
          } = res;
          let children = [];
          if (data.code === '10000') {
            for (let i = 0; i < data.content.list.length; i++) {
              let prChild = data.content.list[i].projects;
              let groupChild = [];
              for (let j = 0; j < prChild.length; j++) {
                let projectChild = [];
                for (let k = 0; k < prChild[j].projects.length; k++) {
                  projectChild.push({
                    id: prChild[j].projects[k].projectId,
                    label: prChild[j].projects[k].projectName,
                    projectId: prChild[j].projects[k].projectId,
                    projectName: prChild[j].projects[k].projectName,
                    opModel: prChild[j].projects[k].opModel,
                    groupId: prChild[j].projects[k].groupId,
                    groupName: prChild[j].projects[k].groupName,
                    projectOnlineDTO: prChild[j].projects[k].projectOnlineDTO
                  })
                }
                groupChild.push({
                  id: prChild[j].groupId,
                  label: prChild[j].groupName,
                  children: projectChild,
                })
              }
              children.push({
                id: i,
                label: data.content.list[i].describe,
                children: groupChild,
                projectImage: 'iconfont icon-wenjianjia1',
              })
            }
          }
          parament.push({
            id: 2,
            label: '他人项目',
            children: children,
            projectImage: 'iconfont icon-wenjianjia1',
          })
          this.projects = this.data;
          this.data[1].children = children;
        })
      },

      /**
       * todo 查询项目信息
       */
      changeInput() {
        // 判断输入框中是否有值
        if (this.projectName !== '') {
          this.showList = true;
          this.getProjectByName();
        } else {
          this.showList = false;
        }

      },

      /**
       * 显示项目信息，打开画布
       */
      showProject() {
        this.showList = false;
      },

      addProject() {
        util.$emit('addProject');
      },

      handleProject(done) {
        this.statusVisible = false;
      },

      /**
       * 打开项目信息
       */
      openProject(datas) {
        this.showList = false;
        openFlow(datas);
      },

    },

  }
</script>

<style>

</style>
