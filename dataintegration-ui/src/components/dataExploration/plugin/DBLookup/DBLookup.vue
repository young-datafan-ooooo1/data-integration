<template>
  <div class="plugin_body">
    <div class="div_form plugin_content plugin_form">
      <el-form :model="step" ref="stepfrom" label-width="30%" size="mini">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
        <el-form-item label="数据库连接">
          <el-select v-model="step.connection" @change="SelectionChange" clearable style="width: 26%" >
            <el-option v-for="item in dataSources" :key="item.datasourceId" :label="item.dsName"
                       :value="item.datasourceId">
            </el-option>
          </el-select>
          <el-button type="primary" icon="el-icon-edit" @click="openConfig('edit')" :disabled="step.connection === ''">编辑</el-button>
          <el-button type="primary" icon="el-icon-plus" @click="openConfig">新建</el-button>
        </el-form-item>
        <el-form-item label="目标模式">
          <el-select v-model="step.lookup.schema" @change="handleSelectionChange" :disabled="schemas.length===0" clearable style="width: 65%">
            <el-option v-for="item in schemas" :value="item"
                       :label="item" :key="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标表" >
          <el-select v-model="step.lookup.table" clearable style="width: 65%">
            <el-option v-for="item in treeData" :key="item.value" :label="item.value" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="使用缓存" prop="cache">
          <el-checkbox v-model="step.cache"></el-checkbox>
        </el-form-item>
        <el-form-item label="缓存大小" prop="cache_size">
          <el-input type="number" v-model="step.cache_size" :disabled="step.cache===false || step.cache_load_all === true" style="width: 65%"></el-input>
        </el-form-item>
        <el-form-item label="从表中加载所有数据" prop="cache_load_all">
          <el-checkbox type="number" v-model="step.cache_load_all" :disabled="step.cache===false"></el-checkbox>
        </el-form-item>
      </el-form>

      <div class="node_title_basic">
        <span>查询表返回的值:</span>
        <span style="float: right">
          <el-button size="mini" type="primary" :disabled="step.lookup.table===''" @click="addFields">新增</el-button>
          <el-button size="mini" type="primary" :loading="columnLoading" :disabled="step.lookup.table === ''" @click="getTableColumns">获取返回字段</el-button>
        </span>
      </div>
      <el-table :data="step.lookup.value" height="200" :header-cell-style="{background:'#eef1f6'}">
        <el-table-column label="字段">
          <template slot-scope="scope">
            <el-select size="mini" clearable v-model="scope.row.name">
              <el-option v-for="item in step.tableFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="新名称">
          <template slot-scope="scope">
           <el-input size="mini" v-model="scope.row.rename" @blur="blurInput(scope.row,scope.$index)"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="默认值">
          <template slot-scope="scope">
            <el-input size="mini" v-model="scope.row.default"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="类型">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.type" clearable>
              <el-option v-for="item in types" :label="item" :key="item" :value="item"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button  type="text" size="mini" @click="deleteFields(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="node_title_basic">
        <span>查询所需要的关键字:</span>
        <span style="float: right">
          <el-button size="mini" @click="addParam" type="primary" :disabled="step.lookup.value.length===0">新增</el-button>
          <el-button size="mini" type="primary" @click="getParams" :disabled="step.lookup.value.length===0">获取查询关键字</el-button>
        </span>
      </div>
      <el-table :data="step.lookup.key" height="200" :header-cell-style="{background:'#eef1f6'}">
        <el-table-column label="表字段">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.field" clearable>
              <el-option v-for="item in step.tableFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="比较操作符">
          <template slot-scope="scope">
            <el-select v-model="scope.row.condition" size="mini" clearable>
              <el-option v-for="item in conditions" :label="item" :key="item" :value="item"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="字段1">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.name" clearable>
              <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="字段2">
          <template slot-scope="scope">
            <el-select size="mini" v-model="scope.row.name2" clearable>
              <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="deleteParam(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-form :model="step.lookup" label-position="right" size="mini" label-width="150px">
        <el-form-item label="查询失败则忽略" prop="fail_on_multiple">
          <el-checkbox v-model="step.lookup.fail_on_multiple"></el-checkbox>
        </el-form-item>
        <el-form-item label="多行结果时失败">
          <el-checkbox v-model="step.lookup.eat_row_on_failure" :disabled="step.cache===true"></el-checkbox>
        </el-form-item>
        <el-form-item label="排序" prop="orderby">
          <el-input v-model="step.lookup.orderby" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <!--    数据源滚管理-->
    <el-dialog :visible.sync="build" class="abow_dialog" width="70%" :title="dataSourceTitle" :append-to-body="true">
      <data-source-manage :key="dataSourcekey" ref="dataSourceView"></data-source-manage>
    </el-dialog>
    <div class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import store from "../../../../vuex/store";
  import {getConnection, getSchema, getTableColumns, getTables} from "../../../../api/api";
  import util from "../../../../common/utils";
  import dataSourceManage from '../../../common/DataSourceManager';
  import {addConnection, getChecktableName, spliceDataJson} from "../../../../common/common";
  import {compareFields} from "../../../../assets/common/tool";

  export default {
    name: "DBLookup",
    data() {
      return {
        columnLoading:false,
        build:false,
        dataSourceTitle:'',
        dataSourcekey:null,
        treeData:[],
        dataSources:[],
        schema:'',
        schemaDisable:false,
        schemas:[],
        conditions:['=','<>','<','<=','>','>=','LIKE','BETWEEN','IS NULL','IS NOT NULL'],
        types:["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"],
        nodeData: '',
        key: '',
        oldStepName:'',
        step: {
          tableFields:[],
          name: "数据库查询",
          type: "DBLookup",
          distribute: "Y",
          copies: "1",
          partitioning: {"method": "none"},
          connection: "",
          cache: false,
          cache_load_all: false,
          cache_size: "0",
          lookup: {
            schema:'',
            table: "",
            fail_on_multiple: false,
            eat_row_on_failure: false,
            orderby:'',
            name:'',
            key: [],
            value: []
          },
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "560",
            yloc: "304",
            draw: "Y"
          }
        }
      }
    },
    mounted() {
      this.init();
    },
    components:{
      dataSourceManage
    },
    methods:{

      blurInput(row,index) {
        let args=JSON.parse(JSON.stringify(this.step.lookup.value));
        args.splice(index,1);
        getChecktableName(args,this.step.outFields,row,'rename');
      },

      changeStep() {
        for (let key in this.step) {
          if (typeof this.step[key] === 'boolean') {
            this.step[key] = this.step[key] === true ? 'Y' : "N"
          }else if( key === 'lookup'){
            for(let subKey in this.step[key]){
              if (typeof this.step[key] === 'boolean') {
                this.step.lookup[key] = this.step.lookup[key] === true ? 'Y' : "N"
              }
            }
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
        this.step.lookup.fail_on_multiple = this.step.lookup.fail_on_multiple ==='Y'?true:false;
        this.step.lookup.eat_row_on_failure =this.step.lookup.eat_row_on_failure==='Y'?true:false;
      },


      getParams(){
        this.step.lookup.key=[];
        if(this.step.lookup.value.length>=this.step.outFields){
          for(let i = 0;i<this.step.outFields.length;i++){
            let tmpData = {
              field: this.step.lookup.value[i].field,
              condition:'=',
              name:this.step.outFields[i].name,
              name2:'',
            }
            this.step.lookup.key.push(tmpData)
          }
        }else {
          for(let i = 0;i<this.step.lookup.value.length;i++){
            let tmpData = {
              field: this.step.lookup.value[i].name,
              condition:'=',
              name:this.step.outFields[i].name,
              name2:'',
            }
            this.step.lookup.key.push(tmpData)

          }
        }

      },

      //获取返回字段
      getTableColumns(){
        this.changeStep();
        let connectionName ='';
        this.columnLoading = true;
        this.step.lookup.value=[];
        this.step.tableFields=[];
        let dataJson = spliceDataJson(this.key, this.step);
        this.dataSources.forEach(item=>{
          if(item.datasourceId === this.step.connection){
            connectionName = item.dsName;
          }
        })

        let params ={
          tableName:this.step.lookup.table,
          connectName:this.step.connection,
          schema:this.step.lookup.schema,
          step:dataJson
        }
        getTableColumns(params).then(res=>{
          let{
            data
          }=res;
          if(data.code ==='10000'){
            data.content.forEach(item=>{
              this.step.tableFields.push({
                name:item.name,
                type:item.type
              })
              this.step.lookup.value.push({
                name:item.name,
                rename:item.name,
                default:'',
                type:item.type
              })
            })
          }
          this.columnLoading = false;
        })
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
      init(){
        this.getDataSources();
        this.oldStepName = this.nodeData.label;
        this.step.name = this.nodeData.label;
        let params = {
          key: this.key,
          value: this.step.name,
        }
        //获取上步骤字段信息
        let curStep = store.getters.getCurrentStep(params);
        if(curStep!==undefined && curStep.lookup.key!==undefined){
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            } else if (curStep[key] === 'N' && key !== 'type') {
              this.step[key] = false;
            } else {
              if (key === 'lookup') {
                for(let subKey in curStep.lookup){
                  if(curStep.lookup['key'] ==='Y'){
                    this.step.lookup[subKey] = true;
                  }else if(curStep.lookup[subKey] ==='N'){
                    this.step.lookup[subKey] = false;
                  }else{
                    this.step.lookup[subKey] = curStep.lookup[subKey];
                  }
                }
              } else {
                this.step[key] = curStep[key];
              }
            }
          }
          let keyData =[];
          curStep.lookup.key.forEach(item=>{
            keyData.push(item);
          })
          this.step.lookup.key = keyData;
          let valueData =[];
          curStep.lookup.value.forEach(item=>{
            valueData.push(item)
          })
          this.SelectionChange();
          this.step.lookup.schema = curStep.lookup.schema;
          this.step.lookup.value = valueData;
        }
        let outFields =store.getters.generateOutputFields(params);
        compareFields(outFields)
        this.step['outFields'] = outFields;
      },
      //获取Schema
      SelectionChange() {
        let params = {
          datasourceName: this.step.connection
        }
        addConnection(this.dataSources,this.step.connection,this.key);
        this.schemas=[];
        this.step.lookup.schema='';
        getSchema(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            if (data.content !== undefined && data.content !== null && data.content.length !== 0) {
              this.schemaDisable = false;
              this.schemas = data.content;
              this.handleSelectionChange();
            }else{
              this.handleSelectionChange();
            }
            this.schema = data.content;
          }
        })
      },

      addParam(){
        this.step.lookup.key.push({
          field: '',
          condition: '=',
          name:'',
          name2:''
        })
      },
      addFields(){
        this.step.lookup.value.push({
          name:'',
          rename:'',
          default:'',
          type:'String'
        })
      },
      deleteParam(index){
        this.step.lookup.key.splice(index,1);
      },
      deleteFields(index){
        this.step.lookup.value.splice(index,1);
      },
      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },
      submit(){
        let step = Object.assign({},this.step);
        step.cache = this.step.cache === true?'Y':'N';
        step.cache_load_all= this.step.cache_load_all===true?'Y':'N';
        step.lookup.fail_on_multiple = this.step.lookup.fail_on_multiple === true?'Y':'N';
        step.lookup.eat_row_on_failure = this.step.lookup.eat_row_on_failure === true?'Y':'N';
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, step.name);
        let flag = false;
        if (step.name !== newName) {
          flag = true;
          step.name = newName;
        }
        step.lookup.value.forEach(item=>{
          step.outFields.push({
            currency: "",
            decimal: "",
            format: "",
            group: "",
            length: -1,
            name: item.rename,
            nullif: '',
            precision: -1,
            set_empty_string: "N",
            type: item.type,
          })
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
      //获取目标表
      handleSelectionChange() {
        let params = {
          datasourceName: this.step.connection,
          schema: this.step.lookup.schema,
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
    },
  }
</script>

<style scoped>

</style>
