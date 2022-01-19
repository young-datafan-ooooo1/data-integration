<template>
  <div>
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item class="demo-form-inline">
        <el-input clearable placeholder="请输入文件名称" v-model="fileName" size="small">
        </el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="init()">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="editFile()">上传</el-button>
      </el-form-item>
    </el-form>

    <el-table class="table_file" :data="tableData" height="400" border style="width: 100%" :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
      :row-style="{padding:'0px 0px'}" v-loading='tableLoading' @selection-change="handleSelect" ref="fileTable">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column label="文件名" prop="fileName"></el-table-column>
      <el-table-column label="分组" prop="groupName"></el-table-column>
      <el-table-column label="上传人" prop="createUserName"></el-table-column>
    </el-table>

    <div>
      <span slot="footer" class="dialog-footer" style="float: right">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </span>
    </div>

    <!-- 上传文件 -->
    <el-dialog width="50%" :title="title" :visible.sync="editVisiable" :close-on-click-modal="false" class="dialog_temp"
      :append-to-body="true" :modal-append-to-body="true" :before-close="colseEdit">
      <el-form :model="editForm" :rules="rules" label-width="100px" ref="editForm">
        <el-form-item label="文件" prop='fileRaw'>
          <el-input v-model="editForm.fileRaw" style="width: 250px;" :readonly="true"></el-input>
          <el-upload style="display: inline-block;" :auto-upload="false" :show-file-list="false" class="upload-demo upload-column"
            action="" :on-change="batchUploadBasicModel" :file-list="fileList" :before-upload="beforUpload">
            <el-button size="small" type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="文件名称" prop="fileName">
          <el-input v-model="editForm.fileName"></el-input>
        </el-form-item>
        <el-form-item label="文件类型" prop="fileType">
          <el-input v-model="editForm.fileType"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input v-model="editForm.notes"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="order ">
          <el-input v-model="editForm.order " type="number"></el-input>
        </el-form-item>
        <el-form-item label="是否有效" prop="isValid ">
          <el-switch v-model="editForm.isValid" active-color="#13ce66" inactive-color="#c91e3a" active-value="Y"
            inactive-value="F">
          </el-switch>
        </el-form-item>
        <el-form-item label="所属分组" prop="groupId">
          <el-select v-model="editForm.groupId" @focus="checkGroup">
            <el-option v-for="item in groupLists" :key="item.groupId" :value="item.groupId" :label="item.groupName"></el-option>
          </el-select>
          <el-button size="mini" type="primary" @click="addGroupViaiable=true">新增</el-button>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="colseEdit">取消</el-button>
        <el-button type="primary" @click="editData" :loading="editLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 新增分组 -->
    <el-dialog title="新增分组" :visible.sync="addGroupViaiable" width="30%" :before-close="closeAdd" :close-on-click-modal='false'
      class="dialog_temp" :append-to-body='true' :modal-append-to-body='false'>
      <el-form :model="groupForm" label-width="100px" ref="groupForm" :rules="groupRules">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model="groupForm.groupName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组描述" prop="describe">
          <el-input v-model="groupForm.describe" rows="2" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="分组排序" prop="groupOrder">
          <el-input v-model="groupForm.groupOrder" type="number"></el-input>
        </el-form-item>

        <el-form-item label="是否启用" class="plug-msg">
          <el-switch v-model="groupForm.enabled" active-color="#13ce66" inactive-color="#c91e3a" active-value="T"
            inactive-value="F">
          </el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeAdd()">取 消2</el-button>
        <el-button type="primary" @click="addData" :loading="addLoading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    addFileOperation,
    selectFileListByType,
    getFileGroup
  } from '../../api/api.js'
  import util from '../../common/utils.js'
  export default {
    props: {
      params: Object,
      fileType: String,
    },
    data() {
      return {
        editLoading: false,
        groupLists: [],
        addLoading: false,
        groupForm: {
          groupName: '',
          describe: '',
          groupOrder: 99,
          groupType: 'WJ',
          enabled: 'T'
        },
        groupRules: {
          groupName: [{
            required: true,
            trigger: 'blur',
            message: '分组名称不能为空'
          }],
          describe: [{
            required: true,
            trigger: 'blur',
            message: '分组描述不能为空',
          }],
          groupType: [{
            required: true,
            trigger: 'blur',
            message: '分组类型不能为空'
          }],

        },
        addGroupViaiable: false,
        fileList: [],
        editVisiable: false,
        tableData: [],
        fileName: '',
        tableLoading: false,
        selecData: {},
        title: '上传文件',
        editForm: {
          fileRaw: '',
          fileId: '',
          file: null,
          fileName: '',
          fileType: '',
          groupId: '',
          notes: '',
          order: 99,
          isValid: 'Y',
        },
        rules: {
          fileName: [{
            required: true,
            trigger: 'blur',
            message: '文件名称'
          }],
          fileType: [{
            required: true,
            trigger: 'blur',
            message: '请选择文件类型',
          }],
          groupId: [{
            required: true,
            trigger: 'blur,change',
            message: '请选择分组'
          }]
        },
      }
    },
    mounted() {
      this.init();
    },
    methods: {
      closeAdd() {
        this.groupForm = {
          groupName: '',
          describe: '',
          groupOrder: 99,
          groupType: 'WJ',
          enabled: 'T'
        };

        this.addGroupViaiable = false;
      },
      colseEdit() {
        this.$refs['editForm'].resetFields();
        this.editVisiable = false;
      },
      handleSelect(val) {
        if (val.length > 1) {
          this.$refs.fileTable.clearSelection()
          this.$refs.fileTable.toggleRowSelection(val[val.length - 1])
        }
        this.selecData = val[val.length - 1]
      },
      //上传文件
      /**
       * 编辑
       */
      editFile() {
        this.title = '新增';
        //获取分组信息
        this.getGroups();
        this.editVisiable = true;
      },
      //获取分组信息
      getGroups() {
        let para = {
          groupName: '',
          pageNum: 1,
          pageSize: 100
        }
        getFileGroup(para).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            let groupList = [];
            for (let i = 0; i < data.content.list.length; i++) {
              groupList.push({
                groupId: data.content.list[i].groupId + "," + data.content.list[i].groupName,
                groupName: data.content.list[i].groupName,
              })
            }
            this.groupLists = groupList;
          }
        })
      },
      //初始化数据
      init() {
        let param = this.params;
        this.tableLoading = true;
        selectFileListByType(param).then(res => {
          // 获取所有数据源
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.tableData = data.content;
            this.tableLoading = false;
          } else {
            this.tableLoading = false;
            // this.ct_loading = false;
          }
        });
      },
      batchUploadBasicModel(file) {
        let sufix = file.name.substr(file.name.lastIndexOf('.') + 1);
        const isCsv = file.name.substr(file.name.lastIndexOf('.') + 1) === this.fileType;
        if (this.fileType.indexOf(sufix) >= 0) {
          this.editForm.fileRaw = file.name;
          this.editForm.file = file.raw;
          this.editForm.fileName = file.name.substr(0, file.name.lastIndexOf('.'));
          this.editForm.fileType = file.name.substr(file.name.lastIndexOf('.') + 1);
        } else {
          this.$message({
            message: '请选择' + this.fileType + '文件',
            type: 'error'
          })
        }

      },
      // 文件上传前校验文件名称
      beforUpload(file) {
        const isCsv = file.type === this.fileType;
        return isCsv;
      },
      // 选择分组时判断是否有分组没有就新增
      checkGroup() {
        if (this.groupLists.length === 0) {
          // 创建分组
          this.addGroupViaiable = true;
        }
      },
      // 编辑数据
      editData() {
        this.$refs['editForm'].validate((valid) => {
          if (valid) {
            let params = Object.assign({}, this.editForm);
            params.groupId = this.editForm.groupId.split(",")[0];
            params.groupName = this.editForm.groupId.split(",")[1];

            let param = new FormData();
            param.append("file", params.file);
            param.append("fileName", params.fileName);
            param.append("fileType", params.fileType);
            param.append('groupId', params.groupId);
            param.append('groupName', params.groupName);
            param.append('notes', params.notes);
            param.append('order', params.order);
            this.editLoading = true;
            addFileOperation(param).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '上传文件成功',
                  type: 'success',
                });
              } else {
                this.$message({
                  message: '上传文件失败',
                  type: 'error'
                })
              }
              this.$refs['editForm'].resetFields();
              this.init();
              this.editLoading = false;
              this.editVisiable = false;
            })


          } else {

          }
        })

      },
      // 新增分组
      addData() {
        this.$refs['groupForm'].validate((valid) => {
          if (!valid) {} else {
            let para = Object.assign({}, this.groupForm);
            let params = {
              groupName: para.groupName,
              groupType: para.groupType,
            }


            this.addLoading = true;
            addGroup(para).then(res => {
              let {
                data
              } = res;
              if (data.code === '10000') {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                })
              } else {
                this.message({
                  message: '新增失败',
                  type: 'error'
                })
              }
              this.$refs['groupForm'].resetFields();
              this.addLoading = false;
              this.getGroups();
              this.addGroupViaiable = false;
              this.groupLists.push({
                groupId: this.groupForm.groupId + "," + this.groupForm.groupName,
                groupName: this.groupForm.groupName,
              })
            })
          }
        });
      },

      closeDialog() {
        util.$emit('close_dialog');
      },
      submit() {
        util.$emit('close_dialog');
      },

    }
  }
</script>

<style>
  .upload-demo input[type="file"] {
    display: none;
  }
  thead .el-table-column--selection {
    .cell {
      display: none;
    }
  }
</style>
