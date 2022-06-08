<template>
  <div class="plugin_body">
    <div class="plugin_content">
      <el-form ref="form" :model="step" :rules="rules" size="mini"  label-position="right"
               label-width="30%">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" size="mini" style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <el-tabs @tab-click="handleClick">
        <el-tab-pane label="文件">
          <el-form :ref="step.file" :model="step.file" size="mini"  label-position="right" label-width="30%">
            <el-form-item label="输出目录" :rules="{
                required: true, message: '请选择文件夹', trigger: 'change,blur'}">
              <el-select v-model="step.folderName" size="mini" placeholder="请选择文件夹" style="width: 65%" @change="getFileList">
                <el-option v-for="item in dirs" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
              </el-select>
              <el-button size="mini" type="primary" @click="changeFile">新建</el-button>
            </el-form-item>
            <el-form-item label="文件名" prop="showFileName" :rules="{
                required: true, message: '文件不能为空', trigger: 'change,blur'}">
              <el-select v-model="step.file.showFileName"  size="mini" placeholder="请选择文件" style="width: 65%"
                         :disabled="step.folderName===''||step.folderName===undefined" :allow-create="true" filterable>
                <el-option v-for="item in files" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="创建父目录">
              <el-checkbox  v-model="step.file.create_parent_folder"></el-checkbox>
            </el-form-item>
            <el-form-item label="启动时不创建文件">
              <el-checkbox label="" v-model="step.file.do_not_open_newfile_init"></el-checkbox>
            </el-form-item>
            <el-form-item label="扩展名" prop="extention">
              <el-input v-model="step.file.extention" style="width: 65%;" size="mini"></el-input>
            </el-form-item>
            <el-form-item label="在文件名里包含步骤数?">
              <el-checkbox  v-model="step.file.split"></el-checkbox>
            </el-form-item>
            <el-form-item label="在文件名里包含日期?">
              <el-checkbox  v-model="step.file.add_data" :disabled="add_data"></el-checkbox>
            </el-form-item>
            <el-form-item label="在文件名里包含时间?">
              <el-checkbox  v-model="step.file.add_time" :disabled="add_time"></el-checkbox>
            </el-form-item>
            <el-form-item label="指定时间格式" prop="SpecifyFormat">
              <el-checkbox @change="changeformat"  v-model="step.file.SpecifyFormat"></el-checkbox>
            </el-form-item>
            <el-form-item label="时间格式" prop="date_time_format">
              <el-select v-model="step.file.date_time_format" style="width: 65%;" clearable :disabled="format" size="mini"
                         filterable>
                <el-option v-for="item in date_time_format" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-dialog title="输出文件" :visible.sync="lookvalue" :modal-append-to-body="false">
              <div>
                <div>
                  过滤：
                  <el-input style="width: 60%; margin-bottom: 10px;" size="mini"></el-input>
                </div>
                <div>
                  输出文件：
                  <el-input style="width: 65%;" size="mini"></el-input>
                </div>
                <div slot="footer" class="dialog-footer" align="right">
                  <el-button @click="lookvalue = false" size="mini">关 闭</el-button>
                </div>
              </div>
            </el-dialog>
            <el-form-item label="结果中添加文件名" prop="add_to_result_filenames">
              <el-checkbox  v-model="step.add_to_result_filenames"></el-checkbox>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="内容">
          <div>
            <div>
              <el-form :ref="step" :model="step" size="mini" label-width="30%" label-position="right">
                <el-form-item label="追加" >
                  <el-checkbox v-model="step.append"></el-checkbox>

                </el-form-item>
                <el-form-item label="头">
                  <el-checkbox label="头" v-model="step.header"></el-checkbox>
                </el-form-item>
                <el-form-item label="脚">
                  <el-checkbox  v-model="step.footer"></el-checkbox>
                </el-form-item>
                <el-form-item label="编码" prop="encoding">
                  <el-select v-model="step.encoding" style="width: 65%;" size="mini" clearable>
                    <el-option v-for="item in encodings" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="拆分...每一行" prop="splitevery">
                  <el-input v-model="step.splitevery"  style="width: 65%" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="工作表名称" prop="sheetname">
                  <el-input v-model="step.sheetname"  style="width: 65%" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="保护工作表?">
                  <el-checkbox v-model="step.protect_sheet" @change="changetable"></el-checkbox>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                  <el-input v-model="step.password" size="mini" show-password style="width: 65%" :disabled="worktable"></el-input>
                </el-form-item>
                <el-form-item label="自动调整列大小">
                  <el-checkbox  v-model="step.autosizecolums"></el-checkbox>

                </el-form-item>
                <el-form-item label="保留null值">
                  <el-checkbox  v-model="step.nullisblank"></el-checkbox>
                </el-form-item>
                <el-form-item label="使用临时文件">
                  <el-checkbox  v-model="step.temporary" @change="changetem"></el-checkbox>
                </el-form-item>
                <el-form-item label="使用临时文件" prop="usetempfiles">
                  <el-input v-model="step.usetempfiles" :disabled="use" size="mini" style="width: 55%"
                            :readonly="true"></el-input>
                  <el-button size="mini" type="primary" @click="changeFile" :disabled="use">选择</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div>
              <el-form ref="step.template" :model="step.template" size="mini" label-width="30%">
                <el-form-item label="使用模板">
                  <el-checkbox v-model="step.template.enabled" @change="changemodel"></el-checkbox>
                </el-form-item>
                <el-form-item label="选择">
                  <el-input v-model="step.template.filename"  style="width: 55%" :disabled="exmodel" size="mini"
                            :readonly="true"></el-input>
                  <el-button size="mini" type="primary" @click="changeFile" :disabled="exmodel">选择</el-button>
                </el-form-item>
                <el-form-item label="追加Excel模板">
                  <el-checkbox v-model="step.template.append" :disabled="zadd"></el-checkbox>
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="格式">
          <div class="node_title_basic">
            <span>
              表头字体
            </span>
          </div>
          <el-card class="box-card">
            <el-form ref="step.custom" :model="step.custom" size="mini" label-width="30%" label-position="right">
              <el-form-item label="表头字体" prop="header_font_name">
                <el-select v-model="step.custom.header_font_name" size="mini" style="width: 65%" clearable>
                  <el-option v-for="item in typeface" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表头字体大小" prop="header_font_size" size="mini">
                <el-input v-model="step.custom.header_font_size" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="表头字体加粗?">
                <el-checkbox v-model="step.custom.header_font_bold" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="表头字体倾斜?">
                <el-checkbox  v-model="step.custom.header_font_italic" size="mini"></el-checkbox>
              </el-form-item>
              <el-form-item label="表头字体下划线" prop="header_font_underline">
                <el-select v-model="step.custom.header_font_underline" size="mini" style="width: 65%;" clearable>
                  <el-option v-for="item in underline" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表头字体方向" prop="header_font_orientation">
                <el-select v-model="step.custom.header_font_orientation" size="mini" style="width: 65%;" clearable>
                  <el-option v-for="item in direction" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表头字体颜色" prop="header_font_color">
                <el-select v-model="step.custom.header_font_color" size="mini" style="width: 65%;" clearable>
                  <el-option v-for="item in color" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表头背景颜色" prop="header_background_color">
                <el-select v-model="step.custom.header_background_color" size="mini" style="width: 65%;" clearable>
                  <el-option v-for="item in bgcolor" :key="item.key" :label="item.label" :value="item.label"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表头对齐方式" prop="header_alignment">
                <el-select v-model="step.custom.header_alignment" size="mini" style="width: 65%;" clearable>
                  <el-option v-for="item in alignment" :key="item.key" :label="item.label" :value="item.key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表头高度" prop="header_row_height">
                <el-input v-model="step.custom.header_row_height" size="mini" style="width: 65%;"></el-input>
              </el-form-item>
              <el-form-item label="添加图片">
                <el-input style="width: 55%;" type="file"></el-input>
                <el-upload style="display: inline-block;" :auto-upload="false" :show-file-list="false"
                           class="upload-demo upload-column"
                           action="" :on-change="batchUploadBasicModel" webkitdirectory :multiple="false" :file-list="fileList"
                           :accept="acceptType">
                  <el-button size="mini" type="primary">浏览</el-button>
                </el-upload>
              </el-form-item>
            </el-form>
          </el-card>
          <div class="datacard">
            <div class="node_title_basic">
            <span>
              表数据字体
            </span>
            </div>
            <el-card class="box-card">
              <el-form ref="step.custom" :model="step.custom" size="mini" label-width="30%">
                <el-form-item label="数据字体" prop="row_font_name">
                  <el-select v-model="step.custom.row_font_name" size="mini" style="width: 65%;" clearable>
                    <el-option v-for="item in datatype" :key="item.key" :label="item.label" :value="item.key"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="数据字体大小" prop="row_font_size">
                  <el-input v-model="step.custom.row_font_size" size="mini" style="width: 65%;"></el-input>
                </el-form-item>
                <el-form-item label="数据字体颜色" prop="row_font_color">
                  <el-select v-model="step.custom.row_font_color" size="mini" style="width: 65%;" clearable>
                    <el-option v-for="item in datacolor" :key="item.key" :label="item.label" :value="item.key"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="数据背景颜色" prop="row_background_color">
                  <el-select v-model="step.custom.row_background_color" size="mini" style="width: 65%;" clearable>
                    <el-option v-for="item in databgcolor" :key="item.key" :label="item.label" :value="item.key"></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane label="字段">
          <div class="node_title_basic">
            <span>字段</span>
            <span style="float: right">
              <el-button type="primary" @click="getFieldInfo" size="mini">获取字段</el-button>
            </span>
          </div>
          <el-table :data="showFields" border height="330" :header-cell-style="{background:'#eef1f6'}">
            <el-table-column type="index" width="50">
            </el-table-column>
            <el-table-column prop="tablename" label="名称">
              <template slot-scope="scope">
                <el-input v-model="scope.row.name" size="mini" v-if="scope.row.edit === true"></el-input>
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
            <el-table-column prop="format" label="格式">
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
    <el-dialog title="新增文件夹" :visible.sync="selectFileVisiable" v-if="selectFileVisiable" class="dialog_temp other_dialog"
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
        dirs:[],
        files:[],
        showFields:[],
        curPage:1,
        pageSize:10,
        acceptType:'image/*',
        queryParams: {
          fileType: '',
        },
        fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"], //字段类型
        formats: [], //字段格式
        fileType: 'xls,xlsx',
        selectFileVisiable: false,
        key: '', //插件对应的画布
        fileList: [],
        dialogVisible: false,
        get: false,
        mapping: false,
        getremove: false,
        getchange: false,
        lookvalue: false,
        getziduan: false,
        mixwidth: false,
        add_time: false,
        add_data: false,
        format: true,
        worktable: true,
        use: true,
        exmodel: true,
        zadd: true,
        folderName:'',
        step: {
          filePath:'',
          file_server_type:'',
          ftp_username: '',
          ftp_password: '',
          folderName: '',
          name: "Excel输出",
          type: "ExcelOutput2",
          distribute: "Y",
          copies: "1",
          partitioning: {
            method: "none"
          },
          header: true,
          footer: false,
          append: false,
          add_to_result_filenames: true,
          file: {
            showFileName:'',
            name: "",
            extention: "xls",
            do_not_open_newfile_init: false,
            create_parent_folder: false,
            split: false,
            add_date: false,
            add_time: false,
            SpecifyFormat: false,
            sheetname: "Sheet1",
            autosizecolums: false,
            nullisblank: false,
            protect_sheet: false,
            password: "",
            splitevery: "0",
            usetempfiles: false
          },
          template: {
            enabled: false,
            append: false,
            filename: "template.xls"
          },
          fields: {
            field: []
          },
          custom: {
            header_font_name: "arial",
            header_font_size: "10",
            header_font_bold: false,
            header_font_italic: false,
            header_font_underline: "no",
            header_font_orientation: "horizontal",
            header_font_color: "black",
            header_background_color: "none",
            header_row_height: "255",
            header_alignment: "left",
            row_font_name: "arial",
            row_font_size: "10",
            row_font_color: "black",
            row_background_color: "none"
          },
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "560",
            yloc: "400",
            draw: "Y"
          }
        },
        rules: {
          name: [{
            required: true,
            message: '步骤名称不能为空',
            trigger: 'blur'
          }],
          folderName:[{
            required:true,
            message:'请选择文件夹',
            trigger:'change'
          }]
        },
        formatDate: [],
        formatNumber: [],
        encodings: [],
        valuelist: [],
        date_time_format: ["m/d/yy", "d-mmm-yy", "d-mmm", "mmm-yy", "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss",
          "m/d/yy h:mm", "mm:ss", "[h]:mm:ss", "mm:ss.0", "yyyy-mm-dd hh:mm:ss", "yyyy/mm/dd", "yyyy-MM-dd",
          "yyyy/mm/dd hh/mm/ss"
        ],
        typeface: [{key:'arial',label:"Arial"}, {key:'courier',label:"Courier"}, {key:'tahoma',label:"Tahoma"}, {key:'times',label:"Times"}],
        underline: [{key:'no',label:"No underline"}, {key:'single',label:"Single"}, {key:'single_accounting',label:"Single accounting"},
          {key:'double',label:"Double"}, {key:'double_accounting',label:"Double accounting"}],
        direction: [{key:'horizontal',label:"Horizontal"}, {key:'minus_45',label:"Minus 45"}, {key:'minus_90',label:"Minus 90"}, {key:'plus_45',label:"Plus 45"},
          {key:'plus_90',label:"Plus 90"}, {key:'stacked',label:"Stacked"}, {key:'vertical',label:"Vertical"}],
        color: [{key:'black',label:"BLACK"}, {key:'white',label:"WHITE"},{key:'red',label:"RED"},
          {key:'bright_green',label:"BRIGHT_GREEN"}, {key:'blue',label:"BLUE"}, {key:'yellow',label:"YELLOW"}, {key:'pink',label:"PINK"},
          {key:'turquoise',label:"TURQUOISE"}, {key:'dark_red',label:"DARK_RED"}, {key:'green',label:"GREEN"},
          {key:'dark_blue',label:"DARK_BLUE"}, {key:'dark_yellow',label:"DARK_YELLOW"}, {key:'violet',label:"VIOLET"}, {key:'teal',label:"TEAL"}
        ],
        bgcolor: [{key:'node',label:"None"}, {key:'white',label:"WHITE"},{key:'red',label:"RED"},
          {key:'bright_green',label:"BRIGHT_GREEN"}, {key:'blue',label:"BLUE"}, {key:'yellow',label:"YELLOW"}, {key:'pink',label:"PINK"},
          {key:'turquoise',label:"TURQUOISE"}, {key:'dark_red',label:"DARK_RED"}, {key:'green',label:"GREEN"},
          {key:'dark_blue',label:"DARK_BLUE"}, {key:'dark_yellow',label:"DARK_YELLOW"}, {key:'violet',label:"VIOLET"}, {key:'teal',label:"TEAL"}
        ],
        alignment: [{key:'left',label:"Left"}, {key:'right',label:"Right"}, {key:'center',label:"Center"}, {key:'fill',label:"Fill"},
          {key:'general',label:"General"}, {key:'justify',label:"Justify"}],
        datatype: [{key:'arial',label:"Arial"}, {key:'courier',label:"Courier"}, {key:'tahoma',label:"Tahoma"}, {key:'times',label:"Times"}],
        datacolor: [{key:'black',label:"BLACK"}, {key:'white',label:"WHITE"},{key:'red',label:"RED"},
          {key:'bright_green',label:"BRIGHT_GREEN"}, {key:'blue',label:"BLUE"}, {key:'yellow',label:"YELLOW"}, {key:'pink',label:"PINK"},
          {key:'turquoise',label:"TURQUOISE"}, {key:'dark_red',label:"DARK_RED"}, {key:'green',label:"GREEN"},
          {key:'dark_blue',label:"DARK_BLUE"}, {key:'dark_yellow',label:"DARK_YELLOW"}, {key:'violet',label:"VIOLET"}, {key:'teal',label:"TEAL"}
        ],
        databgcolor: [{key:'node',label:"None"}, {key:'white',label:"WHITE"},{key:'red',label:"RED"},
          {key:'bright_green',label:"BRIGHT_GREEN"}, {key:'blue',label:"BLUE"}, {key:'yellow',label:"YELLOW"}, {key:'pink',label:"PINK"},
          {key:'turquoise',label:"TURQUOISE"}, {key:'dark_red',label:"DARK_RED"}, {key:'green',label:"GREEN"},
          {key:'dark_blue',label:"DARK_BLUE"}, {key:'dark_yellow',label:"DARK_YELLOW"}, {key:'violet',label:"VIOLET"}, {key:'teal',label:"TEAL"}
        ],
      }
    },
    mounted() {
      this.init();
      // this.getFileByFileType();
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

      //获取字段
      getFieldInfo(){
        this.step.fields.field=[];
        this.lastStepField.forEach(item=>{
          this.step.fields.field.push({
            edit:false,
            name: item.name,
            type: item.type
          })
        })
        this.selectedPage();
      },
      selectedPage() {
        this.showFields = [];
        let fields = [];
        fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          this.showFields.push(item);
        });

      },

      batchUploadBasicModel(file){
        let reader = new FileReader();
        reader.readAsDataURL(file.raw);
        reader.onload=()=>{
        }
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
                  fileServerType:item.fileServerType,
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
                  fileServerType:item.fileServerType,
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
        console.info("-=xs",this.step.file.showFileName);
        this.showFields = [];
        this.step.file.filePath = item.fileRelativPath;
        this.step.file.name = item.fileRelativPath + '/' + this.step.file.showFileName;
        this.step.file_server_type = item.fileServerType;
        this.step.ftp_username = item.userName;
        this.step.ftp_password = item.password;
        this.step.file.password = item.password
      },

      changeFile() {
        this.queryParams = {
          fileType: 'xls,xlsx',
          isFolder: '0',
        };
        this.selectFileVisiable = true;
      },

      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.step.folderName = this.$refs.fileData.selecData.fileName;
          this.step.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.ftp_username = this.$refs.fileData.selecData.userName;
          this.step.file_server_type = this.$refs.fileData.selecData.fileServerType;
          this.step.ftp_password = this.$refs.fileData.selecData.password;
        }
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
          fileType:'xls'
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
        };
        this.step.fields.field = [];
        let curStep = store.getters.getCurrentStep(param);
        let laststeps = store.getters.getLastStep(param);

        let outFields =  store.getters.generateOutputFields(param);
        this.step['outFields'] = outFields;
        if (laststeps !== undefined && laststeps.length > 0) {
          this.laststep = laststeps[0];
          this.lastStepField =outFields;
        }
        if (curStep !== undefined && curStep.fields.field !== undefined) {
          for (let key in curStep) {
            if (curStep[key] === 'Y' && key !== 'type') {
              this.step[key] = true;
            }else if(curStep[key] === 'N' && key!=='type'){
              this.step[key] = false;
            }else{
              if(key ==='fields'){
              }else if(key === 'file'){
                for(let subKey in curStep.file){
                  if(curStep[subKey] === 'Y'){
                    this.step.file[subKey] =true;
                  }else if(curStep[subKey]==='N'){
                    this.step.file[subKey]=false;
                  }else{
                    this.step.file[subKey] = curStep.file[subKey];
                  }
                }
              }
              else{
                this.step[key] = curStep[key];
              }

            }
          }
          if(this.step.file.showFileName!==''&& this.step.file.showFileName!==undefined){
            this.getFileList();
          }

          this.step.type = 'ExcelOutput2';
          this.step.fields.field=[];
          let fields = JSON.parse(JSON.stringify(curStep.fields.field));
          fields.forEach(item => {
            item.edit = false;
            this.step.fields.field.push(item);
          })
          this.selectedPage();
        }

      },

      submit() {
        this.step.file.name = this.step.file.filePath+"/"+this.step.file.showFileName;
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
        for(let key in step.file){
          if (typeof step.file[key] === 'boolean') {
            if (step.file[key] === true) {
              step.file[key] = 'Y'
            } else {
              step.file[key] = 'N'
            }
          }
        }
        step['oldStepName'] = this.oldStepName;
        // 控件重命名
        let paramName = step.name;

        let newName = store.getters.getCheckNodeName(this.key, this.oldStepName, paramName);
        let flag = false;
        if (step.name !== newName) {
          flag = true;
          step.name = newName;
        }
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
        console.info("outFiels",step.outFields)
        step.file.name = step.file.filePath+'/'+step.file.showFileName;
        // 修改步骤xinxi
        let param = {
          key: this.key,
          value: {
            oldStepName: this.oldStepName,
            step: step
          }
        };
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
      changeformat() {
        if (this.step.file.SpecifyFormat == true) {
          this.format = false;
          this.add_time = true;
          this.add_data = true;
          this.step.file.SpecifyFormat = true;
        } else if (this.step.file.SpecifyFormat == false) {
          this.format = true;
          this.add_time = false;
          this.add_data = false;
        }
      }, //指定时间格式
      changetable() {
        if (this.step.protect_sheet == true) {
          this.worktable = false;
          this.step.protect_sheet = true;
        } else if (this.step.protect_sheet == false) {
          this.worktable = true;
        }
      }, //保护工作表
      changetem() {
        if (this.step.temporary == true) {
          this.use = false;
          this.step.temporary = true;
        } else if (this.step.temporary == false) {
          this.use = true;
        }
      }, //临时文件
      changemodel() {
        if (this.step.template.enabled == true) {
          this.exmodel = false;

          this.zadd = false;
          this.step.template.enabled = true;
        } else if (this.step.template.enabled == false) {
          this.exmodel = true;

          this.zadd = true;
        }
      }, //使用模板
    },
  }
</script>

<style>
  .lookvalue {
    margin-left: 60px;
  }

  .el-card.is-always-shadow {
    webkit-box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
    box-shadow: 0 0px 0px 0 rgba(0, 0, 0, 0.1);
  }

  .el-card__body {
    padding: 10px;
    margin-left: 15px;
  }

  .two {
    margin-top: 10px;
  }

  .datacard {
    margin-top: 15px;
  }

  .ziduan {
    margin-top: 5px;
  }

  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }

  input[type="file"] {
    display: none;
  }
</style>
