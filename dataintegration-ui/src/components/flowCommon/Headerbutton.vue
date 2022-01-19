<template>
  <div ref="projectInfo">
    <div class="opera_btn" id="opera_btn" >
      <ul>
        <!--          <li>{{transFinished}}</li>-->
        <el-tooltip content="运行" placement="bottom" effect="dark">
          <li @click="!projectInfo.isExecuted && execute()">
            <img src="../../../static/img/run.png" v-if="!projectInfo.isExecuted" alt="text"/>
            <img src="../../../static/img/run_dis.png" v-else/>
          </li>
        </el-tooltip>
        <el-tooltip content="停止" placement="bottom" effect="dark">
          <li @click=" !projectInfo.isPause && pause()">
            <img src="../../../static/img/stop.png" v-if="!projectInfo.isPause"/>
            <img src="../../../static/img/stop_dis.png" v-else/>
          </li>
        </el-tooltip>
        <el-tooltip content="保存" placement="bottom" effect="dark">
          <li @click="!projectInfo.isSave && addProject()">
            <img src="../../../static/img/add.png" v-if="!projectInfo.isSave"/>
            <img src="../../../static/img/add_dis.png" v-else/>
          </li>
        </el-tooltip>
        <el-tooltip content="另存" placement="bottom" key="8" effect="dark">
          <li @click="!projectInfo.isSave && saveAs()">
            <img src="../../../static/img/saevAs.png" v-if="!projectInfo.isSave"/>
            <img src="../../../static/img/saveAs_dis.png" v-else/>
          </li>
        </el-tooltip>
        <el-tooltip content="缩小画布" placement="bottom" effect="dark">
          <li @click="disRedues && addFlowSize('redues')">
            <img src="../../../static/img/redues.png" />
<!--            <img src="../../../static/img/reduse_dis.png" v-if="!isRedues"/>-->
          </li>

        </el-tooltip>
        {{canvasNumber}}%
        <el-tooltip content="放大画布" placement="bottom" effect="dark">
          <li @click="addFlowSize('add')">
            <img src="../../../static/img/increase.png"/>
          </li>
        </el-tooltip>
      </ul>
    </div>

    <!-- 状态显示开关 -->
    <div style="height: 30px; line-height: 27px;float: right; margin-right: 10px;">
      <el-switch v-model="statusFlag" active-color="#13ce66" inactive-color="#ff4949">
      </el-switch>
    </div>
  </div>
</template>

<script>
  import store from "../../vuex/store";
  export default {
    name: "Headerbutton",
    inject: ['reload'],
    props: {
      parentPath: String,
      projectInfo:Object,
      // statusFlag:Boolean,
      isRedues:Boolean,
      disRedues:Boolean,
    },

    watch: {
      btnStatus: function (newVal, oldVal) {
        if(!newVal.isExecuted){
          this.initData();
        }

      },
        // 如果路由有变化，会再次执行该方法
      $route(to,from){
      }

    },
    data() {
      return {
        statusFlag:true,
        canvasNumber:100,
      }
    },
    computed:{
      btnStatus(){
        return this.$store.state.flowMain.btnStatus[this.parentPath];
      }
    },
    mounted() {
      this.initData();
    },
    methods: {
      initData() {
        let btnStatus = this.$store.state.flowMain.btnStatus[this.parentPath];
        if(btnStatus !==undefined){
          if(btnStatus.isExecuted){
          }else{
            this.projectInfo.isExecuted = false;
            this.projectInfo.isSave = false;
            this.projectInfo.isPause=true;
          }
        }
      },
      execute(){
        this.$parent.execute();
      },
      pause(){
        this.$parent.pause();
      },
      addProject(){
        this.$parent.addProject();
      },
      saveAs(){
        this.$parent.saveAs();
      },
      addFlowSize(type){
        if(type ==='add'){
          this.canvasNumber = this.canvasNumber+10;
        }else{
          this.canvasNumber = this.canvasNumber-10;
        }
        this.$parent.ShrinkZoom(this.canvasNumber);
      },

    }
  }
</script>

<style scoped>

</style>
