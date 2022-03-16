<!--
 * @Author: your name
 * @Date: 2020-07-10 17:33:26
-->
<template>
	<div class="plugin_body">
		<div class="div_form plugin_content">
			<el-form :model="step" ref="step" label-width="30%" size="mini" :rules="rules">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 65%;"></el-input>
				</el-form-item>
				<el-form-item label="限制" prop="limit">
					<el-input v-model="step.limit" style="width: 65%;"></el-input>
				</el-form-item>
				<el-form-item label="从不停止生成行" prop="tranformation">
					<el-checkbox v-model="step.never_ending"></el-checkbox>
				</el-form-item>
				<el-form-item label="间隔毫秒" prop="name">
					<el-input v-model="step.interval_in_ms" :disabled="!step.never_ending" style="width: 65%;" value="10"></el-input>
				</el-form-item>
				<el-form-item label="当前时间字段名" prop="name">
					<el-input v-model="step.row_time_field" style="width: 65%;" :disabled="!step.never_ending" value="10"></el-input>
				</el-form-item>
				<el-form-item label="前一行时间字段名" prop="name">
					<el-input v-model="step.last_time_field" style="width: 65%;" :disabled="!step.never_ending" value="10"></el-input>
				</el-form-item>
			</el-form>
			<div>
				<div style=" font-weight: bold; display:inline-block;font-size:15px;">条件：</div>
				<div style="display: inline-block; margin-bottom: 5px; margin-left: 80%;">
					<el-button type="primary" size="mini" @click="addRow">新增
					</el-button>
				</div>
			</div>
			<el-form :model="step" ref="step" label-width="180px" size="mini">
				<el-table :data="step.fields.field" border height="330" :header-cell-style="{background:'#eef1f6'}">
					<el-table-column type="index" label="#"></el-table-column>
					<el-table-column prop="name" label="名称" width="150">
						<template slot-scope="scope">
							<el-input v-model="scope.row.name" size="mini"></el-input>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="类型" width="120">
						<template slot-scope="scope">
							<el-select v-model="scope.row.type" size="mini" style="width: 100%;" @change="formatData(scope.row)">
								<el-option v-for="item in fieldTypes" :key="item" :label="item" :value="item">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="format" label="格式" width="120">
						<template slot-scope="scope">
							<el-select v-model="scope.row.format" size="mini" style="width: 100%;" clearable>
								<el-option v-for="item in formats" :key="item" :label="item" :value="item">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="length" label="长度">
						<template slot-scope="scope">
							<el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.length">
							</el-input>
						</template>
					</el-table-column>
					<el-table-column prop="precision" label="精确">
						<template slot-scope="scope">
							<el-input type="number" size="mini" style="width: 100%;" v-model="scope.row.precision">
							</el-input>
						</template>
					</el-table-column>
					<el-table-column prop="currency" label="当前的">
						<template slot-scope="scope">
							<el-input size="mini" style="width: 100%;" v-model="scope.row.currency">
							</el-input>
						</template>
					</el-table-column>
					<el-table-column prop="decimal" label="10进制的" width="120">
						<template slot-scope="scope">
							<el-input size="mini" style="width: 100%;" v-model="scope.row.decimal">
							</el-input>
						</template>
					</el-table-column>
					<el-table-column prop="group" label="组">
						<template slot-scope="scope">
							<el-input size="mini" style="width: 100%;" v-model="scope.row.group">
							</el-input>
						</template>
					</el-table-column>
					<el-table-column prop="nullif" label="值" width="180">
						<template slot-scope="scope">
							<el-input size="mini" style="width: 100%;" v-model="scope.row.nullif">
							</el-input>
						</template>
					</el-table-column>
					<el-table-column prop="set_empty_string" label="设为空串?" width="120">
						<template slot-scope="scope">
							<el-select v-model="scope.row.set_empty_string" size="mini" style="width: 100%;" clearable>
								<el-option v-for="item in set_empty_string" :key="item.key" :label="item.label" :value="item.key">
								</el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column label="操作" width="100" fixed="right">
						<template slot-scope="scope">
							<el-button size="mini" type="text" @click="deleteRow(scope.$index)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-form>
		</div>
		<div class="dialog-footer plugin_footer">
			<span slot="footer">
				<el-button @click="closeDialog" size="mini">取 消</el-button>
				<el-button size="mini" @click="line=true" type="primary" :disabled="step.fields.field.length ===0">预览记录</el-button>
				<el-button @click="submit('step')" size="mini" type="primary">确 定</el-button>
			</span>
		</div>

		<el-dialog class="dialog_temp other_dialog" :visible.sync="line" title="选择显示行数" width="30%" :append-to-body="true">
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
		<el-dialog :visible.sync="logVisiable" title="日志" :append-to-body="true" width="70%" v-alterELDialogMarginTop="{marginTop:'2vh'}"
		 :modal-append-to-body="true" class="abow_in_dialog">
			<Log :key="logKey" :logs="log"></Log>
		</el-dialog>

	</div>
</template>
<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store";
	import {
		executePreviewByFile,
		previewDataByFile
	} from "../../../../api/api.js";
	import PreViewData from "../../../common/PreViewData";
	import Log from "../../../flow/Log";
	import {
		spliceDataJson
	} from "../../../../common/common";

	export default {

		props: {
			initF: Boolean,
			tempKey: String,
			testValue: Array,
			scriptName: String,
      scriptId:String,
		},
		components: {
			PreViewData,
			Log
		},
		data() {
			return {
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
				inputStatus: false,
				pageSize: 10,
				curPage: 1,
				total: 0,
				key: '', //插件对应的画布
				nodeData: {},
				step: {
					name: "生成记录",
					type: "RowGenerator",
					distribute: "Y",
					copies: "1",
					partitioning: {
						"method": "none"
					},
					fields: {
						field: []
					},
					limit: 10,
					never_ending: "N",
					interval_in_ms: "5000",
					row_time_field: "now",
					last_time_field: "FiveSecondsAgo",
					remotesteps: {
						input: "",
						output: "",

					},
					GUI: {
						xloc: 200,
						yloc: 100,
						draw: "Y"
					}
				},
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						tigger: 'blur'
					}]
				},

				set_empty_string: [{
					key: 'Y',
					label: '是'
				}, {
					key: 'N',
					label: '否'
				}],
				fieldTypes: ["Number", "String", "Date", "Boolean", "Integer", "BigNumber", "Timestamp"], //字段类型
				formats: [], //字段格式
				formatNumber: [],
				formatDate: [],
				trimType: ["none", "left", "right", "both"],
			}
		},

		mounted() {
			if (this.initF) {
				this.key = this.tempKey;
				this.oldStepName = this.step.name;
				this.step.fields.field = this.testValue;
			} else {
				this.init();
			}


		},

		methods: {

			/**
			 * 显示日志
			 */
			showLog() {
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
			previewDatas(type) {
			  let name = this.step.name;
			  if(type==='pre'){
			    name = this.scriptName;
        }
				this.changeStep();
				let key;
				if(this.tempKey!==undefined){
				  key = this.tempKey;
        }else{
				  key = this.key;
        }
				let dataJson = spliceDataJson(this.key, this.step);
				dataJson.transformation.order.hop = [];
				let steps =[];
				dataJson.transformation.step.forEach((item, index) => {
					if (item.name === this.step.name || item.name === this.scriptName) {
					  steps.push(item);
					} else {
					}
				});
				dataJson.transformation.step = steps;

				if(this.scriptName!==undefined){
          dataJson.transformation.order.hop.push({
            to: this.scriptName,
            from: this.step.name,
            enabled: 'Y'
          });
        }
				let params = {
					previewSize: this.limit,
					previewStepName: name,
					projectFile: JSON.stringify(dataJson),
					projectId: 'sss',
					projectName: 'ddd'
				};
				this.previewLoading = true;
				previewDataByFile(params).then(res => {
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
					} else {
						this.log = data.msg;
						this.showLog();
					}
					this.reverStep();
					this.previewLoading = false;
				})
			},


			formatData(row) {
				if (row.type === 'Number') {
					this.formats = this.formatNumber;
					row.length = 38;
				} else if (row.type === 'Date') {
					this.formats = this.formatDate;
					row.length = -1;
				} else {
					row.length = -1;

				}
			},


			closeDialog() {
				util.$emit('closeDialog', 'close');
				// 初始化画3布数据
			},

			addRow(event) { //新增一行
				this.step.fields.field.push({
					name: '',
					type: '',
					format: '',
					length: -1,
					precision: -1,
					group: '',
					currency: '',
					set_empty_string: '',
					nullif: '',
					decimal: '',
				});
				this.curPage = parseInt(this.step.fields.field.length / this.pageSize + 1);
				//this.selectedPage();
			},

			deleteRow(index) { //移除一行
				// let Index = (this.curPage - 1) * this.pageSize + index
				this.step.fields.field.splice(index, 1); //删掉该行
				//this.selectedPage();
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

			init() {
				this.getConstant();
				this.step.name = this.nodeData.label;
				this.oldStepName = this.nodeData.label;
				let stepName = this.step.name;
				// 获取当前步骤信息参数
				let param = {
					key: this.key, //用于标记画布，这里用的是画布路径
					value: stepName
				};

				let laststeps = store.getters.getLastStep(param);
				if (laststeps !== undefined && laststeps.length > 0) {
					this.laststep = laststeps[0];
				}
				console.log(this.laststep);
				let obj = store.getters.getCurrentStep(param);
				let curstep = {};
				Object.assign(curstep, obj);
				//获取当前步骤信息
				let params = {
					key: this.key,
					value: stepName,
				};
				let curStep = store.getters.getCurrentStep(params);
				let outFields = store.getters.generateOutputFields(param);
				this.step['outFields'] = outFields;

				if (curStep) {
					this.step.name = curStep.name;
					this.step.never_ending = curStep.never_ending === 'Y';
					// this.step.limit = curStep.limit;
					this.step.last_time_field = curStep.last_time_field;

					this.step.interval_in_ms = curStep.interval_in_ms;
					this.step.row_time_field = curStep.row_time_field;
				}

				if (obj.initValid == undefined) {
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(this.laststep.fields.field));
						this.step.fields.field = JSON.parse(JSON.stringify(this.laststep.fields.field));
					}
				} else {
					this.step.fields.field = JSON.parse(JSON.stringify(obj.fields.field));
					console.log(JSON.stringify(obj));
					console.log(this.step.fields.field);
				}


				this.step.name = curstep.name;

			},


			submit() {

			  if(this.scriptName!==undefined){
			    this.previewDatas('pre');
        }else{
          this.step.initFlag = true;
          this.step.initValid = false;
          let step = Object.assign({}, this.step);
          if (this.step.never_ending) {
            step.never_ending = 'Y'
          } else {
            step.never_ending = 'N'
          }
          if (this.step.newline_possible) {
            step.newline_possible = 'Y';
          } else {
            step.newline_possible = 'N';
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
        }

			},

		}
	};
</script>
<style>
	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}
</style>
