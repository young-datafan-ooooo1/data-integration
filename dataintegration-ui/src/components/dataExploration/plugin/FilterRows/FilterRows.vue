<template>
  <div class="plugin_body">
    <div class="plugin_content">
      <el-form ref="step" :rules="rules" :model="step" label-width="30%" label-position="right">
        <el-form-item label="控件名称:" prop="name">
          <el-input v-model="step.name" style="width: 65%" size="mini"></el-input>
        </el-form-item>
      </el-form>
      <div class="node_title_basic">
        <span class="start-css">*</span>&nbsp;<span>过滤设置</span>
        <el-button type="primary" size="mini" @click="addRow" style="float:right;margin-bottom: 5px">新增</el-button>
      </div>
      <div class="table_visiable">
        <el-table :data="conditionData" height="350px"
                  :header-cell-style="{background:'#FAFAFA'}" row-key="id" default-expand-all
                  :tree-props="{children: 'children'}">
          <el-table-column  label="左边字段" width="180px">
            <template slot-scope="scope">
              <el-select v-model="scope.row.leftvalue" clearable filterable allow-create
                         allow-createplaceholder="选择字段" size="mini" style="width: 75%"
                         @change="changeLeftValue(scope,scope.row.leftvalue)">
                <el-option v-for="item in step.outFields" :key="item.name" :label="item.nameCn" :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="function" label="操作符" width="80">
            <template slot-scope="scope">
              <el-select v-model="scope.row.function" placeholder="选择操作符" size="mini" >
                <el-option v-for="item in functions" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="value.text" label="固值" width="160" >
            <template slot-scope="scope">
              <el-input v-model="scope.row.value.text"
                        :disabled="scope.row.rightvalue!=='' && scope.row.rightvalue!==null&&scope.row.rightvalue!==undefined"
                        @blur="updateValue(scope.row)" size="mini"
                        @click.native="setValue(scope.row,scope.$index)" :readonly="true" ></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="rightvalue" label="右边字段" width="100">
            <template slot-scope="scope">
              <el-select
                :disabled="scope.row.value.text!=='' && scope.row.value.text!==null"
                clearable filterable allow-create v-model="scope.row.rightvalue" placeholder="选择字段" size="mini"
                @change="changeLeftValue(scope,scope.row.rightvalue)">
                <el-option v-for="item in step.outFields" :key="item.name" :label="item.nameCn" :value="item.name">
                </el-option>
              </el-select>
            </template>
          </el-table-column>

          <el-table-column prop="negated" label="IS NOT" width="100">
            <template slot-scope="scope">
              <el-select v-model="scope.row.negated" size="mini">
                <el-option v-for="item in operations" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="negated" label="与上一条件关系" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.operator" size="mini" clearable :disabled="scope.$index === 0">
                <el-option v-for="item in operas" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100px">
            <template slot-scope="scope">
              <!--              <el-button type="text" size="mini" :disabled="scope.$index===0" @click="moveUp(scope.$index,scope.row)"><i-->
              <!--                class="el-icon-arrow-up"></i></el-button>-->
              <!--              <el-button type="text" size="mini"-->
              <!--                         :disabled="scope.$index===(step.compare.condition.conditions.condition.length-1)"-->
              <!--                         @click="moveDown(scope.$index,scope.row)"><i class="el-icon-arrow-down"></i></el-button>-->
              <el-button size="mini" type="text" v-if="scope.row.level==='parent'" @click="addSubRow(scope,scope.$index)">新增</el-button>
              <el-button size="mini" type="text" @click="deleteRow(scope,scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

    </div>

    <el-dialog class="dialog_temp other_dialog" width="30%" title="设置值" :visible.sync="valueViaiable" :before-close="closeValue" :append-to-body="true" :modal-append-to-body="true">
      <el-form :mode="valueForm" size="mini" label-width="80px" label-position="right">
        <el-form-item label="类型"  prop="type">
<!--          <el-input v-model="valueForm.type"></el-input>-->
          <el-select v-model="valueForm.type" clearable style="width: 100%">
            <el-option v-for="item in fieldTypes" :key="item" :lable="item" :value="item" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="值" prop="text">
          <el-input v-model="valueForm.text" v-if="valueForm.type !=='Date' && valueForm.type!=='Timestamp'"></el-input>
          <el-date-picker clearable
                          style="width: 100%"
                          v-model="valueForm.text"
                          type="date"
                          size="mini"
                          format="yyyy-MM-dd"
                          placeholder="选择日期" v-if="valueForm.type === 'Date'">
          </el-date-picker>
          <el-date-picker clearable
                          style="width: 100%"
                          v-model="valueForm.text"
                          type="datetime"
                          size="mini"
                          placeholder="选择日期时间" clearable v-if="valueForm.type === 'Timestamp'">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="格式" prop="mask">
<!--          <el-input v-model="valueForm.mask"></el-input>-->
          <el-select v-model="valueForm.mask" clearable filterable allow-create style="width: 100%">
            <el-option v-for="item in formatDate" :key="item" :label="item" :value="item" v-if="valueForm.type ==='Date'">
            </el-option>
            <el-option v-for="item in formatNumber" :key="item" :label="item" :value="item" v-if="valueForm.type ==='Number'">
            </el-option>
            <el-option v-for="item in formats" :key="item" :label="item" :value="item" v-if="valueForm.type !=='Date'&&valueForm.type!=='Timestamp'&& valueForm.type!=='Number'">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="长度" prop="length">
          <el-input v-model="valueForm.length"></el-input>
        </el-form-item>
        <el-form-item label="精度" prop="precision">
          <el-input v-model="valueForm.precision"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="default" size="mini" @click="closeValue">取消</el-button>
        <el-button size="mini" @click="setConiditionValue" type="primary" :loading="valueLoading">确定</el-button>
      </div>
    </el-dialog>

    <div class="dialog-footer plugin_footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="submit('step')">确 定</el-button>
    </div>

  </div>
</template>

<script>
import util from "../../../../common/utils";
import store from "../../../../vuex/store.js";
import formatDate from '../../../../common/date.js'

export default {

  mounted() {
    this.init();
  },

  data() {
    return {
      formats:[],
      formatDate:[],
      formatNumber:[],
      valueLoading:false,
      conditionData:[],
      valueForm:{
        name:'',
        type:'',
        text:'',
        mask:'',
        length:'',
        precision:'',
        isNull:'N',
      },
      valueViaiable:false,
      key: '',
      nodeData: '',
      fieldTypes:["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"],
      systemParams: [{
        name: "batchDay",
        desc: "批量日期(前一天，格式：yyyy-MM-dd)",
        expression: "date_to_string(addDate(sysdate(),5,-1),\"yyyy-MM-dd\")",
        formatSting: "yyyy-MM-dd"
      },
        {
          name: "batchDateTime",
          desc: "批量日期时间戳(前一天，格式：yyyy-MM-dd HH:mm:ss)",
          expression: "date_to_string(addDate(sysdate(),5,-1),\"yyyy-MM-dd HH:mm:ss\")",
          formatSting: "yyyy-MM-dd HH:mm:ss"
        },
        {
          name: "currentDay",
          desc: "当前日期(格式：yyyy-MM-dd)",
          expression: "date_to_string(sysdate(),\"yyyy-MM-dd\")",
          formatSting: "yyyy-MM-dd"
        },
        {
          name: "currentDateTime",
          desc: "当前日期时间戳(格式：yyyy-MM-dd HH:mm:ss)",
          expression: "date_to_string(sysdate(),\"yyyy-MM-dd HH:mm:ss\")",
          formatSting: "yyyy-MM-dd HH:mm:ss"
        }
      ],
      lastStepFields: [],
      lastStep: {},
      functions: ["=", "<>", "<", "<=", ">", ">=", "REGEXP", "IS NULL", "IS NOT NULL", "IN LIST", "CONTAINS",
        "STARTS WITH", "ENDS WITH", "LIKE", "TRUE"
      ],
      operations: ["N", "Y"],
      operas:["AND","OR","OR NOT","AND NOT","XOR"],
      step: {
        initFlag: true,
        name: "过滤插件",
        type: "FilterRows",
        modelName: "",
        description: [
          "",
          ""
        ],
        distribute: "Y",
        custom_distribution: "",
        copies: 1,
        partitioning: {
          method: "none",
          schema_name: ""
        },
        isBusiness: "Y",
        send_true_to: "",
        send_false_to: "",
        compare: {
          condition: {
            negated: "N",
            conditions: {
              condition: [
              ]
            }
          }
        },
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
          xloc: 416,
          yloc: 288,
          draw: "Y"
        }
      },
      rules: {
        name: [{
          required: true,
          message: '步骤名称不能为空',
          trigger: 'blur'
        }],
      },
      index:0,
    }
  },

  methods: {

    changeDate(scope, data) {

    },
    setValue(row,index){
      this.valueForm = row.value;
      if(this.valueForm.type==='Date'&&this.valueForm.text!==''){
        this.valueForm.text= formatDate.formatDate.parse(this.valueForm.text,'yyyy-MM-dd')
      }else if(this.valueForm.type==='Timestamp'&&this.valueForm.text!==''){
        this.valueForm.text= formatDate.formatDate.parse(this.valueForm.text,'yyyy-MM-dd hh:mm:ss')
      }
      this.index = index;
      this.valueViaiable = true;
    },

    closeValue(){
      if(this.valueForm.type==='Date'&&this.valueForm.text!==''){
        this.valueForm.text= formatDate.formatDate.format(this.valueForm.text,'yyyy-MM-dd')
      }else if(this.valueForm.type==='Timestamp'&&this.valueForm.text!==''){
        this.valueForm.text= formatDate.formatDate.format(this.valueForm.text,'yyyy-MM-dd hh:mm:ss')
      }
      this.valueViaiable = false;
    },
    setConiditionValue(){
      this.conditionData.value = this.valueForm;
      if(this.valueForm.type==='Date'&&this.valueForm.text!==''){
        this.valueForm.text= formatDate.formatDate.format(this.valueForm.text,'yyyy-MM-dd')
      }else if(this.valueForm.type==='Timestamp'&&this.valueForm.text!==''){
        this.valueForm.text= formatDate.formatDate.format(this.valueForm.text,'yyyy-MM-dd hh:mm:ss')
      }
      this.valueViaiable = false;
    },
    /**
     * 新增子条件
     */
    addSubRow(scope,index){
      scope.row.children.push({
        level:scope.row.id,
        id:this.getUUID(),
        negated: "N",
        leftvalue: "",
        operator: "AND",
        function: "=",
        rightvalue: "",
        value: {
          name: "constant",
          type: "",
          text: "",
          length: "-1",
          precision: "-1",
          isnull: "",
          mask: ""
        },
      })
    },

    /**
     * 改变字段是触发
     * @param data
     */
    changeLeftValue(data, column) {
      data.row.value.text = '';
      let fields = [];
      fields = this.step.outFields;
      fields.forEach((item, index) => {
        if (item.name === column) {
          if (item.type.toUpperCase() === ('date').toUpperCase()) {
            data.row.value.name = column;
            data.row.value.type = item.type;
          } else if (item.type.toUpperCase() === ('timestamp').toUpperCase()) {
            data.row.value.name = column;
            data.row.value.type = item.type;
          } else {
            data.row.value.name = column;
            data.row.value.type = item.type;
          }
        }
      })
    },


    formatter() {
      let fields = this.conditionData;
      let fieldData = this.lastStepFields;
      fields.forEach((item, index) => {
        fieldData.forEach((item1, index1) => {
          if (item1.name === item.name) {
            if (item.type.toUpperCase() === ('date').toUpperCase()) {
            }
          } else {

          }
        })
      })
    },

    deleteRow(scope,index) { //移除一行
      console.info(index,scope.row);
      if(scope.row.level==='parent'){
        this.conditionData.forEach((item,i)=>{
          if(item.id === scope.row.id){
            this.conditionData.splice(i, 1);
          }
        })
         //删掉该行
      }else{
        let conditionData=[];
        this.conditionData.forEach(item=>{
          if(item.id === scope.row.level){
            conditionData=item.children;
          }
        })
        conditionData.forEach((item,index)=>{
          if(item.id === scope.row.id){
            conditionData.splice(index,1);
          }
        })
      }

    },
    getUUID() {
      let s = [];
      let hexDigits = "0123456789abcdefhijklmnopqrstuzwxyz";
      for (let i = 0; i < 12; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
      }
      return s.join("");
    },
    addRow(event) { //新增一行
      if (this.conditionData.length >= 1) {
        this.conditionData.push({
          level:'parent',
          id:this.getUUID(),
          negated: "N",
          leftvalue: "",
          operator: "AND",
          function: "=",
          rightvalue: "",
          value: {
            name: "constant",
            type: "",
            text: "",
            length: "-1",
            precision: "-1",
            isnull: "",
            mask: ""
          },
          children:[],
        });
      } else {
        this.conditionData.push({
          level:'parent',
          id:this.getUUID(),
          negated: "N",
          leftvalue: "",
          function: "=",
          rightvalue: "",
          value: {
            name: "constant",
            type: "",
            text: "",
            length: "-1",
            precision: "-1",
            isnull: "",
            mask: ""
          },
          children:[],
        });
      }

    },
    changeParmSyle(row) {
      let sparamKey = row.value.sparamKey;
      row.value.paramKey = '${' + sparamKey + '}'
    },
    moveUp(index, row) {
      var that = this;
      if (index > 0) {
        let upDate = that.step.compare.condition.conditions.condition[index - 1];
        that.step.compare.condition.conditions.condition.splice(index - 1, 1);
        that.step.compare.condition.conditions.condition.splice(index, 0, upDate);
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
      console.log('下移', index, row);
      if ((index + 1) === that.step.compare.condition.conditions.condition.length) {
        this.$message({
          message: '已经是最后一条，不可下移',
          type: 'error'
        });
      } else {
        console.log(index);
        let downDate = that.step.compare.condition.conditions.condition[index + 1];
        that.step.compare.condition.conditions.condition.splice(index + 1, 1);
        that.step.compare.condition.conditions.condition.splice(index, 0, downDate);
      }
    },
    closeDialog() {

      util.$emit('closeDialog', 'close');
    },
    updateValue(row) {
      let obj = {};
      obj = this.lastStepFields.find((item) => { //这里的selectList就是上面遍历的数据源
        return item.name === row.leftvalue; //筛选出匹配数据
      });
      if (obj !== undefined) {
        row.value.name = obj.name;
        row.value.type = obj.type;
        row.value.length = obj.length;
        row.value.precision = obj.precision;
        row.value.formatString = 'yyyy-MM-dd hh:mm:ss';
      }
    },

    /**
     * 条件数据重新组合
     * @param Fields
     */
    changeFields(){
      let conditions=[];
      this.conditionData.forEach(item=>{
        if(item.children.length>0){
          let condition =[];
          let sunCondition={};
          // if (item.value.type.toUpperCase() === ('date').toUpperCase()) {
          //   if (typeof item.value.text === 'string') {
          //     let dt = formatDate.formatDate.parse(item.value.text, 'yyyy/MM/dd hh:mm:ss');
          //     item.value.text = formatDate.formatDate.format(dt, 'yyyy/MM/dd hh:mm:ss') + '.000';
          //   } else {
          //     item.value.text = formatDate.formatDate.format(item.value.text, 'yyyy/MM/dd hh:mm:ss') + '.000';
          //   }
          //
          // } else if (item.value.type.toUpperCase() === ('Timestamp').toUpperCase()) {
          //   if (typeof item.value.text === 'string') {
          //     let dt = formatDate.formatDate.parse(item.value.text, 'yyyy/MM/dd hh:mm:ss');
          //     item.value.text = formatDate.formatDate.format(dt, 'yyyy/MM/dd hh:mm:ss') + '.000';
          //   } else {
          //     item.value.text = formatDate.formatDate.format(item.value.text, 'yyyy/MM/dd hh:mm:ss') + '.000';
          //   }
          // }
          item.children.forEach(sub=>{
            // if (sub.value.type.toUpperCase() === ('date').toUpperCase()) {
            //   if (typeof sub.value.text === 'string') {
            //     let dt = formatDate.formatDate.parse(sub.value.text, 'yyyy/MM/dd hh:mm:ss');
            //     sub.value.text = formatDate.formatDate.format(dt, 'yyyy/MM/dd hh:mm:ss') + '.000';
            //   } else {
            //     sub.value.text = formatDate.formatDate.format(sub.value.text, 'yyyy/MM/dd hh:mm:ss') + '.000';
            //   }
            //
            // } else if (sub.value.type.toUpperCase() === ('Timestamp').toUpperCase()) {
            //   if (typeof sub.value.text === 'string') {
            //     let dt = formatDate.formatDate.parse(sub.value.text, 'yyyy/MM/dd hh:mm:ss');
            //     sub.value.text = formatDate.formatDate.format(dt, 'yyyy/MM/dd hh:mm:ss') + '.000';
            //   } else {
            //     sub.value.text = formatDate.formatDate.format(sub.value.text, 'yyyy/MM/dd hh:mm:ss') + '.000';
            //   }
            // }
            condition.push({
              negated: sub.negated,
              operator: sub.operator,
              leftvalue: sub.leftvalue,
              function: sub.function,
              value: {
                name: sub.value.name,
                type: sub.value.type,
                text: sub.value.text,
                length: sub.value.length,
                precision: sub.value.precision,
                isnull: 'N',
                mask: sub.mask
              }
            })
          })
          delete item.children
          condition.push(item);
          sunCondition['conditions']={
            negated:item.negated,
            operator:item.operator,
            condition:condition,
          }
          conditions.push(sunCondition)
        }else{
          delete item.children;
          conditions.push(item);
        }

      })
      return conditions;

    },
    /**
     * 生成表格数据
     */
    setTableData(){
      let conditionData =JSON.parse(JSON.stringify(this.step.compare.condition.conditions.condition));
      conditionData.forEach(item=>{
        if(item.conditions!==undefined){
          let tableData={};
          let condition = item.conditions.condition;
          condition.forEach((sub,index)=>{
            if(sub.level!==undefined && sub.level==='parent'){
              tableData=sub;
              tableData['children']=[];
              condition.splice(index,1);
            }
          })
          let children=[];
          condition.forEach((sub)=>{
            sub.level = tableData.id;
            sub.id = this.getUUID();
            children.push(sub);
          })
          tableData.children=children;
          this.conditionData.push(tableData);
        }else{
          item['children']=[];
          this.conditionData.push(item);
        }
      })
    },


    submit(step) {
      this.$refs[step].validate((valid) => {
        if (valid) {
          this.step['oldStepName'] = this.oldStepName;
          //条件重组
          let conditions = this.changeFields();
          this.step.compare.condition.conditions.condition = conditions;
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
        } else {
          console.log('error submit!!');
          return false;
        }
      });

    },
    getConstant() {
      this.$http.get('static/data.json').then(res => {
        this.formats = res.body.formatters.formats;
        this.formatDate = res.body.formatters.formatDate;
        this.formatNumber = res.body.formatters.formatNumber;
      })
    },
    init() {
      this.getConstant();
      this.step.name = this.nodeData.label;
      this.oldStepName = this.nodeData.label;
      let stepName = this.step.name;
      //获取当前步骤信息
      let params = {
        key: this.key,
        value: stepName,
      }
      let curstep = store.getters.getCurrentStep(params);
      let outFields = store.getters.generateOutputFields(params);
      this.step['outFields'] = outFields;

      this.step.compare.condition.conditions.condition=[];
      this.step.describes = curstep.describes;
      this.step.send_true_to = curstep.send_true_to;
      this.step.send_false_to = curstep.send_false_to;
      if (curstep !== undefined && curstep.compare !== undefined && curstep.compare.condition !== undefined &&
        curstep.compare.condition.conditions.condition !== undefined) {
        this.step.compare.condition.conditions.condition = JSON.parse(JSON.stringify(curstep.compare.condition.conditions.condition));
        let fields = this.step.compare.condition.conditions.condition;
        // fields.forEach((item, index) => {
        //   if (item.value.type.toUpperCase() === ('date').toUpperCase()) {
        //     item.value.text === new Date(item.value.text)
        //   }
        // })
        this.setTableData();
      }

    }
  }
}
</script>

<style>

</style>
