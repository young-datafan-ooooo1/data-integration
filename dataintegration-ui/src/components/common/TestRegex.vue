<template>
  <div>
    <div>
      <el-tabs>
        <el-tab-pane label="正则表达式">
          <el-form ref="file" :model="file" label-width="30%" size="mini">
            <span>请输入新的正则表达式或修改:</span>
            <el-form-item prop="scripts">
              <el-input type="textarea" :rows="2" v-model="file.scripts">
              </el-input>
            </el-form-item>
          </el-form>
          <el-divider content-position="left">要测试的值</el-divider>
          <el-form label-width="30%" size="mini" ref="file" :model="file">
            <el-form-item label="值1">
              <el-input @input="regexValid(file.value1,'value1')" v-model="file.value1" id="value1"></el-input>
            </el-form-item>
            <el-form-item label="值2">
              <el-input @input="regexValid(file.value2,'value2')" v-model="file.value2" id="value2"></el-input>
            </el-form-item>
            <el-form-item label="值3">
              <el-input @input="regexValid(file.value3,'value3')" v-model="file.value3" id="value3"></el-input>
            </el-form-item>
          </el-form>
          <el-divider content-position="left">Capture</el-divider>
          <el-form label-width="30%" size="mini" ref="file" :model="file">
            <el-form-item label="从值中比对">
              <el-input @input="groupCount(file.get,'get')" id="get" v-model="file.get"></el-input>
            </el-form-item>
            <el-form-item label="捕获的字段">
              <el-input v-model="file.getvalue"  style="white-space: pre-wrap;" :autosize="{ minRows: 4}" type="textarea"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer" style="float: right;">
      <el-button size="mini" @click="testCloss">取 消</el-button>
      <el-button type="primary" size="mini" @click="testSumbit">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import {
    groupCount, ismatch

  } from '../../api/api.js'

  export default {
    props: {
      scripts: String,
    },
    data() {
      return {
        timer: null,
        timer1:null,
        file: {
          scripts: "",
          value1: "",
          value2: "",
          value3: "",
          getvalue: "",
          get: "",
        }
      }
    },
    mounted() {
      this.file.scripts = this.scripts;
    },
    destroyed() {
      clearTimeout(this.timer);
      clearTimeout(this.timer1)
    },

    methods: {

      debounce(val, id) {
        var timerId;
        let _this = this;
        function debounced() {
          if (timerId) {
            clearTimeout(timerId);

            timerId = null;
          }
          timerId = setTimeout(function () {
            let result = false;
            if (val != null && val.length > 0) {
              let params = new FormData();
              params.append('regex', _this.file.scripts)
              params.append('matcherValue', val)
              ismatch(params).then(res => {
                let {
                  data
                } = res;
                if (data.code === '10000') {
                  result = data.content === true;
                  let doc = document.getElementById(id);
                  if (data.content === true) {
                    doc.className = 'el-input__inner valid'
                  } else {
                    doc.className = 'el-input__inner invalid'
                  }
                }
              })
            }
          }, 500);
        }

        return debounced;
      },

      regexValid(val, id) {
        let _this = this;
        // this.debounce(val,id);
        if(this.timer === null){
          this.timer = setTimeout(function () {
          let result = false;
          if (val != null && val.length > 0) {
            let params = new FormData();
            params.append('regex', _this.file.scripts)
            params.append('matcherValue', val)
            ismatch(params).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                result = data.content;
                let doc = document.getElementById(id);
                if (data.content === true) {
                  doc.className = 'el-input__inner valid'
                } else {
                  doc.className = 'el-input__inner invalid'
                }
              }
            })
          }
          return result;
          }, 1000)
        }else{
          clearTimeout(this.timer)
          this.timer = setTimeout(function () {
            let result = false;
            if (val != null && val.length > 0) {
              let params = new FormData();
              params.append('regex', _this.file.scripts)
              params.append('matcherValue', val)
              ismatch(params).then(res => {
                let {
                  data
                } = res;
                if (data.code === '10000') {
                  result = data.content;
                  let doc = document.getElementById(id);
                  if (data.content === true) {
                    doc.className = 'el-input__inner valid'
                  } else {
                    doc.className = 'el-input__inner invalid'
                  }
                }
              })
            }
            return result;
          }, 1000)
        }



      },
      groupCount(val,id) {
        let _this = this;
        if(this.timer1 === null){
          this.timer1 =setTimeout(function () {
            let result = false;
            if (val != null && val.length > 0) {
              let params = new FormData();
              params.append('regex', _this.file.scripts)
              params.append('matcherValue', val)
              groupCount(params).then(res => {
                let {
                  data
                } = res;
                let doc = document.getElementById(id);
                if (data.code === '10000') {
                  _this.file.getvalue = data.content;
                  result = true;
                  doc.className='el-input__inner valid'
                } else {
                  _this.file.getvalue = "";
                  doc.className='el-input__inner invalid';
                  result = false;
                }
              })
            }
            return result;
          },1000)
        }else{
          clearTimeout(this.timer1);
          // this.timer=null;
          this.timer1 = setTimeout(function () {
            let result = false;
            if (val != null && val.length > 0) {
              let params = new FormData();
              params.append('regex', _this.file.scripts)
              params.append('matcherValue', val)
              groupCount(params).then(res => {
                let {
                  data
                } = res;
                let doc = document.getElementById(id);
                if (data.code === '10000' && data.content!==false) {
                  // let datas =data.content.join('//n')
                  let datas='';
                  data.content.forEach(item=>{
                    if(datas ==''){
                      datas = item;
                    }else{
                      datas = datas+'\n'+item
                    }

                  })

                  _this.file.getvalue = datas;
                  result = true;
                  doc.className='el-input__inner valid'
                } else {
                  _this.file.getvalue = "";
                  doc.className='el-input__inner invalid';
                  result = false;
                }
              })
            }
            return result;
          },1000)
        }


      },
      // ValueOne() {

      // },

      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
      },

      testCloss() {
        this.$parent.$parent.regexEvalVisiable = false;
      },
      testSumbit() {
        this.$parent.$parent.regexEvalVisiable = false;
        this.$parent.$parent.step.script = this.file.scripts;
      },
    },
  }
</script>

<style>
  .valid {
    background: rgb(85, 202, 113)
  }

  .invalid {
    background: rgb(212, 79, 80);
    color:#FFFFFF;
  }

</style>
