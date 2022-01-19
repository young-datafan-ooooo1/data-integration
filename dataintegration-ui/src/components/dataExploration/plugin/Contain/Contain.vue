<template>
  <div class="plugin_body">
    <div style="height: 90%;margin-top: 0px">
      <el-tabs type="card" active-name="advanced">
        <el-tab-pane label="基础属性" name="base">
          <el-form class="title_div" :rules="rules" ref="step" :model="step" label-width="80px">
            <el-form-item label="步骤名称:" prop="name">
              <el-input v-model="step.name"></el-input>
            </el-form-item>
            <el-form-item label="类型:" prop="stepType">
              <el-input readonly v-model="step.stepType"></el-input>
            </el-form-item>
            <el-form-item label="说明:" prop="describe">
              <el-input type="textarea" v-model="step.describe"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="关联设置" name="advanced">
          <div><span>表和排除关系</span></div>
          <div style="margin-top: 5px">
            <div class="relation">
              <el-select v-model="step.step1" clearable @change="selectLeft" placeholder="表1" style="width: 39%">
                <el-option v-for="item in lastSteps" :key="item.name" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
              <el-select v-model="step.join_type" clearable placeholder="条件" style="width: 20%">
                <el-option v-for="item in joinTypes" :key="item.name" :label="item.dsc" :value="item.name">
                </el-option>
              </el-select>
              <el-select v-model="step.step2" clearable @change="selectRight" placeholder="表2" style="width: 39%">
                <el-option v-for="item in lastSteps" :key="item.name" :label="item.name" :value="item.name">
                </el-option>
              </el-select>
            </div>
          </div>
          <div style="margin-top: 5px">
            <span style="float:left;padding-top: 10px">关联字段</span>
            <el-button style="float:right;margin-bottom: 5px" type="primary" size="mini" @click="addRow">新增</el-button>
          </div>
          <el-table :data="joinConditions" border style="width: 100%;margin-top: 10px" height="270" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column type="index" label="序号" width="50">
            </el-table-column>
            <el-table-column prop="keys_1" label="表1字段">
              <template slot-scope="scope">
                <el-select v-model="scope.row.keys_1" @change="checkFieldsTypeL(scope.row)" clearable placeholder="选择字段"
                  size="mini" style="width: 100%">
                  <el-option v-for="item in leftFields" :key="item.name" :label="item.nameCn" :value="item.name">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="condition" label="条件" width="120">
              <template slot-scope="scope">
                <el-select v-model="scope.row.condition" clearable placeholder="选择条件" size="mini" style="width: 100%">
                  <el-option v-for="item in relations" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="keys_2" label="表2字段">
              <template slot-scope="scope">
                <el-select v-model="scope.row.keys_2" @change="checkFieldsTypeL(scope.row)" clearable placeholder="选择字段"
                  size="mini" style="width: 100%">
                  <el-option v-for="item in rightFields" :key="item.name" :label="item.nameCn" :value="item.name">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100px">
              <template slot-scope="scope">
                <el-button type="text" size="mini" :disabled="scope.$index===0" @click="moveUp(scope.$index,scope.row)"><i
                    class="el-icon-arrow-up"></i></el-button>
                <el-button type="text" size="mini" :disabled="scope.$index===(joinConditions.length-1)" @click="moveDown(scope.$index,scope.row)"><i
                    class="el-icon-arrow-down"></i></el-button>
                <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div style="margin-top: 10px">
      <span slot="footer" class="dialog-footer" style="float: right">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="submit('step')">确 定</el-button>
      </span>
    </div>
  </div>
</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store.js";

  export default {

    data() {
      return {
        joinConditions: [{
          keys_1: '',
          keys_2: '',
          condition: ''
        }],
        leftFields: [],
        rightFields: [],
        relations: ["=", ">=", "<=", "<>", ">", "<"],
        step: {
          name: '',
          stepType: '包含控件',
          type: 'Contain',
          modelName: "",
          description: "",
          initFlag: true,
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          isBusiness: "Y",
          join_type: "IN",
          step1: "",
          step2: "",
          describe: "",
          keys_1: [],
          keys_2: [],
          conditions: [],
          attributes: "",
          fields: {
            field: []
          },
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 544,
            yloc: 192,
            draw: "Y"
          }
        },

        key: '', //插件对应的画布
        nodeData: '',
        lastSteps: [],
        joinTypes: [{
          name: "IN",
          dsc: "包含"
        }, {
          name: "NOT IN",
          dsc: "不包含"
        }],
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          stepType: [{
            required: true,
            message: '',
            trigger: 'blur'
          }]
        }
      }
    },
    mounted() {
      this.init();
    },
    methods: {
      closeDialog() {

        util.$emit('closeDialog', 'close');
      },
      deleteRow(index) { //移除一行
        this.joinConditions.splice(index, 1); //删掉该行
      },
      addRow(event) { //新增一行
        this.joinConditions.push({
          keys_1: '',
          keys_2: '',
          condition: '='
        });
      },
      moveUp(index, row) {
        var that = this;
        if (index > 0) {
          let upDate = that.joinConditions[index - 1];
          that.joinConditions.splice(index - 1, 1);
          that.joinConditions.splice(index, 0, upDate);
        } else {
          this.$message({
            message: '已经是第一条，不可上移',
            type: 'error'
          });

        }
      },

      //下移
      moveDown(index, row) {
        var that = this;
        if ((index + 1) === that.joinConditions.length) {
          this.$message({
            message: '已经是最后一条，不可下移',
            type: 'error'
          });
        } else {
          let downDate = that.joinConditions[index + 1];
          that.joinConditions.splice(index + 1, 1);
          that.joinConditions.splice(index, 0, downDate);
        }
      },

      checkFieldExits(fieldName) {
        for (let i = 0; i < this.step.fields.field.length; i++) {
          if (fieldName === this.step.fields.field[i].name) {
            return true;
          }
        }
        return false;
      },
      pushFields(item) {

        item.fields.field.forEach((item1, index) => {
          let fieldName = item1.name;
          let fieldNameCn = item1.nameCn;
          let ret = 1;
          while (this.checkFieldExits(fieldName)) {
            fieldName = item1.name;
            fieldNameCn = item1.nameCn;
            fieldName = fieldName + '_' + ret;
            fieldNameCn = fieldNameCn + '_' + ret;
            ret++;
          }

          this.step.fields.field.push({
            name: fieldName,
            nameCn: fieldNameCn,
            type: item1.type,
            format: item1.format,
            currency: item1.currency,
            decimal: item1.decimal,
            group: item1.group,
            nullif: item1.nullif,
            trim_type: item1.name,
            length: item1.length,
            precision: item1.precision,
            description: item1.description
          });

        })
      },
      submit(step) {
        this.$refs[step].validate((valid) => {
          if (valid) {
            this.step['oldStepName'] = this.oldStepName;
            this.step.keys_1 = [];
            this.step.keys_2 = [];
            this.step.conditions = [];
            this.step.fields.field = [];
            this.joinConditions.forEach((item, index) => {
              this.step.keys_1.push({
                key: item.keys_1
              });
              this.step.keys_2.push({
                key: item.keys_2
              });
              this.step.conditions.push({
                condition: item.condition
              });
            });

            this.step.initflag = true;

            let obj1 = {};
            obj1 = this.lastSteps.find((item) => {
              return item.name === this.step.step1; //筛选出匹配数据
            });
            this.pushFields(obj1);

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
              this.$alert(paramName + '名称已存在,重命名为' + newName + "!", '注释', {
                confirmButtonText: '确定',
              });
            } else {
              this.$message({
                message: '步骤信息修改成功',
                type: 'success'
              })
            }
          } else {
            return false;
          }
        })
      },
      selectLeft() {
        this.leftFields = [];
        let selStep = undefined;
        if (this.step.step1 !== null && this.step.step1 !== '') {
          this.lastSteps.forEach((item, index) => {
            if (item.name === this.step.step1) {
              selStep = item;
            }
          });
        }
        if (selStep !== undefined) {
          let selStepFields = selStep.fields.field;
          selStepFields.forEach((item, index) => {
            this.leftFields.push(item);
          });
        }


      },
      selectRight() {
        this.rightFields = [];
        let selStep = undefined;
        if (this.step.step2 !== null && this.step.step2 !== '') {
          this.lastSteps.forEach((item, index) => {
            if (item.name === this.step.step2) {
              selStep = item;
            }
          });
        }
        if (selStep !== undefined) {
          let selStepFields = selStep.fields.field;
          selStepFields.forEach((item, index) => {
            this.rightFields.push(item);
          });
        }

      },
      checkFieldsTypeL(row) {
        let lType = undefined;
        let rType = undefined;

        this.leftFields.forEach((item, index) => {
          if (row.keys_1 === item.name) {
            lType = item.type;
          }
        });
        this.rightFields.forEach((item, index) => {
          if (row.keys_2 === item.name) {
            rType = item.type
          }
        });


        if (lType !== undefined && lType !== '' && rType !== undefined && rType !== '' && lType !== rType) {
          this.$message({
            message: '数据类型不匹配',
            type: 'error'
          });
        }

      },
      checkFieldsTypeR(row) {

      },
      init() {
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        };
        this.lastSteps = store.getters.getLastStep(param);


        let obj = store.getters.getCurrentStep(param)
        let curstep = {};
        Object.assign(curstep, obj);

        if (curstep !== undefined && curstep !== null && curstep.step1 !== undefined) {
          this.step.describe = curstep.describe
          this.step.step1 = curstep.step1;
          this.step.step2 = curstep.step2;
          this.step.join_type = curstep.join_type;
          this.joinConditions = [];
          for (let i = 0; i < curstep.keys_1.length; i++) {
            this.joinConditions.push({
              keys_1: curstep.keys_1[i].key,
              keys_2: curstep.keys_2[i].key,
              condition: curstep.conditions[i].condition,
            })
          }
          this.selectRight();
          this.selectLeft();
        }
      }
    }
  }
</script>

<style scoped>
  .title_div {
    margin-left: 100px;
    margin-right: 100px;
  }

  .relation {
    text-align: center;
    margin: 0 auto;
  }
</style>
