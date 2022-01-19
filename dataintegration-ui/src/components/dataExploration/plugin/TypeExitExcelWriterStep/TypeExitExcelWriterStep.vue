<template>
  <div class="plugin_body">
    <!--    插件内容-->
    <div class="plugin_content">
      <div class="div_form">
        <el-form ref="step" :model="step" :rules="rules" size="mini" label-width="30%">
          <el-form-item label="步骤名称" prop="name">
            <el-input v-model="step.name" size="mini" style="width: 65%"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <el-tabs @tab-click="handleClick">
        <el-tab-pane label="文件&工作表">
          <div class="node_title_basic">
            <span>文件</span>
          </div>
          <el-card class="box-card">
            <el-form ref="stepFile" :model="step.file" :rules="rules" size="mini" label-width="30%">
              <el-form-item prop="folderName" label="文件夹" :rules="{
                required: true, message: '请选择文件夹', trigger: 'change,blur'}">
                <el-select v-model="step.folderName" size="mini" placeholder="请选择文件夹" style="width: 65%" @change="getFileList">
                  <el-option v-for="item in dirs" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
                </el-select>
                <el-button size="mini" type="primary" @click="changeFile">新建</el-button>
              </el-form-item>
              <el-form-item prop="showFileName" label="文件" :rules="{
                required: true, message: '请选择文件夹', trigger: 'change,blur'}">
                <el-select v-model="step.file.showFileName" size="mini" placeholder="请选择文件"  filterable allow-create style="width: 65%"
                           :disabled="step.folderName===''||step.folderName===undefined">
                  <el-option v-for="item in files" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="扩展名" prop="extension">
                <el-select v-model="step.file.extention" size="mini" style="width: 65%" clearable>
                  <el-option v-for="item in extention" :key="item.key" :label="item.label"
                             :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="流式XSLX数据">
                <el-checkbox size="mini" v-model="step.file.stream_data"
                             :disabled="step.file.extention === 'xls'"></el-checkbox>
              </el-form-item>
              <el-form-item label="分割...数据行" prop="splitevery">
                <el-input v-model="step.file.splitevery" size="mini" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="在文件名里包含步骤数目">
                <el-checkbox size="mini" v-model="step.file.split"></el-checkbox>
              </el-form-item>
              <el-form-item label="在文件名里包含日期">
                <el-checkbox size="mini" v-model="step.file.add_date"
                             :disabled="add_date"></el-checkbox>
              </el-form-item>
              <el-form-item label="在文件名里包含时间？">
                <el-checkbox size="mini" v-model="step.file.add_time"
                             :disabled="add_time"></el-checkbox>
              </el-form-item>
              <el-form-item label="指定日期时间格式">
                <el-checkbox size="mini" v-model="step.file.SpecifyFormat"></el-checkbox>
              </el-form-item>
              <el-form-item label="日期时间格式" prop="date_time_format">
                <el-select v-model="step.file.date_time_format" size="mini"
                           :disabled="step.file.SpecifyFormat === false" filterable clearable style="width: 65%">
                  <el-option v-for="item in dataformat" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="如果文件已存在" prop="if_file_exists">
                <el-select v-model="step.file.if_file_exists" size="mini" clearable style="width: 65%">
                  <el-option v-for="item in list" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-dialog title="输出文件" :visible.sync="lookfile" width="30%" :modal-append-to-body="false">
                <div>
                  <div>过滤：
                    <el-input style="width: 65%; margin-bottom: 10px;" size="mini"
                              v-model="step.file.filter"></el-input>
                  </div>
                  <div>输出文件：
                    <el-input style="width: 65%;" v-model="step.file.outputfile"></el-input>
                  </div>
                  <div slot="footer" class="dialog-footer" align="right">
                    <el-button @click="lookfile = false" size="mini">关 闭</el-button>
                  </div>
                </div>
              </el-dialog>
              <el-form-item label="在接收到数据前不创建文件">
                <el-checkbox size="mini"
                             v-model="step.file.do_not_open_newfile_init"></el-checkbox>
              </el-form-item>
              <el-form-item label="结果中添加文件名">
                <el-checkbox size="mini" v-model="step.file.add_to_result_filenames"></el-checkbox>
              </el-form-item>
            </el-form>
          </el-card>

          <div class="node_title_basic">
            <span>工作表</span>
          </div>
          <el-card class="box-card">
            <el-form :ref="step.file" :model="step.file" size="mini" label-width="30%">
              <el-form-item label="工作表名" prop="sheetname">
                <el-input v-model="step.file.sheetname" size="mini" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item>
                <el-checkbox label="设为活动工作表" size="mini" v-model="step.makeSheetActive"></el-checkbox>
              </el-form-item>
              <el-form-item label="如果输出文件中已存在工作表" prop="if_sheet_exists">
                <el-select v-model="step.file.if_sheet_exists" size="mini" style="width: 65%;">
                  <el-option v-for="item in exit" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-checkbox label="保护工作表?(仅限XLS格式)" size="mini" v-model="step.file.protect_sheet"
                             @change="changeprotect"></el-checkbox>
              </el-form-item>
              <el-form-item label="保护人">
                <el-input v-model="step.file.protected_by" size="mini" style="width: 65%;"
                          :disabled="people"></el-input>
              </el-form-item>
              <el-form-item label="密码">
                <el-input v-model="step.file.password" show-password size="mini" style="width: 65%;"
                          :disabled="pdw"></el-input>
              </el-form-item>
            </el-form>
          </el-card>

          <div class="node_title_basic">
            <span>模板</span>
          </div>
          <el-card class="box-card">
            <el-form :ref="step.template" :model="step.template" size="mini" label-width="30%">
              <el-form-item label="使用模板创建新文件">
                <el-checkbox size="mini" v-model="step.template.enabled"
                             @change="changenew"></el-checkbox>
              </el-form-item>
              <el-form-item label="模板文件">
                <el-input v-model="step.template.filename" size="mini" style="width: 65%" :readonly="true"
                          :disabled="dis"></el-input>
                <el-button size="mini" type="primary" @click="changeFile" :disabled="dis">选择</el-button>
              </el-form-item>
              <el-form-item label="使用模板创建新工作表">
                <el-checkbox size="mini" v-model="step.template.sheet_enabled"
                             @change="changenewtable"></el-checkbox>
              </el-form-item>
              <el-form-item label="模板工作表">
                <el-input v-model="step.template.sheetname" size="mini" style="width: 65%" :disabled="tdis"></el-input>
              </el-form-item>
              <el-form-item label="隐藏模板工作表">
                <el-checkbox size="mini" :disabled="hdis"
                             v-model="step.file.hidden"></el-checkbox>
              </el-form-item>
            </el-form>
          </el-card>
        </el-tab-pane>

        <el-tab-pane label="内容">
          <div class="node_title_basic">
            <span>内容选项</span>
          </div>
          <el-card class="box-card">
            <el-form :ref="step" :model="step" size="mini" label-width="30%">
              <el-form-item label="开始输出自单元格" prop="startingCell">
                <el-input v-model="step.startingCell" size="mini" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="当输出记录时" prop="record_output">
                <el-select v-model="step.record_output" size="mini" style="width: 65%;" clearable>
                  <el-option v-for="item in record_output" :key="item.key" :label="item.label"
                             :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="输出表头">
                <el-checkbox v-model="step.header" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="输出表尾">
                <el-checkbox v-model="step.footer" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="自动调整列大小">
                <el-checkbox v-model="step.autosizecolums" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="强制公式重新计算">
                <el-checkbox v-model="step.forceFormulaRecalculation" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="不改变现有单元格格式">
                <el-checkbox v-model="step.leaveExistingStylesUnchanged" size="mini"></el-checkbox>
              </el-form-item>
            </el-form>
          </el-card>

          <div class="node_title_basic">
            <span>写入已存在的工作表</span>
          </div>
          <el-card class="box-card">
            <el-form :ref="step" :model="step" size="mini" label-width="30%">
              <el-form-item label="在表末尾追加">
                <el-checkbox v-model="step.appendLines" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="删除表头">
                <el-checkbox v-model="step.appendOmitHeader" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="抵消行数" prop="appendOffset">
                <el-input v-model="step.appendOffset" size="mini" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="在写入文件前添加的空行数" prop="appendEmpty">
                <el-input v-model="step.appendEmpty" size="mini" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </el-card>

          <div class="node_title_basic">
            <span>字段设置</span>
            <span style="float: right">
               <el-button @click="getFieldInfo" type="primary" size="mini">获取字段</el-button>
            </span>
          </div>
          <el-table :data="showFields" border height="330" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column type="index" label="#" width="20"></el-table-column>
            <el-table-column prop="name" label="名称">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.name" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型">
              <template slot-scope="scope">
                <el-select v-model="scope.row.type" size="mini" style="width: 100%;"
                           @change="formatData(scope.row)" v-if="scope.row.edit === true">
                  <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.type}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="format" label="格式" width="80">
              <template slot-scope="scope">
                <el-select v-model="scope.row.format" size="mini" style="width: 100%;" clearable
                           filterable
                           allow-create
                           default-first-option v-if="scope.row.edit === true">
                  <el-option v-for="item in formatDate" :key="item" :label="item" :value="item"
                             v-if="scope.row.type ==='Date'||scope.row.type==='Timestamp'">
                  </el-option>
                  <el-option v-for="item in formatNumber" :key="item" :label="item" :value="item"
                             v-if="scope.row.type ==='Number'">
                  </el-option>
                  <el-option v-for="item in formats" :key="item" :label="item" :value="item"
                             v-if="scope.row.type !=='Date'&&scope.row.type!=='Timestamp'&& scope.row.type!=='Number'">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.format}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="styleCell" label="单元格样式" width="130">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.styleCell" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.styleCell}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="字段标题" width="100">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.title" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.title}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="titleStyleCell" label="单元格表头/表位格式" width="220">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.titleStyleCell"
                          v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.titleStyleCell}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="formula" label="包含公式的字段" width="220">
              <template slot-scope="scope">
                <el-select v-model="scope.row.formula" size="mini" style="width: 100%;" clearable
                           v-if="scope.row.edit === true">
                  <el-option v-for="item in formulas" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.formula}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="hyperlinkField" label="超链接">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.hyperlinkField"
                          v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.hyperlinkField}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="commentField" label="单元格注释(XLSX)" width="280">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.commentField"
                          v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.commentField}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="commentAuthorField" label="单元格注释作者(XLSX)" width="280">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.commentAuthorField"
                          v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.commentAuthorField}}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100px" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="editLine(scope.row)">编辑</el-button>
              </template>
            </el-table-column>

          </el-table>
          <el-pagination style="float: right" class="right" @current-change="selectedPage"
                         :current-page.sync="curPage"
                         @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 50, 150]"
                         layout="total, sizes, prev, pager, next"
                         :total="step.fields.field.length">
          </el-pagination>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div slot="footer" class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button type="primary" @click="submit" size="mini">确 定</el-button>
    </div>
    <el-dialog title="新增文件夹" :visible.sync="selectFileVisiable" v-if="selectFileVisiable"
               class="dialog_temp other_dialog"
               :modal-append-to-body='false'
               :append-to-body="true" width="50%" :before-close="closeAdd">
      <!-- 子组件 -->
      <fileData :params="queryParams" :selec-data="selectData" :fileType="fileType" createChannel="UPLOAD"
                fileFolder="1"
                ref="fileData"></fileData>
    </el-dialog>
  </div>
</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store.js";
  import fileData from '../../../common/UploadFile.vue';
  import {
    selectFileListByType,
  } from '../../../../api/api.js'
  import {compareFields} from "../../../../assets/common/tool";
  import {getAllFilFolderList, getFileByFileFolder, getFileByFileId} from "../../../../api/api";

  export default {
    props: {
      selectData: Object,
    },
    components: {
      fileData,
    },
    data() {
      return {
        dirs: [],
        files: [],
        showFields: [],
        curPage: 1,
        pageSize: 10,
        fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"],
        formatDate: [],
        formats: [],
        formatNumber: [],
        queryParams: {
          fileType: '',
        },
        laststep: [],
        lastStepField: [],
        fileType: 'xls,xlsx',
        selectFileVisiable: false,
        key: '', //插件对应的画布
        nodeData: '',
        fileList: [],
        dialogVisible: false,
        get: false,
        mapping: false,
        getremove: false,
        getchange: false,
        lookfile: false,
        getvalue: false,
        mixwidth: false,
        dformat: true,
        add_date: false,
        add_time: false,
        people: true,
        pdw: true,
        dis: true,
        tdis: true,
        hdis: true,
        step: {
          file_server_type: '',
          ftp_username: '',
          ftp_password: '',
          folderName: '',
          name: "Microsoft Excel 输出",
          type: "TypeExitExcelWriterStep2",
          distribute: "Y",
          copies: "1",
          partitioning: {"method": "none"},
          header: true,
          footer: false,
          makeSheetActive: true,
          rowWritingMethod: [
            "overwrite",
            "overwrite"
          ],
          startingCell: "A1",
          appendOmitHeader: false,
          appendOffset: "0",
          appendEmpty: "0",
          forceFormulaRecalculation: false,
          leaveExistingStylesUnchanged: false,
          appendLines: false,
          add_to_result_filenames: false,
          file: {
            showFileName: '',
            name: "",
            extention: "xlsx",
            do_not_open_newfile_init: false,
            split: false,
            add_date: false,
            add_time: false,
            SpecifyFormat: false,
            sheetname: "Sheet1",
            autosizecolums: false,
            stream_data: false,
            protect_sheet: false,
            password: "",
            splitevery: "0",
            if_file_exists: "new",
            if_sheet_exists: "new"
          },
          template: {
            enabled: false,
            sheet_enabled: false,
            filename: "template.xls",
            hidden: false
          },
          fields: {
            field: []
          },
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "496",
            yloc: "432",
            draw: "Y"
          }
        },
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          folderName: [{
            required: true,
            message: '请选择文件夹',
            trigger: 'change'
          }],
          file_name:[{
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }]
        },
        worktabledata: [],
        extention: [{
          key: 'xls',
          label: 'XLS [Excel 97 and above]',
        }, {
          key: 'xlsx',
          label: 'XLSX [Excel 2007 and above]'
        }],
        dataformat: ["m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss",
          "m/d/yy h:mm", "mm:ss", "[h]:mm:ss", "mm:ss.0", "yyyy-mm-dd hh:mm:ss", "yyyy/mm/dd", "yyyy-MM-dd",
          "yyyy/mm/dd hh/mm/ss"
        ],
        formulas: ["是", "否"],
        list: [{key: 'new', label: "覆盖原文件"}, {key: 'reuse', label: "使用现有文件输出"}],
        exit: [{key: 'new', label: "覆盖原工作表"}, {key: 'reuse', label: "输出至已存在的工作表中"}],
        record_output: [{key: 'new', label: "覆盖已存在的单元格"}, {key: 'reuse', label: "下移已有单元格"}],
      }
    },
    mounted() {
      this.init();
      // this.getFileByFileType();
    },
    methods: {

      editLine(row) {
        this.step.fields.field.forEach((item, index) => {
          if (item.name === row.name) {
            // item.decimal='.'
            this.$set(this.step.fields.field[index], 'edit', true);
            this.$forceUpdate();
          }
        })
      },

      /**
       * 获取上传的文件信息
       */
      getFileData(fileData) {
        this.step.folderName = fileData.fileId+','+fileData.fileName;
        this.selectFileVisiable = false;
        this.getAllFolder();
        this.getFileList();
      },
      blurInput() {
        let flag = true;
        this.restaurants.forEach((item, index) => {
          if (item.fileName === this.step.folderName) {
            flag = false;
          }
        })
        if (flag) {
          this.step.folderName = '';
        }
      },
      formatData(row) {
        if (row.type === 'Number') {
          this.formatterDatas = this.formatNumber;
          row.length = 38;
          row.precision = 0;
        } else if (row.type === 'String') {
          this.formatterDatas = this.formats;
          row.length = 255;
          row.precision = -1;
        } else {
          if (row.type === 'Date' || row.type === 'Timestamp') {
            this.formatterDatas = this.formatDate;
          } else {
            this.formatterDatas = this.formats;
          }
          row.length = -1;
          row.precision = -1;
        }
      },

      querySearch(queryString, cb) {
        var restaurants = this.restaurants;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },

      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },

      selectedPage() {
        this.showFields = [];
        let fields = [];
        fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          this.showFields.push(item);
        });

      },
      //获取字段
      getFieldInfo() {
        this.step.fields.field = [];
        this.lastStepField.forEach(item => {
          this.step.fields.field.push({
            edit: false,
            name: item.name,
            type: item.type,
            format: item.format,
            title: '',
            titleStyleCell: '',
            styleCell: '',
            commentField: '',
            commentAuthorField: '',
            formula: '',
            hyperlinkField: ""
          })
        })
        this.selectedPage();
      },

      changeSelect() {
        this.step.sheets.sheet.name = '';
        this.sheets = [];
        this.showFields = [];
      },
      /**
       * 获取文件列表
       */
      getFileByFileType(fileId) {
        let queryParams = {
          fileType: '',
          isFolder: '1',
        };
        let fileList = [];
        selectFileListByType(queryParams).then(res => {

          if (res.data.code === '10000') {
            if (fileId !== undefined) {
              res.data.content.forEach((item, index) => {
                fileList.push({
                  value: item.fileName,
                  fileName: item.fileName,
                  fileId: item.fileId,
                  fileRelativPath: item.fileRelativPath,
                  fileServerType: item.fileServerType,
                  userName: item.userName,
                  password: item.password,
                });
              })
              res.data.content.forEach((item, index) => {
                if (item.fileId === fileId) {
                  this.handleSelect(item);
                }
              })
            } else {
              res.data.content.forEach((item, index) => {
                fileList.push({
                  value: item.fileName,
                  fileName: item.fileName,
                  fileId: item.fileId,
                  fileRelativPath: item.fileRelativPath,
                  fileServerType: item.fileServerType,
                  userName: item.userName,
                  password: item.password,
                });
              })

            }
            this.restaurants = fileList;
          } else {
            this.$message({
              message: '获取文件列表失败',
              type: 'error',
            });
          }
        })
      },

      handleSelect(item) {
        this.showFields = [];
        this.fileId = item.fileId;
        this.step.file.name = item.fileRelativPath + '/' + this.step.file.showFileName;
        this.step.filePath = item.fileRelativPath;
        this.step.file_server_type = item.fileServerType;
        this.step.ftp_username = item.userName;
        this.step.ftp_password = item.password;
        // this.selectFile();
      },

      changeFile() {
        this.queryParams = {
          fileType: 'xls,xlsx',
          isFolder: '0',
        }
        this.selectFileVisiable = true;
      },

      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          this.step.fileId = this.$refs.fileData.selecData.fileId;
          this.step.filename = this.$refs.fileData.selecData.fileName;
          this.step.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.file_server_type = this.$refs.fileData.selecData.fileServerType;
          this.step.ftp_username = this.$refs.fileData.selecData.userName;
          this.step.ftp_password = this.$refs.fileData.selecData.password;
        }
        // this.selectFile();
        this.selectFileVisiable = false;
      },

      closeDialog() {
        util.$emit('closeDialog', 'close');
        // 初始化画3布数据
      },
      handleClick(tab, event) {
        console.log(tab, event);
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(
          `当前限制选择1个文件，本次选择了 ${
            files.length
          } 个文件，共选择了 ${files.length + fileList.length} 个文件`
        );
      },
      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`);
      },


      getConstant() {
        this.$http.get('static/data.json').then(res => {
          this.formats = res.body.formatters.formats;
          this.dataFormatters = res.body.formatters.localDateFormats;
          this.encodings = res.body.formatters.encodings;
          this.formatDate = res.body.formatters.formatDate;
          this.formatNumber = res.body.formatters.formatNumber;
        })
      },

      /**
       * 获取所有文件夹
       * */
      getAllFolder(){
        this.dirs=[];
        getAllFilFolderList().then(res=>{
          // this.pids = res.data.content;
          let tmpData =[];
          res.data.content.forEach(item=>{
            tmpData.push({
              fileId:item.fileId+','+item.fileName,
              fileName:item.fileName
            })
            this.dirs = tmpData;
          })
        })
      },
      getFileList(){
        let params ={
          fileId:this.step.folderName.split(',')[0],
          fileType:'xls,xlsx'
        }
        getFileByFileFolder(params).then(res=>{
          this.files = res.data.content;
        })
        this.selectFile();
      },
      selectFile(){
        let params={
          fileId:this.step.folderName.split(",")[0]
        }
        getFileByFileId(params).then(res=>{
          this.handleSelect(res.data.content);
        })
      },

      //初始化
      init() {
        this.getConstant();
        this.getAllFolder();
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        // 获取当前步骤信息参数
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        }
        this.step.fields.field = [];
        //this.csvinputData = [];
        let curStep = store.getters.getCurrentStep(param);
        let laststeps = store.getters.getLastStep(param);
        let outFields = store.getters.generateOutputFields(param);
        this.step['outFields'] = outFields;
        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
          this.lastStepField = outFields;
        }
        // if (laststeps !== undefined && laststeps.length > 0) {
        //   this.laststep = laststeps[0];
        //   this.lastStepField = this.laststep.fields.field;
        // }
        if (curStep !== undefined && curStep.fields.field !== undefined) {
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            } else if (curStep[key] === 'N' && key !== 'type') {
              this.step[key] = false;
            } else {

              if (key === 'fields') {
              } else {
                this.step[key] = curStep[key];
              }
            }
          }
          if(this.step.file_name !==''&& this.step.file_name !==undefined){
            this.getFileList();
          }
          this.step.type = 'TypeExitExcelWriterStep2';
          this.step.fields.field = [];
          let fields = JSON.parse(JSON.stringify(curStep.fields.field));
          fields.forEach(item => {
            item.edit = false;
            this.step.fields.field.push(item);
          })
          this.selectedPage();
        }
      },

      submit() {
        this.step.file.name = this.step.filePath+'/'+this.step.file.showFileName;
        this.step.initFlag = true;
        let step = Object.assign({}, this.step);
        for (let key in step) {
          if (typeof step[key] === 'boolean') {
            if (step[key] === true) {
              step[key] = 'Y'
            } else {
              step[key] = 'N'
            }
          }
        }
        step['oldStepName'] = this.oldStepName;
        // 新增字段
        step.fields.field.forEach(item => {
          step.outFields.push({
            name: item.name,
            type: item.type,
            length: -1,
            precision: -1,
            trim_type: "none",
            repeat: "N",
            format: "",
            currency: item.currency === '' ? '¥' : item.currency,
            decimal: item.decimal === '' ? "." : item.decimal,
            group: item.group === '' ? ',' : item.group
          })
          compareFields(step.outFields);
        });
        console.info("outFiels", step.outFields)
        // 控件重命名
        let paramName = step.name;
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
        let flag = false;
        if (step.name !== newName) {
          flag = true;
          step.name = newName;
        }
        // 修改步骤xinxi
        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: step
          }
        }
        store.dispatch('updateStepInfo', param);
        let params = {
          id: this.nodeData.id, //插件id
          label: step.name,
          oldName: this.oldStepName,
        }
        util.$emit('updateNode', params);
        util.$emit('closeDialog', 'close');
        if (flag) {
          this.$alert(paramName + '名称已存在,重命名为' + newName + "!", '注释', {
            confirmButtonText: '确定',
          });
        } else {
          this.$message({
            message: '步骤信息修改成功',
            type: 'success'
          })
        }
      },
      changedataformat() {
        if (this.step.file.SpecifyFormat === true) {
          this.dformat = false;
          this.add_date = true;
          this.add_time = true;
        } else if (this.step.file.SpecifyFormat === false) {
          this.dformat = true;
          this.add_date = false;
          this.add_time = false;
        }
      }, //日期时间格式
      changeprotect() {
        if (this.step.file.protect_sheet === true) {
          this.people = false;
          this.pdw = false;
          this.step.file.protect_sheet = true;
        } else if (this.step.file.protect_sheet === false) {
          this.people = true;
          this.pdw = true;
        }
      }, //保护工作表
      changenew() {
        if (this.step.template.enabled === true) {
          this.dis = false;
          this.step.template.enabled = true;
        } else if (this.step.template.enabled === false) {
          this.dis = true;
        }
      }, //模板文件
      changenewtable() {
        if (this.step.template.sheet_enabled === true) {
          this.tdis = false;
          this.hdis = false;
          this.step.template.sheet_enabled = true;
        } else if (this.step.template.sheet_enabled === false) {
          this.tdis = true;
          this.hdis = true;
        }
      }, //模板工作表
    },
  }
</script>

<style>
  .el-card.is-always-shadow {
    webkit-box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
    box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
  }

  .bu {
    margin-top: 5px;
  }

  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }

  input[type="file"] {
    display: none;
  }
</style>
