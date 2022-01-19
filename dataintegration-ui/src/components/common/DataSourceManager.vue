<template>
  <!--  数据源管理-->
  <div class="data_body">
    <div class="data_content">
      <div class="data_tool">
        <div class="type_css" v-for="(item,index) in treeData" :key="item.id" :id="item.id"
             @click="selectTool(item.id)">{{item.label}}
        </div>

      </div>
      <div class="data_content_body">
        <div class="data_normal" v-if="showItem === 'normal'">
          <div class="data_normal_header">
            <label>连接名称：</label>
            <el-input v-model="connectName" size="mini"></el-input>
          </div>
          <div class="data_normal_body">
            <div class="node_title_basic">
              <span class="start-css">连接类型:</span>
            </div>
            <div class="connect_type">
              <div v-for="(item,index) in dataSourceConfig" :id="'datasource'+index" :key="item.dasourceId"
                   @click="selectType(index)" class="type_css">
                {{item.label}}
              </div>
            </div>
            <div class="node_title_basic">
              <span class="start-css">连接方式:</span>
            </div>
            <div class="connect_func">
              <div v-for="(item,index) in connectWay" :key="item.key" :id="item.key" class="type_css"
                   @click="selectFunc(item.key)">
                {{item.label}}
              </div>
            </div>

          </div>
          <div class="data_normal_body">
            <div class="node_title_basic">
              <span class="start-css">设置:</span>
            </div>
            <div class="data_normal_form">
              <el-form :model="conForm" ref="conForm">
                <el-form-item v-for="item in connectForm" :key="item.key" :prop="item.key">
                  <label v-if="typeof item.value !=='boolean'">{{item.label+':'}}</label>
                  <el-select v-model="item.value" size="mini" style="width: 100%" v-if="dasourceId === 'GENERIC' && item.key ==='DATABASE_DIALECT_ID' " filterable>
                    <el-option v-for="item1 in dataSourceConfig" :key="item1.dasourceId" :value="item1.dasourceId" :label="item1.label" >

                    </el-option>
                  </el-select>
                  <el-input v-model="item.value" size="mini"
                            v-if="typeof item.value !=='boolean' && item.key!=='password' && item.key !=='DATABASE_DIALECT_ID' "></el-input>
                  <el-input v-model="item.value" size="mini" v-if="item.key==='password' && item.key !=='DATABASE_DIALECT_ID'" show-password type="password" auto-complete="new-password"></el-input>
                  <el-checkbox v-model="item.value" v-if="typeof item.value ==='boolean'"></el-checkbox>
                  <label v-if="typeof item.value ==='boolean'">{{item.label}}</label>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>

        <div class="data_normal" v-if="showItem==='advanted'">
          <div class="data_advanted">
            <label>标识符：</label>
            <div class="data_ident" style="overflow: auto">
              <el-form :model="advenForm">
                <el-form-item prop="supportBoolean">
                  <el-checkbox v-model="advenForm.SUPPORTS_BOOLEAN_DATA_TYPE"></el-checkbox>
                  <label>支持布尔类型数据</label>
                </el-form-item>
                <el-form-item prop="supportDate">
                  <el-checkbox v-model="advenForm.SUPPORTS_TIMESTAMP_DATA_TYPE"></el-checkbox>
                  <label>支持时间日期类型数据</label>
                </el-form-item>
                <el-form-item prop="identOfMarks">
                  <el-checkbox v-model="advenForm.QUOTE_ALL_FIELDS"></el-checkbox>
                  <label>标识符使用引号括起来</label>
                </el-form-item>
                <el-form-item prop="identBylower">
                  <el-checkbox v-model="advenForm.FORCE_IDENTIFIERS_TO_LOWERCASE"></el-checkbox>
                  <label>强制标识符使用小写字母</label>
                </el-form-item>
                <el-form-item prop="identByUpper">
                  <el-checkbox v-model="advenForm.FORCE_IDENTIFIERS_TO_UPPERCASE"></el-checkbox>
                  <label>强制标识符使用大写字母</label>
                </el-form-item>
                <el-form-item prop="preservedWords">
                  <el-checkbox v-model="advenForm.PRESERVE_RESERVED_WORD_CASE"></el-checkbox>
                  <label>Preserve case of reserved words</label>
                </el-form-item>
                <el-form-item prop="strictNumer" v-if="dasourceId === 'ORACLE'">
                  <el-checkbox v-model="advenForm.STRICT_NUMBER_38_INTERPRETATION"></el-checkbox>
                  <label>Strict NUMBER(38) interpretation (For oracle db only)</label>
                </el-form-item>
                <el-form-item label="默认模式名称。在没有其他模式名称时使用" prop="supportBoolean">
                  <el-input v-model="advenForm.PREFERRED_SCHEMA_NAME" size="mini"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </div>
          <div class="data_advanted1">
            <label>请输入连接成功后要执行的sql语句，用分号(;)隔开：</label>
            <div class="data_ident1" style="padding:0">
              <codemirror style="height: 100%" ref="code"></codemirror>
            </div>
          </div>
        </div>

        <div class="data_normal" v-if="showItem==='select'">
          <div class="node_title_basic">
            <span class="start-css">设置:</span>
            <span style="float: right"><el-button type="primary" size="mini" @click="addParam">新增</el-button></span>
          </div>
          <div class="data_sub">
            <el-table :data="nameParams" :header-cell-style="{background:'#f5f7fa',color:'#606266'}" border
                      height="100%">
              <el-table-column prop="nameParam" label="命名参数">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.nameParam" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="value" label="值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.value" size="mini"></el-input>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="node_title_basic" style="line-height: 20px">
            <span style="float: right"><el-button type="primary" size="mini" @click="help">帮助...</el-button></span>
          </div>
        </div>

        <div class="data_normal" v-if="showItem ==='connectPool'">
          <div class="node_title_basic" style="border:0;padding: 0;height: 25px">
            <span><el-checkbox v-model="USE_POOLING"></el-checkbox></span>
            <label>使用连接池</label>
          </div>
          <div class="node_title_basic">
            <label>连接池大小</label>
          </div>
          <div class="pool_size">
            <el-form :inline="true" :model="poolForm">
              <el-form-item label="初始大小" prop="initSize">
                <el-input v-model="poolForm.INITIAL_POOL_SIZE" size="mini" :disabled="!USE_POOLING"></el-input>
              </el-form-item>
              <el-form-item label="最大空闲时间" prop="maxIdleTime">
                <el-input v-model="poolForm.MAXIMUM_POOL_SIZE" size="mini" :disabled="!USE_POOLING"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="node_title_basic">
            <span class="start-css">命名参数:</span>
          </div>
          <div class="name_param_css">
            <el-table :data="connectPoolData" @row-click="rowClick" :highlight-current-row="true"
                      :header-cell-style="{background:'#f5f7fa',color:'#606266'}" border height="100%" :disabled="true">
              <el-table-column prop="nameParam" label="命名参数">
              </el-table-column>
              <el-table-column prop="value" label="值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.value" size="mini"></el-input>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <div class="node_title_basic">
            <span class="start-css">描述:</span>
            <span style="float: right"><el-button @click="reset" size="mini">恢复默认设置</el-button></span>
          </div>
          <div class="pool_param_desc" id="pool_desc">
          </div>
        </div>
      </div>
    </div>
    <!--   按钮-->

    <div slot="footer" class="dialog-footer" style="text-align: center">
<!--      <el-button size="mini" @click="testConnection">测试</el-button>-->
<!--      <el-button size="mini">特征列表</el-button>-->
<!--      <el-button size="mini">浏览</el-button>-->
    </div>

    <div slot="footer" class="dialog-footer plugin_footer">

      <el-button size="mini" @click="closeConnectoin">取 消</el-button>
      <el-button size="mini" @click="testConnection" type="primary">测试</el-button>
      <el-button size="mini" type="primary" :loading="editLoading" @click="addConnection">确 定</el-button>
    </div>

  </div>
</template>

<script>
  import {
    base,
    boardUrl,
    testConnection,
    getDataSourceConfig,
    addConnection,
    getConnection,
    previewTable,
    updateConnection
  } from "../../api/api";
  import codemirror from "./codemirror";
  import $ from "jquery";

  export default {
    name: "DataSourceManager",
    // props: {
    //   dataSourceConfig:Array
    // },
    data() {
      return {
        editLoading: false,
        dataSourceData: {},
        conForm: {},
        connectPoolData: [{
          nameParam: '1',
          value: 1
        }],
        poolForm: {
          INITIAL_POOL_SIZE: 0,//初始大小，
          MAXIMUM_POOL_SIZE: 0
        },
        USE_POOLING: false,
        nameParams: [],
        advenForm: {
          SUPPORTS_BOOLEAN_DATA_TYPE: true,
          SUPPORTS_TIMESTAMP_DATA_TYPE: true,
          QUOTE_ALL_FIELDS: false,//标识符使用引号括起来
          FORCE_IDENTIFIERS_TO_LOWERCASE: false,//强制标识符使用小写字母
          FORCE_IDENTIFIERS_TO_UPPERCASE: false,//强制标识符使用大写字母
          PRESERVE_RESERVED_WORD_CASE: true,//保留大小写的保留字
          STRICT_NUMBER_38_INTERPRETATION: false,//
          PREFERRED_SCHEMA_NAME: '',
        },
        showItem: 'normal',
        connectName: '',
        treeData: [{
          id: 'normal',
          label: '一般',
        }, {
          id: 'advanted',
          label: '高级',
        }, {
          id: 'select',
          label: '选项',
        }, {
          id: 'connectPool',
          label: '连接池',
        }, {
          id: 'cluster',
          label: '集群'
        }],
        normalForm: {},
        dataSourceConfig: [],
        connectWay: [],
        connectForm: [],
        dasourceId: '',//数据库类型
        access: '',//连接方式
        editForm: {},
        sql: '',
      }
    },
    components: {
      codemirror
    },
    mounted() {
      this.init();
      // this.getConnections();
    },
    created() {
    },
    methods: {

      /**
       * 关闭弹窗
       **/
      closeConnectoin() {
        console.info("this.$parent.$parent", this.$parent.$parent.build = false);
      },

      addConnection() {
        let attribute = [];
        this.spliceParams(attribute).then(resolve => {
          let params = {
            dsName: this.connectName,
            dsPassword: this.conForm.password,
            describe: '',
            dsType: this.dasourceId,
            dsUsername: this.conForm.userName,
            dsHost: this.conForm.hostName,
            dsPort: this.conForm.port,
            sourcePlatform: 'JCPLAT',
            dsConnectorSetting: this.editForm,
            access: this.access,
            database: this.conForm.dsName,
          }
          if(this.dasourceId ==='GENERIC'){
            params['customDriverClass'] = this.conForm.CUSTOM_DRIVER_CLASS;
            params['customUrl'] = this.conForm.CUSTOM_URL;
            params['databaseDialectId'] = this.conForm.DATABASE_DIALECT_ID;
          }
          this.editLoading = true;
          if (this.dataSourceData.datasourceId !== undefined) {
            params.datasourceId = this.dataSourceData.datasourceId;
            updateConnection(params).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '编辑成功',
                  type: 'success'
                })
                this.$parent.$parent.getDataSources();
                this.$parent.$parent.build = false;
              }
              this.editLoading = false;
            })
          } else {
            addConnection(params).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                })
                this.$parent.$parent.getDataSources();
                this.$parent.$parent.build = false;
              }
              this.editLoading = false;
            })
          }

        })
      },


      //获取数据库连接
      getConnections() {
        let params = {
          sourcePlatform: 'JCPLAT'
        }
        getConnection(params).then(res => {
        })
      },


      /**
       * 拼接参数
       * */
      spliceParams(attribute) {
        let _this = this;
        let promise = new Promise(function (resolve, reject) {
          _this.connectForm.forEach(item => {
            _this.conForm[item.key] = item.value;
          })
          if(_this.dasourceId === 'GENERIC'){
            _this.connectForm.forEach(item => {
              attribute.push({
                code: item.key,
                attribute: item.value,
              })
            })
          }

          //高级参数
          let advenParam = Object.assign({}, _this.advenForm)
          let advenParams = [];
          for (let key in advenParam) {
            if (typeof advenParam[key] === 'boolean') {
              advenParam[key] = _this.advenForm[key] === true ? 'Y' : 'N';
            }
            advenParams.push({
              code: key,
              attribute: advenParam[key],
            });
            attribute.push({
              code: key,
              attribute: advenParam[key],
            })

          }

          //执行sql
          if (_this.$refs.code) {
            let sql = _this.$refs.code.editor.getValue();
            if (sql !== '') {
              advenParams.push({
                code: 'SQL_CONNECT',
                attribute: sql,
              });
              attribute.push({
                code: 'SQL_CONNECT',
                attribute: sql,
              })
            }
          }
          _this.editForm['advancedParameterVo'] = advenParams;//高级参数
          //选项参数 code 为 'EXTRA_OPTION_'+数据库类型+参数名
          let options = [];
          _this.nameParams.forEach(item => {
            options.push({
              code: 'EXTRA_OPTION_' + _this.dasourceId + '.' + item.nameParam,
              attribute: item.value,
            })
            attribute.push({
              code: 'EXTRA_OPTION_' + _this.dasourceId + '.' + item.nameParam,
              attribute: item.value,
            })
          })
          _this.editForm['optionsParameterVo'] = options;
          //连接池参数
          let poolCofigParams = [];
          if (_this.USE_POOLING) {
            for (let key in _this.poolForm) {
              poolCofigParams.push({
                code: key,
                attribute: _this.poolForm[key],
              })
              attribute.push({
                code: key,
                attribute: _this.poolForm[key],
              })
            }
            _this.connectPoolData.forEach(item => {
              poolCofigParams.push({
                code: item.nameParam,
                attribute: item.value,
              });
              attribute.push({
                code: item.nameParam,
                attribute: item.value,
              })
            })
          }
          _this.editForm['poolParameterVo'] = poolCofigParams;
          resolve('s')
        })
        return promise;
      },

      /**
       * 测试数据库连接
       **/
      testConnection() {
        let attribute = [];
        this.spliceParams(attribute).then(resolve => {
          let attributes = {
            attribute: attribute
          };
          let params = {
            name: this.connectName,
            password: this.conForm.password,
            port: this.conForm.port,
            server: this.conForm.hostName,
            type: this.dasourceId,
            database: this.conForm.dsName,
            username: this.conForm.userName,
            access: this.access,
            attributes: attributes,
          }
          let testParams = {
            connection: params,
          }
          testConnection(testParams).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000' && data.content === true) {
              this.$message({
                message: '数据库连接成功',
                type: 'success'
              })
            } else {
              // let msg = data.msg.substr(0,100);
              // console.info("msg",msg);
              // this.$message({
              //   message: data.msg.substr(0,1000)+'...',
              //   type: 'error',
              // })
            }
          })
        })

      },

      /**
       * 单击表格某一行
       */
      rowClick(row, column, ecent) {
        $('#pool_desc').html(row.desc)
      },

      reset() {
        let _this = this;
        $.ajax({
          url: '../../../static/common.json',
          type: 'GET',
          async: false,
          dataType: 'json',
          success: function (result) {
            _this.$nextTick(() => {
              _this.connectPoolData = result.connectionParams;
            })
          }
        })
      },
      init() {
        if (this.dataSourceData.datasourceId !== undefined) {
          if(sessionStorage.getItem('dataConfig') === null){
            getDataSourceConfig().then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.dataSourceConfig = data.content.content.dataSources;
                sessionStorage.setItem("dataConfig",JSON.stringify(this.dataSourceConfig));
                this.selectTool('normal');
                this.showPage();
              }
            })
          }else{
            this.dataSourceConfig = JSON.parse(sessionStorage.getItem('dataConfig'));
            this.showPage();
          }

        } else {
          if(sessionStorage.getItem('dataConfig') !== null){
            this.dataSourceConfig = JSON.parse(sessionStorage.getItem('dataConfig'));
          }else{
            getDataSourceConfig().then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.dataSourceConfig = data.content.content.dataSources;
                sessionStorage.setItem("dataConfig",JSON.stringify(this.dataSourceConfig));
                this.selectTool('normal');

              }
            })
          }

        }
      },

      //页面回显
      showPage(){
        this.connectName = this.dataSourceData.dsName;
        this.connectForm = [];
        if(this.dataSourceData.dsType === 'GENERIC'){
          this.connectForm.push({
            key:'DATABASE_DIALECT_ID',
            label:'数据库类型',
            value:this.dataSourceData.DATABASE_DIALECT_ID,
          })
          this.connectForm.push({
            key:'CUSTOM_DRIVER_CLASS',
            label:'驱动名',
            value:this.dataSourceData.CUSTOM_DRIVER_CLASS,
          })
          this.connectForm.push({
            key:'CUSTOM_URL',
            label:'URL',
            value:this.dataSourceData.CUSTOM_URL,
          })
          if (this.dataSourceData.database !== '') {
            this.connectForm.push({
              key: "userName",
              label: '账号',
              value: this.dataSourceData.dsUsername
            })
          }
          if (this.dataSourceData.database !== '') {
            this.connectForm.push({
              key: "password",
              label: '密码',
              value: this.dataSourceData.dsPassword
            })
          }
        }else{
          if (this.dataSourceData.dsHost !== '') {
            this.connectForm.push({
              key: "hostName",
              label: '主机名',
              value: this.dataSourceData.dsHost
            })
          }
          if (this.dataSourceData.database !== '') {
            this.connectForm.push({
              key: "dsName",
              label: '数据库名称',
              value: this.dataSourceData.database
            })
          }
          if (this.dataSourceData.database !== '') {
            this.connectForm.push({
              key: "port",
              label: '端口',
              value: this.dataSourceData.dsPort
            })
          }
          if (this.dataSourceData.database !== '') {
            this.connectForm.push({
              key: "userName",
              label: '账号',
              value: this.dataSourceData.dsUsername
            })
          }
          if (this.dataSourceData.database !== '') {
            this.connectForm.push({
              key: "password",
              label: '密码',
              value: this.dataSourceData.dsPassword
            })
          }
          if (this.dataSourceData.access === 'Native') {
            this.connectForm.push({
              key: "userResultCursor",
              label: 'use Result  streaming  Cursor',
              value: true
            })
          }
        }

        if (this.dataSourceData.datasourceId !== undefined) {
          this.editType(this.dataSourceData.dsType);
          this.editFunc(this.dataSourceData.access)
        } else {
          this.selectType(this.dataSourceData.dsType);
          this.selectFunc(this.dataSourceData.access);
        }
        //高级
        let dsConnectSetting = JSON.parse(this.dataSourceData.dsConnectorSetting);
        let advancedParameterVo = dsConnectSetting.advancedParameterVo;
        if (advancedParameterVo.length > 0) {
          this.advenForm = {};
          advancedParameterVo.forEach(item => {
            if (item.code !== 'PREFERRED_SCHEMA_NAME' && item.code !== 'SQL_CONNECT') {
              this.advenForm[item.code] = item.attribute === 'Y'

            } else if (item.code === 'SQL_CONNECT') {
              this.sql = item.attribute;
            } else {
              this.advenForm[item.code] = item.attribute;
            }

          })
        }

        //选项
        let optionsParameterVo = dsConnectSetting.optionsParameterVo;
        if (optionsParameterVo.length > 0) {
          optionsParameterVo.forEach(item => {
            let key = item.code;
            let code = key.substr(key.indexOf('.') + 1);
            this.nameParams.push({
              nameParam: code,
              value: item.attribute
            })
          })
        }
        //连接池
        let poolParameterVo = dsConnectSetting.poolParameterVo;
        if (poolParameterVo.length > 0) {
          this.USE_POOLING = true;
          this.poolForm = {}
          this.connectPoolData = [];
          poolParameterVo.forEach(item => {
            if (item.code === 'INITIAL_POOL_SIZE' || item.code === 'MAXIMUM_POOL_SIZE') {
              this.poolForm[item.code] = item.attribute;
            } else {
              this.connectPoolData.push({
                nameParam: item.code,
                value: item.attribute,
              })
            }
          })
        }
      },

      /**
       * 新增参数
       */
      addParam() {
        this.nameParams.push({
          nameParam: '',
          value: '',
        })
      },

      /**
       * 帮助
       */
      help() {
      },

      editType(dataSourceId) {
        this.dataSourceConfig.forEach((item, index) => {
          $('#' + item.id).removeClass('type_css_click');
          if (item.dasourceId === dataSourceId) {
            this.dasourceId = item.dasourceId;
            this.connectWay = item.connectType;
            this.$nextTick(() => {
              let doc = document.getElementById('datasource' + index);
              doc.className = 'type_css_click';
              // this.selectFunc(this.connectWay[0].key);
            })
          }
        })
      },
      editFunc(key) {
        this.connectWay.forEach((item, index) => {
          $('#' + item.key).removeClass('type_css_click');
          if (item.key === key) {
            this.access = item.key;
            this.$nextTick(() => {
              let doc = document.getElementById(key);
              doc.className = 'type_css_click'
              // if (this.dataSourceData.datasourceId === undefined) {
              // }
            })

          }
        })
      },

      selectTool(id) {
        this.treeData.forEach(item => {
          $('#' + item.id).removeClass('type_css_click');
          if (item.id === id) {
            this.showItem = id;
            $('#' + id).toggleClass('type_css_click');
            $('#pool_desc').html('')
            if (id === 'normal') {
              //datasourceId为undefined为新增
              if (this.dataSourceData.datasourceId === undefined) {
                this.selectType(0)
              }
            }
            if (id === 'connectPool') {
              let _this = this;
              //读取配置文件
              $.ajax({
                url: '../../../static/common.json',
                type: 'GET',
                async: false,
                dataType: 'json',
                success: function (result) {
                  _this.$nextTick(() => {
                    _this.connectPoolData = result.connectionParams;
                  })
                }
              })
            } else if (id === 'advanted') {
              if (this.sql !== '') {
                this.$nextTick(() => {
                  this.$refs.code.editor.setValue(this.sql);
                })
              }

            }

          }
        })
      },

      selectType(id) {
        this.dataSourceConfig.forEach((item, index) => {
          $('#datasource' + index).removeClass('type_css_click');
          if (index === id) {
            this.dasourceId = item.dasourceId;
            this.connectWay = item.connectType;
            this.$nextTick(() => {
              let doc = document.getElementById('datasource' + id);
              doc.className = 'type_css_click';
              this.selectFunc(this.connectWay[0].key);
            })
          }

        })
      },

      selectFunc(key) {
        this.connectWay.forEach((item, index) => {
          $('#' + item.key).removeClass('type_css_click');
          if (item.key === key) {
            this.access = item.key;
            this.$nextTick(() => {
              let doc = document.getElementById(key);
              doc.className = 'type_css_click'
              // if (this.dataSourceData.datasourceId === undefined) {
                this.connectForm = item.connectParam;
              // }

            })

          }
        })
      },

    },
  }
</script>

<style>
  .data_normal_form .el-form-item {
    margin-bottom: 2px;
  }

  .data_ident .el-form-item {
    margin-bottom: 2px;
  }

  .data_normal_form .el-checkbox {
    margin-right: 0px !important;
  }

  .data_ident .el-checkbox {
    margin-right: 0px !important;
  }

  .data_sub .el-table .cell {
    padding: 5px;
  }

  .data_sub .el-table__row > td {
    border: none;
  }

  /*.data_sub .el-input--mini .el-input__inner{*/
  /*  border:0 !important;*/
  /*}*/

  /*.data_sub .el-table::before {*/
  /*  height: 0px;*/
  /*}*/

  /*.data_normal_form .el-form-item__label {*/
  /*  text-align: right;*/
  /*  vertical-align: middle;*/
  /*  float: left;*/
  /*  font-size: 14px;*/
  /*  color: #606266;*/
  /*  line-height: 0px;*/
  /*  padding: 0 12px 0 0;*/
  /*  -webkit-box-sizing: border-box;*/
  /*  box-sizing: border-box;*/
  /*}*/

  /*.data_normal_form .el-form-item__content {*/
  /*  line-height: 50px !important;*/
  /*  position: relative;*/
  /*  font-size: 14px;*/
  /*}*/
</style>
