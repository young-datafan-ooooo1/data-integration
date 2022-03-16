<!--
 * @Author: your name
 * @Date: 2020-07-15 18:46:41
-->
<template>
  <div class="plugin_body">
    <div class="div_form plugin_content">
      <el-form :model="step" ref="step" label-width="30%" :rules="rules" size="mini">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%;"></el-input>
        </el-form-item>
      </el-form>
      <div class="father">
        <div class="rightson">
          <div class="node_title_basic">
            <span>Java script函数</span>
          </div>
          <div class="L_right">

            <el-tree :data="data" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
          </div>
        </div>
        <div class="leftson">
          <div style="display: inline-block;padding-left:10px">
            <div class="node_title_basic">
              <span>Java script</span>
            </div>
          </div>
          <div class="left_script">
            <el-tabs v-model="editableTabsValue" type="border-card" :tab-click="tabClick">
              <el-tab-pane
                :key="item.jsScript_type"
                v-for="(item, index) in step.jsScripts.jsScript"
                :label="item.jsScript_name"
                :name="item.jsScript_type">
                <div class="java_script" contenteditable>
                  <code-mirror ref="code" :mime="mime"></code-mirror>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
          <div>
            <!--            <div>位置：</div>-->
            <div style="margin-top:45px;">
              <el-form :model="step" ref="step" label-width="120px" size="mini" :inline="true">
                <el-form-item label-width="70px" label="兼容模式">
                  <el-checkbox v-model="step.compatible"></el-checkbox>
                </el-form-item>
                <el-form-item label="优先级别">
                  <el-input v-model="step.optimizationLevel" clearable></el-input>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>
      <div class="node_title_basic">
        <span>字段设置</span>
        <span style="float: right">
          <el-button size="mini" type="primary" @click="addRows">新增</el-button>
        </span>
      </div>
      <el-table :data="step.fields.field" height="200" :header-cell-style="{background:'#eef1f6'}">
        <el-table-column prop="name" label="字段名称">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" size="mini"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="rename" label="改名为">
          <template slot-scope="scope">
            <el-input v-model="scope.row.rename" size="mini"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型">
          <template slot-scope="scope">
            <el-select v-model="scope.row.type" size="mini" style="width: 100%;">
              <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="length" label="长度">
          <template slot-scope="scope">
            <el-input v-model="scope.row.length" size="mini"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="precision" label="精度">
          <template slot-scope="scope">
            <el-input v-model="scope.row.precision" size="mini"></el-input>
          </template>
        </el-table-column>

        <el-table-column prop="replace" label="替换成“Filedname”或“Rename to”值" width="300px">
          <template slot-scope="scope">
            <el-select v-model="scope.row.replace" size="mini">
              <el-option v-for="item in relaceParams" :key="item.id" :label="item.label" :value="item.id">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="deleteRow(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog title="生成记录" :visible.sync="generatorVisiable" :before-close="cloaseDialog" :close-on-click-modal='false'
               v-dialogDrag :modal="false" class="abow_dialog"
               v-alterELDialogMarginTop="{marginTop:'2vh'}">
      <row-generator :key="generatorKey" ref="generator" :temp-key="key" :init-f="initF" :test-value="testValue"
                     :script-name="scriptName" :script-id="scriptId"></row-generator>
    </el-dialog>
    <div class="dialog-footer plugin_footer">
        <span>
          <el-button size="mini" @click="getVariable" :loading="variableLoading">获取变量</el-button>
          <el-button size="mini" @click="testJavaScript">测试脚本</el-button>
          <el-button @click="closeDialog" size="mini">取 消</el-button>
          <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
          <!--          <el-button type="info" plain size="mini">最小宽度</el-button>-->
        </span>
    </div>
  </div>
</template>
<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store";
  import codeMirror from "../../../common/codemirror.vue";
  import {getJavaScriptFunctions, getVariate} from "../../../../api/api.js";
  import RowGenerator from "../RowGenerator/RowGenerator";
  import {spliceDataJson} from "../../../../common/common";
  import formatDate from '../../../../common/date.js'

  export default {
    data() {
      return {
        variableLoading: false,
        outFields: [],
        mime: 'javascript',
        initF: true,
        scriptName: '',
        scriptId: '',
        testValue: [],
        generatorKey: null,
        generatorVisiable: false,
        relaceParams: [{
          id: 'Y',
          label: '是',
        }, {
          id: 'N',
          label: '否'
        }],
        fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"], //字段类型
        nodeData: '',
        key: '',
        editableTabsValue: '0',
        data: [{
          id: 1,
          label: '转换脚本',
          children: [{
            label: '脚本1'
          }]
        }, {
          id: 2,
          label: '转换常数',
          children: []
        }, {
          id: 3,
          label: '变换函数',
          children: []
        }, {
          id: 4,
          label: '输入字段',
          children: []
        }, {
          id: 5,
          label: '输出字段',
          children: [{
            label: '请使用"替换值"字段名或"重命名为"字段',
          }],
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        datas: [{}, {}],
        step: {
          name: "JavaScript代码",
          type: "ScriptValueMod",
          description: "",
          distribute: "Y",
          custom_distribution: "",
          copies: "1",
          partitioning: {
            method: "none",
            schema_name: ""
          },
          compatible: false,//兼容模式
          optimizationLevel: "9",//优先级
          jsScripts: {
            jsScript: [{
              jsScript_type: "0",
              jsScript_name: "脚本1",
              jsScript_script: "//在此处编写脚本"
            }]
          },
          fields: {
            field: []
          },
          cluster_schema: [],
          remotesteps: {
            input: "\n      ",
            output: "\n      "
          },
          GUI: {
            xloc: "96",
            yloc: "144",
            draw: "Y"
          }
        },
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }]
        },
      };
    },
    components: {
      codeMirror,
      RowGenerator
    },
    mounted() {
      this.init();
    },
    methods: {

      changeStep() {
        for (let key in this.step) {
          if (typeof this.step[key] === 'boolean') {
            this.step[key] = this.step[key] === true ? 'Y' : "N"
          }
        }
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
       * 获取变量
       */
      getVariable() {
        this.changeStep();
        this.step.fields.field = []
        this.step.jsScripts.jsScript[0].jsScript_script = this.$refs.code[0].editor.getValue();
        let dataJson = spliceDataJson(this.key, this.step);
        let params = {
          name: this.step.name,
          projectFile: JSON.stringify(dataJson),
        }
        this.variableLoading = true;
        getVariate(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            data.content.forEach(item => {
              this.step.fields.field.push({
                name: item.name,
                rename: item.reName,
                type: item.type,
                length: item.len,
                precision: item.precision,
                replace: 'N',
              })
            })
            this.variableLoading = false;
          }
        })

      },

      /**
       * 测试脚本
       */
      testJavaScript() {
        this.changeStep();
        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: this.step
          }
        };
        store.dispatch("updateStepInfo", param);


        this.generatorKey = new Date().getTime();
        let testValue = [];
        this.outFields.forEach(item => {
          let fileLength = -1;
          let precision = -1;
          let nullIf;
          if (item.type.toUpperCase() === ('number').toUpperCase() || item.type.toUpperCase() === ('Integer').toUpperCase()
            || item.type.toUpperCase() === ('BigNumber').toUpperCase()) {
            fileLength = 38;
            precision = 0;
            nullIf = 0;
          } else if (item.type.toUpperCase() === ('string').toUpperCase()) {
            fileLength = 255;
            precision = -1;
            nullIf = 'test value test value';
          } else if (item.type.toUpperCase() === ('date').toUpperCase() || item.type.toUpperCase() === ('timestamp').toUpperCase()) {
            fileLength = 50;
            precision = 0;
            nullIf = formatDate.formatDate.format(new Date(), 'yyyy/MM/dd hh:mm:ss') + '.000';
          } else {
            fileLength = -1;
            precision = -1;
            nullIf = 'test value test value';
          }
          testValue.push({
            name: item.name,
            type: item.type,
            format: item.format,
            length: fileLength,
            precision: precision,
            group: ',',
            currency: '',
            set_empty_string: 'N',
            nullif: nullIf,
            decimal: '.',
          })
        })
        this.scriptName = this.step.name;
        this.scriptId = this.nodeData.id;
        this.testValue = testValue;
        this.generatorVisiable = true;
      },

      cloaseDialog() {
        this.generatorVisiable = false;
      },

      tabClick() {
      },
      /**
       * 新增字段
       */
      addRows() {
        this.step.fields.field.push({
          name: "",
          rename: "",
          type: "",
          length: "",
          precision: "",
          replace: "N"
        })
      },
      deleteRow(index) {
        this.step.fields.field.splice(index, 1);
      },

      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },


      getConstant() {
        this.$http.get('static/data.json').then(res => {
          this.formats = res.body.formatters.formats;
          this.dataFormatters = res.body.formatters.localDateFormats;
          this.encodings = res.body.formatters.encodings;
          this.formatDate = res.body.formatters.formatDate;
          this.formatNumber = res.body.formatters.formatNumber;
        })
      },

      /**
       * 获取数据库连接信息
       **/
      handleNodeClick(data, node, ev) {
        if (node.childNodes.length > 0) {

        } else if ((node.parent.data.id === undefined) && node.childNodes.length === 0) {
          this.step.jsScripts.jsScript.forEach((item, index) => {
            if (item.jsScript_type === this.editableTabsValue) {
              this.$nextTick(() => {
                let content = this.$refs.code[index].editor.getValue();
                this.$refs.code[index].editor.replaceSelection(data.label);
                item.jsScript_script = item.jsScript_script + data.label;
                // this.$refs.code[index].editor.setValue(content + '' + data.label);
              })
            }
          })
        } else if (node.parent.data.id === 4 && node.childNodes.length === 0) {
          this.step.jsScripts.jsScript.forEach((item, index) => {
            if (item.jsScript_type === this.editableTabsValue) {
              // item.jsScript_script = item.jsScript_script + data.label;
              this.$nextTick(() => {
                let content = this.$refs.code[index].editor.getValue();
                this.$refs.code[index].editor.replaceSelection(data.label);
                // item.jsScript_script = this.$refs.code[index].edi
                // this.$refs.code[index].editor.setValue(content + '' + data.label);
              })

            }
          })
        }
      },

      init() {
        getJavaScriptFunctions().then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            data.content.children.forEach(item => {
              if (item.label === 'Transform Constants') {
                this.data[1].children = item.children;
              } else if (item.label === 'Transform Functions') {
                this.data[2].children = item.children;
              }
            })
          }
        });
        this.$nextTick(() => {
          this.$refs.code[0].editor.refresh();
          this.$refs.code.mime = 'text/javascript';
        })


        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        //获取当前步骤信息
        let params = {
          key: this.key,
          value: stepName,
        }
        let curStep = store.getters.getCurrentStep(params);
        let outFields = store.getters.generateOutputFields(params);
        this.outFields = outFields;
        let laststeps = store.getters.getLastStep(params);
        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
          this.lastStepField = this.laststep.fields.field;
          let nameData = [];
          outFields.forEach(item1 => {
            nameData.push({
              label: item1.name,
            })
          })
          this.data[3].children = nameData;
        }
        if (curStep !== undefined && curStep.fields !== undefined) {
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            } else if (curStep[key] === 'N' && key !== 'type') {
              this.step[key] = false;
            } else {
              if (key === 'fields') {
              } else {
                this.step[key] = curStep[key];
              }
            }
          }
          this.step.type = 'ScriptValueMod'
          this.step.fields.field = [];
          let fields = curStep.fields.field;
          fields.forEach(item => {
            this.step.fields.field.push(item);
          })
        }
        let javaScriptData = this.step.jsScripts;
        let context = javaScriptData.jsScript[0].jsScript_script;
        this.$nextTick(() => {
          this.$refs.code[0].editor.setValue(context);
        })
      },
      submit() {
        let step = Object.assign({}, this.step);
        for (let key in step) {
          if (typeof step[key] === 'boolean') {
            step[key] = step[key] === 'Y';
          }
        }
        this.$refs.code.forEach((item, index) => {
          this.step.jsScripts.jsScript[index].jsScript_script = item.editor.getValue();
        })
        step.initFlag = true;
        step["oldStepName"] = this.oldStepName;
        // 控件重命名
        let paramName = step.name;
        let newName = store.getters.getCheckNodeName(
          this.key,
          this.oldStepName,
          paramName
        );
        step['outFields'] = this.outFields;
        step.fields.field.forEach(item => {
          console.info(item);
          let fieldName = item.name;
          if (item.replace === 'Y') {
            fieldName = item.rename;
          } else {
            let newFieldName = '';
            this.step.outFields.forEach(sub => {
              if (item.name === sub.name) {
                newFieldName = item.name+'_'+1;
              }else{
                newFieldName = item.name;
              }
            })
            console.info("newFieldName",newFieldName)
            fieldName = newFieldName;
          }
          console.info("fieldsName", fieldName)
          if(fieldName !==''){
            step.outFields.push({
              name: fieldName,
              type: item.type,
              length: item.length,
              precision: item.precision,
              trim_type: "none",
              repeat: "N",
              format: "",
              currency: item.currency === '' ? '¥' : item.currency,
              decimal: item.decimal === '' ? "." : item.decimal,
              group: item.group === '' ? ',' : item.group
            })
          }

        })
        console.info("step,", step.outFields);
        let flag = false;
        if (step.name !== newName) {
          flag = true;
          step.name = newName;
        }
        // 修改步骤xinxi
        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: step
          }
        };

        store.dispatch("updateStepInfo", param);
        let params = {
          id: this.nodeData.id, //插件id
          label: step.name,
          oldName: this.oldStepName
        };
        util.$emit("updateNode", params);
        util.$emit("closeDialog", "close");
        if (flag) {
          this.$alert(paramName + "名称已存在,重命名为" + newName + "!", "注释", {
            confirmButtonText: "确定"
          });
        } else {
          this.$message({
            message: "步骤信息修改成功",
            type: "success"
          });
        }
      }
    }
  };
</script>
<style>
  .father {
    display: inline-block;
    width: 100%;
  }

  .rightson {
    float: left;
    width: 40%;
  }

  .L_right {
    height: 300px;
    margin-right: 5px;
    border: 1px solid #999;
    overflow: auto;
  }

  .leftson {
    float: right;
    width: 60%;
  }

  .left_script {
    /*border: 1px solid #999;*/
    /*height: 280px;*/
    overflow: hidden;
    border-radius: 2%;
    margin-left: 10px;
  }

  .el-menu-item {
    font-size: 12px;
    color: #999;
  }

  .foot {
    margin-left: 68%;
    margin-bottom: 10px;
    position: absolute;
  }

  /* .el-submenu,.el-submenu-item{
   height: 50px;
  } */
</style>
