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
        @node-click="selectTable"
      >
        <span class="custom-tree-node" slot-scope="{node,data}">
          <span class="node" draggable="true">{{node.label}}</span>
          <i class="el-icon-loading" v-if="data.nodeFlag"></i>
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
      <el-button type="primary" :loading="loading" @click="getFileds">确 定</el-button>
    </div>
  </div>
</template>

<script>
  import {getStatement, getStatementSql} from '../../api/api'

  export default {
    name: "TableViews",
    props: {
      loading: Boolean,
      treeData: Object,
      connection: String,
    },
    data() {
      return {
        nodeFlag:false,
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
        console.info("this.treeData",this.treeData);
        for (let key in this.treeData) {
          let data = this.treeData[key];
          let children = [];
          if(data===null || data === ''){
            data = [];
          }
          data.forEach((item, index) => {
            children.push({
              id: key + '_' + index,
              label: item,
              nodeFlag:false,
            })
          })
          this.dataSoueceData.push({
            id: key,
            label: key,
            children: children
          })
        }
      },

      /**
       * 关闭弹窗
       */
      closeAdd() {
        this.$parent.$parent.coloseTable();
      },
      selectTable(nodeData, node, ev) {
        console.info("nodeData-=-=",nodeData);
        nodeData.nodeFlag = true;
        //判断节点等级如果是1级节点不做任何操作
        if (node.level === 1) {

        } else if (node.level === 2) {
          if (node.parent.data.id === 'schemas') {
            // nodeData.label="sdd";
            let params = {
              datasourceName: this.connection,
              schema: nodeData.label,
            };

            this.nodeFlag = true;
            getStatement(params).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                let children = node.parent.data.children;
                let result = data.content.tables;
                children.forEach((item, index) => {
                  if (item.label === nodeData.label) {
                    let child = [];
                    result.forEach((table, i) => {
                      child.push({
                        id: item.label + "_" + i,
                        label: table,
                      })
                    });
                    this.$set(nodeData, 'children', child)
                  }

                })
              }
              nodeData.nodeFlag=false;
              this.nodeFlag = false;
            })
          } else {
            this.getTableSql('', nodeData.label)
          }
          //判断是否为schemas
        } else if (node.level === 3) {
          this.getTableSql('', nodeData.label);
        }

      },

      /**
       * 获取表的sql语句
       * @param schemas schemas
       */
      getTableSql(schemas, tableName) {
        let params = {
          datasourceName: this.connection,
          schema: schemas,
          tables: tableName
        }
        getStatementSql(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.sql = data.content;
          }
        })
      },
      filterNode() {
      },
      getFileds() {
        this.$parent.$parent.formatSql(this.sql);
      },
    }
  }
</script>

<style scoped>

</style>
