<!--
 * @Author: your name
 * @Date: 2020-07-14 14:25:50
-->
<template>
  <div class="plugin_body">
    <div  class="plugin_content">
      <el-form :model="step" label-width="30%" size="mini" class="div_form" :rules="rules">
        <el-form-item label="步骤名称" prop="name" >
          <el-input placeholder="请输入该步骤的名称" v-model="step.name" clearable style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <!-- 页体 -->
      <el-tabs v-model="activeName">
        <!-- 基础属性界面 -->
        <el-tab-pane label="基本属性" name="TFOPBasic">
          <el-form :model="step" ref="step" label-width="160px" size="mini">
           <el-form-item label="选择文件:" prop="showFileName">
              <el-autocomplete class="inline-input" v-model="step.showFileName" :fetch-suggestions="querySearch"
                              @change="changeSelect"
                              placeholder="请输入文件名称搜索文件" :trigger-on-focus="false" @select="handleSelect"
                              @blur="blurInput"
                              style="width: 100%">
                <el-button slot="append" size='mini' type="primary" @click="changeFile">本地文件</el-button>
              </el-autocomplete>
            </el-form-item>
            <el-form-item >
              <el-checkbox v-model="step.file.servlet_output" :disabled="dis">传递输出到servlet</el-checkbox>
              <el-checkbox v-model="step.file.create_parent_folder" :disabled="dis">创建父目录</el-checkbox>
               <el-checkbox v-model="step.file.do_not_open_new_file_init" :disabled="dis">启动时不创建文件名</el-checkbox>
            </el-form-item>
            <el-form-item >
              <el-checkbox v-model="step.file.fileNameInField" :disabled="dis">从字段中获取文件名</el-checkbox>
            </el-form-item>
            <el-form-item label="文件名字段">
              <el-input
                v-model="step.file.fileNameField"
                clearable
                :disabled="dis"
                class="input-with-select">
                <el-select v-model="step.file.select_fileNameField " slot="prepend" placeholder="请选择"></el-select>
              </el-input>
            </el-form-item>
            <el-form-item label="扩展名">
              <el-input v-model="step.file.extention" cleareable ></el-input>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.file.step_num" :disabled="dis">文件名里包含步骤数</el-checkbox>
              <el-checkbox v-model="step.file.split" :disabled="dis">文件名里包含数据分区号</el-checkbox>
            </el-form-item>
            <el-form-item >
              <el-checkbox v-model="step.file.add_date" :disabled="dis">文件名里包含日期</el-checkbox>
              <el-checkbox v-model="step.file.add_time" :disabled="dis">文件名里包含时间</el-checkbox>
              <el-checkbox v-model="step.file.SpecifyFormat" :disabled="dis">指定日期时间格式</el-checkbox>
            </el-form-item>
            <el-form-item label="日期格式">
              <el-input
                v-model="step.file.date_time_format"
                clearable
                :disabled="dis"
                class="input-with-select"

              >
                <el-select  v-model="step.file.select_dt_format" slot="prepend" placeholder="请选择"></el-select>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search">显示文件名</el-button>
            </el-form-item>
            <el-form-item>
              <el-checkbox v-model="step.file.add_to_result_filenames">结果中添加文件名</el-checkbox>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <!-- 内容界面 -->
        <el-tab-pane label="内容" name="step">

          <el-form :model="step" ref="step" label-width="180px" size="mini">
            <el-form-item label="分隔符">
              <el-input v-model="step.separator" ></el-input>
              <el-button type="primary">插入Tab</el-button>
            </el-form-item>
            <el-form-item label="封闭符">
              <el-input v-model="step.enclosure" ></el-input>
            </el-form-item>
            <el-form-item >
              <el-checkbox v-model="step.header">头部</el-checkbox>
              <el-checkbox v-model="step.footer">尾部</el-checkbox>
              <el-checkbox v-model="step.append_mode">追加方式</el-checkbox>
            </el-form-item>
            <el-form-item label="格式">
              <el-input v-model="step.format" clearable class="input-with-select" >
                <el-select v-model="step.select_format" slot="prepend">
                  <el-option label="CR+LF终止（Windows、DOS）" value="end1"></el-option>
                  <el-option label="LF终止（Unix）" value="end2"></el-option>
                  <el-option label="没有新的线路终端" value="end3"></el-option>
                </el-select>
              </el-input>
            </el-form-item>
            <el-form-item label="压缩">
              <el-input  v-model="step.compression" clearable >
                <el-select v-model="step.select_compression" slot="prepend">
                  <el-option label="None" value="none"></el-option>
                  <el-option label="Gzip" value="gzip"></el-option>
                  <el-option label="Zip" value="zip"></el-option>
                  <el-option label="Snappy" value="snappy"></el-option>
                  <el-option label="Hadoop-Snappy" value="hadoop-snappy"></el-option>
                </el-select>
              </el-input>
            </el-form-item>
            <el-form-item label="编码">
              <el-input v-model="step.encoding">
                <el-select v-model="step.select_encoding" slot="prepend" >
                  <el-option v-for="item in encodings" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-input>
            </el-form-item>
            <el-form-item >
              <el-checkbox v-model="step.enclosure_forced">在字段周围强制加封闭符</el-checkbox>
              <el-checkbox v-model="step.enclosure_fix_disabled">禁用封闭符修复</el-checkbox>
            </el-form-item>
            <el-form-item >
              <el-checkbox v-model="step.field_right_fill">字段右填充或裁剪</el-checkbox>
              <el-checkbox v-model="step.fast_data_storage" style="margin-left: 40px;">快速数据存储（无格式）</el-checkbox>
            </el-form-item>
            <el-form-item label="分拆....每一行">
              <el-input v-model="step.split_each_row" clearable ></el-input>
            </el-form-item>
            <el-form-item label="添加文件结束行">
              <el-input v-model="step.add_file_endline" clearable ></el-input>
            </el-form-item>
          </el-form>

        </el-tab-pane>
        <!-- 字段界面 -->
        <el-tab-pane label="字段" name="TFOPFiled">

          <el-form :model="step" ref="TFOPFiled" size="mini" prop="fields" >
            <el-form-item>
              <el-table :data="step.fields.filed" border height="400px" :header-cell-style="{background: '#eef1f6'}">
                <el-table-column prop="fileName" label="字段名称"></el-table-column>
                <el-table-column prop="fileType" label="类型"></el-table-column>
                <el-table-column prop="fileLength" label="长度"></el-table-column>
                <el-table-column prop="fileRepeat" label="重复"></el-table-column>
                <el-table-column prop="flieCurrency" label="货币符号"></el-table-column>
                <el-table-column prop="fileFormat" label="格式"></el-table-column>
                <el-table-column prop="fileDecimal" label="小数"></el-table-column>
                <el-table-column prop="fileGroup" label="分组"></el-table-column>
              </el-table>
              <div style="margin-top: 20px; text-align: center;">
                <el-button type="primary" @click="GetSheetName()">获取字段</el-button>
                <el-button type="primary">最小宽度</el-button>
              </div>
            </el-form-item>
          </el-form>

        </el-tab-pane>
      </el-tabs>
    </div>
    <div
     class="dialog-footer plugin_footer"
    >
      <span>
        <el-button @click="closeDialog" size="mini">取 消</el-button>
        <el-button @click="submit('step')" size="mini">确 定</el-button>
      </span>
    </div>
     <el-dialog title="本地文件上传" :visible.sync="selectFileVisiable" class="dialog_temp other_dialog"
               :modal-append-to-body='false'
               :append-to-body="true" width="50%" :before-close="closeAdd">
      <!-- 子组件 -->
      <fileData :params="queryParams" :selec-data="selectData" :fileType="fileType" createChannel="UPLOAD"
                fileFolder="0" ref="fileData"></fileData>
    </el-dialog>
  </div>
</template>
<script>
import util from "../../../../common/utils";
import store from "../../../../vuex/store";
import codeMirror from "../../../common/codemirror.vue";
import vPinyin from "../../../../py/vue-py.js";
import sqlFormatter from "sql-formatter";
import fileData from '../../../common/UploadFile.vue'
import { getSchema, getTables, getColumns } from "../../../../api/api.js";
export default {
  props: {
      selectData: Object,
    },
    components: {
      fileData,
    },
  data() {
    return {
      dis: false,
      fileData: null,
      selectFileVisiable: false,
      queryParams: {
          fileType: '',
        },
      fileType: 'xls,xlsx',
      encodings: [
        "Big5",
        "Big5-HKSCS",
        "CESU-8",
        "EUC-JP",
        "EUC-KR",
        "GB18030",
        "GB2312",
        "GBK",
        "IBM-Thai",
        "IBM00858",
        "IBM01140",
        "IBM01141",
        "IBM01142",
        "IBM01143",
        "IBM01144",
        "IBM01145",
        "IBM01146",
        "IBM01147",
        "IBM01148",
        "IBM01149",
        "IBM037",
        "IBM1026",
        "IBM1047",
        "IBM273",
        "IBM277",
        "IBM278",
        "IBM280",
        "IBM284",
        "IBM285",
        "IBM290",
        "IBM297",
        "IBM420",
        "IBM424",
        "IBM437",
        "IBM500",
        "IBM775",
        "IBM850",
        "IBM852",
        "IBM855",
        "IBM857",
        "IBM860",
        "IBM861",
        "IBM862",
        "IBM863",
        "IBM864",
        "IBM865",
        "IBM866",
        "IBM868",
        "IBM869",
        "IBM870",
        "IBM871",
        "IBM918",
        "ISO-2022-CN",
        "ISO-2022-JP",
        "ISO-2022-JP-2",
        "ISO-2022-KR",
        "ISO-8859-1",
        "ISO-8859-13",
        "ISO-8859-15",
        "ISO-8859-2",
        "ISO-8859-3",
        "ISO-8859-4",
        "ISO-8859-5",
        "ISO-8859-6",
        "ISO-8859-7",
        "ISO-8859-8",
        "ISO-8859-9",
        "JIS_X0201",
        "JIS_X0212-1990",
        "KOI8-R",
        "KOI8-U",
        "Shift_JIS",
        "TIS-620",
        "US-ASCII",
        "UTF-16",
        "UTF-16BE",
        "UTF-16LE",
        "UTF-32",
        "UTF-32BE",
        "UTF-32LE",
        "UTF-8",
        "windows-1250",
        "windows-1251",
        "windows-1252",
        "windows-1253",
        "windows-1254",
        "windows-1255",
        "windows-1256",
        "windows-1257",
        "windows-1258",
        "windows-31j",
        "x-Big5-HKSCS-2001",
        "x-Big5-Solaris",
        "x-COMPOUND_TEXT",
        "x-euc-jp-linux",
        "x-EUC-TW",
        "x-eucJP-Open",
        "x-IBM1006",
        "x-IBM1025",
        "x-IBM1046",
        "x-IBM1097",
        "x-IBM1098",
        "x-IBM1112",
        "x-IBM1122",
        "x-IBM1123",
        "x-IBM1124",
        "x-IBM1166",
        "x-IBM1364",
        "x-IBM1381",
        "x-IBM1383",
        "x-IBM300",
        "x-IBM33722",
        "x-IBM737",
        "x-IBM833",
        "x-IBM834",
        "x-IBM856",
        "x-IBM874",
        "x-IBM875",
        "x-IBM921",
        "x-IBM922",
        "x-IBM930",
        "x-IBM933",
        "x-IBM935",
        "x-IBM937",
        "x-IBM939",
        "x-IBM942",
        "x-IBM942C",
        "x-IBM943",
        "x-IBM943C",
        "x-IBM948",
        "x-IBM949",
        "x-IBM949C",
        "x-IBM950",
        "x-IBM964",
        "x-IBM970",
        "x-ISCII91",
        "x-ISO-2022-CN-CNS",
        "x-ISO-2022-CN-GB",
        "x-iso-8859-11",
        "x-JIS0208",
        "x-JISAutoDetect",
        "x-Johab",
        "x-MacArabic",
        "x-MacCentralEurope",
        "x-MacCroatian",
        "x-MacCyrillic",
        "x-MacDingbat",
        "x-MacGreek",
        "x-MacHebrew",
        "x-MacIceland",
        "x-MacRoman",
        "x-MacRomania",
        "x-MacSymbol",
        "x-MacThai",
        "x-MacTurkish",
        "x-MacUkraine",
        "x-MS932_0213",
        "x-MS950-HKSCS",
        "x-MS950-HKSCS-XP",
        "x-mswin-936",
        "x-PCK",
        "x-SJIS_0213",
        "x-UTF-16LE-BOM",
        "X-UTF-32BE-BOM",
        "X-UTF-32LE-BOM",
        "x-windows-50220",
        "x-windows-50221",
        "x-windows-874",
        "x-windows-949",
        "x-windows-950",
        "x-windows-iso2022jp"
      ],
      activeName: "TFOPBasic",
      dis: true,
     step: {
       showFileName: "",
        name: "CSV文件输出",
        type: "TextFileOutput",
        stepType: "CSV文件输出",
        modelName: "",
        file_name: null,
        describe: "",
        distribute: false,
        custom_distribution: "",
        copies: 1,
        partitioning: {
          method: "none",
          schema_name: ""
        },
        separator: ",",
        enclosure: '"',
        enclosure_forced: false,
        enclosure_fix_disabled: false,
        header: false,
        footer: false,
        format: "UNIX",
        compression: "None",
        encoding: "UTF-8",
        endedLine: "",
        field_right_fill: "",
        fast_data_storage: "",
        split_each_row: "",
        add_file_endline: "",
        append_mode: "",
        describes: "",
        select_format: "",
        select_encoding: "",
        select_compression: "",
        file: {
          step_num: "",
          fileNameInField: false,
          fileNameField: "",
          create_parent_folder: false,
          filePath: "",
          name: "file",
          servlet_output: false,
          do_not_open_new_file_init: false,
          extention: "csv",
          append: false,
          split: false,
          haspartno: false,
          add_date: false,
          add_time: false,
          SpecifyFormat: false,
          date_time_format: "yyyy-MM-dd HH:mm:ss",
          add_to_result_filenames: false,
          pad: false,
          fast_dump: false,
          splitevery: "",
          select_dt_format: "",
          select_fileNameField: "",
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
      },
      rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
          }],
          showFileName: [{
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }],
				},
    };
  },
  methods: {
    editLine(row){
      this.step.fields.field.forEach((item, index) => {
        if (item.name === row.name) {
          // item.decimal='.'
          this.$set(this.step.fields.field[index], 'edit', true);
          this.$forceUpdate();
        }
      })
    },
     closeDialog() {
      util.$emit("closeDialog", "close");
      // 初始化画布数据
    },
    submit() {
        this.step.initFlag = true;
        console.info("最终字段", this.step.fields);
        let step = Object.assign({}, this.step);
        if (this.step.header) {
          step.header = 'Y'
        } else {
          step.header = 'N'
        }
        if (this.step.noempty) {
          step.noempty = 'Y';
        } else {
          step.noempty = 'N';
        }
        if (this.step.stoponempty) {
          step.stoponempty = 'Y';
        } else {
          step.stoponempty = 'N';
        }
        step['oldStepName'] = this.oldStepName;
        // 控件重命名
        let paramName = step.TextName;
        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
        let flag = false;
        if (step.TextName !== newName) {
          flag = true;
          step.TextName = newName;
        }
        // 修改步骤xinxi
        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: step
          }
        }

        console.info("最终修改的步骤名称", step);
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
     changeFile() {
        this.queryParams = {
          fileType: 'xls,xlsx',
          isFolder: '0',
        }
        this.selectFileVisiable = true;
      },
          changeSelect() {
        this.step.sheets.sheet.name = '';
        this.sheets = [];
        this.showFields = [];
      },
      blurInput() {
        let flag = true;
        this.restaurants.forEach((item, index) => {
          if (item.fileName === this.step.showFileName) {
            flag = false;
          }
        })
        if (flag) {
          this.step.showFileName = '';
        }
      },
        querySearch(queryString, cb) {
        var restaurants = this.restaurants;
        var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
        // 调用 callback 返回建议列表的数据
        console.info(results);
        cb(results);
      },
      handleSelect(item) {
        this.showFields = [];
        this.step.showFileName = item.fileName;
        this.fileId = item.fileId;
        this.step.file.name = item.fileName;
        this.step.fileId = item.fileId;
        this.step.filename = item.fileName;
        this.step.filePath = item.fileRelativPath;
        this.step.fsuserName = item.userName;
        this.step.fspassWord = item.password;
        this.step.sheets.sheet.name = '';
        this.selectFile();
      },
      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          this.step.file.name = this.$refs.fileData.selecData.fileName;
          this.step.fileId = this.$refs.fileData.selecData.fileId;
          this.step.filename = this.$refs.fileData.selecData.fileName;
          this.step.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.fsuserName = this.$refs.fileData.selecData.userName;
          this.step.fspassWord = this.$refs.fileData.selecData.password;
        }
        this.selectFile();
        this.selectFileVisiable = false;
      },
      getFileByFileType(fileId) {
        let queryParams = {
          fileType: 'xls,xlsx',
          isFolder: '0',
        };
        let fileList = [];
        selectFileListByType(queryParams).then(res => {

          if (res.data.code === '10000') {
            console.info("获取文件成功");
            if (fileId !== undefined) {
              res.data.content.forEach((item, index) => {
                fileList.push({
                  value: item.fileName,
                  fileName: item.fileName,
                  fileId: item.fileId,
                  fileRelativPath: item.fileRelativPath,
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
          ;
        })
      },
      changeFile() {
        this.queryParams = {
          fileType: 'xls,xlsx',
          isFolder: '0',
        }
        this.selectFileVisiable = true;
      },
        getFileData(fileData) {
        this.selectFileVisiable = false;
        this.step.showFileName = fileData.fileName;
        let fileId = fileData.fileId;
        this.getFileByFileType(fileId);
      },
      batchUploadBasicModel(file) {
        this.fileName = file.name;
        this.fileData = file.raw;
      },
  },
};
</script>
<style>
.el-input{
  width: 80%;
}
.el-form-item--mini.el-form-item {
  margin-bottom: 10px;
}
.bodyborder{
   height: 360px;
   margin-top: 0px;
  overflow: auto;
}
</style>
