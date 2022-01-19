<template>
  <div class="plugin_body">
    <div class="div_form plugin_content plugin_form">
      <el-form :model="step" ref="stepfrom" label-width="30%" size="mini" :inline-message="true">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <el-tabs>
        <el-tab-pane label="设置">
          <div class="node_title_basic">
            <span>设置:</span>
          </div>

          <div class="form_css">
            <el-form :model="step" size="mini" label-width="30%" :inline-message="true" label-position="right">
              <el-form-item label="URL" prop="url">
                <el-input v-model="step.url" :disabled="step.urlInField === true" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="从字段中获取Url" prop="urlInField">
                <el-checkbox v-model="step.urlInField"></el-checkbox>
              </el-form-item>
              <el-form-item label="url字段名" prop="urlField">
                <el-select v-model="step.urlField" :disabled="step.urlInField===false" style="width: 65%" clearable>
                  <el-option v-for="item in step.outFields" :label="item.name" :value="item.name" :key="item.name"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="编码" prop="encoding">
                <el-select v-model="step.encoding" style="width: 65%" clearable>
                  <el-option v-for="item in encodings" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="连接超时" prop="connectionTimeout">
                <el-input v-model="step.connectionTimeout" type="number" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="scoket超时" prop="connectionTimeout">
                <el-input v-model="step.socketTimeout" type="number" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="连接关闭等待时间" prop="closeIdleConnectionsTime">
                <el-input v-model="step.closeIdleConnectionsTime" type="number" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="node_title_basic">
            <span>输出字段:</span>
          </div>
          <div class="form_css">
            <el-form :model="step.result" label-width="30%" label-position="right" size="mini">
              <el-form-item label="结果字段名" prop="name">
                <el-input v-model="step.result.name" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="状态码字段名" prop="code">
                <el-input v-model="step.result.code" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="响应时间字段名" prop="response_time">
                <el-input v-model="step.result.response_time" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="响应头字段名" prop="response_header">
                <el-input v-model="step.result.response_header" style="width: 65%">
                </el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="node_title_basic">
            <span>HTTP身份验证:</span>
          </div>
          <div class="form_css">
            <el-form :model="step" label-position="right" label-width="30%" size="mini">
              <el-form-item label="http登录" prop="httpLogin">
                <el-input v-model="step.httpLogin" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="httpPassword">
                <el-input type="password" show-password auto-complete="new-password"
                          v-model="step.httpPassword" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="node_title_basic">
            <span>代理</span>
          </div>
          <div class="form_css">
            <el-form :model="step" label-width="30%" label-position="right" size="mini">
              <el-form-item label="代理主机" prop="proxyHost">
                <el-input v-model="step.proxyHost" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="代理端口" prop="proxyPort">
                <el-input type="number" v-model="step.proxyPort" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane label="参数">
          <div class="node_title_basic">
            <span>命名参数</span>
            <span style="float: right">
              <el-button size="mini" type="primary" @click="addArg">新增</el-button>
              <el-button type="primary" size="mini" @click="getArg">获取字段</el-button>
            </span>
          </div>
          <el-table :data="step.lookup.arg" height="300" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column label="名称">
              <template slot-scope="scope">
                <el-select size="mini" v-model="scope.row.name">
                  <el-option v-for="item in step.outFields" :key="item.name" :label="item.name" :value="item.name">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="命名参数">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.parameter"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteArg(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="node_title_basic">
            <span>自定义HTTP头</span>
            <span style="float: right">
              <el-button size="mini" type="primary" @click="addHeader">新增</el-button>
              <el-button type="primary" size="mini" @click="getHeader">获取字段</el-button>
            </span>
          </div>
          <el-table :data="step.lookup.header" height="300" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column label="名称">
              <template slot-scope="scope">
                <el-select size="mini" v-model="scope.row.name">
                  <el-option v-for="item in step.outFields" :key="item.name" :label="item.name" :value="item.name">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="命名参数">
              <template slot-scope="scope">
                <el-input size="mini" v-model="scope.row.parameter"></el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteHeader(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
    </div>
  </div>

</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store";

  export default {
    name: "HTTP",
    data() {
      return {
        encodings: [],
        nodeData: '',
        key: '',
        oldStepName: '',
        step: {
          name: "HTTP client",
          type: "HTTP2",
          distribute: "Y",
          copies: "1",
          partitioning: {"method": "none"},
          url: "",
          urlInField: false,
          urlField:'',
          encoding: "UTF-8",
          httpLogin: "",
          httpPassword: "",
          proxyHost: "",
          proxyPort: "",
          socketTimeout: "10000",
          connectionTimeout: "10000",
          closeIdleConnectionsTime: "-1",
          lookup: {
            arg: [],
            header: []
          },
          result: {
            name: "result",
            code: "",
            response_time: "",
            response_header: ""
          },
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "208",
            yloc: "560",
            draw: "Y"
          }
        }
      }
    },
    mounted() {
      this.init();
    },
    methods: {
      init() {
        this.getConstant();
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        this.step.lookup.arg = [];
        this.step.lookup.header = [];
        let params = {
          key: this.key,
          value: this.step.name,
        }
        //获取上步骤字段信息

        //获取当前步骤信息
        let curStep = store.getters.getCurrentStep(params);
        if (curStep != undefined && curStep.lookup.arg !== undefined) {
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            } else if (curStep[key] === 'N' && key !== 'type') {
              this.step[key] = false;
            } else {
              if (key === 'lookup') {
              } else {
                this.step[key] = curStep[key];
              }
            }
          }
          if (this.step.lookup.arg !== undefined) {
            this.step.lookup.arg = [];
            let fields = curStep.lookup.arg;
            fields.forEach(item => {
              this.step.lookup.arg.push(item);
            })
          }
          if (this.step.lookup.header !== undefined) {
            this.step.lookup.header = [];
            let fields = curStep.lookup.header;
            fields.forEach(item => {
              this.step.lookup.header.push(item);
            })
          }
        }
        let outFields = store.getters.generateOutputFields(params);
        this.step['outFields'] = outFields
      },
      submit() {
        let step = Object.assign({}, this.step);
        step.urlInField = this.step.urlInField === true ? 'Y' : 'N';
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

        step.outFields.push({
          currency: "",
          decimal: "",
          format: "",
          group: "",
          length: -1,
          name: step.result.name,
          nullif: '',
          precision: -1,
          set_empty_string: "N",
          type: 'String',
        })
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

      addArg() {
        this.step.lookup.arg.push({
          name: '',
          parameter: '',
        })
      },
      getArg(){
        this.step.lookup.arg=[];
        this.step.outFields.forEach(item=>{
          this.step.lookup.arg.push({
            name:item.name,
            parameter:item.name
          })
        })
      },
      deleteArg(index) {
        this.step.lookup.arg.splice(index, 1);
      },
      addHeader() {
        this.step.lookup.header.push({
          name: '',
          parameter: ''
        })
      },
      getHeader(){
        this.step.lookup.header=[];
        this.step.outFields.forEach(item=>{
          this.step.lookup.header.push({
            name:item.name,
            parameter:item.name,
          })
        })
      },
      deleteHeader(index) {
        this.step.lookup.header.splice(index, 1)
      },
      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },
      getConstant() {
        this.$http.get('static/data.json').then(res => {
          this.encodings = res.body.formatters.encodings;
        })
      },
    }
  }
</script>

<style scoped>

</style>
