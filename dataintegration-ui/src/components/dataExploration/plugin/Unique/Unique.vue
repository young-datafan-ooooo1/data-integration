<template>
  <div class="plugin_body">
    <div class="plugin_content div_form">
      <el-form ref="step" :model="step" :rules="rules" label-width="30%" size="mini">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" size="mini" style="width: 65%;"></el-input>
        </el-form-item>
        <el-card class="box-card" style="margin: 0px;">
          <div style="margin-bottom: 10px;">设置</div>
          <el-form-item label="增加计数器到输出?">
            <el-checkbox v-model="step.count_rows" size="mini">计数器字段</el-checkbox>
            <el-input v-model="step.count_field" size="mini" :disabled="step.count_rows == false" style="width: 48%;"
                      clearable></el-input>
          </el-form-item>
          <el-form-item label="重定向重复记录">
            <el-checkbox v-model="step.reject_duplicate_row" size="mini">错误描述</el-checkbox>
            <el-input v-model="step.error_description" size="mini" style="width: 51%;"
                      :disabled="step.reject_duplicate_row == false"
                      clearable></el-input>
          </el-form-item>
        </el-card>
        <div style="margin-top: 10px;float:left;">用来比较的字段(没有条目意味着：比较现在完成了)</div>
        <div>
          <el-button style="margin-top:10px;float:right;margin-bottom: 5px;margin-right: 5px" type="primary"
                     @click="addRow"
                     size="mini">新增
          </el-button>
        </div>
        <el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
          <el-table-column type="index" label="#" width="50"></el-table-column>
          <el-table-column prop="name" label="字段名称">
            <template slot-scope="scope">
              <el-select v-model="scope.row.name" size="mini" filterable allow-create clearable>
                <el-option v-for="item in dataName" :key="item.name" :label="item.name" :value="item.name"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="case_insensitive" label="忽略大小写">
            <template slot-scope="scope">
              <el-select v-model="scope.row.case_insensitive" size="mini" style="width: 100%;" clearable>
                <el-option v-for="item in case_insensitive" :key="item.key" :label="item.label" :value="item.key">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100px">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
    <div slot="footer" class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button @click="getDialog" size="mini">获 取</el-button>
      <el-button type="primary" @click="submit" size="mini">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store.js";
  import {compareFields} from "../../../../assets/common/tool";

  export default {
    data() {
      return {
        key: '', //插件对应的画布
        nodeData: {},
        countvalue: true,
        record: true,
        dataName: [],
        case_insensitive: [{
          key: 'Y',
          label: '是'
        }, {
          key: 'N',
          label: '否'
        }],
        step: {
          name: "去除重复记录",
          type: "Unique",
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
          filename: "",
          ftp_username: "",
          ftp_password: "",
          count_rows: false,
          count_field: " ",
          reject_duplicate_row: false,
          error_description: " ",
          attributes: [],
          cluster_schema: [],
          remotesteps: {
            input: "\n      ",
            output: "\n      "
          },
          GUI: {
            xloc: 224,
            yloc: 176,
            draw: "Y"
          }
        },
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            tigger: 'blur'
          }]
        },
        uniqueData: []
      }
    },

    mounted() {
      this.init();
    },
    methods: {
      closeDialog() {
        util.$emit('closeDialog', 'close');
        // 初始化画3布数据
      },
      addRow(event) { //新增一行
        this.step.fields.field.push({
          name: '',
          case_insensitive: '',
        });
        this.curPage = parseInt(this.step.fields.field.length / this.pageSize + 1);
        //this.selectedPage();

      },

      deleteRow(index) { //移除一行
        this.step.fields.field.splice(index, 1); //删掉该行
      },


      getDialog() {
        this.step.fields.field = [];
        this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
        this.lastStepFields.forEach(item => {
          this.step.fields.field.push({
            name: item.name,
          })
        });
      },
      init() {
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        // 获取当前步骤信息参数
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        };

        let laststeps = store.getters.getLastStep(param);
        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
          console.log(this.laststep);
        }

        //生成本步骤输出字段
        let outFields = store.getters.generateOutputFields(param);
        compareFields(outFields);
        this.step['outFields'] = outFields;

        let obj = store.getters.getCurrentStep(param);
        let curstep = {};
        Object.assign(curstep, obj);

        if (obj.initValid == undefined) {

          if (this.laststep !== undefined) {
            this.lastStepFields = JSON.parse(JSON.stringify(outFields));
            this.lastStepFields.forEach(item => {
              this.dataName.push({
                name: item.name,
              })
            });
          }
        } else {
          this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
          this.lastStepFields = JSON.parse(JSON.stringify(outFields));
          this.lastStepFields.forEach(item => {
            this.dataName.push({
              name: item.name,
            })
          });
          console.log(JSON.stringify(obj));
          console.log(this.step.fields.field);

        }
        //判断是否是首次打开控件
        if (curstep !== undefined) {
          this.step.fileName = curstep.fileName;
          this.step.initFlag = curstep.initFlag;
          this.step.description = curstep.description;
          this.step.distribute = curstep.distribute;
          this.step.copies = curstep.copies;
          this.step.partitioning = curstep.partitioning;
          this.step.remotesteps = curstep.remotesteps;
          this.step.GUI = curstep.GUI;
          this.step.name = curstep.name;
          this.step.count_field = curstep.count_field;
          this.step.count_rows = curstep.count_rows === 'Y';
          this.step.reject_duplicate_row = curstep.reject_duplicate_row === 'Y';
          this.step.error_description = curstep.error_description;
        }

        for (let key in this.step) {
          if (this.step[key] == 'Y') {
            this.step[key] = true;
          } else if (this.step[key] == 'N') {
            this.step[key] = false;
          }
          console.log(key)
        }
      },

      changeStep() {
        for (let key in this.step) {
          // console.info(typeof  this.step[key]);
          if (typeof this.step[key] === 'boolean') {
            this.step[key] = this.step[key] === true ? 'Y' : "N"
          }
        }
      },
      submit() {
        this.step.initFlag = true;
        console.info("最终字段", this.step);
        this.step.initValid = false;
        this.changeStep();
        let step = Object.assign({}, this.step);
        console.info(this.step.count_rows);

        step['oldStepName'] = this.oldStepName;

        if (step.count_rows === 'Y') {
          step.outFields.push({
            name: step.count_field,
            type: 'Integer',
            length: -1,
            precision: -1,
            trim_type: "none",
            repeat: "N",
            format: "",
            currency: '',
            decimal: '',
            group: ''
          })
        }
        //新增字段
        // step.fields.field.forEach(item => {
        // 	step.outFields.push({
        // 		name: item.name,
        // 		type: item.type,
        // 		length: -1,
        // 		precision: -1,
        // 		trim_type: "none",
        // 		repeat: "N",
        // 		format: "",
        // 		currency: item.currency === '' ? '¥' : item.currency,
        // 		decimal: item.decimal === '' ? "." : item.decimal,
        // 		group: item.group === '' ? ',' : item.group
        // 	})
        // })

        // 控件重命名
        let paramName = step.name;
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
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
            step: this.step
          }
        };
        store.dispatch('updateStepInfo', param);
        let params = {
          id: this.nodeData.id, //插件id
          label: step.name,
          oldStepName: this.oldStepName,
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

    },
  }
</script>

<style>
  .el-card.is-always-shadow {
    webkit-box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
    box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
  }

  .el-card {
    margin-bottom: 15px;
  }

  .two {
    margin-top: 10px;
  }

  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }

  .el-card__body {
    padding: 10px;
  }
</style>
