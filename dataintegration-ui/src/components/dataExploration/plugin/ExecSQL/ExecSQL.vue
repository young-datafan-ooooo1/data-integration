<!--
 * @Author: your name
 * @Date: 2020-07-10 13:35:03
-->
<template>
  <div class="plugin_body">
    <div class="div_form plugin_content">
      <el-form :model="step" ref="step" label-width="20%" size="mini" :rules="rules">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 80%;"></el-input>
        </el-form-item>

        <el-form-item label="数据源" prop="connection">
          <el-select v-model="step.connection">
            <el-option v-for="item in dataSources" :key="item.datasourceId" :label="item.dsName"
                       :value="item.datasourceId">
            </el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-edit" @click="openConfig('edit')">编辑</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="openConfig">新建</el-button>
        </el-form-item>

        <el-form-item label="SQl执行脚本">
          <el-input v-model="step.sql" type="textarea" :autosize="{ minRows:5, maxRows:5,}"
                    style="width: 80%;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="step.single_statement">作为单个语句执行</el-checkbox>
          <el-checkbox v-model="step.quoteString">引用字符串</el-checkbox>
          <el-checkbox v-model="step.replace_variables">变量替换</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="step.execute_each_row" @change="changeValue()">执行每一行</el-checkbox>
          <el-checkbox v-model="step.BindString" :disabled="dis" style="margin-left: 40px;">绑定字符串</el-checkbox>
        </el-form-item>
        <div style="display:inline-block;width: 100%;">
          <div style="float: left; width: 45%">
            <div class="node_title_basic">
              <span>参数：</span>
              <span style="float: right"><el-button size="mini" type="primary" @click="addRows"
                                                    :disabled="step.execute_each_row===false">新增</el-button></span>
            </div>
            <!--            <el-form-item label="参考">-->
            <el-table :data="showFields" border stripe :disabled="dis" height="180px">

              <el-table-column label="作为参数的字段" prop="name">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.name" style="width: 100%" size="mini" v-if="scope.row.edit">
                    <el-option v-for="item in step.outFields" :key="item.name" :value="item.name" :label="item.name">
                    </el-option>
                  </el-select>
                  <span v-else>{{scope.row.name}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100px" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
                  <el-button size="mini" type="text" @click="editLine(scope.row)">编辑</el-button>
                </template>
              </el-table-column>

            </el-table>
            <el-pagination style="float: right" class="right" samll @current-change="selectedPage"
                           :current-page.sync="curPage"
                           @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 50, 150]"
                           layout="total, sizes, prev,jumper, next"
                           :total="step.arguments.argument.length">
            </el-pagination>
            <!--            </el-form-item>-->
          </div>
          <div style="float:right; width: 50%; ">
            <div class="node_title_basic">
              <span>
                字段：
              </span>
            </div>
            <el-form-item label="包括插入状态的字段" label-width="150px">
              <el-input v-model="step.insert_field"></el-input>
            </el-form-item>
            <el-form-item label="包括更新状态的字段" label-width="150px">
              <el-input v-model="step.update_field" label-width="150px"></el-input>
            </el-form-item>
            <el-form-item label="包括删除状态的字段" label-width="150px">
              <el-input v-model="step.delete_field"></el-input>
            </el-form-item>
            <el-form-item label="包括读状态的字段" label-width="150px">
              <el-input v-model="step.read_field"></el-input>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>

    <div slot="fotter" class="dialog-footer plugin_footer">
      <el-button @click="getFieldInfo()" size="mini">获取字段</el-button>
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button type="primary" @click="submit('step')" size="mini">确 定</el-button>
    </div>

    <!--    数据源滚管理-->
    <el-dialog :visible.sync="build" class="abow_dialog" width="70%" :title="dataSourceTitle" :append-to-body="true">
      <data-source-manage :key="dataSourcekey" ref="dataSourceView"></data-source-manage>
    </el-dialog>
  </div>
</template>
<script>
import util from "../../../../common/utils";
import store from "../../../../vuex/store";
import dataSourceManage from '../../../common/DataSourceManager';


import codeMirror from "../../../common/codemirror.vue";
import vPinyin from "../../../../py/vue-py.js";
import sqlFormatter from "sql-formatter";
import {getSchema, getTables, getColumns, getConnection} from "../../../../api/api.js";
import tableView from "../../../common/TableViews";
import {addConnection} from "../../../../common/common";

export default {
  data() {
    return {
      showFields:[],
      curPage:1,
      pageSize:10,
      ReferRe: [{}, {}, {}],
      dis: true,
      build: false,
      dataSourcekey: null,
      dataSourceTitle: '新增连接',
      tableKey: null,
      active: "normal",
      cluster_parameter: [],
      Named_parameters: [],
      Connectpool_parameter: [],
      dataSources: [],

      step: {
        name: "执行SQL脚本",
        type: "ExecSQL",
        description: [],
        distribute: "Y",
        custom_distribution: [],
        copies: "1",
        partitioning: {
          method: "none",
          schema_name: []
        },
        fields: {
          field: []
        },
        connection: "",
        execute_each_row: false,
        single_statement: false,
        replace_variables: false,
        quoteString: false,
        BindString: "" /**绑定字符串 */,
        sql: "",
        set_params: false,
        insert_field: "",
        update_field: "",
        delete_field: "",
        read_field: "",
        arguments: {
          argument: []
        },
        attributes: [],
        cluster_schema: [],
        remotesteps: {
          input: "\n      ",
          output: "\n      "
        },
        GUI: {
          xloc: "160",
          yloc: "224",
          draw: "Y"
        },
        // 新建对话框
        connecName: "",
        bool: true,
        time_stamp: true,
        identifier: false,
        identifier_captial: false,
        identifier_lowercase: false,
        preserve_case: true,
        strict_number: false,
        defalut_mode: "",
        Excute_sql: "",
        Connectpool: false,
        connect_size: "",
        free_space: "",
        descripe: "The default catalog of connections created by this pool.",
        cluster: false,
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
    dataSourceManage,
  },
  mounted() {
    this.init();
  },
  methods: {
    addRows() {
      this.step.arguments.argument.push({
        edit:false,
        name: '',
      })
      this.selectedPage();
    },
    deleteRow(index) { //移除一行
      // let newIndex = (this.curPage - 1) * this.pageSize + index
      this.step.arguments.argument.splice(index, 1); //删掉该行
      this.selectedPage();
    },
    editLine(row){
      this.step.arguments.argument.forEach((item, index) => {
        if (item.name === row.name) {
          // item.decimal='.'
          this.$set(this.step.arguments.argument[index], 'edit', true);
          this.$forceUpdate();
        }
      })
    },
    selectedPage() {
      this.showFields = [];
      let fields = [];
      fields = this.step.arguments.argument.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
      fields.forEach((item, index) => {
        this.showFields.push(item);
      });

    },

    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {
        });
    },
    changeValue() {
      if (this.step.execute_each_row) {
        (this.dis = false);
      } else {
        (this.dis = true);
      }
    },
    closeDialog() {
      util.$emit("closeDialog", "close");
      // 初始化画布数据
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
      this.step.arguments.argument = [];
      //获取当前步骤信息
      let params = {
        key: this.key,
        value: stepName,
      }
      let curStep = store.getters.getCurrentStep(params);
      let outFields = store.getters.generateOutputFields(params);
      // let laststeps = store.getters.getLastStep(param);
      // if (laststeps !== undefined && laststeps.length > 0) {
      //   this.laststep = laststeps[0];
      // }
      this.step['outFields'] = outFields;
      if (curStep.arguments!==undefined ) {
        this.step.name = curStep.name;
        console.log("connection    " + curStep.connection)

        this.step.connection = curStep.connection;
        this.step.sql = curStep.sql;
        this.step.limit = curStep.limit;
        this.step.execute_each_row = curStep.execute_each_row === 'Y';
        this.step.single_statement = curStep.single_statement === 'Y';
        this.step.replace_variables = curStep.replace_variables === 'Y';
        this.step.quoteString = curStep.quoteString === 'Y';
        let fields = JSON.parse(JSON.stringify(curStep.arguments.argument))
        fields.forEach(item=>{
          item.edit=false;
        })
        this.step.arguments.argument = fields;
        this.selectedPage();
        this.step.insert_field = curStep.insert_field;
        this.step.update_field = curStep.update_field;
        this.step.delete_field = curStep.delete_field;
        this.step.read_field = curStep.read_field;




      }
    },
    //获取字段
    getFieldInfo() {
      this.step.arguments.argument = [];
      let params = {
        key: this.key,
        value: this.step.name,
      }
      let outFields = store.getters.generateOutputFields(params);
      outFields.forEach(item => {
        this.step.arguments.argument.push({
          eidt:false,
          name: item.name
        })
      })
      this.selectedPage();
    },
    submit() {
      this.step.initFlag = true;
      let step = Object.assign({}, this.step);

      if (this.step.execute_each_row) {
        step.execute_each_row = "Y";
      } else {
        step.execute_each_row = "N";
      }

      if (this.step.single_statement) {
        step.single_statement = "Y";
      } else {
        step.single_statement = "N";
      }

      if (this.step.replace_variables) {
        step.replace_variables = "Y";
      } else {
        step.replace_variables = "N";
      }

      if (this.step.quoteString) {
        step.quoteString = "Y";
      } else {
        step.quoteString = "N";
      }

      if (this.step.header) {
        step.header = "Y";
      } else {
        step.header = "N";
      }
      if (this.step.noempty) {
        step.noempty = "Y";
      } else {
        step.noempty = "N";
      }
      if (this.step.stoponempty) {
        step.stoponempty = "Y";
      } else {
        step.stoponempty = "N";
      }
      step["oldStepName"] = this.oldStepName;
      // 控件重命名
      let paramName = step.name;
      let newName = store.getters.getCheckNodeName(
        this.key,
        this.oldStepName,
        paramName
      );
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
      addConnection(this.dataSources, this.step.connection, this.key);
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

</style>
