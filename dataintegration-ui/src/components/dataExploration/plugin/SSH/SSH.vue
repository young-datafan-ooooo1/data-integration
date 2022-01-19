<!--运行SSH命令-->
<template>
  <div class="plugin_body">
    <div class="div_form plugin_content plugin_form1">
      <el-form :model="step" ref="stepfrom" label-width="30%" size="mini">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <el-tabs @tab-click="handleClick">
        <!-- 基础属性界面 -->
        <el-tab-pane label="总则">
          <el-form :model="step" ref="step" label-width="30%" size="mini" label-position="right"
                   aria-autocomplete="none">
            <el-form-item label="服务器IP名称" prop="servername">
              <el-input v-model="step.servername" size="mini" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="服务器端口" prop="port">
              <el-input v-model="step.port" size="mini" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="超时" prop="timeOut">
              <el-input v-model="step.timeOut" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="step.userName" auto-complete="new-password" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="step.password" type="password" style="width: 65%" show-password
                        auto-complete="new-password"></el-input>
            </el-form-item>
            <el-form-item label="使用密钥" prop="usePrivateKey">
              <el-checkbox v-model="step.usePrivateKey"></el-checkbox>
            </el-form-item>
            <el-form-item label="密钥" prop="keyFileName">
              <!--              <el-input v-model="step.keyFileName" style="width: 91%" :disabled="!step.usePrivateKey"></el-input>-->
              <!--              <el-button type="primary" size="mini" :disabled="!step.usePrivateKey">浏览</el-button>-->
              <el-input v-model="step.keyFileName" type="textarea" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="口令" prop="passPhrase">
              <el-input type="password" v-model="step.passPhrase" style="width: 65%" auto-complete="new-password"
                        show-password :disabled="!step.usePrivateKey"></el-input>
            </el-form-item>
            <el-form-item label="代理主机" prop="proxyHost">
              <el-input v-model="step.proxyHost" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="代理端口" prop="proxyPort">
              <el-input v-model="step.proxyPort" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="代理用户名" prop="proxyUsername">
              <el-input v-model="step.proxyUsername" auto-complete="new-password" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="代理密码" prop="proxyPassword">
              <el-input type="password" v-model="step.proxyPassword" show-password auto-complete="new-password"
                        style="width: 65%"></el-input>
            </el-form-item>
          </el-form>
          <div class="node_title_basic">
            <span style="float: right">
              <el-button type="primary" size="mini" @click="testConnection">测试连接</el-button>
            </span>
          </div>

        </el-tab-pane>
        <el-tab-pane label="设置">
          <el-form v-model="step" size="mini" label-position="right" label-width="30%">
            <div class="node_title_basic">
              <span>输出</span>
            </div>
            <el-form-item label="响应字段名称" prop="stdOutFieldName">
              <el-input v-model="step.stdOutFieldName" style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="响应域" prop="stdErrFieldName">
              <el-input v-model="step.stdErrFieldName" style="width: 65%"></el-input>
            </el-form-item>
            <div class="node_title_basic">
              <span>命令</span>
            </div>
            <el-form-item label="从字段获取命令" prop="dynamicCommandField">
              <el-checkbox v-model="step.dynamicCommandField" @change="changeCommand"></el-checkbox>
            </el-form-item>
            <el-form-item label="命令字段" prop="commandfieldname">
              <el-select v-model="step.commandfieldname" :disabled="step.dynamicCommandField===false"  style="width: 65%"
                         clearable>
                <el-option v-for="item in step.outFields" :key="item.name" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="命令" prop="command">
              <el-input type="textarea" :autosize="{ minRows: 6, maxRows: 10}" :disabled="step.dynamicCommandField===true"
                        v-model="step.command" style="width: 65%">
              </el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog class="dialog_temp other_dialog" :visible.sync="line" title="选择显示行数" width="30%"
               :append-to-body="true">
      <div style="width: 100%;padding: 10px">
        <span>显示行数</span>
        <span>
          <el-input v-model="limit" style="width: 80%"></el-input>
        </span>

      </div>
      <span slot="footer" class="dialog-footer">
         <el-button @click="line = false" size="mini">取 消</el-button>
         <el-button type="primary" @click="previewDatas" size="mini" :loading="previewLoading">确 定</el-button>
       </span>
    </el-dialog>

    <!--  数据预览展示窗口 -->
    <el-dialog :visible.sync="preDataVisiable" :append-to-body="true" v-alterELDialogMarginTop="{marginTop:'2vh'}"
               :modal-append-to-body="true" class="abow_in_dialog" width="70%" title="数据预览">
      <pre-view-data :data-pre="dataPre" :data-column="dataColumn" :key="previewKey"></pre-view-data>
      <span slot="footer" class="dialog-footer">
         <el-button @click="preDataVisiable=false" size="mini">关闭</el-button>
         <el-button type="primary" @click="showLog" size="mini">查看日志</el-button>
       </span>
    </el-dialog>
    <el-dialog :visible.sync="logVisiable" title="日志" :append-to-body="true" width="70%"
               v-alterELDialogMarginTop="{marginTop:'2vh'}" :modal-append-to-body="true"
               class="abow_in_dialog">
      <Log :key="logKey" :logs="log"></Log>
    </el-dialog>


    <div class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button size="mini"  @click="line=true" :disabled="step.dynamicCommandField===true">预览</el-button>
      <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
    </div>
  </div>

</template>

<script>
  import {initStep} from "../../../../common/PluginTool";
  import util from "../../../../common/utils";
  import {executePreviewByFile, testSSHConnect} from '../../../../api/api'
  import store from "../../../../vuex/store";
  import {spliceDataJson} from "../../../../common/common";
  import Log from "../../../flow/Log";
  import PreViewData from "../../../common/PreViewData";

  export default {
    name: "SSH.vue",
    data() {
      return {
        logKey:null,
        log:'',
        previewKey:null,
        preDataVisiable:false,
        logVisiable:false,
        previewLoading:false,
        line:false,
        limit:50,
        dataColumn: [],
        dataPre: [],
        key: '',
        nodeData: '',
        fields: [],
        oldStepName: '',

        step: {
          outFields: [],
          name: "运行SSH命令",
          type: "SSH2",
          distribute: "Y",
          copies: "1",
          partitioning: {"method": "none"},
          dynamicCommandField: false,
          command: "",
          port: "22",
          servername: "",
          userName: "",
          password: "",
          usePrivateKey: false,
          keyFileName: "",
          passPhrase: "",
          stdOutFieldName: "stdOut",
          stdErrFieldName: "stdErr",
          timeOut: "10",
          proxyHost: "",
          proxyPort: "",
          proxyUsername: "",
          proxyPassword: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "384",
            yloc: "176",
            draw: "Y"
          }
        }
      }
    },
    components:{
      Log,
      PreViewData
    },
    mounted() {
      this.init();
    },
    methods: {

      /**
       * 显示日志
       */
      showLog() {
        this.logKey = new Date().getTime();
        this.logVisiable = true;
      },


      reverStep() {
        for (let key in this.step) {
          if (this.step[key] === 'Y') {
            this.step[key] = true;
          } else if (this.step[key] === 'N') {
            this.step[key] = false;
          }
        }
      },
      /**
       * 数据预览
       */
      previewDatas() {
        this.changeStep();
        let dataJson = spliceDataJson(this.key, this.step);
        let params = {
          previewSize: this.limit,
          previewStepName: this.step.name,
          projectFile: JSON.stringify(dataJson),
          projectId: 'sss',
          projectName: 'sds'
        }
        this.previewLoading = true;
        executePreviewByFile(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.dataPre = data.content.previewRows;
            this.dataColumn = data.content.previewFieldNames;
            this.previewKey = new Date().getTime();
            this.log = data.content.log;
            this.preDataVisiable = true;
          } else {
            this.log = data.content.log;
            this.showLog();
          }
          this.reverStep();
          this.step.dynamicCommandField=this.step.dynamicCommandField==='Y'?true:false;
          this.previewLoading = false;
        })
      },
      /**
       * 初始化步骤信息
       */
      init() {
        //初始化步骤信息
        this.step = initStep(this.nodeData, this.key, this.step);
        this.oldStepName = this.nodeData.label;
        // if (this.step.dynamicCommandField === true) {
        //获取上步骤步骤信息
        let stepName = this.nodeData.label;
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        };
        let outFields = store.getters.generateOutputFields(param);

        this.step['outFields'] = outFields;
        // }
        this.step.type = 'SSH2';
      },

      //测试SSH连接
      testConnection() {
        let params = {
          publicpublickey: this.step.usePrivateKey,
          realPassword: this.step.proxyPassword,
          realUsername: this.step.userName,
          realkeyFilename: this.step.keyFileName,
          realkeyPass: this.step.passPhrase,
          realproxyhost: this.step.proxyHost,
          realproxypassword: "",
          realproxyport: "",
          realproxyusername: "",
          realservername: this.step.servername,
          realserverpassword: this.step.password,
          realserverport: this.step.port,
          timeout: this.step.timeOut,
          useproxy: false,
        }
        testSSHConnect(params).then(res => {
          console.info(res)
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.$message({
              message: '连接到服务器 [ ' + this.step.servername + ' ] 成功',
              type: 'success'
            })
          }
        })
      },

      changeStep() {
        for (let key in this.step) {
          // console.info(typeof  this.step[key]);
          if (typeof this.step[key] === 'boolean') {
            this.step[key] = this.step[key] === true ? 'Y' : "N"
          }
        }
      },

      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },

      /**
       * 提交步骤信息
       */
      submit() {
        let step = Object.assign({}, this.step);
        let paramName = step.name;
        step.type = 'SSH2';
        step.dynamicCommandField = this.step.dynamicCommandField ? 'Y' : 'N';
        step.usePrivateKey = this.step.usePrivateKey ? 'Y' : 'N';
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);

        let flag = false;
        if (step.name !== newName) {
          flag = true;
          step.name = newName;
        }
        step.initFlag = true;
        // 修改步骤xinxi
        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: step
          }
        }
        store.dispatch('updateStepInfo', param);
        let params = {
          id: this.nodeData.id, //插件id
          label: step.name,
          oldName: this.oldStepName,
        };
        util.$emit('updateNode', params);
        util.$emit('closeDialog', 'close');
        if (flag) {
          this.$alert(paramName + '名称已存在,重命名为' + newName + "!", '注释', {
            confirmButtonText: '确定',
          });
        } else {
          this.$message({
            message: '步骤信息修改成功',
            type: 'success'
          })
        }
      },
      changeCommand(){
        if(this.step.dynamicCommandField === false){
          this.step.commandfieldname='';
        }
      },

      handleClick(tab, event) {
        console.log(tab, event);
      },
    }
  }
</script>

<style scoped>

</style>
