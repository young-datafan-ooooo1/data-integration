<template>
  <div class="column_table">
    <el-table
      :data="showFields"
      border
      style="width: 100%;margin-top: 5px"
      :height="tableHeight"
      size="mini"
      v-loading="tableLoading"
      :header-cell-style="{background:'#FAFAFA'}"
    >
      <el-table-column type="index" label="序号" width="50"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="nameCn" label="显示名称">
        <template slot-scope="scope">
          <el-input v-model="scope.row.nameCn" size="mini" @change="changeColumn(scope.row)"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="fieldType" label="类型">
        <template slot-scope="scope">
          <el-select v-model="scope.row.type" size="mini" style="width: 100%;">
            <el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="format" label="格式">
        <template slot-scope="scope">
          <el-select
            v-model="scope.row.format"
            size="mini"
            style="width: 100%;"
            clearable
            filterable
            allow-create
            default-first-option
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
        </template>
      </el-table-column>
      <el-table-column prop="length" label="长度" width="80">
        <template slot-scope="scope">
          <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.length"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="precision" label="精度" width="80">
        <template slot-scope="scope">
          <el-input
            type="number"
            size="mini"
            style="width: 100%;"
            v-model="scope.row.precision"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="trim_type" label="去空格类型">
        <template slot-scope="scope">
          <el-select
            v-model="scope.row.trim_type"
            clearable
            placeholder="去空格类型"
            size="mini"
            style="width: 100%"
          >
            <el-option v-for="item in trimType" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
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
      :total="total"
    ></el-pagination>
    <div style="margin-top: 2px; text-align: center;">
      <el-button size="mini" @click="getHeaderInfo()">获取来自头部数据的字段</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ColumnTable",
    props: {
      // tableHeight:Number||String,
      showFields:Array,
    },
    data() {
      return {
        tableHeight: '100%',
        tableLoading:false,
        curPage:1,
        pageSize:10,
        total:0,
        fieldTypes: [
          "Number",
          "String",
          "Date",
          "Boolean",
          "Integer",
          "BigNumber",
          "Timestamp",
        ], //字段类型
        formats: ["General", "0", "0.00", "#,##0", "#,##0.00", '"$"#,##0_);("$"#,##0)', '"$"#,##0_);[Red]("$"#,##0)', '"$"#,##0.00_);("$"#,##0.00)', '"$"#,##0.00_);[Red]("$"#,##0.00)',
          "0%", "0.00%", "0.00E+00", "# ?/?",
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
      }
    },
    mounted() {
      this.init();
    },
    methods:{
      init(){
        console.info("=-=dad表格");
      },
      changeColumn(){},
      getHeaderInfo(){},
      deleteRow(){},
      selectedPage(){},
    },
  }
</script>

<style scoped>

</style>
