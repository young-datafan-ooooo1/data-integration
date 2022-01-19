<template>
  <div class="plugin_body">
    <div class="div_form plugin_content plugin_form">
      <el-form :model="step" ref="stepfrom" label-width="30%" size="mini">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <el-tabs>
        <el-tab-pane label="基础设置">
          <div class="node_title_basic">
            <span>设置:</span>
          </div>
          <div class="form_css">
            <el-form size="mini" :model="step" label-position="right" label-width="30%">
              <el-form-item label="统一资源定位地址" prop="url">
                <el-input v-model="step.url" :disabled="step.urlInField === true" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="接受域中的URL" prop="urlInField">
                <el-checkbox v-model="step.urlInField"></el-checkbox>
              </el-form-item>
              <!--              //url字段名-->
              <el-form-item label="url字段名" prop="urlField">
                <el-select v-model="step.urlField" :disabled="step.urlInField===false" style="width: 65%" clearable>
                  <el-option v-for="item in step.outFields" :label="item.name" :value="item.name" :key="item.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="HTTP方法" prop="method">
                <el-select v-model="step.method" :disabled="step.dynamicMethod === true" style="width: 65%" clearable>
                  <el-option v-for="item in methods" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="从字段获取方法" prop="dynamicMethod">
                <el-checkbox v-model="step.dynamicMethod"></el-checkbox>
              </el-form-item>
              <el-form-item label="方法字段名" prop="methodFieldName">
                <el-input v-model="step.methodFieldName" :disabled="step.dynamicMethod===false" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="字段体" prop="bodyField">
                <el-select v-model="step.bodyField" :disabled="step.method === 'GET' || step.method==='OPTIONS'
                || step.method==='HEAD' || step.method === 'DELETE'" style="width: 65%" clearable>
                  <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="应用程序类型" prop="applicationType">
                <el-select v-model="step.applicationType" :disabled="step.method === 'GET' || step.method==='OPTIONS'
                || step.method==='HEAD' || step.method === 'DELETE'" style="width: 65%" clearable>
                  <el-option v-for="item in applications" :label="item" :key="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </div>

          <div class="node_title_basic">
            <span>输出字段:</span>
          </div>
          <div class="form_css">
            <el-form :model="step.result" size="mini" label-width="30%" label-position="right">
              <el-form-item label="结果字段名" prop="name">
                <el-input v-model="step.result.name" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="状态码字段名" prop="code">
                <el-input v-model="step.result.code" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="响应时间字段名(毫秒)" prop="response_time">
                <el-input v-model="step.result.response_time" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="响应头字段名" prop="response_header">
                <el-input v-model="step.result.response_header" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="身份验证">
          <div class="node_title_basic">
            <span>HTTP身份验证:</span>
          </div>
          <div class="form_css">
            <el-form :model="step" label-width="30%" label-position="right" size="mini">
              <el-form-item label="Http登录用户" prop="httpLogin">
                <el-input v-model="step.httpLogin" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="httpPassword">
                <el-input v-model="step.httpPassword" type="password" show-password
                          auto-complete="new-password" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="preemptive" prop="preemptive">
                <el-checkbox v-model="step.preemptive"></el-checkbox>
              </el-form-item>
            </el-form>

          </div>
        </el-tab-pane>
        <el-tab-pane label="SSL协议">
          <div class="node_title_basic">
            <span>信任存储文件</span>
          </div>
          <div class="form_css">
            <el-form :model="step" label-position="right" label-width="30%" size="mini">
              <el-form-item label="信任存储文件" prop="trustStoreFile">
                <el-input v-model="step.trustStoreFile" type="textarea" style="width: 65%;margin-bottom: 5px" ></el-input>
                <el-upload style="display: inline-block;" :auto-upload="false" :show-file-list="false"
                           class="upload-demo upload-column"
                           action="" :on-change="batchUploadBasicModel"  :before-upload="beforUpload">
                  <el-button size="mini" type="primary" style="margin-bottom: 5px">选择文件</el-button>
                </el-upload>
              </el-form-item>
              <el-form-item label="密码" prop="trustStorePassword">
                <el-input v-model="step.trustStorePassword" type="password" show-password
                          auto-complete="new-password" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="头部信息">
          <div class="node_title_basic">
            <span>标题</span>
            <span style="float: right">
              <el-button size="mini" type="primary"  @click="addTitle('title')">新增</el-button>
              <el-button type="primary" size="mini"  @click="getParam('title')">获取字段</el-button>
            </span>
          </div>
          <el-table :data="step.headers.header" height="300" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column label="字段">
              <template slot-scope="scope">
                <el-select size="mini" v-model="scope.row.field">
                  <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="字段名称" prop="name">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteTitle(scope.$index,'title')">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="参数">
          <div class="node_title_basic">
            <span>参数</span>
            <span style="float: right">
               <el-button size="mini" type="primary" :disabled="step.method === 'GET' || step.method==='OPTIONS'
                || step.method==='HEAD' || step.method === 'DELETE'" @click="addTitle('parmeter')">新增</el-button>
              <el-button type="primary" size="mini" :disabled="step.method === 'GET' || step.method==='OPTIONS'
                || step.method==='HEAD' || step.method === 'DELETE'" @click="getParam('parameter')">获取字段</el-button>
            </span>
          </div>
          <el-table :data="step.parameters.parameter" height="300" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column label="字段">
              <template slot-scope="scope">
                <el-select size="mini" v-model="scope.row.field">
                  <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="字段名称" prop="name">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteTitle(scope.$index,'parameter')">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="矩阵参数">
          <div class="node_title_basic">
            <span>参数</span>
            <span style="float: right">
               <el-button size="mini" type="primary" :disabled="step.method === 'GET' || step.method==='OPTIONS'
                || step.method==='HEAD' || step.method === 'DELETE'" @click="addTitle('title')">新增</el-button>
              <el-button type="primary" size="mini" :disabled="step.method === 'GET' || step.method==='OPTIONS'
                || step.method==='HEAD' || step.method === 'DELETE'" @click="getParam('title')">获取字段</el-button>
            </span>
          </div>
          <el-table :data="step.matrixParameters.matrixParameter" height="300"
                    :header-cell-style="{background:'#eef1f6'}">
            <el-table-column label="字段">
              <template slot-scope="scope">
                <el-select size="mini" v-model="scope.row.field">
                  <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="字段名称" prop="name">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.name"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteTitle(scope.$index,'')">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import store from "../../../../vuex/store";
  import {initStep} from "../../../../common/PluginTool";
  import util from "../../../../common/utils";
  export default {
    name: "Rest",
    data() {
      return {
        oldStepName:'',
        key:'',
        nodeData:'',
        methods: ["GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "PATCH"],
        applications:['TEXT PLAIN','XML','JSON','OCTET STREAM','XHTML','FORM URLENCODED','ATOM XML','SVG XML','TEXT XML'],
        step: {
          name: "REST client",
          type: "Rest",
          distribute: "Y",
          copies: "1",
          partitioning: {"method": "none"},
          applicationType: "TEXT PLAIN",
          method: "GET",
          url: "",
          urlInField: false,
          dynamicMethod: false,
          methodFieldName: "",
          bodyField: "",
          httpLogin: "",
          httpPassword: "",
          proxyHost: "",
          proxyPort: "",
          preemptive: true,
          trustStoreFile: "",
          trustStorePassword: "",
          headers: {
            header: []
          },
          parameters: {
            parameter: []
          },
          matrixParameters: {
            matrixParameter: []
          },
          result: {
            name: "result",
            code: "",
            response_time: "",
            response_header: ""
          },
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "400",
            yloc: "160",
            draw: "Y"
          }
        }
      }
    },
    mounted() {
      this.init();
    },
    methods:{

      // 文件上传是检查
      batchUploadBasicModel(file) {
        let sufix = file.name.substr(file.name.lastIndexOf('.') + 1);
        const isCsv = file.name.substr(file.name.lastIndexOf('.') + 1) === this.fileType;
        if (this.fileType.indexOf(sufix) >= 0) {
          this.editForm.fileRaw = file.name;
          this.editForm.file = file.raw;
          this.editForm.fileName = file.name;
          this.editForm.fileType = file.name.substr(file.name.lastIndexOf('.') + 1);
        } else {
          this.acceptType = '';
          this.editForm.fileRaw = file.name;
          this.editForm.file = file.raw;
          this.editForm.fileName = file.name;
          this.editForm.fileType = file.name.substr(file.name.lastIndexOf('.') + 1);
        }

      },

      // 文件上传前校验文件名称
      beforUpload(file) {
        const isCsv = file.type === this.fileType;
        return isCsv;
      },

      init(){
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let params = {
          key: this.key,
          value: this.step.name,
        }
        //获取上步骤字段信息
        let outFields =store.getters.generateOutputFields(params);
        let curStep = store.getters.getCurrentStep(params);
        if(curStep!==undefined && curStep.headers!==undefined){
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            } else if (curStep[key] === 'N' && key !== 'type') {
              this.step[key] = false;
            } else {
              if (key === 'headers' || key ==='parameters' || key ==='matrixParameters') {
              } else {
                this.step[key] = curStep[key];
              }
            }
          }
          if (this.step.headers.header !== undefined) {
            this.step.headers.header = [];
            let fields = curStep.headers.header;
            fields.forEach(item => {
              this.step.headers.header.push(item);
            })
          }
          if (this.step.parameters.parameter !== undefined) {
            this.step.parameters.parameter = [];
            let fields = curStep.parameters.parameter;
            fields.forEach(item => {
              this.step.parameters.parameter.push(item);
            })
          }
          if(curStep.matrixParameters.matrixParameter!==undefined){
            this.step.matrixParameters.matrixParameter=[];
            let fields = curStep.matrixParameters.matrixParameter;
            fields.forEach(item => {
              this.step.matrixParameters.matrixParameter.push(item);
            })
          }
        }
        this.step['outFields'] = outFields
        // this.step = initStep(this.nodeData, this.key, this.step);
      },
      addTitle(type){
        if(type === 'title'){
          this.step.headers.header.push({
            field:'',
            name:''
          })
        }else if(type === 'parameter'){
          this.step.parameters.parameter.push({
            field:'',
            name:'',
          })
        }else{
          this.step.matrixParameters.matrixParameter.push({
            field:'',
            name:''
          })
        }

      },
      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },
      getParam(type){
        let tmpData=[];
        this.step.outFields.forEach(item=>{
          tmpData.push({
            field:item.name,
            name:item.name,
          })
        })
        if(type ==='title'){
          this.step.headers.header=[];
          this.step.headers.header = tmpData;
        }else if(type==='parameter'){
          this.step.parameters.parameter=[];
          this.step.parameters.parameter = tmpData;
        }else{
          this.step.matrixParameters.matrixParameter=[];
          this.step.matrixParameters.matrixParameter = tmpData;
        }
      },
      deleteTitle(index,type){
        if(type ==='title'){
          this.step.headers.header.splice(index,1);
        }else if(type === 'parameter'){
          this.step.parameters.parameter.splice(index,1);
        }else{
          this.step.matrixParameters.matrixParameter.splice(index,1);
        }

      },
      submit(){
        let step = Object.assign({},this.step);
        step.urlInField = this.step.urlInField === true?'Y':'N';
        step.dynamicMethod = this.dynamicMethod === true?'Y':'N';
        step.preemptive = this.preemptive === true?'Y':'N';
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, step.name);
        let flag = false;
        if (step.name !== newName) {
          flag = true;
          step.name = newName;
        }
        step.outFields.push({
          currency: "",
          decimal: "",
          format: "",
          group: "",
          length: -1,
          name: step.result.name,
          nullif: '',
          precision: -1,
          set_empty_string: "N",
          type: 'String',
        })
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
          this.$alert(step.name + '名称已存在,重命名为' + newName + "!", '注释', {
            confirmButtonText: '确定',
          });
        } else {
          this.$message({
            message: '步骤信息修改成功',
            type: 'success'
          })
        }
      },
    }
  }
</script>

<style>
  .form_css input[type="file"] {
    display: none;
  }

</style>
