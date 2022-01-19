<!--<template>-->
<!--  <div class="main">-->
<!--    <div class="header">-->
<!--      <div class="logo" style="font-family: microsoft yahei;">-->
<!--        &lt;!&ndash;<div style="color: white;-->
<!--     text-align: center;-->
<!--     margin: 8% auto;-->
<!--     font-size: 18px;"> <span>一站式数据集成平台</span></div>&ndash;&gt;-->
<!--        <img src="../assets/leftLogo.png" style="  margin-left: 20px;-->
<!--        vertical-align: middle;-->
<!--        height: 40px;-->
<!--        width: 40px;-->
<!--        line-height: 40px;-->
<!--        margin-bottom: 3px; padding-right: 2px;" />-->
<!--        数据集成工具-->
<!--        &lt;!&ndash; <img src="../../static/css/dataExplore.png" style="width: 139px;height: 22px; margin-left: 5px;" /> &ndash;&gt;-->
<!--      </div>-->

<!--      &lt;!&ndash; 用户信息 &ndash;&gt;-->
<!--      <div class="tools" id="tools">-->
<!--        <div style="display: inline-block;" @click="openProject" class="tools_btn">-->
<!--          <i class="iconfont icon-caozuo" style="font-size: 14px;"></i>-->
<!--          项目管理-->
<!--        </div>-->
<!--        <div style="display: inline-block;" @click="openModel" class="tools_btn">-->
<!--          <i class="iconfont icon-caozuo" style="font-size: 14px;"></i>-->
<!--          保存数据-->
<!--        </div>-->
<!--        <div style="display: inline-block;" class="tools_btn" @click="getStatus">-->
<!--          <i class="iconfont icon-yunhangjiankong" style="font-size: 14px;"></i>-->
<!--          运行监控-->
<!--        </div>-->
<!--        <div style="display: inline-block; padding-right: 0px;" class="tools_btn" @click="openUserOpera"-->
<!--          v-popover:popover>-->
<!--          {{userName}}-->
<!--          <i class="el-icon-arrow-down" style="font-size: 14px;" v-show="iflag"></i>-->
<!--          <i class="el-icon-arrow-up" style="font-size: 14px;" v-show="!iflag"></i>-->
<!--        </div>-->

<!--        <el-popover ref="popover" placement="top" trigger="click" class="popover_class">-->
<!--          <div class="opera_class" id="connec" style="width: 100%;height: 100%;">-->
<!--            <div class="connec_item" @click="updatePassWord()">修改密码</div>-->
<!--            <div class="connec_item" @click="loginOut()">退出登录</div>-->
<!--          </div>-->
<!--        </el-popover>-->


<!--        &lt;!&ndash; <div class="dropDown">-->
<!--          <div class="status_monitor">-->
<!--            <el-button type="text" size="medium" icon="el-icon-s-fold" @click="openModel">-->
<!--              保存数据-->
<!--            </el-button>-->
<!--          </div>-->
<!--          <div class="status_monitor">-->
<!--            <el-button type="text" size="medium" icon="el-icon-setting" @click="getStatus">-->
<!--              运行监控-->
<!--            </el-button>-->
<!--          </div>-->
<!--          <el-dropdown trigger="click">-->
<!--            <span class="el-dropdown-link">-->
<!--              {{userName}}<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
<!--            </span>-->
<!--            <el-dropdown-menu slot="dropdown">-->
<!--              <el-dropdown-item @click.native="updatePassWord">修改密码</el-dropdown-item>-->
<!--              <el-dropdown-item @click.native="loginOut">退出登录</el-dropdown-item>-->
<!--            </el-dropdown-menu>-->
<!--          </el-dropdown>-->
<!--        </div> &ndash;&gt;-->
<!--      </div>-->
<!--    </div>-->

<!--    <el-dialog :visible.sync="statusVisible" id="other_dialog" class="other_dialog"  width="70%" :before-close="handleProject"-->
<!--      :close-on-click-modal='false'>-->
<!--      <div class="dialogHeader">-->
<!--        <div class="headerSpan_left2" id="headerSpan_left2" @click="changeStatus">-->
<!--          <span>运行中</span>-->
<!--        </div>-->
<!--        <div class="headerSpan_right2" id="headerSpan_right2" @click="changeHistory">-->
<!--          <span>运行历史</span>-->
<!--        </div>-->

<!--      </div>-->

<!--      <div class="toolbar" v-show="flag">-->
<!--        <el-form :inline="true" size="mini" :model="statusFrom" class="demo-form-inline">-->
<!--          <el-form-item>-->
<!--            <el-input v-model="projectName" placeholder="项目名称"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item>-->
<!--            <el-date-picker v-model="defaultDate" type="datetimerange" range-separator="-" start-placeholder="开始日期"-->
<!--              end-placeholder="结束日期">-->
<!--            </el-date-picker>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" size="mini" @click="getHistory()" icon="el-icon-search"></el-button>-->
<!--          </el-form-item>-->


<!--        </el-form>-->
<!--      </div>-->
<!--      <div v-show="flag">-->
<!--        <el-table :data="statusData" order style="width: 100%;table-layout: fixed;" border height="300" v-loading="statusLoading"-->
<!--          :header-cell-style="{background:'#f5f7fa',color:'#606266'}">-->
<!--          <el-table-column type="selection" width="55"></el-table-column>-->
<!--          <el-table-column label="项目名称" prop="projectName">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="状态" prop="status" :formatter="formatStatus" width="80">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="开始时间" prop="startTime">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="结束时间" prop="endTime">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="运行时长" prop="execSecond" width="80">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="运行人" prop="userName">-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </div>-->
<!--      &lt;!&ndash; 运行中 &ndash;&gt;-->
<!--      <div v-show="!flag">-->
<!--        <el-table :data="status" order style="width: 100%;table-layout: fixed;" border height="300" v-loading="statusLoading"-->
<!--          :header-cell-style="{background:'#f5f7fa',color:'#606266'}">-->
<!--          <el-table-column type="index" lable="序号"></el-table-column>-->
<!--          <el-table-column label="项目名称" prop="projectName">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="状态" prop="status" :formatter="formatRun" width="80">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="开始时间" prop="startTime">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="运行时长" prop="execSecond" width="80">-->

<!--          </el-table-column>-->
<!--          <el-table-column label="运行人" prop="userName">-->
<!--          </el-table-column>-->
<!--          <el-table-column label="操作">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button type="danger" size="mini" @click="stop(scope.row)">中止</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </div>-->
<!--      <div class="footer" clearfix>-->
<!--        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"-->
<!--          :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"-->
<!--          :total="total">-->
<!--        </el-pagination>-->
<!--      </div>-->
<!--      &lt;!&ndash;  <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="statusVisible = false" size="mini">关闭</el-button>-->
<!--      </div> &ndash;&gt;-->

<!--    </el-dialog>-->

<!--    &lt;!&ndash; 模型保存 &ndash;&gt;-->
<!--    <el-dialog :visible.sync="modelVisiable" id="other_dialog" class="other_dialog" width="70%" :close-on-click-modal='false'>-->
<!--      <div class="dialogHeader">-->
<!--        <div class="headerSpan_left1" id="headerSpan_left1" @click="changeSaveModel">-->
<!--          <span>保存中</span>-->
<!--        </div>-->
<!--        <div class="headerSpan_right1" id="headerSpan_right1" @click="changeModelhistory">-->
<!--          <span>保存历史</span>-->
<!--        </div>-->

<!--      </div>-->

<!--      <div class="toolbar" v-show="modelFlag">-->
<!--        <el-form :inline="true" size="mini" :model="modelForm" class="demo-form-inline">-->
<!--          <el-form-item>-->
<!--            <el-input v-model="modelName" placeholder="模型名称"></el-input>-->
<!--          </el-form-item>-->

<!--          <el-form-item>-->
<!--            <el-date-picker v-model="modelDate" type="datetimerange" range-separator="-" start-placeholder="开始日期"-->
<!--              end-placeholder="结束日期">-->
<!--            </el-date-picker>-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" size="mini" @click="getHistoryModel()" icon="el-icon-search"></el-button>-->
<!--          </el-form-item>-->

<!--        </el-form>-->
<!--      </div>-->

<!--      <el-table v-if="!modelFlag" :data="savingData" order style="width: 100%;table-layout: fixed;" border height="300"-->
<!--        v-loading="modelLoading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">-->
<!--        <el-table-column type="selection" width="55"></el-table-column>-->
<!--        <el-table-column label="项目名称" prop="projectName">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="模型名称" prop="modelName">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="状态" prop="status" width="80">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="开始时间" prop="startTime">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="时长" prop="execSecond">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="创建人" prop="userName">-->
<!--        </el-table-column>-->
<!--      </el-table>-->

<!--      <el-table v-if="modelFlag" :data="modelHistory" order style="width: 100%;table-layout: fixed;" border height="300"-->
<!--        v-loading="modelLoading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">-->
<!--        <el-table-column type="index" lable="序号"></el-table-column>-->
<!--        <el-table-column label="项目名称" prop="projectName">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="模型名称" prop="modelName">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="状态" prop="status" width="80">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="开始时间" prop="startTime">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="结束事件" prop="endTime">-->
<!--        </el-table-column>-->
<!--        </el-table-column>-->
<!--        <el-table-column label="运行时长" prop="execSecond" width="80">-->
<!--        </el-table-column>-->
<!--        <el-table-column label="创建人" prop="userName">-->
<!--        </el-table-column>-->

<!--      </el-table>-->
<!--      <div class="footer" clearfix>-->
<!--        <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1" :current-page="currentPage1"-->
<!--          :page-sizes="[10, 50, 100, 200]" :page-size="pageSize1" layout="total, sizes, prev, pager, next, jumper"-->
<!--          :total="total1">-->
<!--        </el-pagination>-->
<!--      </div>-->
<!--      &lt;!&ndash; <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="modelVisiable = false" size="mini">关闭</el-button>-->
<!--      </div> &ndash;&gt;-->

<!--    </el-dialog>-->
<!--    &lt;!&ndash; 项目管理分组管理 &ndash;&gt;-->
<!--    <el-dialog :visible.sync="projectManagerVisiable" id="other_dialog" class="other_dialog" width="70%"-->
<!--      :close-on-click-modal='false'>-->
<!--      <projectManager ref="projectManager"></projectManager>-->
<!--    </el-dialog>-->

<!--    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :before-close="handleClose" class="dialog_temp" :close-on-click-modal='false'>-->
<!--      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">-->
<!--        <el-form-item label="原密码" prop="oldPass">-->
<!--          <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off" show-password></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="新密码" prop="pass">-->
<!--          <el-input type="password" v-model="ruleForm.pass" autocomplete="off" show-password></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="确认密码" prop="checkPass">-->
<!--          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" show-password></el-input>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="submit" :loading="updatePassLoading">确 定</el-button>-->
<!--      </span>-->
<!--    </el-dialog>-->

<!--    <div class="dataExploration">-->


<!--      &lt;!&ndash; 系统管理首页 &ndash;&gt;-->
<!--      <div class="systemHome">-->
<!--        <div class="systemSidebar1">-->
<!--          &lt;!&ndash; 集成项目和核心控件按钮 &ndash;&gt;-->
<!--          <div class="headTools">-->
<!--            <div class="item" id="item1" @click="changeProject">集成项目</div>-->
<!--            <div class="item2" id="item2" @click="changeControl" style="pointer-events:none">核心控件</div>-->
<!--          </div>-->

<!--          &lt;!&ndash; <group-manager></group-manager> &ndash;&gt;-->

<!--          &lt;!&ndash; <div class="link-top"></div> &ndash;&gt;-->
<!--          <div v-show="changeFlag">-->
<!--            &lt;!&ndash; 树形展示项目信息 &ndash;&gt;-->
<!--            <project-tree></project-tree>-->

<!--          </div>-->

<!--          <div v-show="!changeFlag">-->
<!--            <plugin-tree></plugin-tree>-->
<!--          </div>-->

<!--          &lt;!&ndash; <div class="flow-menu" v-show="!changeFlag">-->
<!--            <div class="menu-item" v-for="item in menueList" draggable="true" @dragstart="drag(item)">-->
<!--              <i :class="item.icon"></i>-->
<!--              <div>{{item.name}}</div>-->
<!--            </div>-->
<!--            <hr>-->

<!--          </div> &ndash;&gt;-->

<!--        </div>-->

<!--        <div class="systemContent">-->
<!--          <div class="tagsTools">-->
<!--            <tags-view></tags-view>-->
<!--          </div>-->
<!--          <div id="centerContent" class="center-content">-->
<!--            <transition name="move" mode="out-in">-->
<!--              &lt;!&ndash;这里的router-view用来渲染子页面&ndash;&gt;-->
<!--              <keep-alive>-->
<!--                <router-view :key="key"></router-view>-->
<!--              </keep-alive>-->
<!--            </transition>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--  import {-->
<!--    updatePassWord,-->
<!--    getGroup,-->
<!--    getStatus,-->
<!--    getStatusHistory,-->
<!--    savingModel, //查询正在保存的模型-->
<!--    modelHistory, //查询保存的历史模型-->
<!--    loginOut, //注销接口-->
<!--  } from '../api/api.js'-->

<!--  import util from '../common/utils.js'-->

<!--  import tagsView from './common/TagsView.vue'-->
<!--  import projectTree from './dataExploration/ProjectTree.vue'-->
<!--  import pluginTree from './dataExploration/PluginTree.vue'-->
<!--  import projectManager from './dataExploration/projectManager.vue'-->
<!--  import store from '../vuex/store.js'-->
<!--  import {-->
<!--    jsPlumb-->
<!--  } from 'jsplumb'-->
<!--  export default {-->
<!--    watch: {-->
<!--      // 监听屏幕宽度变化-->
<!--      screenWidth(val) {-->
<!--        console.info("屏幕宽度变为", val);-->
<!--        // if (1000 < val < 1400) {-->
<!--        //   value = 0.75;-->
<!--        // } else if (val > 1400) {-->
<!--        //   value = 0.85-->
<!--        // }-->
<!--        this.initClass();-->

<!--        // if (!this.timer) {-->
<!--        //   this.screenWidth = val-->
<!--        //   if (this.screenWidth < 768) {-->
<!--        //     this.isCollapse = true-->
<!--        //   }-->
<!--        //   console.info("-&#45;&#45;&#45;&#45;宽度发生变化");-->
<!--        //   this.timer = true-->
<!--        //   let that = this-->
<!--        //   setTimeout(function() {-->
<!--        //     that.timer = false-->
<!--        //   }, 400)-->
<!--        // }-->
<!--      }-->
<!--    },-->
<!--    data() {-->
<!--      var validatePass = (rule, value, callback) => {-->
<!--        if (value === '') {-->
<!--          callback(new Error('请输入密码'));-->
<!--        } else {-->
<!--          if (this.ruleForm.checkPass !== '') {-->
<!--            this.$refs.ruleForm.validateField('checkPass');-->
<!--          }-->
<!--          callback();-->
<!--        }-->
<!--      };-->
<!--      var validatePass2 = (rule, value, callback) => {-->
<!--        if (value === '') {-->
<!--          callback(new Error('请再次输入密码'));-->
<!--        } else if (value !== this.ruleForm.pass) {-->
<!--          callback(new Error('两次输入密码不一致!'));-->
<!--        } else {-->
<!--          callback();-->
<!--        }-->
<!--      };-->
<!--      return {-->
<!--        updatePassLoading:false,-->
<!--        iflag: true,-->
<!--        projectManagerVisiable: false,-->
<!--        screenWidth: document.body.clientWidth,-->
<!--        showClose: false,-->
<!--        pluginShow: false,-->
<!--        modelVisiable: false, //保存模型弹窗-->
<!--        modelFlag: false,-->
<!--        savingData: [], //正在保存的模型-->
<!--        modelHistory: [], //保存模型历史数据-->
<!--        modelLoading: false,-->
<!--        modelDate: [],-->
<!--        modelForm: {},-->
<!--        status: [],-->
<!--        defaultDate: [],-->
<!--        flag: false,-->
<!--        statusLoading: false,-->
<!--        currentPage: 1,-->
<!--        pageSize: 10,-->
<!--        total: 0,-->
<!--        currentPage1: 1,-->
<!--        pageSize1: 10,-->
<!--        total1: 0,-->
<!--        statusVisible: false,-->
<!--        statusData: [],-->
<!--        statusFrom: {-->
<!--          projectName: '',-->
<!--          dateTime: '',-->
<!--        },-->
<!--        projectName: '',-->
<!--        modelName: '',-->
<!--        now: '',-->
<!--        active: '',-->
<!--        userName: '',-->
<!--        dialogVisible: false,-->
<!--        ruleForm: {-->
<!--          oldPass: '', //原密码-->
<!--          pass: '', //密码-->
<!--          checkPass: '', //确认密码-->
<!--        },-->
<!--        changeFlag: true,-->
<!--        rules: {-->
<!--          oldPass: [{-->
<!--            required: true,-->
<!--            message: '请输入原密码',-->
<!--            trigger: 'blur'-->
<!--          }],-->
<!--          pass: [{-->
<!--            required: true,-->
<!--            validator: validatePass,-->
<!--            trigger: 'blur'-->
<!--          }],-->
<!--          checkPass: [{-->
<!--            required: true,-->
<!--            validator: validatePass2,-->
<!--            trigger: 'blur'-->
<!--          }]-->
<!--        },-->
<!--        menueList: [],-->
<!--        isConnect: false,-->
<!--        index: 1,-->
<!--        timer: null,-->
<!--        leftd: 0.8,-->
<!--        // 默认设置参数-->
<!--      };-->
<!--    },-->
<!--    computed: {-->
<!--      // 多个路由进入相同页面保持页面刷新避免 数据重复达到数据隔离的效果-->
<!--      key() {-->
<!--        if (this.$route.path === '/genneralView') {-->
<!--          return this.$route.path;-->
<!--        } else {-->
<!--          return this.$route.name !== undefined ? this.$route.name + new Date() : this.$route + new Date()-->
<!--        }-->
<!--      },-->
<!--      onRoutes() {-->
<!--        return this.$route.path.replace('/', '');-->
<!--      },-->

<!--    },-->
<!--    components: {-->
<!--      tagsView,-->
<!--      projectTree,-->
<!--      pluginTree,-->
<!--      projectManager-->
<!--    },-->
<!--    mounted() {-->
<!--      // 初始化头部样式-->
<!--      let value = 0.8;-->
<!--      this.initClass();-->
<!--      this.timer = setInterval(this.getWidth, 1000);-->
<!--      util.$on("changeControl", (red) => {-->
<!--        this.changeControl();-->
<!--      })-->
<!--      util.$on("changeProject", (red) => {-->
<!--        this.changeProject();-->
<!--      })-->
<!--      this.init();-->
<!--      // document.getElementById('centerContent').setAttribute('style',-->
<!--      //   'width:100%;height: 80%;float:right')-->
<!--      this.getData();-->
<!--    },-->
<!--    destroyed() {-->
<!--      clearInterval(this.timer);-->
<!--    },-->

<!--    methods: {-->
<!--      // 用户操作-->
<!--      openUserOpera() {-->
<!--        let flag = this.iflag;-->
<!--        this.iflag = !flag;-->
<!--      },-->
<!--      handleProject(done) {-->
<!--        this.statusVisible = false;-->
<!--      },-->
<!--      openProject() {-->
<!--        this.projectManagerVisiable = true;-->
<!--        this.$nextTick(() => {-->
<!--          this.$refs.projectManager.changeProjectManager();-->
<!--          console.info("子组件", this.$refs.projectManager);-->
<!--        })-->
<!--      },-->
<!--      // 初始化头部样式-->
<!--      initClass() {-->
<!--        let tools = document.getElementById('tools');-->
<!--        console.info(tools.offsetWidth);-->
<!--        let width = tools.offsetWidth;-->
<!--        let val = 0.5;-->
<!--        if (this.screenWidth > 1400) {-->
<!--          val = 0.95;-->
<!--        } else if (this.screenWidth < 1400 && this.screenWidth > 1200) {-->
<!--          val = 0.85;-->
<!--        } else if (this.screenWidth < 1200 && this.screenWidth > 1000) {-->
<!--          val = 0.8;-->
<!--        } else if (this.screenWidth < 1000 && this.screenWidth > 900) {-->
<!--          val = 0.75-->
<!--        }-->
<!--        let left = (this.screenWidth - width) * val;-->
<!--        // tools.style.left = left + 'px';-->
<!--        // console.info("tools",tools);-->
<!--      },-->
<!--      formatterStatus(data) {-->
<!--        return '运行中';-->
<!--      },-->

<!--      formatStatus(data) {-->
<!--        if (data.status === 'END') {-->
<!--          return '已完成';-->
<!--        } else if (data.status === 'TERMINATIN') {-->
<!--          return '停止';-->
<!--        }-->
<!--      },-->
<!--      formatRun(data) {-->
<!--        return '运行中'-->
<!--      },-->
<!--      getWidth() {-->
<!--        this.screenWidth = document.body.clientWidth;-->
<!--      },-->
<!--      openModel() {-->
<!--        this.modelVisiable = true;-->
<!--        this.changeSaveModel();-->
<!--      },-->
<!--      //-->
<!--      getModel() {-->
<!--        this.modelLoading = true;-->
<!--        savingModel().then(res => {-->
<!--          let {-->
<!--            data-->
<!--          } = res;-->
<!--          if (data.code === '10000') {-->
<!--            this.savingData = data.content;-->
<!--            this.total1 = data.content.length;-->
<!--          }-->
<!--          this.modelLoading = false;-->
<!--        })-->

<!--      },-->

<!--      getHistoryModel() {-->
<!--        let starDate = '';-->
<!--        let endDate = '';-->
<!--        if (this.modelDate.length > 0) {-->
<!--          var mm = this.modelDate[0].getMonth() + 1;-->
<!--          var d = this.modelDate[0].getDate();-->
<!--          var h = this.modelDate[0].getHours();-->
<!--          var m = this.modelDate[0].getMinutes();-->
<!--          var s = this.modelDate[0].getSeconds();-->
<!--          mm = mm < 10 ? ("0" + mm) : mm;-->
<!--          d = d < 10 ? ("0" + d) : d;-->
<!--          h = h < 10 ? ("0" + h) : h;-->
<!--          m = m < 10 ? ("0" + m) : m;-->
<!--          s = s < 10 ? ("0" + s) : s;-->

<!--          var mm1 = this.modelDate[1].getMonth() + 1;-->
<!--          var d1 = this.modelDate[1].getDate();-->
<!--          var h1 = this.modelDate[1].getHours();-->
<!--          var m1 = this.modelDate[1].getMinutes();-->
<!--          var s1 = this.modelDate[1].getSeconds();-->
<!--          mm1 = mm1 < 10 ? ("0" + mm1) : mm1;-->
<!--          d1 = d1 < 10 ? ("0" + d1) : d1;-->
<!--          h1 = h1 < 10 ? ("0" + h1) : h1;-->
<!--          m1 = m1 < 10 ? ("0" + m1) : m1;-->
<!--          s1 = s1 < 10 ? ("0" + s1) : s1;-->
<!--          starDate = this.modelDate[0].getFullYear() + '-' + mm + '-' + d + ' ' + h + ':' + m + ':' + s;-->
<!--          endDate = this.modelDate[1].getFullYear() + '-' + mm1 + '-' + d1 + ' ' + h1 + ':' + m1 + ':' + s1;-->
<!--        } else {-->
<!--          starDate = '2000-01-01 00:00:00',-->
<!--            endDate = '2999-01-01 00:00:00'-->
<!--        }-->

<!--        let param = {-->
<!--          pageNum: 1,-->
<!--          pageSize: 10,-->
<!--          saveModelVO: {-->
<!--            "startTime": starDate,-->
<!--            "modelName": this.modelName,-->
<!--            "endTime": endDate-->
<!--          }-->
<!--        }-->
<!--        modelHistory(param).then(res => {-->
<!--          let {-->
<!--            data-->
<!--          } = res;-->
<!--          if (data.code === '10000') {-->
<!--            this.modelHistory = data.content.list;-->
<!--            this.total1 = data.content.total;-->
<!--          }-->
<!--          this.modelLoading = false;-->
<!--        })-->
<!--      },-->
<!--      stop(data) {-->
<!--        // 中止项目运行-->
<!--        // 获取项目运行编号-->
<!--        let executorId = this.$store.getters.getRunningProject(data.projectId);-->
<!--        let stompClient = this.globalWs.ws;-->
<!--        stompClient.send("/stop", {}, JSON.stringify({-->
<!--          executorId: executorId,-->
<!--          projectId: data.projectId,-->
<!--        }));-->
<!--      },-->
<!--      handleCurrentChange(curPage) {-->
<!--        this.currentPage = curPage;-->
<!--        this.getHistory();-->
<!--      },-->
<!--      handleSizeChange(pageSize) {-->
<!--        this.pageSize = pageSize;-->
<!--        this.getHistory();-->
<!--      },-->
<!--      handleCurrentChange1(curPage) {-->
<!--        this.currentPage1 = curPage;-->
<!--        this.getHistoryModel();-->
<!--      },-->
<!--      handleSizeChange1(pageSize) {-->
<!--        this.pageSize1 = pageSize;-->
<!--        this.getHistoryModel();-->
<!--      },-->

<!--      changeStatus() {-->
<!--        this.flag = false;-->
<!--        this.$nextTick(() => {-->
<!--          document.getElementById("headerSpan_right2").className = "headerSpan_right2";-->
<!--          document.getElementById("headerSpan_left2").className = "headerSpan_left2_active";-->
<!--          this.getStatusData();-->
<!--        })-->

<!--        // this.$nextTick(function() {-->
<!--        //   // 每五秒运行一次-->
<!--        //   // this.timer = setInterval(this.getStatusData, 5000);-->
<!--        // })-->
<!--      },-->

<!--      changeHistory() {-->
<!--        this.flag = true;-->
<!--        this.$nextTick(() => {-->
<!--          document.getElementById("headerSpan_left2").className = "headerSpan_left2";-->
<!--          document.getElementById("headerSpan_right2").className = "headerSpan_right2_active";-->
<!--          this.getHistory();-->
<!--        })-->

<!--      },-->
<!--      changeSaveModel() {-->
<!--        this.modelFlag = false;-->
<!--        this.$nextTick(() => {-->
<!--          document.getElementById("headerSpan_right1").className = "headerSpan_right1";-->
<!--          document.getElementById("headerSpan_left1").className = "headerSpan_left1_active";-->
<!--          this.getModel();-->
<!--        })-->
<!--        // this.$nextTick(function() {-->
<!--        //   // 每五秒运行一次-->
<!--        //   this.timer1 = setInterval(this.getModel, 5000);-->
<!--        // })-->
<!--      },-->

<!--      changeModelhistory() {-->
<!--        this.modelFlag = true;-->
<!--        this.$nextTick(() => {-->
<!--          document.getElementById("headerSpan_left1").className = "headerSpan_left1";-->
<!--          document.getElementById("headerSpan_right1").className = "headerSpan_right1_active";-->
<!--          this.getHistoryModel();-->
<!--        })-->

<!--      },-->

<!--      // 获取运行历史-->
<!--      getHistory() {-->
<!--        let starDate = '';-->
<!--        let endDate = '';-->
<!--        if (this.defaultDate.length > 0) {-->
<!--          var mm = this.defaultDate[0].getMonth() + 1;-->
<!--          var d = this.defaultDate[0].getDate();-->
<!--          var h = this.defaultDate[0].getHours();-->
<!--          var m = this.defaultDate[0].getMinutes();-->
<!--          var s = this.defaultDate[0].getSeconds();-->
<!--          mm = mm < 10 ? ("0" + mm) : mm;-->
<!--          d = d < 10 ? ("0" + d) : d;-->
<!--          h = h < 10 ? ("0" + h) : h;-->
<!--          m = m < 10 ? ("0" + m) : m;-->
<!--          s = s < 10 ? ("0" + s) : s;-->

<!--          var mm1 = this.defaultDate[1].getMonth() + 1;-->
<!--          var d1 = this.defaultDate[1].getDate();-->
<!--          var h1 = this.defaultDate[1].getHours();-->
<!--          var m1 = this.defaultDate[1].getMinutes();-->
<!--          var s1 = this.defaultDate[1].getSeconds();-->
<!--          mm1 = mm1 < 10 ? ("0" + mm1) : mm1;-->
<!--          d1 = d1 < 10 ? ("0" + d1) : d1;-->
<!--          h1 = h1 < 10 ? ("0" + h1) : h1;-->
<!--          m1 = m1 < 10 ? ("0" + m1) : m1;-->
<!--          s1 = s1 < 10 ? ("0" + s1) : s1;-->
<!--          starDate = this.defaultDate[0].getFullYear() + '-' + mm + '-' + d + ' ' + h + ':' + m + ':' + s;-->
<!--          endDate = this.defaultDate[1].getFullYear() + '-' + mm1 + '-' + d1 + ' ' + h1 + ':' + m1 + ':' + s1;-->
<!--        } else {-->
<!--          starDate = '2000-01-01 00:00:00',-->
<!--            endDate = '2999-01-01 00:00:00'-->
<!--        }-->
<!--        this.statsData = [];-->
<!--        let param = {-->
<!--          pageNum: this.currentPage,-->
<!--          pageSize: this.pageSize,-->
<!--          projectHistoryExecuteVO: {-->
<!--            endTime: endDate,-->
<!--            projectName: this.projectName,-->
<!--            startTime: starDate,-->
<!--          }-->
<!--        }-->
<!--        this.statusLoading = true;-->
<!--        getStatusHistory(param).then(res => {-->
<!--          let {-->
<!--            data-->
<!--          } = res;-->
<!--          if (data.code === '10000') {-->
<!--            this.total = data.content.total;-->
<!--            this.statusData = data.content.list;-->
<!--          }-->
<!--          this.statusLoading = false;-->
<!--        })-->
<!--      },-->

<!--      // 获取运行中的项目信息-->
<!--      getStatusData() {-->
<!--        this.statsData = [];-->
<!--        this.statusLoading = true;-->
<!--        getStatus().then(res => {-->
<!--          let {-->
<!--            data-->
<!--          } = res;-->
<!--          if (data.code === '10000') {-->
<!--            this.status = data.content;-->
<!--            this.total = data.content.length;-->
<!--          }-->
<!--          this.statusLoading = false;-->
<!--        })-->
<!--      },-->


<!--      // 获取运行状态信息-->
<!--      getStatus() {-->
<!--        this.statusVisible = true;-->


<!--        this.$nextTick(() => {-->
<!--          this.changeStatus();-->
<!--        })-->
<!--      },-->
<!--      init() {},-->
<!--      getGroup() {-->
<!--        let para = {-->
<!--          groupName: '',-->
<!--          groupType: '',-->
<!--          pageNum: 1,-->
<!--          pageSize: 100-->
<!--        }-->

<!--        getGroup(para).then(res => {})-->

<!--      },-->
<!--      changeProject() {-->
<!--        this.changeFlag = true;-->
<!--        let doc = document.getElementById('item2');-->
<!--        if (this.$store.state.tagsView.visitedViews.length === 1) {-->
<!--          doc.setAttribute('style', 'pointer-events:none');-->
<!--        } else {-->

<!--        }-->
<!--        doc.className = "item2";-->
<!--        document.getElementById('item1').className = "itemclick";-->
<!--      },-->
<!--      changeControl() {-->
<!--        this.changeFlag = false;-->
<!--        let doc = document.getElementById('item2');-->
<!--        doc.removeAttribute('style', 'pointer-events:none');-->
<!--        document.getElementById('item1').className = "item";-->
<!--        doc.className = "itemclick1";-->
<!--      },-->
<!--      getData() {-->
<!--        this.userName = store.getters.getUserName-->

<!--      },-->
<!--      handleSelect(key, keyPath) {-->
<!--        console.log(key, keyPath);-->

<!--      },-->
<!--      updatePassWord() {-->
<!--        this.dialogVisible = true;-->
<!--        this.$nextTick(() => {-->
<!--          this.$refs['ruleForm'].resetFields();-->
<!--        })-->

<!--      },-->
<!--      submit() {-->
<!--        let params = {-->
<!--          oldPassword: this.ruleForm.oldPass,-->
<!--          password: this.ruleForm.pass,-->
<!--          confirmPassword: this.ruleForm.checkPass,-->
<!--        };-->
<!--        this.updatePassLoading = true;-->
<!--        updatePassWord(params).then(res => {-->
<!--          let {-->
<!--            data-->
<!--          } = res;-->
<!--          if (data.code === '10000') {-->
<!--            if (data.content) {-->
<!--              this.$message({-->
<!--                message: '密码修改成功',-->
<!--                type: 'success',-->
<!--              });-->
<!--              this.updatePassLoading = false;-->
<!--              this.dialogVisible = false;-->
<!--            } else {-->
<!--              this.$message({-->
<!--                message: data.msg,-->
<!--                type: 'error',-->
<!--              })-->
<!--              this.updatePassLoading = false;-->
<!--              // this.dialogVisible = false;-->
<!--            }-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      loginOut() {-->
<!--        this.$confirm('确定要退出登录吗？', '警告', {-->
<!--          type: 'warning'-->
<!--        }).then(() => {-->
<!--          // this.$store.replaceState(Object.assign({}, this.$store.state, {}));-->
<!--          //断开websocket连接-->
<!--          let stompClient = this.globalWs.ws;-->
<!--          stompClient.disconnect({}, function(frame) {});-->
<!--          this.$router.push('/');-->
<!--          this.$store.dispatch('logOut');-->
<!--          loginOut().then(res => {-->
<!--            console.info("退出登录", res);-->
<!--            let {-->
<!--              data-->
<!--            } = res;-->
<!--            if (data.code === '10000') {-->

<!--              //退出登录清空vuex中的数据-->
<!--              this.$store.commit('setUserName', '');-->
<!--            }-->
<!--          })-->



<!--        });-->

<!--      },-->
<!--      handleClose(done) {-->
<!--        this.$confirm('确认关闭？')-->
<!--          .then(_ => {-->
<!--            this.dialogVisible = false;-->
<!--          })-->
<!--          .catch(_ => {});-->
<!--      },-->
<!--      handleOpen1(key, keyPath) {-->
<!--        console.log(key, keyPath);-->
<!--      },-->
<!--      handleClose1(key, keyPath) {-->
<!--        console.log(key, keyPath);-->
<!--      },-->
<!--      addRouter(data) {},-->
<!--      //拖出节点-->
<!--      drag(item) {-->
<!--        this.currentItem = item;-->
<!--        this.$store.dispatch("setItem", JSON.stringify(item));-->
<!--      },-->

<!--      // 是否连线-->
<!--      changeConnect() {-->
<!--        this.isConnect = true;-->
<!--        this.$store.dispatch("setContent", this.isConnect);-->
<!--      },-->
<!--      changeConnect1() {-->
<!--        this.isConnect = false;-->
<!--        this.$store.dispatch("setContent", this.isConnect);-->
<!--      }-->

<!--    },-->

<!--  }-->
<!--</script>-->

<!--<style>-->
<!--  .header {-->
<!--    position: relative;-->
<!--    width: 100%;-->
<!--    height: 60px;-->
<!--    line-height: 60px;-->
<!--    background-color: #1a3754;-->
<!--    color: #FFFFFF;-->
<!--  }-->

<!--  .logo {-->
<!--    position: absolute;-->
<!--    text-align: left;-->
<!--    display: inline-block;-->
<!--    /* width: 80%; */-->
<!--    /* line-height: 60px; */-->
<!--    /* height: 60px; */-->
<!--    /* float: left; */-->
<!--    /* background-color: #1a3754; */-->
<!--    /* color: #FFFFFF; */-->
<!--    font-size: 20px;-->
<!--  }-->

<!--  .sidebarcon {-->
<!--    width: 65%;-->
<!--    height: 30px;-->
<!--    float: left;-->
<!--  }-->

<!--  .main {-->
<!--    height: 100%;-->
<!--    width: 100%;-->
<!--    background-color: #EAEAEA;-->
<!--  }-->

<!--  .contentwrap {-->
<!--    height: 80%;-->
<!--  }-->

<!--  .tools {-->
<!--    position: absolute;-->
<!--    height: 60px;-->
<!--    float: left;-->
<!--    right: 20px;-->
<!--    /* width: 20%; */-->
<!--    /* float: left; */-->
<!--    background-color: #1a3754;-->
<!--    -webkit-user-select: none;-->
<!--    -moz-user-select: none;-->
<!--    -ms-user-select: none;-->
<!--    user-select: none;-->
<!--  }-->

<!--  .status_monitor {-->
<!--    margin-left: 20px;-->
<!--    display: inline-block;-->
<!--    margin-right: 20px auto;-->
<!--    text-align: right;-->
<!--    font-size: 30px;-->
<!--    line-height: 60px;-->
<!--  }-->

<!--  .dropDown {-->
<!--    display: inline-block;-->
<!--    margin-right: 20px;-->
<!--    height: 60px;-->
<!--    line-height: 60px;-->
<!--    text-align: right;-->
<!--    position: relative;-->
<!--    font-size: 14px;-->
<!--  }-->

<!--  .el-dropdown {-->
<!--    color: #f5f7fa;-->
<!--    font-size: 14px;-->
<!--    margin-left: 20px;-->
<!--  }-->

<!--  .dataExploration {-->
<!--    background-color: #EAEAEA;-->
<!--    height: calc(100% - 65px);-->
<!--    height: -moz-calc(100% - 65px);-->
<!--    height: -webkit-calc(100% - 65px);-->
<!--  }-->

<!--  .center-content {-->
<!--    height: calc(100% - 40px);-->
<!--    height: -moz-calc(100% - 40px);-->
<!--    height: -webkit-calc(100% - 40px);-->
<!--    position: relative;-->
<!--  }-->

<!--  .systemHome {-->
<!--    height: 100%;-->
<!--    position: relative;-->

<!--  }-->

<!--  .systemSidebar1 {-->
<!--    display: inline-block;-->
<!--    width: 20%;-->
<!--    /* float: left; */-->
<!--    height: 100%;-->
<!--    margin: 0 auto;-->
<!--    white-space: nowrap;-->
<!--    background-color: #FFFFFF;-->
<!--  }-->

<!--  .headTools {-->
<!--    background-color: #EAEAEA;-->
<!--    height: 40px;-->
<!--    border: none;-->
<!--  }-->

<!--  .tagsTools {-->
<!--    background-color: #EAEAEA;-->
<!--    height: 40px;-->
<!--    border: none;-->
<!--  }-->

<!--  .systemContent {-->
<!--    display: inline-block;-->
<!--    width: 79%;-->
<!--    /* float: left; */-->
<!--    height: 100%;-->
<!--    margin: 0 auto;-->
<!--    white-space: nowrap;-->
<!--    position: absolute;-->
<!--    margin-left: 10px;-->
<!--    /* padding-left: 10px; */-->
<!--    background-color: #FFFFFF;-->
<!--  }-->

<!--  /* .item {-->
<!--    text-align: center;-->
<!--    font-size: 16px;-->
<!--    float: left;-->
<!--    width: 50%;-->
<!--    height: 40px;-->
<!--    margin: 0 auto;-->
<!--    line-height: 40px;-->
<!--    background-color: #EAEAEA;-->
<!--  } */-->

<!--  /* 去除重复的边框 */-->
<!--  .item2 {-->
<!--    text-align: center;-->
<!--    font-size: 16px;-->
<!--    float: left;-->
<!--    width: 50%;-->
<!--    height: 40px;-->
<!--    margin: 0 auto;-->
<!--    line-height: 40px;-->
<!--    background-color: #EAEAEA;-->
<!--    border-left: 0px;-->
<!--  }-->

<!--  .itemclick {-->
<!--    text-align: center;-->
<!--    font-size: 16px;-->
<!--    float: left;-->
<!--    width: 50%;-->
<!--    height: 40px;-->
<!--    margin: 0 auto;-->
<!--    line-height: 40px;-->
<!--    background-color: #FFFFFF;-->
<!--    color: #000000;-->
<!--  }-->

<!--  .itemclick1 {-->
<!--    text-align: center;-->
<!--    font-size: 16px;-->
<!--    float: left;-->
<!--    width: 50%;-->
<!--    height: 40px;-->
<!--    margin: 0 auto;-->
<!--    line-height: 40px;-->
<!--    background-color: #FFFFFF;-->
<!--    color: #000000;-->
<!--    border-left: 0px;-->
<!--  }-->

<!--  .link-top {-->
<!--    margin-top: 20px;-->
<!--    /* margin-left: 5%;-->
<!--    margin-right: 5%; */-->
<!--    width: 100%;-->
<!--    height: 1px;-->
<!--    border-top: 1px solid #99A9BF;-->
<!--  }-->


<!--  /* 核心控件 */-->
<!--  .flow-menu {-->
<!--    width: 100px;-->
<!--    padding: 10px;-->
<!--    margin: 0 10px;-->
<!--    border: 1px solid #EBEEF5;-->
<!--    padding: 12px;-->
<!--    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);-->
<!--    word-break: break-all;-->
<!--    border-radius: 4px;-->
<!--  }-->

<!--  .menu-item {-->
<!--    text-align: center;-->
<!--    box-shadow: 0 0 4px #696969;-->
<!--    margin-bottom: 8px;-->
<!--    cursor: pointer;-->
<!--    font-size: 14px;-->
<!--    color: #606266;-->
<!--    padding: 5px 0;-->
<!--    border-radius: 5px;-->
<!--  }-->

<!--  .menu-item>i {-->
<!--    font-size: 30px;-->
<!--  }-->

<!--  .dialogHeader {-->
<!--    display: inline-block;-->
<!--    margin: 0 auto;-->
<!--    text-align: center;-->
<!--    font-size: 20px;-->
<!--    margin-top: -30px;-->
<!--    width: 100%;-->
<!--    padding: 0px 0px 0px 0px;-->
<!--    border-bottom: 1px solid #99A9BF;-->
<!--    -ms-user-select: none;-->
<!--    -moz-user-select: none;-->
<!--    -webkit-user-select: none;-->
<!--  }-->

<!--  .headerSpan_left1 {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    float: left;-->
<!--    margin-left: 35%;-->

<!--  }-->

<!--  .headerSpan_left1_active {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    float: left;-->
<!--    margin-left: 35%;-->
<!--    border-bottom: 2px solid #808080;-->
<!--  }-->

<!--  .headerSpan_right1 {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    margin-left: 50%-->
<!--  }-->

<!--  .headerSpan_right1_active {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    margin-left: 50%;-->
<!--    border-bottom: 2px solid #808080;-->
<!--  }-->

<!--  .headerSpan_left2 {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    float: left;-->
<!--    margin-left: 35%;-->

<!--  }-->

<!--  .headerSpan_left2_active {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    float: left;-->
<!--    margin-left: 35%;-->
<!--    border-bottom: 2px solid #808080;-->
<!--  }-->

<!--  .headerSpan_right2 {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    margin-left: 50%-->
<!--  }-->

<!--  .headerSpan_right2_active {-->
<!--    height: 40px;-->
<!--    width: 15%;-->
<!--    margin-left: 50%;-->
<!--    border-bottom: 2px solid #808080;-->
<!--  }-->

<!--  /* img {-->

<!--    margin-bottom: 15px;-->
<!--  } */-->

<!--  .el-button&#45;&#45;text {-->
<!--    /* color: #FFFF; */-->
<!--    background: 0 0;-->
<!--    padding-left: 0;-->
<!--    padding-right: 0;-->
<!--  }-->

<!--  #other_dialog .el-dialog__header {-->
<!--    padding: 0px 0px 0px;-->
<!--  }-->

<!--  .systemSidebar1 {-->
<!--    -webkit-user-select: none;-->
<!--    -moz-user-select: none;-->
<!--    -ms-user-select: none;-->
<!--    user-select: none;-->
<!--  }-->

<!--  .el-table&#45;&#45;enable-row-transition .el-table__body td {-->
<!--    padding: 0;-->
<!--    -webkit-transition: background-color .25s ease;-->
<!--    transition: background-color .25s ease;-->
<!--  }-->

<!--  .tools_btn {-->
<!--    height: 60px;-->
<!--    vertical-align: bottom;-->
<!--    padding-right: 45px;-->
<!--  }-->

<!--  .el-table td,-->
<!--  .el-table th.is-leaf {-->
<!--    padding: 0;-->
<!--    border-bottom: 1px solid #ebeef5;-->
<!--  }-->

<!--  .el-table td,-->
<!--  .el-table th {-->
<!--    padding: 0;-->
<!--    min-width: 0;-->
<!--    -webkit-box-sizing: border-box;-->
<!--    box-sizing: border-box;-->
<!--    text-overflow: ellipsis;-->
<!--    vertical-align: middle;-->
<!--    position: relative;-->
<!--    text-align: left;-->
<!--  }-->

<!--  .el-form-item {-->
<!--    margin-bottom: 10px;-->
<!--    margin-top: 10px;-->
<!--  }-->

<!--  .el-form-item&#45;&#45;mini.el-form-item,-->
<!--  .el-form-item&#45;&#45;small.el-form-item {-->
<!--    margin-top: 10px;-->
<!--    margin-bottom: 10px;-->
<!--  }-->

<!--  .el-popover {-->
<!--    position: absolute;-->
<!--    background: #FFF;-->
<!--    min-width: 150px;-->
<!--    border: 1px solid #EBEEF5;-->
<!--    padding: 0px 0px;-->
<!--    z-index: 2000;-->
<!--    color: #606266;-->
<!--    line-height: 1.4;-->
<!--    text-align: justify;-->
<!--    font-size: 14px;-->
<!--    -webkit-box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);-->
<!--    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);-->
<!--    word-break: break-all;-->
<!--  }-->

<!--  .el-popper[x-placement^=bottom] {-->
<!--    margin-top: 5px;-->
<!--  }-->


<!--  .opera_class {-->
<!--    padding-top: 5px;-->
<!--    width: 120px;-->
<!--    height: 50px;-->
<!--    border: 1px solid #EAEAEA;-->
<!--    background-color: #FFFFFF;-->
<!--  }-->

<!--  .el-form-item__error {-->
<!--    color: #F56C6C;-->
<!--    font-size: 12px;-->
<!--    line-height: 1;-->
<!--    padding-top: 0px;-->
<!--    position: absolute;-->
<!--    top: 100%;-->
<!--    left: 0;-->
<!--  }-->
<!--</style>-->
