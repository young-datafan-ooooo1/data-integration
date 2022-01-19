<template>
	<div class="plugin_body">
		<div class="div_form  plugin_content">
			<el-form :model="step" ref="step" label-width="20%" size="mini" :rules="rules">
				<el-form-item label="步骤名称" prop="name">
					<el-input v-model="step.name" style="width: 80%;"></el-input>
				</el-form-item>
				<el-form-item label="数据库名称">
					<el-select v-model="step.connection" style="width: 45%;">
						<el-option v-for="item in dataSources" :key="item.datasourceId" :label="item.dsName" :value="item.datasourceId">
						</el-option>
					</el-select>
					<el-button type="primary" size="mini" @click="openConfig('edit')" icon="el-icon-edit">编辑</el-button>
					<el-button type="primary" size="mini" @click="openConfig" icon="el-icon-plus">新建</el-button>
				</el-form-item>
				<el-form-item label="存储过程名称">
					<el-input v-model="step.procedure" style="width: 80%;"></el-input>
					<el-button type="primary" size="mini" @click="getProcedure">查找</el-button>
				</el-form-item>
				<el-form-item label="启用自动提交">
					<el-checkbox v-model="step.auto_commit"></el-checkbox>
				</el-form-item>
				<el-form-item label="返回值名称">
					<el-input v-model="step.result.name" style="width: 80%;"></el-input>
				</el-form-item>
				<el-form-item label="返回值类型">
					<el-select v-model="step.result.type" style="width: 80%;" filterable allow-create clearable>
						<el-option v-for="item in type" :key="item" :label="item" :value="item"></el-option>
					</el-select>
				</el-form-item>

				<div style="float: left;">参数：</div>
				<div>
					<el-button style=" float:right;margin-bottom: 5px;margin-right: 5px" type="primary" @click="addRow" size="mini">新增
					</el-button>
				</div>

				<el-table :data="step.lookup.arg" border height="330px" :header-cell-style="{background:'#eef1f6'}">
					<el-table-column type="index" label="#" width="50"></el-table-column>
					<el-table-column prop="name" label="名称">
						<template slot-scope="scope">
							<el-input v-if="scope.row.direction == 'OUT'" size="mini" v-model="scope.row.name" clearable @blur="blurInput(scope.row,scope.$index)"></el-input>
							<el-select v-model="scope.row.name" filterable allow-create clearable size="mini" v-else>
								<el-option v-for="item in tablename" :key="item.name" :label="item.name" :value="item.name"></el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="direction" label="方向">
						<template slot-scope="scope">
							<el-select v-model="scope.row.direction" size="mini">
								<el-option v-for="item in direction" :key="item" :label="item" :value="item"></el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="类型">
						<template slot-scope="scope">
							<el-select v-model="scope.row.type" size="mini">
								<el-option v-for="item in type" :key="item" :label="item" :value="item"></el-option>
							</el-select>
						</template>
					</el-table-column>
					<el-table-column label="操作">
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
				<el-button size="mini" @click="getDialog">获取字段</el-button>
				<el-button @click="submit('step')" size="mini">确 定</el-button>
			</span>
		</div>
		<!--    数据源滚管理-->
		<el-dialog :visible.sync="build" class="abow_dialog" width="70%" :title="dataSourceTitle" :append-to-body="true">
			<data-source-manage :key="dataSourcekey" ref="dataSourceView"></data-source-manage>
		</el-dialog>

		<!--    存储过程-->
		<el-dialog title="存储过程" :visible.sync="preduceVisiable" :before-close="handleClose" :modal-append-to-body="false"
		 class="abow_dialog" width="40%">
			<procedure :key="tableKey" :tree-data="tableViews" :loading="loading" :connection="selectConnect"></procedure>
		</el-dialog>
	</div>
</template>
<script>
	import util from "../../../../common/utils";
	import store from "../../../../vuex/store";
	import codeMirror from "../../../common/codemirror.vue";
	import sqlFormatter from "sql-formatter";
	import procedure from '../../../common/Procedure'
	import dataSourceManage from '../../../common/DataSourceManager';
	import {
		getSchema,
		getStatement,
		getProcedure
	} from "../../../../api/api.js";
	import {
		getConnection
	} from "../../../../api/api";
	import {
		addConnection,
		getChecktableName
	} from "../../../../common/common";

	export default {
		data() {
			return {
				preduceVisiable: false,
				tableKey: null,
				loading: false,
				selectConnect: '',
				tableViews: [],
				key: '', //插件对应的画布
				nodeData: {},
				dataSources: [],
				build: false,
				dataSourcekey: null,
				dataSourceTitle: '新增连接',
				tablename: [],
				direction: ["IN", "OUT", "INOUT"],
				step: {
					name: "调用DB存储过程",
					type: "DBProc",
					description: [],
					distribute: "Y",
					copies: "1",
					partitioning: {
						method: "none",
						schema_name: []
					},
					connection: "",
					procedure: "",
					lookup: {
						arg: [],
					},
					result: {
						name: "result",
						type: "Number",
					},
					fields: {
						field: []
					},
					auto_commit: true,
					attributes: [],
					cluster_schema: [],
					remotesteps: {
						input: "\n      ",
						output: "\n      "
					},
					GUI: {
						xloc: "160",
						yloc: "224",
						draw: "Y"
					}
				},
				type: ["BigNumber", "Binary", "Boolean", "Date", "Integer", "Internet Address", "Number", "String", "Timestamp"],
				rules: {
					name: [{
						required: true,
						message: '步骤名称不能为空',
						trigger: 'blur'
					}]
				},
				preOutfileds: []
			};
		},
		components: {
			dataSourceManage,
			procedure,
		},
		mounted() {
			this.init()
		},
		methods: {

			blurInput(row, index) {
				let args = JSON.parse(JSON.stringify(this.step.lookup.arg));
				args.splice(index, 1);
				getChecktableName(args, this.step.outFields, row, 'name');
				this.$message({message:'该字段名已存在，重命名为'+row.name,
								type:'warning'});
			},

			addRow(event) { //新增一行
				this.step.lookup.arg.push({
					name: '',
					type: '',
					direction: 'IN',
				});
				this.curPage = parseInt(this.step.lookup.arg.length / this.pageSize + 1);
				//this.selectedPage();
			},
			deleteRow(index) { //移除一行
				// let Index = (this.curPage - 1) * this.pageSize + index
				this.step.lookup.arg.splice(index, 1); //删掉该行
				//this.selectedPage();
			},
			/**
			 *编辑
			 **/
			openConfig(type) {
				this.dataSourcekey = new Date().getTime();
				this.build = true;
				//编辑
				if (type === 'edit') {
					this.build = true;
					this.dataSourceTitle = '编辑连接';
					this.$nextTick(() => {
						this.dataSources.forEach(item => {
							if (item.datasourceId === this.step.connection) {
								this.$refs.dataSourceView.dataSourceData = item;
								this.$refs.dataSourceView.init();
							}
						})
					})

				} else {
					this.build = true;
					this.dataSourceTitle = '新增连接';
				}

			},


			getDialog() {
				this.step.lookup.arg = [];
				this.lastStepFields = JSON.parse(JSON.stringify(this.step['outFields']));
				this.lastStepFields.forEach(item => {
					this.step.lookup.arg.push({
						name: item.name,
						type: item.type,
						direction: 'IN',
						repeat: item.repeat,
						decimal: item.decimal,
						group: item.group,
						length: item.length,
					})
				});
			},

			//获取存储过程信息
			getProcedure() {
				this.previewLoading = true;
				this.selectConnect = this.step.connection;
				let params = {
					databaseId: this.step.connection,
				};
				getProcedure(params).then(res => {
					if (res.code === '10000') {
						this.preduceVisiable = true;
						this.tempKey = new Date().getTime();
						this.tableViews = res.content;
					}
				})
			},
			handleClose(done) {
				done();
			},

			setProcedure(data) {
				this.step.procedure = data;
			},

			closeDialog() {
				util.$emit("closeDialog", "close");
				// 初始化画布数据
			},


			/**
			 * 获取数据库连接信息
			 * */
			getDataSources() {
				let params = {
					sourcePlatform: 'JCPLAT'
				};
				getConnection(params).then(res => {
					let {
						data
					} = res;
					if (data.code === '10000') {
						this.dataSources = data.content;
					}
				})
			},


			init() {
				this.getDataSources();
				console.log(this.step);
				this.step.name = this.nodeData.label;
				this.oldStepName = this.nodeData.label;
				let stepName = this.step.name;
				//获取当前步骤信息
				let params = {
					key: this.key,
					value: stepName,
				};
				let laststeps = store.getters.getLastStep(params);
				if (laststeps !== undefined && laststeps.length > 0) {
					this.laststep = laststeps[0];
					console.log(this.laststep);
				}

				//生成本步骤输出字段
				let outFields = store.getters.generateOutputFields(params);
				this.step['outFields'] = outFields;
				this.preOutfileds = outFields;
				console.log(outFields);


				let obj = store.getters.getCurrentStep(params);
				let curStep = {};
				Object.assign(curStep, obj);

				console.log(curStep);
				if (obj.initValid == undefined) {
					if (this.laststep !== undefined) {
						this.lastStepFields = JSON.parse(JSON.stringify(outFields));
						this.lastStepFields.forEach(item => {
							this.tablename.push({
								name: item.name,
								type: item.type,
							})
						});
					}
				} else {
					this.step.lookup.arg = JSON.parse(JSON.stringify(obj.lookup.arg));
					this.lastStepFields = JSON.parse(JSON.stringify(outFields));
					this.lastStepFields.forEach(item => {
						this.tablename.push({
							name: item.name,
							type: item.type,
						})
					});
					this.step = obj;
				}

				if(curStep !== undefined){
					this.step.name = curStep.name;
					this.step.initFlag = curStep.initFlag;
					this.step.description = curStep.description;
					this.step.distribute = curStep.distribute;
					this.step.copies = curStep.copies;
					this.step.partitioning = curStep.partitioning;
					this.step.model_name = curStep.model_name;
					this.step.remotesteps = curStep.remotesteps;
					this.step.GUI = curStep.GUI;
					this.step.connection = curStep.connection;
					this.step.procedure = curStep.procedure;
					this.step.auto_commit = (curStep.auto_commit == 'Y' || curStep.auto_commit == undefined);
				}
				for (let key in this.step) {
					if (this.step[key] == 'Y') {
						this.step[key] = true;
					} else if (this.step[key] == 'N') {
						this.step[key] = false;
					}
				}
			},

			submit() {
				this.step.initFlag = true;
				this.step.initValid = false;
				let step = Object.assign({}, this.step);
				if (this.step.auto_commit == true) {
					step.auto_commit = "Y";
				} else {
					step.auto_commit = "N";
				}

				step["oldStepName"] = this.oldStepName;
				step['outFields'] = this.preOutfileds;
				//增加OUT字段
				this.step.lookup.arg.forEach(item => {
					if (item.direction == 'OUT') {
						step.outFields.push({
							name: item.name,
							type: item.type,
							repeat: item.repeat,
							decimal: item.decimal,
							group: item.group,
							length: item.length,
						})
					}
				});
				step.outFields.push({
					name: this.step.result.name,
					type: this.step.result.type,
				});
				console.log(step.outFields);
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
						step: step
					}
				};
				store.dispatch("updateStepInfo", param);
				let params = {
					id: this.nodeData.id, //插件id
					label: step.name,
					oldName: this.oldStepName
				};
				addConnection(this.dataSources, this.step.connection, this.key);
				util.$emit("updateNode", params);
				util.$emit("closeDialog", "close");
				if (flag) {
					this.$alert(paramName + "名称已存在,重命名为" + newName + "!", "注释", {
						confirmButtonText: "确定"
					});
				} else {
					this.$message({
						message: "步骤信息修改成功",
						type: "success"
					});
				}
			}
		}
	};
</script>
<style>
	.el-form-item--mini.el-form-item {
		margin-bottom: 10px;
	}
</style>
