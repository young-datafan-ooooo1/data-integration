<template>

  <div class="plugin_tree">
    <div class="fileter_text">
      <el-input placeholder="输入关键字进行过滤" v-model="filterText">
        <i slot="suffix" class="el-input__icon el-icon-close" @click="clear()"></i>
      </el-input>
    </div>
    <div class="plugin_tree_data" v-loading="treeLoading">
      <el-tree class="filter-tree" :data="data" :props="defaultProps" :default-expanded-keys="expendedKey"
               :filter-node-method="filterNode" ref="tree" node-key="nodeId">
        <span class="custom-tree-node" slot-scope="{node,data}">
          <i :class='data.pluginImage'></i>
          <el-tooltip class="item" effect="dark" :content="node.label" placement="right">
            <span class="node" draggable="true" @dragstart="drag(data)">{{node.label}}</span>
          </el-tooltip>
        </span>
      </el-tree>
    </div>
  </div>

</template>

<script>
  import {
    jsPlumb
  } from 'jsplumb'

  import {
    selectBusinessModel,
    selectDatasource,
    selectBusinessMetaData
  } from '../../api/api.js'

  import {
    getBasicPlugin,
    getBusinessPlugin
  } from '../../api/api.js'

  import util from '../../common/utils.js'

  export default {
    watch: {
      filterText(val) {
        this.$refs.tree.filter(val);
      }
    },

    data() {
      return {
        expendedKey: [],
        treeLoading: false,
        filterText: '',
        data: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },

        modelMetaData: [],
        connection: {
          name: null,
          server: "",
          type: "GENERIC",
          access: "Native",
          database: "",
          port: 0,
          username: "",
          password: "",
          servername: "",
          data_tablespace: "",
          index_tablespace: "",
          attributes: {
            attribute: [{
              code: "PORT_NUMBER",
              attribute: 0
            }]
          }
        },
        step: {
          name: null,
          type: "BasicModelInput",
          modelName: "",
          description: "",
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          isBusiness: "Y",
          connection: "clickhouse",
          sql: 'select * from default.type_test',
          limit: 0,
          lookup: "",
          execute_each_row: "N",
          variables_active: "N",
          lazy_conversion_active: "N",
          model_name: "数据类型测试模型",
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 176,
            yloc: 160,
            draw: "Y"
          },
          fields: {
            field: []
          }
        },
      };
    },
    mounted() {
      this.getBasicPlugin();
      util.$on('getBasicPlugin', () => {
        this.getBasicPlugin();
      })
    },
    methods: {
      // 清空筛选框
      clear() {
        this.filterText = '';
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },

      // 每次拖拉节点初始化数据
      init() {
        this.connection = {
          name: null,
          server: "",
          type: "",
          access: "Native",
          database: "",
          port: 0,
          username: "",
          password: "",
          servername: "",
          data_tablespace: "",
          index_tablespace: "",
          attributes: {
            attribute: [{
              code: "PORT_NUMBER",
              attribute: 0
            }]
          }
        };
        this.step = {
          name: null,
          type: "BasicModelInput",
          modelName: "",
          description: "",
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          isBusiness: "Y",
          connection: "",
          sql: '',
          limit: 0,
          lookup: "",
          execute_each_row: "N",
          variables_active: "N",
          lazy_conversion_active: "N",
          model_name: "数据类型测试模型",
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 176,
            yloc: 160,
            draw: "Y"
          },
          fields: {
            field: []
          }
        };
      },

      // 拖动插件到画布
      drag(data) {
        // 判断插件类型
        this.init();
        console.info("======", data);
        if (data.modelName === undefined) {
          this.currentItem = data;
          let step = {
            name: data.label,
            type: data.pluginId
          }
          this.$store.dispatch("setItem", JSON.stringify(data));

        } else {
          this.currentItem = data;
          this.$store.dispatch("setItem", JSON.stringify(data));
        }
      },


      getBasicPlugin() {
        // 获取业务插件
        this.treeLoading = true;
        getBusinessPlugin().then(res => {
          // 获取业务插件
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.data  = data.content;
            this.treeLoading = false;
          } else {
            this.$message({
              message: '查询控件结果失败',
              type: 'error'
            })
            this.treeLoading = false;
          }

        });

      }
    }
  };
</script>

<style>
</style>
