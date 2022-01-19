<template>
     <div class="dialog_body">
              <el-tabs v-model="active">
                <el-tab-pane label="一般" name="normal">
                  <div>
                    <div>
                      <el-form :model="step" size="mini" label-width="25%">
                        <el-form-item label="连接名称" prop="connectName">
                          <el-input v-model="step.connecName" style="width: 80%"></el-input>
                        </el-form-item>
                      </el-form>
                    </div>
                    <div class="Bfather">
                      <div class="son1">
                        <p>连接类型</p>
                        <div class="son_child1"></div>
                        <div class="son_child2"></div>
                        <div ></div>
                      </div>
                      <div class="son2">
                        <p>设置</P>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="高级" name="special">
                  <div class="Build_father">
                  <div class="Build">
                    <p>标识符</p>
                    <div>
                      <el-form :model="step" size="mini">
                        <el-form-item>
                          <el-checkbox v-model="step.bool">支持bool数据类型</el-checkbox>
                          <el-checkbox v-model="step.time_stamp">支持时间戳数据类型</el-checkbox>
                        </el-form-item>
                        <el-form-item>
                          <el-checkbox v-model="step.identifier">标识符使用引号</el-checkbox>
                          <el-checkbox v-model="step.identifier_captial">强制标识符大写字母</el-checkbox>
                          <el-checkbox v-model="step.identifier_lowercase">强制标识符小写字母</el-checkbox>
                        </el-form-item>
                        <el-form-item>
                          <el-checkbox v-model="step.preserve_case">保留字母大小写</el-checkbox>
                          <el-checkbox v-model="step.strict_number">Strict NUMBER(38) Interpretation(for orcal db only)</el-checkbox>
                        </el-form-item>
                        <el-form-item label="默认名称（在没有其它名称下使用）">
                          <el-input v-model="step.defalut_mode"></el-input>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>
                  <div style="margin-top: 40px;">
                    <p>请输入连接成功后要执行的Sql语句，以;隔开</p>
                    <div class="son_child2" style="overflow: hidden;width: 580px;">
                      <el-form :model="step" size="mini">
                        <el-form-item>
                          <el-input type="textarea" v-model="step.Excute_sql" :autosize="{ minRows:10, maxRows:10}"></el-input>
                        </el-form-item>
                         </el-form>
                    </div>
                  </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="选项" name="option">
                  <el-form :model="step" size="mini">
                  <div class="Bfather">
                    <div style="float: left;">命名参数</div>
                    <div style="margin-left:600px">
                      <el-form-item>
                        <el-button  @click="addRow()">新增</el-button>
                      </el-form-item>
                    </div>
                  </div>
                    <el-form-item>
                      <el-table 
                      border
                      :date="Named_parameters"
                      :header-cell-style="{background:'#eef1f6'}"
                      height="480px">
                      <el-table-column prop="NmParamete" label="命名参数">
                        <template slot-scope="scope">
                          <el-input size="mini" v-model="scope.row.NmParamete" ></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column prop="ParameteValue" label="值">
                        <template slot-scope="scope">
                          <el-input size="mini" v-model="scope.row.ParameteValue" ></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column fixed="right" label="操作">
                      <template slot-scope="scope">
                        <el-button
                          @click.native.prevent="deleteRow(scope.$index, Named_parameters)"
                          type="text"
                        >移除</el-button>
                      </template>
                </el-table-column>
                      </el-table>
                    </el-form-item>
                  </el-form>
                </el-tab-pane>
                <el-tab-pane label="连接池" name="Connection_pool">
                  <el-form :model="step" size="mini">
                    <el-form-item>
                      <el-checkbox v-model="step.Connectpool">使用连接池</el-checkbox>
                    </el-form-item>
                    <el-row>
                      <el-col :span="11">
                    <el-form-item label="初始大小">
                      <el-input v-model="step.connect_size"></el-input>
                    </el-form-item>
                      </el-col>
                      <el-col :span="11" style="margin-left: 10px;">
                    <el-form-item label="最大空闲空间">
                      <el-input v-model="step.free_space"></el-input>
                    </el-form-item>
                      </el-col>
                  </el-row>
                  <el-form-item label="命名参数:">
                    <el-table :date="Connectpool_parameter" border :header-cell-style="{background: '#eef1f6'}" height="200">
                        <el-table-column label="参数名"></el-table-column>
                        <el-table-column label="值"></el-table-column>
                    </el-table>
                  </el-form-item>
                  <div style="float: right;">
                    <el-button size="mini">恢复默认设置</el-button>
                  </div>
                  <el-form-item label="描述" style="margin-top:50px">
                    <el-input v-model="step.descripe" ></el-input>
                  </el-form-item>
                  </el-form>
                </el-tab-pane>
                <el-tab-pane label="集群" name="colony">
                  <el-form :model="step" size="mini">
                    <el-form-item>
                      <el-checkbox v-model="step.cluster">使用集群</el-checkbox>
                    </el-form-item> <el-table :date="cluster_parameter" border :header-cell-style="{background: '#eef1f6'}" height="400">
                        <el-table-column label="分区ID"></el-table-column>
                        <el-table-column label="主机名称"></el-table-column>
                        <el-table-column label="端口"></el-table-column>
                        <el-table-column label="数据名称"></el-table-column>
                        <el-table-column label="用户名"></el-table-column>
                        <el-table-column label="密码"></el-table-column>
                    </el-table>
                  </el-form>
                </el-tab-pane>
              </el-tabs>
              <div class="footer">
                <el-button size="mini">测试</el-button>
                <el-button size="mini">特征列表</el-button>
                <el-button size="mini">浏览</el-button>
              </div>
            </div>
</template>
<script>
export default {
    
}
</script>
<style >
.footer{
  position: absolute;
  text-align: right;
  left: 0;
  right: 0;
  margin: 0px  250px;
  bottom: 20px;
}

.dialog_body{
 overflow: auto;
 padding: 10px 20px;
 height: 500px;
}
</style>