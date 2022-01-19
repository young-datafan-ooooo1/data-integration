<template>
  <div class="plugin_body">
    <div class="div_form plugin_content">
      <el-form :model="step" ref="TableInput" label-width="30%" size="mini" :rules="rules" label-position="right">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
        <el-form-item label="数据库连接">
          <el-select v-model="step.connection" @change="selectDataSource" clearable>
            <el-option v-for="item in dataSources" :key="item.datasourceId" :label="item.dsName"
                       :value="item.datasourceId">
            </el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-edit" @click="openConfig('edit')" :disabled="step.connection === ''">
            编辑
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click="openConfig">新建</el-button>
        </el-form-item>
      </el-form>

      <div class="node_title_basic">
        <span>高级设置</span>
        <!--        <span style="float: right">-->
        <!--          <el-button type="primary" :loading="sqlLoading" size="mini"-->
        <!--                     :disabled="true"-->
        <!--                     @click="getTables">获取sql查询语句</el-button>-->
        <!--        </span>-->
      </div>
      <div style="width: 100%;height:155px; overflow: auto;">
        <code-mirror ref="code" :mime="mime"></code-mirror>
      </div>

      <el-form :model="step" ref="TableInput" label-width="30%" size="mini" :rules="rules" label-position="right">

        <el-form-item label="允许简易转换" prop="variables_active">
          <el-checkbox v-model="step.variables_active"></el-checkbox>
        </el-form-item>
        <el-form-item label="替换SQL的变量" prop="replace_variables">
          <el-checkbox v-model="step.replace_variables"></el-checkbox>
        </el-form-item>
        <el-form-item label="从步骤插入数据">
          <el-input class="input-with-select" v-model="step.insert_Date" style="width: 65%">
            <el-select v-model="step.lookup" slot="prepend" placeholder="请选择"></el-select>
          </el-input>
        </el-form-item>
        <el-form-item label="执行每一步?" prop="execute_each_row">
          <el-checkbox v-model="step.execute_each_row"></el-checkbox>
        </el-form-item>
        <el-form-item label="记录数量限制">
          <el-input v-model="step.limit" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button type="primary" size="mini" @click="line=true" :disabled="step.connection===''">预览</el-button>
      <el-button @click="submit('step')" type="primary" size="mini" :loading="subLoading">确 定</el-button>
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
         <el-button @click="closePreview" size="mini">关闭</el-button>
         <el-button type="primary" @click="showLog" size="mini">查看日志</el-button>
       </span>
    </el-dialog>

    <!-- 获取sql语句dialog界面 -->
    <el-dialog
      title="获取sql语句"
      :visible.sync="select_sql"
      :before-close="handleClose"
      :modal-append-to-body="false"
      class="abow_dialog" width="40%">
      <table-view :key="tableKey" :tree-data="tableViews" :loading="loading" :connection="selectConnect"></table-view>
    </el-dialog>
    <!--    数据源滚管理-->
    <el-dialog :visible.sync="build" class="dialog_temp other_dialog" width="70%" :title="dataSourceTitle"
               :append-to-body="true">
      <data-source-manage :key="dataSourcekey" ref="dataSourceView"></data-source-manage>
    </el-dialog>

    <el-dialog :visible.sync="logVisiable" title="日志" :append-to-body="true" width="70%"
               v-alterELDialogMarginTop="{marginTop:'2vh'}" :modal-append-to-body="true"
               class="abow_in_dialog">
      <Log :key="logKey" :logs="log"></Log>
    </el-dialog>

  </div>
</template>
<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store";
  import codeMirror from "../../../common/codemirror.vue";
  import sqlFormatter from "sql-formatter";
  import dataSourceManage from '../../../common/DataSourceManager';
  import tableView from '../../../common/TableViews';
  import Log from "../../../flow/Log";
  import {
    previewTable,
    getStatement,
    getTableInputFields
  } from "../../../../api/api.js";
  import {executePreviewByFile, getConnection} from "../../../../api/api";
  import {addConnection, spliceDataJson} from "../../../../common/common";
  import PreViewData from "../../../common/PreViewData";

  export default {
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      },
    },
    data() {
      return {
        mime: 'text/x-mariadb',
        subLoading: false,
        loading: false,
        log: '',
        logKey: null,
        logVisiable: false,
        previewKey: null,
        tableLoading: false,
        dataColumn: [],
        dataPre: [],
        previewLoading: false,
        preDataVisiable: false,
        limit: 10,
        line: false,
        dataSourceTitle: '新增连接',
        sqlLoading: false,
        nodeData: {},
        key: '',
        selectConnect: '',
        dataSources: [],
        tableViews: {},
        dataSourcekey: null,
        tableKey: null,
        build: false,
        filterText: "",
        isShow: false, //是否展示获取sql的按钮
        schemas: [], //数据库列表
        treeData: [], //数据库表集合
        Connectpool_parameter: [],
        cluster_parameter: [],
        active: "normal",
        bulid: false,
        select_sql: false,
        tabPosition: "left",
        step: {
          name: "Table input",
          type: "TableInput",
          distribute: "Y",//分发或复制 Y为分发，N为复制
          copies: "1",
          partitioning: {"method": "none"},
          connection: "",
          sql: "",
          limit: "0",
          replace_variables: false,
          execute_each_row: false,
          variables_active: false,
          lazy_conversion_active: "N",
          remotesteps: {
            input: "",
            output: ""
          },
          fields: {
            field: []
          },
          GUI: {
            xloc: "400",
            yloc: "128",
            draw: "Y"
          }
        },
        rules: {
          name: [
            {
              required: true,
              message: "步骤名称不能为空",
              trigger: "blur",
            },
          ],
        },
      };
    },
    components: {
      dataSourceManage,
      tableView,
      codeMirror,
      PreViewData,
      Log
    },
    mounted() {
      this.init();
    },
    methods: {
      selectDataSource() {

      },

      closePreview() {
        this.preDataVisiable = false;
        this.line = false;
      },

      /**
       * 显示日志
       */
      showLog() {
        this.logKey = new Date().getTime();
        this.logVisiable = true;
      },
      /**
       * 数据预览
       */
      previewDatas(line) {
        let _this = this;
        if (line !== undefined && typeof line === 'number') {
          _this.limit = line;
        } else {
        }
        _this.step.replace_variables = _this.step.replace_variables === true ? 'Y' : 'N';
        _this.step.execute_each_row = _this.step.execute_each_row === true ? 'Y' : 'N';
        _this.step.variables_active = _this.step.variables_active === true ? 'Y' : 'N';
        _this.step.sql = _this.$refs.code.editor.getValue();
        addConnection(_this.dataSources, _this.step.connection, _this.key);
        _this.getColumns().then(function (resolve1) {
          let dataJson = spliceDataJson(_this.key, _this.step);
          let params = {
            previewSize: _this.limit,
            previewStepName: _this.step.name,
            projectFile: JSON.stringify(dataJson),
            projectId: 'sss',
            projectName: 'sds'
          }
          _this.previewLoading = true;
          executePreviewByFile(params).then(res => {
            let {
              data
            } = res;
            if (typeof line !== 'number') {
              if (data.code === '10000' && data.content.errors === 0) {
                _this.dataPre = data.content.previewRows;
                _this.dataColumn = data.content.previewFieldNames;
                _this.previewKey = new Date().getTime();
                _this.log = data.content.log;
                _this.preDataVisiable = true;
                _this.previewLoading = false;
              } else {
                _this.previewLoading = false;
                _this.log = data.content.log;
                _this.showLog();
              }
              _this.limit = 10;
              _this.reverStep();
            } else {
              _this.previewLoading = false;
              _this.limit = 10;
              _this.reverStep();
            }

          })
        });

      },

      changeStep() {
        this.step.replace_variables = this.step.replace_variables === true ? 'Y' : 'N';
        this.step.execute_each_row = this.step.execute_each_row === true ? 'Y' : 'N';
        this.step.variables_active = this.step.variables_active === true ? 'Y' : 'N';
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

      getColumns() {
        let _this = this;
        let promise = new Promise(function (resolve, reject) {
          let params = {};
          let connectionDetail = {};
          _this.dataSources.forEach(item => {
            _this.step.fields.field = [];
            if (item.datasourceId === _this.step.connection) {
              let param = {
                name: item.dsName,
                username: item.dsUsername,
                password: item.dsPassword,
                database: item.database,
                access: item.access,
                port: item.dsPort,
                server: item.dsHost,
                type: item.dsType,
                limit: 10,
                attributes: {
                  attribute: []
                },
              }

              let settings = JSON.parse(item.dsConnectorSetting);
              for (let key in settings) {
                let attribute = settings[key];
                if (attribute !== null) {
                  attribute.forEach(item => {
                    param.attributes.attribute.push({
                      code: item.code,
                      attribute: item.attribute
                    })
                  })
                }

              }
              connectionDetail = {
                connectionDetailVO: {
                  connection: param,
                },
                querySql: _this.step.sql
              }
              // params = {
              //   connection: param,
              // }
            }
          });
          _this.loading = true;
          getTableInputFields(connectionDetail).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              _this.step.fields.field = [];
              // this.csvinputData = data.content;
              for (let i = 0; i < data.content.length; i++) {
                let fileLength = -1;
                let precision = -1;
                if (data.content[i].fieldType.toUpperCase() === ('number').toUpperCase()) {
                  fileLength = 38;
                  precision = 0;
                } else if (data.content[i].fieldType.toUpperCase() === ('string').toUpperCase()) {
                  fileLength = 255;
                  precision = -1;
                } else {
                  fileLength = -1;
                  precision = -1;
                }
                _this.step.fields.field.push({
                  name: data.content[i].fieldName,
                  //nameCn: data.content[i],
                  type: data.content[i].fieldType,
                  length: data.content[i].fieldLength,
                  precision: data.content[i].fieldPrecision,
                  trim_type: "none",
                  repeat: 'N',
                  format: "",
                  currency: data.content[i].dollarSign,//货币符号
                  decimal: data.content[i].pointSymbol,//小数点符号
                  group: data.content[i].pointSymbol,//分组
                })
              }
              _this.loading = false;
              _this.coloseTable();
            } else {
              _this.loading = false;
              _this.$message({
                message: data.msg,
                type: 'error'
              })
            }
            resolve("success");
          })
        })
        return promise;
      },

      /**
       * 表输入预览数据
       **/
      previewData() {
        let params = {}
        this.previewLoading = true;
        this.dataSources.forEach(item => {
          if (item.datasourceId === this.step.connection) {
            let param = {
              name: item.dsName,
              username: item.dsUsername,
              password: item.dsPassword,
              database: item.database,
              access: item.access,
              port: item.dsPort,
              server: item.dsHost,
              type: item.dsType,
              limit: 10,
              attributes: {
                attribute: []
              },
            }
            params = {
              connectionDetailVO: {
                connection: param,
              },
              querySql: this.step.sql,

            }
          }
        });
        previewTable(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000' && data.content.errors === 0) {
            this.dataColumn = data.content.cloumns;
            this.previewLoading = false;
            this.preDataVisiable = true;
          } else {
            this.previewLoading = false;
          }
        })
      },

      /**
       *编辑
       **/
      openConfig(type) {
        this.dataSourcekey = new Date().getTime();
        this.build = true;
        //编辑
        if (type === 'edit') {
          // this.build = true;
          this.dataSourceTitle = '编辑连接';
          this.$nextTick(() => {
            this.dataSources.forEach(item => {
              if (item.datasourceId === this.step.connection) {
                this.$refs.dataSourceView.dataSourceData = item;
                this.$refs.dataSourceView.init();
              }
            })
          })
        } else {
          // this.build = true;
          this.dataSourceTitle = '新增连接';
        }
      },

      /**
       * 获取数据库连接信息
       * */
      getDataSources() {
        let params = {
          sourcePlatform: 'JCPLAT'
        }
        getConnection(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.dataSources = data.content;
          }
        })
      },


      init() {
        this.getDataSources();
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        //获取当前步骤信息
        let params = {
          key: this.key,
          value: stepName,
        }
        let curStep = store.getters.getCurrentStep(params);
        if (curStep && curStep.fields !== undefined) {
          this.step.name = curStep.name;
          this.step.connection = curStep.connection;
          this.step.sql = curStep.sql;
          // this.formatterSql(this.step.sql);
          this.$refs.code.setValue(this.step.sql);
          this.step.variables_active = curStep.variables_active === 'Y';
          this.step.execute_each_row = curStep.execute_each_row === 'Y';
          this.step.replace_variables = curStep.replace_variables === 'Y';
          this.step.limit = curStep.limit;
        }
      },

      coloseTable() {
        this.select_sql = false;
      },

      formatterSql(sql) {
        let dom = this.$refs.code;
        this.$refs.code.setValue(sqlFormatter.format(sql));
      },

      formatSql(sql) {
        this.step.sql = sql;
        this.$refs.code.setValue(sqlFormatter.format(sql));
        this.coloseTable()
        // this.getColumns();
      },

      /**
       * 获取数据库中的表信息
       */
      getTables() {
        let params = {
          datasourceName: this.step.connection,
          schema: "",
        };
        this.sqlLoading = true;
        getStatement(params).then(res => {
          this.selectConnect = this.step.connection;
          this.tableViews = res.data.content;
          this.sqlLoading = false;
          this.select_sql = true;
          this.tableKey = new Date().getTime();
        })
      },

      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },
      /**
       * 提交数据需要查询数据源信息
       */
      submit() {
        this.step.initFlag = true;
        this.step.sql = this.$refs.code.editor.getValue();
        let step = Object.assign({}, this.step);
        if (this.step.execute_each_row === true) {
          step.execute_each_row = "Y";
        } else {
          step.execute_each_row = "N";
        }

        if (this.step.variables_active === true) {
          step.variables_active = "Y";
        } else {
          step.variables_active = "N";
        }

        if (this.step.replace_variables === true) {
          step.replace_variables = 'Y';
        } else {
          step.replace_variables = 'N';
        }
        this.subLoading = true;
        let _this = this;
        addConnection(this.dataSources, this.step.connection, this.key);
        this.getColumns().then(function (resolve) {
          // let fieldData =[]
          // let fields = resolve.previewFieldNames===undefined?[]:resolve.previewFieldNames;
          // let types = resolve.previewFieldTypes === undefined?[]:resolve.previewFieldTypes;
          // for(let i = 0;i<types.length;i++){
          //   let length = -1;
          //   let precision =-1;
          //   if (types[i] === 'Number') {
          //     length = 15;
          //     precision=5;
          //   }else if(types[i] === 'BigNumber'){
          //     length = 38;
          //     precision=5;
          //   }else if(types[i] === 'String'){
          //     length = 255;
          //     precision=-1;
          //   }else{
          //     length = -1;
          //     precision=-1;
          //   }
          //   fieldData.push({
          //     name: fields[i],
          //     //nameCn: data.content[i],
          //     type: types[i],
          //     length: length,
          //     precision:precision,
          //     trim_type: "none",
          //     repeat: 'N',
          //     format: "",
          //     currency: '',//货币符号
          //     decimal: '',//小数点符号
          //     group: '',//分组
          //   })
          //
          // }
          // step.fields.field = fieldData;
          step["oldStepName"] = _this.oldStepName;
          // 控件重命名
          let paramName = step.name;
          let newName = store.getters.getCheckNodeName(
            _this.key,
            _this.oldStepName,
            paramName
          );
          let flag = false;
          if (step.name !== newName) {
            flag = true;
            step.name = newName;
          }
          // 修改步骤xinxi
          let param = {
            key: _this.key,
            value: {
              oldStepName: _this.oldStepName,
              step: step,
            },
          };
          store.dispatch("updateStepInfo", param);
          let params = {
            id: _this.nodeData.id, //插件id
            label: step.name,
            oldName: _this.oldStepName,
          };

          util.$emit("updateNode", params);
          _this.subLoading = false;
          util.$emit("closeDialog", "close");
          if (flag) {
            _this.$alert(paramName + "名称已存在,重命名为" + newName + "!", "注释", {
              confirmButtonText: "确定",
            });
          } else {
            _this.$message({
              message: "步骤信息修改成功",
              type: "success",
            });
          }
        })
      },
      handleClose(done) {
        done();
      },

      deleteRow(index, rows) {
        rows.splice(index, 1);
      },

      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      closeAdd() {
        this.select_sql = false;
      },
    },
  };
</script>
<style>
  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }

  .Bfather {
    display: inline-block;
  }

  .son1,
  .son2 {
    float: left;
    margin-left: 10px;
  }

  .son2 {
    height: 380px;
    width: 242px;
  }

  .son_child1 {
    height: 250px !important;
  }

  .son_child1,
  .son_child2 {
    border: 1px solid #ccc;
    border-radius: 4px;
    width: 300px;
    height: 140px;
    margin-top: 5px;
  }

  .Build {
    background: #eef1f6;
  }

  .Build_father {
    background: aliceblue;
  }


  .dialog_body {
    overflow: auto;
    padding: 10px 20px;
    height: 550px;
  }

  .item_sql {
    margin-left: 100px;
    font-weight: bold;
    padding-top: 7px;
    float: left;
    font-size: 15px;
  }

  .item_button {
    float: right;
    margin-bottom: 5px;
    margin-left: 300px;
  }
</style>
