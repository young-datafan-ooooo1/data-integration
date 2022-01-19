<template>
  <div class="plugin_css">


    <el-collapse v-model="activeNames" @change="handleChange">
      <el-collapse-item v-for="(item,index) in plugins" :title="item.label" :key="index" :name="item.id">
        <div v-for="(sub) in item.children" :key="sub.id" >
          <el-tooltip class="item" effect="dark" :content="sub.label" placement="right">
            <div style="width: 100%;">
              <i :class="sub.pluginImage" :draggable="true" @dragstart="drag(sub)"></i>
            </div>
          </el-tooltip>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
  import {
    getBusinessPlugin
  } from '../../api/api.js'
  export default {
    data() {
      return {
        activeNames: [0,1],
        plugins: [], //控件信息
      };
    },
    mounted() {
      this.getBasicPlugin();
    },
    methods: {
      handleChange(val) {
        console.log(val);
      },

      drag(data) {
        this.$store.dispatch("setItem", JSON.stringify(data));
      },

      /**
       * 获取所有控件信息
       */
      getBasicPlugin() {
        let parament = [];
        let firstChild = [];
        // 获取业务插件
        (params).then(res => {
          // 获取业务插件
          let {
            data
          } = res;
          if (data.code === '10000') {
            let datas = data.content;

            let children = [];
            for (let i = 0; i < datas.length; i++) {
              let firpluginCategoryDTOs = datas[i].pluginCategoryDTOs;

              if (firpluginCategoryDTOs === null || firpluginCategoryDTOs === undefined) {
                let seconds = [];
                let pluginInfoDTOS = datas[i].pluginInfoDTOS;
                for (let k = 0; k < pluginInfoDTOS.length; k++) {
                  seconds.push({
                    nodeId: pluginInfoDTOS[k].pluginId,
                    id: pluginInfoDTOS[k].pluginId,
                    label: pluginInfoDTOS[k].pluginName,
                    categoryOrder: pluginInfoDTOS[k].categoryOrder,
                    pluginCategory: pluginInfoDTOS[k].pluginCategory,
                    pluginDescribe: pluginInfoDTOS[k].pluginDescribe,
                    pluginId: pluginInfoDTOS[k].pluginId,
                    pluginName: pluginInfoDTOS[k].pluginName,
                    pluginOrder: pluginInfoDTOS[k].pluginOrder,
                    pluginType: pluginInfoDTOS[k].pluginType,
                    pluginImage: pluginInfoDTOS[k].pluginImage === '' || pluginInfoDTOS[k].pluginImage ===
                      undefined || pluginInfoDTOS[k].pluginImage === null ? 'el-icon-question' : pluginInfoDTOS[
                        k].pluginImage,
                    secondPluginType: pluginInfoDTOS[k].secondPluginType,
                  })
                }
                firstChild.push({
                  nodeId: i,
                  id: i,
                  label: datas[i].category,
                  children: seconds,
                  pluginImage: 'el-icon-folder-opened',
                })

              } else {
                let children = [];
                for (let j = 0; j < firpluginCategoryDTOs.length; j++) {
                  let seconds = [];
                  let secondDtos = firpluginCategoryDTOs[j].pluginCategoryDTOs;
                  for (let k = 0; k < secondDtos.length; k++) {
                    let third = [];
                    let pluginDTOS = secondDtos[k].pluginInfoDTOS;
                    for (let h = 0; h < pluginDTOS.length; h++) {
                      third.push({
                        nodeId: pluginDTOS[h].modelName,
                        id: pluginDTOS[h].pluginId,
                        label: pluginDTOS[h].modelNameCn,
                        categoryOrder: pluginDTOS[h].categoryOrder,
                        pluginCategory: pluginDTOS[h].pluginCategory,
                        pluginDescribe: pluginDTOS[h].pluginDescribe,
                        pluginId: pluginDTOS[h].pluginId,
                        pluginName: pluginDTOS[h].pluginName,
                        pluginOrder: pluginDTOS[h].pluginOrder,
                        pluginType: pluginDTOS[h].pluginType,
                        modelType: pluginDTOS[h].modelType,
                        pluginImage: pluginDTOS[h].pluginImage === '' || pluginDTOS[h].pluginImage ===
                          undefined || pluginDTOS[h].pluginImage === null ? 'el-icon-question' : pluginDTOS[h].pluginImage,
                        modelName: pluginDTOS[h].modelName,
                        modelNameCn: pluginDTOS[h].modelNameCn,
                        secondPluginType: pluginDTOS[h].secondPluginType,
                      });
                    }
                    seconds.push({
                      id: k,
                      nodeId: j + '_' + k,
                      label: secondDtos[k].category,
                      children: third,
                      pluginImage: 'el-icon-folder-opened'
                    })
                  }
                  children.push({
                    id: j,
                    nodeId: i + '-' + j,
                    label: firpluginCategoryDTOs[j].category,
                    children: seconds,
                    pluginImage: 'el-icon-folder-opened'
                  })

                }

                firstChild.push({
                  nodeId: i,
                  id: i,
                  label: datas[i].category,
                  children: children,
                  pluginImage: 'el-icon-folder-opened'
                })
              }
            }
            parament.push({
              id: 1,
              nodeId: 1,
              label: '业务插件',
              children: firstChild,
              pluginImage: 'el-icon-folder-opened'
            })
          } else {
            this.$message({
              message: '查询控件结果失败',
              type: 'error'
            })
          }
          this.plugins = firstChild;
          let expends = [];
          this.plugins.forEach((item, index) => {
              expends.push(item.id);
          });
          this.activeNames = expends;
        });
      },
    }
  }
</script>

<style scoped>
  .el-collapse-item__arrow {
    margin: 0 0px 0 auto !important;
    transition: -webkit-transform .3s;
    -webkit-transition: -webkit-transform .3s;
    transition: transform .3s;
    transition: transform .3s, -webkit-transform .3s;
    font-weight: 300;
  }
</style>
<style>
  .el-collapse-item__arrow {
    margin: 0 0px 0 auto !important;
    transition: -webkit-transform .3s;
    -webkit-transition: -webkit-transform .3s;
    transition: transform .3s;
    transition: transform .3s, -webkit-transform .3s;
    font-weight: 300;
  }
</style>
