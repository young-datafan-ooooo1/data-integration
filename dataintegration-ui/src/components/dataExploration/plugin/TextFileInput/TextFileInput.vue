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
              :data="fileSelect"
              height="230"
              size="mini"
              :header-cell-style="{background:'#eef1f6'}"
            >
              <el-table-column type="index" label="序号"></el-table-column>
              <el-table-column prop="fileName" label="文件">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.fileName"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="filemask" label="通配符号">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.filemask"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="exclude_filemask" label="通配符号（排除）" width="150">
                <template slot-scope="scope">
                  <el-input size="mini" v-model="scope.row.exclude_filemask"></el-input>
                </template>
              </el-table-column>
              <el-table-column prop="file_required" label="要求">
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
              <el-table-column prop="include_subfolders" label="包含子目录" width="100">
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
                    @click.native.prevent="deletefileSelect(scope.$index)"
                    type="text">移除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="node_title_basic">
              <span>接受上一步步骤的文件：</span>
            </div>
            <el-form :model="step" ref="SetName" label-width="30%" size="mini">
              <el-form-item label="从以前的步骤获取文件名">
                <el-checkbox v-model="step.accept_filenames" @change="FileNmchangeValue()"></el-checkbox>
              </el-form-item>
              <el-form-item label="从以前的步骤接受字段名">
                <el-checkbox v-model="step.passing_through_fields" :disabled="ac_file_dis"></el-checkbox>
              </el-form-item>
              <el-form-item label="来自步骤读取的文件名">
                <el-input v-model="step.accept_field" :disabled="ac_file_dis" style="width: 65%"></el-input>
              </el-form-item>
              <el-form-item label="输入的字段当做文件名">
                <el-input v-model="step.accept_stepname" :disabled="ac_file_dis" style="width: 65%"></el-input>
              </el-form-item>
            </el-form>
          </el-form>
          <!--          </div>-->
        </el-tab-pane>
        <el-tab-pane label="内容" name="WorkSheet">
          <el-form :model="step" ref="TextCon" label-width="120px" size="mini">
            <el-form-item label="文件类型">
              <el-select v-model="step.file.type" style="width: 65%" clearable placeholder="请选择文件类型">
                <el-option label="Fixed" value="Fixed"></el-option>
                <el-option label="CSV" value="CSV"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="分隔符">
              <el-input v-model="step.separator" clearable style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="文本限定符">
              <el-input v-model="step.enclosure" clearable style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="逃逸字符">
              <el-input v-model="step.escapechar" clearable style="width: 65%"></el-input>
            </el-form-item>
            <el-form-item label="记录数量限制">
              <el-input v-model="step.limit" clearable style="width: 65%"></el-input>
            </el-form-item>
            <el-row>
              <el-col :span="8">
                <el-form-item label="没有空行">
                  <el-checkbox v-model="step.noempty"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="在文本限定符里允许换行">
                  <el-checkbox v-model="step.enclosure_breaks"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="8">
                <el-form-item label="头部">
                  <el-checkbox v-model="step.header" @change="changeValue()"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="头部数量">
                  <el-input v-model="step.nr_headerlines" :disabled="header_dis" style="width: 65%"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="尾部">
                  <el-checkbox v-model="step.footer" @change="changeValue()"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="尾部数量">
                  <el-input v-model="step.nr_footerlines" :disabled="tail_dis" style="width: 65%"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="包装行">
                  <el-checkbox v-model="step.line_wrapped" @change="changeValue()"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="以时间包装行数">
                  <el-input v-model="step.nr_wraps" :disabled="wraps_dis" style="width: 65%"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="输出包括字段名">
                  <el-checkbox v-model="step.include" @change="changeValue()"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="包含文件名的字段名称">
                  <el-input v-model="step.include_field" :disabled="incfield_dis" style="width: 65%"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="8">
                <el-form-item label="输出包括行数">
                  <el-checkbox v-model="step.rownum" @change="changeValue()"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="行数字段名称">
                  <el-input v-model="step.rownum_field" :disabled="rowNmfiled_dis" style="width: 65%"></el-input>
                </el-form-item>
                <el-form-item label="按文件取行号">
                  <el-checkbox v-model="step.rownumByFile" :disabled="rowNmfiled_dis"></el-checkbox>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="分页布局">
                  <el-checkbox v-model="step.layout_paged" @change="changeValue()"></el-checkbox>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="记录的行数">
                  <el-input v-model="step.nr_lines_per_page" :disabled="page_dis" style="width: 65%"></el-input>
                </el-form-item>
                <el-form-item label="按文件取行号">
                  <el-input v-model="step.nr_lines_doc_header" :disabled="page_dis" style="width: 65%"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="压缩">
              <el-select v-model="step.Compre" clearable style="width: 65%">
                <el-option label="None" value="none"></el-option>
                <el-option label="Gzip" value="gzip"></el-option>
                <el-option label="Zip" value="zip"></el-option>
                <el-option label="Snappy" value="snappy"></el-option>
                <el-option label="Hadoop-Snappy" value="hadoop-snappy"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="格式">
              <el-select v-model="step.a3at" style="width: 65%" clearable>
                <el-option label="Dos" value="dos"></el-option>
                <el-option label="Unix" value="unix"></el-option>
                <el-option label="Mixed" value="mix"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="编码方式">
              <el-select v-model="step.encoding" style="width: 65%" clearable>
                <el-option v-for="item in encodings" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="长度">
              <el-select v-model="step.length" style="width: 65%" clearable>
                <el-option label="Character" value="character"></el-option>
                <el-option label="Bytes" value="bytes"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="本地日期格式">
              <el-select v-model="step.date_format_locale" style="width: 65%" clearable>
                <el-option v-for="item in dataFormatters" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否严格解析日期">
              <el-checkbox v-model="step.date_format_lenient"></el-checkbox>
            </el-form-item>
          </el-form>
          <div
            style=" width:100%; height: 100px; border:1px solid #ccc;padding-top:30px;"
          >
            <el-form :model="step" label-width="180px">
              <el-form-item label="添加文件名">
                <el-checkbox v-model="step.add_to_result_filenames"></el-checkbox>
              </el-form-item>
            </el-form>
          </div>
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
        <el-tab-pane label="过滤" name="TextFilter">

          <el-form :model="step" ref="FileFilter" size="mini">
            <div class="node_title_basic">
              <span style="float: right">
                <el-button size="mini" type="primary" @click="addFilter">新增</el-button>
              </span>
            </div>
            <el-form-item>
              <el-table
                :data="step.filters.filter"
                border
                height="440px"
                :header-cell-style="{background:'#eef1f6'}">
                <el-table-column prop="filter_string" label="过滤字符串">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.filter_string" size="mini"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="filter_position" label="过滤器位置">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.filter_position" size="mini"></el-input>
                  </template>
                </el-table-column>
                <el-table-column prop="filter_is_last_line" label="停在过滤器">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.filter_is_last_line" clearable size="mini">
                      <el-option
                        v-for="item in requires"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column prop="filter_is_positive" label="积极匹配">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.filter_is_positive" clearable size="mini">
                      <el-option
                        v-for="item in requires"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                      </el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button type="text" @click="delteFilter(scope.$index)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-form>

        </el-tab-pane>
        <el-tab-pane label="字段" name="Filed">
          <div class="node_title_basic">
            <span>字段信息</span>

            <span style="float: right;margin-left: 5px">
              <el-button size="mini" @click="getCsvHeaderColumn()" type="primary">获取来自头部数据的字段</el-button>
            </span>
            <!-- <span style="float: right">
              <el-button size="mini" @click="addRow" type="primary">新增</el-button>
            </span> -->
          </div>
          <el-table
            :data="showFields"
            style="width: 100%;margin-top: 5px"
            height="350"
            size="mini"
            v-loading="tableLoading"
            :header-cell-style="{background:'#FAFAFA'}"
          >
            <el-table-column type="index" label="序号" width="50"></el-table-column>
            <el-table-column prop="name" label="名称" width="150">
              <template slot-scope="scope">
                <el-input v-model="scope.row.name" size="mini" v-if="scope.row.edit===true"></el-input>
                <span v-else>{{scope.row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="fieldType" label="类型" width="120">
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
                  v-if="scope.row.edit===true"
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
            <el-table-column prop="position" label="位置" width="80">
              <template slot-scope="scope">
                <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.position"  v-if="scope.row.edit=== true"></el-input>
                <span v-else>{{scope.row.position}}</span>
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

            <el-table-column prop="currency" label="货币类型" width="80">
              <template slot-scope="scope">
                <el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.currency" v-if="scope.row.edit === true"></el-input>
                <span v-else>{{scope.row.currency}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="decimal" label="小数" width="80">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.decimal" v-if="scope.row.edit === true"></el-input>
                <span v-else>{{scope.row.decimal}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="group" label="分组" width="80">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.group" v-if="scope.row.edit === true"></el-input>
                <span v-else>{{scope.row.group}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="nullif" label="Null if" width="80">
              <template slot-scope="scope">
                <el-input size="mini" style="width: 100%;" v-model="scope.row.nullif" v-if="scope.row.edit === true"></el-input>
                <span v-else>{{scope.row.nullif}}</span>
              </template>
            </el-table-column>

            <el-table-column prop="trim_type" label="去除字符串方式">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.trim_type"
                  clearable
                  placeholder="去除字符串方式"
                  size="mini"
                  style="width: 100%"  v-if="scope.row.edit === true">
                  <el-option v-for="item in trimType" :key="item" :label="item" :value="item"></el-option>
                </el-select>
                <span v-else>{{scope.row.trim_type}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="repeat" label="重复" width="80">
              <template slot-scope="scope">
                <el-select v-model="scope.row.repeat" size="mini" v-if="scope.row.edit === true">
                  <el-option v-for="item in requires" :key="item.value" :value="item.value"
                             :label="item.label"></el-option>
                </el-select>
                <span v-else>{{scope.row.repeat}}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100px" fixed="right">
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
        </el-tab-pane>
        <el-tab-pane label="其它输出字段" name="OtherFile">
          <div class="tab_content">
            <el-form :model="step" ref="OFiled" label-width="30%" size="mini">
              <el-form-item label="文件名称字段" prop="shortFileFieldName">
                <el-input v-model="step.shortFileFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="扩展名字段" prop="extensionFieldName">
                <el-input v-model="step.extensionFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="路径字段" prop="pathFieldName">
                <el-input v-model="step.pathFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="文件大小字段" prop="sizeFieldName">
                <el-input v-model="step.sizeFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="是否为隐藏文件字段" prop="hiddenFieldName">
                <el-input v-model="step.hiddenFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="最后修改时间字段" prop="lastModificationTimeFieldName">
                <el-input v-model="step.lastModificationTimeFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="Uri字段" prop="uriNameFieldName">
                <el-input v-model="step.uriNameFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
              <el-form-item label="Root Uri字段" prop="rootUriNameFieldName">
                <el-input v-model="step.rootUriNameFieldName" style="width: 65%" clearable></el-input>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="dialog-footer plugin_footer">
      <span>
        <el-button @click="closeDialog" size="mini">取 消</el-button>
        <el-button size="mini" @click="line=true" :disabled="step.fields.field.length===0"
                   type="primary">预览记录</el-button>
        <el-button @click="submit('step')" type="primary" size="mini">确 定</el-button>
      </span>
    </div>


    <el-dialog class="dialog_temp other_dialog" title="选择工作表" :visible.sync="sheetVisiable" :modal-append-to-body="true"
               :append-to-body="true" width="40%">
      <el-transfer v-model="selectValue" :data="allSheetData" :titles="['可选列表', '已选列表']"></el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="">确 定</el-button>
      </div>
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
  </div>
</template>

<script>
  import util from "../../../../common/utils";
  import store from "../../../../vuex/store";
  import fileData from "../../../common/UploadFile.vue";
  import columnTable from "../../../common/ColumnTable";
  import {
    selectFileListByType,
  } from "../../../../api/api.js";
  import ColumnTable from "../../../common/ColumnTable";
  import {
    executePreviewByFile,
    getAllFilFolderList,
    getCsvHeaderColumn, getFileByFileFolder, getFileByFileId,
    getTxtCsvHeaderColumn
  } from "../../../../api/api";
  import {spliceDataJson} from "../../../../common/common";
  import PreViewData from "../../../common/PreViewData";
  import Log from "../../../flow/Log";

  export default {
    props: {
      selectData: Object,
    },
    components: {
      ColumnTable,
      fileData,
      PreViewData,
      Log
    },
    data() {
      return {
        dirs:[],
        files:[],
        fileFolder:'1',
        title:'新增文件夹',
        pid:'',
        folderName:'',
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
        formats: [],
        page_dis: true,
        ac_file_dis: true,
        header_dis: true,
        tail_dis: true,
        wraps_dis: true,
        incfield_dis: true,
        rowNmfiled_dis: true,
        nodeData: '',
        key: '',
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
        fileSelect: [],
        queryParams: {
          fileType: "",
        },
        dataFormatters: [],
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
        trimType: ["none", "left", "right", "both"],
        fileData: null,
        modelName: "",
        data: "",
        oldStepName: "",
        modelMetaData: [],
        curPage: 1,
        pageSize: 10,
        fileType: "csv,txt",
        dis: true,

        tabletype: ["xlxs", "csv", "text"],
        activeName: "Basic",
        step: {
          dirName:'',
          file_name:'',
          showFileName: '',
          name: "文本文件输入",
          type: "TextFileInput2",
          file_server_type: '',
          ftp_username: "",
          ftp_password: "",
          description: "",
          distribute: "Y",
          custom_distribution: "",
          copies: 1,
          partitioning: {
            method: "none",
            schema_name: ""
          },
          accept_filenames: false,
          passing_through_fields: false,
          accept_field: "",
          accept_stepname: "",
          separator: ",",
          enclosure: "\"",
          enclosure_breaks: "N",
          escapechar: "",
          header: true,
          nr_headerlines: 1,
          footer: false,
          nr_footerlines: 1,
          line_wrapped: false,
          nr_wraps: 1,
          layout_paged: false,
          nr_lines_per_page: 80,
          nr_lines_doc_header: 0,
          noempty: true,
          include: false,
          include_field: "",
          rownum: "N",
          rownumByFile: "N",
          rownum_field: "",
          format: "mixed",
          encoding: "",
          length: "Characters",
          add_to_result_filenames: "Y",
          file: {
            name: [],
            filemask: [],
            exclude_filemask: [],
            fileName: [],
            file_required: [],
            include_subfolders: [],
            type: "CSV",
            compression: "None",
          },
          filters: {
            filter: [],
          },
          fields: {
            field: []
          },
          limit: 0,
          error_ignored: "N",
          skip_bad_files: "N",
          file_error_field: "",
          file_error_message_field: "",
          error_line_skipped: "N",
          error_count_field: "",
          error_fields_field: "",
          error_text_field: "",
          bad_line_files_destination_directory: "",
          bad_line_files_extension: "warning",
          error_line_files_destination_directory: "",
          error_line_files_extension: "error",
          line_number_files_destination_directory: "",
          line_number_files_extension: "line",
          date_format_lenient: true,
          date_format_locale: "zh_CN",
          shortFileFieldName: "",
          pathFieldName: "",
          hiddenFieldName: "",
          lastModificationTimeFieldName: "",
          uriNameFieldName: "",
          rootUriNameFieldName: "",
          extensionFieldName: "",
          sizeFieldName: "",
          attributes: "",
          cluster_schema: "",
          remotesteps: {
            input: "",
            output: ""
          },
          GUI: {
            xloc: 432,
            yloc: 144,
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
        formatNumber: [],
        formatDate: [],
        encodings: [],
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
        this.step.fields.field.forEach((item,index)=>{
          if (item.name === row.name) {
            // item.decimal='.'
            this.$set(this.step.fields.field[index], 'edit', true);
            this.$forceUpdate();
          }
        });
      },

      /**
       * 新增字段
       */
      addFields(){
        this.step.fields.field.push({
          edit:false,
          name:'',
          fieldType:'',
          format:'',
          length:'',
          precision:'',
          position:'',
          currency:'',
          decimal:'',
          group:'',
          nullif:'',
          trim_type:'',
          repeat:'',
        })
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
        }
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
          this.reverStep();
          this.previewLoading = false;
        })
      },
      /**
       * 移除过滤字段
       * @param index
       */
      delteFilter(index) {
        this.step.filters.filter.splice(index, 1);
      },

      /**
       * 新增一条过滤字段
       */
      addFilter() {
        this.step.filters.filter.push({
          filter_string: '',
          filter_position: '',
          filter_is_last_line: 'N',
          filter_is_positive: 'N',
        })
      },

      submitUpload() {
        this.$refs.upload.submit();
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
      handleClick(row) {
        console.log(row);
      },

      deletefileSelect(index) {
        this.fileSelect.splice(index, 1);
      },
      deleteRow(index, rows) {
        this.step.fields.field.splice(index, 1);
      },
      changeValue() {
        if (this.step.error_ignored) {
          this.dis = false;
          this.step.error_ignored = true;
        } else {
          this.dis = true;

        }
        if (this.step.skip_bad_files) {
          this.skbadfile_dis = false;
        } else {
          this.skbadfile_dis = true;
        }
        // 头部
        if (this.step.header) {
          this.header_dis = false;
          this.step.header = true;
        } else {
          this.header_dis = true;
        }
        // 尾部
        if (this.step.footer) {
          this.tail_dis = false;
          this.step.footer = true;
        } else {
          this.tail_dis = true;
        }
        // 以时间包装行
        if (this.step.line_wrapped) {
          this.wraps_dis = false;
        } else {
          this.wraps_dis = true;
        }
        //包括字段名
        if (this.step.include) {
          this.incfield_dis = false;
        } else {
          this.incfield_dis = true;
        }
        //包括行数
        if (this.step.rownum) {
          this.rowNmfiled_dis = false;
        } else {
          this.rowNmfiled_dis = true;
        }
        // 分页布局
        if (this.step.layout_paged) {
          this.page_dis = false;
        } else {
          this.page_dis = true;
        }
      },
      FileNmchangeValue() {
        if (this.step.accept_filenames) {
          this.ac_file_dis = false;
          this.step.accept_filenames = true;
        } else {
          this.step.passing_through_fields = false;
          this.ac_file_dis = true;
        }
      },
      addRow(event) {
        //新增一行
        this.step.fields.field.push({
          edit:false,
          name: "",
          nameCn: "",
          type: "",
          format: "",
          currency: "",
          decimal: "",
          group: "",
          trim_type: "",
          nullif: "",
          length: -1,
          precision: -1,
        });
        this.curPage = parseInt(
          this.step.fields.field.length / this.pageSize + 1
        );
        this.selectedPage();
      },
      closeDialog() {
        util.$emit("closeDialog", "close");
        // 初始化画布数据
      },
      submit() {
        this.step.initFlag = true;
        this.changeStep();
        let step = Object.assign({}, this.step);
        step["oldStepName"] = this.oldStepName;
        // 控件重命名
        if (step.accept_filenames === 'N') {
          step.passing_through_fields = 'N';
        }
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
        // 修改步骤信息
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
      changeFile() {
        this.queryParams = {
          fileType: "txt",
          isFolder: "0",
        };
        this.title='上传文件';
        this.fileFolder='0';
        this.pid = this.step.showFileName.split(",")[0];
        this.folderName = this.step.showFileName.split(",")[1]
        this.selectFileVisiable = true;
      },
      changeFile1() {
        this.fileFolder='1';
        this.title="新增文件夹"
        this.selectFileVisiable = true;
      },
      changeSelect() {
        this.showFields = [];
      },
      blurInput() {
        let flag = true;
        this.restaurants.forEach((item, index) => {
          if (item.fileName === this.step.showFileName) {
            flag = false;
          }
        });
        if (flag) {
          this.step.showFileName = "";
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


      handleSelect(item) {
        // this.step.showFileName = item.fileName;
        this.fileId = item.fileId;
        this.step.file.name = item.fileName;
        // this.step.fileId = item.fileId;
        // this.step.filename = item.fileName;
        // this.step.filePath = item.fileRelativPath;
        this.step.file_server_type = item.fileServerType;
        this.step.ftp_username = item.userName;
        this.step.ftp_password = item.password;
        this.fileSelect.push({
          name: item.fileRelativPath,
          fileName: item.fileName,
          filemask: '',
          exclude_filemask: '',
          file_required: 'N',
          include_subfolders: 'N',

        })
        // this.getHeaderInfo();
      },
      closeAdd() {
        if (this.$refs.fileData.selecData === undefined) {
        } else {
          this.fileId = this.$refs.fileData.selecData.fileId;
          // this.step.file.name = this.$refs.fileData.selecData.fileName;
          // this.step.fileId = this.$refs.fileData.selecData.fileId;
          // this.step.filename = this.$refs.fileData.selecData.fileName;
          // this.step.filePath = this.$refs.fileData.selecData.fileRelativPath;
          this.step.file_server_type = this.$refs.fileData.selecData.fileServerType;
          this.step.ftp_username = this.$refs.fileData.selecData.userName;
          this.step.ftp_password = this.$refs.fileData.selecData.password;
        }
        this.selectFileVisiable = false;
      },

      /**
       * 初始化选择的文件列表
       */
      initFileSelect() {
        for (let i = 0; i < this.step.file.name.length; i++) {
          this.fileSelect.push({
            name: this.step.file.name[i],
            fileName: this.step.file.fileName[i],
            filemask: this.step.file.filemask[i],
            exclude_filemask: this.step.file.exclude_filemask[i],
            file_required: this.step.file.file_required[i],
            include_subfolders: this.step.file.include_subfolders[i],
          })
        }
      },

      getFileByFileType(fileId) {
        let queryParams = {
          fileType: "txt,csv",
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
                  fileServerType: item.fileServerType,
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
                  fileRelativPath: item.fileRelativPath,
                  fileServerType: item.fileServerType,
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

      changeStep() {
        this.step.fileName = [];
        this.step.file.name = [];
        this.step.file.filemask = [];
        this.step.file.exclude_filemask = [];
        this.step.file.file_required = [];
        this.step.file.include_subfolders = [];
        for (let key in this.step) {
          if (typeof this.step[key] === 'boolean') {
            this.step[key] = this.step[key] === true ? 'Y' : "N"
          }
        }
        this.fileSelect.forEach(item => {
          this.step.file.fileName.push(item.fileName);
          this.step.file.name.push(item.name);
          this.step.file.filemask.push(item.filemask);
          this.step.file.exclude_filemask.push(item.exclude_filemask);
          this.step.file.file_required.push(item.file_required);
          this.step.file.include_subfolders.push(item.include_subfolders);
        })
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
        getTxtCsvHeaderColumn(params).then(res => {
          let {
            data
          } = res;
          if (data.code === '10000') {
            this.total = data.content.length;
            // this.csvinputData = data.content;
            this.step.fields.field = [];
            for (let i = 0; i < data.content.length; i++) {
              let fileLength = -1;
              let precision = -1;
              if (data.content[i].type.toUpperCase() === (('number').toUpperCase())) {
                fileLength = 38;
                precision = 0;
              } else if (data.content[i].type.toUpperCase() === (('string').toUpperCase())) {
                fileLength = 255;
                precision = -1;
              } else {
                fileLength = -1;
                precision = -1;
              }
              this.step.fields.field.push({
                edit:false,
                name: data.content[i].name,
                nameCn: data.content[i].name,
                type: data.content[i].type,
                length: data.content[i].len,
                precision: data.content[i].precision,
                trim_type: "none",
                repeat: "N",
                format: data.content[i].format,
                currency: data.content[i].dollarSign,
                decimal: data.content[i].pointSymbol,
                group: "",
              })
            }
            this.tableLoading = false;
            this.selectedPage();
            this.reverStep();
          } else {
            this.$message({
              message: '获取头部信息失败',
              type: 'error'
            })
            this.tableLoading = false;
          }
        })
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      selectedPage() {
        this.showFields = [];
        let fields = this.step.fields.field.slice((this.curPage - 1) * this.pageSize, this.curPage * this.pageSize);
        fields.forEach((item, index) => {
          this.showFields.push(item);
        });
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
          fileType:'txt'
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
        this.getConstant();
        this.getAllFolder();
        this.step.name = this.nodeData.label;
        this.oldStepName = this.nodeData.label;
        let stepName = this.step.name;
        // 获取当前步骤信息参数
        let param = {
          key: this.key, //用于标记画布，这里用的是画布路径
          value: stepName,
        };
        let curStep = store.getters.getCurrentStep(param);
        console.info("-=-=-=s",curStep);
        if (curStep !== undefined && curStep.fields !== undefined && curStep.filters !== undefined &&curStep.fields.field.length>0) {
          this.step.name = curStep.name;
          this.step.showFileName = curStep.showFileName;
          if(curStep.file_name!==''&&curStep.file_name!==undefined){
            this.step.file_name = curStep.file_name;
            this.getFileList();
          }
          this.step.file_server_type = curStep.file_server_type;
          this.step.ftp_username = curStep.ftp_username;
          this.step.ftp_password = curStep.ftp_password;
          this.step.description = curStep.description;
          this.step.distribute = curStep.distribute;
          this.step.accept_filenames = curStep.accept_filenames === 'Y';
          this.step.passing_through_fields = curStep.passing_through_fields === 'Y';
          this.step.accept_field = curStep.accept_field;
          this.step.accept_stepname = curStep.accept_stepname;
          this.step.separator = curStep.separator;
          this.step.enclosure = curStep.enclosure;
          this.step.enclosure_breaks = curStep.enclosure_breaks === 'Y';
          this.step.escapechar = curStep.escapechar;
          this.step.header = curStep.header === 'Y';
          this.step.footer = curStep.footer;
          this.step.nr_footerlines = curStep.nr_footerlines;
          this.step.line_wrapped = curStep.line_wrapped === 'Y';
          this.step.nr_wraps = curStep.nr_wraps;
          this.step.layout_paged = curStep.layout_paged === 'Y';
          this.step.nr_lines_per_page = curStep.nr_lines_per_page;
          this.step.nr_lines_doc_header = curStep.nr_lines_doc_header;
          this.step.noempty = curStep.noempty === 'Y';
          this.step.include = curStep.include === 'Y';
          this.step.include_field = curStep.include_field;
          this.step.rownum = curStep.rownum === 'Y';
          this.step.rownumByFile = curStep.rownumByFile === 'Y';
          this.step.rownum_field = curStep.rownum_field;
          this.step.format = curStep.format;
          this.step.encoding = curStep.encoding;
          this.step.length = curStep.length;
          this.step.file = curStep.file;

          this.initFileSelect();
          let fields = JSON.parse(JSON.stringify(curStep.fields.field));
          fields.forEach(item => {
            item.edit = false;
            this.step.fields.field.push(item);
          })
          this.selectedPage();
          let filter = curStep.filters.filter;
          filter.forEach(item => {
            this.step.filters.filter.push(item);
          });
          this.step.limit = curStep.limit;
          this.step.error_ignored = curStep.error_ignored === 'Y';
          this.step.skip_bad_files = curStep.skip_bad_files === 'Y';
          this.step.file_error_field = curStep.file_error_field;
          this.step.file_error_message_field = curStep.file_error_message_field;
          this.step.error_line_skipped = curStep.error_line_skipped === 'Y';
          this.step.error_count_field = curStep.error_count_field;
          this.step.error_fields_field = curStep.error_fields_field;
          this.step.error_text_field = curStep.error_text_field;
          this.step.bad_line_files_destination_directory = curStep.bad_line_files_destination_directory;
          this.step.bad_line_files_extension = curStep.bad_line_files_extension;
          this.step.error_line_files_destination_directory = curStep.error_line_files_destination_directory;
          this.step.error_line_files_extension = curStep.error_line_files_extension;
          this.step.line_number_files_destination_directory = curStep.line_number_files_destination_directory;
          this.step.line_number_files_extension = curStep.line_number_files_extension;
          this.step.date_format_lenient = curStep.date_format_lenient === 'Y';
          this.step.date_format_locale = curStep.date_format_locale;
          this.step.shortFileFieldName = curStep.shortFileFieldName;
          this.step.pathFieldName = curStep.pathFieldName;
          this.step.hiddenFieldName = curStep.hiddenFieldName;
          this.step.lastModificationTimeFieldName = curStep.lastModificationTimeFieldName;
          this.step.uriNameFieldName = curStep.uriNameFieldName;
          this.step.rootUriNameFieldName = curStep.rootUriNameFieldName;
          this.step.extensionFieldName = curStep.extensionFieldName;
          this.step.sizeFieldName = curStep.sizeFieldName;
        }
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
