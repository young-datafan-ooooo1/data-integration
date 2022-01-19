<template>
  <div class="plugin_body">
    <div style="height: 90%">
      <el-tabs type="card" active-name="advanced">
        <el-tab-pane label="基础属性" name="base">
          <el-form class="title_div" :rules="rules" ref="step" :model="step" label-width="80px">
            <el-form-item label="步骤名称:" prop="name">
              <el-input v-model="step.name"></el-input>
            </el-form-item>
            <el-form-item label="类型:" prop="stepType">
              <el-input readonly v-model="step.stepType"></el-input>
            </el-form-item>
            <el-form-item label="说明:" prop="describes">
              <el-input type="textarea" v-model="step.describes"></el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="列属性" name="advanced">
          <el-form class="title_div" :rules="rules" ref="step" :model="step" label-width="80px" size="mini">
            <el-form-item label="存放目录" prop="file_name">
              <el-input v-model="step.filename" readonly></el-input>
              <el-button type="primary" @click="changeFile">选择</el-button>
            </el-form-item>
            <el-form-item label="文件名" prop="file_name">
              <el-input v-model="step.file_name"></el-input>
            </el-form-item>
          </el-form>
          <div>
            <el-button style="float:right ;;margin-bottom: 5px;margin-right: 5px" type="primary" @click="addRow"
                       size="mini">新增
            </el-button>
            <el-button style="float:right;;margin-bottom: 5px;margin-right: 5px" type="primary" @click="pushGetFields"
                       size="mini">获取字段
            </el-button>
          </div>
          <el-table :data="showFields" border style="width: 100%;margin-top: 5px" height="210"
                    :header-cell-style="{background:'#eef1f6'}">
            <el-table-column type="index" label="序号" width="50">
            </el-table-column>
            <el-table-column prop="name" label="名称">
              <template slot-scope="scope">
                <el-select v-model="scope.row.name" @change="selectFields(scope.row)" size="mini" style="width: 100%;">
                  <el-option v-for="item in lastStepFields" :key="item.name" :label="item.nameCn" :value="item.name">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="name" label="显示名称">
              <template slot-scope="scope">
                <el-input v-model="scope.row.name" size="mini" style="width: 100%;">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型">
              <template slot-scope="scope">
                <el-select v-model="scope.row.type" size="mini" style="width: 100%;" disabled readonly>
                  <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="format" label="格式">
              <template slot-scope="scope">
                <el-select v-model="scope.row.format" size="mini" style="width: 100%;" clearable>
                  <el-option v-for="item in formats" :key="item" :label="item" :value="item">
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="length" label="长度" width="80">
              <template slot-scope="scope">
                <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.length">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column prop="precision" label="精度" width="80">
              <template slot-scope="scope">
                <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.precision">
                </el-input>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100px">
              <template slot-scope="scope">
                <el-button type="text" size="mini" :disabled="scope.$index===0" @click="moveUp(scope.$index,scope.row)">
                  <i
                    class="el-icon-arrow-up"></i></el-button>
                <el-button type="text" size="mini" :disabled="scope.$index===(showFields.length-1)"
                           @click="moveDown(scope.$index,scope.row)"><i
                  class="el-icon-arrow-down"></i></el-button>
                <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination style="float: right" class="right" @current-change="selectedPage" :current-page.sync="curPage"
                         @size-change="selectedPage" :page-size.sync="pageSize"
                         :page-sizes="[10, 20, 30, 40,step.fields.field.length]"
                         layout="total, sizes, prev, pager, next" :total="step.fields.field.length">
          </el-pagination>
        </el-tab-pane>
        <el-tab-pane label="高级">
          <el-form class="title_div" :rules="rules" ref="step" :model="step" label-width="150px" size="mini">
            <el-form-item label="记录追加：">
              <el-radio v-model="step.file.append" label="Y">是</el-radio>
              <el-radio v-model="step.file.append" label="N">否</el-radio>
            </el-form-item>
            <el-form-item label="包含头部：">
              <el-radio v-model="step.header" label="Y">是</el-radio>
              <el-radio v-model="step.header" label="N">否</el-radio>
            </el-form-item>
            <el-form-item label="列分隔符：">
              <el-input v-model="step.separator" style="width: 50%"></el-input>
            </el-form-item>
            <el-form-item label="强制使用封闭符：">
              <el-radio v-model="step.enclosure_forced" label="Y">是</el-radio>
              <el-radio v-model="step.enclosure_forced" label="N">否</el-radio>
            </el-form-item>
            <el-form-item label="封闭符：">
              <el-input v-model="step.enclosure" style="width: 50%"></el-input>
            </el-form-item>

            <el-form-item label="文件格式：">
              <el-select v-model="step.format" style="width: 50%">
                <el-option v-for="item in formatMapperLineTerminator" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="编码：">
              <el-select v-model="step.encoding" style="width: 50%">
                <el-option v-for="item in encodings" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="压缩：">
              <el-select v-model="step.compression" style="width: 50%">
                <el-option v-for="item in fileCompressionTypeCodes" :key="item" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div>
      <span slot="footer" class="dialog-footer" style="float:right;">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="submit('step')">确 定</el-button>
      </span>
    </div>
    <el-dialog title="提示" :visible.sync="confirmDialog" :modal-append-to-body='false' width="30%"
               :before-close="handleClose">
      <span>此操作将清空所有字段信息</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmDialogCancel">取 消</el-button>
        <el-button type="primary" @click="confirmDialogOk">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="选择文件" :visible.sync="selectFileVisiable" class="dialog_temp" :modal-append-to-body='false'
               :append-to-body="true" width="30%" :before-close="closeAdd">
      <!-- 子组件 -->
      <fileData :params="queryParams" :selec-data="selectData" :fileType="fileType" createChannel="OUTPUT" ref="fileData"></fileData>
    </el-dialog>
  </div>

</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store.js";
  import fileData from '../../../common/FileData.vue'

  export default {
    name: "CsvFileOutput",
    data() {
      return {
        fileType: '',
        selectFileVisiable: false,
        queryParams: {},
        selectData: {},
        showFields: [],
        curPage: 1,
        pageSize: 10,
        formatMapperLineTerminator: ["DOS", "UNIX", "CR", "None"],
        fileCompressionTypeCodes: ["None", "Zip"],
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
        key: '', //插件对应的画布
        nodeData: '',
        trunc_first: "0",
        stepType: '文件输出',
        rename_field: "0",
        lastStepFields: [],
        lastStep: null,
        createUser: null,
        step_name: "csv输出",
        oldStepName: null,
        confirmDialog: false,
        fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"],
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
        ],
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          stepType: [{
            required: true,
            message: '',
            trigger: 'blur'
          }],
          file_name: [{
            required: true,
            message: '文件名不能为空',
            trigger: 'blur'
          }],
          separator: [{
            required: true,
            message: '分隔符不能为空你',
            trigger: 'blur'
          }],
          extention: [{
            required: true,
            message: '文件类型不能为空',
            trigger: 'blur'
          }]
        },
        step: {
          fileId: '',
          filename: '',
          filePath: '',
          fsuserName: '',
          fspassWord: '',
          baseDir: "./business_out_data/",
          initFlag: false,
          name: "CSV文件输出",
          type: "CsvFileOutput",
          stepType: 'CSV文件输出',
          modelName: "",
          file_name: null,
          describe: "",
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          isBusiness: "Y",
          separator: ",",
          endFileExtension: "end",
          enclosure: "\"",
          enclosure_forced: "N",
          enclosure_fix_disabled: "N",
          header: "Y",
          footer: "N",
          format: "UNIX",
          compression: "None",
          encoding: "UTF-8",
          endedLine: "",
          fileNameInField: "N",
          fileNameField: "",
          create_parent_folder: "Y",
          describes: '',
          file: {
            filePath:'',
            name: "file",
            servlet_output: "N",
            do_not_open_new_file_init: "Y",
            extention: "csv",
            append: "N",
            split: "Y",
            haspartno: "N",
            add_date: "Y",
            add_time: "Y",
            SpecifyFormat: "N",
            date_time_format: "yyyy-MM-dd HH:mm:ss",
            add_to_result_filenames: "N",
            pad: "N",
            fast_dump: "N",
            splitevery: ""
          },
          fields: {
            field: []
          },
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 448,
            yloc: 160,
            draw: "Y"
          }
        }
      }
    },
    mounted() {
      this.init();
      util.$on("close_dialog", () => {
        this.closeAdd();
      })

    },
    methods: {
      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          this.step.fileId = this.$refs.fileData.selecData.fileId;
          this.step.filename = this.$refs.fileData.selecData.fileName;
          this.step.file.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.fsuserName = this.$refs.fileData.selecData.userName;
          this.step.fspassWord = this.$refs.fileData.selecData.password;
        }
        this.selectFileVisiable = false;
      },
      selectedPage() {
        this.showFields = [];
        let fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          this.showFields.push(item);
        });
      },
      deleteRow(index) { //移除一行
        let newIndex = (this.curPage - 1) * this.pageSize + index
        this.step.fields.field.splice(newIndex, 1); //删掉该行
        this.selectedPage();
      },
      addRow(event) { //新增一行
        this.step.fields.field.push({
          name: '',
          type: '',
          format: '',
          currency: '',
          decimal: '',
          group: '',
          trim_type: '',
          nullif: '',
          length: -1,
          precision: -1
        });
        this.curPage = parseInt(this.step.fields.field.length / this.pageSize + 1);
        this.selectedPage();
      },
      moveUp(index, row) {
        var that = this;
        if (index > 0) {
          let newIndex = (this.curPage - 1) * this.pageSize + index
          let upDate = that.step.fields.field[newIndex - 1];
          that.step.fields.field.splice(newIndex - 1, 1);
          that.step.fields.field.splice(newIndex, 0, upDate);
          this.selectedPage();
        } else {
          this.$message.error('已经是第一条，不可上移');
        }
      },

      //下移
      moveDown(index, row) {
        var that = this;
        if ((index + 1) === that.showFields.length) {
          this.$message.error('已经是最后一条，不可下移');
        } else {
          let newIndex = (this.curPage - 1) * this.pageSize + index
          let downDate = that.step.fields.field[newIndex + 1];
          that.step.fields.field.splice(newIndex + 1, 1);
          that.step.fields.field.splice(newIndex, 0, downDate);
          this.selectedPage();

        }
      },
      pushGetFields() {
        if (this.step.fields.field.length > 0) {
          this.confirmDialog = true;
        } else {
          this.getAllFields();
        }
      },
      getAllFields() {
        this.lastStepFields.forEach((item, index) => {
          this.step.fields.field.push({
            name: item.name,
            type: item.type,
            format: item.format,
            currency: item.currency,
            decimal: item.decimal,
            group: item.group,
            trim_type: item.trim_type,
            nullif: item.nullif,
            length: item.length,
            precision: item.precision
          });
        });
        this.selectedPage();
      },
      selectFields(row) {
        let obj = {};
        obj = this.lastStepFields.find((item) => { //这里的selectList就是上面遍历的数据源
          return item.name === row.name; //筛选出匹配数据
        });
        if (obj !== undefined && obj !== null) {
          row.type = obj.type;
          row.length = obj.length;
          row.precision = obj.precision;
        }
      },
      closeDialog() {

        util.$emit('closeDialog', 'close');
      },
      confirmDialogOk() {
        this.step.fields.field = [];
        this.getAllFields();
        this.confirmDialog = false;
      },
      confirmDialogCancel() {
        this.confirmDialog = false;
      },
      handleClose() {
        this.confirmDialog = false;
      },
      submit(step) {

        this.$refs[step].validate((valid) => {
          if (valid) {
            this.step.file.name = this.step.file.filePath+'/'+this.step.file_name;;
            this.step['oldStepName'] = this.oldStepName;
            this.step.initFlag = true;

            //校验是否有重复名称并获取重命名之后的名称
            let paramName = this.step.name;
            let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
            let flag = false;
            if (this.step.name !== newName) {
              flag = true;
              this.step.name = newName;
            }
            let param = {
              key: this.key,
              value: {
                oldStepName: this.oldStepName,
                step: this.step
              }
            }
            // 修改krt节点信息
            store.dispatch('updateStepInfo', param);
            let params = {
              id: this.nodeData.id, //插件id
              label: this.step.name,
              oldStepName: this.oldStepName
            }
            util.$emit('updateNode', params);
            util.$emit('closeDialog', 'close');

            if (flag) {
              this.$alert(paramName + ' 名称已存在,重命名为 ' + newName, '注释', {
                confirmButtonText: '确定',
              });
            } else {
              this.$message({
                message: '步骤信息修改成功',
                type: 'success'
              })
            }
          } else {
            return false;
          }
        });
      },
      init() {
        this.created_user = store.getters.getUserId;

        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName
        };
        let laststeps = store.getters.getLastStep(param);

        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
        }

        let obj = store.getters.getCurrentStep(param)
        let curstep = {};
        Object.assign(curstep, obj);
        this.step.describes = curstep.describes;

        if (this.laststep !== undefined) {
          this.lastStepFields = this.laststep.fields.field;

        }


        if (typeof curstep.file == 'undefined' || typeof curstep.file.name == 'undefined') {
        } else {
          this.step.file.name = curstep.file.name;
          curstep.fields.field.forEach((item, index) => {
            item.edit = false;
            this.step.fields.field.push(item);
          });
          this.step.fileName = curstep.fileName == undefined?'':curstep.fieldName;
          this.step.baseDir = curstep.baseDir=== undefined?'':curstep.baseDir;
          this.step.file = curstep.file === undefined?'':curstep.file;
          this.step.append = curstep.append;
          this.step.separator = curstep.separator;
          this.step.enclosure_forced = curstep.enclosure_forced;
          this.step.enclosure = curstep.enclosure;
          this.step.encoding = curstep.encoding;
          this.step.compression = curstep.compression;
          this.step.format = curstep.format;
        }
        this.step.file_name = this.step.file.name.replace(this.step.file.filePath, '').replace(this.created_user + "/", '');
        this.selectedPage();

      },
      changeFile() {
        this.queryParams = {
          fileType: '',
          isFolder: '1',
        }

        this.selectFileVisiable = true;
      }
    },
    components: {
      fileData,
    },

  }
</script>

<style>

</style>
