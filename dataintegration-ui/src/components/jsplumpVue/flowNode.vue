<template>
  <!--  @click="selectChange" v-popover:popover -->
  <div :class="isActives(clickNode)?'node-item_click':'node-item'" ref="node" :style="flowNodeContainer" v-on:dblclick="changeData"
    @keyup.dlete="deleteNode1" @mouseenter="showDelete" @mouseleave="hideDelete" @mouseup="changeNodeSite" @click.stop="editNode"
    @click="selectChange" v-contextmenu:contextmenu @inputColumn="showInput" v-popover:popover>
    <div class="node-con"><i :class="iconClass" class="type-icon"></i></div>
    <div :class="labelClass(node.id+1)?'node-label':'node-label'" :id="node.id+1">{{node.label}}</div>
    <!-- 失败状态 -->
    <!-- @click.stop="deleteNode" -->
    <div class="node-del" v-if="node.stepExecutionStatus==='STATUS_FAILD'">
      <i class="el-icon-circle-close"></i>
    </div>
<!--    <div class="node-del" v-if="node.stepExecutionStatus === 'STATUS_STOPPED' && node.errors === 0">-->
<!--      <i class="el-icon-remove-outline"></i>-->
<!--    </div>-->
<!--    <div class="node-del" v-if="node.stepExecutionStatus === 'STATUS_HALTED' && node.errors === 0">-->
<!--      <i class="el-icon-remove-outline"></i>-->
<!--    </div>-->
    <div class="node-del" v-if="node.errors>0 && node.stepExecutionStatus === 'STATUS_STOPPED'">
      <i class="el-icon-circle-close"></i>
    </div>
    <div class="node-del2" v-if="node.errors===0 && node.stepExecutionStatus === 'STATUS_STOPPED'">
      <i class="iconfont icon-tingzhi"></i>
    </div>
    <div class="node-del2" v-if="node.errors===0 && node.stepExecutionStatus === 'STATUS_HALTING'">
      <i class="iconfont icon-tingzhi"></i>
    </div>
    <div class="node-del" v-if="node.errors>0 && node.stepExecutionStatus !== 'STATUS_STOPPED'">
      <i class="el-icon-circle-close"></i>
    </div>
    <!-- 完成状态 -->
    <div class="node-del1" v-if="(node.errors===0 && node.stepExecutionStatus==='STATUS_FINISHED')">
      <i class="el-icon-circle-check"></i>
    </div>
    <!-- 运行状态 -->
    <div class="node-del2" v-if="node.stepExecutionStatus==='STATUS_RUNNING'">
      <i class="el-icon-loading"></i>
    </div>
    <!-- 停止状态 -->
    <!-- <div class="stopping" v-if="node.stepExecutionStatus==='STATUS_STOPPED'" @click.stop="deleteNode">
      <i class="el-icon-remove-outline"></i>
    </div> -->


    <v-contextmenu ref="contextmenu" :theme="theme">
      <v-contextmenu-item @click="changeData">编辑步骤</v-contextmenu-item>
      <v-contextmenu-item @click="deleteNode">删除步骤</v-contextmenu-item>
    </v-contextmenu>

    <div v-show="false">
      <el-popover ref="popover" placement="top" trigger="hover" :value="statusFlag" :disabled="statusFlag">
        <table border="0" class="table_class">
          <tr>
            <td width="100px">错误</td>
            <td>{{node.errors}}</td>
          </tr>
          <tr>
            <td width="100px">输入</td>
            <td>{{node.linesInput}}</td>
          </tr>
          <tr>
            <td width="100px">输出</td>
            <td>{{node.linesOutput}}</td>
          </tr>
          <tr>
            <td width="100px">读</td>
            <td>{{node.linesRead}}</td>
          </tr>
          <tr>
            <td width="100px">写</td>
            <td>{{node.linesWritten}}</td>
          </tr>
          <tr>
            <td width="100px">拒绝</td>
            <td>{{node.linesRejected}}</td>
          </tr>
          <tr>
            <td width="100px">修改</td>
            <td>{{node.linesUpdated}}</td>
          </tr>

          <tr>
            <td width="100px">速度(条/秒)</td>
            <td>{{node.speed}}</td>
          </tr>
          <tr>
            <td width="100px">状态描述</td>
            <td>{{node.statusDescription}}</td>
          </tr>
          <tr>
            <td width="100px">运行时间</td>
            <td>{{node.seconds}}</td>
          </tr>
          <tr>
            <td width="100px">运行状态</td>
            <td>{{node.stepExecutionStatus}}</td>
          </tr>
          <tr>
            <td width="50px">插件名称</td>
            <td>{{node.label}}</td>
          </tr>
        </table>
      </el-popover>
    </div>

    <!--连线用--//触发连线的区域，默认不显示-->
    <div class="flow-node-drag" v-show="isconnect"></div>

    <el-dialog :title="title" :visible.sync="centerDialogVisible" width="50%" :before-close="cloaseDialog" v-if="centerDialogVisible">
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
      <div id="test" ref="component" :data="nodeData"></div>

    </el-dialog>


  </div>
</template>

<script>
  import util from '../../common/utils.js'
  import Vue from 'vue'
  export default {
    props: {
      node: Object,
      isconnect: Boolean,
      clickNode: String,
      statusFlag: Boolean,
    },
    data() {
      return {
        tableData: [],
        columnViaiable: false,
        columnTitle: '',
        theme: 'string',
        title: '',
        flag: true,
        mouseEnter: false,
        centerDialogVisible: false,
        status: false,
        nodeData: this.node,
        myComponent: null,
        keyUp: true,
      }
    },
    created() {

    },
    computed: {

      // 节点容器样式
      flowNodeContainer: {
        key() {
          return this.$route.name !== undefined ? this.$route.name + new Date() : this.$route + new Date()
        },
        get() {
          return {
            position: 'absolute',
            width: '40px',
            top: this.node.top,
            left: this.node.left,
            boxShadow: this.mouseEnter ? '#66a6e0 0px 0px 12px 0px' : '',
          }
        }
      },
      iconClass() {
        this.status = this.node.status;
        return this.node.icon;
      }

    },

    mounted() {
      this.init();
    },
    methods: {
      init(){
      },
      labelClass(nodeId) {
        this.$nextTick(() => {
          let width = $('#' + nodeId).width();
          if (width > 40) {
            let doc = document.getElementById(nodeId);
            let left = -((width - 42) / 2);
            doc.style.left = left + 'px';
          }
        })
        return true;
      },
      isActives(clickNode) {
        return clickNode === this.node.id;
      },
      // 显示输入字段
      showInput() {
        this.$emit('show-input', this.node);
      },
      // 显示输出字段
      showOutput() {
        this.$emit('show-output', this.node);
      },
      handleClick(vm, event) {
        console.log(vm, event)
      },
      handleSubmenuShow(vm, placement) {
        console.log(vm, placement)
      },
      showStatus() {
        if (this.node.errors === 0) {
          //显示运行状态弹窗
          this.flag = false;
        }
        this.selectChange();
      },
      // 关闭弹窗
      closeDialog() {
        this.centerDialogVisible = false;
      },
      //编辑节点数据
      changeData() {
        this.$emit('change-data', this.node)
      },

      cloaseDialog(done) {
        this.centerDialogVisible = false;
      },

      //返回上一个菜单
      back() {
        let view = this.$route;
        this.$store.dispatch('delVisitedViews', view).then((views) => {
          if (this.isActive(view)) {
            const latestView = views.slice(-1)[0]
            if (latestView) {
              this.$router.push(latestView.path)
            } else {
              this.$router.push('/genneralView')
            }
          }
        })
      },
      submit() {
        this.centerDialogVisible = false;
      },

      isActive(route) {
        return route.path === this.$route.path
      },
      // 删除节点
      deleteNode() {
        this.$emit('delete-node', this.node.label)
      },
      // 编辑节点
      editNode() {
        this.$emit('edit-node', this.node.id)
      },
      selectChange(event) {
        // if (this.node.errors === 0) {
        //   //显示运行状态弹窗
        //   this.flag = false;
        // }
        let doc = document.getElementById(this.node.id);
        // doc.className='node-item_click';
        // $('#'+this.node.id).toggleClass('node-item_click');
        this.$emit('change-node', this.node);
      },
      // 鼠标进入
      showDelete() {
        this.mouseEnter = true;
      },
      // 鼠标离开
      hideDelete() {
        this.mouseEnter = false;
      },
      // 鼠标移动后抬起
      changeNodeSite() {
        let data = {
          nodeId: this.node.id,
          left: this.$refs.node.style.left,
          top: this.$refs.node.style.top,
        };
        util.$emit("changeNodeSite", data);
      }
    }
  }
</script>
<style>
  .node-item {
    border-radius: 4px;
    text-align: center;
    box-shadow: 0 0 2px #696969;
    cursor: default;
    overflow: initial;
    position: relative;
    /* background: #fbf4dc; */
    /* padding: 0 10px; */
  }

  .node-item_click {
    width: 40px;
    -moz-box-shadow: 0 0 20px #1eaafa;
    -webkit-box-shadow: 0 0 20px #1eaafa;
    box-shadow: 0 0 20px #1eaafa;
    text-align: center;
    cursor: default;
    overflow: initial;
    position: relative;
    border-radius: 4px;
  }

  .node-titel {
    height: 20px;
    background: #ffc400;
  }

  .node-icon {
    position: absolute;
    top: 0px;
    right: 0px;
    line-height: 20px
  }


  .node-icon i {
    cursor: pointer;
  }

  .node-con {
    text-align: center;
    /* line-height: 30px; */
    margin: 0 auto;
    cursor: default;
  }

  .node-con .iconfont {
    font-size: 25px;
    color: #1eaafa;
  }

  .node-label {
    margin-top: 7px;
    text-align: left;
    position: absolute;
    font-size: small;
    overflow: initial;
    white-space: nowrap;
  }

  .node-del {
    position: absolute;
    color: red;
    font-size: 16px;
    cursor: pointer;
    top: -8px;
    right: -8px;
  }

  .node-del1 {
    position: absolute;
    color: #42B983;
    font-size: 16px;
    cursor: pointer;
    top: -8px;
    right: -8px;
  }

  .node-del2 {
    position: absolute;
    color: #767676;
    font-size: 16px;
    cursor: pointer;
    top: -8px;
    right: -8px;
  }

  .status_ing {
    position: absolute;
    top: -100px;
    right: -80px;
  }

  .flow-node-drag {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 100;
    cursor: pointer;
  }

  .flow-node-drag1 {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 100;
    cursor: pointer;
  }


  .status {
    position: absolute;
    left: 0;
    top: 0;
  }

  .table_class tr:nth-child(odd) {
    background: #ccc;
  }

  .v-contextmenu .v-contextmenu-item {
    font-family: Verdana, "Lucida Sans", Arial, Helvetica, sans-serif;
    padding: 5px 14px;
    line-height: 1;
    color: #333;
  }

  .v-contextmenu .v-contextmenu-item.v-contextmenu-item--hover {
    font-family: Verdana, "Lucida Sans", Arial, Helvetica, sans-serif;
    color: #FFF;
    background-color: darkgray;
    /* color: rgb(74, 74, 74); */
  }

  .v-contextmenu {
    width: 200px;
  }
</style>
