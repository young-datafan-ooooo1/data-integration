<!--
 * @Author: your name
 * @Date: 2020-07-15 11:38:21
-->
<template>
  <div class="plugin_body">
    <div class="div_form plugin_content">
      <el-form :model="step" ref="step" label-width="30%" size="mini" :rules="rules">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
        <el-form-item label="数据库连接">
          <el-select v-model="step.connection" @change="SelectionChange" clearable style="width: 26%">
            <el-option v-for="item in dataSources" :key="item.datasourceId" :label="item.dsName"
                       :value="item.datasourceId">
            </el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-edit" @click="openConfig('edit')" :disabled="step.connection === ''">编辑</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="openConfig">新建</el-button>
        </el-form-item>
        <!-- 目标模式 -->

        <el-form-item label="目标模式" prop="schema">
          <el-select v-model="step.schema" @change="handleSelectionChange" style="width: 65%" clearable :disabled="schemaDisable">
            <el-option v-for="item in schemas" :value="item"
                       :label="item" :key="item"></el-option>
          </el-select>
          <!-- todo 新增分组 -->

        </el-form-item>
        <!-- 目标 -->

        <el-form-item label="目标表" prop="table">
          <el-select v-model="step.table" allow-create filterable default-first-option style="width: 65%" clearable :disabled="step.tablename_in_field===true">
            <el-option v-for="item in treeData" :key="item.value" :label="item.value" :value="item.value"></el-option>
          </el-select>
          <!-- todo 新增分组 -->
        </el-form-item>
        <el-form-item label="提交记录数量">
          <el-input v-model="step.commit" clearable style="width: 65%"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="step.truncate" :disabled="step.partitioning_enabled===true">裁剪表</el-checkbox>
          <el-checkbox v-model="step.ignore_errors" :disabled="step.use_batch===true">忽略插入错误</el-checkbox>
          <el-checkbox v-model="step.specify_fields">指定数据可字段</el-checkbox>
        </el-form-item>
      </el-form>
      <!-- 第二个界面，两个内容 -->
      <el-tabs v-model="activeName">
        <el-tab-pane label="主选项" name="MainOption">
          <el-form :model="step" ref="step" label-width="30%" class="title_div" size="mini" label-position="right">
            <el-form-item label="表分区数据">
              <el-checkbox v-model="step.partitioning_enabled"></el-checkbox>
            </el-form-item>
            <el-form-item label="分区字段">
              <el-select v-model="step.partitioning_field" placeholder="请选择" clearable style="width: 65%" :disabled="step.partitioning_enabled===false">
                <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.partitioning_daily"  @change="changDaily()" :disabled="step.partitioning_enabled===false">每月分区数据</el-checkbox>
              <el-checkbox v-model="step.partitioning_monthly" @change="changeMonth()" :disabled="step.partitioning_enabled===false">每天分区数据</el-checkbox>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.use_batch" :disabled="step.return_keys===true">使用批量插入</el-checkbox>
              <el-checkbox v-model="step.tablename_in_field">表名定义在一个字段里</el-checkbox>
            </el-form-item>
            <el-form-item label="包含表名的字段">
              <el-select v-model="step.tablename_field" placeholder="请选择" style="width: 65%" clearable :disabled="step.tablename_in_field===false">
                <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.tablename_in_table" :disabled="step.tablename_in_field===false">存储表名字段</el-checkbox>
              <el-checkbox v-model="step.return_keys">返回一个自动产生的关键字</el-checkbox>
            </el-form-item>
            <el-form-item label="自动产生关键字的名称">
              <el-input v-model="step.return_field" style="width: 65%" :disabled="step.return_keys===false"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="数据库字段" name="DateBaseFiled">
          <el-form
            :model="step"
            ref="DateBaseFiled"
            size="mini"
            prop="fields"
          >
            <el-form-item>
              <div class="node_title_basic">
                <span>表字段</span>
<!--                <span style="float: right;margin-left: 5px">-->
<!--                  <el-button type="primary">-->
<!--                    输入字段映射-->
<!--                  </el-button>-->
<!--                </span>-->
                <span style="float: right">
                  <el-button type="primary" @click="getFieldInfo" :disabled="step.specify_fields===false">
                    获取字段
                  </el-button>
                </span>
              </div>
              <el-table :data="showFields" height="250">
                <el-table-column prop="column_name" label="表字段"></el-table-column>
                <el-table-column prop="stream_name" label="流字段">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.stream_name" v-if="scope.row.edit">
                      <el-option v-for="item in fields" :key="item.key" :label="item.label" :value="item.key">
                      </el-option>
                    </el-select>
                    <span v-else>{{scope.row.stream_name}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100px" fixed="right">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
                    <el-button size="mini" type="text" @click="editLine(scope.row)">编辑</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-pagination style="float: right" class="right" @current-change="selectedPage"
                             :current-page.sync="curPage"
                             @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 50, 150]"
                             layout="total, sizes, prev, pager, next"
                             :total="step.fields.field.length">
              </el-pagination>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer plugin_footer">
      <span>
        <el-button @click="closeDialog" size="mini">取 消</el-button>
         <el-button type="primary" size="mini" :loading="createLoading" @click="getCreateTableDDL('step') "
                    :disabled="this.step.table===undefined||this.step.table===null||this.step.table===''||step.is_newTable===false||step.tablename_in_field ===true">
              点击建表
            </el-button>
        <el-button type="primary" @click="submit('step')" size="mini">确 定</el-button>
      </span>
    </div>

    <el-dialog title="新增表" :visible.sync="addTableViaiable" width="50%" class="dialog_temp other_dialog"
               v-loading="sql_loading" :modal-append-to-body='false' :append-to-body="true">
      <code-mirror ref="code"></code-mirror>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addTableViaiable = false">取 消</el-button>
        <el-button type="primary" @click="addTableEdit">执行</el-button>
      </span>
    </el-dialog>

    <!--    数据源滚管理-->
    <el-dialog :visible.sync="build" class="abow_dialog" width="70%" :title="dataSourceTitle" :append-to-body="true">
      <data-source-manage :key="dataSourcekey" ref="dataSourceView"></data-source-manage>
    </el-dialog>
  </div>
</template>
<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store";
  import codeMirror from "../../../common/codemirror.vue";
  import vPinyin from "../../../../py/vue-py.js";
  import sqlFormatter from "sql-formatter";
  import {getSchema, getTables, getColumns, getConnection,getCreateTableDDL,executeCreateDDL} from "../../../../api/api.js";
  import dataSourceManage from '../../../common/DataSourceManager';
  import tableView from "../../../common/TableViews";
  import {addConnection} from "../../../../common/common";

  export default {
    data() {
      return {
        showFields:[],
        curPage:1,
        pageSize:10,
        sql_loading:false,
        fields:[],
        addTableViaiable:false,
        createLoading:false,
        build: false,
        dataSourceTitle: '',
        schemaDisable: true,
        activeName: "MainOption",
        dataSources: [],
        dataSourcekey: '',
        schemas: [],
        treeData: [],
        step: {
          name: "表输出",
          type: "TableOutput",
          description: [],
          distribute: "Y",
          custom_distribution: [],
          copies: "1",
          partitioning: {
            method: "none",
            schema_name: []
          },
          connection: "",
          schema: "",
          table: "",
          commit: "1000",
          truncate: false,
          ignore_errors: false,
          use_batch: true,
          specify_fields: false,
          partitioning_enabled: false,
          partitioning_field: "",
          partitioning_daily: false,
          partitioning_monthly: true,
          tablename_in_field: false,
          tablename_field: "",
          tablename_in_table: false,
          return_keys: false,
          return_field: "",
          fields: {
            field: []
          },
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 448,
            yloc: 160,
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
      }
    },
    components: {
      dataSourceManage,
      codeMirror
    },
    mounted() {
      this.init();
    },
    methods: {

      changDaily(){
        if(this.step.partitioning_daily===true){
          this.step.partitioning_monthly =false;
        }else{
          this.step.partitioning_monthly =true;
        }
      },
      changeMonth(){
        if(this.step.partitioning_monthly ===true){
          this.step.partitioning_daily=false;
        }else{
          this.step.partitioning_daily=true;
        }
      },


      selectedPage() {
        this.showFields = [];
        let fields = [];
        fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          this.showFields.push(item);
        });

      },

      addTableEdit() {

        this.ct_loading = true;

        // 获取编辑框中的值
        let ddl = this.$refs.code.editor.getValue();
        let params = {
          ddl: ddl,
          dataSourceId: this.step.connection
        }
        executeCreateDDL(params).then(res => {
          // 获取所有数据源
          let {
            data
          } = res;
          if (data.code === '10000') {

            this.$message({
              message: '创建成功！',
              type: 'success'
            });
            this.addTableViaiable = false;
            this.ct_loading = false;

          } else {
            this.$message.error(data.msg);
            this.ct_loading = false;

          }
        })

      },

      deleteRow(index, rows) {
        this.step.fields.field.splice(index, 1);
      },
      editLine(row){
        this.step.fields.field.forEach((item,index)=>{
          if (item.name === row.name) {
            // item.decimal='.'
            this.$set(this.step.fields.field[index], 'edit', true);
            this.$forceUpdate();
          }
        });
      },
      getCreateTableDDL(step) {
        this.ct_loading = true;

        this.$refs[step].validate((valid) => {
          if (valid) {
            let params = {
              dataSourceId: this.step.connection,
              fieldsInfos: [],
              pk: null,
              schema: this.step.schema,
              semicolon: false,
              tableName: this.step.table,
              tk: null,
              use_autoinc: false
            };

            this.lastStepField.forEach(item=>{
              console.info("item",item);
              params.fieldsInfos.push({
                length: item.length,
                name:item.name,
                nameCn:item.name,
                precision:item.precision,
                type:item.type,
              })
            })
            this.createLoading = true;
            getCreateTableDDL(params).then(res => {
              // 获取所有数据源
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.ct_loading = false;
                this.addTableViaiable = true;
                //console.info("ddl is ", data.content);
                this.sql_loading = true;
                this.$nextTick(() => {
                  this.$refs.code.editor.setValue(data.content);

                })
                this.sql_loading = false;
                this.createLoading = false;
              } else {
                this.$message({
                  message:data.msg,
                  type:'error'
                })
                this.ct_loading = false;
                this.createLoading = false;
              }
            })
          }
        })
      },

      /**
       * 获取字段
       */
      getFieldInfo(){
        this.step.fields.field=[];
        this.fields=[]
        this.step.outFields.forEach(item=>{
          this.step.fields.field.push({
            edit:false,
            column_name:item.name,
            stream_name:item.name,
          })
          this.fields.push({
            key:item.name,
            label:item.name
          })
        })
        this.selectedPage();
      },

      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
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
      //获取Schema
      SelectionChange(schema) {
        let params = {
          datasourceName: this.step.connection
        }
        console.info(schema)
        if(schema!==undefined && schema!=this.step.connection){
          this.step.schema=schema;
        }else{
          this.step.schema='';
        }


        getSchema(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            if (data.content !== undefined && data.content !== null && data.content.length !== 0) {
              this.schemaDisable = false;
              this.schemas = data.content;
              this.handleSelectionChange();
            }
            this.schema = data.content;
            if(this.schema.length===0){
              this.schemaDisable = true;
              this.handleSelectionChange();
            }
          }
        })

      },
      addConnection(){

      },

      //获取目标表
      handleSelectionChange() {
        let params = {
          datasourceName: this.step.connection,
          schema: this.step.schema,
        }


        getTables(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            let tmpData = [];
            let tables = data.content;
            tables.forEach((item, index) => {
              tmpData.push({
                value: item
              })
            })
            this.treeData = tmpData;
          } else {
            this.$message.error("获取表异常");

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
          this.build = true;
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
          this.build = true;
          this.dataSourceTitle = '新增连接';
        }

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
      init() {
        this.getConstant();
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

        let laststeps = store.getters.getLastStep(params);


        //获取上一步步骤信息
        if (curStep !== undefined && curStep.fields.field !== undefined && curStep.connection!=='') {
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
          this.step.type ='TableOutput'
          this.step.fields.field = [];
          let fields = JSON.parse(JSON.stringify(curStep.fields.field));
          fields.forEach(item => {
            item.edit =false;
            this.step.fields.field.push(item);
          })
          this.SelectionChange(curStep.schema);
        }
        let outFields =  store.getters.generateOutputFields(params);
        this.step['outFields'] = outFields;
        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
          this.lastStepField = outFields;
        }
      },
      submit() {
        this.step.initFlag = true;
        let step = Object.assign({}, this.step);
        for (let key in step) {
          if (typeof step[key] === 'boolean') {
            if (step[key] === true) {
              step[key] = 'Y'
            } else {
              step[key] = 'N'
            }
          }
        }
        let oldName = step.name;
        step["oldStepName"] = this.oldStepName;
        // 控件重命名
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, step.name);
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
        addConnection(this.dataSources,this.step.connection,this.key);
        util.$emit("updateNode", params);
        util.$emit("closeDialog", "close");
        if (flag) {
          this.$alert(
            oldName + "名称已存在,重命名为" + newName + "!",
            "注释",
            {
              confirmButtonText: "确定"
            }
          );
        } else {
          this.$message({
            message: "步骤信息修改成功",
            type: "success"
          });
        }
      }
    },
  };
</script>
<style>
  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }


  .bodyborder {
    height: 150px;
    margin-top: 0px;
    overflow: auto;
  }
</style>
