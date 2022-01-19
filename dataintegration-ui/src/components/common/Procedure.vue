<template>
  <div class="table_tree">
    <div style="text-align: center;padding-right: 20px">
      <el-input placeholder="输入关键字进行过滤" v-model="filterText" size="mini" clearable>
      </el-input>
    </div>
    <div style="height: 90%;padding-right: 0px;overflow: auto">
      <el-tree
        :data="dataSoueceData"
        :highlight-current="true"
        ref="tree"
        :filter-node-method="filterNode"
        @node-click="selectProcedure"
      >
        <span class="custom-tree-node" slot-scope="{node,data}">
          <span class="node" draggable="true">{{node.label}}</span>
        </span>
      </el-tree>
    </div>
    <div
      style="height: 50px;
                    line-height: 43px;text-align: center;"
      v-show="isShow"
    >
    </div>
    <div slot="footer" class="footer plugin_footer">
      <el-button @click="closeAdd()">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="submit">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import {getStatement, getStatementSql} from '../../api/api'

  export default {
    name: "procedrue",
    props: {
      loading: Boolean,
      treeData: Array,
      connection: String,
    },
    data() {
      return {
        procedure: '',
        nodeFlag: false,
        isShow: false,
        filterText: '',
        dataSoueceData: [],
        sql: '',
      }
    },
    mounted() {
      this.init();
    },
    methods: {

      init() {
        this.dataSoueceData = [];
        this.treeData.forEach(item => {
          this.dataSoueceData.push({
            id: item.name,
            label: item.name
          })
        });
      },

      /**
       * 关闭弹窗
       */
      closeAdd() {
        // this.$parent.$parent.coloseTable();
		this.$parent.$parent.preduceVisiable = false;
      },
      selectProcedure(nodeData, node, ev) {
        this.procedure = nodeData.label;
      },


      filterNode() {
      },
      submit() {
        this.$parent.$parent.setProcedure(this.procedure);
		this.$parent.$parent.preduceVisiable = false;
      },
    }
  }
</script>

<style scoped>

</style>
