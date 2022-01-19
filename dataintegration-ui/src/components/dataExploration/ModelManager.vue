<template>
  <div class="modelManager">
    <div class="dialogHeader">
      <div class="headerSpan_left1" id="headerSpan_left1" @click="changeSaveModel">
        <span>保存中</span>
      </div>
      <div class="headerSpan_right1" id="headerSpan_right1" @click="changeModelhistory">
        <span>保存历史</span>
      </div>

    </div>

    <div class="toolbar" v-show="modelFlag">
      <el-form :inline="true" size="mini" :model="modelForm" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="modelName" placeholder="模型名称" clearable></el-input>
        </el-form-item>

        <el-form-item>
          <el-date-picker v-model="modelDate" type="datetimerange" range-separator="-" start-placeholder="开始日期"
                          end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="getHistoryModel()" icon="el-icon-search"></el-button>
        </el-form-item>

      </el-form>
    </div>

    <el-table v-if="!modelFlag" :data="savingData" order style="width: 100%;table-layout: fixed;" border height="300"
              v-loading="modelLoading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="项目名称" prop="projectName">
      </el-table-column>
      <el-table-column label="模型名称" prop="modelName">
      </el-table-column>
      <el-table-column label="状态" prop="status" width="80">
      </el-table-column>
      <el-table-column label="开始时间" prop="startTime">
      </el-table-column>
      <el-table-column label="时长" prop="execSecond">
      </el-table-column>
      <el-table-column label="创建人" prop="userName">
      </el-table-column>
    </el-table>

    <el-table v-if="modelFlag" :data="modelHistory" order style="width: 100%;table-layout: fixed;" border height="300"
              v-loading="modelLoading" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column type="index" lable="序号"></el-table-column>
      <el-table-column label="项目名称" prop="projectName">
      </el-table-column>
      <el-table-column label="模型名称" prop="modelName">
      </el-table-column>
      <el-table-column label="状态" prop="status" width="80">
      </el-table-column>
      <el-table-column label="开始时间" prop="startTime">
      </el-table-column>
      <el-table-column label="结束时间" prop="endTime">
      </el-table-column>
      <el-table-column label="运行时长" prop="execSecond" width="80">
      </el-table-column>
      <el-table-column label="创建人" prop="userName">
      </el-table-column>

    </el-table>
    <div class="footer">
      <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
                     :current-page="currentPage1"
                     :page-sizes="[10, 50, 100, 200]" :page-size="pageSize1"
                     layout="total, sizes, prev, pager, next, jumper" :total="total1">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {modelHistory, savingModel} from '../../api/api.js'

  export default {
    data() {
      return {
        modelFlag: false,
        modelName: '',
        modelForm: {},
        modelDate: [],
        savingData: [], //正在保存的模型
        modelHistory: [], //保存模型历史数据
        modelLoading: false,
        currentPage1: 1,
        pageSize1: 10,
        total1: 0,
      };
    },
    mounted() {
      this.changeSaveModel();
    },
    methods: {
      changeSaveModel() {
        this.modelFlag = false;
        this.$nextTick(() => {
          document.getElementById("headerSpan_right1").className = "headerSpan_right1";
          document.getElementById("headerSpan_left1").className = "headerSpan_left1_active";
          this.getModel();
        })
      },

      changeModelhistory() {
        this.modelFlag = true;
        this.$nextTick(() => {
          document.getElementById("headerSpan_left1").className = "headerSpan_left1";
          document.getElementById("headerSpan_right1").className = "headerSpan_right1_active";
          this.getHistoryModel();
        })
      },

      handleCurrentChange1(curPage) {
        this.currentPage1 = curPage;
        this.getHistoryModel();
      },
      handleSizeChange1(pageSize) {
        this.pageSize1 = pageSize;
        this.getHistoryModel();
      },

      getModel() {
        this.modelLoading = true;
        savingModel().then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.savingData = data.content;
            this.total1 = data.content.length;
          }
          this.modelLoading = false;
        })

      },

      getHistoryModel() {
        let starDate = '';
        let endDate = '';
        if (this.modelDate.length > 0) {
          var mm = this.modelDate[0].getMonth() + 1;
          var d = this.modelDate[0].getDate();
          var h = this.modelDate[0].getHours();
          var m = this.modelDate[0].getMinutes();
          var s = this.modelDate[0].getSeconds();
          mm = mm < 10 ? ("0" + mm) : mm;
          d = d < 10 ? ("0" + d) : d;
          h = h < 10 ? ("0" + h) : h;
          m = m < 10 ? ("0" + m) : m;
          s = s < 10 ? ("0" + s) : s;

          var mm1 = this.modelDate[1].getMonth() + 1;
          var d1 = this.modelDate[1].getDate();
          var h1 = this.modelDate[1].getHours();
          var m1 = this.modelDate[1].getMinutes();
          var s1 = this.modelDate[1].getSeconds();
          mm1 = mm1 < 10 ? ("0" + mm1) : mm1;
          d1 = d1 < 10 ? ("0" + d1) : d1;
          h1 = h1 < 10 ? ("0" + h1) : h1;
          m1 = m1 < 10 ? ("0" + m1) : m1;
          s1 = s1 < 10 ? ("0" + s1) : s1;
          starDate = this.modelDate[0].getFullYear() + '-' + mm + '-' + d + ' ' + h + ':' + m + ':' + s;
          endDate = this.modelDate[1].getFullYear() + '-' + mm1 + '-' + d1 + ' ' + h1 + ':' + m1 + ':' + s1;
        } else {
          starDate = '2000-01-01 00:00:00';
          endDate = '2999-01-01 00:00:00'
        }

        let param = {
          pageNum: 1,
          pageSize: 10,
          saveModelVO: {
            "startTime": starDate,
            "modelName": this.modelName,
            "endTime": endDate
          }
        }
        modelHistory(param).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.modelHistory = data.content.list;
            this.total1 = data.content.total;
          }
          this.modelLoading = false;
        })
      },
    }
  }
</script>

<style>
</style>
