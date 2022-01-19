<template>
  <div class="plugin_body">
    <div class="div_form plugin_content plugin_form">
      <el-form :model="step" label-width="30%" label-position="right" size="mini">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" size="mini" style="width: 65%"></el-input>
        </el-form-item>
        <el-form-item label="要作为文件名的输入字段" prop="accept_field">
          <el-select v-model="step.accept_field" size="mini" style="width: 65%" clearable>
            <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div class="node_title_basic">
        <span>从文件中选择的字段:</span>
        <span style="float: right">
          <el-button size="mini" type="primary">获取字段</el-button>
        </span>
      </div>
      <el-table :data="step.field" height="300" :header-cell-style="{background:'#eef1f6'}">
        <el-table-column label="源字段">
          <template slot-scope="scope">
            <el-select size="mini" clearable v-model="scope.row.name">
              <el-option v-for="item in fields" :key="item.name" :label="item.name" :value="item.name"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="重命名为">
          <template slot-scope="scope">
            <el-input v-model="scope.row.rename" size="mini"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="类型">
          <template slot-scope="scope">
            <el-select v-model="scope.row.type" clearable size="mini">
              <el-option v-for="item in types" :label="item" :key="item" :value="item"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="转换掩码">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.conversion_mask"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="长度">
          <template slot-scope="scope">
            <el-input size="mini" type="number" v-model="scope.row.length"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="精度">
          <template slot-scope="scope">
            <el-input size="mini" type="number" v-model="scope.row.precision"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="十进制">
          <template slot-scope="scope">
            <el-input size="mini" type="number" v-model="scope.row.decimal"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="分组">
          <template slot-scope="scope">
            <el-input size="mini"  v-model="scope.row.group"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="去空格类型">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.trim_type" clearable>
              <el-option v-for="item in trimType" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import store from "../../../../vuex/store";
  import util from "../../../../common/utils";

  export default {
    name: "SASInput",
    data(){
      return{
        fields:[],
        nodeData:'',
        key:'',
        oldStepName:'',
        trimType:["none", "left", "right", "both"],
        types:[
          "Number",
          "String",
          "Date",
          "Boolean",
          "Integer",
          "BigNumber",
          "Timestamp",
        ],
        step:{
          name: "SAS 输入",
          type: "SASInput",
          distribute: "Y",
          copies: "1",
          partitioning: { "method": "none" },
          accept_field: "sd",
          field:[{
            name: "source",
            rename: "rename",
            type: "Binary",
            length: "-1",
            precision: "10",
            conversion_mask: "yanma",
            decimal: "10j",
            grouping: "group",
            trim_type: "left"
          }],
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "624",
            yloc: "80",
            draw: "Y"
          }
        }
      }
    },
    mounted() {
      this.init();
    },
    methods:{
      init(){
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let params = {
          key: this.key,
          value: this.step.name,
        }

        //获取当前步骤信息
        let curStep = store.getters.getCurrentStep(params);
        if (curStep !== undefined && curStep.field !== undefined) {
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            } else if (curStep[key] === 'N' && key !== 'type') {
              this.step[key] = false;
            } else {
              if (key === 'field') {
              } else {
                this.step[key] = curStep[key];
              }
            }
          }
          if (this.step.field !== undefined) {
            this.step.field = [];
            let fields = curStep.field;
            fields.forEach(item => {
              this.step.field.push(item);
            })
          }

        }
        let outFields = store.getters.generateOutputFields(params);
        this.step['outFields'] = outFields;
      },
      closeDialog() {
        util.$emit("closeDialog", "close");
      },
      submit(){
        let step = Object.assign({}, this.step);
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, step.name);
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

<style scoped>

</style>
