<template>
  <div class="appcontainer">
    <!-- 项目预览时不显示 -->
    <div class="headTools1" v-show="showTool">
      <div class="opera_btn" id="opera_btn">
        <el-tooltip content="运行" placement="bottom" effect="dark">
          <div class="opera_btn_item" @click="!projectInfo.isExecuted && preExecute()">
            <img src="../../../static/img/run.png" v-if="!projectInfo.isExecuted" alt="text"/>
            <img src="../../../static/img/run_dis.png" v-else/>
          </div>
        </el-tooltip>

        <el-tooltip content="停止" placement="bottom" effect="dark">
          <div class="opera_btn_item" @click=" !projectInfo.isPause && pause()">
            <img src="../../../static/img/stop.png" v-if="!projectInfo.isPause"/>
            <img src="../../../static/img/stop_dis.png" v-else/>
          </div>
        </el-tooltip>

        <el-tooltip content="保存" placement="bottom" effect="dark">
          <div class="opera_btn_item" @click="!projectInfo.isSave && addProject()">
            <img src="../../../static/img/add.png" v-if="!projectInfo.isSave"/>
            <img src="../../../static/img/add_dis.png" v-else/>
          </div>
        </el-tooltip>

        <el-tooltip content="另存" placement="bottom" effect="dark">
          <div class="opera_btn_item" @click="!projectInfo.isSave && saveAs()">
            <img src="../../../static/img/saevAs.png" v-if="!projectInfo.isSave"/>
            <img src="../../../static/img/saveAs_dis.png" v-else/>
          </div>
        </el-tooltip>
        <!--        <el-tooltip content="放大" placement="bottom" effect="dark">-->
        <!--          <div class="opera_btn_item" @click="addFlowSize('add')">-->
        <!--            <img src="../../../static/img/increase.png"/>-->
        <!--          </div>-->
        <!--        </el-tooltip>-->
        <!--        <div class="opera_btn_item" style="width: 40px">-->
        <!--          {{canvasNumber}}%-->
        <!--        </div>-->

        <!--        <el-tooltip content="缩小" placement="bottom" effect="dark">-->
        <!--          <div class="opera_btn_item" @click="addFlowSize('redues')">-->
        <!--            <img src="../../../static/img/redues.png"/>-->
        <!--          </div>-->
        <!--        </el-tooltip>-->

        <el-tooltip content="参数配置" placement="bottom" effect="dark">
          <div class="opera_btn_item" @click="configParam">
            <img src="../../../static/img/setup.png"/>
          </div>
        </el-tooltip>
      </div>

      <!-- 状态显示开关 -->
      <div style="height: 30px; line-height: 27px;float: right; margin-right: 10px;">
        <el-switch v-model="statusFlag" active-color="#13ce66" inactive-color="#ff4949">
        </el-switch>
      </div>

    </div>

    <!-- 编辑节点信息弹窗 -->
    <el-dialog :title="title" :visible.sync="centerDialogVisible" :before-close="cloaseDialog"
               v-if="centerDialogVisible"
               :close-on-click-modal='false' v-dialogDrag v-drag-dialog :modal="false" class="abow_dialog"
               v-alterELDialogMarginTop="{marginTop:'2vh'}">
      <div id="test" ref="component" :data="nodeData"></div>
    </el-dialog>

    <!-- 显示输入输出字段 -->
    <el-dialog :title="columnTitle" :visible.sync="columnViaiable" width="60%" :close-on-click-modal='false'>
      <el-table :data="tableData" height="400" border style="width: 100%"
                :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
                :row-style="{padding:'0px 0px'}">
        <el-table-column prop="name" label="字段名称" width="180">
        </el-table-column>
        <el-table-column prop="type" label="类型" width="180">
        </el-table-column>
        <el-table-column prop="type" label="长度">
        </el-table-column>
        <el-table-column prop="currency" label="精度">
        </el-table-column>
        <el-table-column prop="stepName" label="步骤来源">
        </el-table-column>
        <el-table-column prop="stepName" label="存储">
        </el-table-column>
      </el-table>
    </el-dialog>
    <div class="flow-detail" id="flow-detail">
      <div id="flowContent" ref="flowContent" @drop="drop($event)" @dragover="allowDrop($event)" class="flow-content"
           v-loading="contentLoading" :element-loading-text="lodingContext" element-loading-spinner="el-icon-loading">
        <div id="flowNode" class="flow-node">
          <flowNode v-for="node in data.nodeList" :key="node.id" :node="node" :id="node.id" :statusFlag="statusFlag"
                    :ref="node.id" :isconnect="isConnect" :clickNode="clickNode" @delete-node="deleteNode"
                    @change-node-site="changeNodeSite"
                    @change-node="selectNode" @change-data="openEdit" @show-input="showInput" @show-output="showOutput">
          </flowNode>
          <!-- 过滤插件右键菜单 -->
          <div class="fielter" id="fielter" v-show="filter">
            <ul>
              <li @click="fit()">符合条件输出</li>
              <li @click="unFit()">不符合条件输出</li>
            </ul>
          </div>

          <div class="connec_style" id="fielter1" v-show="selectMain">
            <div class="connec_item" @click="mainOutStep">
              主输出步骤
            </div>
            <div class="connec_item" @click="errorOutStep">
              错误处理步骤
            </div>
          </div>
          <!-- 右键连接线菜单 -->
          <div class="connec_style" id="connec" v-show="connecOperaShow"
               :style="{left: divLeft + 'px',top:divTop+'px'}">
            <div v-for="(item,index) in connecOpera" class="connec_item" @click="clickConnect(item)">{{item.name}}</div>
          </div>
        </div>
      </div>
      <!-- 执行结果 -->
      <div class="log_class1" id="opera" v-show="operaShow">
        <!-- 头部tab标签 -->
        <div class="tab_header">
          <div style="width: 40%;height: 35px;  float: left;">
            <div v-for="item in operaData" :class="isActive(item.index)?'header_tab_ative':'header_tab'"
                 :disabled="true" @click="changeOpear(item)">
              <i :class="item.icon"></i>
              {{item.label}}
            </div>
          </div>
          <!-- 拖拽区域 -->
          <div style="width: 50%;height: 35px; float: left;position: relative;">
            <div class="drag_class" id="middle">
              <i class="el-icon-more"></i>
            </div>
          </div>
          <!-- 右侧操作区 -->
          <div
            style="width: 10%;height: 35px; line-height: 35px;  float: left;text-align: right;">
            <!-- 最大化 -->
            <i class=""></i>
            <!-- 最小化 -->
            <!-- 关闭 -->
            <i class="el-icon-close" style="margin-right: 10px;" @click="hideOpera()"></i>
          </div>
        </div>
        <!-- 日志 -->
        <div class="content_tab" v-show="showLog===0">
          <!--  <div class="pane_header">
            <el-tooltip content="清空日志" placement="top" effect="light">
              <el-button size="mini" icon="el-icon-delete" @click="clearLog"></el-button>
            </el-tooltip>
          </div> -->
          <div class="logShow" id="log">
          </div>
        </div>
        <!-- 步骤度量 -->
        <div class="content_tab" v-show="showLog===1">
          <div class="logs1">
            <el-table border id="measurement" :data="measureData" highlight-current-row
                      v-loading='measureLoading'
                      height="100%" style="overflow: auto;width: 100%; table-layout:fixed" stripe
                      :header-cell-style="{background:'#f5f7fa',color:'#606266',width:'40px'}"
                      :row-style="{padding:'0px 0px'}"
                      :cell-style="{padding:'0px 0px'}">
              <el-table-column type="index" width="50">
              </el-table-column>
              <el-table-column label="步骤名称" prop="label" width="180px"></el-table-column>
              <el-table-column label="复制记录数" prop="copy"></el-table-column>
              <el-table-column label="读" prop="linesRead"></el-table-column>
              <el-table-column label="写" prop="linesWritten"></el-table-column>
              <el-table-column label="输入" prop="linesInput"></el-table-column>
              <el-table-column label="输出" prop="linesOutput"></el-table-column>
              <el-table-column label="更新" prop="linesUpdated"></el-table-column>
              <el-table-column label="拒绝" prop="linesRejected"></el-table-column>
              <el-table-column label="错误" prop="errors"></el-table-column>
              <el-table-column label="状态" prop="stepExecutionStatus" width="150px"
                               :formatter="formatStatus">
              </el-table-column>
              <el-table-column label="时间/秒" prop="seconds"></el-table-column>
              <el-table-column label="速度(条/秒)" prop="speed"></el-table-column>
              <el-table-column label="pri/in/out" prop="priority"></el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 数据预览 -->
        <div class="content_tab" v-show="showLog===2">
          <div class="pane_header">
            <div class="pane_header_label"></div>
            {{labelName}}
            <!--            总记录数:{{executeCount}}-->
            <div class="pane_header_button">
              <el-button type="primary" size="mini" icon="el-icon-download" @click="download" :disabled="buttonDis">下载
              </el-button>
            </div>
          </div>

          <div class="logs" v-loading="preLoading">
            <el-table border ref="singleTable" id="dataAnaTable" :data="dataPre" highlight-current-row
                      v-loading='tableLoading'
                      height="100%" v-if="analyzeFlag" style="overflow: auto;width: 100%; table-layout:fixed" stripe
                      :header-cell-style="{background:'#f5f7fa',color:'#606266',width:'40px'}"
                      :row-style="{padding:'0px 0px'}"
                      :cell-style="{padding:'0px 0px'}" class='tableCss'>
              <el-table-column type="index" width="50">
              </el-table-column>
              <el-table-column v-for="(item,index) in dataColumn" :show-overflow-tooltip='true' :label="item"
                               :key="item"
                               :property="item" maxWidth='200px' v-if="item!==undefined">
                <template slot-scope="scope">
                  <span>{{scope.row[index]}}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>


        <!-- 字段信息 -->
        <div class="content_tab" v-show="showLog===3">
          <div class="pane_header">
            <div class="pane_header_label"></div>
            {{labelName}}
          </div>

          <div class="logs">
            <!-- 字段信息 -->
            <el-table border ref="singleTable" :data="columnDatas" highlight-current-row class='tableCss' height="100%"
                      stripe style="width: 100%;overflow: auto;"
                      :header-cell-style="{background:'#f5f7fa',color:'#606266',width:'40px'}"
                      :row-style="{padding:'0px 0px'}">
              <el-table-column type="index" width="50">
              </el-table-column>
              <el-table-column property="inName" label="输入字段" sortable>
              </el-table-column>
              <el-table-column property="inNameCn" label="输入字段中文名" sortable>
              </el-table-column>
              <el-table-column property="inType" label="输入字段类型" sortable>
              </el-table-column>
              <el-table-column property="outName" label="输出字段" sortable>
              </el-table-column>
              <el-table-column property="outNameCn" label="输出字段中文名" sortable>
              </el-table-column>
              <el-table-column property="outType" label="输出字段" sortable>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div style="clear:both"/>
    </div>

    <!-- 高级预览-下载文件 选择字段 -->
    <el-dialog title="请选择字段" :visible.sync="downLoadFileVisiable" class="dialog_temp" :close-on-click-modal='false'>
      <el-table :data="downloadData" height="300px" border tooltip-effect="dark" ref="downloadTable"
                @selection-change="handleSelectionChange"
                @cell-dblclick="updateCell" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
        <el-table-column type="selection" width="55">
        </el-table-column>
        <el-table-column property="outName" label="字段名称" sortable>
        </el-table-column>
        <el-table-column label="字段中文名称" sortable>
          <template slot-scope="scope">
            <el-input v-if="scope.row.updateFlag" v-model="scope.row.outNameCn" size="mini"></el-input>
            <span v-else>{{scope.row.outNameCn}}</span>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="default" size="mini" @click="closeDownload">取消</el-button>
        <el-button type="primary" size="mini" @click="startDownload" :loading="downloadLoading">下载</el-button>
      </div>
    </el-dialog>

    <!-- 新增项目 -->
    <el-dialog :title="addTitle" :visible.sync="editFormVisiable" width="30%" class="dialog_temp"
               :close-on-click-modal='false'>
      <el-form :model="editForm" label-width="100px" ref="editForm" :rules="rules">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="editForm.projectName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="editForm.description" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="所属分组" prop="groupId">
          <el-select v-model="editForm.groupId">
            <el-option v-for="item in groupIds" :key="item.groupId" :value="item.groupId"
                       :label="item.groupName"></el-option>
          </el-select>
          <el-button type="primary" size="mini" @click="addGroup">新增</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="default" size="mini" @click="editFormVisiable = false">关闭</el-button>
        <el-button @click="addData" size="mini" type="primary" :loading="addProjectLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 新增分组 -->
    <el-dialog title="新增分组" :visible.sync="addGroupViaiable" width="30%" class="dialog_temp" :before-close="closeGroup"
               :close-on-click-modal='false' :modal-append-to-body='false' :append-to-body="true">
      <el-form :model="groupForm" label-width="100px" ref="groupForm" :rules="groupRules">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model="groupForm.groupName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组描述" prop="describe">
          <el-input v-model="groupForm.describe" rows="2" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组排序" prop="groupOrder">
          <el-input v-model="groupForm.groupOrder" type="number"></el-input>
        </el-form-item>
        <el-form-item label="是否启用" class="plug-msg">
          <el-switch v-model="groupForm.enabled" active-color="#13ce66" inactive-color="#c91e3a" active-value="T"
                     inactive-value="F">
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeGroup()">取 消</el-button>
        <el-button type="primary" @click="addGroupData" :loading="addGroupLoading">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="警告" class="dialog_temp other_dialog" width="30%" :visible.sync="inputCopy">
      <div class="input_copy">
        步骤 '{{inputStepName}}' 有两个子步骤需要发送数据<br>
        这里有两种发送数据的方式：<br>
        &nbsp;&nbsp;.分发记录:目标步骤轮流接受记录。<br>
        &nbsp;&nbsp;.复制记录:所有记录同时发送到所有的目标步骤<br>
        请选择你需要的方式（默认是复制）
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="distribution('Y')" type="primary" size="mini">分发</el-button>
        <el-button @click="distribution('N')" type="primary" size="mini">复制</el-button>
      </div>
    </el-dialog>


    <!--  命名参数配置-->
    <el-dialog class="dialog_temp" width="40%" :visible.sync="configVisiable" title="命名参数配置">
      <span style="float: right">
        <el-button size="mini" type="primary" @click="addParam">新增</el-button>
      </span>
      <el-table table :header-cell-style="{background:'#f5f7fa',color:'#606266'}" height="200px"
                :data="dataJson.transformation.info.parameters.parameter">
        <el-table-column label="命名参数" prop="name">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" size="mini"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="值" prop="default_value">
          <template slot-scope="scope">
            <el-input v-model="scope.row.default_value" size="mini"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="描述" prop="description">
          <template slot-scope="scope">
            <el-input v-model="scope.row.description" size="mini">
            </el-input>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">

            <el-button size="mini" type="text" @click="deleteParam(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="configVisiable=false" type="primary" size="mini">取消</el-button>
        <el-button @click="saveRunParam" type="primary" size="mini">确定</el-button>
      </div>
    </el-dialog>

    <!--    运行配置参数-->
    <el-dialog class="dialog_temp" title="运行配置" width="40%" :visible.sync="runConfigVisiable">
      <el-form :model="runForm" size="mini" label-position="right" label-width="150px">
        <el-form-item label="日志级别">
          <el-select v-model="runForm.logLevel" clearable>
            <el-option v-for="item in logLevels" :key="item.key" :label="item.label" :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用安全模式">
          <el-checkbox v-model="runForm.safeModeEnabled">
          </el-checkbox>
        </el-form-item>
      </el-form>
      <el-tabs v-model="activeName">
        <span style="float: right">
          <el-button size="mini" type="primary" @click="addParam">新增</el-button>
        </span>
        <el-tab-pane label="命名参数" name="params">
          <el-table table :header-cell-style="{background:'#f5f7fa',color:'#606266'}" height="200px"
                    :data="dataJson.transformation.info.parameters.parameter">
            <el-table-column label="命名参数" prop="name">
              <template slot-scope="scope">
                <el-input v-model="scope.row.name" size="mini"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="值" prop="default_value">
              <template slot-scope="scope">
                <el-input v-model="scope.row.default_value" size="mini"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="描述" prop="description">
              <template slot-scope="scope">
                <el-input v-model="scope.row.description" size="mini">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">

                <el-button size="mini" type="text" @click="deleteParam(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="变量" name="valide">
          <span style="float: right">
            <el-button size="mini" type="primary" @click="addVariables">
                  新增
            </el-button>
          </span>
          <el-table :data="variables" :header-cell-style="{background:'#f5f7fa',color:'#606266'}" height="200px">
            <el-table-column label="变量" prop="name">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="值" prop="value">
              <template slot-scope="scope">
                <el-input v-model="scope.row.value" size="mini"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteVariable(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="runConfigVisiable=false" type="primary" size="mini">取消</el-button>
        <el-button @click="execute" type="primary" size="mini">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {updatePluginState} from '@/common/common'
  import util from '../../common/utils.js'
  import {jsPlumb} from 'jsplumb'
  import flowNode from './flowNode.vue'
  import {export_json_to_excel} from "../../excel/Export2Excel";
  import {
    getStatus,
    addGroup,
    addProject,
    base,
    checkGroupName,
    getGroup,
    getModelInfo,
    saveAnalyzeData,
    updateSelectProject,
  } from '../../api/api.js'
  // import SockJS from 'sockjs-client';
  // import Stomp from 'stompjs';
  import headerButton from '../flowCommon/Headerbutton.vue'
  import Vue from 'vue'
  import {compareObj} from '../../assets/common/tool.js'
  import {getConnection} from "../../api/api";
  import store from "../../vuex/store";

  let stompClient = null;
  let flag = false;
  let executeById = "";
  export default {
    name: 'flowMain',
    inject: ['reload', 'initWs'],
    watch: {
      // 监听路由变化
      '$route': function(val,oldVal){
      },
      // time:function(val,oldVal){
      //   this.initWebSocket();
      // },
      screenHeight(val) {
        this.initFlowHeight();
      }
    },
    computed:{
      time(){
        return this.$store.getters.getWebSocketTime;
      },
    },
    data() {
      return {
        configVisiable: false,
        activeName: 'params',
        measureData: [],
        measureLoading: false,
        md5: '',//项目md5信息
        runConfigVisiable: false,//运行配置弹窗
        runForm: {
          logLevel: 'Basic,',//日志级别
          safeModeEnabled: false,
        },//运行配置参数
        logLevels: [{
          key: 'Basic',
          label: '基本日志',
        }, {
          key: 'Detailed',
          label: '详细日志',
        }, {
          key: 'Debug',
          label: '调试日志',
        }, {
          key: 'Error',
          label: '错误日志',
        }, {
          key: 'Rowlevel',
          label: '行级日志',
        }, {
          key: 'Minimal',
          label: '最小日志',
        }, {
          key: 'Nothing',
          label: '没有日志',
        },],
        selectMain: false,
        scale: 1,
        inputStepName: '',
        inputCopy: false,
        downloadData: [],
        columnDatas: [],//输入输出字段
        parentPath: '',
        projectInfo: {
          isExecuted: false,
          isSave: false,
          isPause: true,
        },
        requestId: this.$route.path,
        isRedues: false,
        canvasNumber: 100,
        disRedues: true,
        preLoading: false,
        addGroupViaiable: false,
        addGroupLoading: false,
        groupForm: {
          groupName: '',
          describe: '',
          groupOrder: 99,
          groupType: 'JCJB',
          enabled: 'T'
        }, //分组数据
        groupRules: {
          groupName: [{
            required: true,
            message: '分组名称不能为空',
            trigger: 'blur',
          }],
          groupOrder: [{
            required: true,
            message: '分组排序不能为空',
            trigger: 'blur'
          }],
        },
        tableLoading: false,
        screenHeight: document.body.clientHeight,
        acviteColor: '#1eaafa',
        pauseColor: '#EAEAEA',
        lodingContext: '正在初始化节点信息',
        divLeft: 0,
        divTop: 0,
        initExecute: true,
        downloadLoading: false,
        fileName: '',
        dropNodeName: '', //拖入的节点名称
        statusFlag: true,
        connecOperaShow: false, //连接线操作菜单默认不显示
        // 节点连接线操作
        connecOpera: [{
          index: 0,
          name: '使节点连接失效',
        }, {
          index: 1,
          name: '删除节点连接'
        }],
        showTool: true, //工具栏默认显示
        operaShow: false,
        operaData: [{
          index: 0,
          label: '日志',
          icon: 'iconfont icon-rizhi'
        }, {
          index: 1,
          label: '步骤度量',
          icon: 'iconfont icon-duliang'
        }, {
          index: 2,
          label: '数据预览',
          icon: 'iconfont icon-shujuyulan-copy'
        }, {
          index: 3,
          label: '字段信息',
          icon: 'iconfont icon-ziduanliebiao'
        }], //执行结果操作项
        showLog: 0,
        isPause: true,
        isSave: false,
        isExecuted: false,
        showExecute: this.$store.getters.getExecuteFlag(this.$route.path), //执行结果弹窗
        executeCount: 0, //执行总记录数
        isConnect: false,
        clickNode: '',
        addTitle: '新增',
        logs: '', //执行日志
        showClose: false,
        contentLoading: this.$store.getters.getFlowMainLoading,
        buttonDis: true,
        analyzeFlag: false,
        stepName: '',
        stepId: '',
        selelctModelColumn: [], //保存为模型时选中的字段
        groupList: [], //输出类模型分组
        filter: false,
        projectId: '', //项目编号
        addProjectLoading: false,
        tableData: [],
        columnViaiable: false,
        columnTitle: '',
        stepList: [],
        downLoadFileVisiable: false, //数据预览-下载字段选择弹窗
        selectColumns: [], //下载文件时选择的字段
        centerDialogVisible: false,
        nodeData: '',
        changeFalg: false,
        repeatLabel: '',
        repeatDetailData: [],
        repeatDetailTitle: '',
        repeatDetailVisiable: false,
        statsData: [],
        statsRegion: [],
        repeatData: [],
        repeatTitle: '',
        repeatVisiable: false,
        statsTilte: '',
        statsViaiable: false,
        disable: true,
        columnMessage: [],
        columnData: [], //字段数据
        dataColumn: [],
        dataPreColumn: [],
        dataPre: [], //数据预览数据
        showFlag: flag,
        activeIndex2: 1,
        labelName: '',
        rules: {
          projectName: [{
            required: true,
            trigger: 'blur',
            message: '项目名称不能为空',
          }],
          groupId: [{
            required: true,
            trigger: 'change',
            message: '请选择所属分组',
          }],
          projectStatus: [{
            required: true,
            trigger: 'change',
            message: '请选择项目状态',
          }],
        },
        groupIds: [],
        editFormVisiable: false,
        editForm: {
          projectName: '',
          description: '',
          groupId: '',
          projectStatus: '',
          projectType: '',

        },
        jsPlumb: null, // jsPlumb 实例
        index: 1,
        // 默认设置参数
        jsplumbSetting: {
          // 动态锚点、位置自适应
          Anchors: ['Top', 'TopCenter', 'TopRight', 'TopLeft', 'Right', 'RightMiddle', 'Bottom', 'BottomCenter',
            'BottomRight', 'BottomLeft', 'Left', 'LeftMiddle'
          ],
          Container: 'flowContent',
          // 连线的样式 StateMachine、Flowchart,有四种默认类型：Bezier（贝塞尔曲线），Straight（直线），Flowchart（流程图），State machine（状态机）
          Connector: 'Straight',
          //这个是鼠标拉出来的线的属性
          ConnectionOverlays: [
            ["Label", {
              label: "",
              id: "label-1",
              cssClass: "csslabel",
              location: 0.4
            }]
          ],
          // 鼠标不能拖动删除线
          ConnectionsDetachable: false,
          // 删除线的时候节点不删除
          DeleteEndpointsOnDetach: false,
          // 连线的端点
          // Endpoint: ["Dot", {radius: 5}],
          Endpoint: ["Rectangle", {
            height: 10,
            width: 10,
            // cssClass: 'cssPoint' //给线加上端点
          }],
          // 线端点的样式
          EndpointStyle: {
            fill: 'rgba(255,255,255,0)',
            outlineWidth: 1
          },
          LogEnabled: true, //是否打开jsPlumb的内部日志记录
          // 绘制线的样式
          PaintStyle: {
            stroke: '#409eff',
            strokeWidth: 2
          },
          HoverPaintStyle: {
            stroke: '#409eff',
            strokeWidth: 4
          },
          // 绘制箭头
          Overlays: [
            ['Arrow', {
              width: 10,
              length: 10,
              location: 0.5
            }],
          ],
          RenderMode: "svg"
        },
        // jsplumb连接参数
        jsplumbSourceOptions: {
          filter: ".flow-node-drag", //触发连线的区域
          /*"span"表示标签，".className"表示类，"#id"表示元素id*/
          filterExclude: false,
          anchor: "Continuous", //连续锚
          allowLoopback: false
        },
        jsplumbTargetOptions: {
          filter: ".flow-node-drag",
          /*"span"表示标签，".className"表示类，"#id"表示元素id*/
          filterExclude: false,
          anchor: "Continuous", //连续锚
          allowLoopback: false
        },
        // 是否加载完毕
        loadEasyFlowFinish: false,
        // 画布数据
        data: {
          flowInfo: {
            Id: this.getUUID(),
            Name: '我的流程',
            Remark: '',
          },
          nodeList: [],
          lineList: [],
          statusListen: [], //运行状态，
        },
        timer: null, //定时器websocket是否连接,
        connecTime: 0, //判断连接websocket的时间
        connectWsFlag: false,
        currentConnect: '', //当前的连接线
        currentLine: '', //当前连接线数据
        editType: '', //编辑的类型

        stompClient: '',
        selectPreColumn: [], //高级分析选中的字段
        dataJson: {
          transformation: {
            info: {
              name: "",
              description: "",
              extended_description: "",
              trans_version: "",
              trans_type: "Normal",
              directory: "/home/admin",
              parameters: {//命名参数
                parameter: [{
                  name: '',
                  default_value: '',
                  description: '',
                }]
              },
              log: "",
              maxdate: {
                connection: "",
                table: "",
                field: "",
                offset: 0,
                maxdiff: 0
              },
              size_rowset: 10000,
              sleep_time_empty: 50,
              sleep_time_full: 50,
              unique_connections: "N",
              feedback_shown: "Y",
              feedback_size: 50000,
              using_thread_priorities: "Y",
              shared_objects_file: "",
              capture_step_performance: "N",
              step_performance_capturing_delay: 1000,
              step_performance_capturing_size_limit: 100,
              dependencies: "",
              partitionschemas: "",
              slaveservers: "",
              clusterschemas: "",
              created_user: this.$store.getters.getUserId,
              created_date: "2020/02/05 13:34:55.102",
              modified_user: "admin",
              modified_date: "2020/02/19 17:08:25.150",
              key_for_session_key: "",
              is_key_private: "N"
            },
            notepads: "",
            connection: [],
            order: {
              hop: []
            },
            step: [],
            step_error_handling: {
              error: [],
            },
            "slave-step-copy-partition-distribution": "",
            slave_transformation: "N",
            attributes: ""
          }
        },
        connections: [],
        variables: [{
          name: 'Internal.Job.Filename.Directory',
          value: 'Parent Job File Directory'
        }, {
          name: 'Internal.Job.Filename.Name',
          value: 'Parent Job Filename'
        }, {
          name: 'Internal.Job.Name',
          value: 'Parent Job Name',
        }, {
          name: 'Internal.Job.Repository.Directory',
          value: 'Parent Job Repository Directory'
        }],//运行配置-变量
        step: {
          name: null,
          type: "BasicModelInput",
          modelName: "",
          description: "",
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          isBusiness: "Y",
          connection: "",
          sql: '',
          limit: 0,
          lookup: "",
          execute_each_row: "N",
          variables_active: "N",
          lazy_conversion_active: "N",
          model_name: "",
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 176,
            yloc: 160,
            draw: "Y"
          },
          fields: {
            field: []
          }
        },
        nowDate: '',
        logFlag: true,
        title: '',
        selectRepeatRow: {
          outName: '',
          outNameCn: '',
          outType: '',
          fieldLength: '',
        }, //选中的重复字段列
        lines: [], //连线信息
        fromLabel: '',
        toLabel: '',
        executeFlag: false,
        projectName: '', //项目名称
        dataQuerySql: '',
        dataSourceId: '',
        executorId: '', //运行编号
        editableTabsValue: 'log',
        saveFlag: false, //项目保存标志，如果为true刷新页面后还原保存的信息，false就清空
        connecIndex: 99,
        sourceId: '',
        targetId: '',
        connEnabled: 'Y',
        changeFlag: false,
        routerActive: false,
      }

    },
    created() {
      // 获取当前时间
      let date = new Date();
      let mm = date.getMonth() + 1;
      let d = date.getDate();
      let h = date.getHours();
      let m = date.getMinutes();
      let s = date.getSeconds();

      mm = mm < 10 ? ("0" + mm) : mm;
      d = d < 10 ? ("0" + d) : d;
      h = h < 10 ? ("0" + h) : h;
      m = m < 10 ? ("0" + m) : m;
      s = s < 10 ? ("0" + s) : s;
      this.nowDate = date.getFullYear() + '/' + mm + '/' + d + ' ' + h + ':' + m + ':' + s;
      //初始化websocket
      // this.initWebSocket();
      //在页面刷新时将vuex里的信息保存到sessionStorage里
      let that = this;
      let code = 0;
      let code2 = 0;
      /** * 是否为mac系统（包含iphone手机） * */
      var isMac = function () {
        return /macintosh|mac os x/i.test(navigator.userAgent);
      }();
      document.onkeyup = e => {
        e.preventDefault();
        that.isConnect = false;
      };
      document.onkeydown = function (e) {
        let key = window.event.shiftKey;
        if (key) {
          if (that.isConnect) {
          } else {
            that.setConnection();
          }
        } else {
          // let ev =e||event;
          // console.info("ev-=",ev)
          // let keyCode = ev.keyCode || ev.which||ev.charCode;
          // if(keyCode === 17){
          //   code = 1;
          // }
          // if(keyCode === 67){
          //   code2 = 1;
          // }
          //
          // if(keyCode === 86){
          //   code2 = 2;
          // }
          //
          // if(keyCode === 91){
          //   code ===2;
          // }
          //
          // if(isMac === true){
          //   //command+c
          //   if(code === 2 && code2 === 1){
          //     //todo 复制步骤
          //     this.copyStep();
          //   }
          //   //command+v
          //   if(code === 2 && code2 === 2){
          //     //todo 粘贴步骤
          //
          //   }
          // }else{
          //   //control+c
          //   if(code === 1 && code2 ===1){
          //     console.info("control+c");
          //     this.copyStep();
          //     //todo 复制步骤
          //   }
          //
          //   //control+v
          //   if(code ===1 && code2 ===2){
          //     console.info("control+v")
          //     //todo 粘贴步骤
          //   }
          // }
        }


      }

    },
    activated() {
      this.routerActive = true;
    },
    deactivated() {
      this.$destroy();
      this.routerActive = false;
    },

    components: {
      flowNode,
      headerButton
    },
    mounted() {
      if(this.time !== new Date().getTime()){
        this.initWebSocket();
      }
      // 去除画布上默认的右键菜单
      document.getElementById("flowContent").oncontextmenu = function () {
        return false;
      };
      window.addEventListener('click', this.handleClick);
      $('#tool').hide();
      $('#opera').hide();
      // 项目预览工具栏不显示
      if (this.$route.path.indexOf("preProject") >= 0) {
        this.showTool = false;
      }
      this.dragControllerDiv();
      // 初始化步骤信息
      // this.$store.dispatch('clearNodeLine', this.$route.path);
      this.initData();
      this.jsPlumb = jsPlumb.getInstance();
      this.$nextTick(() => {
        this.init();
      });

      util.$on("init", () => {
        this.init();
      });
      util.$on('changeLoadingFalse', () => {
        this.changeLoadingFalse();
      });
      util.$on("changeLoading", () => {
        this.changeLoading();
      });
      util.$on("changeNodeSite", (data) => {
        this.changeNodeSite(data);
      });
      util.$on("closeDialog", () => {
        this.closeDialog();
      });
      util.$on("updateNode", (res) => {
        this.updateNode(res);
      });
      this.getGroup();
      // 初始化下发给弹窗高度
      this.initTool();
      // this.editFlow()
      // this.initWebSocket();
      this.initFlowHeight();

      window.onresize = () => {
        return (() => {
          this.screenHeight = document.body.clientHeight;
        })();
      };

      // 浏览器刷新前保存动态路由信息
      window.addEventListener('beforeunload', () => {
        let path = this.$route.path;
        let component = 'flowMain.vue';
        let pathParam = {
          path: path,
          name: this.$route.name,
          component: 'flowMain.value',
          meta: {
            title: this.projectName === '' ? this.$route.name : this.projectName,
          }
        };
        localStorage.setItem('cacheViews', JSON.stringify(this.$store.state.tagsView.visitedViews));
        localStorage.setItem("newRouter", JSON.stringify(pathParam))
      })

    },
    destroyed() {
    },
    methods: {

      addVariables() {
        this.variables.push({
          name: '',
          value: ''
        })
      },

      deleteVariable(index) {
        this.variables.splice(index, 1);
      },

      /**
       * 参数配置
       */
      configParam() {
        this.configVisiable = true;
      },
      /**
       * 保存命名参数信息
       */
      saveRunParam() {
        this.saveDataJson();
        this.configVisiable = false;
      },

      addParam() {
        if (this.dataJson.transformation.info.parameters === '') {
          this.dataJson.transformation.info.parameters = {
            parameter: []
          }
        }
        this.dataJson.transformation.info.parameters.parameter.push({
          name: '',
          default_value: '',
          description: '',
        })
      },
      deleteParam(index) {
        this.dataJson.transformation.info.parameters.parameter.splice(index, 1)
      },

      /**
       * 步骤度量-格式化状态
       */
      formatStatus(row, column) {
        if (row !== null && row !== undefined) {
          if (row.statusDescription === 'Halted') {
            return '终止'
          } else if (row.statusDescription === 'Halting') {
            return '停止'
          } else if (row.statusDescription === 'Disposed') {
            return '已处理'
          } else if (row.statusDescription === 'Stopped') {
            return '已停止'
          } else if (row.statusDescription === 'Init') {
            return '初始化';
          } else if (row.statusDescription === 'Finished') {
            return '已完成'
          } else if (row.statusDescription === 'Paused') {
            return '已暂停'
          } else if (row.statusDescription === 'Running') {
            return '运行中'
          } else if (row.statusDescription === 'Empty') {
            return '空'
          }
        }

      },

      /**
       * 复制步骤信息
       */
      copyStep() {

      },

      /**
       * 修改画布大小
       * @param {Object} type
       */
      addFlowSize(type) {
        this.jsPlumb.setZoom(0.5);
        let height = $('#flow-detail').height();
        let flowContent = document.getElementById('flowNode');
        if (type === 'add') {
          this.canvasNumber = this.canvasNumber + 10;
        } else {
          this.canvasNumber = this.canvasNumber - 10;
        }
        if (this.canvasNumber >= 100) {
          this.isRedues = true;
          this.disRedues = true;

        } else {
          this.isRedues = false;
          this.disRedues = false;
        }
        flowContent.style.height = height * (this.canvasNumber / 100) + 'px';
      },

      /**
       * 关闭新增分组弹窗
       */
      closeGroup() {
        this.$refs['groupForm'].resetField();
        this.addGroupViaiable = false;
      },

      /**
       * 新增分组数据
       */
      addGroupData() {
        this.$refs['groupForm'].validate((valid) => {
          if (!valid) {
          } else {
            let para = Object.assign({}, this.groupForm);
            let params = {
              groupName: para.groupName,
              groupType: para.groupType,
            };
            this.addGroupLoading = true;
            checkGroupName(params).then(res => {
              if (res.data.code === '10000' && res.data.content === false) {
                addGroup(para).then(res => {
                  let {
                    data
                  } = res;
                  if (data.code === '10000') {
                    this.$message({
                      message: '新增成功',
                      type: 'success'
                    })
                    this.groupIds.push({
                      groupId: data.content.groupId + "," + data.content.groupName,
                      groupName: data.content.groupName,
                    })
                    this.editForm.groupId = data.content.groupId + "," + data.content.groupName;
                  } else {
                    this.message({
                      message: '新增失败',
                      type: 'error'
                    })
                  }
                  this.$refs['groupForm'].resetFields();
                  this.addGroupLoading = false;
                  this.addGroupViaiable = false;
                })
              } else {
                this.$message({
                  message: '分组已存在，请修改分组名称或分组类型',
                  type: 'error'
                });
                this.addGroupLoading = false;
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

      /**
       * 添加分组
       */
      addGroup() {
        this.addGroupViaiable = true;
      },
      // 初始化画布高度
      initFlowHeight() {
        if (this.operaShow) {
          let flowContent = document.getElementById('flowContent');
          let flowDetail = document.getElementById("flow-detail");

          let opera = document.getElementById('opera');
          let operaHeight = $('#opera').height();
          if (flowDetail.offsetHeight < operaHeight) {
            flowContent.style.height = 0 + 'px';
          }
          flowContent.style.height = flowDetail.offsetHeight - operaHeight - 5 + 'px';
        }
      },

      clickConnect(item, ev) {
        this.connecIndex = item.index;
        let lines = this.jsPlumb.getConnections({
          source: this.sourceId,
          target: this.targetId
        });
        if (item.index === 0) {
          // 判断节点链接状态
          let enable = '';
          if (this.connEnabled === 'Y') {
            enable = 'N';
            lines[0].setType("basic")
          } else {
            enable = 'Y';
            lines[0].setType("selected")
          }
          this.changeFlag = true;
          let line = {
            key: this.$route.path,
            value: {
              fromId: this.sourceId,
              toId: this.targetId,
              enabled: enable,
            }
          };
          this.$store.dispatch('updateLine', line);
          // 更新节点连接数据

          this.connecOperaShow = false;
        } else {
          // 判断是from是否是过滤插件 如果是过滤插件需要修改对应的send_true或者send_false;
          let formLabel = this.$store.getters.getMappingData(this.sourceId);
          let toLabel = this.$store.getters.getMappingData(this.targetId);
          let stepParam = {
            key: this.$route.path,
            value: formLabel
          };
          let curStep1 = this.$store.getters.getCurrentStep(stepParam);
          if (curStep1.type === 'FilterRows') {
            if (curStep1.send_true_to === toLabel) {
              curStep1.send_true_to = undefined;
            } else if (curStep1.send_false_to === toLabel) {
              curStep1.send_false_to = undefined;
            }
            let updateStepParam = {
              key: this.$route.path,
              value: {
                oldStepName: curStep1.name,
                step: curStep1,
              }
            };
            this.$store.dispatch('updateStepInfo', updateStepParam);
            let curStep = this.$store.getters.getCurrentStep(stepParam);
          }
          // 删除节点连接
          this.changeFlag = true;
          this.jsPlumb.deleteConnection(lines[0]);
          this.connecOperaShow = false;
        }
      },
      active(index) {
        return index === this.connecIndex;
      },
      // 隐藏执行结果弹窗
      hideOpera() {
        this.operaShow = false;
        let logData = {
          key: this.$route.path,
          value: {
            dataQuerySql: this.dataQuerySql,
            dataSourceId: this.dataSourceId,
            stepName: this.stepName,
            operaShow: this.operaShow,
            log: this.logs,
            showLog: this.showLog, //点击的执行按钮
            tranFinished: true,
            labelName: this.labelName,
            executeCount: this.executeCount,
            dataPre: this.dataPre,
            dataColumn: this.dataColumn,
            dataPreColumn: this.dataPreColumn,
            columnMessage: this.columnMessage,
          }
        };
        let flowDetail = document.getElementById("flow-detail");
        let doc = document.getElementById('flowContent');
        doc.style.height = flowDetail.offsetHeight + 'px';
        this.$store.dispatch('setExecuteLog', logData);
      },
      isActive(index) {
        return index === this.showLog;
      },

      // 选择操作
      changeOpear(item) {
        this.showLog = item.index;
        if (item.index === 1) {
          if (this.measureData.length === 0) {
            this.measureLoading = true;
          }
          this.measureData = this.data.nodeList;
          this.measureLoading = false;
        }
        let logData = {
          key: this.$route.path,
          value: {
            dataQuerySql: this.dataQuerySql,
            dataSourceId: this.dataSourceId,
            stepName: this.stepName,
            operaShow: this.operaShow,
            log: this.logs,
            showLog: this.showLog, //点击的执行按钮
            tranFinished: true,
            labelName: this.labelName,
            executeCount: this.executeCount,
            dataPre: this.dataPre,
            dataColumn: this.dataColumn,
            dataPreColumn: this.dataPreColumn,
            columnMessage: this.columnMessage,
          }
        };
        this.$store.dispatch('setExecuteLog', logData);
      },
      // 监听页面鼠标点击事件
      handleClick(event) {
        // 点击画布空白地方触发
        if (event.target.className === 'flow-node') {
          this.clickNode = '';
          this.connecOperaShow = false;
          this.filter = false;
          this.selectMain = false;
          this.operaData = [{
            index: 0,
            label: '日志',
            icon: 'iconfont icon-rizhi'
          }, {
            index: 1,
            label: '步骤度量',
            icon: 'iconfont icon-duliang'
          }, {
            index: 2,
            label: '数据预览',
            icon: 'iconfont icon-shujuyulan-copy'
          }, {
            index: 3,
            label: '字段信息',
            icon: 'iconfont icon-ziduanliebiao'
          }];
          this.showLog = 0;
        } else {
        }
      },


      setConnection() {
        this.isConnect = true;
      },

      enterShift() {
      },
      // 修改状态
      changeLoading() {
        this.contentLoading = true;
      },
      changeLoadingFalse() {
        this.contentLoading = false;
        this.reload();
      },

      beforeunloadHandler(e) {
        e = e || window.event;
        if (e) {
          e.returnValue = '关闭提示'
        }
        return '关闭提示'
      },

      handleSelectionColumn(selfs) {
        this.selelctModelColumn = selfs;
      },
      // 双击显示输入框
      updateCell(row, column, cell, event) {
        row.updateFlag = true;
      },

      // 获取鼠标位置
      getMousePos(event, from, to, id) {
        this.toLabel = to;
        this.fromLabel = from;
        let width = document.body.scrollWidth;
        let p = document.getElementById('fielter');
        p.style.position = "absolute";
        p.style.left = event.clientX - (width * 0.2) + 'px';
        p.style.top = event.clientY - 140 + 'px';
        this.filter = true;
      },
      // 获取鼠标位置
      getMousePos1(event, from, to, id) {
        this.toLabel = to;
        this.fromLabel = from;
        let width = document.body.scrollWidth;
        let p = document.getElementById('fielter1');
        p.style.position = "absolute";
        p.style.left = event.clientX - (width * 0.2) - 10 + 'px';
        p.style.top = event.clientY - 105 + 'px';
        this.selectMain = true;
      },
      /**
       * 分发复制
       */
      distribution(type) {
        let path = this.$route.path;
        let lineList = this.$store.state.flowMain.lines[path];
        //获取当前步骤信息
        let params = {
          key: path,
          value: this.fromLabel,
        };
        let curStep = this.$store.getters.getCurrentStep(params);
        let css = '';
        lineList.forEach(item => {
          if (type === 'Y') {
            css = ''
          } else {
            css = 'dis_tribution';
          }
          if (item.fromId === this.sourceId) {
            let lines = this.jsPlumb.getConnections({
              source: item.fromId,
              target: item.toId
            });
            if(lines.length>0){
              lines[0].setLabel({
                label: '',
                cssClass: css,
                location: 0.25,
              });
            }

          }
        });
        curStep.distribute = type;//type为'Y'分发，为'N'复制
        let param = {
          key: path,
          value: {
            oldStepName: this.fromLabel,
            step: curStep,
          }
        };
        this.$store.dispatch('updateStepInfo', param);
        this.inputCopy = false;
      },
      //分发复制
      isCopy() {
        let paramData = {
          key: this.$route.path,
          value: this.fromLabel
        };
        let currnetStep = this.$store.getters.getCurrentStep(paramData);
        let path = this.$route.path;
        let lineList = this.$store.state.flowMain.lines[path];
        let count = 0;
        if (lineList !== undefined) {
          lineList.forEach(item => {
            if (item.fromId === this.sourceId) {
              count++;
            }
          });
          if (count === 2) {
            this.inputStepName = this.fromLabel;
            this.inputCopy = true;
          } else if (count === 3) {
            this.distribution(currnetStep.distribute);
          }
        }
      },

      //主输出步骤
      mainOutStep() {
        this.selectMain = false;
      },
      //错误输出步骤
      errorOutStep() {
        let flag = false;
        this.dataJson.transformation.step_error_handling.error.forEach((item, index) => {
          if (item.source_step === this.fromLabel) {
            let fromId = this.$store.getters.getMappingkey(item.source_step);
            let toId = this.$store.getters.getMappingkey(item.target_step);
            let lines = this.jsPlumb.getConnections({
              source: fromId,
              target: toId,
            });
            //获取当前步骤信息
            let params = {
              key: this.$route.path,
              value: this.fromLabel,
            };
            let curStep = this.$store.getters.getCurrentStep(params);
            if (curStep.distribute === 'N') {
              lines[0].setLabel({
                label: '',
                cssClass: 'dis_tribution',
                location: 0.25,
              })
            }
            lines[0].setType("selected");
            flag = true;
            this.dataJson.transformation.step_error_handling.error.splice(index, 1)
          }
        });
        // if(flag){
        //   // toto修改连线和errors
        //
        // }else{
        //新增
        let lines = this.jsPlumb.getConnections({
          source: this.sourceId,
          target: this.targetId
        });
        lines[0].setType("error");
        lines[0].setLabel({
          label: '',
          cssClass: 'labelClass2',
          location: 0.25,
        });
        lines[0].setHoverPaintStyle({
          stroke: '#fd6d67',
          strokeWidth: 4
        });
        let errors = {
          source_step: this.fromLabel,
          target_step: this.toLabel,
          is_enabled: "Y"
        };
        this.dataJson.transformation.step_error_handling.error.push(errors);
        this.isCopy();
        // }
        this.selectMain = false;
      },
      // 符合条件输出
      fit() {
        let lines = this.jsPlumb.getConnections({
          source: this.sourceId,
          target: this.targetId
        });
        lines[0].setLabel({
          label: '',
          cssClass: 'labelClass',
          location: 0.25,
        });
        let params = {
          key: this.$route.path,
          value: this.fromLabel,
        };
        let curStep = this.$store.getters.getCurrentStep(params);
        curStep.send_true_to = this.toLabel;
        let param = {
          key: this.$route.path,
          value: {
            oldStepName: this.fromLabel,
            step: curStep,
          }
        };
        this.$store.dispatch('updateStepInfo', param);
        this.filter = false;
        // 获取过滤步骤信息
      },
      // 不符合条件输出
      unFit() {
        let params = {
          key: this.$route.path,
          value: this.fromLabel,
        };
        let curStep = this.$store.getters.getCurrentStep(params);
        curStep.send_false_to = this.toLabel;
        let param = {
          key: this.$route.path,
          value: {
            oldStepName: this.fromLabel,
            step: curStep,
          }
        };
        this.$store.dispatch('updateStepInfo', param);
        let lines = this.jsPlumb.getConnections({
          source: this.sourceId,
          target: this.targetId
        });
        lines[0].setLabel({
          label: '',
          cssClass: 'labelClass1',
          location: 0.25,
        });
        this.filter = false;
      },

      // todo修改鼠标拖拽
      dragControllerDiv() {
        let resize = document.getElementById("middle");
        let height = document.getElementById('opera');
        let flowDetail = document.getElementById('flow-detail');
        let flowContent = document.getElementById('flowContent');
        // let logsDiv = document.getElementById('logDiv');
        resize.onmousedown = function (e) {
          // 获取开始位置
          let startY = e.clientY;
          let logHeight = height.offsetHeight;
          document.onmousemove = function (e) {
            let contentHeight = flowDetail.offsetHeight;

            let endY = e.clientY;
            let moveLength = startY - endY;
            if (logHeight + moveLength > 35) {
              height.style.height = logHeight + moveLength + 'px';
              // logsDiv.style.height = logHeight + moveLength - 79 + 'px';
              flowContent.style.height = contentHeight - (logHeight + moveLength) + 'px';
            }
          };
          document.onmouseup = function (evt) {
            evt.stopPropagation();
            document.onmousemove = null;
            document.onmouseup = null;
            resize.releaseCapture && resize.releaseCapture();
          };
          resize.setCapture && resize.setCapture();
          return false;
        };
      },
      dragDiv(data) {
      },
      // 显示输入字段
      showInput(data) {
        let param = {
          key: this.$route.path,
          value: data.label,
        };
        this.columnTitle = '输入字段信息';
        let lastStep = this.$store.getters.getLastStep(param);
        let inputColumn = [];
        if (lastStep.length > 0) {
          lastStep.forEach((item, index) => {
            let fields = item.fields.field;
            if (fields.length > 0) {
              fields.forEach(field => {
                field.stepName = item.name;
              });
              inputColumn = fields;
              this.tableData = inputColumn;
              this.columnViaiable = true;
            } else {
              this.$alert('无法找到输入字段信息', '字段信息', {
                confirmButtonText: '确定',
              });
            }
          })
        } else {
          this.$alert('无法找到输入字段信息', '字段信息', {
            confirmButtonText: '确定',
          });
        }
      },
      // 显示输出字段
      showOutput(data) {
      },

      // 下载文件时选择的字段信息
      handleSelectionChange(sels) {
        this.selectColumns = [];
        this.selectColumns = sels;
      },
      // 关闭弹窗
      closeDialog() {
        this.centerDialogVisible = false;
      },
      registerComponent(name) {
        return import(`../dataExploration/plugin/${name}/${name}.vue`);
      },
      openEdit(node) {
        // this.centerDialogVisible = true;
        this.title = node.label;
        node.projectId = this.projectId;
        node.projectName = this.projectName;
        node.datasourceId = this.dataSourceId;
        node.stepSql = this.dataQuerySql;
        this.registerComponent(node.pluginId).then(component => {
          const cpt = Vue.extend(component.default);
          new cpt({
            data: {
              nodeData: node,
              key: this.$route.path
            }
          }).$mount(this.$refs.component);
        });
        this.centerDialogVisible = true;
      },

      cloaseDialog(done) {
        this.centerDialogVisible = false;
      },
      // 显示隐藏日志弹窗
      clearLog() {
        $('#log').html('');
      },
      initData() {
        let tabData = localStorage.getItem('operaData');
        if (tabData !== null) {
          this.operaData = JSON.parse(localStorage.getItem('operaData'));
        }
        let path = this.$route.path;
        let flowData = this.$store.state.flowMain.flowData;
        if (flowData[path] !== undefined) {
          this.data = flowData[path];
        } else {

        }
        this.measureData = this.data.nodeList;
        let logsData = this.$store.getters.getExecuteLog(path);
        if (logsData !== undefined) {
          // this.logs=logsData;
        }
        let btnStatus = this.$store.state.flowMain.btnStatus[path];
        if (btnStatus !== undefined) {
          this.projectInfo = btnStatus;
        }
        this.parentPath = path;
        let dataKrt = this.$store.state.flowMain.flowDataJson;
        if (dataKrt[path] !== undefined) {
          this.dataJson = dataKrt[path];
          // this.dataJson.transformation.connection = Array.from(dataJson.transformation.connection);
        }
        let projectInfo = this.$store.state.flowMain.projects;
        if (projectInfo[path] !== undefined) {
          this.projectId = projectInfo[path].projectId;
          this.projectName = projectInfo[path].projectName;
          this.$route.meta.title = this.projectName;
          this.$store.dispatch('updateView', this.$route);
        }

        //获取项目md5信息
        let md5Data = this.$store.state.flowMain.projectMd5;
        if (md5Data[path] !== undefined) {
          this.md5 = md5Data[path]
        }
        // 获取执行结果
        let executeData = this.$store.getters.getExecuteLog(path);
        if (executeData !== undefined) {
          this.showLog = executeData.showLog;
          if (this.showLog !== 0) {
            this.dataPre = executeData.dataPre;
            // if (this.dataPre.length > 0) {
            //   this.buttonDis = false;
            // }
            this.stepName = executeData.stepName;
            this.dataSourceId = executeData.dataSourceId;
            this.dataQuerySql = executeData.dataQuerySql;
            this.dataColumn = executeData.dataColumn;
            this.dataPreColumn = executeData.dataPreColum;
            this.columnMessage = executeData.columnMessage;
            this.labelName = executeData.labelName;
            this.executeCount = executeData.executeCount;
            this.analyzeFlag = true;
          }
          this.logs = executeData.log;
          this.operaShow = executeData.operaShow;
          $('#log').html(this.logs);
        }
      },
      initWeb(data) {
        let path = this.$route.path;
        //初始化步骤信息
        let flowData = this.$store.state.flowMain.flowData;
        if (flowData[path] !== undefined) {
          this.data = Object.assign({}, flowData[path]);
          // let runSteps = this.$store.state.flowMain.runStps[path];
          //步骤运行状态
        } else {

        }

        let btnStatus = this.$store.state.flowMain.btnStatus[path];
        if (btnStatus !== undefined) {
          this.projectInfo = btnStatus;
        }
        this.parentPath = path;
        //websocket运行信息
        let webSocketResponse = data.value;

        if (webSocketResponse !== undefined) {
          let runSteps = webSocketResponse.content.executorSteps;
          let content = webSocketResponse.content;
          let datasourceId = content.masterDatasourceId;
          let code = webSocketResponse.code;
          let transFinished = content.transFinished;
          let flowId = content.requestId;

          if (this.$route.path === flowId) {
            if (code !== '10000') {
              if (runSteps !== undefined && runSteps != null) {
                for (let j = 0; j < runSteps.length; j++) {
                  if (runSteps[j].previewFieldNames !== null && runSteps.previewRows !== null) {
                    let params = {
                      key: flowId,
                      value: {
                        stepName: item.label,
                        data: {
                          fields: runSteps[j].previewFieldNames,
                          rows: runSteps[j].previewRows,
                        }
                      }
                    };
                    this.$store.dispatch('addPreViewData', params)
                  } else {
                    // let params = {
                    //   key: flowId,
                    //   value: {
                    //     stepName: item.label,
                    //     data: {
                    //       fields: [],
                    //       rows: [],
                    //     }
                    //   }
                    // };
                    // this.$store.dispatch('addPreViewData', params)
                  }
                  updatePluginState(this.data, runSteps[j], content, datasourceId)

                }
                this.setCashFlowData();
              }
            } else {

              let runData = [];
              //更新节点状态
              if (runSteps !== undefined && runSteps !== null) {

                for (let j = 0; j < runSteps.length; j++) {
                  if (runSteps[j].previewFieldNames !== null && runSteps[j].previewRows !== null) {
                    let params = {
                      key: flowId,
                      value: {
                        stepName: runSteps[j].stepName,
                        data: {
                          fields: runSteps[j].previewFieldNames,
                          rows: runSteps[j].previewRows,
                        }
                      }
                    };
                    this.$store.dispatch('addPreViewData', params)
                  } else {

                  }
                  updatePluginState(this.data, runSteps[j], content, datasourceId)

                }
                this.setCashFlowData();
              }

              this.transFinished = transFinished;
              if (transFinished) {
                this.reload();
                this.transFinished = false;
                this.projectInfo.isExecuted = false;
                this.projectInfo.isSave = false;
                this.projectInfo.isPause = true;
              }
            }
            //拼接日志
            let log = webSocketResponse.content.log;
            if (log.endsWith("\n")) {
              log = log.substr(0, log.length - 1);
            }
            this.logs = this.$store.state.flowMain.executeLogs[flowId] === undefined ? '' : this.$store.state.flowMain.executeLogs[flowId].log;

            let reg = new RegExp("\n", "g");
            if (log !== '') {
              let ll = log.replace(/\n/g, "</br>");
              this.logs = this.logs + "</br>" + ll;
              this.logFlag = true;
              let executeResult = {
                key: this.projectId,
                value: this.logs
              };
              this.$store.dispatch('setExecuteResult', executeResult);

              //根据项目名称获取日志信息
              let executeLog = this.logs;
              //执行完毕将日志存入到缓存
              let logData = {
                key: flowId,
                value: {
                  operaShow: true,
                  log: executeLog, //日志
                  showLog: this.showLog, //点击的执行按钮
                  tranFinished: transFinished, //是否执行完成的标志
                }
              };
              this.$store.dispatch('setExecuteLog', logData);
              $('#log').html(this.logs);
            }
          } else {

          }


        }
      },

      //平台跳转接口
      redirectUrl(url) {
        let subfrom = document.createElement("form");
        subfrom.style = "display:none;";
        subfrom.method = 'get';
        subfrom.target = "_blank";
        subfrom.action = base + '/dataintegration-common-sso-provider/oauth/authorize';
        // ?Authorization='+this.$store.getters.getToken+'&response_type=code&redirect_uri='+'http://prime.shuzhaninfo.com:51808/de-vue'+'&client_id=dp-client';

        let Authorization = document.createElement('input');
        Authorization.type = 'text';
        Authorization.name = 'Authorization';
        Authorization.value = this.$store.getters.getToken;
        subfrom.appendChild(Authorization);

        let response_type = document.createElement('input');
        response_type.type = 'text';
        response_type.name = 'response_type';
        response_type.value = "code";
        subfrom.appendChild(response_type);

        let redirect_uri = document.createElement('input');
        redirect_uri.type = 'text';
        redirect_uri.name = 'redirect_uri';
        redirect_uri.value = url;
        subfrom.appendChild(redirect_uri);

        subfrom.appendChild(response_type);
        let dp_client = document.createElement('input');
        dp_client.type = 'text';
        dp_client.name = 'client_id';
        dp_client.value = 'dp-client';
        subfrom.appendChild(dp_client);
        document.body.appendChild(subfrom);
        subfrom.submit();
      },


      initTool() {


      },
      handleSelect(key, keyPath) {
      },
      // 初始化websoket链接
      initWebSocket() {
        // 判断浏览器是否支持websocket
        if (typeof (WebSocket) === undefined) {
        } else {
          this.connection(this.data);
        }
      },

      connection(data) {
        let _this = this;
        this.stompClient = _this.globalWs.ws;
        let ws = this.stompClient.ws;
        let path = this.$route.path;
        ws.onclose = function (frame) {
          _this.initWs();
          this.stompClient = _this.globalWs.ws;
        };
        let connectFlag = this.stompClient.connected;
        this.contentLoading = false;
        // // 建立连接对象
        let data1 = _this.data;
        let userId = this.$store.getters.getUserId;
        let sublength = Object.keys(this.stompClient.subscriptions).length;
        if (sublength === 0) {
          this.stompClient.subscribe('/user/' + userId + '/executeById', function (response) {
          });
          this.stompClient.subscribe('/user/' + userId + '/stop', function (response) {
          });
          // 监听返回状态信息
          this.stompClient.subscribe('/user/' + userId + '/runningState', function (response) {

            //获取节点运行状态
            let code = JSON.parse(response.body).code;
            let content = JSON.parse(response.body).content;
            let flowId = content.requestId;
            // if (code !== '10000') {
            //   _this.$message({
            //     message: content.log,
            //     type: 'error'
            //   })
            // }
            let responseData = {
              key: flowId,
              value: JSON.parse(response.body)
            };
            //将返回信息返回
            _this.$store.dispatch('addWebData', responseData);
            let tranFinished = false;
            if (content !== null) {
              _this.dataSourceId = content.masterDatasourceId;
              _this.executorId = content.executorId;
              // 获取实时运行状态
              tranFinished = content.transFinished;
            }
            let btnStatus = {};
            if (tranFinished) {
              btnStatus = {
                key: flowId,
                UUID: _this.getUUID(),
                value: {
                  isExecuted: false,
                  isSave: false,
                  isPause: true,
                }
              }
            } else {
              btnStatus = {
                key: flowId,
                UUID: _this.getUUID(),
                value: {
                  isExecuted: true,
                  isSave: true,
                  isPause: false,
                }
              }
            }
            _this.$store.dispatch('setBtnStatus', btnStatus);

            let running = {
              projectId: _this.projectId,
              executorId: _this.executorId
            };
            // 保存项目运行信息
            _this.$store.dispatch('addProjectRunning', running);
            _this.initWeb(responseData);
          });
        }
      },


      preExecute() {
        this.runForm = {
          logLevel: 'Basic',
          safeModeEnabled: false,
        }
        if (this.dataJson.transformation.info.parameters.parameter === undefined || this.dataJson.transformation.info.parameters.parameter.length === 0 || this.dataJson.transformation.info.parameters === '') {
          this.dataJson.transformation.info.parameters = {
            parameter: []
          }
        }
        this.runConfigVisiable = true;
      },

      // 执行项目
      execute() {
        this.runConfigVisiable = false;
        // 通过双击项目展示项目信息，需要换种判断方式
        let path = this.$route.path;
        let logData = {
          key: this.requestId,
          value: {
            operaShow: false,
            log: '', //日志
            showLog: '', //点击的执行按钮
            tranFinished: false, //是否执行完成的标志
          }
        };
        let btnStatus = {
          key: path,
          UUID: this.getUUID(),
          value: {
            isExecuted: true,
            isSave: true,
            isPause: false,
          }
        };
        this.projectInfo.isExecuted = true;
        this.projectInfo.isSave = true;
        this.projectInfo.isPause = false;
        this.$store.dispatch('setBtnStatus', btnStatus);
        this.transFinished = true;
        this.logs = '';
        this.$store.dispatch('setExecuteLog', logData);
        let steps = this.$store.state.flowMain.stepList[path];
        let lines = this.$store.state.flowMain.lines[path];
        let tmpStep = [];
        let tmpLines = [];
        let nodeLists = this.data.nodeList;
        let lineList = this.data.lineList;
        nodeLists.forEach((item, index) => {
          if (steps !== undefined) {
            steps.forEach((step, i) => {
              if (step.name === item.label) {
                tmpStep.push(step);
              }
            })
          }
          // if (lines !== undefined) {
          //   lines.forEach((line, j) => {
          //     if (line.to === item.label || line.form === item.label) {
          //       tmpLines.push(line);
          //     }
          //   })
          // }
        });
        lineList.forEach(item => {
          if (lines !== undefined) {
            lines.forEach(line => {
              if (item.sourceUUId === line.sourceUUId && item.targetUUId === line.targetUUId) {
                tmpLines.push(line);
              }
            })
          }
        })
        steps = tmpStep.length === 0 ? undefined : tmpStep;
        lines = tmpLines;
        let connection = this.$store.state.flowMain.connections[path];
        let nodeName = '';
        let flag = true;
        if (steps === undefined) {
          this.$message({
            message: '请拖入控件再执行！',
            type: 'error'
          });
          this.projectInfo = {
            isPause: true,
            isExecuted: false,
            isSave: false,
          }
        } else {
          steps.forEach((item, index) => {
            if (item.initFlag === undefined) {
              nodeName = item.name;
              flag = false;
            } else if (item.initFlag === false) {
              nodeName = item.name;
              flag = false;
            }
          });
          if (!flag) {
            this.$message({
              message: nodeName + '步骤没有初始化完成！',
              type: 'error'
            });
            this.projectInfo = {
              isPause: true,
              isExecuted: false,
              isSave: false,
            }
          } else {
            this.dataJson.transformation.order.hop = lines ? lines : [];
            this.dataJson.transformation.step = steps;
            if (connection !== undefined) {
              // let ss = this.dataJson.transformation.connection.concat(connection);
              // let newConnection = new Set(ss);
              // let connections = Array.from(newConnection);
              // compareObj(connections);
              this.dataJson.transformation.connection = connection;
            } else {
              this.dataJson.transformation.connection = [];
            }
            // 去除重复的数据源信息
            let _this = this;
            $('#log').html(''); //清空日志
            this.showLog = 0;
            this.analyzeFlag = false;
            this.logs = '';

            //如果项目为保存或者不是本人创建的项目，新增
            let createFlag = this.$store.getters.getCreateFlag(this.$route.path);
            let deleteProject = this.$store.getters.getDeleteProjects;
            if (this.projectId === '' || deleteProject.indexOf(this.projectId) >= 0) {
              this.getGroup();
              this.addTitle = '保存项目';
              this.editFormVisiable = true;
              this.executeFlag = true;
              this.editForm = {
                projectName: this.projectName,
                description: '',
                groupId: '',
                projectStatus: '',
                projectType: '',
              }
            } else {

              localStorage.removeItem(this.$route.path);
              let nodeDatas = this.reverData();

              this.dataJson.transformation.nodeList = nodeDatas;
              //更新项目
              let params = {
                projectId: this.projectId,
                projectName: null,
                description: null,
                projectStatus: null,
                projectType: null,
                groupId: null,
                groupName: null,
                projectFileVO: {
                  projectFile: JSON.stringify(this.dataJson),
                  projectVersion: 0,
                  md5: this.md5,
                }
              };
              this.contentLoading = true;
              this.lodingContext = '保存中';
              updateSelectProject(params).then(res => {
                let {
                  data
                } = res;
                if (data.code === '10000') {
                  this.changeFlag = false;
                  this.isSave = true;
                  this.isExecuted = true;
                  this.isPause = false;
                  this.pauseColor = '#1eaafa';
                  this.acviteColor = '#EAEAEA';
                  // 判断webscoket连接状态 如果还没有连接上就不执行运行方法 显示加载效果
                  this.contentLoading = true;
                  this.lodingContext = '连接中';
                  // this.executeProject();
                  this.timer = setInterval(this.getWsStatus, 1000);
                  let md5Data = {
                    key: path,
                    md5: data.content,
                  }
                  let createFlagData = {
                    key: path,
                    value: createFlag,
                  };
                  this.$store.dispatch('setCreateFlag', JSON.stringify(createFlagData));
                  this.$store.dispatch('addProjectMd5', md5Data);
                }
              })

            }
          }

        }

      },

      getWsStatus() {
        this.stompClient = this.globalWs.ws;
        let ws = this.stompClient.ws;

        if (this.connecTime < 10) {
          if (this.stompClient.connected) {
            if (ws.readyState === 2 || ws.readyState == 3) {
              this.stompClient.disconnect({}, function (frame) {
              });
              this.initWs();
              clearInterval(this.timer);
            } else {
              clearInterval(this.timer);
              this.connectWsFlag = true;
              this.contentLoading = false;
              // if (this.stompClient === '') {
              this.initWebSocket();
              // }
              this.executeProject();
            }
          } else {
            this.connecTime = this.connecTime + 1;
            this.stompClient = this.globalWs.ws;
            this.connectWsFlag = true;
          }
        } else {
          clearInterval(this.timer);
          this.connectWsFlag = true;
          this.contentLoading = false;
          this.$message({
            message: '连接超时，请刷新重试',
            type: 'error'
          });
          this.connecTime = 0;
        }
      },

      //发送websocket消息
      executeProject() {
        this.dataJson.transformation.info.name = this.projectName;
        let variable = {};
        this.variables.forEach(item => {
          if (item.name === '' || item.value === "") {

          } else {
            variable[item.name] = item.value
          }
        })
        this.dataJson.transformation.info.parameters.parameter.forEach((item, index) => {
          if (item.name === "" || item.default_value === "") {
            this.dataJson.transformation.info.parameters.parameter.splice(index, 1);
          }
        })
        //每次运行清空上一步的日志
        try {
          this.stompClient.send("/executeByFile", {}, JSON.stringify({
            projectId: this.projectId,
            requestId: this.requestId,
            variables: variable,
            preview: true,
            previewModel: 'FIRST',
            previewSize: 100,
            logLevel: this.runForm.logLevel,
            projectType: 'JCJB',
            projectName: this.projectName,
            safeModeEnabled: this.runForm.safeModeEnabled,
            projectFile: JSON.stringify(this.dataJson),
          }))
        } catch (e) {
          console.info("e---", e)
        }
        let params = {
          key: this.$route.path,
          value: this.projectId
        };
        // 存储路径项目编号映射
        this.$store.dispatch('setPathProject', params);
        let flowNode = document.getElementById('flowContent');
        let flowDetail = document.getElementById("flow-detail");
        let opera = document.getElementById('opera');
        let operaHeight = $('#opera').height();
        flowNode.style.height = flowDetail.offsetHeight - operaHeight - 5 + 'px';
        this.operaShow = true;
      },

      pause() {
        if (this.stompClient === '') {
          this.initWebSocket();
        }
        let executorId = '';
        getStatus().then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            if (data.content.length > 0) {
              data.content.forEach(item => {
                if (this.projectId === item.projectId) {
                  executorId = item.executorId;
                }
              })
            } else {
              executorId = this.$store.state.flowMain.projectsRun[this.projectId];
            }

          } else {
            executorId = this.$store.state.flowMain.projectsRun[this.projectId];
          }
          this.projectInfo.isSave = false;
          this.projectInfo.isExecuted = false;
          this.projectInfo.isPause = true;
          this.pauseColor = '#EAEAEA';
          this.acviteColor = '#1eaafa';
          this.stompClient.send("/stop", {}, JSON.stringify({
            executorId: executorId,
            projectId: this.projectId,
          }));

        });

      },

      getGroup() {
        this.groupIds = [];
        let para = {
          groupName: '',
          pageNum: 1,
          pageSize: 100
        };

        getGroup(para).then(res => {
          let {
            data
          } = res;
          let datas = [];
          if (data.code === '10000') {
            for (let i = 0; i < data.content.list.length; i++) {
              datas.push({
                groupId: data.content.list[i].groupId + "," + data.content.list[i].groupName,
                groupName: data.content.list[i].groupName,
              })
            }
            this.groupIds = datas;
          }
        });
      },
      addProject() {
        //获取是否他人创建项目的标志 flag 为true表示是本人创建的项目 为false表示是他人创建的项目分享给自己
        let flag = this.$store.getters.getCreateFlag(this.$route.path);
        // 判断项目是否保存过 ||是否是他人项目
        if (this.projectName === '' || this.projectId === '') {
          // 打开新增项目页面获取分组信息
          this.getGroup();
          this.addTitle = '保存项目';
          this.editFormVisiable = true;
          this.editForm = {
            projectName: '',
            description: '',
            groupId: '',
            projectStatus: '',
            projectType: '',
          };
        } else {
          // 已保存过的项目就修改
          this.updateProject();
        }
      },

      // 修改项目
      updateProject() {
        let path = this.$route.path;
        let steps = this.$store.state.flowMain.stepList[path];
        let lines = this.$store.state.flowMain.lines[path];
        let connection = this.$store.state.flowMain.connections[path];
        this.dataJson.transformation.order.hop = lines;
        this.dataJson.transformation.step = steps;
        if (connection !== undefined) {
          let ss = this.dataJson.transformation.connection.concat(connection);
          let newConnection = new Set(ss);
          let connections = Array.from(newConnection);
          compareObj(connections);
          this.dataJson.transformation.connection = connections;
        }
        let nodeDatas = this.reverData();
        this.dataJson.transformation.info.name = this.$route.meta.title;
        this.dataJson.transformation.nodeList = nodeDatas;

        let md5Data = this.$store.state.flowMain.projectMd5;
        if (md5Data[path] !== undefined) {
          this.md5 = md5Data[path]
        }

        let params = {
          projectId: this.projectId,
          projectName: null,
          description: null,
          projectStatus: null,
          projectType: null,
          groupId: null,
          groupName: null,
          projectFileVO: {
            projectFile: JSON.stringify(this.dataJson),
            projectVersion: 0,
            md5: this.md5,
          }
        }
        this.contentLoading = true;
        this.lodingContext = "项目保存中"
        updateSelectProject(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.$message({
              message: '保存成功',
              type: 'success',
            })
            let md5Data = {
              key: path,
              md5: data.content,
            }
            this.$store.dispatch('addProjectMd5', md5Data);
          }
          this.contentLoading = false;
        })

      },

      saveAs() {
        this.addTitle = "另存";
        this.editFormVisiable = true;
        this.editForm = {
          projectName: '',
          description: '',
          groupId: '',
          projectStatus: '',
          projectType: '',
        };
      },

      /**
       * 重组数据
       */
      reverData() {
        let datas = JSON.parse(JSON.stringify(this.data));
        datas.nodeList.forEach(item => {
          item.errors = 0;
          item.linesInput = 0;
          item.linesOutput = 0;
          item.linesRead = 0;
          item.linesRejected = 0;
          item.linesUpdated = 0;
          item.linesWritten = 0;
          item.speed = 0;
          item.statusDescription = "";
          item.stepExecutionStatus = "";
          item.stepName = "";
          item.seconds = 0;
          item.projectId = "";
          item.projectName = "";
          item.datasourceId = "";
          item.stepSql = "";
          item.projectError = 0;
          item.dataSourceId = "";
          item.stepMetaExecutionStatus = null;
          item.dataQuerySql = "";
          item.stepExecutionStatu = "";
          item.copy = 0;
          item.priority = "-";
        })
        return datas;
      },

      // 新建项目
      addData() {
        this.$refs['editForm'].validate((valid) => {
          if (!valid) {

          } else {
            this.addProjectLoading = true;
            let path = this.$route.path;
            let steps = this.$store.state.flowMain.stepList[path];
            let lines = this.$store.state.flowMain.lines[path];
            let connection = this.$store.state.flowMain.connections[path];
            this.dataJson.transformation.order.hop = lines;
            this.dataJson.transformation.step = steps;
            this.dataJson.transformation.info.name = this.editForm.projectName;
            if (connection !== undefined) {
              let ss = this.dataJson.transformation.connection.concat(connection);
              let newConnection = new Set(ss);
              let connections = Array.from(newConnection)
              compareObj(connections);
              this.dataJson.transformation.connection = connections;
            }
            this.dataJson.transformation.nodeList = this.data;
            let params = {
              projectName: this.editForm.projectName,
              description: this.editForm.description,
              projectStatus: "0",
              projectType: "JCJB",
              groupId: this.editForm.groupId.split(',')[0],
              groupName: this.editForm.groupId.split(',')[1],
              projectFileVO: {
                projectFile: JSON.stringify(this.dataJson),
                projectVersion: 0,
              }
            };

            addProject(params).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                });
                this.saveFlag = true;
                this.projectName = this.editForm.projectName;
                this.$route.meta.title = this.projectName;
                this.$store.dispatch('addRouteTitle', this.projectName);
                this.$store.dispatch('updateView', this.$route);
                this.projectId = data.content;
                let proejectInfo = {
                  key: this.$route.path,
                  value: {
                    projectId: this.projectId,
                    projectName: this.projectName,
                  }
                };
                this.$store.dispatch('addProjects', proejectInfo);
                this.addProjectLoading = false;
                this.editFormVisiable = false;
                if (this.executeFlag === true) {
                  let _this = this;
                  let ws = this.stompClient.ws;
                  if (this.stompClient === '') {
                    this.initWebSocket();
                  }
                  this.logs = '';
                  console.info("运行时报文", JSON.stringify(this.dataJson));
                  this.isSave = true;
                  this.isExecuted = true;
                  this.isPause = false;
                  this.pauseColor = '#1eaafa';
                  this.acviteColor = '#EAEAEA';
                  //运行项目
                  this.contentLoading = true;
                  this.lodingContext = '连接中';
                  this.operaShow = false;
                  this.timer = setInterval(this.getWsStatus, 1000);
                  this.executeFlag = false;
                }
              } else {
                this.$message({
                  message: data.attachment,
                  type: 'error'
                });
                this.addProjectLoading = false;
              }
            })
          }
        });
      },
      // 下载文件
      download() {
        this.downLoadFileVisiable = true;
        // 默认选中所有字段
        this.$nextTick(() => {
          this.downloadData.forEach(row => {
            this.$refs.downloadTable.toggleRowSelection(row, true);
          })
        })
      },
      closeDownload() {
        this.$refs.downloadTable.clearSelection();
        this.downLoadFileVisiable = false;
      },
      // 开始下载文件
      startDownload() {
        let fields = [];
        let fieldCns = [];
        this.selectColumns.forEach(item => {
          fields.push(item.outName);
          fieldCns.push(item.outNameCn);
        });
        let download = [];
        this.dataPre.forEach(item => {
          let dataSub = [];
          for (let i = 0; i < fields.length; i++) {
            dataSub.push(item[i]);
          }
          download.push(dataSub);
        });
        export_json_to_excel(fieldCns, download, this.stepName);
        this.downLoadFileVisiable = false;
      },

      // 改变连接线的样式
      changeLabel(conn) {
        conn.setLabel({
          label: '',
          // cssClass: 'labelClass a b'
        })
      },


      /**
       * 节点缩放
       */
      ShrinkZoom(data) {
        let scale = 0;
        if (data <= 1) {
          scale = data;
        } else {
          scale = data / 100;
        }
        this.scale = scale;
        this.data.nodeList.forEach(item => {
          $("#" + item.id).css({
            "-webkit-transform": `scale(${scale})`,
            "-moz-transform": `scale(${scale})`,
            "-ms-transform": `scale(${scale})`,
            "-o-transform": `scale(${scale})`,
            "transform": `scale(${scale})`
          })
          // this.jsPlumb.setZoom(scale);
        })

      },

      init() {
        const _this = this;
        this.jsPlumb.ready(function () {
          // 导入默认配置
          _this.jsPlumb.importDefaults(_this.jsplumbSetting);
          // 会使整个jsPlumb立即重绘。
          _this.jsPlumb.setSuspendDrawing(false, true);
          // 初始化节点
          _this.loadEasyFlow();
          // 单点连接线改变线的颜色,设置是否失效
          _this.jsPlumb.bind('click', function (conn, originalEvent) {
            // 获取hop里面的值并修改enabled
            _this.changeLabel(conn);
          });

          // contextmenu 右键连接线
          _this.jsPlumb.bind("contextmenu", function (conn, evt) {
            // 获取节点连接状态显示菜单项
            let line = _this.$store.getters.getLineBystep(_this.$route.path, conn.sourceId, conn.targetId);
            _this.sourceId = conn.sourceId;
            _this.targetId = conn.targetId;
            _this.connEnabled = line.enabled;
            if (line.enabled === 'Y') {
              _this.connecOpera = [{
                index: 0,
                name: '使节点连接失效',
              }, {
                index: 1,
                name: '删除节点连接'
              }]
            } else if (line.enabled === 'N') {
              _this.connecOpera = [{
                index: 0,
                name: '使节点连接生效',
              }, {
                index: 1,
                name: '删除节点连接'
              }]
            }
            _this.connecIndex = 0;
            // 获取鼠标位置显示右键菜单
            let width = document.body.scrollWidth;
            let p = document.getElementById("connec");
            p.style.position = "absolute";
            _this.divLeft = event.clientX - (width * 0.2);
            _this.divTop = event.clientY - 140;
            p.style.left = event.clientX - (width * 0.2) + 'px';
            p.style.top = event.clientY - 140 + 'px';
            _this.connecOperaShow = true;
          });

          // 连接线样式
          _this.jsPlumb.registerConnectionTypes({
            "basic": {
              paintStyle: {
                stroke: "#777777a6",
                strokeWidth: 1.5
              },
            },
            'error': {
              paintStyle: {
                stroke: "#fd6d67",
                strokeWidth: 1.5
              },
              HoverPaintStyle: {
                stroke: '#fd6d67',
                strokeWidth: 4
              }
            },
            "selected": {
              paintStyle: {
                stroke: "#409eff",
                strokeWidth: 1.5
              },
              HoverPaintStyle: {
                stroke: '#409eff',
                strokeWidth: 4
              }
            },
          });

          // 双击连接线（删除）,
          _this.jsPlumb.bind('dblclick', function (conn, originalEvent) {
            _this.$confirm('确定删除所点击的线吗?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              _this.changeFlag = true;
              _this.jsPlumb.deleteConnection(conn)
            }).catch(() => {
            })
          });

          // 连线
          _this.jsPlumb.bind("connection", function (evt) {
            console.log('connection', evt);
            _this.changeFlag = true;
            let data1 = this.dataJson;
            let from = evt.sourceId;
            let to = evt.targetId;
            _this.sourceId = evt.sourceId;
            _this.targetId = evt.targetId;
            // 提交id和label映射对象
            let fromLabel = _this.$store.getters.getMappingData(from);
            let toLable = _this.$store.getters.getMappingData(to);
            _this.dataJson.transformation.info.created_date = _this.nowDate;
            let lineParam = {
              fromId: from,
              toId: to,
              from: fromLabel,
              to: toLable,
              enabled: 'Y',
            };
            let li = Object.assign({}, lineParam);
            let line = {
              key: _this.$route.path,
              lines: li,
            };
            _this.$store.dispatch('addLines', line);
            let paramData = {
              key: _this.$route.path,
              value: fromLabel
            };
            let currnetStep = _this.$store.getters.getCurrentStep(paramData);
            if (from.indexOf("FilterRows") >= 0) {
              // 获取过滤插件判断是否连结果其他插件
              let trueLabel = currnetStep.send_true_to;
              let falseLabel = currnetStep.send_false_to;
              if (trueLabel === undefined && falseLabel === undefined) {
                _this.getMousePos(event, fromLabel, toLable, 'fielter');
              } else if (trueLabel !== undefined && falseLabel === undefined) {
                _this.toLabel = toLable;
                _this.fromLabel = fromLabel;
                _this.unFit();
              } else if (trueLabel === undefined && falseLabel !== undefined) {
                _this.fromLabel = fromLabel;
                _this.toLabel = toLable;
                _this.fit();
              }
            } else {
              let flowData = _this.$store.state.flowMain.flowData[_this.$route.path].nodeList;
              let flag = false;
              flowData.forEach(item => {
                if (item.id === from && item.pluginOutput === 'Y') {
                  flag = true;
                }
              });

              //主输出步骤和错误处理输出步骤
              if (flag === true || (currnetStep.type === 'Unique' && currnetStep.reject_duplicate_row === 'Y')) {
                _this.fromLabel = fromLabel;
                _this.inputStepName = fromLabel;
                _this.getMousePos1(event, fromLabel, toLable, 'fielter1');
              } else {
                //分发或复制
                let path = _this.$route.path;
                let lineList = _this.$store.state.flowMain.lines[path];
                let count = 0;
                if (lineList !== undefined) {
                  lineList.forEach(item => {
                    if (item.fromId === evt.sourceId) {
                      count = count + 1;
                    }
                  });
                  if (count === 2) {
                    _this.fromLabel = fromLabel;
                    _this.inputStepName = fromLabel;
                    _this.inputCopy = true;
                  } else if (count > 2) {
                    _this.distribution(currnetStep.distribute);
                  }
                }
              }

            }


            _this.saveDataJson();
            if (_this.loadEasyFlowFinish) {
              _this.data.lineList.push({
                from: from,
                to: to,
                // label: '连线名称',
                id: _this.getUUID(),
                Remark: '',
              });
            }
          });

          // 删除连线
          _this.jsPlumb.bind("connectionDetached", function (evt) {
            console.log('connectionDetached', evt);
            _this.deleteLine(evt.sourceId, evt.targetId)
          });

          // 改变线的连接节点
          _this.jsPlumb.bind("connectionMoved", function (evt) {
            console.log('connectionMoved', evt);
            _this.changeLine(evt.originalSourceId, evt.originalTargetId)
          });

          // beforeDrop
          _this.jsPlumb.bind("beforeDrop", function (evt) {
            console.log('beforeDrop', evt);
            let from = evt.sourceId;
            let to = evt.targetId;
            if (from === to) {
              _this.$message.error('不能连接自己');
              return false
            }
            if (_this.hasLine(from, to)) {
              _this.$message.error('不能重复连线');
              return false
            }
            if (_this.hashOppositeLine(from, to)) {
              _this.$message.error('不能回环');
              return false
            }

            return true
          });

          // beforeDetach
          _this.jsPlumb.bind("beforeDetach", function (evt) {
            console.log('beforeDetach', evt)
          })
        })

      },

      // 判断过滤插件连接
      checkFielter(from) {
        let j = 0;
        for (let i = 0; i < this.data.lineList.length; i++) {
          if (this.data.lineList[i].from === from) {
            j = j + 1;
          }
        }
        if (from.indexOf("FilterRows") >= 0) {
          if (j >= 2) {
            this.$message.error("过滤只允许拖入两条线");
            return true;
          }
        }
      },

      // 输入控件只能作为源控件，输出控件只能作为目标控件
      checkModel(from, to) {
        if (from === '') {
          this.$message.error("模型类控件只能不能被其他控件连接");
          return true;
        }
        if (to === '') {
          this.$message.error("输出类控件不能连接其他控件");
          return true;
        }
        return false;
      },

      // 判断是否被其他线链接
      judgeLine(from, to) {
        // 根据插件id获取插件类型
        let fromType = this.$store.getters.getPluginMap(from);
        let toType = this.$store.getters.getPluginMap(to);
        // 获取当前步骤作为被连接的线信息
        let j = 0;
        for (let i = 0; i < this.data.lineList.length; i++) {
          if (this.data.lineList[i].to === to) {
            j = j + 1;
          }
        }
        if (to.indexOf('Contain') >= 0 || to.indexOf('bcMergeJoin') >= 0 || to.indexOf("Union") >= 0) {
          if (j >= 2) {
            this.$message.error("只允许拖入两条线");
            return true;
          }
        } else {
          if (j >= 1) {
            this.$message.error("只允许拖入一条线");
            return true;
          } else {
            //判断插件类型
            if (fromType === 'output') {
              //输出控件不能拖出线，只能被连接
              this.$message.error("输出控件只能被连接");
              return true;
            } else if (toType === 'input') {
              this.$message.error("输入控件不能被其他控件连接");
              return true;
            } else {
              return false;
            }
          }
        }
        return false;
      },
      // 加载流程图
      loadEasyFlow() {
        let filterNode = null;
        let errorFlag = true;
        // 初始化节点
        for (let i = 0; i < this.data.nodeList.length; i++) {
          let node = this.data.nodeList[i];
          // 设置源点，可以拖出线连接其他节点
          this.jsPlumb.makeSource(node.id, this.jsplumbSourceOptions);
          // // 设置目标点，其他源点拖出的线可以连接该节点
          this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions);
          this.jsPlumb.draggable(node.id, {
            containment: 'parent',
            grid: [5, 5]
          })
        }
        //画出错误步骤的连线
        if (this.dataJson.transformation.step_error_handling.error.length > 0 && errorFlag === true) {
          this.dataJson.transformation.step_error_handling.error.forEach(item => {
            let fromId = this.$store.getters.getMappingkey(item.source_step);
            let toId = this.$store.getters.getMappingkey(item.target_step);
            let connect = this.jsPlumb.connect({
              source: fromId,
              target: toId,
              paintStyle: {
                stroke: "#fd6d67",
                strokeWidth: 1.5
              },
              HoverPaintStyle: {
                stroke: '#fd6d67',
                strokeWidth: 4
              },
              overlays: [
                ["Label", {
                  label: "",
                  location: 0.25,
                  cssClass: 'labelClass2'
                }]
              ]
            });
            connect.setHoverPaintStyle({
              stroke: '#fd6d67',
              strokeWidth: 4
            });
          });
          errorFlag = false;
        }
        //判断当前节点是否是过滤控件
        for (let i = 0; i < this.data.lineList.length; i++) {
          let flag = true;
          let line = this.data.lineList[i];
          let curNode = null;
          let nodeData = null;
          this.data.nodeList.forEach((item) => {
            if (item.id === line.from && item.pluginId === 'FilterRows') {
              curNode = item;
              flag = false;
            } else if (line.from === item.id) {
              nodeData = item;
            }
          });

          // 判断插件类型如果是过滤插件就不画线
          if (flag) {
            // 连接线的颜色
            let stroke = '#409eff';
            let curLine = this.$store.getters.getLineBystep(this.$route.path, line.from, line.to);
            // 判断是否使失效线
            if (curLine.enabled === 'N') {
              stroke = "#777777a6";
            }
            // 分发复制的线
            let path = this.$route.path;
            let lineList = this.$store.state.flowMain.lines[path];
            let distribute = [];
            lineList.forEach(item1 => {
              if (item1.fromId === nodeData.id) {
                distribute.push(item1)
              }
            });
            if (distribute.length > 1) {
              let css = '';
              let stepParam = {
                key: this.$route.path,
                value: nodeData.label
              };
              let curStep1 = this.$store.getters.getCurrentStep(stepParam);
              if (curStep1.distribute === 'Y') {
                css = '';
              } else {
                css = 'dis_tribution'
              }
              if (this.dataJson.transformation.step_error_handling.error.length > 0) {
                this.dataJson.transformation.step_error_handling.error.forEach(item => {
                  let fromId = this.$store.getters.getMappingkey(item.source_step);
                  let toId = this.$store.getters.getMappingkey(item.target_step);
                  if (fromId === line.from && toId === line.to) {

                  } else {
                    let connection = this.jsPlumb.connect({
                      source: line.from,
                      target: line.to,
                      paintStyle: {
                        stroke: stroke,
                        strokeWidth: 1.5,
                      },

                      overlays: [
                        ["Label", {
                          label: "",
                          location: 0.25,
                          cssClass: css
                        }]
                      ]
                    });
                  }
                })
              } else {
                let connection = this.jsPlumb.connect({
                  source: line.from,
                  target: line.to,
                  paintStyle: {
                    stroke: stroke,
                    strokeWidth: 1.5,
                  },
                  overlays: [
                    ["Label", {
                      label: "",
                      location: 0.25,
                      cssClass: css
                    }]
                  ]
                });
              }

            } else {
              if (this.dataJson.transformation.step_error_handling.error.length > 0) {
                this.dataJson.transformation.step_error_handling.error.forEach(item => {
                  let fromId = this.$store.getters.getMappingkey(item.source_step);
                  let toId = this.$store.getters.getMappingkey(item.target_step);
                  if (fromId === line.from && toId === line.to) {

                  } else {
                    let connection = this.jsPlumb.connect({
                      source: line.from,
                      target: line.to,
                      paintStyle: {
                        stroke: stroke,
                        strokeWidth: 1.5,
                      },
                    });
                  }
                })
              } else {
                let connection = this.jsPlumb.connect({
                  source: line.from,
                  target: line.to,
                  paintStyle: {
                    stroke: stroke,
                    strokeWidth: 1.5,
                  },
                });
              }


            }

          } else {

            // 过滤插件绘制线
            let currentData = {
              key: this.$route.path,
              value: curNode.label,
            };
            // 获取过滤插件步骤信息
            let sourceNode = this.$store.getters.getCurrentStep(currentData);
            let sendTrueLabel = sourceNode.send_true_to;
            let sendFalseLabel = sourceNode.send_false_to;
            if (sendTrueLabel !== undefined && line.to === this.$store.getters.getMappingkey(sendTrueLabel)) {
              let curLine = this.$store.getters.getLineBystep(this.$route.path, line.from, line.to);
              let stroke = '#409eff';
              if (curLine.enabled === 'N') {
                stroke = "#777777a6";
              }
              this.drawTrueLine(curNode, sendTrueLabel, 'labelClass', stroke);
            }
            if (sendFalseLabel !== undefined && line.to === this.$store.getters.getMappingkey(sendFalseLabel)) {
              let curLine = this.$store.getters.getLineBystep(this.$route.path, line.from, line.to);
              let stroke = '#409eff';
              if (curLine.enabled === 'N') {
                stroke = "#777777a6";
              }
              this.drawTrueLine(curNode, sendFalseLabel, 'labelClass1', stroke);
            }
          }
        }
        this.$nextTick(function () {
          this.loadEasyFlowFinish = true
        })
      },

      // 过滤插件符合连接
      /**
       * @param {Object} node 节点信息
       * @param {Object} sendTrueLabel 目标节点名称
       * @param {Object} css  连接线图标
       * @param {Object} stroke 连接线颜色
       */
      drawTrueLine(node, sendTrueLabel, css, stroke) {
        let params = {
          key: this.$route.path,
          value: sendTrueLabel
        };
        let sendTrueStep = this.$store.getters.getCurrentStep(params);
        let toId = this.$store.getters.getMappingkey(sendTrueLabel);
        let connection = this.jsPlumb.connect({
          source: node.id,
          target: toId,
          paintStyle: {
            stroke: stroke,
            strokeWidth: 1.5
          },
          overlays: [
            ["Label", {
              label: "",
              location: 0.25,
              cssClass: css
            }]
          ]
        });
      },

      // 过滤插件不符合连接
      drawFalseLine(node, sendTrueLabel) {
        let params = {
          key: this.$route.path,
          value: sendTrueLabel
        };
        let sendTrueStep = this.$store.getters.getCurrentStep(params);
        let toId = this.$store.getters.getMappingkey(sendTrueLabel);
        let connection = this.jsPlumb.connect({
          source: node.id,
          target: toId,
          overlays: [
            ["Label", {
              label: "",
              location: 0.25,
              cssClass: "labelClass1"
            }]
          ]
        });
      },


      // 添加新的节点
      addNode(temp) {
        this.data.nodeList.push(temp);
        this.setCashFlowData();
        this.saveDataJson();
        this.$nextTick(function () {
          this.ShrinkZoom(this.scale);
          this.jsPlumb.makeSource(temp.id, this.jsplumbSourceOptions);
          this.jsPlumb.makeTarget(temp.id, this.jsplumbTargetOptions);
          this.jsPlumb.draggable(temp.id, {
            containment: 'parent',
            grid: [5, 5]
          })
        })
      },
      // 删除线
      deleteLine(from, to) {
        let params = {
          key: this.$route.path,
          value: {
            fromId: from,
            toId: to,
          }
        };

        let lines = this.data.lineList;
        let newLines = [];
        for (let i = 0; i < lines.length; i++) {
          if (lines[i].from === from && lines[i].to === to) {

          } else {
            newLines.push(lines[i]);
          }
        }
        this.data.lineList = newLines;
        // this.data.lineList = this.data.lineList.filter(function(line) {
        //   return line.from !== from && line.to !== to
        // })
        let formLabel = this.$store.getters.getMappingData(from);
        let toLabel = this.$store.getters.getMappingData(to);
        this.dataJson.transformation.step_error_handling.error.forEach((item, index) => {
          if (formLabel === item.source_step && toLabel === item.target_step) {
            this.dataJson.transformation.step_error_handling.error.splice(index, 1);
          }
        });
        // 删除连线更新krt数据
        this.$store.dispatch('deleteFlowLine', params);
        this.setCashFlowData();
        let key = this.$route.path;
        this.dataJson = this.$store.state.flowMain.flowDataJson[key];

        // this.saveDataJson();

      },
      // 改变连线
      changeLine(oldFrom, oldTo) {
        this.deleteLine(oldFrom, oldTo)
      },
      // 选中节点获取节点数据预览
      selectNode(node) {
        this.dataPre = [];
        this.dataColumn = [];
        this.fileName = node.label;
        this.operaData = [{
          index: 0,
          label: '日志',
          icon: 'iconfont icon-rizhi'
        }, {
          index: 1,
          label: '步骤度量',
          icon: 'iconfont icon-duliang'
        }, {
          index: 2,
          label: '数据预览',
          icon: 'iconfont icon-shujuyulan-copy'
        }, {
          index: 3,
          label: '字段信息',
          icon: 'iconfont icon-ziduanliebiao'
        }];
        localStorage.setItem("operaData", JSON.stringify(this.operaData));
        this.clickNode = node.id;
        // 获取步骤名称
        this.stepName = node.label;
        this.stepId = node.modelName;
        // 获取步骤信息
        this.labelName = "【" + node.label + "】";
        if (node.linesInput !== 0) {
          this.executeCount = node.linesInput;
        } else if (node.linesInput === 0 && node.linesRead !== 0) {
          this.executeCount = node.linesRead;
        }

        let stepkey = this.$route.path + '-' + node.label;
        let preData = this.$store.state.flowMain.preViewData[stepkey];
        this.dataPre = [];
        this.dataColumn = [];
        if (node.errors === 0 && node.stepExecutionStatus === "STATUS_FINISHED") {
          this.downloadData = [];
          if (preData === undefined) {
            this.dataColumn = [];
            this.dataPre = [];
          } else {
            preData.fields.forEach(item => {
              this.downloadData.push({
                outName: item,
                outNameCn: item,
                updateFlag: false,
              })
            });
            this.dataColumn = preData.fields;
            this.dataPre = preData.rows;
          }

          this.analyzeFlag = true;
          let columnTableData = [];
          this.buttonDis = false;
          // 获取字段信息
          let param = {
            key: this.$route.path,
            value: node.label,
          };
          // 获取当前步骤信息
          let columnData = this.$store.getters.getCurrentStep(param);
          if (columnData.outFields !== undefined) {
            let outColumns = columnData.outFields;
            let dataColumns = [];
            // 获取输出字段
            if (outColumns.length > 0) {
              outColumns.forEach((item, index) => {
                if (item.name !== '') {
                  dataColumns.push({
                    outName: item.name,
                    outNameCn: item.nameCn === undefined ? item.name : item.nameCn,
                    outType: item.type,
                    fileLength: item.length,
                    description: item.description,
                  });
                }
                columnTableData[index] = {
                  outName: item.name,
                  outNameCn: item.nameCn === undefined ? item.name : item.nameCn,
                  outType: item.type,
                  fileLength: item.length,
                  description: item.description,
                  inName: '',
                  inNameCn: '',
                  inType: '',
                  updateFlag: false,
                };
              });
              // this.downloadData = dataColumns;
              this.dataPreColumn = dataColumns;
              // 字段统计表格数据赋值
              this.columnData = dataColumns;
              this.columnMessage = dataColumns;
              //获取上一步步骤输出字段
              let lastStepData = this.$store.getters.getLastStep(param);

              //获取生成输出字段
              let outStepData = this.$store.getters.generateOutputFields(param);

              //获取输入字段
              if (outStepData.length > 0) {
                if (outStepData !== undefined) {
                  let inColumns = outStepData;
                  inColumns.forEach((item, index) => {
                    if (columnTableData[index] !== undefined) {
                      columnTableData[index] = {
                        outName: columnTableData[index].outName,
                        outNameCn: columnTableData[index].outNameCn,
                        outType: columnTableData[index].outType,
                        fileLength: columnTableData[index].fileLength,
                        description: columnTableData[index].fileLength,
                        inName: item.name,
                        inNameCn: item.nameCn,
                        inType: item.type,
                      };
                    } else {
                      columnTableData[index] = {
                        inName: item.name,
                        inNameCn: item.nameCn === undefined ? item.name : item.nameCn,
                        inType: item.type,
                      }
                    }
                  })
                }
              }
              this.columnDatas = columnTableData;
            }
          }

        }
      },
      // 改变节点的位置
      changeNodeSite(data) {
        for (let i = 0; i < this.data.nodeList.length; i++) {
          let node = this.data.nodeList[i];
          if (node.id === data.nodeId) {
            node.left = data.left;
            node.top = data.top
          }
        }
        this.setCashFlowData();
      },
      // 修改节点信息
      updateNode(params) {
        this.data.nodeList.forEach((item, index) => {
          if (item.id === params.id) {
            item.label = params.label;
          }
        });
        this.changeFlag = true;
        // 获取上一步步骤信息 判断是否是过滤插件 如果是过滤插件判断当前插件是过滤插件的符合还是不符合并修改过滤插件信息
        let parameter = {
          key: this.$route.path,
          value: params.oldStepName,
        };
        let lastStep = this.$store.getters.getLastStep(parameter);
        //修改错误输出步骤信息
        if (this.dataJson.transformation.step_error_handling.error.length > 0) {
          this.dataJson.transformation.step_error_handling.error.forEach(item => {
            if (item.source_step === params.oldName) {
              item.source_step = params.label;
            } else if (item.target_step === params.oldName) {
              item.target_step = params.label;
            }
          })
        }
        if (lastStep.length > 0 && params.oldName !== params.label) {
          let steps = {};
          lastStep.forEach((item, index) => {
            if (item.type === 'FilterRows') {
              if (item.send_true_to === params.oldName) {
                item.send_true_to = params.label;
                steps = item;
              } else if (item.send_false_to === params.oldName) {
                item.send_false_to = params.label;
                steps = item;
              }
              // 修改步骤信息
              let updateParams = {
                key: this.$route.path,
                value: {
                  oldStepName: params.oldName,
                  step: steps,
                }
              };
              this.$store.dispatch('updateStepInfo', updateParams);
            }
          })
        }
        // 修改节点后需要修改krt文件中线的信息和更新连接线id和label
        let param = {
          key: this.$route.path,
          value: {
            nodeId: params.id,
            nodeLabel: params.label,
          }
        };
        this.$store.dispatch('updateFlowNodeLine', param);
        let map = {
          key: params.id,
          value: params.label,
        };
        this.$store.dispatch('setMap', JSON.stringify(map));
        // 修改线里面的步骤名称
        let key = this.$route.path;
        this.dataJson = this.$store.state.flowMain.flowDataJson[key];
        this.setCashFlowData();
      },
      //删除节点
      deleteNode(nodeId) {
        this.$confirm('确定要删除节点' + nodeId + '?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          closeOnClickModal: false
        }).then(() => {
          this.changeFlag = true;
          let name = '';
          let id = '';
          this.data.nodeList.forEach((item, index) => {
            if (item.label === nodeId) {
              name = item.label;
              this.data.nodeList.splice(index, 1);
              id = item.id;
            }
          });
          let paramsData = {
            key: this.$route.path,
            value: name,
          };
          //判断是否要删除错误步骤信息
          if (this.dataJson.transformation.step_error_handling.error.length > 0) {
            this.dataJson.transformation.step_error_handling.error.forEach((item, index) => {
              if (item.source_step === name || item.target_step === name) {
                this.dataJson.transformation.step_error_handling.error.splice(index, 1);
              }
            })
          }

          // 获取该节点上一步步骤信息
          let lastSteps = this.$store.getters.getLastStep(paramsData);
          if (lastSteps !== undefined) {
            for (let i = 0; i < lastSteps.length; i++) {
              if (lastSteps[i].type === 'FilterRows') {
                let stepInfo = lastSteps[i];
                if (stepInfo.send_true_to === name) {
                  stepInfo.send_true_to = undefined;
                } else {
                  stepInfo.send_false_to = undefined;
                }
                let updateParam = {
                  key: this.$route.path,
                  value: {
                    oldStepName: name,
                    step: stepInfo,
                  }
                };
                // 修改步骤信息
                this.$store.dispatch('updateStepInfo', updateParam);
              }
            }
          }
          let params = {
            key: this.$route.path,
            value: name,
          };
          this.$store.dispatch('deleteFlowNodeLine', params);

          // 删除节点数据后
          // this.dataJson = JSON.parse(this.$store.getters.getNodes);
          //删除对应的线信息


          // 删除节点数据
          this.$nextTick(function () {
            this.jsPlumb.removeAllEndpoints(id);
          });
          this.setCashFlowData();
          this.saveDataJson();

        }).catch(() => {
        });
        return true
      },

      //删除线
      delLine(conn) {
        this.$confirm('确定删除所点击的线吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.changeFlag = true;
          this.jsPlumb.deleteConnection(conn)
        }).catch(() => {
        })
      },
      // 是否具有该线
      hasLine(from, to) {
        for (let i = 0; i < this.data.lineList.length; i++) {
          let line = this.data.lineList[i];
          if (line.from === from && line.to === to) {
            return true
          }
        }
        return false
      },
      // 是否含有相反的线
      hashOppositeLine(from, to) {
        return this.hasLine(to, from)
      },
      lineLabelSave(line) {
        this.currentConnect.getOverlay("label-1").setLabel(line.label);
      },

      /**
       * 检索节点名称是否存在
       * @param {Object} nodeName节点名称
       */
      renameNode(nodeName, newName, i) {
        let val = this.data.nodeList.find((item) => {
          return item.label === newName;
        });
        if (val !== undefined) {
          i++;
          let name = nodeName + i;
          this.renameNode(nodeName, name, i);
        } else {
          this.dropNodeName = newName;
        }
        // return newName;
      },

      // 拖出节点并赋值
      drop(event) {
        this.changeFlag = true;
        this.currentItem = JSON.parse(this.$store.getters.getCurrentItem);
        //event.preventDefault();
        // 判断是否节点是否已经存在
        let id = '';
        let label = '';

        this.renameNode(this.currentItem.label, this.currentItem.label, 0);
        if (this.data.nodeList.length > 0) {
          let i = 0;
          for (let j = 0; j < this.data.nodeList.length; j++) {
            let item = this.data.nodeList[j];
            let label1 = this.currentItem.label;
            let strLenght = label1.length;
            let str = item.label.substr(0, strLenght);
            if (item.label.substr(0, label1.length) === this.currentItem.label) {
              let num = item.label.substr(label1.length, item.length);
              if (num === "") {
                num = '0';
              }
              i = parseInt(num) + 1;

            }
          }
          if (i === 0) {
            id = this.currentItem.id;
            label = this.currentItem.label;
          } else {
            id = this.currentItem.id + '' + i;
            label = this.currentItem.label + '' + i;
          }

        } else {
          id = this.currentItem.id;
          label = this.currentItem.label;
        }

        let finalId = id + '' + this.getUUID();
        // 更新映射值
        let map = {
          key: finalId,
          value: this.dropNodeName,
        };

        this.$store.dispatch('setMap', JSON.stringify(map));
        let _obj = this.$refs.flowContent;
        let temp = {
          id: finalId,
          label: this.dropNodeName,
          top: event.offsetY + 'px',
          left: event.offsetX + 'px',
          Type: this.currentItem.type,
          icon: this.currentItem.pluginImage,
          modelName: this.currentItem.modelName,
          modelType: this.currentItem.modelType,
          pluginId: this.currentItem.pluginId,
          seconPluginType: this.currentItem.secondPluginType,
          status: true, //状态,
          errors: '',
          linesInput: '',
          linesOutput: '',
          linesRead: '',
          linesRejected: '',
          linesUpdated: '',
          linesWritten: '',
          speed: '',
          statusDescription: '',
          stepExecutionStatus: '',
          stepName: '',
          seconds: '',
          pluginOutput: this.currentItem.pluginOutput,
        };
        this.addNode(temp);
        // pluginId和控件类型映射集合
        let pluginMap = {
          pluginId: finalId,
          pluginType: this.currentItem.secondPluginType,
        };
        this.$store.dispatch('addPluginMap', pluginMap);
        this.getModelData(temp);
      },

      // 初始化控件参数
      getModelData(datas) {
        this.initStep();
        this.step.name = datas.label;
        this.step.type = datas.pluginId;
        // 查询模型信息
        this.lodingContext = "正在初始化节点信息";
        this.contentLoading = true;
        if (datas.modelType !== undefined) {
          let requestParam = {
            modelName: datas.modelName,
            modelType: datas.modelType
          };
          getModelInfo(requestParam).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              this.step.modelName = data.content.name;
              this.step.description = data.content.description;
              this.step.sql = data.content.querySql;
              this.step.model_name = datas.pluginName;
              this.step.connection = data.content.datasourceName;
              this.step.type = datas.pluginId;
              this.step.initFlag = true;
              let basicModelMetaDataDTOS = data.content.modelMetaDataDTOS;
              for (let i = 0; i < basicModelMetaDataDTOS.length; i++) {
                this.step.fields.field.push({
                  name: basicModelMetaDataDTOS[i].columnName,
                  nameCn: basicModelMetaDataDTOS[i].columnChineseName,
                  type: basicModelMetaDataDTOS[i].columnType,
                  format: '',
                  currency: "",
                  decimal: ".",
                  group: "",
                  nullif: "",
                  trim_type: "both",
                  length: basicModelMetaDataDTOS[i].columnLength,
                  precision: basicModelMetaDataDTOS[i].columnPrecision,
                  description: basicModelMetaDataDTOS[i].columnDescription === undefined ? '' : basicModelMetaDataDTOS[
                    i].columnDescription,
                })
              }

              let data1 = Object.assign({}, this.step);
              this.stepList.push(data1);
              let parameter = {
                key: this.$route.path,
                step: data1,
              };
              this.$store.dispatch("addSteps", parameter);
              this.contentLoading = false;
            }
          })
        } else {
          this.step.initFlag = true;
          let data1 = Object.assign({}, this.step);
          let param = {
            key: this.$route.path,
            step: data1,
          };
          this.$store.dispatch("addSteps", param);
          this.contentLoading = false;
        }
      },
      // 初始化步骤数据
      initStep() {
        this.step = {
          name: null,
          type: "",
          modelName: "",
          description: "",
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          isBusiness: "Y",
          connection: "",
          sql: '',
          limit: 0,
          lookup: "",
          execute_each_row: "N",
          variables_active: "N",
          lazy_conversion_active: "N",
          model_name: "",
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 176,
            yloc: 160,
            draw: "Y"
          },
          fields: {
            field: []
          }
        };
      },

      allowDrop(event) {
        event.preventDefault()
      },
      getUUID() {
        let s = [];
        let hexDigits = "0123456789abcdefhijklmnopqrstuzwxyz";
        for (let i = 0; i < 12; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        return s.join("");
      },

      // 将页面数据塞入缓存，根据路由不通取不通缓存

      setCashFlowData() {
        let data = {
          key: this.$route.path,
          value: this.data
        };
        this.$store.dispatch('addFlowData', data)
      },
      saveDataJson() {
        let params = {
          key: this.$route.path,
          value: this.dataJson,
        };
        this.$store.dispatch('addDataJson', params);
      },


      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
      }

    }
  }
</script>

<style>

  .flow-detail {
    height: calc(100% - 40px);
    height: -moz-calc(100% - 40px);
    height: -webkit-calc(100% - 40px);
    /* border: 1px solid #dcdcdc; */
    word-break: keep-all;
    position: relative;
    overflow: auto;
    width: 100%;
  }

  .flow-node {
    height: 100%;
    width: 100%;
    position: absolute;
  }

  #flowContent {
    float: left;
    width: 100%;
    height: 100%;
    position: absolute;
    z-index: 3;
    overflow: auto;
  }

  .csslabel {
    font-weight: 200;
    z-index: 10;
    font-size: 10px;
    color: #21c56ad9;
    background: #fff;
  }

  .csslabel .label-text {
    background: white;
  }

  .savebtn {
    position: absolute;
    top: 5px;
    right: 5px;
  }

  .log_class {
    width: 100%;
    height: 50%;
    overflow: hidden;
    position: absolute;
    outline: none;
    z-index: 1;
    bottom: 0px;
    background: #FFFFFF;
    border: 1px solid #D8DCE5;
    border-radius: 5px;
  }

  .log_class1 {
    width: 100%;
    height: 50%;
    overflow: hidden;
    position: absolute;
    outline: none;
    z-index: 1;
    bottom: 0px;
    background: #FFFFFF;
    border: 1px solid #D8DCE5;
    border-radius: 5px;
  }

  .logShow {
    width: 100%;
    height: 100%;
    overflow: auto;
    bottom: 0;
    padding: 0 20px;
  }

  .logs {
    width: 100%;
    height: calc(100% - 36px);
    height: -moz-calc(100% - 36px);
    height: -webkit-calc(100% - 36px);
    overflow: auto;
    bottom: 1px;
  }

  .logs1 {
    width: 100%;
    height: 100%;
    overflow: auto;
    bottom: 1px;
  }

  .pane_header {
    width: 100%;
    height: 34px;
    /* border: 1px solid #B4BCCC; */
    border-bottom: 1px solid #B4BCCC;
    /* border-radius: 5px; */
    line-height: 34px;
  }

  .log_class .el-tabs--border-card > .el-tabs__content {
    padding: 0px;
  }

  .pane_header_label {
    display: inline-block;
    position: absolute;
  }

  .pane_header_button {
    text-align: right;
    float: right;
    display: inline-block;
    margin-right: 10px;
  }

  .log_class .el-tabs__content {
    position: relative;
    height: 320px;
    word-break: break-all;
    overflow: auto;
  }

  .log_class .el-tabs--border-card > .el-tabs__content {
    padding: 0px;
  }

  .dialog_body {
    width: 100%;
    height: 330px;
  }

  .dialog_left {
    width: 40%;
    display: inline-block;
    height: 300px;
  }

  .dialog_right {
    display: inline-block;
    float: right;
    width: 40%;
    height: 300px;
  }


  .headTools1 .iconfont {
    margin-left: 1px;
    color: #1eaafa;
    font-size: 20px;
  }

  .el-button + .el-button {
    margin-left: 0px;
  }

  .execute .el-button--medium {
    font-size: 14px;
    border-radius: 4px;
  }

  .exeucted {
    display: inline-block;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    background: #FFF;
    color: #606266;
    -webkit-appearance: none;
    text-align: center;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    outline: 0;
    margin: 0;
    -webkit-transition: .1s;
    transition: .1s;
    padding: 0 0;
    font-size: 14px;
    border-radius: 4px;
    width: 30px;
    height: 36px;
  }

  #middle {
    width: 100%;
    cursor: n-resize;
    min-height: 30px;
    position: relative;
  }

  #middle:hover {
    width: 100%;
    cursor: n-resize;
    min-height: 30px;
    position: relative;
  }

  .fielter {
    z-index: 200;
    width: 180px;
    height: 40px;
    background: #FFFFFF;
    border: 0.0625rem solid #D3D3D3;
    border-radius: 5px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .fielter li {
    list-style-type: none
  }

  .fielter li:active {
    color: #000000;
    background: #66B1FF;
  }

  .header_tab {
    display: inline-block;
    height: 35px;
    width: 25%;
    line-height: 35px;
    margin: 0 auto;
    text-align: center;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .header_tab_ative {
    display: inline-block;
    background: #FFFFFF;
    height: 35px;
    width: 25%;
    line-height: 35px;
    margin: 0 auto;
    text-align: center;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .drag_class {
    text-align: center;
    width: 40px;
    position: absolute;
    margin: auto;
    top: 0;
    left: 0;
    right: 0;
    cursor: n-resize;
  }

  .drag_class:hover {
    text-align: center;
    width: 40px;
    position: absolute;
    margin: auto;
    top: 0;
    left: 0;
    right: 0;
    cursor: n-resize;
  }

  .content_tab {
    width: 100%;
    height: calc(100% - 40px);
    height: -moz-calc(100% - 40px);
    height: -webkit-calc(100% - 40px);
    position: absolute;
  }


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

  .connec_style {
    z-index: 200;
    padding-top: 5px;
    width: 120px;
    height: 50px;
    border: 1px solid #EAEAEA;
    border-radius: 5px;
    background: #FFFFFF;
    position: absolute;
  }

  .connec_item {
    background: #FFFFFF;
    padding-left: 10px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .connec_item:hover {
    background-color: #FFFFFF;
    padding-left: 10px;
    background: #1EAAFA;
    color: #FFFFFF;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .connec_item_active {
    padding-left: 10px;
    background: #1EAAFA;
    color: #FFFFFF;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .labelClass {
    background: url('../../assets/image/allow.png') no-repeat center;
    background-size: 100% 100%;
    background-size: cover;
    width: 18px;
    height: 18px;
  }

  .dis_tribution {
    background: url('../../assets/image/copy.png') no-repeat center;
    background-size: 100% 100%;
    background-size: cover;
    width: 18px;
    height: 18px;
  }

  .labelClass1 {
    background: url('../../assets/image/reject.png') no-repeat center;
    background-size: 100% 100%;
    background-size: cover;
    width: 18px;
    height: 18px;
  }

  .labelClass2 {
    background: url('../../assets/image/error.png') no-repeat center;
    background-size: 100% 100%;
    background-size: cover;
    width: 18px;
    height: 18px;
  }


  .tableCss {
  }

  .cssPoint {
    border: 1px solid #007AFF;
    background: #1EAAFA;
  }
</style>
