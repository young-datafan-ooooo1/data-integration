<template>
  <div class="upload_file">
    <el-form :model="editForm" :rules="rules" label-width="100px" ref="editForm">
      <el-form-item label="文件" prop='fileRaw' v-if="btnShow">
        <el-input v-model="editForm.fileRaw" style="width: 250px;" :readonly="true"></el-input>
        <el-upload style="display: inline-block;" :auto-upload="false" :show-file-list="false"
                   class="upload-demo upload-column"
                   action="" :on-change="batchUploadBasicModel" :file-list="fileList" :before-upload="beforUpload"
                   :accept="acceptType">
          <el-button size="small" type="primary">选择文件</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item :label="labelFile" prop="fileName">
        <el-input v-model="editForm.fileName"></el-input>
      </el-form-item>
      <el-form-item label="文件类型" prop="fileType" v-if="btnShow">
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
          <el-option v-for="item in groupLists" :key="item.groupId" :value="item.groupId"
                     :label="item.groupName"></el-option>
        </el-select>
        <el-button size="mini" type="primary" @click="addGroupViaiable=true">新增</el-button>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="colseEdit">取消</el-button>
      <el-button type="primary" @click="editData" :loading="editLoading">确定</el-button>
    </div>

    <!-- 新增分组 -->
    <el-dialog title="新增分组" :visible.sync="addGroupViaiable" width="30%" :before-close="closeAdd"
               :close-on-click-modal='false'
               class="dialog_temp other_dialog" :append-to-body='true' :modal-append-to-body='false'>
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
        <el-button @click="closeAdd()">取 消</el-button>
        <el-button type="primary" @click="addData" :loading="addLoading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    addFileOperation,
    selectFileListByType,
    getFileGroup,
    addGroup,
    checkFileName,
    updateFileOperation,
    getFileByFileId
  } from '../../api/api.js'
  import util from '../../common/utils.js'

  export default {
    props: {
      params: Object,
      fileType: String,
      createChannel: String,
      fileFolder: String,
    },
    data() {
      return {
        acceptType: '',
        labelFile: '文件名称',
        fileData: {},
        btnShow: false,
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
          groupType: [{
            required: true,
            trigger: 'blur',
            message: '分组类型不能为空'
          }],

        },
        fileList: [],
        tableData: [],
        fileName: '',
        addGroupViaiable: false,
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
      let types = this.fileType.split(',');
      let type = [];
      types.forEach((item, index) => {
        type.push('.' + item);
      })
      this.acceptType = type.join(',')
      if (this.fileFolder === '1') {
        this.labelFile = '目录名称';
        this.btnShow = false;
      } else {
        this.labelFile = '文件名称';
        this.btnShow = true;
      }
      this.getGroups();
    },
    methods: {
      closeAdd() {

        this.addGroupViaiable = false;
      },
      colseEdit() {
        this.$parent.$parent.selectFileVisiable = false;
        this.$refs['editForm'].resetFields();
        this.editVisiable = false;
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

      // 文件上传是检查
      batchUploadBasicModel(file) {
        let sufix = file.name.substr(file.name.lastIndexOf('.') + 1);
        const isCsv = file.name.substr(file.name.lastIndexOf('.') + 1) === this.fileType;
        if (this.fileType.indexOf(sufix) >= 0) {
          this.editForm.fileRaw = file.name;
          this.editForm.file = file.raw;
          this.editForm.fileName = file.name;
          this.editForm.fileType = file.name.substr(file.name.lastIndexOf('.') + 1);
        } else {
          this.acceptType = '';
          this.editForm.fileRaw = file.name;
          this.editForm.file = file.raw;
          this.editForm.fileName = file.name;
          this.editForm.fileType = file.name.substr(file.name.lastIndexOf('.') + 1);
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
            param.append('createChannel', this.createChannel);
            param.append('isFolder', this.fileFolder);
            this.editLoading = true;
            let checkParam = {
              fileName: params.fileName,
            }
            checkFileName(checkParam).then(response => {
              let {
                data
              } = response;
              if (response.code === '10000') {
                if (response.content === undefined) {
                  addFileOperation(param).then(res => {
                    let {
                      data
                    } = res;
                    if (data.code === '10000') {
                      this.$message({
                        message: '上传文件成功',
                        type: 'success',
                      });
                      let fileData = res.data.content;
                      this.$refs['editForm'].resetFields();
                      this.$parent.$parent.getFileData(fileData);
                    } else {
                      this.$message({
                        message: data.msg,
                        type: 'error'
                      })
                    }
                    this.editLoading = false;
                    this.editVisiable = false;
                  })
                } else {
                  this.$confirm('文件已存在，是否要覆盖该文件', '提示', () => {
                    type:'info'
                  }).then(() => {
                    param.append('fileId', response.content);
                    param.append('isValid', 'Y');
                    updateFileOperation(param).then(res => {
                      let {data} = res;
                      if (data.code === '10000') {
                        this.$message({
                          message: '文件上传成功',
                          type: 'success'
                        })
                        let fileData = res.data.content;
                        this.$refs['editForm'].resetFields();
                        this.$parent.$parent.getFileData(fileData);
                      } else {
                        this.$message({
                          message: data.msg,
                          type: 'error'
                        })
                      }
                      this.editLoading = false;
                      this.editVisiable = false;
                    })
                  }).catch(() => {
                    let queryParams = {
                      fileId: response.content,
                    }
                    getFileByFileId(queryParams).then(res => {
                      let {
                        data
                      } = res;
                      if (data.code === '10000') {
                        this.$message({
                          message: '取消文件上传',
                          type: 'success'
                        })
                        let fileData = res.data.content;
                        this.$refs['editForm'].resetFields();
                        this.$parent.$parent.getFileData(fileData);
                      } else {
                        this.$message({
                          message: data.msg,
                          type: 'error'
                        })
                      }
                    })
                  })
                }
              }
            })

          } else {
          }
        })

      },
      // 新增分组
      addData() {
        this.$refs['groupForm'].validate((valid) => {
          if (!valid) {
          } else {
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
              this.addLoading = false;
              this.groupLists.push({
                groupId: this.groupForm.groupId + "," + this.groupForm.groupName,
                groupName: this.groupForm.groupName,
              })
              this.editForm.groupId = this.groupForm.groupId + "," + this.groupForm.groupName;
              this.getGroups();
              this.$refs['groupForm'].resetFields();
              this.addGroupViaiable = false;
            })
          }
        });
      },
    }
  }
</script>

<style>
</style>
