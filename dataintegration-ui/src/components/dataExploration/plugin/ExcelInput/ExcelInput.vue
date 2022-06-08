<template>
  <div class="plugin_body">
    <div class="plugin_content">
      <el-form size="mini" :model="step" :rules="rules" label-width="30%" label-position="right">
        <el-form-item label="步骤名称" prop="name">
          <el-input v-model="step.name" placeholder="请输入步骤名称" clearable style="width: 65%"></el-input>
        </el-form-item>
      </el-form>
      <el-tabs v-model="activeName" class="custom_tabs" style="height: 90%">
        <el-tab-pane label="文件" name="Basic">
          <el-form :model="step" label-position="right" :rules="rules" label-width="30%" size="mini">
<!--            <el-form-item label="表格类型" prop="type">-->
<!--              <el-select v-model="step.type" placeholder="请选择文件类型" style="width: 100%">-->
<!--                <el-option v-for="item in tabletype" :key="item" :value="item"></el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
            <el-form-item prop="showFileName" label="文件夹">
              <el-select v-model="step.showFileName" size="mini" placeholder="请选择文件夹" style="width: 65%" @change="getFileList">
                <el-option v-for="item in dirs" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
              </el-select>
              <el-button size="mini" type="primary" @click="changeFile1">新建</el-button>
            </el-form-item>
            <el-form-item prop="file_name" label="文件">
              <el-select v-model="step.file_name" size="mini" placeholder="请选择文件"  style="width: 65%" @change="selectFile(step.file_name)" :disabled="step.showFileName===''||step.showFileName===undefined">
                <el-option v-for="item in files" :key="item.fileId" :value="item.fileId" :label="item.fileName"></el-option>
              </el-select>
              <!--            -->
              <el-button size="mini" type="primary" :disabled="step.showFileName===''||step.showFileName===undefined"  @click="changeFile">上传</el-button>
            </el-form-item>
<!--            <el-form-item label="选择文件" prop="showFileName">-->
<!--              <el-autocomplete-->
<!--                class="inline-input"-->
<!--                v-model="step.showFileName"-->
<!--                :fetch-suggestions="querySearch"-->
<!--                @change="changeSelect"-->
<!--                placeholder="请输入文件名称搜索文件"-->
<!--                :trigger-on-focus="false"-->
<!--                @select="handleSelect"-->
<!--                @blur="blurInput"-->
<!--                style="width:65%">-->
<!--                <el-button slot="append" size="mini" type="primary" @click="changeFile">本地文件</el-button>-->
<!--              </el-autocomplete>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="正则表达式" prop="Regular">-->
<!--              <el-input v-model="step.Regular" clearable style="width: 65%"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="正则表达式（排除）" prop="ExRegular">-->
<!--              <el-input v-model="step.ExRegular" clearable style="width: 65%"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="密码" prop="PassWord" autocomplete="off">-->
<!--              <el-input v-model="step.Password" clearable style="width: 65%"></el-input>-->
<!--            </el-form-item>-->
            <div class="node_title_basic">
              <span>选中的文件：</span>
            </div>
            <el-table
              :data="step.file"
              height="230"
              size="mini"
              :header-cell-style="{background:'#eef1f6'}"
            >
              <el-table-column type="index" label="序号"></el-table-column>
              <el-table-column prop="file" label="文件">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.fileName"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="GeneralSymbols" label="通配符号">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.GeneralSymbols"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="GSymbolsEx" label="通配符号（排除）" width="150">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.GSymbolsEx"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="Require" label="要求">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.file_required" clearable size="mini">
                    <el-option
                      v-for="item in requires"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="Obtain" label="包含子目录" width="100">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.include_subfolders" clearable size="mini">
                    <el-option
                      v-for="item in requires"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作">
                <template slot-scope="scope">
                  <el-button
                    @click.native.prevent="deleteFile(scope.$index, step.file)"
                    type="text">移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

          </el-form>
          <!--          </div>-->
        </el-tab-pane>
        <el-tab-pane label="工作表" name="WorkSheet">
          <div class="tab_content">
            <div class="node_title_basic">
              <span>要读取的工作表列表</span>
              <span style="float: right">
                <el-button type="primary" size="mini" @click="getSheetNameByStep">获取工作表名称</el-button>
              </span>
            </div>
            <el-table :data="step.sheets.sheet" border style="width: 100%;margin-top: 5px" height="350"
                      v-loading="tableLoading"
                      :header-cell-style="{background:'#FAFAFA'}">
              <el-table-column type="index" width="50"></el-table-column>
              <el-table-column prop="name" label="工作表名称"></el-table-column>
              <el-table-column prop="startrow" label="起始行">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.startrow" size="mini"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="startcol" label="起始列">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.startcol" size="mini"></el-input>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="内容" name="Content">
          <el-form :model="step" ref="Exel_Content" label-width="30%" size="mini">
            <el-form-item prop="header" label="头部">
              <el-checkbox v-model="step.header"></el-checkbox>
            </el-form-item>
            <el-form-item prop="noempty" label="非空记录">
              <el-checkbox v-model="step.noempty"></el-checkbox>
            </el-form-item>
            <el-form-item prop="stoponempty" label="停在空记录">
              <el-checkbox v-model="step.stoponempty"></el-checkbox>
            </el-form-item>
            <el-form-item label="限制" prop="limit">
              <el-input v-model="step.limit" clearable style="width: 65%;"></el-input>
            </el-form-item>

            <el-form-item label="编码:" prop="encoding">
              <el-select
                v-model="step.encoding"
                clearable
                placeholder="编码"
                size="mini"
                style="width: 65%;">
                <el-option v-for="item in encodings" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item prop="add_to_result_filenames" label="添加文件名">
              <el-checkbox v-model="step.add_to_result_filenames"></el-checkbox>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="错误处理" name="ErrorHandle">
          <el-form :model="step" label-position="right" label-width="30%" size="mini">
            <el-form-item label="严格类型？" prop="strict_types">
              <el-checkbox v-model="step.strict_types"></el-checkbox>
            </el-form-item>
            <el-form-item label="忽略错误？" prop="error_ignored">
              <el-checkbox v-model="step.error_ignored" @change="changeValue()"></el-checkbox>
            </el-form-item>
            <el-form-item label="跳过错误行为？">
              <el-checkbox v-model="step.error_line_skipped" :disabled="dis"></el-checkbox>
            </el-form-item>

            <el-form-item label="告警文件目录">
              <el-input v-model="step.bad_line_files_destination_directory" :disabled="dis"
                        style="width: 20%"></el-input>
              <label>扩展名</label>
              <el-input v-model="step.bad_line_files_extension" :disabled="dis" style="width: 20%"></el-input>
              <el-button type="primary" size="mini" :disabled="dis">变量</el-button>
              <el-button type="primary" size="mini" :disabled="dis">浏览</el-button>
            </el-form-item>
            <el-form-item label="错误文件路径">
              <el-input v-model="step.error_line_files_destination_directory" :disabled="dis"
                        style="width: 20%"></el-input>
              <label>扩展名</label>
              <el-input v-model="step.error_line_files_extension" :disabled="dis" style="width: 20%"></el-input>
              <el-button type="primary" size="mini" :disabled="dis">变量</el-button>
              <el-button type="primary" size="mini" :disabled="dis">浏览</el-button>
            </el-form-item>
            <el-form-item label="失败记录数文件路径">
              <el-input v-model="step.line_number_files_destination_directory" :disabled="dis"
                        style="width: 20%"></el-input>
              <label>扩展名</label>
              <el-input v-model="step.line_number_files_extension" :disabled="dis" style="width: 20%"></el-input>
              <el-button type="primary" size="mini" :disabled="dis">变量</el-button>
              <el-button type="primary" size="mini" :disabled="dis">浏览</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="字段" name="Filed">
          <div class="node_title_basic">
            <span>字段信息</span>
            <span style="float: right">
              <el-button size="mini" @click="getSheetHeaderColumn()" type="primary">获取来自头部数据的字段</el-button>
            </span>
          </div>
<!--          <div class="tab_content">-->
            <el-table
              :data="showFields"
              border
              style="width: 100%;margin-top: 5px"
              height="350"
              size="mini"
              v-loading="tableLoading"
              :header-cell-style="{background:'#FAFAFA'}"
            >
              <el-table-column type="index" label="序号" width="50"></el-table-column>
              <el-table-column prop="name" label="名称" width="150"></el-table-column>
<!--              <el-table-column prop="nameCn" label="显示名称" width="150">-->
<!--                <template slot-scope="scope">-->
<!--                  <el-input v-model="scope.row.nameCn" size="mini" @change="changeColumn(scope.row)"></el-input>-->
<!--                </template>-->
<!--              </el-table-column>-->
              <el-table-column prop="fieldType" label="类型" width="180">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type" size="mini" style="width: 100%;" v-if="scope.row.edit === true">
                    <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                  <span v-else>{{scope.row.type}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="format" label="格式" width="180">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.format"
                    size="mini"
                    style="width: 100%;"
                    clearable
                    filterable
                    allow-create
                    default-first-option
                    v-if="scope.row.edit === true"
                  >
                    <el-option
                      v-for="item in formatDate"
                      :key="item"
                      :label="item"
                      :value="item"
                      v-if="scope.row.type ==='Date'||scope.row.type==='Timestamp'"
                    ></el-option>
                    <el-option
                      v-for="item in formatNumber"
                      :key="item"
                      :label="item"
                      :value="item"
                      v-if="scope.row.type ==='Number'"
                    ></el-option>
                    <el-option
                      v-for="item in formats"
                      :key="item"
                      :label="item"
                      :value="item"
                      v-if="scope.row.type !=='Date'&&scope.row.type!=='Timestamp'&& scope.row.type!=='Number'"
                    ></el-option>
                  </el-select>
                  <span v-else>{{scope.row.format}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="length" label="长度" width="80">
                <template slot-scope="scope">
                  <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.length" v-if="scope.row.edit === true"></el-input>
                  <span v-else>{{scope.row.length}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="precision" label="精度" width="80">
                <template slot-scope="scope">
                  <el-input
                    type="number"
                    size="mini"
                    style="width: 100%;"
                    v-model="scope.row.precision"
                    v-if="scope.row.edit === true"
                  ></el-input>
                  <span v-else>{{scope.row.precision}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="trim_type" label="去空格类型">
                <template slot-scope="scope">
                  <el-select
                    v-model="scope.row.trim_type"
                    clearable
                    placeholder="去空格类型"
                    size="mini"
                    v-if="scope.row.edit === true"
                    style="width: 100%">
                    <el-option v-for="item in trimType" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                  <span v-else>{{scope.row.trim_type}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100px">
                <template slot-scope="scope">
                  <!-- <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button> -->
                  <el-button size="mini" type="text" @click="editLine(scope.row)">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-pagination
              style="float: right"
              class="right"
              @current-change="selectedPage"
              :current-page.sync="curPage"
              @size-change="selectedPage"
              :page-size.sync="pageSize"
              :page-sizes="[10, 50,150]"
              layout="total, sizes, prev, pager, next"
              :total="step.fields.field.length"
            ></el-pagination>
<!--          </div>-->
        </el-tab-pane>
        <el-tab-pane label="其它字段" name="OtherFile">
          <div class="tab_content">
            <el-form :model="step" ref="OtherFile" label-width="30%" label-position="right" size="mini">
              <el-form-item label="文件名称字段" prop="Filename">
                <el-input v-model="step.Filename" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="工作表名称名称字段" prop="WSheetname">
                <el-input v-model="step.WSheetname" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="表单的行列号" prop="FormNo">
                <el-input v-model="step.FormNo" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="文件名的字段" prop="name">
                <el-input v-model="step.namefiled" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="扩展名字段" prop="extensionFieldName">
                <el-input v-model="step.extensionFieldName" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="路径字段" prop="pathFieldName">
                <el-input v-model="step.pathFieldName" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="文件大小字段" prop="sizeFieldName">
                <el-input v-model="step.sizeFieldName" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="是否为隐藏文件字段" prop="hiddenFieldName">
                <el-input v-model="step.hiddenFieldName" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="最后修改时间字段" prop="lastModificationTimeFieldName">
                <el-input v-model="step.lastModificationTimeFieldName" clearable style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="Uri字段" prop="uriNameFieldName">
                <el-input v-model="step.uriNameFieldName" clearabl style="width: 65%"e></el-input>
              </el-form-item>
              <el-form-item label="Root Uri字段" prop="rootUriNameFieldName">
                <el-input v-model="step.rootUriNameFieldName" clearable style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer plugin_footer">
      <span>
        <el-button @click="closeDialog" size="mini">取 消</el-button>
        <el-button size="mini" type="primary" @click="line=true" :disabled="step.fields.field.length===0">预览记录</el-button>
        <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
      </span>
    </div>


    <el-dialog class="dialog_temp other_dialog" title="选择工作表" :visible.sync="sheetVisiable" :modal-append-to-body="true"
               :append-to-body="true" width="40%">
      <el-transfer v-model="selectValue" :data="allSheetData" :titles="['可选列表', '已选列表']"></el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sheetVisiable = false">取 消</el-button>
        <el-button type="primary" @click="setSheetName">确 定</el-button>
      </div>
    </el-dialog>

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
    <el-dialog :visible.sync="logVisiable" title="日志" :append-to-body="true" width="70%" v-alterELDialogMarginTop="{marginTop:'2vh'}" :modal-append-to-body="true"
               class="abow_in_dialog">
      <Log :key="logKey" :logs="log"></Log>
    </el-dialog>

    <el-dialog
      :title="title"
      :visible.sync="selectFileVisiable"
      v-if="selectFileVisiable"
      class="dialog_temp other_dialog"
      :modal-append-to-body="false"
      :append-to-body="true"
      width="50%"
      :before-close="closeAdd"
    >
      <!-- 子组件 -->
      <fileData
        :params="queryParams"
        :selec-data="selectData"
        :fileType="fileType"
        createChannel="UPLOAD"
        :fileFolder="fileFolder"
        :pid="pid"
        :folder-name="folderName"
        ref="fileData"
      ></fileData>
    </el-dialog>
  </div>
</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store";
  import fileData from "../../../common/UploadFile.vue";
  import columnTable from "../../../common/ColumnTable";
  import {
    getSheetList,
    getCsvHeader,
    selectFileListByType,
    getMoreSheetList,
    getSheetNameByStep,
    getSheetHeaderColumn
  } from "../../../../api/api.js";
  import ColumnTable from "../../../common/ColumnTable";
  import {spliceDataJson} from "../../../../common/common";
  import {executePreviewByFile, getAllFilFolderList, getFileByFileFolder, getFileByFileId} from "../../../../api/api";
  import PreViewData from "../../../common/PreViewData";
  import Log from "../../../flow/Log";

  export default {
    props: {
      selectData: Object,
    },
    components: {
      ColumnTable,
      fileData,
      Log,
      PreViewData
    },
    data() {
      return {
        dirs:[],
        files:[],
        fileFolder:'1',
        title:'新增文件夹',
        pid:'',
        folderName:'',
        logVisiable:false,
        logKey:null,
        log:'',
        preDataVisiable:false,
        previewLoading:false,
        previewKey:null,
        dataPre:[],
        dataColumn:[],
        limit:50,
        line:false,
        sheetVisiable: false,
        selectValue: [],//已经选择的sheet页名称
        allSheetData: [],//可选的sheet页 名称
        sheetNameData: [],
        requires: [{
          label: '是',
          value: 'Y',
        }, {
          label: '否',
          value: 'N',
        }],
        tableHeight: '100%',
        tableLoading: false,
        fileId: "", //文件编号
        showFields: [],
        selectFileVisiable: false,
        GetSheetName: false,
        sheet: [],
        sheets: [],
        fileList: [],
        loadingSheet: false,
        FileSelect: [],
        queryParams: {
          fileType: "",
        },
        formatters: ["DOS", "Unix", "mixed"],
        formatterDatas: [],
        fieldTypes: [
          "Number",
          "String",
          "Date",
          "Boolean",
          "Integer",
          "BigNumber",
          "Timestamp",
        ], //字段类型
        formats: [
          "General",
          "0",
          "0.00",
          "#,##0",
          "#,##0.00",
          '"$"#,##0_);("$"#,##0)',
          '"$"#,##0_);[Red]("$"#,##0)',
          '"$"#,##0.00_);("$"#,##0.00)',
          '"$"#,##0.00_);[Red]("$"#,##0.00)',
          "0%",
          "0.00%",
          "0.00E+00",
          "# ?/?",
          "# ??/??",
          "m/d/yy",
          "d-mmm-yy",
          "d-mmm",
          "mmm-yy",
          "h:mm AM/PM",
          "h:mm:ss AM/PM",
          "h:mm",
          "h:mm:ss",
          "m/d/yy h:mm",
          "reserved-0x17",
          "reserved-0x18",
          "reserved-0x19",
          "reserved-0x1A",
          "reserved-0x1B",
          "reserved-0x1C",
          "reserved-0x1D",
          "reserved-0x1E",
          "reserved-0x1F",
          "reserved-0x20",
          "reserved-0x21",
          "reserved-0x22",
          "reserved-0x23",
          "reserved-0x24",
          "#,##0_);(#,##0)",
          "#,##0_);[Red](#,##0)",
          "#,##0.00_);(#,##0.00)",
          "#,##0.00_);[Red](#,##0.00)",
          '_(* #,##0_);_(* (#,##0);_(* "-"_);_(@_)',
          '_("$"* #,##0_);_("$"* (#,##0);_("$"* "-"_);_(@_)',
          '_(* #,##0.00_);_(* (#,##0.00);_(* "-"??_);_(@_)',
          '_("$"* #,##0.00_);_("$"* (#,##0.00);_("$"* "-"??_);_(@_)',
          "mm:ss",
          "[h]:mm:ss",
          "mm:ss.0",
          "##0.0E+0",
          "@",
        ], //字段格式
        trimType: ["none", "left", "right", "both"],
        fileData: null,
        modelName: "",
        data: "",
        oldStepName: "",
        modelMetaData: [],
        curPage: 1,
        pageSize: 10,
        fileType: "xls,xlsx",
        dis: true,
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
          "x-windows-iso2022jp",
        ],
        tabletype: ["xlxs", "csv", "text"],
        activeName: "Basic",

        step: {
          dirName:'',
          file_name:'',
          showFileName: '',
          name: "Excel输入",
          type: "ExcelInput2",
          ftp_username: "",
          file_server_type:'',
          ftp_password: "",
          distribute: "Y",
          copies: "1",
          partitioning: {"method": "none"},
          header: true,
          noempty: true,
          stoponempty: false,
          limit: "0",
          add_to_result_filenames: true,
          accept_filenames: false,
          file: [],
          fields: {
            field: []
          },
          sheets: {
            sheet: []
          },
          strict_types: false,
          error_ignored: false,
          error_line_skipped: false,
          bad_line_files_extension: "warning",
          error_line_files_extension: "error",
          line_number_files_extension: "line",
          spreadsheet_type: "POI",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: "144",
            yloc: "112",
            draw: "Y"
          }
        },
        // selectData: {},
        csvModel: {
          header: "",
          inputCount: 0,
          character: "",
          space: "",
          splices: "",
        },
        options: [
          {
            name: "GBK",
            label: "GBK",
          },
          {
            name: "UTF-8",
            label: "UTF-8",
          },
        ],
        total: 0,
        restaurants: [],
        rules: {
          showFileName: [
            {
              required: true,
              message: "请选择文件",
              trigger: "change",
            },
          ],
          dirName:[{
            required: true,
            message: '请选择文件夹',
            trigger: 'change'
          }],
          file_name:[{
            required: true,
            message: '请选择文件',
            trigger: 'change'
          }],
          name: [
            {
              required: true,
              message: "步骤名称不能为空",
              trigger: "blur",
            },
          ],
          type: [
            {
              required: true,
              message: "",
              trigger: "blur",
            },
          ],
          "sheets.sheet": [
            {
              required: true,
              message: "请选择工作表",
              trigger: "blur",
            },
          ],
        },
        formatNumber: [
          "0",
          "0.00",
          "#,##0",
          "#,##0.00",
          '"$"#,##0_);("$"#,##0)',
          '"$"#,##0_);[Red]("$"#,##0)',
          '"$"#,##0.00_);("$"#,##0.00)',
          '"$"#,##0.00_);[Red]("$"#,##0.00)',
          "0%",
          "0.00%",
          "0.00E+00",
          "# ?/?",
          "# ??/??",
        ],
        formatDate: [
          "yyyy/MM/dd",
          "yyyy/mm/dd HH/mm/ss",
          "yyyy/MM/dd HH:mm:ss",
          "yyyy/MM/dd HH:mm:ss.S",
          "yyyy/MM/dd HH:mm:ss AM/PM",
          "yyyy-MM-dd",
          "yyyy-mm-dd HH/mm/ss",
          "yyyy-MM-dd HH:mm:ss",
          "yyyy-MM-dd HH:mm:ss.S",
          "yyyy-MM-dd HH:mm:ss AM/PM",
        ],
      };
    },
    comments: {
      columnTable
    },
    mounted() {
      this.init();
      util.$on("close_dialog", () => {
        this.closeAdd();
      });
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

      /**
       * 显示日志
       */
      showLog(){
        this.logKey = new Date().getTime();
        this.logVisiable = true;
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
        }
        this.previewLoading=true;
        executePreviewByFile(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            if(data.content.errors===0){
              this.dataPre=data.content.previewRows;
              this.dataColumn = data.content.previewFieldNames;
              this.previewKey = new Date().getTime();
              this.log = data.content.log;
              this.preDataVisiable = true;
            }else{
              this.log = data.content.log;
              this.showLog();
            }
          }
          this.reverStep();
          this.previewLoading = false;
        })
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

      setSheetName() {
        this.step.sheets.sheet = [];
        this.allSheetData.forEach(item => {
          this.selectValue.forEach(key => {
            if (item.key === key) {
              this.sheetNameData.push({
                name: item.label,
                startrow: "0",
                startcol: "0",
              });
              this.step.sheets.sheet.push({
                key: item.key,
                name: item.label,
                startrow: "0",
                startcol: "0"
              })
            }
          });
        })
        this.sheetVisiable = false;
      },

      transStep() {
        let _this = this;
        let promise = new Promise(function (resolve, reject) {
          if (_this.step.header === true) {
            _this.step.header = "Y";
          } else {
            _this.step.header = "N";
          }
          if (_this.step.noempty === true) {
            _this.step.noempty = "Y";
          } else {
            _this.step.noempty = "N";
          }
          if (_this.step.stoponempty === true) {
            _this.step.stoponempty = "Y";
          } else {
            _this.step.stoponempty = "N";
          }

          _this.step.accept_filenames = _this.step.accept_filenames === true ? 'Y' : 'N';
          _this.step.add_to_result_filenames = _this.step.add_to_result_filenames === true ? 'Y' : 'N';
          _this.step.strict_types = _this.step.strict_types === true ? 'Y' : 'N';
          _this.step.error_ignored = _this.step.error_ignored === true ? 'Y' : 'N';
          _this.step.error_line_skipped = _this.step.error_line_skipped === true ? 'Y' : 'N';
          resolve('success');
        })
        return promise;
      },

      //获取工作表名称
      getSheetNameByStep() {
        this.allSheetData = [];
        this.transStep().then(resolve => {
          let params = {
            step: this.step
          }
          getSheetNameByStep(params).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              data.content.forEach((item, index) => {
                this.allSheetData.push({
                  key: index,
                  label: item,
                })
              })
              this.sheetVisiable = true;
            }
          })
          this.step.header = this.step.header === 'Y';
          this.step.noempty = this.noempty === 'Y';
          this.step.stoponempty = this.step.stoponempty === 'Y';
          this.step.add_to_result_filenames = this.step.add_to_result_filenames === 'Y';
        })
      },

      getSheetHeaderColumn() {
        this.transStep().then(resolve => {
          let params = {
            step: this.step,
          }
          this.step.fields.field = [];
          getSheetHeaderColumn(params).then(res => {
            let {
              data
            } = res;
            if (data.code === '10000') {
              data.content.forEach(item => {
                let fileLength = -1;
                let precision = -1;
                if (item.type.toUpperCase() === ('number').toUpperCase()) {
                  fileLength = 38;
                  precision = 0;
                } else if (item.type.toUpperCase() === ('string').toUpperCase()) {
                  fileLength = 255;
                  precision = -1;
                } else {
                  fileLength = -1;
                  precision = -1;
                }
                this.step.fields.field.push({
                  name: item.name,
                  edit:false,
                  nameCn: item.name,
                  type: item.type,
                  length: item.len,
                  precision: item.precision,
                  trim_type: "none",
                  repeat: "N",
                  format: item.format,
                  currency: item.dollarSign,
                  decimal: item.pointSymbol,
                  group: item.pointSymbol,
                })
                // this.total = this.step.fields.field.length;
              })
              this.selectedPage();
              this.step.header = this.step.header === 'Y';
              this.step.noempty = this.noempty === 'Y';
              this.step.stoponempty = this.step.stoponempty === 'Y';
              this.step.add_to_result_filenames = this.step.add_to_result_filenames === 'Y';
            }
          })
        })
      },
      //获取多个文件的sheet页
      getMoreSheetList() {
        this.allSheetData = [];
        let fileIds = [];
        this.FileSelect.forEach(item => {
          fileIds.push(item.fileId);
        })
        let params = {
          fileId: fileIds,
        }
        getMoreSheetList(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            data.content.forEach((item, index) => {
              this.allSheetData.push({
                key: index,
                label: item,
              })
            })
            this.sheetVisiable = true;
          }
        })
      },

      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleExceed(files, fileList) {
        this.$message.warning(
          `当前限制选择1个文件，本次选择了 ${files.length} 个文件，共选择了 ${
            files.length + fileList.length
          } 个文件`
        );
      },

      beforeRemove(file, fileList) {
        return this.$confirm(`确定移除 ${file.name}？`);
      },
      changeValue() {
        if (this.step.error_ignored) {
          this.dis = false;
          this.step.error_ignored = true;
        } else {
          this.dis = true;
        }
      },
      closeDialog() {
        util.$emit("closeDialog", "close");
      },
      submit() {
        this.step.initFlag = true;
        let step = Object.assign({}, this.step);
        if (this.step.header === true) {
          step.header = "Y";
        } else {
          step.header = "N";
        }
        if (this.step.noempty === true) {
          step.noempty = "Y";
        } else {
          step.noempty = "N";
        }
        if (this.step.stoponempty === true) {
          step.stoponempty = "Y";
        } else {
          step.stoponempty = "N";
        }

        step.accept_filenames = this.step.accept_filenames === true ? 'Y' : 'N';
        step.add_to_result_filenames = this.step.add_to_result_filenames === true ? 'Y' : 'N';
        step.strict_types = this.step.strict_types === true ? 'Y' : 'N';
        step.error_ignored = this.step.error_ignored === true ? 'Y' : 'N';
        step.error_line_skipped = this.step.error_line_skipped === true ? 'Y' : 'N';
        step["oldStepName"] = this.oldStepName;
        // 控件重命名
        let paramName = step.name;
        let newName = store.getters.getCheckNodeName(
          this.key,
          this.oldStepName,
          paramName
        );
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
            step: step,
          },
        };
        store.dispatch("updateStepInfo", param);
        let params = {
          id: this.nodeData.id, //插件id
          label: step.name,
          oldName: this.oldStepName,
        };
        util.$emit("updateNode", params);
        util.$emit("closeDialog", "close");
        if (flag) {
          this.$alert(paramName + "名称已存在,重命名为" + newName + "!", "注释", {
            confirmButtonText: "确定",
          });
        } else {
          this.$message({
            message: "步骤信息修改成功",
            type: "success",
          });
        }
      },
      handleClose(done) {
        this.$confirm("确认关闭？")
          .then((_) => {
            done();
          })
          .catch((_) => {
          });
      },
      changeSelect() {
        this.step.sheets.sheet.name = "";
        this.sheets = [];
        // this.showFields = [];
      },
      blurInput() {
        let flag = true;
        this.restaurants.forEach((item, index) => {
          if (item.fileName === this.step.showFileName) {
            flag = false;
          }
        });
        if (flag) {
          // this.step.showFileName = "";
        }
      },
      querySearch(queryString, cb) {
        var restaurants = this.restaurants;
        var results = queryString
          ? restaurants.filter(this.createFilter(queryString))
          : restaurants;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      handleSelect(item) {
        this.FileSelect.push({
          filePath: item.fileRelativPath + '/' + item.fileName,
          fileId: item.fileId,
          file: item.fileName,
          GeneralSymbols: '',
          GSymbolsEx: '',
          Require: 'N',
          Obtain: 'N'
        })
        // this.showFields = [];
        // this.step.showFileName = item.fileName;
        this.fileId = item.fileId;
        // this.step.file.name = item.fileRelativPath;
        this.step.file.push({
          fileName: item.fileName,
          name: item.fileRelativPath,
          file_required: 'N',
          include_subfolders: 'N',
        })
        this.step.fileId = item.fileId;
        this.step.filename = item.fileName;
        this.step.filePath = item.fileRelativPath;
        this.step.file_server_type = item.fileServerType;
        this.step.ftp_username = item.userName;
        this.step.ftp_password = item.password;
        // this.step.sheets.sheet['name'] = "";
        // this.step.showFileName = '';
      },
      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          this.step.file.push({
            fileName: this.$refs.fileData.selecData.fileName,
            name: this.$refs.fileData.selecData.fileRelativPath,
            file_required: 'N',
            include_subfolders: 'N',
          })
          // this.step.file.name = this.$refs.fileData.selecData.fileRelativPath;
          this.step.fileId = this.$refs.fileData.selecData.fileId;
          this.step.filename = this.$refs.fileData.selecData.fileName;
          this.step.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.file_server_type = this.$refs.fileData.selecData.fileServerType;
          this.step.ftp_username = this.$refs.fileData.selecData.userName;
          this.step.ftp_password = this.$refs.fileData.selecData.password;
        }
        this.selectFileVisiable = false;
      },
      getFileByFileType(fileId) {
        let queryParams = {
          fileType: "xls,xlsx",
          isFolder: "0",
        };
        let fileList = [];
        selectFileListByType(queryParams).then((res) => {
          if (res.data.code === "10000") {
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
              });
            } else {
              res.data.content.forEach((item, index) => {
                fileList.push({
                  value: item.fileName,
                  fileName: item.fileName,
                  fileId: item.fileId,
                  fileServerType:item.fileServerType,
                  fileRelativPath: item.fileRelativPath,
                  userName: item.userName,
                  password: item.password,
                });
              });
            }
            this.restaurants = fileList;
          } else {
            this.$message({
              message: "获取文件列表失败",
              type: "error",
            });
          }
        });
      },
      changeFile() {
        this.queryParams = {
          fileType: "xls,xlsx",
          isFolder: "0",
        };
        this.title='上传文件';
        this.fileFolder='0';
        this.pid = this.step.showFileName.split(",")[0];
        this.folderName  = this.step.showFileName.split(",")[1];
        this.selectFileVisiable = true;
      },
      changeFile1() {
        this.fileFolder='1';
        this.title="新增文件夹"
        this.selectFileVisiable = true;
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
      batchUploadBasicModel(file) {
        this.fileName = file.name;
        this.fileData = file.raw;
      },

      //移除文件
      deleteFile(index, data) {
        data.forEach((item, i) => {
          if (i === index) {
            data.splice(index, 1);
          }
        })
      },

      // 获取头部数据
      deleteRow(index) {
        //移除一行
        let newIndex = (this.curPage - 1) * this.pageSize + index;
        this.step.fields.field.splice(newIndex, 1); //删掉该行
        this.selectedPage();
      },

      selectedPage() {
        this.showFields = [];
        let fields = this.step.fields.field.slice(
          (this.curPage - 1) * this.pageSize,
          this.curPage * this.pageSize
        );
        fields.forEach((item, index) => {
          this.showFields.push(item);
        });
      },

      getHeaderInfo() {
        // this.showFields = [];
        if (this.step.fileId === "") {
          this.$message({
            message: "请先选择文件",
            type: "error",
          });
        } else {
          let params = {
            fileId: this.step.fileId,
            sheetName: this.step.sheets.sheet.name,
            split: "",
          };
          let tmpData = [];
          this.tableLoading = true;
          getCsvHeader(params).then((res) => {
            let {data} = res;
            if (data.code === "10000") {
              // this.total = data.content.length;
              for (let i = 0; i < data.content.length; i++) {
                let fileLength = -1;
                let precision = -1;
                if (
                  data.content[i].fileType.toUpperCase() ===
                  "number".toUpperCase()
                ) {
                  fileLength = 38;
                  precision = 0;
                } else if (
                  data.content[i].fileType.toUpperCase() ===
                  "string".toUpperCase()
                ) {
                  fileLength = 255;
                  precision = -1;
                } else {
                  fileLength = -1;
                  precision = -1;
                }
                tmpData.push({
                  name: data.content[i].fieldName,
                  nameCn: data.content[i].fieldName,
                  type: data.content[i].fileType,
                  length: fileLength,
                  precision: precision,
                  trim_type: "none",
                  repeat: "N",
                  format: "",
                  currency: "",
                  decimal: "",
                  group: "",
                });
              }
              this.step.fields.field = tmpData;
              this.tableLoading = false;
              this.selectedPage();
            } else {
              this.$message({
                message: "获取头部信息失败",
                type: "error",
              });
              this.tableLoading = false;
            }
          });
        }
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
          fileType:'xls,xlsx'
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
        // this.showFields = [];
        this.getAllFolder();
        this.step.fields.field = [];
        this.step.file = [];
        this.step.sheets.sheet = [];
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        // 获取当前步骤信息参数
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName,
        };
        let curStep = store.getters.getCurrentStep(param);
        console.info(curStep)
        if (curStep !== undefined && curStep.sheets!==undefined) {
          this.step.name = curStep.name;
          this.step.initFlag = curStep.initFlag;
          this.step.showFileName = curStep.showFileName;
          this.step.modelName = curStep.modelName;
          this.step.description = curStep.description;
          this.step.distribute = curStep.distribute;
          this.step.custom_distribution = curStep.distribute;
          this.step.copies = curStep.copies;
          this.step.partitioning = curStep.partitioning;
          this.step.isBusiness = curStep.isBusiness;
          this.step.connection = curStep.connection;
          this.step.sql = curStep.sql;
          this.step.lookup = curStep.lookup;
          if(curStep.file_name !==''&&curStep.file_name !==undefined){
            this.step.file_name = curStep.file_name;
            this.getFileList();
          }
          this.step.execute_each_row = curStep.execute_each_row;
          this.step.variables_active = curStep.variables_active;
          this.step.lazy_conversion_active = curStep.lazy_conversion_active;
          this.step.model_name = curStep.model_name;
          this.step.attributes = curStep.attributes;
          this.step.cluster_schema = curStep.cluster_schema;
          this.step.remotesteps = curStep.remotesteps;
          this.step.sheets.sheet = curStep.sheets.sheet;
          this.step.sheets.sheet.forEach(item => {
            this.selectValue.push(item.key);
          })
          this.step.GUI = curStep.GUI;
          // this.step.file = curStep.file;
          let fields = JSON.stringify(curStep.fields.field);
          let tmpFields = JSON.parse(fields);
          tmpFields.forEach((item, index) => {
            item.edit = false;
            this.step.fields.field.push(item);
          });
          this.total = this.step.fields.field.length;
          this.selectedPage();

          let fileData = curStep.file;
          let tmpFile = [];
          fileData.forEach(item => {
            tmpFile.push({
              fileName: item.fileName,
              name: item.name,
              file_required: item.file_required,
              include_subfolders: item.include_subfolders,
            })
          })
          this.step.file = tmpFile;
          this.step.header =
            curStep.header === "Y";
          this.step.footer = curStep.footer === "Y";
          this.step.delimiter =
            curStep.delimiter === undefined ? "," : curStep.delimiter;
          this.step.enclosure =
            curStep.enclosure === undefined ? '"' : curStep.enclosure;
          this.step.encoding =
            curStep.encoding === undefined ? "UTF-8" : curStep.encoding;
          this.step.limit = curStep.limit === undefined ? 0 : curStep.limit;
          this.step.nr_headerlines =
            curStep.nr_headerlines === undefined ? 1 : curStep.nr_headerlines;
          this.step.nr_footerlines =
            curStep.nr_footerlines === undefined ? 1 : curStep.nr_footerlines;
          this.step.format = curStep.format === undefined ? "DOS" : curStep.format;
          this.step.newline_possible =
            curStep.newline_possible === "Y";
          this.step.noempty =
            curStep.noempty === undefined || curStep.noempty === "Y";
          this.step.date_format_lenient =
            curStep.date_format_lenient === undefined ||
            curStep.date_format_lenient === "Y";
          this.step.date_format_locale =
            curStep.date_format_locale === undefined
              ? "zh_CN"
              : curStep.date_format_locale;
          this.step.filePath = curStep.filePath;
          this.step.fileId = curStep.fileId;
          this.step.ftp_password = curStep.ftp_password;
          this.step.ftp_username = curStep.ftp_username;
          this.step.file_server_type = curStep.file_server_type;
        }
        this.step.tableName = this.nodeData.id;
      },
    },
  };
</script>

<style>
  input[type="file"] {
    display: none;
  }

  .el-form-item--mini.el-form-item {
    margin-bottom: 10px;
  }

  .bodyborder {
    height: 350px;
    overflow: auto;
  }

  .Textborder {
    border: 1px solid #ccc;
    width: 90%;
    height: 100px;
    text-align: center;
  }

  .father_Sheet {
    display: inline-block;
    width: 400px;
  }

  .Sheet1,
  .Sheet2 {
    float: left;
  }

  .Sheet2 {
    width: 80px;
    height: 350px;
    border: none;
    padding: 100px 0px;
  }

  .Sheet3 {
    float: right;
  }

  .Sheetson {
    border: 1px solid #ccc;
    width: 150px;
    height: 300px;
    margin: 0px 5px;
  }
</style>
