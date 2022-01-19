<template>
  <div class="pre_data">
    <el-table border ref="singleTable" id="dataAnaTable" :data="dataPre" highlight-current-row
              v-loading='tableLoading'
              height="100%"  style="overflow: auto;width: 100%; table-layout:fixed" stripe
              :header-cell-style="{background:'#f5f7fa',color:'#606266',width:'40px'}"
              :row-style="{padding:'3px 3px'}"
              :cell-style="{padding:'3px 3px'}">
      <el-table-column type="index" width="50" label="序号">
      </el-table-column>
      <el-table-column v-for="(item,index) in dataColumn" :show-overflow-tooltip='true' :label="item"
                       :key="item"
                       :property="item" maxWidth='200px' v-if="item!==undefined">
        <template slot-scope="scope">
          <span>{{scope.row[index]}}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>

<script>
  export default {
    name: "PreViewData",
    props:{
      dataPre:Array,
      dataColumn:Array,
    },
    data() {
      return {
        tableLoading: false,
      }
    },

  }
</script>

<style scoped>

</style>
