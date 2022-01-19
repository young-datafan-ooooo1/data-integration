<template>
  <div class="plugin_body">
    <div class="div_form plugin_content">
      <el-form ref="step" :model="step" size="mini" :rules="rules" label-width="30%">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%;" size="mini"></el-input>
        </el-form-item>
        <el-form-item prop="showFileName" label="文件夹">
          <el-select v-model="step.showFileName" size="mini" style="width: 65%" @change="getFileList">
            <el-option v-for="item in dirs" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
          </el-select>
          <el-button size="mini" type="primary" @click="changeFile1">新建</el-button>
        </el-form-item>
        <el-form-item prop="file_name" label="文件">
          <el-select v-model="step.file_name" size="mini" placeholder="请选择文件" @change="selectFile(step.file_name)"  style="width: 65%" :disabled="step.showFileName===''||step.showFileName===undefined">
            <el-option v-for="item in files" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
          </el-select>
          <!--            -->
          <el-button size="mini" type="primary" :disabled="step.showFileName===''||step.showFileName===undefined"  @click="changeFile">上传</el-button>
        </el-form-item>
<!--        <el-form-item label="文件名" prop="showFileName">-->
<!--          <el-autocomplete style="width: 65%;" class="inline-input" v-model="step.showFileName"-->
<!--                           :fetch-suggestions="querySearch"-->
<!--                           placeholder="请输入文件名称搜索文件" :trigger-on-focus="false" @select="handleSelect" @blur="blurInput">-->
<!--            <el-button slot="append" size='mini' type="primary" @click="changeFile" id="csvfile">本地文件-->
<!--            </el-button>-->
<!--          </el-autocomplete>-->
<!--        </el-form-item>-->
        <el-form-item label="列分隔符" prop="delimiter">
          <el-input v-model="step.delimiter" style="width: 65%;" size="mini"></el-input>
<!--          <el-button type="info" size="mini">插入制表符</el-button>-->
        </el-form-item>
        <el-form-item label="封闭符" prop="enclosure">
          <el-input v-model="step.enclosure" style="width: 65%;" size="mini"></el-input>
        </el-form-item>
        <el-form-item label="NIO缓存大小" prop="size">
          <el-input v-model="step.buffer_size" style="width: 65%;" size="mini"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox label="简易转换?" v-model="step.lazy_conversion"></el-checkbox>
          <el-checkbox label="包含列头行" v-model="step.header"></el-checkbox>
          <el-checkbox label="将文件添加到结果文件中" v-model="step.add_filename_result"></el-checkbox>
        </el-form-item>
        <el-form-item label="行号字段(可选)" prop="rownum_field">
          <el-input v-model="step.rownum_field" style="width: 65%;" size="mini"></el-input>
        </el-form-item>
        <el-form-item>
          <el-checkbox label="并发运行?" v-model="step.parallel" :disabled="step.newline_possible == true"></el-checkbox>
          <el-checkbox label="字段中有回车换行?" v-model="step.newline_possible"></el-checkbox>
        </el-form-item>
        <el-form-item label="格式" prop="format">
          <el-select v-model="step.format" style="width:65%;" size="mini" filterable>
            <el-option v-for="item in stepformat" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文件编码" prop="code">
          <el-select v-model="step.encoding" style="width:65%;" size="mini" filterable>
            <el-option v-for="item in encodings" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <el-table :data="csvinputData" border height="330" :header-cell-style="{background:'#eef1f6'}">
        <el-table-column type="index" label="#" width="50"></el-table-column>
        <el-table-column prop="name" label="名称" width="180px">
          <template slot-scope="scope">
            <el-input v-model="scope.row.name" size="mini" v-if="scope.row.edit===true"></el-input>
            <span v-if="scope.row.edit === false">{{scope.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="180px">
          <template slot-scope="scope">
            <el-select v-model="scope.row.type" size="mini" style="width: 100%;" v-if="scope.row.edit === true">
              <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>
            <span v-else>{{scope.row.type}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="format" label="格式" width="180px">
          <template slot-scope="scope">
            <el-select v-model="scope.row.format" size="mini" style="width: 100%;" clearable filterable allow-create
                       default-first-option v-if="scope.row.edit===true">
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
            <span v-else> {{scope.row.format}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="length" label="长度">
          <template slot-scope="scope">
            <el-input v-model="scope.row.length" size="mini" v-if="scope.row.edit===true"></el-input>
            <span v-else>{{scope.row.length}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="precision" label="精度">
          <template slot-scope="scope">
            <el-input v-model="scope.row.precision" size="mini" v-if="scope.row.edit===true"></el-input>
            <span v-else>{{scope.row.precision}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="currency" label="货币符号" width="100">
          <template slot-scope="scope">
            <el-input v-model="scope.row.currency" size="mini" v-if="scope.row.edit===true"></el-input>
            <span v-else>{{scope.row.currency}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="decimal" label="小数点符号" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.decimal" size="mini" v-if="scope.row.edit === true"></el-input>
            <span v-else>{{scope.row.decimal}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="group" label="分组符号" width="100">
          <template slot-scope="scope">
            <el-input v-model="scope.row.group" size="mini" v-if="scope.row.edit===true"></el-input>
            <span v-else>{{scope.row.group}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="repeat" label="去除空格类型" width="140">
          <template slot-scope="scope">
            <el-select v-model="scope.row.repeat" clearable placeholder="去空格类型" size="mini" style="width: 100%"
                       v-if="scope.row.edit===true">
              <el-option v-for="item in trimType" :key="item" :label="item" :value="item">
              </el-option>
            </el-select>
            <span v-else>{{scope.row.repeat}}</span>
            <!-- <el-select v-model="scope.row.spaceTypeRemoved" clearable placeholder="去空格类型" size="mini" style="width: 100%">
              <el-option v-for="item in trimType" :key="item" :label="item" :value="item">
              </el-option>
            </el-select> -->
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="editLine(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="float: right" class="right" @current-change="selectedPage" :current-page.sync="curPage"
                     @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 50, 150]"
                     layout="total, sizes, prev, pager, next"
                     :total="total">
      </el-pagination>
    </div>
    <div slot="footer" class="dialog-footer plugin_footer">
      <el-button @click="closeDialog" size="mini">取 消</el-button>
      <el-button @click="getDialog" size="mini" type="primary">获取字段</el-button>
      <!--<el-dialog title="获取字段" :visible.sync="getDialog" :modal-append-to-body="false">
        <div>
          <div>We were unable to find any new incoming filds</div>
          <div slot="footer" class="dialog-footer" align="right">
            <el-button @click="getDialog = false" size="mini">确 定</el-button>
          </div>
        </div>
      </el-dialog>-->
      <el-button @click="line = true" size="mini" :disabled="csvinputData.length === 0" type="primary">预览记录</el-button>
      <el-button type="primary" @click="submit" size="mini">确 定</el-button>
    </div>

    <el-dialog class="dialog_temp other_dialog" :visible.sync="line" title="选择显示行数" width="30%"
               :append-to-body="true">
      <div style="width: 100%;padding: 10px">
        <span>显示行数</span>
        <span>
          <el-input v-model="limit" style="width: 80%"></el-input>
        </span>

      </div>
      <span slot="footer" class="dialog-footer">
         <el-button @click="line = false" size="mini">取 消</el-button>
         <el-button type="primary" @click="previewDatas" size="mini" :loading="previewLoading">确 定</el-button>
       </span>
    </el-dialog>

    <!--  数据预览展示窗口 -->
    <el-dialog :visible.sync="preDataVisiable" :append-to-body="true" v-alterELDialogMarginTop="{marginTop:'2vh'}"
               :modal-append-to-body="true" class="abow_in_dialog" width="70%" title="数据预览">

      <pre-view-data :data-pre="dataPre" :data-column="dataColumn" :key="previewKey"></pre-view-data>
      <span slot="footer" class="dialog-footer">
         <el-button @click="preDataVisiable=false" size="mini">关闭</el-button>
         <el-button type="primary" @click="showLog" size="mini">查看日志</el-button>
       </span>
    </el-dialog>
    <el-dialog :visible.sync="logVisiable" title="日志" :append-to-body="true" width="70%"
               v-alterELDialogMarginTop="{marginTop:'2vh'}" :modal-append-to-body="true"
               class="abow_in_dialog">
      <Log :key="logKey" :logs="log"></Log>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="selectFileVisiable" v-if="selectFileVisiable" class="dialog_temp other_dialog"
               :modal-append-to-body='false'
               :append-to-body="true" width="50%" :before-close="closeAdd">
      <!-- 子组件 -->
      <fileData :params="queryParams" :selec-data="selectData" :fileType="fileType" createChannel="UPLOAD" :pid="pid" :folder-name="folderName"
                :fileFolder="fileFolder"
                ref="fileData"></fileData>
    </el-dialog>
  </div>
</template>

<script>
  import util from "../../../../common/utils"
  import store from '../../../../vuex/store.js'
  import fileData from '../../../common/UploadFile.vue'
  import {
    selectFileListByType,
    getCsvHeaderColumn,
    getFileByFileId,
    getFileByFileFolder,
    getAllFilFolderList
  } from '../../../../api/api.js'
  import PreViewData from "../../../common/PreViewData";
  import Log from "../../../flow/Log";
  import {spliceDataJson} from "../../../../common/common";
  import {executePreviewByFile} from "../../../../api/api";

  export default {
    components: {
      fileData,
      PreViewData,
      Log
    },
    computed: {},
    data() {

      return {
        dirs:[],
        files:[],
        title:'新建文件夹',
        fileFolder:'1',
        pid:'',
        folderName:'',
        edit: false,
        logVisiable: false,
        logKey: null,
        log: '',
        preDataVisiable: false,
        previewLoading: false,
        previewKey: null,
        dataPre: [],
        dataColumn: [],
        limit: 50,
        line: false,
        pageSize: 10,
        curPage: 1,
        total: 0,
        fileName: '',
        tableLoading: false,
        restaurants: [],
        csvinputData: [],
        fileId: '', //文件编号
        queryParams: {
          fileType: '',
        },
        fileType: 'csv',
        selectFileVisiable: false,
        key: '', //插件对应的画布
        nodeData: {},
        fileList: [],
        //getDialog: false,
        look: false,
        selectData: {},
        step: {
          dirName:'',
          file_name:'',
          showFileName: '',
          name: "CSV文件输入",
          type: "CsvInput2",
          delimiter: ',',
          filename: "",
          file_server_type: '',
          ftp_username: "",
          ftp_password: "",
          description: "",
          distribute: "Y",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          include_filename: "N",
          separator: ",",
          enclosure: "\"",
          header: true,
          buffer_size: "50000",
          lazy_conversion: true,
          add_filename_result: false,
          parallel: false,
          newline_possible: false,
          encoding: "UTF-8",
          format: "mixed",
          fields: {
            field: []
          },
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 176,
            yloc: 160,
            draw: "Y"
          }
        },
        stepformat: ["mixed", "DOS", "Unix"],
        fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"], //字段类型
        formats: ["General", "0", "0.00", "#,##0", "#,##0.00", "\"$\"#,##0_);(\"$\"#,##0)",
          "\"$\"#,##0_);[Red](\"$\"#,##0)", "\"$\"#,##0.00_);(\"$\"#,##0.00)", "\"$\"#,##0.00_);[Red](\"$\"#,##0.00)",
          "0%", "0.00%", "0.00E+00", "# ?/?", "# ??/??", "m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM",
          "h:mm:ss AM/PM", "h:mm", "h:mm:ss", "m/d/yy h:mm", "reserved-0x17", "reserved-0x18", "reserved-0x19",
          "reserved-0x1A", "reserved-0x1B", "reserved-0x1C", "reserved-0x1D", "reserved-0x1E", "reserved-0x1F",
          "reserved-0x20", "reserved-0x21", "reserved-0x22", "reserved-0x23", "reserved-0x24", "#,##0_);(#,##0)",
          "#,##0_);[Red](#,##0)", "#,##0.00_);(#,##0.00)", "#,##0.00_);[Red](#,##0.00)",
          "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)", "_(\"$\"* #,##0_);_(\"$\"* (#,##0);_(\"$\"* \"-\"_);_(@_)",
          "_(* #,##0.00_);_(* (#,##0.00);_(* \"-\"??_);_(@_)",
          "_(\"$\"* #,##0.00_);_(\"$\"* (#,##0.00);_(\"$\"* \"-\"??_);_(@_)", "mm:ss", "[h]:mm:ss", "mm:ss.0",
          "##0.0E+0", "@"
        ], //字段格式
        formatNumber: ["0", "0.00", "#,##0", "#,##0.00", "\"$\"#,##0_);(\"$\"#,##0)",
          "\"$\"#,##0_);[Red](\"$\"#,##0)", "\"$\"#,##0.00_);(\"$\"#,##0.00)", "\"$\"#,##0.00_);[Red](\"$\"#,##0.00)",
          "0%", "0.00%", "0.00E+00", "# ?/?", "# ??/??"
        ],
        formatDate: ["yyyy/MM/dd", "yyyy/mm/dd HH/mm/ss", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss.S",
          "yyyy/MM/dd HH:mm:ss AM/PM", "yyyy-MM-dd", "yyyy-mm-dd HH/mm/ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S",
          "yyyy-MM-dd HH:mm:ss AM/PM"
        ],
        encodings: ["Big5", "Big5-HKSCS", "CESU-8", "EUC-JP", "EUC-KR", "GB18030", "GB2312", "GBK", "IBM-Thai",
          "IBM00858", "IBM01140", "IBM01141", "IBM01142", "IBM01143", "IBM01144", "IBM01145", "IBM01146", "IBM01147",
          "IBM01148", "IBM01149", "IBM037", "IBM1026", "IBM1047", "IBM273", "IBM277", "IBM278", "IBM280", "IBM284",
          "IBM285", "IBM290", "IBM297", "IBM420", "IBM424", "IBM437", "IBM500", "IBM775", "IBM850", "IBM852",
          "IBM855", "IBM857", "IBM860", "IBM861", "IBM862", "IBM863", "IBM864", "IBM865", "IBM866", "IBM868",
          "IBM869", "IBM870", "IBM871", "IBM918", "ISO-2022-CN", "ISO-2022-JP", "ISO-2022-JP-2", "ISO-2022-KR",
          "ISO-8859-1", "ISO-8859-13", "ISO-8859-15", "ISO-8859-2", "ISO-8859-3", "ISO-8859-4", "ISO-8859-5",
          "ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9", "JIS_X0201", "JIS_X0212-1990", "KOI8-R", "KOI8-U",
          "Shift_JIS", "TIS-620", "US-ASCII", "UTF-16", "UTF-16BE", "UTF-16LE", "UTF-32", "UTF-32BE", "UTF-32LE",
          "UTF-8", "windows-1250", "windows-1251", "windows-1252", "windows-1253", "windows-1254", "windows-1255",
          "windows-1256", "windows-1257", "windows-1258", "windows-31j", "x-Big5-HKSCS-2001", "x-Big5-Solaris",
          "x-COMPOUND_TEXT", "x-euc-jp-linux", "x-EUC-TW", "x-eucJP-Open", "x-IBM1006", "x-IBM1025", "x-IBM1046",
          "x-IBM1097", "x-IBM1098", "x-IBM1112", "x-IBM1122", "x-IBM1123", "x-IBM1124", "x-IBM1166", "x-IBM1364",
          "x-IBM1381", "x-IBM1383", "x-IBM300", "x-IBM33722", "x-IBM737", "x-IBM833", "x-IBM834", "x-IBM856",
          "x-IBM874", "x-IBM875", "x-IBM921", "x-IBM922", "x-IBM930", "x-IBM933", "x-IBM935", "x-IBM937", "x-IBM939",
          "x-IBM942", "x-IBM942C", "x-IBM943", "x-IBM943C", "x-IBM948", "x-IBM949", "x-IBM949C", "x-IBM950",
          "x-IBM964", "x-IBM970", "x-ISCII91", "x-ISO-2022-CN-CNS", "x-ISO-2022-CN-GB", "x-iso-8859-11", "x-JIS0208",
          "x-JISAutoDetect", "x-Johab", "x-MacArabic", "x-MacCentralEurope", "x-MacCroatian", "x-MacCyrillic",
          "x-MacDingbat", "x-MacGreek", "x-MacHebrew", "x-MacIceland", "x-MacRoman", "x-MacRomania", "x-MacSymbol",
          "x-MacThai", "x-MacTurkish", "x-MacUkraine", "x-MS932_0213", "x-MS950-HKSCS", "x-MS950-HKSCS-XP",
          "x-mswin-936", "x-PCK", "x-SJIS_0213", "x-UTF-16LE-BOM", "X-UTF-32BE-BOM", "X-UTF-32LE-BOM",
          "x-windows-50220", "x-windows-50221", "x-windows-874", "x-windows-949", "x-windows-950",
          "x-windows-iso2022jp"
        ],
        trimType: ["none", "left", "right", "both"],
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          dirName:[{
            required: true,
            message: '请选择文件夹',
            trigger: 'change'
          }],
          showFileName:[{
            required: true,
            message: '请选择文件夹',
            trigger: 'change'
          }],
          file_name:[{
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }],
          buffer_size: [{
            required: true,
            message: '不能为空',
            trigger: 'blur'
          }]
        },
        fileData: null,
        modelName: '',
        data: '',
        oldStepName: '',
        modelMetaData: []
      }
    },

    mounted() {
      this.init();
      util.$on("close_dialog", () => {
        this.closeAdd();
      });

      // this.getFileByFileType();
    },


    methods: {

      /**
       * 编辑行
       */
      editLine(row) {
        this.csvinputData.forEach((item, index) => {
          if (item.name === row.name) {
            // item.decimal='.'
            this.$set(this.csvinputData[index], 'edit', true);
            this.$forceUpdate();
          }
        })
        // console.info(row);
      },
      /**
       * 显示日志
       */
      showLog() {
        this.logKey = new Date().getTime();
        this.logVisiable = true;
      },

      /**
       * 数据预览
       */
      previewDatas() {
        this.changeStep();
        let dataJson = spliceDataJson(this.key, this.step);
        let params = {
          previewSize: this.limit,
          previewStepName: this.step.name,
          projectFile: JSON.stringify(dataJson),
          projectId: 'sss',
          projectName: 'sds'
        };
        this.previewLoading = true;
        executePreviewByFile(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            if (data.content.errors === 0) {
              this.dataPre = data.content.previewRows;
              this.dataColumn = data.content.previewFieldNames;
              this.previewKey = new Date().getTime();
              this.log = data.content.log;
              this.preDataVisiable = true;
            } else {
              this.log = data.content.log;
              this.showLog();
            }
          }
          this.previewLoading = false;
          this.reverStep();
        })
      },
      blurInput() {
        let flag = true;
        this.restaurants.forEach((item, index) => {
          if (item.fileName === this.step.fileName) {
            flag = false;
          }
        });
        if (flag) {
          this.step.showFileName = '';
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


      changeStep() {
        for (let key in this.step) {
          if (typeof this.step[key] === 'boolean') {
            this.step[key] = this.step[key] === true ? 'Y' : "N"
          }
        }
      },
      reverStep() {
        for (let key in this.step) {
          if (this.step[key] === 'Y') {
            this.step[key] = true;
          } else if (this.step[key] === 'N') {
            this.step[key] = false;
          }
        }
      },

      /**
       * 获取表字段信息
       */
      getCsvHeaderColumn() {
        this.changeStep();
        let params = {
          step: this.step,
        };
        getCsvHeaderColumn(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.total = data.content.length;
            this.step.fields.field = [];
            // this.csvinputData = data.content;
            for (let i = 0; i < data.content.length; i++) {
              let fileLength = -1;
              let precision = -1;
              if (data.content[i].type.toUpperCase() === ('number').toUpperCase()) {
                fileLength = 38;
                precision = 0;
              } else if (data.content[i].type.toUpperCase() === ('string').toUpperCase()) {
                fileLength = 255;
                precision = -1;
              } else {
                fileLength = -1;
                precision = -1;
              }
              this.step.fields.field.push({
                name: data.content[i].name,
                edit:false,
                //nameCn: data.content[i].name,
                type: data.content[i].type,
                length: data.content[i].len,
                precision: data.content[i].precision,
                trim_type: "none",
                repeat: "N",
                format: data.content[i].format,
                currency: data.content[i].dollarSign,//货币符号
                decimal: data.content[i].pointSymbol,//小数点符号
                group: data.content[i].classSymbol,//分组

                /* currency: "",
                decimal: "",
                group: "",
                dollarSign: data.content[i].dollarSign,
                pointSymbol: data.content[i].pointSymbol,
                classSymbol: data.content[i].classSymbol,
                spaceTypeRemoved: data.content[i].spaceTypeRemoved, */
              })
            }
            this.tableLoading = false;
            this.selectedPage();
            this.reverStep();
          } else {
            this.$message({
              message: '获取头部信息失败',
              type: 'error'
            });
            this.tableLoading = false;
          }
        })
      },

      /**
       * 获取上传的文件信息
       */
      getFileData(fileData,type){
        if(type==='dir'){
          this.step.showFileName = fileData.fileId+','+fileData.fileName;
          this.getAllFolder();
          this.getFileList();
        }else{
          this.step.file_name = fileData.fileId
          this.getFileList();
          this.selectFile(fileData.fileId)
        }
        this.selectFileVisiable = false;
      },

      chageFormat(row, column) {
        if (row.type === 'Number') {
          this.formats = this.formatNumber;
        } else if (row.type === 'Date' || row.type === 'Timestamp') {
          this.formats = this.formatDate;
        }
        return row.nameCn;
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
      /* changeColumn(row) {
        row.nameCn = vPinyin.chineseToPinYin(row.nameCn);
      }, */


      /**
       * 获取文件列表
       */
      getFileByFileType(fileId) {
        let queryParams = {
          fileType: 'csv',
          isFolder: '0',
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
              });
              res.data.content.forEach((item, index) => {
                if (item.fileId === fileId) {
                  this.handleSelect(item);
                }
              })
            } else {
              res.data.content.forEach((item, index) => {
                console.info("item-=-=", item);
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
        console.info("itrmd", item);
        //this.csvinputData = [];
        this.fileId = item.fileId;
        this.step.fileId = item.fileId;
        // this.step.showFileName = item.fileName;
        this.step.filename = item.fileRelativPath;
        this.step.file_server_type = item.fileServerType;
        this.step.ftp_username = item.userName;
        this.step.ftp_password = item.password;
        this.getDialog();
      },

      changeFile() {
        this.queryParams = {
          fileType: 'csv',
          isFolder: '0',
        };
        this.fileFolder='0';
        this.title='上传文件'
        this.pid = this.step.showFileName.split(",")[0];
        this.folderName = this.step.showFileName.split(",")[1];
        this.selectFileVisiable = true;
      },
      changeFile1() {
        this.fileFolder='1';
        this.title="新增文件夹"
        this.selectFileVisiable = true;
      },

      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          this.step.file.name = this.$refs.fileData.selecData.fileName;
          this.step.fileId = this.$refs.fileData.selecData.fileId;
          this.step.showFileName = this.$refs.fileData.selecData.fileName;
          this.step.filename = this.$refs.fileData.selecData.fileRelativPath;
          this.step.file_server_type = this.$refs.fileData.selecData.fileServerType;
          this.step.ftp_username = this.$refs.fileData.selecData.userName;
          this.step.ftp_password = this.$refs.fileData.selecData.password;
        }
        this.selectFileVisiable = false;
      },
      // 获取头部信息
      getDialog() {
        this.csvinputData = [];
        if (this.step.fileId === '') {
          this.$message({
            message: "请先选择文件",
            type: 'error'
          });
        } else {
          let params = {
            fileId: this.step.fileId,
            sheetName: '',
            split: this.step.delimiter,
          };
          this.tableLoading = true;
          this.getCsvHeaderColumn();
          // getCsvHeader(params).then(res => {
          //   let {
          //     data
          //   } = res;
          //   if (data.code === '10000') {
          //     this.total = data.content.length;
          //     this.step.fields.field = [];
          //     // this.csvinputData = data.content;
          //
          //     this.step.fields.field = [];
          //     for (let i = 0; i < data.content.length; i++) {
          //       let fileLength = -1;
          //       let precision = -1;
          //       if (data.content[i].fileType.toUpperCase() === ('number').toUpperCase()) {
          //         fileLength = 38;
          //         precision = 0;
          //       } else if (data.content[i].fileType.toUpperCase() === ('string').toUpperCase()) {
          //         fileLength = 255;
          //         precision = -1;
          //       } else {
          //         fileLength = -1;
          //         precision = -1;
          //       }
          //       this.step.fields.field.push({
          //         name: data.content[i].fieldName,
          //         nameCn: data.content[i].fieldName,
          //         type: data.content[i].fileType,
          //         length: fileLength,
          //         precision: precision,
          //         trim_type: "none",
          //         repeat: "N",
          //         format: "",
          //         currency: "",
          //         decimal: "",
          //         group: "",
          //       })
          //     }
          //     this.tableLoading = false;
          //     this.selectedPage();
          //   } else {
          //     this.$message({
          //       message: '获取头部信息失败',
          //       type: 'error'
          //     })
          //     this.tableLoading = false;
          //   }
          //
          // })
        }
      },

      selectedPage() {
        this.csvinputData = [];
        let fields = [];
        fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          // item.edit = false;
          this.csvinputData.push(item);
        });
      },


      closeDialog() {
        util.$emit('closeDialog', 'close');
        // 初始化画3布数据
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
        // this.step.file_name='';
        let params ={
          fileId:this.step.showFileName.split(',')[0],
          fileType:'csv'
        }
        getFileByFileFolder(params).then(res=>{
          this.files = res.data.content;
        })
      },
      /**
       * 根据文件id获取文件信息
       */
      selectFile(fileId){
        // console.info("zhigaibian");
        let params={
          fileId:this.step.file_name
        }
        getFileByFileId(params).then(res=>{
          this.handleSelect(res.data.content);
        })
      },

      init() {
        this.getAllFolder();
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        // 获取当前步骤信息参数
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        };
        this.step.fields.field = [];
        //this.csvinputData = [];
        let curStep = store.getters.getCurrentStep(param);
        //判断是否是首次打开控件
        if (curStep !== undefined) {
          this.step.showFileName = curStep.showFileName;
          this.step.name = curStep.name;
          this.step.initFlag = curStep.initFlag;
          this.step.description = curStep.description;
          this.step.distribute = curStep.distribute;
          this.step.copies = curStep.copies;
          this.step.partitioning = curStep.partitioning;
          this.step.model_name = curStep.model_name;
          this.step.remotesteps = curStep.remotesteps;
          this.step.GUI = curStep.GUI;
          let fields = JSON.stringify(curStep.fields.field);
          let tmpFields = JSON.parse(fields);
          tmpFields.forEach((item, index) => {
            item.edit = false;
            this.step.fields.field.push(item);
          });
          this.total = this.step.fields.field.length;
          this.selectedPage();
          this.step.filename = curStep.filename;
          this.step.header = (curStep.header === 'Y' || curStep.header === undefined);
          if(curStep.file_name !=='' && curStep.file_name!==undefined){
            this.step.file_name = curStep.file_name;
            this.getFileList();
          }
          this.step.newline_possible = curStep.newline_possible === 'Y';
          this.step.delimiter = curStep.delimiter === undefined ? ',' : curStep.delimiter;
          this.step.enclosure = curStep.enclosure === undefined ? '\"' : curStep.enclosure;
          this.step.buffer_size = curStep.buffer_size === undefined ? '5000' : curStep.buffer_size;
          this.step.lazy_conversion = (curStep.lazy_conversion === 'Y' || curStep.lazy_conversion === undefined);
          this.step.add_filename_result = (curStep.add_filename_result === 'Y');
          this.step.parallel = curStep.parallel === 'Y';
          this.step.encoding = curStep.encoding === undefined ? 'UTF-8' : curStep.encoding;
          this.step.fileId = curStep.fileId;
          this.step.ftp_password = curStep.ftp_password;
          this.step.ftp_username = curStep.ftp_username;
          this.step.file_server_type = curStep.file_server_type;
          this.step.tableName = this.nodeData.id;
        }
        console.info(this.step);
      },

      submit() {
        this.step.initFlag = true;
        // this.step.fields.field = this.csvinputData;
        let step = Object.assign({}, this.step);
        if (this.step.header === true) {
          step.header = 'Y'
        } else {
          step.header = 'N'
        }
        if (this.step.newline_possible === true) {
          step.newline_possible = 'Y';
        } else {
          step.newline_possible = 'N';
        }
        step.add_filename_result = this.step.add_filename_result ? 'Y' : 'N';
        step.lazy_conversion = this.step.lazy_conversion ? 'Y' : 'N';
        step.parallel = this.step.parallel ? 'Y' : 'N';
        step['oldStepName'] = this.oldStepName;
        step.distribute = this.distribute ? 'Y' : 'N';
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
        };
        console.info("step,-=-=-", step);
        store.dispatch('updateStepInfo', param);
        let params = {
          id: this.nodeData.id, //插件id
          label: step.name,
          oldName: this.oldStepName,
        };
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
    },
  }
</script>

<style>
  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }

  input[type="file"] {
    display: none;
  }
</style>
