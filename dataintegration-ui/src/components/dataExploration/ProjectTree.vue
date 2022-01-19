<template>
  <div class="tree_data">
    <div class="header_tools">
      <div class="fileter_text">
        <el-input placeholder="输入关键字进行过滤" v-model="filterText">
          <i slot="suffix" class="el-input__icon el-icon-close" @click="clear()"></i>
        </el-input>
      </div>
      <!-- <div class="icon-body"> -->
      <!-- <div class="icon-wrap">
          <i class="iconfont icon-shuaxin" @click="getTreeData()"></i>
        </div> -->
      <!-- <div class="icon-wrap">
          <i class="iconfont icon-xinzeng" @click="editData()"></i>
        </div>
        <div class="icon-wrap">
          <i class="iconfont icon-caidan2" @click="openMannager()"></i>
        </div> -->

      <!-- </div> -->
    </div>

    <!-- <div class="link-top"></div> -->

    <el-dialog :visible.sync="dialogVisible" id="other_dialog" class="other_dialog" width="60%" :before-close="handleClose"
      :close-on-click-modal='false'>
      <div class="dialogHeader">
        <div class="headerSpan_left" id="headerSpan_left" @click="changeProjectManager">
          <span>项目管理</span>
        </div>
        <div class="headerSpan_right" id="headerSpan_right" @click="changeGroupManager">
          <span>分组管理</span>
        </div>

      </div>



      <!-- 项目管理 -->
      <div v-show="projectFlag">
        <div class="toolbar1">
          <el-form :inline="true" :model="projectForm" size="mini" class="demo-form-inline">
            <el-form-item>
              <input v-model="projectForm.projectName" class="el-input__inner group_input" placeholder="项目名称"></input>
            </el-form-item>

            <el-form-item>
              <el-select size="small" v-model="projectForm.groupId">
                <el-option v-for="item in groupIds" :key="item.groupId" :value="item.groupId" :label="item.groupName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="getProject()" icon="el-icon-search"></el-button>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" size="mini" @click="deleteProjects" :disabled="this.selectProject.length===0">删除</el-button>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" size="mini" @click="grantUser" :disabled="this.selectProject.length===0">分享</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="backUpdate" :disabled="this.selectProject.length===0">撤回上线</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="subLine" :disabled="subDis">提交上线</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table :data="projectData" order style="width: 100%;table-layout: fixed;" border height="400"
          @selection-change="handleSelectionChange2" v-loading="projectLoading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="项目名称" prop="projectName">

          </el-table-column>
          <el-table-column label="所属分组" prop="groupName">

          </el-table-column>
          <el-table-column label="项目描述" prop="description">
          </el-table-column>
          <el-table-column label="提交时间" prop="projectOnlineDTO.createTime" width="180">

          </el-table-column>

          <el-table-column label="状态" prop="projectOnlineDTO.status" :formatter="statusFormat">

          </el-table-column>
          <el-table-column label="审核人" prop="projectOnlineDTO.reviewUserName">

          </el-table-column>
          <el-table-column label="审核备注" prop="projectOnlineDTO.reviewNotes">
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="updateProject(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="footer" clearfix>
          <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1" :current-page="currentPage1"
            :page-sizes="[10, 50, 100, 200]" :page-size="pageSize1" layout="total, sizes, prev, pager, next, jumper"
            :total="total1">
          </el-pagination>
        </div>
      </div>

      <!-- 分组管理 -->
      <div v-show="flag">
        <div class="toolbar1">
          <el-form :inline="true" size="mini" :model="formInline" class="demo-form-inline">
            <el-form-item>
              <input v-model="formInline.groupName" class="el-input__inner group_input" placeholder="分组名称"></input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="getGroup()" icon="el-icon-search"></el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="deleteRows" :loading="deleteLoading">删除</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table :data="tableData" border style="width: 100%;table-layout: fixed;" height="400" @selection-change="handleSelectionChange"
          v-loading="loading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="分组名称" prop="groupName">
          </el-table-column>

          <el-table-column label="分组描述" prop="describe">
          </el-table-column>

          <el-table-column label="分组类型" :formatter="formatType">
          </el-table-column>

          <el-table-column prop="enabled" sortable label="是否启用" :formatter="formatterEnable">
          </el-table-column>

          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="editData(scope.row)">编辑</el-button>
            </template>
          </el-table-column>


        </el-table>
        <div class="footer" clearfix>
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
            :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
          </el-pagination>
        </div>

      </div>
    </el-dialog>


    <el-dialog :title="title" :visible.sync="addGroupViaiable" width="30%" class="dialog_temp" :before-close="close"
      :close-on-click-modal='false'>
      <el-form :model="editForm" label-width="100px" ref="editForm" :rules="rules">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model="editForm.groupName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组描述" prop="describe">
          <el-input v-model="editForm.describe" rows="2" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组排序" prop="groupOrder">
          <el-input v-model="editForm.groupOrder" type="number"></el-input>
        </el-form-item>

        <el-form-item label="是否启用" class="plug-msg">
          <el-switch v-model="editForm.enabled" active-color="#13ce66" inactive-color="#c91e3a" active-value="T"
            inactive-value="F">
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeAdd()">取 消</el-button>

        <el-button type="primary" @click="addData" :loading="addLoading">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 项目上线 -->
    <el-dialog :title="lineTile" :visible.sync="lineVisiable" class="dialog_temp" width="30%" :before-close="closeLine"
      :close-on-click-modal='false'>
      <div style="width: 100%;height: 300px;overflow: auto;">
        <el-form :model="lineForm" ref='lineForm' label-width="100px" :rules="lineFormRules">
          <el-form-item label="审核人" prop="userName">
            <el-select v-model="lineForm.userName" placeholder="请选择审核人">
              <el-option v-for="item in userNames" :key="item.userNames" :value="item.userId" :label="item.describe"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运行配置" prop="config">
            <!-- <el-input v-model="lineForm.config" style="width:220px" :readonly="true"></el-input>
            <el-button type="primary" @click="moreConfig">更多</el-button> -->
            <el-select v-model="lineForm.config" clearable placeholder="请选择">
              <el-option v-for="item in frequencys" :key="item.frequencyId" :label="item.frequencyDescription" :value="item.frequencyId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="运行时间" prop="configTime">
            <el-time-picker v-model="lineForm.configTime" :picker-options="{
                  selectableRange: '00:00:00 - 23:59:59'
                }"
              placeholder="请选择运行时间" value-format="HHmmss">
            </el-time-picker>
          </el-form-item>
          <el-form-item label="上线说明">
            <el-input type="textarea" v-model="lineForm.remark"></el-input>
          </el-form-item>
        </el-form>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeLine">取 消</el-button>
        <el-button type="primary" @click="lineSub" :loading="lineLoading">确 定</el-button>
      </span>

    </el-dialog>

    <!-- 撤回上线 -->
    <el-dialog :title="backTitle" :visible.sync="backFormVisiable" width="30%" class="dialog_temp"
      :close-on-click-modal='false'>
      <div style="width: 100%;height: 300px;overflow: auto;">
        <el-form :model="backForm" class="demo-form-inline" ref='backForm' label-width="100px">

          <el-form-item label="撤回说明">
            <el-input type="textarea" rows="4" v-model="backForm.remark"></el-input>
          </el-form-item>

        </el-form>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="backFormVisiable=false">取 消</el-button>
        <el-button type="primary" @click="backSubmit">确 定</el-button>
      </span>

    </el-dialog>

    <!-- 更多配置 -->
    <el-dialog title="选择运行配置" :visible.sync="moreVisiable" class="dialog_temp" width="20%" :close-on-click-modal='false'>
      <el-table :data="moreConfigData" border style="width: 100%;table-layout: fixed;" height="300" v-loading="moreConfigLoading"
        highlight-current-row @current-change="selectCurrentConfig" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
        <el-table-column prop="frequencyName" label="配置描述" sortable>
        </el-table-column>
        <el-table-column prop="frequencyCron" label="表达式" sortable>
        </el-table-column>
      </el-table>
      <div class="footer" <span slot="footer" class="dialog-footer">
        <el-button @click="moreVisiable=false">取 消</el-button>
        <el-button type="primary" @click="subConfig">确 定</el-button>
        </span>

    </el-dialog>

    <!-- 项目授权 -->
    <el-dialog :title="grantTitle" :visible.sync="grantProject" class="dialog_temp" width="30%" :before-close="cloaseGrant"
      :close-on-click-modal='false'>
      <div class="grantUser">
        <span>已授权用户</span>
        <span style="margin-left:150px">
          <el-button size="mini" type="primary" @click="closeAllUser">清空</el-button>
        </span>
        <span style="margin-left:80px">
          <el-button size="mini" type="primary" @click="openUser">添加用户</el-button>
        </span>
      </div>
      <div class="body_list">
        <div style="float: left;margin-left:20px;" class="select_user" v-for="item in grantUsers">
          {{item.describe}}
          <span class="el-icon-close" @click.prevent.stop='closeSelectedTag(item)'></span>
        </div>

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="grantProject= false">取 消</el-button>
        <el-button size="mini" type="primary" @click="grantProjectUser">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 添加用户 -->
    <el-dialog title="请选择用户" :visible.sync="selectUserVisiable" width="30%" class="dialog_temp" :before-close="cloaseAddUser"
      :close-on-click-modal='false'>
      <div class="toolbar">
        <el-form :inline="true" :model="userForm" class="demo-form-inline">
          <el-form-item>
            <input v-model="userForm.userName" class="el-input__inner group_input" placeholder="用户名称"></input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="getUsers()" icon="el-icon-search"></el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="userData" border style="width: 100%;table-layout: fixed;" height="300" @selection-change="selectUser"
        v-loading="getUserLoading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column prop="describe" label="用户名称" sortable>
        </el-table-column>

      </el-table>
      <div class="footer" clearfix>
        <el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2" :current-page="currentPage"
          :page-sizes="[10, 50, 100, 200]" :page-size="pageSize2" layout="total, sizes, prev, pager, next, jumper"
          :total="total2">
        </el-pagination>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="selectUserVisiable= false">取 消</el-button>
        <el-button size="mini" type="primary" @click="selectUserList">确 定</el-button>
      </span>
    </el-dialog>


    <!-- 项目编辑 -->
    <el-dialog :visible.sync="updateProjectDisiable" class="dialog_temp" title="项目编辑" width="30%" :close-on-click-modal='false'>
      <el-form label-position="left" label-width="80px" :model="proejctFrom">
        <el-form-item label="项目名称" :rules="[{required:true,trigger:'blur',message:'项目名称不能为空'}]">
          <el-input v-model="proejctFrom.projectName"></el-input>
        </el-form-item>
        <el-form-item label="所属分组" prop="groupId">
          <el-select v-model="proejctFrom.groupId">
            <el-option v-for="item in groupLists" :key="item.groupId" :value="item.groupId" :label="item.groupName"></el-option>
          </el-select>
        </el-form-item>
        </el-form-item>
        <el-form-item label="项目描述">
          <el-input v-model="proejctFrom.description"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="updateProjectDisiable= false">取 消</el-button>
        <el-button size="mini" type="primary" @click="subProject" :loading="updateLoading">确 定</el-button>
      </span>
    </el-dialog>


    <div class="tree_datas" v-loading="treeLading">
      <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick" :indent="25" node-key="id"
        :filter-node-method="filterNode" ref="tree" :default-expanded-keys="[1]" :highlight-current="true">
        <span class="custom-tree-node" slot-scope="{node,data}">
          <i :class='data.projectImage'></i>
          <span class="node" @dblclick="showProject(data)">{{node.label}}</span>
        </span>
      </el-tree>
    </div>
  </div>
</template>

<script>
  import {
    getGroup,
    addGroup,
    deleteGroup,
    updateGroup,
    checkGroupName,
    selectProjectByGroupName,
    // 项目管理
    selectAllProjectBygroupId, //根据项目名称模糊查询项目信息
    deletePrjects, //批量删除项目
    selectGrantProjectUser, //获取项目已授权的用户
    reGrant, //项目授权,
    updateProject, //修改项目
    getUser,
    selectNotGrantUser,
    selectProjectByProjectName, //获取我的项目
    selectGrantProject, //获取他人授予我的项目
    deleteGroups, //批量删除分组
    getProjectVo,
    projectOnLine, //项目上线
    getFrequency, //获取运行配置
    getAudingUser, //获取审核用户
    cancelProjectLine, //撤回项目上线
    getModelInfo
  } from '../../api/api.js'
  import util from '../../common/utils.js'
  import {
    routerMap
  } from '@/router'
  export default {
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },
    data() {
      var checkOrder = (rule, value, callback) => {

        setTimeout(() => {
          if (!Number.isInteger(value)) {
            callback(new Error('请输入数字值'));
          } else {

            callback();

          }
        }, 1000);
      };
      return {
        lineLoading: false,
        frequencys: [],
        moreConfigData: [],
        moreConfigLoading: false,
        moreVisiable: false,
        treeLading: false,
        showClose: false,
        deleteLoading: false,
        proejctFrom: {
          projectName: '',
          groupId: '',
          description: '',
        },
        updateProjectDisiable: false,
        // 撤回修改
        backForm: {
          remark: '',
        },
        backFormVisiable: false,
        backTitle: '',
        lineTile: '提交上线',
        filterText: '',
        lineVisiable: false,
        lineForm: {
          userName: '',
          config: '',
          configId: '',
          configTime: '',
          remark: '',
        },
        lineFormRules: {
          userName: [{
            required: true,
            trigger: 'change',
            message: '请选择审核人'
          }],
          config: [{
            required: true,
            trigger: 'change',
            message: '请选择运行配置',
          }],
          configTime: [{
            required: true,
            trigger: 'change',
            message: '请选择运行时间'
          }],
        },
        userNames: [], //所有用户
        data: [{
          id: 1,
          label: '我的项目',
          projectImage: 'iconfont icon-wenjianjia1',
          children: [],
        }, {
          id: 2,
          label: '他人项目',
          projectImage: 'iconfont icon-wenjianjia1',
          children: [],
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        nodeName: '',
        getUserLoading: false,
        userData: [],
        selectUsers: [],
        selectUserVisiable: false,
        dialogVisible: false,
        tableData: [],
        projectData: [], //项目表格数据
        selectGroup: [], //选择的分组数据
        currentPage: 1,
        pageSize: 10,
        total: 0,
        currentPage2: 1,
        pageSize2: 10,
        total2: 0,
        projectFlag: true,
        grantUsers: [],
        currentPage1: 1,
        pageSize1: 10,
        total1: 0,
        loading: true,
        projectForm: {
          projectName: '',
          groupId: '',
        },
        formInline: {
          groupName: ''
        },
        editForm: {
          groupName: '',
          describe: '',
          groupOrder: 99,
          groupType: 'JCJB',
          enabled: 'T'
        },
        projectLoading: true,
        groupIds: [], //所有分组
        dis: false,
        flag: false, //控制显示分组管理还是项目管理
        rules: {
          groupName: [{
            required: true,
            trigger: 'blur',
            message: '分组名称不能为空'
          }],
          describe: [{
            required: true,
            trigger: 'blur',
            message: '分组描述不能为空',
          }],
          groupType: [{
            required: true,
            trigger: 'blur',
            message: '分组类型不能为空'
          }],
        },


        selectProject: [],
        title: '新增',
        addGroupViaiable: false,

        grantProject: false, //项目授权页面
        updateLoading: false,
        grantTitle: '',
        projectId: '',
        userForm: {
          userName: ''
        },
        addLoading: false,
        groupLists: [],
        frequencyId: '',
        frequencyName: '',
        subDis: true,
      };
    },
    mounted() {
      this.getTreeData();
      util.$on("openMannager", () => {
        this.openMannager();
      })
      // this.getProject();
      // this.getGroup();
    },
    methods: {

      // 清空筛选框
      clear() {
        this.filterText = '';
      },
      // 格式化审核状态
      statusFormat(data) {
        if (data.projectOnlineDTO !== null) {
          if (data.projectOnlineDTO.status === 'CHECKING') {
            return '审核中';
          } else if (data.projectOnlineDTO.status === 'REFUSE') {
            return '驳回';
          } else if (data.projectOnlineDTO.status === 'OFFLINE') {
            return '下线'
          } else if (data.projectOnlineDTO.status === 'CANCEL') {
            return '撤回中'
          } else if (data.projectOnlineDTO.status === 'ONLINE') {
            return '上线'
          }
        }
      },

      clickTest() {
        this.filterText = '';
      },
      // 双击显示项目信息
      showProject(datas) {
        //判断项目是否是本人创建，在画布中保存项目时先判断是否是本人创建的如果不是点击保存时默认时新增一个项目
        let createFlag = true; //createFlag为true默认是本人创建
        if(datas.projectOnlineDTO === null){
            createFlag  = true;
        }else{
          createFlag = false;
        }
        //判断节点是否是项目，是的话跳转到对应项目的集成页面s
        if (datas.children === undefined) {
          let projectId = datas.projectId;
          let projectName = datas.projectName;
          // 双击获取项目信息
          let path = '/' + datas.label;
          let newPath = '/project/' + projectId;
          let name = datas.projectName;
           let createFlagData={
             key : newPath,
             value:createFlag,
           }
           this.$store.dispatch('setCreateFlag',JSON.stringify(createFlagData));
          //存储项目信息
          // 动态路由跳转
          // let routers = routerMap.concat([{
          //   path: '/genneralView',
          //   // name: '首页',
          //   component: resolve => require(['../HomePage.vue'], resolve),
          //   children: [{
          //     path: newPath,
          //     name: name,
          //     component: resolve => require(['../jsplumpVue/flowMain.vue'], resolve),
          //   }]
          // }]);
          this.$store.dispatch('addRouteTitle', name);
          this.$store.dispatch('deleteExecuteLog', newPath);
          this.$store.dispatch('setFlowMainLoading', true);
          // this.$router.addRoutes(routers);
          // let newPath = '/project/' + projectName
          // 添加项目信息
          let param = {
            key: newPath,
            value: {
              projectId: projectId,
              projectName: projectName,
            }
          };
          util.$emit('changeControl', 'test');
          this.$store.dispatch('addProjects', param);
          this.$router.push(newPath);
          getProjectVo(projectId).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              let content = data.content;
              let projectId = content.projectId;
              let projectFile = JSON.parse(content.projectFile);
              let nodeList = projectFile.transformation.nodeList;
              // 动态路由打开界面
              for (let i = 0; i < nodeList.nodeList.length; i++) {
                let map = {
                  key: nodeList.nodeList[i].id,
                  value: nodeList.nodeList[i].label,
                }
                this.$store.dispatch('setMap', JSON.stringify(map));
              }
              //遍历取出过滤插件信息保存 label和id映射

              // 添加项目节点到store里面
              let params = {
                key: newPath,
                value: nodeList,
              }
              // for (let i = 0; i < nodeList.nodeList.length; i++) {
              //   let map = {
              //     key: nodeList.nodeList[i].id,
              //     value: nodeList.nodeList[i].label,
              //   }
              //   this.$store.dispatch('setMap', JSON.stringify(map));
              // }

              this.$store.dispatch('addFlowData', params)

              // 拆分步骤信息并添加
              let steps = projectFile.transformation.step;
              if (steps === undefined) {
                this.$message({
                  message: '获取项目信息失败',
                  type: 'error'
                })
              }
              for (let i = 0; i < steps.length; i++) {
                let modelType = '';
                nodeList.nodeList.forEach((item, index) => {
                  if (item.modelName !== undefined && steps[i].modelName !== undefined && item.modelName ===
                    steps[i].modelName) {
                    modelType = item.modelType;
                  }
                })
                //获取模型数据源信息
                if (modelType === '') {
                  let stepData = {
                    key: newPath,
                    step: steps[i],
                  };
                  this.$store.dispatch('addSteps', stepData);
                } else {
                  let requestParam = {
                    modelName: steps[i].modelName,
                    modelType: modelType
                  }
                  getModelInfo(requestParam).then(res => {
                    steps[i].connection = res.data.content.datasourceName;
                    let stepData = {
                      key: newPath,
                      step: steps[i],
                    };
                    this.$store.dispatch('addSteps', stepData);
                  })
                }
              }
              // 拆分数据源信息并添加
              let lines = projectFile.transformation.order.hop;
              if (lines !== undefined) {
                for (let i = 0; i < lines.length; i++) {
                  let lineData = {
                    key: newPath,
                    lines: lines[i],
                  };
                  this.$store.dispatch('addLines', lineData);
                }
              }

              //拆分连接线信息并添加
              let connection = projectFile.transformation.connection;
              for (let i = 0; i < connection.length; i++) {
                let connectionData = {
                  key: newPath,
                  connection: connection[i],
                };
                this.$store.dispatch('addConnection', connectionData);
              }
              // 添加项目执行文件
              projectFile.transformation.step = [];
              projectFile.transformation.order.hop = [];
              projectFile.transformation.connection = [];
              let parameter = {
                key: newPath,
                value: projectFile,
              }
              this.$store.dispatch('addDataJson', parameter);
              this.$store.dispatch('setFlowMainLoading', false);
              util.$emit('changeLoadingFalse');

            }
          })
        }

        // 双击项目获取项目信息
      },
      formatterEnable(data) {
        if (data.enabled === 'T') {
          return '启用'
        } else {
          return '不启用'
        }
      },

      //批量删除分组
      deleteRows() {
        this.$confirm('确定删除选中项目？').then(_ => {
          let id = [];
          this.deleteLoading = true;
          for (let i = 0; i < this.selectGroup.length; i++) {
            id.push(
              this.selectGroup[i].groupId
            )
          }
          deleteGroups(id).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              this.$message({
                message: '删除成功',
                type: 'success',
              })
            } else {
              this.$message({
                message: '删除失败',
                type: 'error'
              })
            }
            this.deleteLoading = false;
            this.getGroup();
          })
        })

      },
      // 修改项目
      subProject() {
        let para = Object.assign({}, this.proejctFrom)
        para.groupId = this.proejctFrom.groupId.split(",")[0];
        para.groupName = this.proejctFrom.groupId.split(",")[1];
        updateProject(para).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.$message({
              message: '编辑成功',
              type: "success",
            })
            this.updateLoading = false;
            this.updateProjectDisiable = false;
            this.getProject();
          } else {
            this.$message({
              message: '编辑失败',
              type: 'error'
            })
          }


        })
      },
      // 获取更多配置
      moreConfig() {
        let params = {
          frequencyName: '',
        }
        getFrequency(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            let frequencys = [];
            for (let i = 0; i < data.content.list.length; i++) {
              frequencys.push({
                frequencyId: data.content.list[i].frequencyId + ',' + data.content.list[i].frequencyDescription,
                frequencyDescription: data.content.list[i].frequencyDescription,
              })
            }
            this.frequencys = frequencys;
            this.moreConfigData = data.content.list;
          }
        })
      },

      // 当前选中的行
      selectCurrentConfig(val) {
        this.frequencyId = val.frequencyId;
        this.frequencyName = val.frequencyName;
      },
      // 选择更多配置
      subConfig() {
        this.lineForm.config = this.frequencyName;
        this.lineForm.configId = this.frequencyId;
        this.moreVisiable = false;
      },
      // 打开提交上线页面
      subLine() {
        this.lineForm = {
          userName: '',
          config: '',
          configId: '',
          configTime: '',
          remark: '',
        };
        this.lineTile = '提交上线：'
        let flag = true;
        for (let i = 0; i < this.selectProject.length; i++) {
          if (i === 0) {
            this.lineTile = this.lineTile + this.selectProject[i].projectName;
          } else {
            this.lineTile = this.lineTile + ',' + this.selectProject[i].projectName;
          }
          if (this.selectProject[i].projectOnlineDTO !== null) {
            if (this.selectProject[i].projectOnlineDTO.status === 'ONLINE' || this.selectProject[i].projectOnlineDTO.status ===
              'CHECKING') {
              this.$message({
                message: this.selectProject[i].projectName + "已经提交上线，请重新选择",
                type: 'error',
              })
              return;
            }
          }
        }
        this.lineVisiable = true;
        this.moreConfig();
        this.getUser();

      },
      // 撤回修改
      backUpdate(row) {
        this.backTitle = "撤回项目："
        for (let i = 0; i < this.selectProject.length; i++) {
          if (i === 0) {
            this.backTitle = this.backTitle + this.selectProject[i].projectName
          } else {
            this.backTitle = this.backTitle + "," + this.selectProject[i].projectName
          }
          if (this.selectProject[i].projectOnlineDTO === null || this.selectProject[i].projectOnlineDTO.status ===
            'REFUSE' || this.selectProject[i].projectOnlineDTO.status ===
            'OFFLINE') {
            this.$message({
              message: '未提交上线，无需撤回',
              type: 'error',
            })
            return;
          }
        }
        this.backForm = {
          remark: '',
        }
        this.backFormVisiable = true;
      },
      backSubmit() {
        let projects = [];
        for (let i = 0; i < this.selectProject.length; i++) {
          projects.push(this.selectProject[i].projectId)
        }
        let params = {
          projectId: projects,
        }
        cancelProjectLine(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.$message({
              message: '撤回成功',
              type: 'success'
            })
            this.getProject();
            this.backFormVisiable = false;
          } else {
            this.$message({
              message: '撤回失败',
              type: 'success'
            })
            this.getProject();
            this.backFormVisiable = false;
          }
        })
        // this.backFormVisiable = false;

        // this.$message({
        //   message: '撤回成功',
        //   type: 'success'
        // });
      },
      lineSub() {
        this.$refs['lineForm'].validate((valid) => {
          if (valid) {
            let parameter = [];
            for (let i = 0; i < this.selectProject.length; i++) {
              parameter.push({
                createUserName: this.$store.getters.getUserName,
                projectId: this.selectProject[i].projectId,
                projectName: this.selectProject[i].projectName,
                description: this.lineForm.remark, //上线说明
                frequencyId: this.lineForm.config.split(',')[0], //运行配置说明
                reviewUserId: this.lineForm.userName.split(",")[0], //审核人id
                reviewUserName: this.lineForm.userName.split(",")[1], //审核人名称
                runStartTime: this.lineForm.configTime,
              })
            }
            this.lineLoading = true;
            projectOnLine(parameter).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '提交上线成功',
                  type: 'success'
                })
                this.lineLoading = false;
                this.lineVisiable = false;
                this.getProject();
                this.$refs['lineForm'].resetFields();
              } else {
                this.$message({
                  message: '提交上线失败',
                  type: 'error'
                })
                this.lineLoading = false;
                this.lineVisiable = false;
                this.getProject();
                this.$refs['lineForm'].resetFields();
              }
            })
          }
        })
      },
      // 关闭
      closeLine() {
        this.$refs['lineForm'].resetFields();
        this.lineVisiable = false;
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      handleNodeClick(data) {
        // util.$emit('changeControl','test');
      },
      db(data) {
        this.$router.push('/codemirror');
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
          this.data[1].children = children;
        })
      },
      getProject() {
        this.projectData = [];
        let params = {
          pageNum: this.currentPage1,
          pageSize: this.pageSize1,
          projectName: this.projectForm.projectName,
          groupId: this.projectForm.groupId,
          projectType: 'JCJB',
        }
        this.projectLoading = true;
        selectAllProjectBygroupId(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.projectData = data.content.list;
            this.total1 = data.content.total;
            this.projectLoading = false;
          }
        });
      },

      //打开详情信息
      openMannager() {
        this.dialogVisible = true;
        this.changeProjectManager();
      },

      changeProjectManager() {
        this.$nextTick(() => {
          this.flag = false;
          this.projectFlag = true;
          this.selectGroup = [];
          this.selectProject = [];

          document.getElementById("headerSpan_right").className = "headerSpan_right";
          document.getElementById("headerSpan_left").className = "headerSpan_left_active";
          this.getGroup();
          this.getProject();

        })
      },

      changeGroupManager() {
        this.$nextTick(() => {
          this.projectFlag = false;
          this.flag = true;
          this.selectGroup = [];
          this.selectProject = [];
          document.getElementById("headerSpan_left").className = "headerSpan_left";
          document.getElementById("headerSpan_right").className = "headerSpan_right_active";
        })


        this.getGroup();

      },

      // 选中的项目信息
      handleSelectionChange2(sels) {
        this.selectProject = sels;
        this.subDis = true;
        for (let i = 0; i < sels.length; i++) {
          if (this.selectProject[i].projectOnlineDTO == null || this.selectProject[i].projectOnlineDTO.status ===
            'REFUSE') {
            this.subDis = false;
          } else {
            this.subDis = true;
            return;
          }
        }
      },

      // 选中的项目信息
      handleSelectionChange(sels) {
        this.selectGroup = sels;
      },
      closeAdd() {
        this.editForm = {
          groupName: '',
          describe: '',
          groupOrder: 99,
          groupType: 'JCJB',
          enabled: 'T'
        };

        this.addGroupViaiable = false;
      },
      formatEnable(data) {
        if (data.enabled === 'T') {
          return '启用'
        } else {
          return '不启用'
        }
      },
      formatType(data) {

        if (data.groupType = 'JCJB') {
          return '集成脚本'
        } else if (data.groupType = 'JBMX') {
          return '基本模型'
        } else if (data.groupType = 'YWMX') {
          return '业务模型'
        } else if (data.groupType = 'SCLMX') {
          return '输出类模型'
        } else if (data.groupType = 'ZDYMX') {
          return '自定义模型'
        } else if (data.groupType = 'JCJB') {
          return '报表'
        } else if (data.groupType = 'BB') {
          return '集成脚本'
        } else if (data.groupType = 'XM') {
          return '项目'
        }
      },

      // 获取分组信息
      getGroup() {
        this.tableData = [];
        this.groupIds = [];
        let para = {
          groupName: this.formInline.groupName,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
        getGroup(para).then(res => {
          let {
            data
          } = res;
          let datas = [];
          let groupList = [];
          if (data.code === '10000') {
            this.tableData = data.content.list;
            this.total = data.content.total;
            for (let i = 0; i < data.content.list.length; i++) {
              datas.push({
                groupId: data.content.list[i].groupId,
                groupName: data.content.list[i].groupName,
              })
              groupList.push({
                groupId: data.content.list[i].groupId + "," + data.content.list[i].groupName,
                groupName: data.content.list[i].groupName,
              })
            }
            this.groupIds = datas;
            this.groupLists = groupList;
          }
          this.loading = false;
        });
      },

      //打开新增分组页面
      editData(row) {
        if (row === undefined) {
          this.dis = false;
          this.title = '新增';
          this.editForm = {
            groupName: '',
            describe: '',
            groupOrder: 99,
            groupType: 'JCJB',
            enabled: 'T'
          };
        } else {
          this.title = '编辑';
          this.dis = true;
          this.editForm = Object.assign({}, row);
        }
        this.addGroupViaiable = true;

      },

      handleSizeChange(pageSize) {
        this.pageSize = pageSize;
        this.getGroup();
      },
      handleCurrentChange(currentPage) {
        this.currentPage = currentPage;
        this.getGroup();
      },

      handleSizeChange1(pageSize) {
        this.pageSize1 = pageSize;
        this.getProject();
      },
      handleCurrentChange1(currentPage) {
        this.currentPage1 = currentPage;
        this.getProject();
      },
      handleSizeChange2(pageSize) {
        this.pageSize2 = pageSize;
        this.getUsers();
      },
      handleCurrentChange2(currentPage) {
        this.currentPage2 = currentPage;
        this.getUsers();
      },
      // 新增数据
      addData() {
        this.$refs['editForm'].validate((valid) => {
          if (!valid) {} else {
            let para = Object.assign({}, this.editForm);
            let params = {
              groupName: para.groupName,
              groupType: para.groupType,
            }
            this.addLoading = true;
            checkGroupName(params).then(res => {
              if (res.data.code === '10000' && res.data.content === false) {
                if (this.title === '新增') {
                  addGroup(para).then(res => {
                    let {
                      data
                    } = res;
                    if (data.code === '10000') {
                      this.$message({
                        message: '新增成功',
                        type: 'success'
                      })
                    } else {
                      this.message({
                        message: '新增失败',
                        type: 'error'
                      })
                    }
                    this.$refs['editForm'].resetFields();
                    this.addLoading = false;
                    this.getGroup();
                    this.addGroupViaiable = false;
                  })
                } else {

                  updateGroup(para).then(res => {
                    let {
                      data
                    } = res;
                    if (data.code === '10000') {
                      this.$message({
                        message: '编辑成功',
                        type: 'success'
                      })
                    } else {
                      this.message({
                        message: '编辑失败',
                        type: 'error'
                      })
                    }
                    this.$refs['editForm'].resetFields();
                    this.addLoading = false;
                    this.getGroup();
                    this.addGroupViaiable = false;
                  })
                }
              } else {
                this.$message({
                  message: '分组已存在，请修改分组名称或分组类型',
                  type: 'error'
                })
                this.addLoading = false;
              }
            })
            // if(this.title==='新增'){
            //   //判断分组是否存在



            //   addGroup(para).then(res=>{

            //   })
            // }
          }
        });
      },

      // 批量删除项目
      deleteProjects() {
        let flag = true;
        let ids = []
        if (this.selectProject.length == 0) {}
        for (let i = 0; i < this.selectProject.length; i++) {
          if (this.selectProject[i].projectOnlineDTO !== null && this.selectProject[i].projectOnlineDTO.status ===
            'ONLINE') {
            this.$message({
              message: '存在一上线的项目，请重新选择',
              type: 'warn',
            })
            flag = false;
            break;
          } else {
            ids.push(this.selectProject[i].projectId);
          }
        }
        let projectIds = '';
        for (let i = 0; i < ids.length; i++) {
          projectIds = ids[i] + ',' + projectIds;
        }
        let para = {
          projectIds: projectIds,
        }
        if (flag) {
          this.$confirm('确定删除选中项目？').then(_ => {
            deletePrjects(para).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '删除数据成功',
                  type: 'success',
                })
                this.projectLoading = false;
                this.getProject();
              } else {
                this.$message({
                  message: '删除数据失败',
                  type: 'error',
                })
                this.projectLoading = false;
                this.getProject();
              }
            })
          }).catch(_ => {});
        }

        // deletePrjects().then(_=>{

        // })
      },

      deleteRow(row) {
        let params = {
          groupId: row.groupId,
          pageSize: 1,
          pageNum: 1,
          projectType: 'JCJB',
          projectName: '',
        }

        selectProjectByGroupName(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000' && data.content.total > 0) {
            this.$message({
              message: '分组下面存在项目，不能直接删除',
              type: 'error',
            })
          } else {
            this.$confirm('确认删除？')
              .then(_ => {
                deleteGroup(row.groupId).then(res => {
                  if (data.code === '10000') {
                    this.$message({
                      message: '删除成功',
                      type: 'success',
                    })
                    this.getGroup();
                  } else {
                    this.$message({
                      message: '删除失败',
                      type: 'error'
                    })
                    this.getGroup();
                  }
                })
              })
              .catch(_ => {});
          }
        })
      },
      updateProject(row) {
        this.updateProjectDisiable = true;
        this.proejctFrom = Object.assign({}, row);
        this.proejctFrom.groupId = row.groupId + ',' + row.groupName;
        // this.$confirm("确定提交修改数据？").then(_ => {
        //   let flag = false;
        //   this.updateLoading = true;
        //   let params = Object.assign({}, row);
        //   updateProject(params).then(res => {
        //     if (res.data.code === '10000') {
        //       flag = true;
        //     } else {
        //       this.$message({
        //         message: '编辑失败',
        //         type: 'error',
        //       })
        //       this.dialogVisible = false;
        //       this.updateLoading = false;
        //     }
        //     if (flag) {
        //       this.$message({
        //         message: '编辑成功',
        //         type: 'success',
        //       })
        //       this.dialogVisible = false;
        //       this.updateLoading = false;
        //       this.getProject();
        //     }
        //   })

        // }).catch(_ => {});
      },
      // 修改数据
      updateData() {
        this.updateLoading = true;
        if (this.projectFlag === true) {
          this.$confirm("确定提交修改数据？").then(_ => {
            let flag = false;
            this.updateLoading = true;
            for (let i = 0; i < this.projectData.length; i++) {
              let para = this.projectData[i];
              let params = Object.assign({}, para);
              updateProject(params).then(res => {
                if (res.data.code === '10000') {
                  flag = true;
                } else {
                  this.$message({
                    message: '编辑失败',
                    type: 'error',
                  })
                  this.dialogVisible = false;
                  this.updateLoading = false;
                }
                if (flag) {
                  this.$message({
                    message: '编辑成功',
                    type: 'success',
                  })
                  this.dialogVisible = false;
                  this.updateLoading = false;
                  this.getProject();
                }
              })

            }
          }).catch(_ => {});
        } else {
          this.$confirm("确定提交修改数据？").then(_ => {
            let flag = false;
            this.updateLoading = true;
            for (let i = 0; i < this.tableData.length; i++) {
              let para = this.tableData[i];
              let params = Object.assign({}, para);
              updateGroup(para).then(res => {
                let {
                  data
                } = res;
                if (data.code === '10000') {
                  flag = true;
                } else {
                  this.message({
                    message: '编辑失败',
                    type: 'error'
                  })
                  this.dialogVisible = false;
                  this.updateLoading = false;
                }
                if (flag) {
                  this.$message({
                    message: '编辑成功',
                    type: 'success',
                  })
                  this.dialogVisible = false;
                  this.updateLoading = false;
                  this.getGroup();
                }

              })

            }

          }).catch(_ => {});

        }

      },

      // 项目分享
      grantUser() {
        if (this.selectProject.length > 1 || this.selectProject.length === 0) {
          this.$message({
            message: '请选择一个项目分享'
          });
        } else {
          let param = {
            projectId: this.selectProject[0].projectId,
            pageNum: 1,
            pageSize: 100,
          }
          this.projectId = this.selectProject[0].projectId,
            this.grantTitle = this.selectProject[0].projectName + '项目分享';
          selectGrantProjectUser(param).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              this.grantUsers = data.content.list;
            }
          })
          this.grantProject = true;
        }

      },


      // 获取用户列表
      getUser() {
        getAudingUser().then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {

            let users = [];
            for (let i = 0; i < data.content.length; i++) {
              users.push({
                userId: data.content[i].userId + ',' + data.content[i].describe,
                describe: data.content[i].describe,
              });
            }
            this.userNames = users;
          }
        })
      },

      getUsers() {
        this.getUserLoading = true;
        let params = {
          keyWord: this.userForm.userName,
          curPage: this.currentPage2,
          pageSize: this.pageSize2
        }
        selectNotGrantUser(this.projectId).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.userData = data.content;
            this.total2 = data.content.length;
          }
        })
        this.getUserLoading = false;
      },

      openUser() {
        this.selectUserVisiable = true;
        this.getUsers();
      },

      // 获取添加的用户列表
      selectUserList() {
        let userProject = [];
        for (let i = 0; i < this.selectUsers.length; i++) {
          userProject.push({
            opModel: 'RW',
            userId: this.selectUsers[i].userId,
          })
          this.grantUsers.push({
            userId: this.selectUsers[i].userId,
            describe: this.selectUsers[i].describe
          })
        }
        // let params={
        //   projectId:this.projectId,
        //   projectUserGrants:userProject
        // }
        // console.info(params);

        // reGrant(params).then(res=>{
        //   let {
        //     data
        //   }=res;
        //   if(data.code==='10000'){
        //     this.$message({
        //       message:'分享成功',
        //       type:'success',
        //     })
        //     this.grantUser();

        //   }
        // });
        this.selectUserVisiable = false;

      },
      selectUser(sels) {
        this.selectUsers = sels;
      },
      //项目授权
      grantProjectUser() {
        let userProject = [];
        for (let i = 0; i < this.grantUsers.length; i++) {
          userProject.push({
            opModel: 'RW',
            userId: this.grantUsers[i].userId,
          })
        }
        let params = {
          projectId: this.projectId,
          projectUserGrants: userProject
        }
        reGrant(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.$message({
              message: '分享成功',
              type: 'success',
            })
          } else {
            this.$message({
              message: '分享失败',
              type: 'error',
            })
          }
          this.grantProject = false;
        });
      },

      // 清空已授权用户
      closeAllUser() {
        this.grantUsers = [];
      },
      closeSelectedTag(tag) {
        let newTag = [];
        for (let i = 0; i < this.grantUsers.length; i++) {
          if (this.grantUsers[i].userId === tag.userId) {

          } else {
            newTag.push({
              userId: this.grantUsers[i].userId,
              describe: this.grantUsers[i].describe,
            })
          }
        }
        this.grantUsers = newTag;
      },
      handleClose(done) {
        this.dialogVisible = false;
        this.selectUsers = [];
        this.selectProject = [];
      },
      close(done) {
        this.addGroupViaiable = false;
        this.$refs['editForm'].resetFields();
      },

      cloaseGrant(done) {
        this.grantProject = false;
      },

      cloaseAddUser(done) {
        this.selectUserVisiable = false;
      }
    }
  };
</script>

<style>
  .el-table--enable-row-transition .el-table__body td {
    padding: 0;
    -webkit-transition: background-color .25s ease;
    transition: background-color .25s ease;
  }

  .el-table td,
  .el-table th.is-leaf {
    padding: 0;
    border-bottom: 1px solid #ebeef5;
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
  }

  .header_tools {
    margin-top: 5px;
    height: 40px;
    line-height: 40px;
  }


  .header_tools .el-input--suffix .el-input__inner {
    padding-right: 30px;
    border-radius: 20px;
    height: 30px;
  }

  .header_tools .el-input__icon {
    height: 100%;
    width: 25px;
    text-align: center;
    -webkit-transition: all .3s;
    transition: all .3s;
    line-height: 30px;
  }

  .header_tools .iconfont {
    font-family: "iconfont" !important;
    font-size: 20px;
    color: #1eaafa;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }

  .tree_data {
    /* height:100%; */
    width: 19%;
    margin-left: 10px;
    height: calc(100% - 50px);
    height: -moz-calc(100% - 50px);
    height: -webkit-calc(100% - 50px);
    /* overflow: auto; */
    position: absolute;

  }

  .tree_datas {
    width: 100%;
    height: calc(100% - 101px);
    height: -moz-calc(100% - 101px);
    height: -webkit-calc(100% - 101px);
    overflow: auto;
    position: absolute;
    margin-top: 8px;
  }

  .tree_data .el-tree-node__content {
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    height: 26px;
    cursor: pointer;
    font-size: 16px;
    margin-top: 5px;
  }

  .toolbar1 .el-input__inner {
    -webkit-appearance: none;
    background-color: #FFF;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #DCDFE6;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    height: 30px;
    line-height: 30px;
    outline: 0;
    padding: 0 15px;
    -webkit-transition: border-color .2s cubic-bezier(.645, .045, .355, 1);
    transition: border-color .2s cubic-bezier(.645, .045, .355, 1);
    width: 98%;
  }

  .left {
    float: left;
    width: 62%;
    height: 40px;
    /* margin-left: 5%; */
  }


  .icon-body {
    /* display: inline-block; */
    margin-left: 5%;
    width: 50%;
    text-align: center;
    line-height: 40px;
    height: 40px;
  }

  .grantUser {
    height: 40px;
    font-size: 20px;
  }

  .icon-wrap {
    /* float: left; */
    margin-left: 5%;
    display: inline-block;
    font-size: 24px;
  }

  .group_input {
    width: 200px;
    height: 30px;
  }

  .dialogHeader {
    display: inline-block;
    margin: 0 auto;
    text-align: center;
    font-size: 20px;
    margin-top: -30px;
    width: 100%;
    padding: 0px 0px 0px 0px;
    border-bottom: 1px solid #99A9BF;
  }

  .headerSpan_left {
    height: 40px;
    width: 15%;
    float: left;
    margin-left: 35%;

  }

  .headerSpan_left_active {
    height: 40px;
    width: 15%;
    float: left;
    margin-left: 35%;
    border-bottom: 2px solid #808080;
  }

  .headerSpan_right {
    height: 40px;
    width: 15%;
    margin-left: 50%
  }

  .headerSpan_right_active {
    height: 40px;
    width: 15%;
    margin-left: 50%;
    border-bottom: 2px solid #808080;
  }

  .body_list {
    word-wrap: break-word;
    width: 100%;
    height: 300px;
    border: 1px solid #EAEAEA;
  }

  .select_user {
    background-color: #66b1ff;
    border: 1px solid #EAEAEA;
    height: 30px;
    margin: 0 auto;
    line-height: 30px;
    color: #FFFFFF;
    border-radius: 10px;
    margin-top: 10px;
  }

  .link-top {
    margin-top: 5px;
    /* margin-left: 5%;
    margin-right: 5%; */
    width: 100%;
    height: 1px;
    border-top: 1px dashed #e4e7ed;
  }

  #other_dialog .el-dialog__header {
    padding: 0px 0px 0px;
  }

  .toolbar1 {
    /* float: right; */
  }

  /* .el-button--primary {
    color: #FFF;
    background-color: #1eaafa;
    border-color: #1eaafa;
  } */
</style>
