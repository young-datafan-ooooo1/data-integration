<template>
  <div>
    <div>
      <el-tabs type="card" active-name="advanced">
        <el-tab-pane label="基础属性" name="base">
          <el-form class="title_div" ref="form" :rules="rules" :model="step" label-width="80px">
            <el-form-item label="步骤名称:" prop="name">
              <el-input v-model="step.name"></el-input>
            </el-form-item>
            <el-form-item label="行数:" prop="rowCnt">
              <el-input readonly v-model="step.rowCnt"></el-input>
            </el-form-item>
            <el-form-item label="列数:" prop="fieldCnt">
              <el-input readonly v-model="step.fieldCnt"></el-input>
            </el-form-item>
            <el-form-item label="说明:" prop="description">
              <el-input type="textarea" v-model="step.description"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="列属性" name="advanced">
          <!-- 第二个标签页 -->
          <div class="plugin_body">
            <el-table :data="showFields" border style="width: 100%" :header-cell-style="{background:'#eef1f6'}">
              <el-table-column type="index" label="序号" width="50">
              </el-table-column>
              <el-table-column prop="nameCn" label="列名称" width="180">
              </el-table-column>
              <el-table-column prop="nameCn" label="列说明" width="180">
              </el-table-column>
              <el-table-column prop="field_sum_dsc" label="列数值统计说明">
              </el-table-column>
            </el-table>
          </div>
          <el-pagination style="float: right" class="right" @current-change="selectedPage" :current-page.sync="curPage"
            @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 20, 30, 40,step.fields.field.length]"
            layout="total, sizes, prev, pager, next" :total="step.fields.field.length">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </span>
    </div>
  </div>
</template>

<script>
  import util from "../../../../common/utils"
  import vPinyin from '../../../../py/vue-py.js'
  import store from '../../../../vuex/store.js'

  export default {
    data() {
      return {
        key: '', //插件对应的画布
        nodeData: '',
        name: '',
        flag: true, //选项卡切换
        modelName: '',
        data: '',
        oldStepName: '',
        modelMetaData: [],
        curPage: 1,
        pageSize: 10,
        showFields: [],
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          fieldCnt: [{
            required: true,
            message: '',
            trigger: 'blur'
          }],
          rowCnt: [{
            required: true,
            message: '',
            trigger: 'blur'
          }]
        },
        step: {
          rowCnt: -1,
          fieldCnt: null,
          name: '',
          type: "CustomizeModelInput",
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
          connection: "clickhouse",
          sql: '',
          limit: 0,
          lookup: "",
          execute_each_row: "N",
          variables_active: "N",
          lazy_conversion_active: "N",
          model_name: "数据类型测试模型",
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
      }
    },
    mounted() {
      this.init();

    },
    computed: {},
    methods: {
      selectedPage() {
        this.showFields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize)
      },

      closeDialog() {
        util.$emit('closeDialog', 'close');
        // 初始化画布数据
      },
      clicks() {
        this.name = vPinyin.chineseToPinYin(this.name);
      },
      init() {
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        // 获取当前步骤信息参数
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        }
        let curStep = store.getters.getCurrentStep(param);
        this.step.name = curStep.name;
        this.step.type = curStep.type;
        this.step.modelName = curStep.modelName;
        this.step.description = curStep.description;
        this.step.distribute = curStep.distribute;
        this.step.custom_distribution = curStep.distribute;
        this.step.copies = curStep.copies;
        this.step.partitioning = curStep.partitioning;
        this.step.isBusiness = curStep.isBusiness;
        this.step.connection = curStep.connection;
        this.step.sql = curStep.sql;
        this.step.limit = curStep.limit;
        this.step.lookup = curStep.lookup;
        this.step.execute_each_row = curStep.execute_each_row;
        this.step.variables_active = curStep.variables_active;
        this.step.lazy_conversion_active = curStep.lazy_conversion_active;
        this.step.model_name = curStep.model_name;
        this.step.attributes = curStep.attributes;
        this.step.cluster_schema = curStep.cluster_schema;
        this.step.remotesteps = curStep.remotesteps;
        this.step.GUI = curStep.GUI;
        this.step.fields.field = curStep.fields.field;
        this.step.fieldCnt = this.step.fields.field.length;
        // 获取上一步步骤信息
        let parameter = {
          key: this.key,
          value: this.step.name,
        }

        let lastStep = store.getters.getLastStep(parameter);
        this.showFields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize)


      },
      submit() {
        this.step['oldStepName'] = this.oldStepName;

        //校验是否有重复名称并获取重命名之后的名称
        let paramName = this.step.name;
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
        let flag = false;
        if (this.step.name !== newName) {
          flag = true;
          this.step.name = newName;
        }

        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: this.step
          }
        }
        // 修改krt节点信息
        store.dispatch('updateStepInfo', param);
        let params = {
          id: this.nodeData.id, //插件id
          label: this.step.name,
          oldStepName: this.oldStepName
        }

        util.$emit('updateNode', params);
        util.$emit('closeDialog', 'close');

        if (flag) {
          this.$alert(paramName + ' 名称已存在,重命名为 ' + newName, '注释', {
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

  .title_span {
    margin-right: 20%;
    margin-top: 20px;

  }

  .title_text {
    margin-left: 0;
    margin-right: 100px;

  }

  .title_div {
    margin-left: 100px;
    margin-right: 100px;
  }
</style>
