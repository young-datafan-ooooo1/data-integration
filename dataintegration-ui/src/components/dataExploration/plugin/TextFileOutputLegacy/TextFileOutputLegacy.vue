<template>
  <div class="plugin_body">
    <div class="plugin_content div_form plugin_form1 ">
      <el-form :model="step" ref="stepfrom" label-width="30%" size="mini" :rules="rules">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <!-- 页体 -->
      <el-tabs @tab-click="handleClick">
        <!-- 基础属性界面 -->
        <el-tab-pane label="基本属性">
          <el-form :model="step" ref="stepfrom" label-width="30%" :rules="rules" size="mini">
            <el-form-item prop="showFileName" label="文件夹">
              <el-select v-model="step.showFileName" size="mini" placeholder="请选择文件夹" style="width: 65%" @change="getFileList">
                <el-option v-for="item in dirs" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
              </el-select>
              <el-button size="mini" type="primary" @click="changeFile">新建</el-button>
            </el-form-item>
            <el-form-item prop="file_name" label="文件">
              <el-select v-model="step.file_name" size="mini" placeholder="请选择文件"  filterable allow-create style="width: 65%" :disabled="step.showFileName===''||step.showFileName===undefined">
                <el-option v-for="item in files" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
              </el-select>
            </el-form-item>
<!--            <el-form-item label="选择目录:" prop="fileName">-->
<!--              <el-autocomplete class="inline-input" v-model="step.fileName" :fetch-suggestions="querySearch"-->
<!--                               :disabled="file"-->
<!--                               :trigger-on-focus="false" @select="handleSelect" style="width: 65%">-->
<!--                <el-button slot="append" size='mini' type="primary" @click="changeFile" :disabled="file">创建目录-->
<!--                </el-button>-->
<!--              </el-autocomplete>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="文件名:" prop="file_name">-->
<!--              <el-input v-model="step.file_name" style="width: 65%;" :disabled="step.fileNameInField === true"></el-input>-->
<!--            </el-form-item>-->
            <el-form-item label="结果输送至命令行或脚本">
              <el-checkbox v-model="step.file.is_command" :disabled="step.file.servlet_output===true"
                          ></el-checkbox>
            </el-form-item>
            <el-form-item label="传递输出到servlet">
              <el-checkbox v-model="step.file.servlet_output" :disabled="dis"
                           ></el-checkbox>
            </el-form-item>
            <el-form-item label="创建父目录">
              <el-checkbox v-model="step.create_parent_folder"  :disabled="step.file.is_command===true"></el-checkbox>
            </el-form-item>

            <el-form-item label="启动时不创建文件名">
              <el-checkbox v-model="step.file.do_not_open_new_file_init"
                           :disabled="step.fileNameInField === true"></el-checkbox>
            </el-form-item>
            <el-form-item label="从字段中获取文件名">
              <el-checkbox v-model="step.fileNameInField"
              ></el-checkbox>
            </el-form-item>
            <el-form-item label="文件名字段">
              <el-select v-model="step.fileNameField"  style="width: 65%;" :disabled="step.fileNameInField === false" clearable>
                <el-option v-for="item in step.outFields" :label="item.name" :key="item.name" :value="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="扩展名">
              <el-input v-model="step.file.extention" cleareable style="width: 65%;" :disabled="step.fileNameInField === false"></el-input>
            </el-form-item>
            <el-form-item label="文件名里包含步骤数">
              <el-checkbox v-model="step.file.split"  :disabled="step.fileNameInField === true"></el-checkbox>
            </el-form-item>
            <el-form-item label="文件名里包含数据分区号">
              <el-checkbox v-model="step.file.haspartno"   :disabled="step.fileNameInField === true"></el-checkbox>
            </el-form-item>
            <el-form-item label="文件名里包含日期">
              <el-checkbox v-model="step.file.add_date" :disabled="step.fileNameInField === true || step.file.SpecifyFormat ===true" ></el-checkbox>

            </el-form-item>
            <el-form-item label="文件名里包含时间">
              <el-checkbox v-model="step.file.add_time" :disabled="step.fileNameInField === true || step.file.SpecifyFormat ===true" ></el-checkbox>
            </el-form-item>
            <el-form-item label="指定日期时间格式">
              <el-checkbox v-model="step.file.SpecifyFormat" :disabled="step.fileNameInField === true"
                           ></el-checkbox>
            </el-form-item>
            <el-form-item label="日期时间格式">
              <el-select v-model="step.file.date_time_format" style="width: 65%;" :disabled="step.fileNameInField === true"
                         size="mini">
                <el-option v-for="item in date_time_format" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" :disabled="step.fileNameInField === true">显示文件名</el-button>
            </el-form-item>
            <el-form-item label="结果中添加文件名">
              <el-checkbox v-model="step.file.add_to_result_filenames" ></el-checkbox>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 内容界面 -->
        <el-tab-pane label="内容">
          <el-form :model="step" ref="stepfrom" label-width="30%" size="mini">
            <el-form-item label="分隔符">
              <el-input v-model="step.separator" style="width: 65%;"></el-input>
              <el-button type="primary" size="mini" @click="tab">插入Tab</el-button>
            </el-form-item>
            <el-form-item label="封闭符">
              <el-input v-model="step.enclosure" style="width: 65%;"></el-input>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.header" label="头部"></el-checkbox>
              <el-checkbox v-model="step.footer" label="尾部"></el-checkbox>
              <el-checkbox v-model="step.append" label="追加方式"></el-checkbox>
            </el-form-item>
            <el-form-item label="格式">
              <el-select v-model="step.format" style="width:65%">
                <el-option v-for="item in format" :key="item.key" :label="item.label" :value="item.key"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="压缩">
              <el-select v-model="step.compression" style="width:65%">
                <el-option v-for="item in select_compressions" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="编码">
              <el-select v-model="step.encoding" style="width:65%">
                <el-option v-for="item in encoding" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.enclosure_forced" label="在字段周围强制加封闭符"></el-checkbox>
              <el-checkbox v-model="step.enclosure_fix_disabled" label="禁用封闭符修复"></el-checkbox>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.file.pad" label="字段右填充或裁剪"></el-checkbox>
              <el-checkbox v-model="step.file.fast_dump" style="margin-left: 40px;" label="快速数据存储(无格式)"></el-checkbox>
            </el-form-item>
            <el-form-item label="分拆....每一行">
              <el-input v-model="step.file.splitevery" style="width:65%"></el-input>
            </el-form-item>
            <el-form-item label="添加文件结束行">
              <el-input v-model="step.endedLine" style="width:65%"></el-input>
            </el-form-item>
          </el-form>

        </el-tab-pane>
        <!-- 字段界面 -->
        <el-tab-pane label="字段">
          <div>
            <el-button style="float:right ;;margin-bottom: 5px;margin-left: 5px" type="primary" @click="addRow"
                       size="mini">新增
            </el-button>
            <span>
            <el-button type="primary" style="float: right" @click="getDialog" size="mini">获取字段</el-button>
              </span>
          </div>
          <el-table :data="showFields" border height="330px" :header-cell-style="{background: '#eef1f6'}">
            <el-table-column type="index" label="#" width="50"></el-table-column>
            <el-table-column prop="name" label="字段名称" width="100">
              <template slot-scope="scope">
                <el-select v-model="scope.row.name" size="mini" style="width: 100%;" clearable filterable allow-create v-if="scope.row.edit === true">
                  <el-option v-for="item in tablename" :key="item.name" :label="item.name" :value="item.name">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型">
              <template slot-scope="scope">
                <el-select v-model="scope.row.type" size="mini" style="width: 100%;" @change="formatData(scope.row)" v-if="scope.row.edit === true">
                  <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.type}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="length" label="长度">
              <template slot-scope="scope">
                <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.length" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.length}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="precision" label="精度">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.precision" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.precision}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="repea" label="重复">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.repea" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.repea}}</span>
              </template>
            </el-table-column>

            <el-table-column prop="format" label="格式" width="100">
              <template slot-scope="scope">
                <el-select v-model="scope.row.format" size="mini" style="width: 100%;" clearable v-if="scope.row.edit === true">
                  <el-option v-for="item in formats" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.format}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="decimal" label="小数">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.decimal" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.decimal}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="group" label="分组">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.group" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.group}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="repeat" label="去除空字符串方式" width="180">
              <template slot-scope="scope">
                <el-select v-model="scope.row.repeat" clearable size="mini" style="width: 100%" v-if="scope.row.edit === true">
                  <el-option v-for="item in trimType" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
                <span v-else>{{scope.row.repeat}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="null" label="Null">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.null" v-if="scope.row.edit === true">
                </el-input>
                <span v-else>{{scope.row.null}}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100px" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
                <el-button size="mini" type="text" @click="editLine(scope.row)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination style="float: right" class="right" @current-change="selectedPage" :current-page.sync="curPage"
                         @size-change="selectedPage" :page-size.sync="pageSize" :page-sizes="[10, 50, 150]"
                         layout="total, sizes, prev, pager, next"
                         :total="step.fields.field.length">
          </el-pagination>
          <!--					<div style="margin-top: 20px; text-align: center;">-->
          <!--						<el-button type="primary" @click="getDialog" size="mini">获取字段</el-button>-->
          <!--						<el-button type="primary" @click="mixwidth" size="mini">最小宽度</el-button>-->
          <!--					</div>-->
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer plugin_footer">
			<span>
				<el-button @click="closeDialog" size="mini">取 消</el-button>
				<el-button @click="submit('stepfrom')" type="primary" size="mini">确 定</el-button>
			</span>
    </div>
    <el-dialog title="新增文件夹" :visible.sync="selectFileVisiable" v-if="selectFileVisiable" class="dialog_temp other_dialog"
               :modal-append-to-body='false'
               :append-to-body="true" width="50%" :before-close="closeAdd">
      <!-- 子组件 -->
      <fileData :params="queryParams" :selec-data="selectData" :fileType="fileType" fileFolder="1"
                createChannel="OUTPUT"
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
    getAllFilFolderList,
    getFileByFileFolder,
    getFileByFileId
  } from '../../../../api/api.js'
  import {compareFields} from "../../../../assets/common/tool";

  export default {
    components: {
      fileData,
    },
    data() {
      return {
        dirs:[],
        files:[],
        showFields:[],
        tablename: [],
        pageSize: 10,
        curPage: 1,
        total: 0,
        key: '', //插件对应的画布
        nodeData: {},
        restaurants: [],
        selectData: {},
        createUser: null,
        lastStep: null,
        lastStepFields: [],
        oldStepName: null,
        file: false,
        filename: false,
        exten: false,
        display: false,
        resultoutput: false,
        Specifyformat: false,
        stepnum: false,
        dis: false,
        datetimeformat: true,
        creatfa: false,
        notcreat: false,
        getfilename: false,
        filevalue: true,
        numpartition: false,
        data: false,
        time: false,
        fileData: null,
        selectFileVisiable: false,
        queryParams: {},
        fileId: '', //文件编号
        fileType: '',
        date_time_format: ["m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss",
          "m/d/yy h:mm", "mm:ss", "[h]:mm:ss", "mm:ss.0", "yyyy-mm-dd hh:mm:ss", "yyyy/mm/dd", "yyyy-MM-dd",
          "yyyy/mm/dd hh/mm/ss"
        ],
        format: [{
          key: 'DOS',
          label: 'CR+LF终止(Windows,DOS)'
        },
          {
            key: 'UNIX',
            label: 'LF终止(Unix)'
          },
          {
            key: 'CR',
            label: 'CR终止'
          },
          {
            key: 'None',
            label: '没有新的线路终端'
          }
        ],
        select_compressions: ["GZip", "Hadoop-snappy", "None", "Snappy", "Zip"],
        encoding: ["Big5", "Big5-HKSCS", "CESU-8", "EUC-JP", "EUC-KR", "GB18030", "GB2312", "GBK", "IBM-Thai", "IBM00858",
          "IBM01140", "IBM01141", "IBM01142", "IBM01143", "IBM01144", "IBM01145", "IBM01146", "IBM01147", "IBM01148",
          "IBM01149", "IBM037", "IBM1026", "IBM1047", "IBM273", "IBM277", "IBM278", "IBM280", "IBM284", "IBM285", "IBM290",
          "IBM297", "IBM420", "IBM424", "IBM437", "IBM500", "IBM775", "IBM850", "IBM852", "IBM855", "IBM857", "IBM860",
          "IBM861", "IBM862", "IBM863", "IBM864", "IBM865", "IBM866", "IBM868", "IBM869", "IBM870", "IBM871", "IBM918",
          "ISO-2022-CN", "ISO-2022-JP", "ISO-2022-JP-2", "ISO-2022-KR", "ISO-8859-1", "ISO-8859-13", "ISO-8859-15",
          "ISO-8859-2", "ISO-8859-3", "ISO-8859-4", "ISO-8859-5",
          "ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9", "JIS_X0201", "JIS_X0212-1990", "KOI8-R", "KOI8-U",
          "Shift_JIS", "TIS-620", "US-ASCII", "UTF-16", "UTF-16BE", "UTF-16LE", "UTF-32", "UTF-32BE", "UTF-32LE", "UTF-8",
          "windows-1250", "windows-1251", "windows-1252", "windows-1253", "windows-1254", "windows-1255", "windows-1256",
          "windows-1257", "windows-1258", "windows-31j", "x-Big5-HKSCS-2001", "x-Big5-Solaris", "x-COMPOUND_TEXT",
          "x-euc-jp-linux", "x-EUC-TW", "x-eucJP-Open", "x-IBM1006",
          "x-IBM1025", "x-IBM1046", "x-IBM1097", "x-IBM1098", "x-IBM1112", "x-IBM1122", "x-IBM1123", "x-IBM1124",
          "x-IBM1166", "x-IBM1364", "x-IBM1381", "x-IBM1383", "x-IBM300",
          "x-IBM33722", "x-IBM737", "x-IBM833", "x-IBM834", "x-IBM856", "x-IBM874", "x-IBM875", "x-IBM921", "x-IBM922",
          "x-IBM930", "x-IBM933", "x-IBM935", "x-IBM937",
          "x-IBM939", "x-IBM942", "x-IBM942C", "x-IBM943", "x-IBM943C", "x-IBM948", "x-IBM949", "x-IBM949C", "x-IBM950",
          "x-IBM964", "x-IBM970", "x-ISCII91",
          "x-ISO-2022-CN-CNS", "x-ISO-2022-CN-GB", "x-iso-8859-11", "x-JIS0208", "x-JISAutoDetect", "x-Johab",
          "x-MacArabic", "x-MacCentralEurope", "x-MacCroatian", "x-MacCyrillic", "x-MacDingbat", "x-MacGreek",
          "x-MacHebrew", "x-MacIceland", "x-MacRoman", "x-MacRomania", "x-MacSymbol", "x-MacThai", "x-MacTurkish",
          "x-MacUkraine",
          "x-MS932_0213", "x-MS950-HKSCS", "x-MS950-HKSCS-XP", "x-mswin-936", "x-PCK", "x-SJIS_0213", "x-UTF-16LE-BOM",
          "X-UTF-32BE-BOM", "X-UTF-32LE-BOM", "x-windows-50220", "x-windows-50221", "x-windows-874", "x-windows-949",
          "x-windows-950", "x-windows-iso2022jp"
        ],
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
        formatDate: ["m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss",
          "m/d/yy h:mm",
          "mm:ss", "[h]:mm:ss", "mm:ss.0", "yyyy-mm-dd hh:mm:ss", "yyyy/mm/dd", "yyyy-MM-dd", "yyyy/mm/dd hh/mm/ss"
        ],
        trimType: ["none", "left", "right", "both"],
        step: {
          dirName:'',
          file_Name:'',
          fileName:'',
          name: "文本文件文件输出",
          type: "TextFileOutput2",
          file_server_type: '',
          ftp_username: "",
          ftp_password: "",
          distribute: "Y",
          copies: "1",
          partitioning: { "method": "none" },
          separator: ";",
          enclosure: "\"",
          enclosure_forced: false,
          enclosure_fix_disabled: false,
          header: true,
          footer: false,
          format: "DOS",
          compression: "None",
          encoding: "GBK",
          fileNameInField: false,
          fileNameField: "",
          create_parent_folder: true,
          file: {
            name: "file",
            servlet_output: false,
            do_not_open_new_file_init: false,
            extention: "txt",
            append: false,
            split: false,
            haspartno: false,
            add_date: false,
            add_time: false,
            SpecifyFormat: false,
            add_to_result_filenames: true,
            pad: false,
            fast_dump: false,
            splitevery: "0",
            is_command: false
          },
          fields: {field:[
            ]},
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "224",
            yloc: "544",
            draw: "Y"
          }
        },
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          fileName: [{
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }],
          dirName:[{
            required:true,
            message:'请选择文件夹',
            trigger:'change'
          }],
          file_name:[{
            required:true,
            message:'文件不能为空',
            trigger:'change,blur'
          }]
        },
      };
    },
    mounted() {
      this.init();
      util.$on("close_dialog", () => {
        this.closeAdd();
      });
      this.getFileByFileType();
    },
    methods: {

      mixwidth() {

      },
      editLine(row){
        this.step.fields.field.forEach((item, index) => {
          if (item.name === row.name) {
            // item.decimal='.'
            this.$set(this.step.fields.field[index], 'edit', true);
            this.$forceUpdate();
          }
        })
      },

      tab() {
        this.step.separator = '	' + this.step.separator;
      },


      getDialog() {
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: this.step.name
        };

        //生成本步骤输出字段
        let outFields = store.getters.generateOutputFields(param);
        this.step.outFields = outFields;
        this.step.fields.field = [];
        console.info(outFields.length);
        this.lastStepFields = JSON.parse(JSON.stringify(this.step.outFields));
        this.step.outFields.forEach(item => {
          this.step.fields.field.push({
            edit:false,
            name: item.name,
            type: item.type,
            repeat: item.repeat,
            decimal: item.decimal,
            group: item.group,
            length: '',
            precision:'',
          })
        });
        this.selectedPage();
      },


      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据

      },

      handleClick(tab, event) {
        console.log(tab, event);
      },

      addRow(event) { //新增一行
        this.step.fields.field.push({
          edit:false,
          name: '',
          type: '',
          format: '',
          trim_type: '',
          length: -1,
          null: '',
          group: '',
          repeat: '',
          decimal: '',
        });
        this.curPage = parseInt(this.step.fields.field.length / this.pageSize + 1);
        this.selectedPage();
      },

      deleteRow(index) { //移除一行
        // let newIndex = (this.curPage - 1) * this.pageSize + index
        this.step.fields.field.splice(index, 1); //删掉该行
        this.selectedPage();
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
      /**
       * 获取上传的文件信息
       */
      getFileData(fileData) {
        this.step.showFileName = fileData.fileId+','+fileData.fileName;
        this.selectFileVisiable = false;
        this.getAllFolder();
        this.getFileList();
      },

      handleSelect(item) {
        this.fileId = item.fileId;
        this.step.fileId = item.fileId;
        this.step.fileName = item.fileName;
        this.step.file.filePath = item.fileRelativPath;
        this.step.ftp_username = item.userName;
        this.step.file_server_type = item.fileServerType;
        this.step.ftp_password = item.password;
      },

      // getAllFields() {
      // 	this.lastStepFields.forEach((item, index) => {
      // 		this.step.fields.field.push({
      // 			name: item.name,
      // 			type: item.type,
      // 			length: item.length,
      // 			repeat: item.repeat,
      // 			format: item.format,
      // 			currency: item.currency,
      // 			decimal: item.decimal,
      // 			group: item.group,
      // 			trim_type: item.trim_type,
      // 			null: item.null,
      // 			precision: item.precision

      // 		});
      // 	});
      // 	//this.selectedPage();
      // },
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
              });
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
        console.info(this.step.fields.field.length);
        fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          // item.length='';
          // item.precision='';
          this.showFields.push(item);
        });

      },

      selectFields(row) {
        let obj = {};
        obj = this.lastStepFields.find((item) => { //这里的selectList就是上面遍历的数据源
          return item.name === row.name; //筛选出匹配数据
        });
        if (obj !== undefined && obj !== null) {
          row.type = obj.type;
          row.length = obj.length;

        }
      },

      changeFile() {
        this.queryParams = {
          fileType: '',
          isFolder: '1',
        };
        this.selectFileVisiable = true;
      },

      closeAdd() {
        this.selectFileVisiable=false;
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          this.step.file.name = this.$refs.fileData.selecData.fileName;
          this.step.fileId = this.$refs.fileData.selecData.fileId;
          this.step.fileName = this.$refs.fileData.selecData.fileName;
          this.step.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.ftp_username = this.$refs.fileData.selecData.userName;
          this.step.file_server_type = this.$refs.fileData.selecData.fileServerType;
          this.step.ftp_password = this.$refs.fileData.selecData.password;
        }
        this.selectFile();
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
          fileId:this.step.showFileName.split(',')[0],
          fileType:'txt'
        }
        getFileByFileFolder(params).then(res=>{
          this.files = res.data.content;
        })
        this.selectFile();
      },
      selectFile(){
        let params={
          fileId:this.step.showFileName.split(",")[0]
        }
        getFileByFileId(params).then(res=>{
          this.handleSelect(res.data.content);
        })
      },

      init() {
        this.getAllFolder();
        this.step.fields.field=[];
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        /* this.created_user = store.getters.getUserId; */
        let stepName = this.step.name;
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        };

        let laststeps = store.getters.getLastStep(param);
        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
        }

        //生成本步骤输出字段
        let outFields = store.getters.generateOutputFields(param);
        this.step['outFields'] = outFields;

        let obj = store.getters.getCurrentStep(param);
        let curstep = {};
        if (obj.initValid == undefined) {
          if (this.laststep !== undefined) {
            this.lastStepFields = outFields;
            this.lastStepFields.forEach(item => {
              this.tablename.push({
                name: item.name,
              })
            });
            // this.getAllFields();
          }
        } else {
          this.lastStepFields = outFields;
          this.lastStepFields.forEach(item => {
            this.tablename.push({
              name: item.name,

            })
          });
         let tempFields = JSON.parse(JSON.stringify(obj.fields.field));

          // this.getAllFields();
          this.step = obj;
          if(this.step.file_name!==''&&this.step.file_name!==undefined){
            this.getFileList();
          }
          this.step.fields.field=[];
          console.info(obj)
          tempFields.forEach(item=>{
            item.edit = false;
            this.step.fields.field.push(item)
          })
          this.selectedPage();
        }
        for (let key in this.step) {
          if (this.step[key] == 'Y') {
            this.step[key] = true;
          } else if (this.step[key] == 'N') {
            this.step[key] = false;
          }
        }
        for (let key in this.step.file) {
          if (this.step.file[key] == 'Y') {
            this.step.file[key] = true;
          } else if (this.step[key] == 'N') {
            this.step.file[key] = false;
          }
        }
        if (typeof curstep.file == 'undefined' || typeof curstep.file.name == 'undefined') {
        } else {
          this.step.file.name = curstep.file.name;
          this.step.fileName = curstep.fileName;
        }
      },
      submit(stepfrom) {
        this.step.initFlag = true;
        this.step.initValid = false;
        let step = Object.assign({}, this.step);
        if (this.step.file.is_command == true) {
          step.file.is_command = 'Y';
        } else {
          step.file.is_command = 'N';
        }
        if (this.step.file.servlet_output == true) {
          step.file.servlet_output = 'Y';
        } else {
          step.file.servlet_output = 'N';
        }
        if (this.step.create_parent_folder == true) {
          step.create_parent_folder = 'Y';
        } else {
          step.create_parent_folder = 'N';
        }
        if (this.step.file.do_not_open_new_file_init == true) {
          step.file.do_not_open_new_file_init = 'Y';
        } else {
          step.file.do_not_open_new_file_init = 'N';
        }
        if (this.step.fileNameInField == true) {
          step.fileNameInField = 'Y';
        } else {
          step.fileNameInField = 'N';
        }
        if (this.step.file.haspartno == true) {
          step.file.haspartno = 'Y'
        } else {
          step.file.haspartno = 'N'
        }
        if (this.step.file.split == true) {
          step.file.split = 'Y';
        } else {
          step.file.split = 'N';
        }
        if (this.step.file.add_date == true) {
          step.file.add_date = 'Y';
        } else {
          step.file.add_date = 'N';
        }
        if (this.step.file.add_time == true) {
          step.file.add_time = 'Y';
        } else {
          step.file.add_time = 'N';
        }
        if (this.step.file.SpecifyFormat == true) {
          step.file.SpecifyFormat = 'Y';
        } else {
          step.file.SpecifyFormat = 'N';
        }
        if (this.step.file.add_to_result_filenames == true) {
          step.file.add_to_result_filenames = 'Y';
        } else {
          step.file.add_to_result_filenames = 'N';
        }
        if (this.step.header == true) {
          step.header = 'Y';
        } else {
          step.header = 'N';
        }
        if (this.step.footer == true) {
          step.footer = 'Y';
        } else {
          step.footer = 'N';
        }
        if (this.step.append == true) {
          step.append = 'Y';
        } else {
          step.append = 'N';
        }
        if (this.step.enclosure_forced == true) {
          step.enclosure_forced = 'Y';
        } else {
          step.enclosure_forced = 'N';
        }
        if (this.step.enclosure_fix_disabled == true) {
          step.enclosure_fix_disabled = 'Y';
        } else {
          step.enclosure_fix_disabled = 'N';
        }
        if (this.step.file.fast_dump == true) {
          step.file.fast_dump = 'Y';
        } else {
          step.file.fast_dump = 'N';
        }
        if (this.step.file.pad == true) {
          step.file.pad = 'Y';
        } else {
          step.file.pad = 'N';
        }


        this.$refs[stepfrom].validate((valid) => {
          if (valid) {
            this.step.file.name = this.step.fileName + this.created_user + "/" + this.step.file_name;
            this.step['oldStepName'] = this.oldStepName;
            this.step.file.name = this.step.file.name = this.step.file.filePath + '/' + this.step.file_name;
            let paramName = this.step.name;
            let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
            this.step.file.name = this.step.file.name = this.step.file.filePath + '/' + this.step.file_name;
            let flag = false;
            if (this.step.name !== newName) {
              flag = true;
              this.step.name = newName;
            }
            let param = {
              key: this.key,
              value: {
                oldStepName: this.oldStepName,
                step: step
              }
            };
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

            // 修改krt节点信息
            store.dispatch('updateStepInfo', param);
            let params = {
              id: this.nodeData.id, //插件id
              label: this.step.name,
              oldStepName: this.oldStepName
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
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
    }
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
