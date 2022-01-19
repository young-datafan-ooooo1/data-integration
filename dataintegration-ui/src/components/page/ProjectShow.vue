<template>
  <div class="project_datas">
    <div class="general_title" v-if="show" id="header">
      <div class="general_title_icon">
        <div class="title_icon">
          <!-- <svg class="icon" aria-hidden="true">
            <use></use>
          </svg> -->
          {{userFirstName}}
        </div>
      </div>
      <div class="general_introduce">
        <div class="general_title_header">
          您好，{{userName}}，欢迎来到一站式数据集成平台
        </div>
        <div class="general_content">
          我们是以all in one box的方式提供专业高效、安全可靠的一站式大数据智能云研发平台。 同时能满足用户对数据治理的需求，赋予用户对外提供数据服务的能力。
        </div>
      </div>
      <div class="img_class">
        <img src="../../../static/img/computer.png">
      </div>
    </div>
    <!-- 项目展示 -->
    <div class="project_show" ref="project_show" id="project_show" v-has="'dataIntegra_newDataIntegra'">
      <div class="item_none" v-show="items.length===0">
        <div class="add_pro">
          <div class="icon_add" @click="addProject">
            <i class="iconfont icon-jia"></i>
            <div class="add_title">新建集成</div>
          </div>
        </div>
        <div class="add_pro_img">
          <img src="../../../static/img/addLogo.png">
        </div>
      </div>

      <el-row v-show="items.length>0" v-if="!moreProject">
        <el-col :xl="{span:'4-8'}" :lg="{span:'4-8'}" :md="{span:'4-8'}" :sm="{span:'4-8'}" v-has="'dataIntegra_addProject'">
          <div class="block_div" >
            <div class="block_content">
              <div class="icon_add" @click="addProject">
                <i class="iconfont icon-jia"></i>
                <div class="add_title">新建集成</div>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :xl="{span:'4-8'}" :lg="{span:'4-8'}" :md="{span:'4-8'}" :sm="{span:'4-8'}" v-for="item in items"
                :key="item.id">
          <div class="grid_project" :id="item.id" @mouseover="overFunction(item)" v-on:click="openProject(item)" @mouseleave="leaveFunc(item)">
            <div class="grid_content">
              <div class="pro_title"><i class="el-icon-tickets"></i>&nbsp;{{item.groupName}}</div>
              <div class="pro_name">
                {{item.label}}
              </div>
              <div class="pro_content" v-show="item.isdown">
                项目描述：{{item.description}}
              </div>
              <div class="diverde" v-show="item.isdown">
                <div style="height: 1px;width: 100%;background: #E8E8E8"></div>
              </div>
              <div class="pro_time" v-show="item.isdown">
                {{item.createTime}}
              </div>
              <div class="pro_status" v-show="!item.isdown">
                <div class="status_desc" v-if="item.projectOnlineDTO !==null">
                  <i class="iconfont icon-jiankong"></i>
                  {{item.projectOnlineDTO.status}} 审核人:{{item.projectOnlineDTO.reviewUserName}}
                </div>
                <div class="status_desc" v-if="item.projectOnlineDTO ===null">
                </div>
                <div class="status_btn"  v-has="'dataIntegra_addProject'">
                  <button class="btn_status" @click="openProject(item)">查看项目</button>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 显示更多项目 -->
      <div class="more-project" v-if="moreProject">
        <el-table :data="projectData" order style="width: 100%;table-layout: fixed;" height="90%"
                  v-loading="projectLoading"
                  :header-cell-style="{background:'#FAFAFA',color:'#606266'}">
          <el-table-column label="项目名称" prop="projectName">
          </el-table-column>
          <el-table-column label="创建时间" prop="createTime" sortable>
          </el-table-column>
          <el-table-column label="状态" prop="projectOnlineDTO.status" :formatter="statusFormat">
          </el-table-column>
          <el-table-column label="审核人" prop="projectOnlineDTO.reviewUserName">
          </el-table-column>
          <el-table-column label="更新时间" prop="updateTime" sortable>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="text" @click="openProject(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="footer" clearfix>
          <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
                         :current-page="currentPage1"
                         :page-sizes="[10, 50, 100, 200]" :page-size="pageSize1"
                         layout="total, sizes, prev, pager, next, jumper"
                         :total="total1">
          </el-pagination>
        </div>
      </div>
      <!--      <div class="dataexplore-img" v-show="items.length>0">-->
      <!--        <img src="../../../static/img/addLogo.png">-->
      <!--      </div>-->
    </div>
  </div>
</template>

<script>
  import {
    selectAllProject,
    selectProjectByProjectName,
    selectAllProjectBygroupId
  } from '../../api/api.js'
  import {
    openFlow
  } from '../../common/common.js'
  import util from '../../common/utils.js'


  export default {
    data() {
      return {
        headerHeight: 0,
        show: true,
        projectForm: {
          projectName: '',
          groupId: '',
        },
        currentPage1: 1,
        pageSize1: 10,
        total1: 0,
        showUserName: false,
        projectData: [],
        projectLoading: false,
        moreProject: false,
        count: 0,
        userFirstName: '',
        userName: this.$cookies.get('userName'),
        items: [],
      }
    },
    mounted() {
      this.headerHeight = $('#header').height();
      this.$refs.project_show.addEventListener('scroll', this.handleScroll, true)
      this.userFirstName = this.userName.substr(0, 1);
      this.getProjectByProjectName();
      this.initStyle();
      util.$on('addProject', (res) => {
        this.addProject();
      });
    },
    methods: {
      /**
       * 监控滚动条事件
       * */
      handleScroll(e) {
        let scrollTop = this.$refs.project_show.scrollTop;
        let doc = document.getElementById('project_show');
        if (scrollTop > 0) {
          // this.show = false;
          // doc.className = 'project_show_scroll';
        } else if(scrollTop === 1) {
          // this.$message("0dsdkfhakhd");
          // doc.className = 'project_show'
          // this.show = true;
        }else{
          // this.$message("99999999");
        }
      },

      initStyle() {
        let doc = document.getElementById('block_div');
      },
      overFunction(item) {
        let that = this;
        item.isdown = false;
        let id =item.id;
        // $('#'+id).click(function () {
        //   that.openProject(item);
        // })
      },

      // 格式化审核状态
      statusFormat(data) {
        if (data.projectOnlineDTO !== null) {
          if (data.projectOnlineDTO.status === 'CHECKING') {
            return '审核中';
          } else if (data.projectOnlineDTO.status === 'REFUSE') {
            return '驳回';
          } else if (data.projectOnlineDTO.status === 'OFFLINE') {
            return '下线'
          } else if (data.projectOnlineDTO.status === 'CANCEL') {
            return '撤回中'
          } else if (data.projectOnlineDTO.status === 'ONLINE') {
            return '上线'
          }
        }
      },

      handleSizeChange1(pageSize) {
        this.pageSize1 = pageSize;
        this.getProject();
      },
      handleCurrentChange1(currentPage) {
        this.currentPage1 = currentPage;
        this.getProject();
      },

      getProject() {
        this.projectData = [];
        let params = {
          pageNum: this.currentPage1,
          pageSize: this.pageSize1,
          projectName: this.projectForm.projectName,
          groupId: this.projectForm.groupId,
          projectType: 'JCJB',
        }
        this.projectLoading = true;
        selectAllProjectBygroupId(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.projectData = data.content.list;
            this.total1 = data.content.total;
            this.projectLoading = false;
          }
        });
      },

      /**
       * 展示更多项目
       */
      showMoreProject() {
        this.moreProject = true;
        this.getProject();
      },

      /**
       * 打开项目信息
       */
      openProject(datas) {
        openFlow(datas);
      },
      leaveFunc(item) {
        item.isdown = true;
      },

      // 选中的项目信息
      handleSelectionChange2(sels) {
        this.selectProject = sels;
        this.subDis = true;
        for (let i = 0; i < sels.length; i++) {
          if (this.selectProject[i].projectOnlineDTO == null || this.selectProject[i].projectOnlineDTO.status ===
            'REFUSE') {
            this.subDis = false;
          } else {
            this.subDis = true;
            return;
          }
        }
      },
      /**
       * @param {Object} params 获取项目信息
       */
      getProjectByProjectName(params) {
        let para = {
          pageNum: 1,
          pageSize: 10,
          projectName: '',
          projectType: 'JCJB',
        }
        selectProjectByProjectName(para).then(res => {
          let {
            data
          } = res;
          let subData = [];
          if (data.code === '10000') {
            for (let i = 0; i < data.content.list.length; i++) {
              let prChlid = data.content.list[i].projects;
              for (let j = 0; j < prChlid.length; j++) {
                let projectOnline = prChlid[j].projectOnlineDTO;
                let tmpOnline=[];
                if(projectOnline !==null){
                  if(projectOnline.status === 'ONLINE'){
                    projectOnline.status ='上线运行中'
                  }else if(projectOnline.status ==='OFFLINE'){
                    projectOnline.status ='已下线'
                  }else if(projectOnline.status ==='CHECKING'){
                    projectOnline.status ='审核中'
                  }else if(projectOnline.status ==='REFUSE'){
                    projectOnline.status ='驳回'
                  }else if(projectOnline.status ==='CANCELING'){
                    projectOnline.status='撤销申请中'
                  }else if(projectOnline.status ==='CANCEL'){
                    projectOnline.status ='已撤销'
                  }
                }
                subData.push({
                  id: prChlid[j].projectId,
                  label: prChlid[j].projectName,
                  projectId: prChlid[j].projectId,
                  projectName: prChlid[j].projectName,
                  opModel: prChlid[j].opModel,
                  groupId: prChlid[j].groupId,
                  description: prChlid[j].description === '' ? '该项目暂无描述' : prChlid[j].description,
                  groupName: prChlid[j].groupName,
                  projectOnlineDTO: prChlid[j].projectOnlineDTO,
                  isdown: true,
                  createTime: prChlid[j].createTime
                })
              }
            }
            this.items = subData;
          } else {
            this.$message({
              message: '获取项目信息失败',
              type: 'error',
            })
          }
        });
      },
      // 新增集成项目
      addProject() {
        this.count++;
        let itemList = {
          path: '',
          name: '数据集成' + this.count,
        }
        localStorage.setItem("count", this.count);
        util.$emit('changeControl', 'test');
        let createFlag = true;
        let path = '/SJTS' + this.getUUID();
        let newPath = '/project' + path;
        let name = '数据集成' + this.count;

        let createFlagData = {
          key: newPath,
          value: true,
        };
        this.$store.dispatch('setCreateFlag', JSON.stringify(createFlagData));
        this.$store.dispatch('setFlowMainLoading', false);
        this.$store.dispatch('addRouteTitle', name);
        // this.$router.addRoutes(routers);
        let executeFlag = {
          key: path,
          value: false,
        }
        this.$store.dispatch('setExecuFlag', executeFlag);
        this.$router.push(newPath);
      },
      getUUID() {
        var s = [];
        var hexDigits = "0123456789abcdefhijklmnopqrstuzwxyz";
        for (var i = 0; i < 5; i++) {
          s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        var uuid = s.join("");
        return uuid;
      },
    }
  }
</script>

<style>
  .el-col-lg-4-8 {
    width: 20% !important;
  }
</style>
