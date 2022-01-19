<template>
  <div class="run">
    <div class="dialogHeader">
      <el-tabs v-model="activeName" stretch @tab-click="handleClick" style="width: 98%">
        <el-tab-pane v-for="(item,index) in items"  :label="item.label" :name="item.name" :key="item.name"></el-tab-pane>
        <!--        <el-tab-pane label="分组管理" name="second"></el-tab-pane>-->
      </el-tabs>

    </div>

    <div class="toolbar" v-show="flag">
      <el-form :inline="true" size="mini" :model="statusFrom" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="projectName" placeholder="项目名称"></el-input>
        </el-form-item>

        <el-form-item>
          <el-date-picker v-model="defaultDate" type="datetimerange" range-separator="-" start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="getHistory()" icon="el-icon-search"></el-button>
        </el-form-item>


      </el-form>
    </div>
    <div v-show="flag">
      <el-table :data="statusData" order style="width: 100%;table-layout: fixed;" border height="300" v-loading="statusLoading"
        :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="项目名称" prop="projectName">
        </el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatStatus" width="80">
        </el-table-column>
        <el-table-column label="开始时间" prop="startTime">
        </el-table-column>
        <el-table-column label="结束时间" prop="endTime">
        </el-table-column>
        <el-table-column label="运行时长" prop="execSecond" width="80">
        </el-table-column>
        <el-table-column label="运行人" prop="userName">
        </el-table-column>
      </el-table>
    </div>
    <!-- 运行中 -->
    <div v-show="!flag">
      <el-table :data="status" order style="width: 100%;table-layout: fixed;" border height="300" v-loading="statusLoading"
        :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
        <el-table-column type="index" lable="序号"></el-table-column>
        <el-table-column label="项目名称" prop="projectName">
        </el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatRun" width="80">
        </el-table-column>
        <el-table-column label="开始时间" prop="startTime">
        </el-table-column>
        <el-table-column label="运行时长" prop="execSecond" width="80">

        </el-table-column>
        <el-table-column label="运行人" prop="userName">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="stop(scope.row)">中止</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="footer" clearfix>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
        :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import {getStatus,getStatusHistory} from '../../api/api.js'
  export default {
    data() {
      return {
        items:[{
          resId:'dataExplore_manage_project',
          name:'run',
          label:'运行中',
        },{
          resId:'dataExplore_manage_group',
          name:'runned',
          label:'运行历史',
        }],
        activeName:'first',
        flag: false,
        statusFrom: {},
        projectName: '',
        defaultDate: [],
        statusData: [],
        statusLoading: false,
        currentPage: 1,
        pageSize: 10,
        total: 0,
        status:[],
      };
    },
    mounted() {
      // this.changeStatus();
      this.initData();
    },
    methods: {
      handleClick(tab,ev){
        let type =tab.name;
        if(type ==='run'){
          this.flag = false;
          this.getStatusData();
        }else{
          this.flag = true;
          this.getHistory();
        }
      },
      initData(){
        this.activeName =this.items[0].name;
        if(this.items[0].name === 'run'){
          this.flag=false;
          this.getStatusData();
        }else{
          this.flag = true;
          this.getHistory();
        }
      },
      formatStatus(data) {
        if (data.status === 'END') {
          return '已完成';
        } else if (data.status === 'TERMINATIN') {
          return '停止';
        }
      },
      formatRun(data) {
        return '运行中'
      },
      changeStatus() {
        this.flag = false;
        this.$nextTick(() => {
          document.getElementById("headerSpan_right2").className = "headerSpan_right2";
          document.getElementById("headerSpan_left2").className = "headerSpan_left2_active";
          this.getStatusData();
        })
      },

      changeHistory() {
        this.flag = true;
        this.$nextTick(() => {
          document.getElementById("headerSpan_left2").className = "headerSpan_left2";
          document.getElementById("headerSpan_right2").className = "headerSpan_right2_active";
          this.getHistory();
        })
      },

      stop(data) {
        // 中止项目运行
        // 获取项目运行编号

        let executorId = data.executorId;
        let stompClient = this.globalWs.ws;
        stompClient.send("/stop", {}, JSON.stringify({
          executorId: executorId,
          projectId: data.projectId,
        }));
        this.getStatusData();
      },

      // 获取运行历史
      getHistory() {
        let starDate = '';
        let endDate = '';
        if (this.defaultDate.length > 0) {
          var mm = this.defaultDate[0].getMonth() + 1;
          var d = this.defaultDate[0].getDate();
          var h = this.defaultDate[0].getHours();
          var m = this.defaultDate[0].getMinutes();
          var s = this.defaultDate[0].getSeconds();
          mm = mm < 10 ? ("0" + mm) : mm;
          d = d < 10 ? ("0" + d) : d;
          h = h < 10 ? ("0" + h) : h;
          m = m < 10 ? ("0" + m) : m;
          s = s < 10 ? ("0" + s) : s;

          var mm1 = this.defaultDate[1].getMonth() + 1;
          var d1 = this.defaultDate[1].getDate();
          var h1 = this.defaultDate[1].getHours();
          var m1 = this.defaultDate[1].getMinutes();
          var s1 = this.defaultDate[1].getSeconds();
          mm1 = mm1 < 10 ? ("0" + mm1) : mm1;
          d1 = d1 < 10 ? ("0" + d1) : d1;
          h1 = h1 < 10 ? ("0" + h1) : h1;
          m1 = m1 < 10 ? ("0" + m1) : m1;
          s1 = s1 < 10 ? ("0" + s1) : s1;
          starDate = this.defaultDate[0].getFullYear() + '-' + mm + '-' + d + ' ' + h + ':' + m + ':' + s;
          endDate = this.defaultDate[1].getFullYear() + '-' + mm1 + '-' + d1 + ' ' + h1 + ':' + m1 + ':' + s1;
        } else {
          starDate = '2000-01-01 00:00:00',
            endDate = '2999-01-01 00:00:00'
        }
        this.statsData = [];
        let param = {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          projectHistoryExecuteVO: {
            endTime: endDate,
            projectName: this.projectName,
            startTime: starDate,
          }
        }
        this.statusLoading = true;
        getStatusHistory(param).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.total = data.content.total;
            this.statusData = data.content.list;
          }
          this.statusLoading = false;
        })
      },

      // 获取运行中的项目信息
      getStatusData() {
        this.statsData = [];
        this.statusLoading = true;
        getStatus().then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.status = data.content;
            this.total = data.content.length;
          }
          this.statusLoading = false;
        })
      },

      handleCurrentChange(curPage) {
        this.currentPage = curPage;
        this.getHistory();
      },
      handleSizeChange(pageSize) {
        this.pageSize = pageSize;
        this.getHistory();
      },
      // 获取运行状态信息
      getStatus() {
        this.statusVisible = true;
        this.$nextTick(() => {
          this.changeStatus();
        })
      },
    },
  }
</script>

<style>
</style>
